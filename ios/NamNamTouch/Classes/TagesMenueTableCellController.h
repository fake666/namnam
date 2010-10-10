//
//  TagesMenueTableCellController.h
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-10-09.
//  Copyright 2010 Trading & Consulting 'H.P.C.' GmbH. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface TagesMenueTableCellController : UITableViewCell {

	IBOutlet UITextView *titleText; 
	IBOutlet UILabel *price; 
	IBOutlet UIImageView *token1; 
	IBOutlet UIImageView *token2; 
	IBOutlet UIImageView *token3; 
}

@property (nonatomic, retain) IBOutlet UITextView* titleText;
@property (nonatomic, retain) IBOutlet UILabel* price;
@property (nonatomic, retain) IBOutlet UIImageView* token1;
@property (nonatomic, retain) IBOutlet UIImageView* token2;
@property (nonatomic, retain) IBOutlet UIImageView* token3;

@end
