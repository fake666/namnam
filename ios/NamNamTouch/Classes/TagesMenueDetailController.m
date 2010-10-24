//
//  TagesMenueDetailController.m
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-09-26.
//  Copyright 2010 Thomas "fake" Jakobi. All rights reserved.
//

#import "TagesMenueDetailController.h"
#import "TagesMenueTableCellController.h"
#import "Mensa.h"
#import "Tagesmenue.h"
#import "Mensaessen.h"
#import "ModelLocator.h"

@implementation TagesMenueDetailController

@synthesize  tmpCell, veggie, nopork, beef,  model;

#pragma mark -
#pragma mark View lifecycle

- (id)init {
	self = [super init];
	
	model = [ModelLocator sharedInstance];
	
	self.veggie = [UIImage imageNamed:@"veggie.png"];
	self.nopork = [UIImage imageNamed:@"nopork.png"];
	self.beef = [UIImage imageNamed:@"rind.png"];

	UIBarButtonItem *currentButtonItem = [[UIBarButtonItem alloc] initWithTitle:@"Aktuell" style:UIBarButtonItemStyleBordered target:self action:@selector(scrollToCurrentTagesmenue) ];
	self.navigationItem.rightBarButtonItem = currentButtonItem;
	[currentButtonItem release];	
	
	return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
	
	self.tableView.sectionIndexMinimumDisplayRowCount=2;
	self.tableView.rowHeight = 138.0;
	self.view.userInteractionEnabled = YES;
	
	/*UIButton* currentButton = [UIButton buttonWithType:UIButtonTypeCustom];
	[currentButton addTarget:self action:@selector(scrollToCurrentTagesmenue) forControlEvents:UIControlEventTouchUpInside];
	[currentButton setTitle:@"Aktuell" forState:UIControlStateNormal];
	currentButton.frame = GCRect(0,0,100,100); */
}

- (void)scrollToTagesmenue:(Tagesmenue*)tm {
	if(tm != nil) {
		[self.tableView reloadData];
		int secIdx = [model.mensa.dayMenues indexOfObject:tm];
		NSIndexPath* indPath = [NSIndexPath indexPathForRow:0 inSection:secIdx];
		[self.tableView scrollToRowAtIndexPath:indPath atScrollPosition:UITableViewScrollPositionTop animated:YES];	
	}
}

- (void)scrollToCurrentTagesmenue {
	[self scrollToTagesmenue:[model closestDayMenue]];
}

/*
- (void)handleSwipeFrom:(UISwipeGestureRecognizer *)recognizer {
	
	int curIdx = [model.mensa.dayMenues indexOfObject:tagesmenue];

	CGPoint location = self.tableView.center;
	CGSize size = self.tableView.bounds.size;

	if (recognizer.direction == UISwipeGestureRecognizerDirectionLeft) {
		if((curIdx + 1) >= [model.mensa.dayMenues count]) return;
		
		location.x = size.width + (size.width / 2);
	} else {
		if(curIdx == 0) return;
		
		location.x = -(size.width / 2);
	}
	
	[UIView beginAnimations:nil context:NULL];
	[UIView setAnimationDuration:0.35];
	self.tableView.alpha = 0.0;
	self.tableView.center = location;
	[UIView commitAnimations];

    if (recognizer.direction == UISwipeGestureRecognizerDirectionLeft) {
		[delegate setNextTagesmenue:tagesmenue];
		self.title = self.navTitle;
    } else {
		[delegate setPrevTagesmenue:tagesmenue];		
		self.title = self.navTitle;
    }

	location.x = (size.width / 2);

	[UIView beginAnimations:nil context:NULL];
	[UIView setAnimationDuration:0.35];
	self.tableView.alpha = 1.0;
	self.tableView.center = location;
	[UIView commitAnimations];
	
}*/



- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
	[self.tableView reloadData];
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
	if(model.appWasInBackGround) {
		[self.tableView reloadData];
		[self scrollToTagesmenue:[model closestDayMenue]];
	}
}


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
    return [model.mensa.dayMenues count];
}

- (NSArray *)sectionIndexTitlesForTableView:(UITableView *)tableView {
	// returns the array of section titles. There is one entry for each unique character that an element begins with
	// [A,B,C,D,E,F,G,H,I,K,L,M,N,O,P,R,S,T,U,V,X,Y,Z]
	return [model.mensa dayMenuIndexArray];
}

- (NSInteger)tableView:(UITableView *)tableView sectionForSectionIndexTitle:(NSString *)title atIndex:(NSInteger)index {
	return index;
}



- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    // Return the number of rows in the section.
    return [[[model.mensa.dayMenues objectAtIndex:section] menues]  count];
}

- (NSString *)tableView:(UITableView *)aTableView titleForHeaderInSection:(NSInteger)section {
	// Section title is the region name
	Tagesmenue *tm = [model.mensa.dayMenues objectAtIndex:section];
	return [model getNiceDate:tm.tag];
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
	Tagesmenue* tm = [model.mensa.dayMenues objectAtIndex:indexPath.section];
	Mensaessen* essen = [tm.menues objectAtIndex:indexPath.row];
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

