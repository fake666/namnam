//
//  TagesMenueDetailController.m
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-09-26.
//  Copyright 2010 Trading & Consulting 'H.P.C.' GmbH. All rights reserved.
//

#import "TagesMenueDetailController.h"
#import "TagesMenueTableCellController.h"
#import "Tagesmenue.h"
#import "Mensaessen.h"
#import "ModelLocator.h"

@implementation TagesMenueDetailController

@synthesize tagesmenue, tmpCell, veggie, nopork, beef, navTitle, delegate, model;

#pragma mark -
#pragma mark View lifecycle

- (id)init {
	self = [super init];
	
	model = [ModelLocator sharedInstance];
	
	self.veggie = [UIImage imageNamed:@"veggie.png"];
	self.nopork = [UIImage imageNamed:@"nopork.png"];
	self.beef = [UIImage imageNamed:@"rind.png"];
	
	return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
	
	self.tableView.rowHeight = 138.0;
	
	UISwipeGestureRecognizer *recognizer;
	recognizer = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(handleSwipeFrom:)];
	[self.view addGestureRecognizer:recognizer];
	[recognizer release];

	recognizer = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(handleSwipeFrom:)];
	recognizer.direction = UISwipeGestureRecognizerDirectionLeft;
	[self.view addGestureRecognizer:recognizer];
	[recognizer release];
	
	
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
}

- (void)handleSwipeFrom:(UISwipeGestureRecognizer *)recognizer {
	CGPoint location = self.tableView.center;
	CGSize size = self.tableView.bounds.size;

	if (recognizer.direction == UISwipeGestureRecognizerDirectionLeft) {
		location.x = size.width + (size.width / 2);
	} else {
		location.x = -(size.width / 2);
	}
	
	[UIView beginAnimations:nil context:NULL];
	[UIView setAnimationDuration:0.35];
	self.tableView.alpha = 0.0;
	self.tableView.center = location;
	[UIView commitAnimations];

    if (recognizer.direction == UISwipeGestureRecognizerDirectionLeft) {
		[delegate setNextTagesmenue:self];
		self.title = self.navTitle;
    } else {
		[delegate setPrevTagesmenue:self];		
		self.title = self.navTitle;
    }

	location.x = (size.width / 2);

	[UIView beginAnimations:nil context:NULL];
	[UIView setAnimationDuration:0.35];
	self.tableView.alpha = 1.0;
	self.tableView.center = location;
	[UIView commitAnimations];
	
}



- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
	self.title = self.navTitle;
	[self.tableView reloadData];
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

/*
- (void)viewDidDisappear:(BOOL)animated {
    [super viewDidDisappear:animated];
}
*/

// Override to allow orientations other than the default portrait orientation.
- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}



#pragma mark -
#pragma mark Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    // Return the number of sections.
    return 1;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    // Return the number of rows in the section.
    return [tagesmenue.menues count];
}


// Customize the appearance of table view cells.
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *CellIdentifier = @"Cell";
    
    TagesMenueTableCellController *cell = (TagesMenueTableCellController*)[tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
		
		[[NSBundle mainBundle] loadNibNamed:@"TagesMenueTableCellController" owner:self options:nil];
		cell = tmpCell;
		self.tmpCell = nil;
        //cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:CellIdentifier] autorelease];
    }
    
    // Configure the cell...
	Mensaessen* essen = [tagesmenue.menues objectAtIndex:indexPath.row];
    cell.titleText.text = essen.beschreibung;
	
	if(model.priceDisplayType == PRICE_DISPLAY_STUDENT) {
		cell.price.text = [[NSString alloc] initWithFormat:@"%.2f €", (essen.studentenPreis / 100.0)];
	} else if (model.priceDisplayType == PRICE_DISPLAY_NORMAL) {
		cell.price.text = [[NSString alloc] initWithFormat:@"%.2f €", (essen.preis / 100.0)];
	} else {
		cell.price.text = [[NSString alloc] initWithFormat:@"%.2f € / %.2f €", (essen.studentenPreis / 100.0), (essen.preis / 100.0)];
	}
	
	if(essen.vegetarian) {
		cell.token1.image = veggie;
		[cell.token1 sizeToFit];
	}
	if(essen.moslem) {
		if(cell.token1.image == nil) {
			cell.token1.image = nopork;
			[cell.token1 sizeToFit];
		} else {
			cell.token2.image = nopork;
			[cell.token2 sizeToFit];
		}
	}
	if(essen.beef) {
		if(cell.token1.image == nil) {
			cell.token1.image = beef;
			[cell.token1 sizeToFit];
		} else if(cell.token2.image == nil) {
			cell.token2.image = beef;
			[cell.token2 sizeToFit];
		} else {
			cell.token3.image = beef;
			[cell.token3 sizeToFit];
		}
	}
		
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
        // Delete the row from the data source
        [tableView deleteRowsAtIndexPaths:[NSArray arrayWithObject:indexPath] withRowAnimation:UITableViewRowAnimationFade];
    }   
    else if (editingStyle == UITableViewCellEditingStyleInsert) {
        // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
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
    // Navigation logic may go here. Create and push another view controller.
	/*
	 <#DetailViewController#> *detailViewController = [[<#DetailViewController#> alloc] initWithNibName:@"<#Nib name#>" bundle:nil];
     // ...
     // Pass the selected object to the new view controller.
	 [self.navigationController pushViewController:detailViewController animated:YES];
	 [detailViewController release];
	 */
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
    [super dealloc];
}


@end

