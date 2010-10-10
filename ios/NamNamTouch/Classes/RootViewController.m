//
//  RootViewController.m
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-09-25.
//  Copyright 2010 Trading & Consulting 'H.P.C.' GmbH. All rights reserved.
//

#import "RootViewController.h"
#import "NamNamSettingsController.h"
#import "Mensa.h"
#import "MensaURL.h"
#import "Tagesmenue.h"
#import "TagesMenueDetailController.h"

@implementation RootViewController

@synthesize parser, mensa, dateFormatter, settingsController, tmController;
#pragma mark -
#pragma mark View lifecycle


#pragma mark <NamNamXMLParserDelegate> Implementation

- (void)parserDidEndParsingData:(NamNamXMLParser *)theparser {
	self.mensa = theparser.parsedMensa;
	
	if(mensa.dayMenues.count <= 0) {
		UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Keine Einträge" 
														message:@"Kein Essen gefunden! :("
													   delegate:nil 
											  cancelButtonTitle:@"Einstellungen" 
											  otherButtonTitles: nil];
		[alert show];
		[alert release];
		
		// XXX TODO show settings screen here
	} else {
		
		NSDateFormatter* formatter = [[NSDateFormatter alloc] init];
		[formatter setDateStyle:NSDateFormatterShortStyle];
		[formatter setTimeStyle:NSDateFormatterNoStyle];
		[formatter setLocale:[[[NSLocale alloc] initWithLocaleIdentifier:@"DE"] autorelease]];
		[formatter setDateFormat:@"dd.MM."];
		
		
		self.title = settingsController.mensaSelection.name;
		[self.tableView reloadData];
		
		int nearestIdx = -1;
		long nearestEntryDistance = -1;
		for(int n = 0; n < mensa.dayMenues.count; n++) {
			Tagesmenue* cur = [mensa.dayMenues objectAtIndex:n];
			
			long dist = [cur.tag timeIntervalSinceNow];
			if(nearestEntryDistance < 0 || dist < nearestEntryDistance) {
				nearestEntryDistance = dist;
				nearestIdx = n;
			}
		}
		
		if(nearestIdx >= 0) {			
			NSIndexPath* indPath = [NSIndexPath indexPathForRow:nearestIdx inSection:0];
			[self.tableView scrollToRowAtIndexPath:indPath atScrollPosition:UITableViewScrollPositionMiddle animated:YES];	
		}

		self.parser = nil;
	}	
}

- (void)parser:(NamNamXMLParser *)parser didFailWithError:(NSError *)error {
	UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Fehler" 
                                                    message:[error localizedDescription]
                                                   delegate:nil 
                                          cancelButtonTitle:@"Einstellungen" 
                                          otherButtonTitles: nil];
    [alert show];
    [alert release];
	
	// XXX TODO show settings screen here
}



- (void)viewDidLoad {
    [super viewDidLoad];
	
	if (self.settingsController == nil)
        self.settingsController = [[[NamNamSettingsController alloc] initWithNibName:
									NSStringFromClass([NamNamSettingsController class]) bundle:nil] autorelease];
	settingsController.delegate = self;
	[settingsController loadSettings];
	
	UIButton* modalViewButton = [UIButton buttonWithType:UIButtonTypeInfoLight];
	[modalViewButton addTarget:self action:@selector(modalViewAction:) forControlEvents:UIControlEventTouchUpInside];
	UIBarButtonItem *modalBarButtonItem = [[UIBarButtonItem alloc] initWithCustomView:modalViewButton];
	self.navigationItem.rightBarButtonItem = modalBarButtonItem;
	[modalBarButtonItem release];
	

	dateFormatter = [[NSDateFormatter alloc] init];
	[dateFormatter setDateStyle:NSDateFormatterLongStyle];
	[dateFormatter setTimeStyle:NSDateFormatterNoStyle];
	[dateFormatter setLocale:[[[NSLocale alloc] initWithLocaleIdentifier:@"DE"] autorelease]];
	
	parser = [[NamNamXMLParser alloc] init];
	parser.url = self.settingsController.mensaSelection.url;
	parser.delegate = self;
	
	[parser start];
}

- (void)mensaChanged:(MensaURL *)mensaUrl {
	[parser release];
	parser = [[NamNamXMLParser alloc] init];
	parser.url = mensaUrl.url;
	parser.delegate = self;
	
	[parser start];
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
	return YES; // (interfaceOrientation == UIInterfaceOrientationPortrait);
}



#pragma mark -
#pragma mark Table view data source

// Customize the number of sections in the table view.
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}


// Customize the number of rows in the table view.
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [self.mensa.dayMenues count];
}


// Customize the appearance of table view cells.
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier] autorelease];
    }
    
	// Configure the cell.
	Tagesmenue* t = [self.mensa.dayMenues objectAtIndex:indexPath.row];
	cell.textLabel.text = [self transformedValue:t.tag];
		
    return cell;
}


- (NSString*)transformedValue:(NSDate *)date
{
	// Initialize the calendar and flags.
	unsigned unitFlags = NSYearCalendarUnit | NSMonthCalendarUnit |  NSDayCalendarUnit | NSWeekdayCalendarUnit;
	NSCalendar *calendar = [NSCalendar currentCalendar];
	
	// Create reference date for supplied date.
	NSDateComponents *comps = [calendar components:unitFlags fromDate:date];
	[comps setHour:0];
	[comps setMinute:0];
	[comps setSecond:0];
	NSDate *suppliedDate = [calendar dateFromComponents:comps];
	
	// Iterate through the eight days (tomorrow, today, and the last six).
	int i;
	for (i = -1; i < 7; i++)
	{
		// Initialize reference date.
		comps = [calendar components:unitFlags fromDate:[NSDate date]];
		[comps setHour:0];
		[comps setMinute:0];
		[comps setSecond:0];
		[comps setDay:[comps day] - i];
		NSDate *referenceDate = [calendar dateFromComponents:comps];
		// Get week day (starts at 1).
		int weekday = [[calendar components:unitFlags fromDate:referenceDate] weekday] - 1;
		
		if ([suppliedDate compare:referenceDate] == NSOrderedSame && i == -1)
		{
			// Tomorrow
			return [NSString stringWithString:@"Morgen"];
		}
		else if ([suppliedDate compare:referenceDate] == NSOrderedSame && i == 0)
		{
			return [NSString stringWithString:@"Heute"];
		}
		else if ([suppliedDate compare:referenceDate] == NSOrderedSame && i == 1)
		{
			return [NSString stringWithString:@"Gestern"];
		}
		else if ([suppliedDate compare:referenceDate] == NSOrderedSame)
		{
			// Day of the week
			NSString *day = [[dateFormatter weekdaySymbols] objectAtIndex:weekday];
			return day;
		}
	}
	
	// It's not in those eight days.
	NSString *defaultDate = [dateFormatter stringFromDate:date];
	return defaultDate;
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
	
	UIBarButtonItem *backButton = [[UIBarButtonItem alloc] initWithTitle:@"Alle" style:UIBarButtonItemStylePlain target:nil action:nil];
	self.navigationItem.backBarButtonItem = backButton;
	[backButton release];
	
	if(self.tmController == nil) {
		self.tmController = [[TagesMenueDetailController alloc] init];
		self.tmController.settingsController = self.settingsController;
		self.tmController.delegate = self;
	}
	Tagesmenue* t = [self.mensa.dayMenues objectAtIndex:indexPath.row];
	tmController.tagesmenue = t;
	tmController.navTitle = [self transformedValue:t.tag];
	[self.navigationController pushViewController:tmController animated:YES];
}


- (void)setNextTagesmenue:(TagesMenueDetailController *)view {
	NSIndexPath* selected = [self.tableView indexPathForSelectedRow];
	
	if((selected.row + 1) >= [self.mensa.dayMenues count]) return;
	
	NSIndexPath* indPath = [NSIndexPath indexPathForRow:(selected.row + 1) inSection:0];
	[self.tableView selectRowAtIndexPath:indPath animated:NO scrollPosition:UITableViewScrollPositionMiddle ];	

	Tagesmenue* t = [self.mensa.dayMenues objectAtIndex:indPath.row];
	tmController.tagesmenue = t;
	tmController.navTitle = [self transformedValue:t.tag];
	[tmController.tableView reloadData];
}

- (void)setPrevTagesmenue:(TagesMenueDetailController *)view {
	NSIndexPath* selected = [self.tableView indexPathForSelectedRow];

	if(selected.row == 0) return;
	
	NSIndexPath* indPath = [NSIndexPath indexPathForRow:(selected.row-1) inSection:0];
	[self.tableView selectRowAtIndexPath:indPath animated:NO scrollPosition:UITableViewScrollPositionMiddle ];	
	
	Tagesmenue* t = [self.mensa.dayMenues objectAtIndex:indPath.row];	
	tmController.tagesmenue = t;
	tmController.navTitle = [self transformedValue:t.tag];
	[tmController.tableView reloadData];
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
	[dateFormatter release];
	[settingsController release];
	[tmController release];
	[mensa release];
	[parser release];
    [super dealloc];
}


@end

