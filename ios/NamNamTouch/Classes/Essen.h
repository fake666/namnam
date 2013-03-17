//
//  Essen.h
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-09-25.
//  Copyright 2010 Thomas "fake" Jakobi. All rights reserved.
//

#import <Foundation/Foundation.h>

@class Preis;

@interface Essen : NSObject {
	
	int preis;
	NSString* beschreibung;
	
	BOOL vegetarian;
	BOOL vegan;
	BOOL pork;
	BOOL poultry;
	BOOL beef;
	BOOL fish;
}

@property int preis;
@property (nonatomic, strong) NSString *beschreibung;

@property BOOL vegetarian;
@property BOOL vegan;
@property BOOL pork;
@property BOOL poultry;
@property BOOL beef;
@property BOOL fish;

-(NSDictionary*)serialize;
+ (Essen*)deserialize:(NSDictionary*)dict;

@end
