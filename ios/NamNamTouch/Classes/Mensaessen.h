//
//  Mensaessen.h
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-09-25.
//  Copyright 2010 Thomas "fake" Jakobi. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Essen.h"

@class Preis;

@interface Mensaessen : Essen {

	int studentenPreis;
	
}

@property int studentenPreis;

-(NSDictionary*)serialize;
+ (Mensaessen*)deserialize:(NSDictionary*)dict;

@end
