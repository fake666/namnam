//
//  NamNamMensaPickerController.h
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-10-09.
//  Copyright 2010 Thomas "fake" Jakobi. All rights reserved.
//

#import <UIKit/UIKit.h>

@class MensaURL, ModelLocator;

@protocol NamNamMensaPickerDelegate <NSObject>

@optional
- (void)didSelectMensaUrl:(MensaURL *)mensaUrl;
@end

@interface NamNamMensaPickerController : UITableViewController {
	id <NamNamMensaPickerDelegate> delegate;
	ModelLocator* model;
}
@property (nonatomic, assign) id <NamNamMensaPickerDelegate> delegate;
@property (nonatomic, retain) ModelLocator* model;

@end
