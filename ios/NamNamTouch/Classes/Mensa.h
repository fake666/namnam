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

@property (nonatomic, strong) NSString *name;
@property (nonatomic, strong) NSArray *dayMenues;
@property (nonatomic, strong) NSDate *firstDate;
@property (nonatomic, strong) NSDate *lastDate;

@property (nonatomic, strong) NSDate *lastUpdate;
@property (nonatomic, strong) NSDateFormatter *dateFormat;

-(NSArray*)dayMenuIndexArray;
-(NSDictionary*)serialize;
+ (Mensa*)deserialize:(NSDictionary*)dict;

@end
