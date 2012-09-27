//
//  NamNamTouchAppDelegate.h
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-09-25.
//  Copyright 2010 Thomas "fake" Jakobi. All rights reserved.
//

#import <UIKit/UIKit.h>

@class ModelLocator;

@interface NamNamTouchAppDelegate : NSObject <UIApplicationDelegate> {
    
    UIWindow *window;
    UINavigationController *navigationController;
	UIActivityIndicatorView *act;
	
	
	ModelLocator* model;
}

@property (nonatomic, strong) IBOutlet UIWindow *window;
@property (nonatomic, strong) IBOutlet UINavigationController *navigationController;
@property (nonatomic, strong) ModelLocator *model;
@property (nonatomic, strong) UIActivityIndicatorView *act;

- (void)doLoad;

@end

