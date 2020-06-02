/// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
/// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

#import "AbbyyImageCapture.h"
#import "RTRImageCapturePluginAdapter.h"

#pragma mark -

@interface AbbyyImageCapture () <RTRLocalizer>

@property (nonatomic, weak) AbbyyMobileCapture* mobileCapture;
@property (nonatomic, strong, nullable) AUIMultiPageImageCaptureScenario* captureScenario;
@property (nonatomic, readonly) NSBundle* uiComponentsLocalizationBundle;

@end

@implementation AbbyyImageCapture

@synthesize uiComponentsLocalizationBundle = _uiComponentsLocalizationBundle;

- (instancetype)initWithMobileCapture:(AbbyyMobileCapture*)mobileCapture
	settings:(NSDictionary*)settings
	resolver:(RCTPromiseResolveBlock)resolve
	rejecter:(RCTPromiseRejectBlock)reject
{
	self = [super init];
	if(self != nil) {
		NSError* error = nil;
		if(mobileCapture.rtrEngine == nil && ![mobileCapture createRTREngineWithSettings:settings error:&error]) {
			NSParameterAssert(error != nil);
			reject(@(error.code).stringValue, error.localizedDescription, error);
			return nil;
		}

		_mobileCapture = mobileCapture;
		_settings = settings;
		_rctReject = reject;
		_rctResolve = resolve;
	}
	return self;
}

- (void)startImageCapture
{
	dispatch_async(dispatch_get_main_queue(), ^{
		__weak typeof(self) weakSelf = self;

		RTRImageCapturePluginAdapter* imageCapture = [[RTRImageCapturePluginAdapter alloc] initWithEngine:self.mobileCapture.rtrEngine];
		[imageCapture startImageCapture:self.settings rootController:[self.mobileCapture topViewController] localizer:self
			onError:^(NSError* error) {
				[weakSelf reactRejectWithError:error];
			} onSuccess:^(NSDictionary* result) {
				[weakSelf reactResolve:result];
			}];
	});
}

- (NSBundle*)uiComponentsLocalizationBundle
{
	if(_uiComponentsLocalizationBundle == nil) {
		if([NSBundle.mainBundle pathForResource:@"AbbyyUI" ofType:@"strings"]) {
			_uiComponentsLocalizationBundle = NSBundle.mainBundle;
		}
		_uiComponentsLocalizationBundle = [NSBundle bundleForClass:AUICaptureController.class];
		NSParameterAssert(_uiComponentsLocalizationBundle != nil);
	}
	return _uiComponentsLocalizationBundle;
}

- (void)reactResolve:(NSDictionary*)result
{
	if(self.rctResolve != nil) {
		self.rctResolve(result);
		self.rctResolve = nil;
		self.rctReject = nil;
	}
}

- (void)reactRejectWithError:(NSError*)error
{
	if(self.rctReject != nil) {
		self.rctReject(@(error.code).stringValue, error.localizedDescription, error);
		self.rctReject = nil;
		self.rctResolve = nil;
	}
}

#pragma mark - RTRLocalizer

- (NSString*)localizedStringForKey:(NSString*)key
{
	return NSLocalizedStringWithDefaultValue(key, @"MobileCapture", NSBundle.mainBundle,
		NSLocalizedStringFromTableInBundle(key, @"AbbyyUI", self.uiComponentsLocalizationBundle, @""), @"");
}

@end
