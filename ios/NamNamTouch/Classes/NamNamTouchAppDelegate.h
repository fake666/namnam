//
//  NamNamTouchAppDelegate.h
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-09-25.
//  Copyright 2010 Trading & Consulting 'H.P.C.' GmbH. All rights reserved.
//

#import <UIKit/UIKit.h>

@class ModelLocator;

@interface NamNamTouchAppDelegate : NSObject <UIApplicationDelegate> {
    
    UIWindow *window;
    UINavigationController *navigationController;
	UIActivityIndicatorView *act;
	
	
	ModelLocator* model;
}

@property (nonatomic, retain) IBOutlet UIWindow *window;
@property (nonatomic, retain) IBOutlet UINavigationController *navigationController;
@property (nonatomic, retain) ModelLocator *model;
@property (nonatomic, retain) UIActivityIndicatorView *act;

- (void)doLoad;

@end

