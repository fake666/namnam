//
//  Essen.m
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-09-25.
//  Copyright 2010 Trading & Consulting 'H.P.C.' GmbH. All rights reserved.
//

#import "Essen.h"


@implementation Essen

@synthesize preis;
@synthesize beschreibung;
@synthesize vegetarian;
@synthesize moslem;
@synthesize beef;

- (void)dealloc {
	[beschreibung release];
	
    [super dealloc];
}

@end
