//
//  TagesMenueDetailController.h
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-09-26.
//  Copyright 2010 Trading & Consulting 'H.P.C.' GmbH. All rights reserved.
//

#import <UIKit/UIKit.h>

@class Tagesmenue;

@interface TagesMenueDetailController : UITableViewController {

	Tagesmenue* tagesmenue;
	
}

@property (nonatomic, retain) Tagesmenue* tagesmenue;

@end
