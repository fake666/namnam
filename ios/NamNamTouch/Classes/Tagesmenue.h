//
//  Tagesmenue.h
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-09-25.
//  Copyright 2010 Thomas "fake" Jakobi. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface Tagesmenue : NSObject {
	NSDate* tag;
	NSArray* menues;
}

@property (nonatomic, retain) NSArray *menues;
@property (nonatomic, retain) NSDate *tag;

-(NSDictionary*)serialize;
+ (Tagesmenue*)deserialize:(NSDictionary*)dict;

@end
