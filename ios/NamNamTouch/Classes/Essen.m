//
//  Essen.m
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-09-25.
//  Copyright 2010 Thomas "fake" Jakobi. All rights reserved.
//

#import "Essen.h"


@implementation Essen

@synthesize preis;
@synthesize beschreibung;
@synthesize vegetarian;
@synthesize moslem;
@synthesize beef;

-(NSDictionary*)serialize {
	NSMutableDictionary *dict = [NSMutableDictionary dictionaryWithCapacity:6];
	[dict setValue:[NSNumber numberWithInt:preis] forKey:@"preis"];
	[dict setValue:beschreibung forKey:@"beschreibung"];
	[dict setValue:vegetarian?@"YES":@"NO" forKey:@"vegetarian"];
	[dict setValue:moslem?@"YES":@"NO" forKey:@"moslem"];
	[dict setValue:beef?@"YES":@"NO" forKey:@"beef"];
	return dict;
}

+ (Essen*)deserialize:(NSDictionary*)dict {
	
	Essen* ret = [[[Essen alloc] init] autorelease];
	ret.preis = [[dict objectForKey:@"preis"] integerValue];
	
	ret.beschreibung = [[[dict objectForKey:@"beschreibung"] copy] autorelease];  
	
	ret.vegetarian = [[dict objectForKey:@"vegetarian"] isEqualToString:@"YES"];
	ret.moslem = [[dict objectForKey:@"moslem"] isEqualToString:@"YES"];
	ret.beef = [[dict objectForKey:@"beef"] isEqualToString:@"YES"];

	return ret;
}


- (void)dealloc {
	[beschreibung release];
	
    [super dealloc];
}

@end
