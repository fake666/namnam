//
//  NamNamSettingsScreen.h
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-10-09.
//  Copyright 2010 Trading & Consulting 'H.P.C.' GmbH. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "NamNamMensaPickerController.h"

@class MensaURL;

#define PRICE_DISPLAY_BOTH 0
#define PRICE_DISPLAY_STUDENT 1
#define PRICE_DISPLAY_NORMAL 2

@protocol NamNamMensaReloadDelegate <NSObject>

@optional
- (void)mensaChanged:(MensaURL *)mensaUrl;
@end


@interface NamNamSettingsController : UIViewController<NamNamMensaPickerDelegate> {
	id<NamNamMensaReloadDelegate> delegate;
	
	BOOL mensaChanged;
	
	NSArray* mensen;
	MensaURL* mensaSelection;
	NSInteger priceDisplaySelection;
	
	IBOutlet UIButton *selectMensaButton;
	IBOutlet UISegmentedControl *selectPriceControl; 

}
@property (nonatomic, assign) id <NamNamMensaReloadDelegate> delegate;
@property (nonatomic, retain) NSArray *mensen;
@property (nonatomic, retain) MensaURL *mensaSelection;
@property NSInteger priceDisplaySelection;
@property BOOL mensaChanged;
@property (nonatomic, retain) IBOutlet UIButton* selectMensaButton;
@property (nonatomic, retain) IBOutlet UISegmentedControl* selectPriceControl;

- (void)saveProperties;
- (void)loadSettings;
- (void)loadMensae;
- (IBAction) selectMensa: (id) sender;
- (IBAction) selectPriceDisplay: (id) sender;
- (IBAction) openNamNamWebpage: (id) sender;
- (void)didSelectMensaUrl:(MensaURL *)mensaUrl;

@end
