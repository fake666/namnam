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
	UIImage* pork;
	UIImage* beef;
  	UIImage* poultry;
  	UIImage* vegan;
  	UIImage* fish;
}
@property (nonatomic, strong) UIImage* veggie;
@property (nonatomic, strong) UIImage* pork;
@property (nonatomic, strong) UIImage* beef;
@property (nonatomic, strong) UIImage* poultry;
@property (nonatomic, strong) UIImage* vegan;
@property (nonatomic, strong) UIImage* fish;
@property (nonatomic, unsafe_unretained) IBOutlet TagesMenueTableCellController *tmpCell;
@property (nonatomic, strong) ModelLocator* model;

- (void)scrollToTagesmenue:(Tagesmenue*)tm;

@end
