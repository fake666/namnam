//
//  NamNamSettingsScreen.m
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-10-09.
//  Copyright 2010 Thomas "fake" Jakobi. All rights reserved.
//

#import "NamNamSettingsController.h"
#import "NamNamMensaPickerController.h"
#import "MensaURL.h"
#import "Mensa.h"
#import "ModelLocator.h"

@implementation NamNamSettingsController

@synthesize delegate, selectMensaButton, selectPriceControl, mensaChanged, lastUpdateLabel, formatter, model;

#pragma mark -
#pragma mark View lifecycle

- (void)viewDidLoad {
    [super viewDidLoad];
	
	model = [ModelLocator sharedInstance];

	[selectMensaButton setTitle:model.mensaURL.name forState:UIControlStateNormal];
	selectPriceControl.selectedSegmentIndex = model.priceDisplayType;
	
	self.title = @"Einstellungen";
	
	//init the date formater for the last update display
	self.formatter = [[[NSDateFormatter alloc] init] autorelease];
    [formatter setDateStyle:NSDateFormatterLongStyle];
    [formatter setTimeStyle:NSDateFormatterMediumStyle];
	
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
   // self.navigationItem.rightBarButtonItem = self.editButtonItem;
	//self.navigationItem.backBarButtonItem = nil;
	//self.navigationItem.leftBarButtonItem = nil;
}



- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
	if(model.mensa.lastUpdate != nil) {
		lastUpdateLabel.text = [[[NSString alloc] initWithFormat:@"leztes Update: %@", [formatter stringFromDate:model.mensa.lastUpdate]] autorelease];
	} else {
		lastUpdateLabel.text = @"leztes Update: nie";
	}
}

/*
- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
}
*/


- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
	model.appWasInBackGround = NO;
}


- (void)viewDidDisappear:(BOOL)animated {
    [super viewDidDisappear:animated];
	
	if(self.mensaChanged) {
		self.mensaChanged = NO;
		[delegate mensaChanged:model.mensaURL]; 
	}
	
}


// Override to allow orientations other than the default portrait orientation.
- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}


- (IBAction) selectPriceDisplay: (id) sender {
	model.priceDisplayType = selectPriceControl.selectedSegmentIndex;
	[model saveSettings];
}

- (IBAction) selectMensa: (id) sender {
	NamNamMensaPickerController *picker = [[NamNamMensaPickerController alloc] init];
	picker.delegate = self;
	[self.navigationController presentModalViewController:picker animated:YES];
	[picker release];
}

- (void)didSelectMensaUrl:(MensaURL *)mensaUrl {
	if(![model.mensaURL.name isEqualToString:mensaUrl.name]) {
		self.mensaChanged = YES;
		model.mensaURL = mensaUrl;
		[selectMensaButton setTitle:mensaUrl.name forState:UIControlStateNormal];
		[model saveSettings];
	}
}

- (IBAction) openNamNamWebpage: (id) sender {
	[[UIApplication sharedApplication] openURL:[NSURL URLWithString:@"http://namnam.bytewerk.org"]];
}
#pragma mark -
#pragma mark Memory management

- (void)didReceiveMemoryWarning {
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Relinquish ownership any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {
    // Relinquish ownership of anything that can be recreated in viewDidLoad or on demand.
    // For example: self.myOutlet = nil;
}


- (void)dealloc {
	[formatter release];
    [super dealloc];
}


@end

