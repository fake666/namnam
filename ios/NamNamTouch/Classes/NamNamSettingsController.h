//
//  NamNamSettingsScreen.h
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-10-09.
//  Copyright 2010 Trading & Consulting 'H.P.C.' GmbH. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "NamNamMensaPickerController.h"

@class MensaURL, ModelLocator;


@protocol NamNamMensaReloadDelegate <NSObject>

@optional
- (void)mensaChanged:(MensaURL *)mensaUrl;
@end


@interface NamNamSettingsController : UIViewController<NamNamMensaPickerDelegate> {
	id<NamNamMensaReloadDelegate> delegate;
	ModelLocator* model;
	
	BOOL mensaChanged;
	
	NSDateFormatter* formatter;
	
	IBOutlet UIButton *selectMensaButton;
	IBOutlet UISegmentedControl *selectPriceControl; 
	IBOutlet UILabel *lastUpdateLabel;

}
@property (nonatomic, assign) id <NamNamMensaReloadDelegate> delegate;
@property (nonatomic, retain) ModelLocator* model;
@property BOOL mensaChanged;
@property (nonatomic, retain) NSDateFormatter* formatter;
@property (nonatomic, retain) IBOutlet UIButton* selectMensaButton;
@property (nonatomic, retain) IBOutlet UISegmentedControl* selectPriceControl;
@property (nonatomic, retain) IBOutlet UILabel* lastUpdateLabel;

- (IBAction) selectMensa: (id) sender;
- (IBAction) selectPriceDisplay: (id) sender;
- (IBAction) openNamNamWebpage: (id) sender;
- (void)didSelectMensaUrl:(MensaURL *)mensaUrl;

@end
