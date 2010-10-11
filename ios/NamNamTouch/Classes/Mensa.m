//
//  Mensa.m
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-09-25.
//  Copyright 2010 Trading & Consulting 'H.P.C.' GmbH. All rights reserved.
//

#import "Mensa.h"
#import "Tagesmenue.h"

@implementation Mensa

@synthesize name, firstDate, lastDate, dayMenues, lastUpdate;

-(NSDictionary*)serialize {
	NSMutableDictionary *dict = [NSMutableDictionary dictionaryWithCapacity:5];
	[dict setValue:name forKey:@"name"];
	[dict setValue:firstDate forKey:@"firstDate"];
	[dict setValue:lastDate forKey:@"lastDate"];
	[dict setValue:lastUpdate forKey:@"lastUpdate"];
	
	NSMutableArray* days = [[[NSMutableArray alloc] initWithCapacity:dayMenues.count] autorelease];
	
	NSEnumerator* dmEnum = [dayMenues objectEnumerator];
	Tagesmenue* tm;
	while(tm = [dmEnum nextObject]) {
		[days addObject:[tm serialize]];
	}
	
	[dict setValue:days forKey:@"dayMenues"];
	
	return dict;
}

+ (Mensa*)deserialize:(NSDictionary*)dict {

	Mensa* ret = [[[Mensa alloc] init] autorelease];
	ret.lastUpdate = [[[dict objectForKey:@"lastUpdate"] copy] autorelease];
	ret.lastDate = [[[dict objectForKey:@"lastDate"] copy] autorelease];
	ret.firstDate = [[[dict objectForKey:@"firstDate"] copy] autorelease];
	ret.name = [[[dict objectForKey:@"name"] copy] autorelease];
	
	NSArray* dms = [dict objectForKey:@"dayMenues"];
	NSMutableArray* dmar = [[[NSMutableArray alloc] initWithCapacity:dms.count] autorelease];
	
	NSEnumerator* en = [dms objectEnumerator];
	NSDictionary* tmd;
	while(tmd = [en nextObject]) {
		[dmar addObject:[Tagesmenue deserialize:tmd]];
	}
	[ret setDayMenues:dmar];
	
	dms = nil;
	en = nil;
	tmd = nil;
	dmar = nil;
	
	return ret;
}

- (void)dealloc {
	[lastUpdate release];
	[firstDate release];
	[lastDate release];
	[dayMenues release];
	[name release];
	
    [super dealloc];
}

@end
