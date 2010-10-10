//
//  NamNamMensaPickerController.h
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-10-09.
//  Copyright 2010 Trading & Consulting 'H.P.C.' GmbH. All rights reserved.
//

#import <UIKit/UIKit.h>

@class MensaURL;

@protocol NamNamMensaPickerDelegate <NSObject>

@optional
- (void)didSelectMensaUrl:(MensaURL *)mensaUrl;
@end

@interface NamNamMensaPickerController : UITableViewController {
	id <NamNamMensaPickerDelegate> delegate;

	NSArray* mensen;
	MensaURL* currentSelection;
}
@property (nonatomic, assign) id <NamNamMensaPickerDelegate> delegate;
@property (nonatomic, retain) NSArray *mensen;
@property (nonatomic, retain) MensaURL *currentSelection;

@end
