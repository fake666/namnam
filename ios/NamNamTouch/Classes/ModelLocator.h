//
//  ModelLocator.h
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-10-10.
//  Copyright 2010 Trading & Consulting 'H.P.C.' GmbH. All rights reserved.
//

#import <Foundation/Foundation.h>

#define PRICE_DISPLAY_BOTH 0
#define PRICE_DISPLAY_STUDENT 1
#define PRICE_DISPLAY_NORMAL 2

@class Mensa, MensaURL, Tagesmenue;

@interface ModelLocator : NSObject {

	BOOL appWasInBackGround; // scroll to current day if on ios 4+ on iphone 3gs/4 (TODO XXX)
	
	MensaURL* mensaURL; // the mensa we are currently using;
	NSArray* mensen; // the mensae we know
	Mensa* mensa; // the loaded mensa, either restored from local cache or freshly loaded from the net
	
	NSInteger priceDisplayType; // how to display prices
}

@property BOOL appWasInBackGround;

@property(nonatomic, retain) MensaURL *mensaURL;
@property(nonatomic, retain) NSArray *mensen;
@property(nonatomic, retain) Mensa *mensa;
@property NSInteger priceDisplayType;

- (void) loadSettings;
- (void) loadMensae;
- (void) saveSettings;
- (void) saveData;
- (void) loadData;

- (Tagesmenue*)closestDayMenue;

+ (ModelLocator*)sharedInstance;
+ (id)allocWithZone:(NSZone *)zone;
- (id)copyWithZone:(NSZone *)zone;
- (id)retain;
- (unsigned)retainCount;
- (void)release;
- (id)autorelease;


@end
