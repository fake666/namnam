//
//  ModelLocator.m
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-10-10.
//  Copyright 2010 Thomas "fake" Jakobi. All rights reserved.
//

#import "ModelLocator.h"
#import "MensaURL.h"
#import "Mensa.h"
#import "Tagesmenue.h"

static ModelLocator *sharedInstance = nil;

@implementation ModelLocator

#pragma mark -
#pragma mark class instance methods

@synthesize appWasInBackGround, mensaURL, mensa, mensen, priceDisplayType, activity, parser, delegate, niceDateFormatter;

NSInteger mensaNameSort(MensaURL *mensa1, MensaURL *mensa2, void* ignored) {
	return [[mensa1 name] localizedCaseInsensitiveCompare:[mensa2 name]];
}

- (id) init {
	self = [super init];
	niceDateFormatter = [[NSDateFormatter alloc] init];
	[niceDateFormatter setDateStyle:NSDateFormatterLongStyle];
	[niceDateFormatter setTimeStyle:NSDateFormatterNoStyle];
	[niceDateFormatter setLocale:[[NSLocale alloc] initWithLocaleIdentifier:@"DE"]];
	return self;
}	

- (void) loadSettings {
	[self loadMensae];
	
	// now load the current settings and select the corresponding mensa
	NSString *errorDesc = nil;
	NSPropertyListFormat format;
	NSString *plistPath;
	NSString *rootPath = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,
															  NSUserDomainMask, YES) objectAtIndex:0];
	plistPath = [rootPath stringByAppendingPathComponent:@"NamNamSettings.plist"];
	if (![[NSFileManager defaultManager] fileExistsAtPath:plistPath]) {
		plistPath = [[NSBundle mainBundle] pathForResource:@"NamNamSettings" ofType:@"plist"];
	}
	NSData *plistXML = [[NSFileManager defaultManager] contentsAtPath:plistPath];
	NSDictionary *temp = (NSDictionary *)[NSPropertyListSerialization
										  propertyListFromData:plistXML
										  mutabilityOption:NSPropertyListMutableContainersAndLeaves
										  format:&format
										  errorDescription:&errorDesc];
	if (!temp) {
		NSLog(@"Error reading plist: %@, format: %d", errorDesc, format);
	}
	
	NSDictionary* selectedMensa = [temp objectForKey:@"SelectedMensa"];
	NSString* priceDisplaySelectionString = [temp objectForKey:@"ShowPrice"];
	
	if([priceDisplaySelectionString isEqualToString:@"STUDENT"]) {
		self.priceDisplayType = PRICE_DISPLAY_STUDENT;
	} else if([priceDisplaySelectionString isEqualToString:@"NORMAL"]) {
		self.priceDisplayType = PRICE_DISPLAY_NORMAL;
	} else {
		self.priceDisplayType = PRICE_DISPLAY_BOTH;
	}
	
	NSString* selMensaName = [selectedMensa objectForKey:@"name"];
	[delegate mensaNameKnown:selMensaName];
	
	for(int n = 0; n < self.mensen.count; n++) {
		MensaURL* cur = [self.mensen objectAtIndex:n];
		if([cur.name isEqualToString:selMensaName]) {
			mensaURL = cur;
			break;
		}
	}
}

- (void)loadMensae {
	NSString *errorDesc = nil;
	NSPropertyListFormat format;
	NSString *plistPath;
	NSString *rootPath = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,
															  NSUserDomainMask, YES) objectAtIndex:0];
	plistPath = [rootPath stringByAppendingPathComponent:@"NamNamURLs.plist"];
	if (![[NSFileManager defaultManager] fileExistsAtPath:plistPath]) {
		plistPath = [[NSBundle mainBundle] pathForResource:@"NamNamURLs" ofType:@"plist"];
	}
	NSData *plistXML = [[NSFileManager defaultManager] contentsAtPath:plistPath];
	NSDictionary *temp = (NSDictionary *)[NSPropertyListSerialization
										  propertyListFromData:plistXML
										  mutabilityOption:NSPropertyListMutableContainersAndLeaves
										  format:&format
										  errorDescription:&errorDesc];
	if (!temp) {
		NSLog(@"Error reading plist: %@, format: %d", errorDesc, format);
	}
	
	NSDictionary* input = [temp objectForKey:@"Mensen"];
	NSMutableArray* parsed = [[NSMutableArray alloc] init];
	
	NSEnumerator *enumerator = [input keyEnumerator];
	id key;
	
	while ((key = [enumerator nextObject])) {
		MensaURL* mensaurl = [[MensaURL alloc] init];
		mensaurl.url = [input objectForKey:key];
		mensaurl.name = key;
		[parsed addObject:mensaurl];
	}
	
	[self setMensen:[parsed sortedArrayUsingFunction:mensaNameSort context:nil]];
}

- (void)saveSettings {
	NSString *error;
	NSString *rootPath = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
	NSString *plistPath = [rootPath stringByAppendingPathComponent:@"NamNamSettings.plist"];
	NSMutableDictionary *plistDict = [NSMutableDictionary dictionaryWithCapacity:2];
	
	NSMutableDictionary *mensaSelDict = [NSMutableDictionary dictionaryWithCapacity:2];
	[mensaSelDict setValue:mensaURL.name forKey:@"name"];
	[mensaSelDict setValue:mensaURL.url forKey:@"url"];
	
	[plistDict setValue:mensaSelDict forKey:@"SelectedMensa"];
	
	NSString* priceSelectionString;
	if(self.priceDisplayType == PRICE_DISPLAY_STUDENT) {
		priceSelectionString = @"STUDENT";
	} else if(self.priceDisplayType == PRICE_DISPLAY_NORMAL) {
		priceSelectionString = @"NORMAL";
	} else {
		priceSelectionString = @"BOTH";
	}
	[plistDict setValue:priceSelectionString forKey:@"ShowPrice"];
	
	NSData *plistData = [NSPropertyListSerialization dataFromPropertyList:plistDict
																   format:NSPropertyListBinaryFormat_v1_0
														 errorDescription:&error];
	if(plistData) {
		[plistData writeToFile:plistPath atomically:YES];
	} else {
		NSLog(@"error saving settings: %@",error);
	}	
}

- (void)saveData {
	NSString *error;
	NSString *rootPath = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
	NSString *plistPath = [rootPath stringByAppendingPathComponent:@"NamNamData.plist"];
	NSMutableDictionary *plistDict = [NSMutableDictionary dictionaryWithCapacity:1];
	
	[plistDict setValue:[mensa serialize] forKey:@"mensa"];
	
	NSData *plistData = [NSPropertyListSerialization dataFromPropertyList:plistDict
																   format:NSPropertyListBinaryFormat_v1_0
														 errorDescription:&error];
	if(plistData) {
		[plistData writeToFile:plistPath atomically:YES];
	} else {
		NSLog(@"error saving data: %@",error);
	}
}

- (void)loadData{
	// now load the current settings and select the corresponding mensa
	NSString *errorDesc = nil;
	NSPropertyListFormat format;
	NSString *plistPath;
	NSString *rootPath = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,
															  NSUserDomainMask, YES) objectAtIndex:0];
	plistPath = [rootPath stringByAppendingPathComponent:@"NamNamData.plist"];
	if (![[NSFileManager defaultManager] fileExistsAtPath:plistPath]) {
		plistPath = [[NSBundle mainBundle] pathForResource:@"NamNamData" ofType:@"plist"];
	}
	NSData *plistXML = [[NSFileManager defaultManager] contentsAtPath:plistPath];
	NSDictionary *temp = (NSDictionary *)[NSPropertyListSerialization
										  propertyListFromData:plistXML
										  mutabilityOption:NSPropertyListMutableContainersAndLeaves
										  format:&format
										  errorDescription:&errorDesc];
	if (!temp) {
		NSLog(@"Error reading plist: %@, format: %d", errorDesc, format);
	} else {		
		NSDictionary* dmensa = [temp objectForKey:@"mensa"];
		self.mensa = [Mensa deserialize:dmensa];
	}
}

- (Tagesmenue*) closestDayMenue {
	int nearestIdx = -1;
	long nearestEntryDistance = -1;
	for(int n = 0; n < self.mensa.dayMenues.count; n++) {
		Tagesmenue* cur = [self.mensa.dayMenues objectAtIndex:n];
		
		long dist = abs([cur.tag timeIntervalSinceNow]);
		if(nearestEntryDistance < 0 || dist < nearestEntryDistance) {
			nearestEntryDistance = dist;
			nearestIdx = n;
		}
	}
	if(nearestIdx >= 0) {
		return [self.mensa.dayMenues objectAtIndex:nearestIdx];
	} else {
		return nil;
	}

}

- (void)parserDidEndParsingData:(NamNamXMLParser *)theparser {
	// automatically releases the current value if set!
	self.mensa = theparser.parsedMensa;
	
	if(mensa.dayMenues.count <= 0) {
		[activity stopAnimating];
		UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Keine Einträge" 
														message:@"Kein Essen gefunden! :("
													   delegate:nil 
											  cancelButtonTitle:@"Einstellungen" 
											  otherButtonTitles: nil];
		[alert show];
		
		[delegate loadingFailed];
	} else {
		[self saveData];
		[activity stopAnimating];
		[delegate loadingFinished];
	}	
}

- (void)parser:(NamNamXMLParser *)parser didFailWithError:(NSError *)error {
	[activity stopAnimating];	
	
	UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Fehler" 
                                                    message:[error localizedDescription]
                                                   delegate:nil 
                                          cancelButtonTitle:@"Einstellungen" 
                                          otherButtonTitles: nil];
    [alert show];
	
	[delegate loadingFailed];
}

- (void)fetchMensaData {
	parser = [[NamNamXMLParser alloc] init];
	parser.delegate = self;
	[activity startAnimating];
	[parser start];
}

- (NSString*)getNiceDate:(NSDate *)date {
	// Initialize the calendar and flags.
	unsigned unitFlags = NSYearCalendarUnit | NSMonthCalendarUnit |  NSDayCalendarUnit | NSWeekdayCalendarUnit;
	NSCalendar *calendar = [NSCalendar currentCalendar];
	
	// Create reference date for supplied date.
	NSDateComponents *comps = [calendar components:unitFlags fromDate:date];
	[comps setHour:0];
	[comps setMinute:0];
	[comps setSecond:0];
	NSDate *suppliedDate = [calendar dateFromComponents:comps];
	
	// Iterate through the eight days (tomorrow, today, and the last six).
	int i;
	for (i = -6; i < 3; i++)
	{
		// Initialize reference date.
		comps = [calendar components:unitFlags fromDate:[NSDate date]];
		[comps setHour:0];
		[comps setMinute:0];
		[comps setSecond:0];
		[comps setDay:[comps day] - i];
		NSDate *referenceDate = [calendar dateFromComponents:comps];
		// Get week day (starts at 1).
		int weekday = [[calendar components:unitFlags fromDate:referenceDate] weekday] - 1;
		
		if ([suppliedDate compare:referenceDate] == NSOrderedSame && i == -1) {
			// Tomorrow
			return @"Morgen";
		} else if ([suppliedDate compare:referenceDate] == NSOrderedSame && i == 0)	{
			return @"Heute";
		} else if ([suppliedDate compare:referenceDate] == NSOrderedSame && i == 1)	{
			return @"Gestern";
		} else if ([suppliedDate compare:referenceDate] == NSOrderedSame) {
			// Day of the week
			NSString *day = [[niceDateFormatter weekdaySymbols] objectAtIndex:weekday];
			return day;
		}
	}
	
	// It's not in those eight days.
	NSString *defaultDate = [niceDateFormatter stringFromDate:date];
	return defaultDate;
}		


#pragma mark -
#pragma mark Singleton methods

+ (ModelLocator*)sharedInstance {
    @synchronized(self) {
        if (sharedInstance == nil)
			sharedInstance = [[ModelLocator alloc] init];
    }
    return sharedInstance;
}

+ (id)allocWithZone:(NSZone *)zone {
    @synchronized(self) {
        if (sharedInstance == nil) {
            sharedInstance = [super allocWithZone:zone];
            return sharedInstance;  // assignment and return on first allocation
        }
    }
    return nil; // on subsequent allocation attempts return nil
}

- (id)copyWithZone:(NSZone *)zone {
    return self;
}

@end
