//
//  Tagesmenue.h
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-09-25.
//  Copyright 2010 Trading & Consulting 'H.P.C.' GmbH. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface Tagesmenue : NSObject {
	NSDate* tag;
	NSMutableArray* menues;
}

@property (nonatomic, retain) NSMutableArray *menues;
@property (nonatomic, retain) NSDate *tag;

@end
