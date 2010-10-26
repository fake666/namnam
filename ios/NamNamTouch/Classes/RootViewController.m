//
//  RootViewController.m
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-09-25.
//  Copyright 2010 Thomas "fake" Jakobi. All rights reserved.
//

#import "RootViewController.h"
#import "NamNamSettingsController.h"
#import "Mensa.h"
#import "MensaURL.h"
#import "Tagesmenue.h"
#import "TagesMenueDetailController.h"
#import "ModelLocator.h"

@implementation RootViewController

@synthesize settingsController, tmController, model;
#pragma mark -
#pragma mark View lifecycle

- (void)awakeFromNib {
	model = [ModelLocator sharedInstance];
	model.delegate = self;
}

- (void)loadingFinished {
	self.title = model.mensaURL.name;
	[self.tableView reloadData];
	Tagesmenue* current = [model closestDayMenue];
	[self scrollToTagesmenue:current];
	
	if(model.appWasInBackGround) {
		model.appWasInBackGround = NO;
		[self switchToTagesMenueDetailView:current];
	}
}

- (void)mensaNameKnown:(NSString*)name {
	self.title = name;
}

- (void)loadingFailed {
	[self modalViewAction:self];
}

- (void)scrollToTagesmenue:(Tagesmenue*)tm {
	if(tm != nil) {
		int nearestIdx = [model.mensa.dayMenues indexOfObject:tm];
		NSIndexPath* indPath = [NSIndexPath indexPathForRow:nearestIdx inSection:0];
		[self.tableView scrollToRowAtIndexPath:indPath atScrollPosition:UITableViewScrollPositionMiddle animated:YES];	
	}
}

- (void)viewDidLoad {
    [super viewDidLoad];
	
	if (self.settingsController == nil) {
        self.settingsController = [[NamNamSettingsController alloc] 
									initWithNibName:NSStringFromClass([NamNamSettingsController class]) bundle:nil];
		settingsController.delegate = self;
	}
	
	if(self.tmController == nil) {
		self.tmController = [[TagesMenueDetailController alloc] init];
	}
	
	UIButton* modalViewButton = [UIButton buttonWithType:UIButtonTypeInfoLight];
	[modalViewButton addTarget:self action:@selector(modalViewAction:) forControlEvents:UIControlEventTouchUpInside];
	UIBarButtonItem *modalBarButtonItem = [[UIBarButtonItem alloc] initWithCustomView:modalViewButton];
	self.navigationItem.rightBarButtonItem = modalBarButtonItem;
	[modalBarButtonItem release];
		
	self.title = model.mensaURL.name;
	
	// check wether it's time to re-load data
	if(model.mensa != nil && [model.mensa.dayMenues count] > 0) {
		[self.tableView reloadData];
		[self scrollToTagesmenue:[model closestDayMenue]];
		[tmController scrollToTagesmenue:[model closestDayMenue]];
	}
}

- (void)mensaChanged:(MensaURL *)mensaUrl {
	model.mensa = nil;
	[model saveData];
	self.title = model.mensaURL.name;
	[self.tableView reloadData];
	[model fetchMensaData];
}



- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
	[self.tableView reloadData];
}


- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];

	if(model.appWasInBackGround && [model.mensa.dayMenues count] > 0) {
		[model.activity stopAnimating];
		model.appWasInBackGround = NO;
		// scroll to the current date and open the current or next daymenue
		Tagesmenue* current = [model closestDayMenue];
		[self scrollToTagesmenue:current];
		[self switchToTagesMenueDetailView:current];
	}
}

/*
- (void)viewWillDisappear:(BOOL)animated {
	[super viewWillDisappear:animated];
}
*/
/*
- (void)viewDidDisappear:(BOOL)animated {
	[super viewDidDisappear:animated];
}
*/


 // Override to allow orientations other than the default portrait orientation.
- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
	// Return YES for supported orientations.
	return (interfaceOrientation == UIInterfaceOrientationPortrait);
}



#pragma mark -
#pragma mark Table view data source

// Customize the number of sections in the table view.
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}


// Customize the number of rows in the table view.
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
	if(model.mensa != nil) return [model.mensa.dayMenues count];
	else return 0;
}


// Customize the appearance of table view cells.
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier] autorelease];
    }
    
	// Configure the cell.
	Tagesmenue* t = [model.mensa.dayMenues objectAtIndex:indexPath.row];
	cell.textLabel.text = [model getNiceDate:t.tag];
	cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    return cell;
}


/*
// Override to support conditional editing of the table view.
- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath {
    // Return NO if you do not want the specified item to be editable.
    return YES;
}
*/


/*
// Override to support editing the table view.
- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        // Delete the row from the data source.
        [tableView deleteRowsAtIndexPaths:[NSArray arrayWithObject:indexPath] withRowAnimation:UITableViewRowAnimationFade];
    }   
    else if (editingStyle == UITableViewCellEditingStyleInsert) {
        // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view.
    }   
}
*/


/*
// Override to support rearranging the table view.
- (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath {
}
*/


/*
// Override to support conditional rearranging of the table view.
- (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath {
    // Return NO if you do not want the item to be re-orderable.
    return YES;
}
*/


#pragma mark -
#pragma mark Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	[self switchToTagesMenueDetailView:[model.mensa.dayMenues objectAtIndex:indexPath.row]];
}

- (void) switchToTagesMenueDetailView:(Tagesmenue*)tm {
	UIBarButtonItem *backButton = [[UIBarButtonItem alloc] initWithTitle:@"Alle" style:UIBarButtonItemStylePlain target:nil action:nil];
	self.navigationItem.backBarButtonItem = backButton;
	[backButton release];
	
	[tmController.tableView reloadData];
	[tmController scrollToTagesmenue:tm];
	[self.navigationController pushViewController:tmController animated:YES];	
}

- (IBAction)modalViewAction:(id)sender {
	UIBarButtonItem *backButton = [[UIBarButtonItem alloc] initWithTitle:@"Zurück" style:UIBarButtonItemStylePlain target:nil action:nil];
	self.navigationItem.backBarButtonItem = backButton;
	[backButton release];
	
  	[self.navigationController pushViewController:self.settingsController animated:YES];
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
	[settingsController release];
	[tmController release];
    [super dealloc];
}


@end

