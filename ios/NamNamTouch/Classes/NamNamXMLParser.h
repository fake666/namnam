#import <UIKit/UIKit.h>

@class NamNamXMLParser, Mensa, Tagesmenue, Mensaessen,ModelLocator;

// Protocol for the parser to communicate with its delegate.
@protocol NamNamXMLParserDelegate <NSObject>

@optional
// Called by the parser when parsing is finished.
- (void)parserDidEndParsingData:(NamNamXMLParser *)parser;
// Called by the parser in the case of an error.
- (void)parser:(NamNamXMLParser *)parser didFailWithError:(NSError *)error;
@end

#if __IPHONE_OS_VERSION_MIN_REQUIRED >= __IPHONE_4_0
#else
@protocol NSXMLParserDelegate
@end
#endif

@interface NamNamXMLParser : NSObject <NSXMLParserDelegate> {
    id <NamNamXMLParserDelegate> delegate;
    Mensa *parsedMensa;
	
	ModelLocator* model;
	
	BOOL parseErrorOccurred;
	
	Tagesmenue* currentTagesmenue;
	NSMutableArray* currentDayMenues;
	Mensaessen* currentMensaessen;
	NSMutableArray* currentMenues;
	
	NSDateFormatter *dateFormatter;
	NSNumberFormatter *numberFormatter;
    NSMutableData *xmlData;
	BOOL done;
	BOOL storingCharacters;
    NSURLConnection *connection;
	NSMutableString* currentString;
	
	NSAutoreleasePool *downloadAndParsePool;
}

@property (nonatomic, assign) id <NamNamXMLParserDelegate> delegate;
@property (nonatomic, retain) Mensa *parsedMensa;
@property (nonatomic, retain) ModelLocator *model;
@property (nonatomic, retain) NSDateFormatter *dateFormatter;
@property (nonatomic, retain) NSNumberFormatter *numberFormatter;
@property (nonatomic, retain) NSMutableData *xmlData;
@property BOOL done;
@property BOOL storingCharacters;
@property (nonatomic, retain) NSURLConnection *connection;
@property (nonatomic, retain) NSString *currentString;
@property (nonatomic, retain) Tagesmenue* currentTagesmenue;
@property (nonatomic, retain) Mensaessen* currentMensaessen;
@property BOOL parseErrorOccurred;
@property (nonatomic, assign) NSAutoreleasePool *downloadAndParsePool;

@property (nonatomic, retain) NSMutableArray *currentDayMenues;
@property (nonatomic, retain) NSMutableArray *currentMenues;

- (void)start;

// Subclasses must implement this method. It will be invoked on a secondary thread to keep the application responsive.
// Although NSURLConnection is inherently asynchronous, the parsing can be quite CPU intensive on the device, so
// the user interface can be kept responsive by moving that work off the main thread. This does create additional
// complexity, as any code which interacts with the UI must then do so in a thread-safe manner.
- (void)downloadAndParse:(NSURL *)url;

// Subclasses should invoke these methods and let the superclass manage communication with the delegate.
// Each of these methods must be invoked on the main thread.
- (void)downloadStarted;
- (void)downloadEnded;
- (void)parseEnded:(Mensa*) mensa;
- (void)parseError:(NSError *)error;

@end
