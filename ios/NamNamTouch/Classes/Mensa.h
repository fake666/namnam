//
//  Mensa.h
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-09-25.
//  Copyright 2010 Thomas "fake" Jakobi. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface Mensa : NSObject {
	NSArray* dayMenues;
	NSString* name;
	NSDate* firstDate;
	NSDate* lastDate;
	
	NSDate* lastUpdate;
	NSDateFormatter* dateFormat;
}

@property (nonatomic, retain) NSString *name;
@property (nonatomic, retain) NSArray *dayMenues;
@property (nonatomic, retain) NSDate *firstDate;
@property (nonatomic, retain) NSDate *lastDate;

@property (nonatomic, retain) NSDate *lastUpdate;
@property (nonatomic, retain) NSDateFormatter *dateFormat;

-(NSArray*)dayMenuIndexArray;
-(NSDictionary*)serialize;
+ (Mensa*)deserialize:(NSDictionary*)dict;

@end
