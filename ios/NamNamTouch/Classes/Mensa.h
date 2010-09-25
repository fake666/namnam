//
//  Mensa.h
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-09-25.
//  Copyright 2010 Trading & Consulting 'H.P.C.' GmbH. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface Mensa : NSObject {
	NSMutableArray* dayMenues;
	NSString* name;
	NSDate* firstDate;
	NSDate* lastDate;
}

@property (nonatomic, retain) NSString *name;
@property (nonatomic, retain) NSMutableArray *dayMenues;
@property (nonatomic, retain) NSDate *firstDate;
@property (nonatomic, retain) NSDate *lastDate;

@end
