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
@synthesize vegan;
@synthesize beef;
@synthesize pork;
@synthesize poultry;
@synthesize fish;

-(NSDictionary*)serialize {
	NSMutableDictionary *dict = [NSMutableDictionary dictionaryWithCapacity:8];
	[dict setValue:[NSNumber numberWithInt:preis] forKey:@"preis"];
	[dict setValue:beschreibung forKey:@"beschreibung"];
	[dict setValue:vegetarian?@"YES":@"NO" forKey:@"vegetarian"];
	[dict setValue:vegan?@"YES":@"NO" forKey:@"vegan"];
	[dict setValue:beef?@"YES":@"NO" forKey:@"beef"];
	[dict setValue:pork?@"YES":@"NO" forKey:@"pork"];
	[dict setValue:poultry?@"YES":@"NO" forKey:@"poultry"];
	[dict setValue:fish?@"YES":@"NO" forKey:@"fish"];
	return dict;
}

+ (Essen*)deserialize:(NSDictionary*)dict {
	
	Essen* ret = [[Essen alloc] init];
	ret.preis = [[dict objectForKey:@"preis"] integerValue];
	
	ret.beschreibung = [[dict objectForKey:@"beschreibung"] copy];  
	
	ret.vegetarian = [[dict objectForKey:@"vegetarian"] isEqualToString:@"YES"];
	ret.vegan = [[dict objectForKey:@"vegan"] isEqualToString:@"YES"];
	ret.pork = [[dict objectForKey:@"pork"] isEqualToString:@"YES"];
	ret.beef = [[dict objectForKey:@"beef"] isEqualToString:@"YES"];
	ret.poultry = [[dict objectForKey:@"poultry"] isEqualToString:@"YES"];
	ret.fish = [[dict objectForKey:@"fish"] isEqualToString:@"YES"];    

	return ret;
}



@end
