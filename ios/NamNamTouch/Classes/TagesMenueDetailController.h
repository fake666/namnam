//
//  TagesMenueDetailController.h
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-09-26.
//  Copyright 2010 Trading & Consulting 'H.P.C.' GmbH. All rights reserved.
//

#import <UIKit/UIKit.h>

@class Tagesmenue;
@class TagesMenueTableCellController;
@class TagesMenueDetailController;
@class NamNamSettingsController;

@protocol NamNamTagesmenueNextPrevDelegate <NSObject>

@optional
- (void)setNextTagesmenue:(TagesMenueDetailController *)view;
- (void)setPrevTagesmenue:(TagesMenueDetailController *)view;
@end

@interface TagesMenueDetailController : UITableViewController <UIGestureRecognizerDelegate> {

	Tagesmenue* tagesmenue;
	TagesMenueTableCellController* tmpCell;
	
	NSString* navTitle;
	
	UIImage* veggie;
	UIImage* nopork;
	UIImage* beef;
	
	NamNamSettingsController* settingsController;
	
	id<NamNamTagesmenueNextPrevDelegate> delegate;
	
}

@property (nonatomic, retain) Tagesmenue* tagesmenue;
@property (nonatomic, retain) NSString* navTitle;
@property (nonatomic, retain) UIImage* veggie;
@property (nonatomic, retain) UIImage* nopork;
@property (nonatomic, retain) UIImage* beef;
@property (nonatomic, assign) IBOutlet TagesMenueTableCellController *tmpCell;
@property (nonatomic, retain) NamNamSettingsController* settingsController;
@property (nonatomic, assign) id <NamNamTagesmenueNextPrevDelegate> delegate;

@end
