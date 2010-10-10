//
//  RootViewController.h
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-09-25.
//  Copyright 2010 Trading & Consulting 'H.P.C.' GmbH. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "NamNamXMLParser.h"
#import "NamNamSettingsController.h"
#import "TagesMenueDetailController.h"

@class MensaURL;


@interface RootViewController : UITableViewController <NamNamXMLParserDelegate, UINavigationBarDelegate, NamNamMensaReloadDelegate, NamNamTagesmenueNextPrevDelegate> {
	NamNamXMLParser* parser;
	Mensa* mensa;
	
	NSDateFormatter* dateFormatter;
	NamNamSettingsController *settingsController;
	TagesMenueDetailController *tmController;
}

-transformedValue:(NSDate*)theDate;
- (void)mensaChanged:(MensaURL *)mensaUrl;

- (void)setNextTagesmenue:(TagesMenueDetailController *)view;
- (void)setPrevTagesmenue:(TagesMenueDetailController *)view;

@property(nonatomic, retain) NamNamXMLParser* parser;
@property(nonatomic, retain) Mensa* mensa;
@property(nonatomic, retain) NSDateFormatter* dateFormatter;
@property (nonatomic, retain) NamNamSettingsController *settingsController;
@property (nonatomic, retain) TagesMenueDetailController *tmController;

@end
