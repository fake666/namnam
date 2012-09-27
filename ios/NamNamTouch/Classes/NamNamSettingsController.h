//
//  NamNamSettingsScreen.h
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-10-09.
//  Copyright 2010 Thomas "fake" Jakobi. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "NamNamMensaPickerController.h"

@class MensaURL, ModelLocator;


@protocol NamNamMensaReloadDelegate <NSObject>

@optional
- (void)mensaChanged:(MensaURL *)mensaUrl;
@end


@interface NamNamSettingsController : UIViewController<NamNamMensaPickerDelegate> {
	id<NamNamMensaReloadDelegate> __unsafe_unretained delegate;
	ModelLocator* model;
	
	BOOL mensaChanged;
	
	NSDateFormatter* formatter;
	
	IBOutlet UIButton *selectMensaButton;
	IBOutlet UISegmentedControl *selectPriceControl; 
	IBOutlet UILabel *lastUpdateLabel;

}
@property (nonatomic, unsafe_unretained) id <NamNamMensaReloadDelegate> delegate;
@property (nonatomic, strong) ModelLocator* model;
@property BOOL mensaChanged;
@property (nonatomic, strong) NSDateFormatter* formatter;
@property (nonatomic, strong) IBOutlet UIButton* selectMensaButton;
@property (nonatomic, strong) IBOutlet UISegmentedControl* selectPriceControl;
@property (nonatomic, strong) IBOutlet UILabel* lastUpdateLabel;

- (IBAction) selectMensa: (id) sender;
- (IBAction) selectPriceDisplay: (id) sender;
- (IBAction) openNamNamWebpage: (id) sender;
- (void)didSelectMensaUrl:(MensaURL *)mensaUrl;

@end
