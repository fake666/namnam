#import "NamNamXMLParser.h"
#import "Mensa.h"
#import "MensaURL.h"
#import "Mensaessen.h"
#import "Tagesmenue.h"
#import "ModelLocator.h"

@implementation NamNamXMLParser

@synthesize delegate, parsedMensa, dateFormatter, numberFormatter, xmlData, done, storingCharacters, connection, currentString, currentMensaessen, currentTagesmenue, parseErrorOccurred, currentMenues, currentDayMenues, model;

- (void)start {
	
	model = [ModelLocator sharedInstance];
	
    [[NSURLCache sharedURLCache] removeAllCachedResponses];
    self.parsedMensa = nil;
    NSURL *theurl = [NSURL URLWithString:model.mensaURL.url];
    [NSThread detachNewThreadSelector:@selector(downloadAndParse:) toTarget:self withObject:theurl];
}


- (void)downloadAndParse:(NSURL *)theurl {
	done = NO;
    
	NSTimeZone* timeZone = [NSTimeZone timeZoneWithName:@"GMT"]; // otherwise the local timezone will be applied to the parsed date-only-dates
	
	self.dateFormatter = [[NSDateFormatter alloc] init];
	[dateFormatter setTimeZone:timeZone];
    [dateFormatter setDateStyle:NSDateFormatterLongStyle];
    [dateFormatter setTimeStyle:NSDateFormatterNoStyle];
    [dateFormatter setDateFormat:@"yyyy-MM-dd"];
	
	self.numberFormatter = [[NSNumberFormatter alloc] init];
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
    //[connection release];
	self.connection = nil;
	self.dateFormatter = nil;
	self.numberFormatter = nil;
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
	self.parsedMensa.lastUpdate = [[NSDate alloc] init];
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
	
	self.currentDayMenues = [[NSMutableArray alloc] initWithCapacity:20];
	self.currentMenues = [[NSMutableArray alloc] initWithCapacity:3];
	
    [parser parse];
	if(!self.parseErrorOccurred) {
		[self performSelectorOnMainThread:@selector(parseEnded:) withObject:self.parsedMensa waitUntilDone:NO];
	}
    self.currentString = nil;
    self.xmlData = nil;
	self.currentMensaessen = nil;
	self.currentTagesmenue = nil;
	self.currentDayMenues = nil;
	self.currentMenues = nil;
	self.parseErrorOccurred = NO;
    // Set the condition which ends the run loop.
    done = YES; 
}

#pragma mark NSXMLParser Parsing Callbacks

// Constants for the XML element names that will be considered during the parse. 
// Declaring these as static constants reduces the number of objects created during the run
// and is less prone to programmer error.
static NSString *kName_Mensa = @"Mensa";
static NSString *kName_Tagesmenue = @"Tagesmenue";
static NSString *kName_Mensaessen = @"Mensaessen";

static NSString *kName_firstDate = @"firstDate";
static NSString *kName_lastDate = @"lastDate";
static NSString *kName_tag = @"tag";
static NSString *kName_nameAttr = @"name";
static NSString *kName_beschreibung = @"beschreibung";
static NSString *kName_token = @"token";
static NSString *kName_studentenPreis = @"studentenPreis";
static NSString *kName_normalerPreis = @"normalerPreis";


- (void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *) qualifiedName attributes:(NSDictionary *)attributeDict {
    if ([elementName isEqualToString:kName_Mensa]) {
        self.parsedMensa = [[Mensa alloc] init];
		self.parsedMensa.name = [attributeDict objectForKey:kName_nameAttr];
	} else if ([elementName isEqualToString:kName_Tagesmenue]) {
		self.currentTagesmenue = [[Tagesmenue alloc] init];
	} else if ([elementName isEqualToString:kName_Mensaessen]) {
		self.currentMensaessen = [[Mensaessen alloc] init];
    } else if ([elementName isEqualToString:kName_firstDate] || [elementName isEqualToString:kName_lastDate] || [elementName isEqualToString:kName_tag] ||
			   [elementName isEqualToString:kName_beschreibung] || [elementName isEqualToString:kName_studentenPreis] || [elementName isEqualToString:kName_normalerPreis] || [elementName isEqualToString:kName_token]) {
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
    } else if ([elementName isEqualToString:kName_token]) {
        if([currentString isEqualToString:@"Vegetarisch"]) {
            self.currentMensaessen.vegetarian = YES;
        } else if([currentString isEqualToString:@"Vegan"]) {
            self.currentMensaessen.vegan = YES;
        } else if([currentString isEqualToString:@"Rind"]) {
            self.currentMensaessen.beef = YES;
        } else if([currentString isEqualToString:@"Schwein"]) {
            self.currentMensaessen.pork = YES;
        } else if([currentString isEqualToString:@"Gefluegel"]) {
            self.currentMensaessen.poultry = YES;
        } else if([currentString isEqualToString:@"Fisch"]) {
            self.currentMensaessen.fish = YES;
        }
    } else if ([elementName isEqualToString:kName_Mensaessen]) {
		[currentMenues addObject:currentMensaessen];
    } else if ([elementName isEqualToString:kName_Tagesmenue]) {
		currentTagesmenue.menues = [NSArray arrayWithArray:currentMenues];
		[currentDayMenues addObject:currentTagesmenue];
		[currentMenues removeAllObjects];
    } else if ([elementName isEqualToString:kName_Mensa]) {
		parsedMensa.dayMenues = [NSArray arrayWithArray:currentDayMenues];
		[currentDayMenues removeAllObjects];
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
