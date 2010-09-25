//
//  RootViewController.m
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-09-25.
//  Copyright 2010 Trading & Consulting 'H.P.C.' GmbH. All rights reserved.
//

#import "RootViewController.h"
#import "Mensa.h"
#import "Tagesmenue.h"
#import "TagesMenueDetailController.h"

@implementation RootViewController

@synthesize parser, mensa, dateFormatter;
#pragma mark -
#pragma mark View lifecycle


#pragma mark <NamNamXMLParserDelegate> Implementation

- (void)parserDidEndParsingData:(NamNamXMLParser *)theparser {
	self.mensa = theparser.parsedMensa;
	
	NSDateFormatter* formatter = [[NSDateFormatter alloc] init];
	[formatter setDateStyle:NSDateFormatterShortStyle];
	[formatter setTimeStyle:NSDateFormatterNoStyle];
	[formatter setLocale:[[[NSLocale alloc] initWithLocaleIdentifier:@"DE"] autorelease]];
	[formatter setDateFormat:@"dd.MM."];
	
	
	self.title = [NSString stringWithFormat:@"%@ - %@", [formatter stringFromDate:self.mensa.firstDate], [formatter stringFromDate:self.mensa.lastDate]];
    [self.tableView reloadData];
	
	// TODO: scroll to today or the nearest upcoming date if available or the latest date or die ^^
	NSIndexPath* indPath = [NSIndexPath indexPathForRow:10 inSection:0];
	[self.tableView scrollToRowAtIndexPath:indPath atScrollPosition:UITableViewScrollPositionMiddle animated:YES];	
	
    //self.navigationItem.rightBarButtonItem.enabled = YES;
    self.parser = nil;
}

- (void)parser:(NamNamXMLParser *)parser didFailWithError:(NSError *)error {
    // handle errors as appropriate to your application...
}



- (void)viewDidLoad {
    [super viewDidLoad];

	dateFormatter = [[NSDateFormatter alloc] init];
	[dateFormatter setDateStyle:NSDateFormatterLongStyle];
	[dateFormatter setTimeStyle:NSDateFormatterNoStyle];
	[dateFormatter setLocale:[[[NSLocale alloc] initWithLocaleIdentifier:@"DE"] autorelease]];
	
	parser = [[NamNamXMLParser alloc] init];
	parser.delegate = self;
	
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    //self.navigationItem.rightBarButtonItem = self.editButtonItem;
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
    
	 TagesMenueDetailController *detailViewController = [[TagesMenueDetailController alloc] init];
     Tagesmenue* t = [self.mensa.dayMenues objectAtIndex:indexPath.row];
	 detailViewController.tagesmenue = t;
     // Pass the selected object to the new view controller.
	 [self.navigationController pushViewController:detailViewController animated:YES];
	 [detailViewController release];
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
	[mensa release];
	[parser release];
    [super dealloc];
}


@end

