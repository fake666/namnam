//
//  Mensaessen.m
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-09-25.
//  Copyright 2010 Thomas "fake" Jakobi. All rights reserved.
//

#import "Mensaessen.h"


@implementation Mensaessen

@synthesize studentenPreis;

-(NSDictionary*)serialize {
	NSMutableDictionary *dict = (NSMutableDictionary *)[super serialize];
	[dict setValue:[NSNumber numberWithInt:studentenPreis] forKey:@"studentenPreis"];
	return dict;
}

+ (Mensaessen*)deserialize:(NSDictionary*)dict {
	
	Mensaessen* ret = [[Mensaessen alloc]init];
	
	ret.preis = [[dict objectForKey:@"preis"] integerValue];
	
	ret.beschreibung = [[dict objectForKey:@"beschreibung"] copy]; 
	
	ret.vegetarian = [[dict objectForKey:@"vegetarian"] isEqualToString:@"YES"];
	ret.moslem = [[dict objectForKey:@"moslem"] isEqualToString:@"YES"];
	ret.beef = [[dict objectForKey:@"beef"] isEqualToString:@"YES"];
	
	ret.studentenPreis = [[dict objectForKey:@"studentenPreis"] integerValue];
	
	return ret;
}




@end
