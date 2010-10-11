//
//  ModelLocator.h
//  NamNamTouch
//
//  Created by Thomas "fake" Jakobi on 2010-10-10.
//  Copyright 2010 Trading & Consulting 'H.P.C.' GmbH. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "NamNamXMLParser.h"

#define PRICE_DISPLAY_BOTH 0
#define PRICE_DISPLAY_STUDENT 1
#define PRICE_DISPLAY_NORMAL 2

@class Mensa, MensaURL, Tagesmenue;

@protocol NamNamModelLocatorDelegate <NSObject>

@optional
// Called by the parser when parsing is finished.
- (void)loadingFinished;
- (void)loadingFailed;
- (void)mensaNameKnown:(NSString*)name;
@end

@interface ModelLocator : NSObject <NamNamXMLParserDelegate> {

	NamNamXMLParser* parser;
	id<NamNamModelLocatorDelegate> delegate;
	
	BOOL appWasInBackGround; // scroll to current day if on ios 4+ on iphone 3gs/4 (TODO XXX)
	
	MensaURL* mensaURL; // the mensa we are currently using;
	NSArray* mensen; // the mensae we know
	Mensa* mensa; // the loaded mensa, either restored from local cache or freshly loaded from the net
	
	NSInteger priceDisplayType; // how to display prices
	
	UIActivityIndicatorView* activity; // a generic indicator to show activty
}

@property BOOL appWasInBackGround;

@property(assign) id<NamNamModelLocatorDelegate> delegate;
@property(retain) NamNamXMLParser *parser;
@property(retain) MensaURL *mensaURL;
@property(retain) NSArray *mensen;
@property(retain) Mensa *mensa;
@property NSInteger priceDisplayType;
@property(retain) UIActivityIndicatorView *activity;

- (void) loadSettings;
- (void) loadMensae;
- (void) saveSettings;
- (void) saveData;
- (void) loadData;
- (void) fetchMensaData;

- (void)parserDidEndParsingData:(NamNamXMLParser *)theparser;
- (void)parser:(NamNamXMLParser *)parser didFailWithError:(NSError *)error;

- (Tagesmenue*)closestDayMenue;

+ (ModelLocator*)sharedInstance;
+ (id)allocWithZone:(NSZone *)zone;
- (id)copyWithZone:(NSZone *)zone;
- (id)retain;
- (unsigned)retainCount;
- (void)release;
- (id)autorelease;


@end
