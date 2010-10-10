#import "NamNamXMLParser.h"
#import "Mensa.h"
#import "Mensaessen.h"
#import "Tagesmenue.h"

@implementation NamNamXMLParser

@synthesize delegate, parsedMensa, url, dateFormatter, numberFormatter, xmlData, done, storingCharacters, connection, currentString, currentMensaessen, currentTagesmenue, parseErrorOccurred, downloadAndParsePool;

- (void)start {
    [[NSURLCache sharedURLCache] removeAllCachedResponses];
    self.parsedMensa = nil;
    NSURL *theurl = [NSURL URLWithString:self.url];
    [NSThread detachNewThreadSelector:@selector(downloadAndParse:) toTarget:self withObject:theurl];
}

- (void)dealloc {
    [parsedMensa release];
    [super dealloc];
}

- (void)downloadAndParse:(NSURL *)theurl {
	self.downloadAndParsePool = [[NSAutoreleasePool alloc] init];
	done = NO;
    
	NSTimeZone* timeZone = [NSTimeZone timeZoneWithName:@"GMT"]; // otherwise the local timezone will be applied to the parsed date-only-dates
	
	self.dateFormatter = [[[NSDateFormatter alloc] init] autorelease];
	[dateFormatter setTimeZone:timeZone];
    [dateFormatter setDateStyle:NSDateFormatterLongStyle];
    [dateFormatter setTimeStyle:NSDateFormatterNoStyle];
    [dateFormatter setDateFormat:@"yyyy-MM-dd"];
	
	self.numberFormatter = [[[NSNumberFormatter alloc] init] autorelease];
    [numberFormatter setNumberStyle:NSNumberFormatterDecimalStyle];
	
    self.xmlData = [NSMutableData data];
    [[NSURLCache sharedURLCache] removeAllCachedResponses];
    NSURLRequest *theRequest = [NSURLRequest requestWithURL:theurl];

    // create the connection with the request and start loading the data
    connection = [[NSURLConnection alloc] initWithRequest:theRequest delegate:self];
    [self performSelectorOnMainThread:@selector(downloadStarted) withObject:nil waitUntilDone:NO];
    if (connection != nil) {
        do {
            [[NSRunLoop currentRunLoop] runMode:NSDefaultRunLoopMode beforeDate:[NSDate distantFuture]];
        } while (!done);
    }
    self.connection = nil;
    self.dateFormatter = nil;
	self.numberFormatter = nil;
	
	[downloadAndParsePool release];
	self.downloadAndParsePool = nil;
}


- (void)downloadStarted {
    NSAssert2([NSThread isMainThread], @"%s at line %d called on secondary thread", __FUNCTION__, __LINE__);
    [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
}

- (void)downloadEnded {
    NSAssert2([NSThread isMainThread], @"%s at line %d called on secondary thread", __FUNCTION__, __LINE__);
    [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
}

- (void)parseEnded:(Mensa*) mensa {
    NSAssert2([NSThread isMainThread], @"%s at line %d called on secondary thread", __FUNCTION__, __LINE__);
	self.parsedMensa = mensa;
    if (self.delegate != nil && [self.delegate respondsToSelector:@selector(parserDidEndParsingData:)]) {
        [self.delegate parserDidEndParsingData:self];
    }
}

- (void)parseError:(NSError *)error {
    NSAssert2([NSThread isMainThread], @"%s at line %d called on secondary thread", __FUNCTION__, __LINE__);
    if (self.delegate != nil && [self.delegate respondsToSelector:@selector(parser:didFailWithError:)]) {
		self.parseErrorOccurred = YES;
        [self.delegate parser:self didFailWithError:error];
    }
}

#pragma mark NSURLConnection Delegate methods

/*
 Disable caching so that each time we run this app we are starting with a clean slate. You may not want to do this in your application.
 */
- (NSCachedURLResponse *)connection:(NSURLConnection *)connection willCacheResponse:(NSCachedURLResponse *)cachedResponse {
    return nil;
}

// Forward errors to the delegate.
- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error {
    done = YES;
    [self performSelectorOnMainThread:@selector(parseError:) withObject:error waitUntilDone:YES];
}

// Called when a chunk of data has been downloaded.
- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data {
    // Append the downloaded chunk of data.
    [xmlData appendData:data];
}

- (void)connectionDidFinishLoading:(NSURLConnection *)connection {
    [self performSelectorOnMainThread:@selector(downloadEnded) withObject:nil waitUntilDone:NO];

	NSXMLParser *parser = [[NSXMLParser alloc] initWithData:xmlData];
    parser.delegate = self;
    self.currentString = [NSMutableString string];
    [parser parse];
	if(self.parseErrorOccurred) {
		NSLog(@"parse error already communicated to delegate");
	} else {
		NSLog(@"essen fuer %d tage",[self.parsedMensa.dayMenues count]);
		[self performSelectorOnMainThread:@selector(parseEnded:) withObject:self.parsedMensa waitUntilDone:NO];
	}
	[parser release];
    self.currentString = nil;
    self.xmlData = nil;
	self.currentMensaessen = nil;
	self.currentTagesmenue = nil;
	self.parseErrorOccurred = NO;
    // Set the condition which ends the run loop.
    done = YES; 
}

#pragma mark Parsing support methods

static const NSUInteger kAutoreleasePoolPurgeFrequency = 1;

/*
- (void)finishedCurrentMensa {
    [self performSelectorOnMainThread:@selector(parsedMensa:) withObject:currentMensa waitUntilDone:NO];
    // performSelectorOnMainThread: will retain the object until the selector has been performed
    // setting the local reference to nil ensures that the local reference will be released
    self.currentMensa = nil;
} */

#pragma mark NSXMLParser Parsing Callbacks

// Constants for the XML element names that will be considered during the parse. 
// Declaring these as static constants reduces the number of objects created during the run
// and is less prone to programmer error.
static NSString *kName_Mensa = @"Mensa";
static NSString *kName_firstDate = @"firstDate";
static NSString *kName_lastDate = @"lastDate";
static NSString *kName_Tagesmenue = @"Tagesmenue";
static NSString *kName_tag = @"tag";
static NSString *kName_Mensaessen = @"Mensaessen";
static NSString *kName_beschreibung = @"beschreibung";
static NSString *kName_studentenPreis = @"studentenPreis";
static NSString *kName_normalerPreis = @"normalerPreis";

static NSString *kName_nameAttr = @"name";
static NSString *kName_moslemAttr = @"moslem";
static NSString *kName_rindAttr = @"rind";
static NSString *kName_vegetarischAttr = @"vegetarisch";


- (void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *) qualifiedName attributes:(NSDictionary *)attributeDict {
    if ([elementName isEqualToString:kName_Mensa]) {
        self.parsedMensa = [[[Mensa alloc] init] autorelease];
		self.parsedMensa.name = [attributeDict objectForKey:kName_nameAttr];
	} else if ([elementName isEqualToString:kName_Tagesmenue]) {
		self.currentTagesmenue = [[[Tagesmenue alloc] init] autorelease];
	} else if ([elementName isEqualToString:kName_Mensaessen]) {
		self.currentMensaessen = [[[Mensaessen alloc] init] autorelease];
		self.currentMensaessen.vegetarian = [[attributeDict objectForKey:kName_vegetarischAttr] isEqualToString:@"true"];
		self.currentMensaessen.beef = [[attributeDict objectForKey:kName_rindAttr] isEqualToString:@"true"];
		self.currentMensaessen.moslem = [[attributeDict objectForKey:kName_moslemAttr] isEqualToString:@"true"];
    } else if ([elementName isEqualToString:kName_firstDate] || [elementName isEqualToString:kName_lastDate] || [elementName isEqualToString:kName_tag] ||
			   [elementName isEqualToString:kName_beschreibung]|| [elementName isEqualToString:kName_studentenPreis]|| [elementName isEqualToString:kName_normalerPreis]  ) {
        [currentString setString:@""];
        storingCharacters = YES;
    }
}

- (void)parser:(NSXMLParser *)parser didEndElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName {
    if ([elementName isEqualToString:kName_firstDate]) {
        parsedMensa.firstDate = [dateFormatter dateFromString:currentString];
    } else if ([elementName isEqualToString:kName_lastDate]) {
        parsedMensa.lastDate = [dateFormatter dateFromString:currentString];
	} else if ([elementName isEqualToString:kName_tag]) {
        currentTagesmenue.tag = [dateFormatter dateFromString:currentString];
    } else if ([elementName isEqualToString:kName_beschreibung]) {
        currentMensaessen.beschreibung = [[NSString alloc] initWithString:currentString];
    } else if ([elementName isEqualToString:kName_studentenPreis]) {
        currentMensaessen.studentenPreis = [[numberFormatter numberFromString:currentString] intValue];
    } else if ([elementName isEqualToString:kName_normalerPreis]) {
        currentMensaessen.preis = [[numberFormatter numberFromString:currentString] intValue];
		
		// start adding up
    } else if ([elementName isEqualToString:kName_Mensaessen]) {
        [currentTagesmenue.menues addObject:currentMensaessen];
    } else if ([elementName isEqualToString:kName_Tagesmenue]) {
        [parsedMensa.dayMenues addObject:currentTagesmenue];
	}
	
    storingCharacters = NO;
}

- (void)parser:(NSXMLParser *)parser foundCharacters:(NSString *)string {
    if (storingCharacters) [currentString appendString:string];
}

- (void)parser:(NSXMLParser *)parser parseErrorOccurred:(NSError *)parseError {
	[self performSelectorOnMainThread:@selector(parseError:) withObject:parseError waitUntilDone:YES];
}



@end
