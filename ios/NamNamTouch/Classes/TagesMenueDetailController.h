//
//  TagesMenueDetailController.h
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-09-26.
//  Copyright 2010 Thomas "fake" Jakobi. All rights reserved.
//

#import <UIKit/UIKit.h>

@class Tagesmenue;
@class TagesMenueTableCellController;
@class TagesMenueDetailController;
@class ModelLocator;

@interface TagesMenueDetailController : UITableViewController {

	ModelLocator* model;
	
	TagesMenueTableCellController* tmpCell;
	
	UIImage* veggie;
	UIImage* nopork;
	UIImage* beef;
	
}
@property (nonatomic, retain) UIImage* veggie;
@property (nonatomic, retain) UIImage* nopork;
@property (nonatomic, retain) UIImage* beef;
@property (nonatomic, assign) IBOutlet TagesMenueTableCellController *tmpCell;
@property (nonatomic, retain) ModelLocator* model;

- (void)scrollToTagesmenue:(Tagesmenue*)tm;

@end
