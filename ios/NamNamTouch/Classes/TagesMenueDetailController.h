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
	
	TagesMenueTableCellController* __unsafe_unretained tmpCell;
	
	UIImage* veggie;
	UIImage* nopork;
	UIImage* beef;
	
}
@property (nonatomic, strong) UIImage* veggie;
@property (nonatomic, strong) UIImage* nopork;
@property (nonatomic, strong) UIImage* beef;
@property (nonatomic, unsafe_unretained) IBOutlet TagesMenueTableCellController *tmpCell;
@property (nonatomic, strong) ModelLocator* model;

- (void)scrollToTagesmenue:(Tagesmenue*)tm;

@end
