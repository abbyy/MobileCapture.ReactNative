/// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
/// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

#import "AbbyyMobileCapture.h"
#import <AbbyyRtrSDK/AbbyyRtrSDK.h>
#import <React/RCTLog.h>
#import <React/RCTUtils.h>
#import "AbbyyImageCapture.h"
#import "NSDictionary+parseReactTypes.h"

@interface AbbyyMobileCapture ()

@property (nonatomic, strong, null_resettable) AbbyyImageCapture* imageCapture;
@property (nonatomic, copy) NSDictionary<NSString*, id>* settings;

@end

@implementation AbbyyMobileCapture

- (UIViewController*)topViewController
{
	UIViewController* topController = UIApplication.sharedApplication.delegate.window.rootViewController;
	NSParameterAssert(topController != nil);
	while(topController.presentedViewController != nil) {
		topController = topController.presentedViewController;
	}
	return topController;
}

- (BOOL)createRTREngine:(NSError**)error
{
	if(_rtrEngine != nil) {
		return YES;
	}

	NSString* licenseFilename = self.settings[@"licenseFileName"];
	if(licenseFilename == nil) {
		RCTLog(@"Warning license filename unknown. Default filename used.");
		licenseFilename = @"MobileCapture.License";
	}
	NSString* licensePath = [[[NSBundle mainBundle] bundlePath] stringByAppendingPathComponent:licenseFilename];
	NSData* data = [NSData dataWithContentsOfFile:licensePath];
	NSParameterAssert(data != nil);
	_rtrEngine = [RTREngine sharedEngineWithLicenseData:data];
	if(_rtrEngine == nil && error != nil ) {
		NSDictionary* userInfo = @{NSLocalizedDescriptionKey : NSLocalizedString(@"Invalid license", nil)};
		*error = [NSError errorWithDomain:@"User" code:0 userInfo:userInfo];
	}
	return _rtrEngine != nil;
}

- (AbbyyImageCapture*)imageCapture
{
	if(_imageCapture == nil) {
		_imageCapture = [[AbbyyImageCapture alloc] initWithMobileCapture:self];
	}
	return _imageCapture;
}

// To export a module named AbbyyMobileCapture
RCT_EXPORT_MODULE();

- (NSDictionary*)errorWithMessage:(NSString*)message
{
	return RCTMakeError(NSLocalizedString(message, nil), nil, nil);
}

RCT_REMAP_METHOD(startImageCapture,
                 startImageCaptureWithSetings:(NSDictionary*)settings resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
	self.settings = settings;
	[self.imageCapture startImageCaptureWithSetings:settings resolver:resolve rejecter:reject];
}

@end

