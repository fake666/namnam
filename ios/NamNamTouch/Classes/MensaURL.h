//
//  MensaURL.h
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-10-09.
//  Copyright 2010 Thomas "fake" Jakobi. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface MensaURL : NSObject {

	NSString* url;
	NSString* name;
}

@property (nonatomic, strong) NSString *name;
@property (nonatomic, strong) NSString *url;

- (NSString *)description;

@end
