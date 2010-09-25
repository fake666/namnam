//
//  Mensa.m
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-09-25.
//  Copyright 2010 Trading & Consulting 'H.P.C.' GmbH. All rights reserved.
//

#import "Mensa.h"


@implementation Mensa

@synthesize dayMenues;
@synthesize name;
@synthesize firstDate;
@synthesize lastDate;

- (id)init {
	id ret = [super init];
	
	dayMenues = [[NSMutableArray alloc] initWithCapacity:20];
	
	return ret;
}

- (void)dealloc {
	[firstDate release];
	[lastDate release];
	[dayMenues release];
	[name release];
	
    [super dealloc];
}

@end
