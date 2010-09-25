//
//  Tagesmenue.m
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-09-25.
//  Copyright 2010 Trading & Consulting 'H.P.C.' GmbH. All rights reserved.
//

#import "Tagesmenue.h"


@implementation Tagesmenue

@synthesize tag;
@synthesize menues;

- (id)init {
	id ret = [super init];
	
	menues = [[NSMutableArray alloc] initWithCapacity:3];
	
	return ret;
}

- (void)dealloc {
	[menues release];
	[tag release];
	
    [super dealloc];
}

@end
