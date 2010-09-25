//
//  RootViewController.h
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-09-25.
//  Copyright 2010 Trading & Consulting 'H.P.C.' GmbH. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "NamNamXMLParser.h"

@interface RootViewController : UITableViewController <NamNamXMLParserDelegate> {
	NamNamXMLParser* parser;
	Mensa* mensa;
	
	NSDateFormatter* dateFormatter;
}

-transformedValue:(NSDate*)theDate;

@property(nonatomic, retain) NamNamXMLParser* parser;
@property(nonatomic, retain) Mensa* mensa;
@property(nonatomic, retain) NSDateFormatter* dateFormatter;

@end
