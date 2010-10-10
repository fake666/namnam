//
//  NamNamSettingsScreen.m
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-10-09.
//  Copyright 2010 Trading & Consulting 'H.P.C.' GmbH. All rights reserved.
//

#import "NamNamSettingsController.h"
#import "NamNamMensaPickerController.h"
#import "MensaURL.h"

@implementation NamNamSettingsController

@synthesize delegate, mensen, mensaSelection, selectMensaButton, selectPriceControl, priceDisplaySelection, mensaChanged;

#pragma mark -
#pragma mark View lifecycle

NSInteger mensaNameSort(MensaURL *mensa1, MensaURL *mensa2, void* ignored) {
	return [[mensa1 name] localizedCaseInsensitiveCompare:[mensa2 name]];
}	

- (void) loadSettings {
	[self loadMensae];
	
	// now load the current settings and select the corresponding mensa
	NSString *errorDesc = nil;
	NSPropertyListFormat format;
	NSString *plistPath;
	NSString *rootPath = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,
															  NSUserDomainMask, YES) objectAtIndex:0];
	plistPath = [rootPath stringByAppendingPathComponent:@"NamNamSettings.plist"];
	if (![[NSFileManager defaultManager] fileExistsAtPath:plistPath]) {
		plistPath = [[NSBundle mainBundle] pathForResource:@"NamNamSettings" ofType:@"plist"];
	}
	NSData *plistXML = [[NSFileManager defaultManager] contentsAtPath:plistPath];
	NSDictionary *temp = (NSDictionary *)[NSPropertyListSerialization
										  propertyListFromData:plistXML
										  mutabilityOption:NSPropertyListMutableContainersAndLeaves
										  format:&format
										  errorDescription:&errorDesc];
	if (!temp) {
		NSLog(@"Error reading plist: %@, format: %d", errorDesc, format);
	}
	
	NSDictionary* selectedMensa = [temp objectForKey:@"SelectedMensa"];
	NSString* priceDisplaySelectionString = [temp objectForKey:@"ShowPrice"];
	
	if([priceDisplaySelectionString isEqualToString:@"STUDENT"]) {
		self.priceDisplaySelection = PRICE_DISPLAY_STUDENT;
	} else if([priceDisplaySelectionString isEqualToString:@"NORMAL"]) {
		self.priceDisplaySelection = PRICE_DISPLAY_NORMAL;
	} else {
		self.priceDisplaySelection = PRICE_DISPLAY_BOTH;
	}
	
	NSString* selMensaName = [selectedMensa objectForKey:@"name"];
	for(int n = 0; n < self.mensen.count; n++) {
		MensaURL* cur = [self.mensen objectAtIndex:n];
		if([cur.name isEqualToString:selMensaName]) {
			mensaSelection = cur;
			break;
		}
	}
}


- (void)loadMensae {
	NSString *errorDesc = nil;
	NSPropertyListFormat format;
	NSString *plistPath;
	NSString *rootPath = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,
															  NSUserDomainMask, YES) objectAtIndex:0];
	plistPath = [rootPath stringByAppendingPathComponent:@"NamNamURLs.plist"];
	if (![[NSFileManager defaultManager] fileExistsAtPath:plistPath]) {
		plistPath = [[NSBundle mainBundle] pathForResource:@"NamNamURLs" ofType:@"plist"];
	}
	NSData *plistXML = [[NSFileManager defaultManager] contentsAtPath:plistPath];
	NSDictionary *temp = (NSDictionary *)[NSPropertyListSerialization
										  propertyListFromData:plistXML
										  mutabilityOption:NSPropertyListMutableContainersAndLeaves
										  format:&format
										  errorDescription:&errorDesc];
	if (!temp) {
		NSLog(@"Error reading plist: %@, format: %d", errorDesc, format);
	}
	
	NSDictionary* input = [temp objectForKey:@"Mensen"];
	NSMutableArray* parsed = [[NSMutableArray alloc] init];
	
	NSEnumerator *enumerator = [input keyEnumerator];
	id key;
	
	while ((key = [enumerator nextObject])) {
		MensaURL* mensaurl = [[MensaURL alloc] init];
		mensaurl.url = [input objectForKey:key];
		mensaurl.name = key;
		[parsed addObject:mensaurl];
	}
	
	self.mensen = [NSArray arrayWithArray:[parsed sortedArrayUsingFunction:mensaNameSort  context:nil]];
	
	[parsed release];
}

- (void)saveProperties {
	NSString *error;
	NSString *rootPath = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
	NSString *plistPath = [rootPath stringByAppendingPathComponent:@"NamNamSettings.plist"];
	NSMutableDictionary *plistDict = [NSMutableDictionary dictionaryWithCapacity:2];
	
	NSMutableDictionary *mensaSelDict = [NSMutableDictionary dictionaryWithCapacity:2];
	[mensaSelDict setValue:mensaSelection.name forKey:@"name"];
	[mensaSelDict setValue:mensaSelection.url forKey:@"url"];
	
	[plistDict setValue:mensaSelDict forKey:@"SelectedMensa"];
	
	NSString* priceSelectionString;
	if(self.priceDisplaySelection == PRICE_DISPLAY_STUDENT) {
		priceSelectionString = [[NSString alloc] initWithString:@"STUDENT"];
	} else if(self.priceDisplaySelection == PRICE_DISPLAY_NORMAL) {
		priceSelectionString = [[NSString alloc] initWithString:@"NORMAL"];
	} else {
		priceSelectionString = [[NSString alloc] initWithString:@"BOTH"];
	}
	[plistDict setValue:priceSelectionString forKey:@"ShowPrice"];
	
	NSData *plistData = [NSPropertyListSerialization dataFromPropertyList:plistDict
																   format:NSPropertyListXMLFormat_v1_0
														 errorDescription:&error];
	if(plistData) {
		[plistData writeToFile:plistPath atomically:YES];
	} else {
		NSLog(@"error saving settings: %@",error);
		[error release];
	}	
}

- (void)viewDidLoad {
    [super viewDidLoad];

	[selectMensaButton setTitle:mensaSelection.name forState:UIControlStateNormal];
	selectPriceControl.selectedSegmentIndex = self.priceDisplaySelection;
	
	self.title = @"Einstellungen";
	
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
   // self.navigationItem.rightBarButtonItem = self.editButtonItem;
	self.navigationItem.backBarButtonItem = nil;
	self.navigationItem.leftBarButtonItem = nil;
}


/*
- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
}
*/
/*
- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
}
*/

// XXX TODO reload mensa data if it changed
- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
	
	if(self.mensaChanged) {
		self.mensaChanged = NO;
		[delegate mensaChanged:self.mensaSelection]; 
	}
}

/*
- (void)viewDidDisappear:(BOOL)animated {
    [super viewDidDisappear:animated];
}
*/
/*
// Override to allow orientations other than the default portrait orientation.
- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}
*/

- (IBAction) selectPriceDisplay: (id) sender {
	self.priceDisplaySelection = selectPriceControl.selectedSegmentIndex;
	[self saveProperties];
}

- (IBAction) selectMensa: (id) sender {
	NamNamMensaPickerController *picker = [[NamNamMensaPickerController alloc] init];
	picker.delegate = self;
	picker.mensen = self.mensen;
	picker.currentSelection = self.mensaSelection;
	// Pass the selected object to the new view controller.
	[self.navigationController presentModalViewController:picker animated:YES];
	[picker release];
}

- (void)didSelectMensaUrl:(MensaURL *)mensaUrl {
	if(![self.mensaSelection.name isEqualToString:mensaUrl.name]) {
		self.mensaChanged = YES;
		self.mensaSelection = mensaUrl;
		[selectMensaButton setTitle:mensaSelection.name forState:UIControlStateNormal];
		[self saveProperties];
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
	[mensaSelection release];
	[mensen release];
    [super dealloc];
}


@end

