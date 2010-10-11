//
//  RootViewController.h
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-09-25.
//  Copyright 2010 Trading & Consulting 'H.P.C.' GmbH. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "NamNamSettingsController.h"
#import "TagesMenueDetailController.h"
#import "ModelLocator.h"

@class MensaURL;

@interface RootViewController : UITableViewController <UINavigationBarDelegate, NamNamModelLocatorDelegate,
													   NamNamMensaReloadDelegate, NamNamTagesmenueNextPrevDelegate> {
	NSDateFormatter *dateFormatter;
	NamNamSettingsController* settingsController;
	TagesMenueDetailController* tmController;
														   
    ModelLocator* model;
}

- (void)scrollToTagesmenue:(Tagesmenue*)tm;
- (void)switchToTagesMenueDetailView:(Tagesmenue*)t;

- (NSString*)transformedValue:(NSDate*)theDate;
- (void)mensaChanged:(MensaURL *)mensaUrl;
- (IBAction)modalViewAction:(id)sender;

- (void)setNextTagesmenue:(Tagesmenue *)currentTm;
- (void)setPrevTagesmenue:(Tagesmenue *)currentTm;

- (void)loadingFinished;
- (void)loadingFailed;
- (void)mensaNameKnown:(NSString*)name;

@property(nonatomic, retain) NSDateFormatter *dateFormatter;
@property(nonatomic, retain) NamNamSettingsController *settingsController;
@property(nonatomic, retain) TagesMenueDetailController *tmController;
@property(nonatomic, retain) ModelLocator *model;

@end
