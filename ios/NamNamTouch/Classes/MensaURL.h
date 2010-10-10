//
//  MensaURL.h
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-10-09.
//  Copyright 2010 Trading & Consulting 'H.P.C.' GmbH. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface MensaURL : NSObject {

	NSString* url;
	NSString* name;
}

@property (nonatomic, retain) NSString *name;
@property (nonatomic, retain) NSString *url;

- (NSString *)description;

@end
