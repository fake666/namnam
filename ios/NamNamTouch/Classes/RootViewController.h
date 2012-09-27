//
//  RootViewController.h
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-09-25.
//  Copyright 2010 Thomas "fake" Jakobi. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "NamNamSettingsController.h"
#import "TagesMenueDetailController.h"
#import "ModelLocator.h"

@class MensaURL;

@interface RootViewController : UITableViewController <UINavigationBarDelegate, NamNamModelLocatorDelegate,
													   NamNamMensaReloadDelegate> {
	NamNamSettingsController* settingsController;
	TagesMenueDetailController* tmController;
														   
    ModelLocator* model;
}

- (void)scrollToTagesmenue:(Tagesmenue*)tm;
- (void)switchToTagesMenueDetailView:(Tagesmenue*)t;

- (void)mensaChanged:(MensaURL *)mensaUrl;
- (IBAction)modalViewAction:(id)sender;

- (void)loadingFinished;
- (void)loadingFailed;
- (void)mensaNameKnown:(NSString*)name;

@property(nonatomic, strong) NamNamSettingsController *settingsController;
@property(nonatomic, strong) TagesMenueDetailController *tmController;
@property(nonatomic, strong) ModelLocator *model;

@end
