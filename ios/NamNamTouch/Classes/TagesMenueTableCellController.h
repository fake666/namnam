//
//  TagesMenueTableCellController.h
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-10-09.
//  Copyright 2010 Thomas "fake" Jakobi. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface TagesMenueTableCellController : UITableViewCell {

	IBOutlet UITextView *titleText; 
	IBOutlet UILabel *price; 
	IBOutlet UIImageView *token1; 
	IBOutlet UIImageView *token2; 
	IBOutlet UIImageView *token3; 
}

@property (nonatomic, strong) IBOutlet UITextView* titleText;
@property (nonatomic, strong) IBOutlet UILabel* price;
@property (nonatomic, strong) IBOutlet UIImageView* token1;
@property (nonatomic, strong) IBOutlet UIImageView* token2;
@property (nonatomic, strong) IBOutlet UIImageView* token3;

@end
