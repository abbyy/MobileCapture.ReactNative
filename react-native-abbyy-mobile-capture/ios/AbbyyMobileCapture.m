/// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
/// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

#import "AbbyyMobileCapture.h"
#import <AbbyyRtrSDK/AbbyyRtrSDK.h>
#import <React/RCTLog.h>
#import <React/RCTUtils.h>
#import "AbbyyImageCapture.h"
#import "AbbyyCoreAPI.h"
#import "NSDictionary+parseReactTypes.h"
#import "RTRPluginConstants.h"

@interface AbbyyMobileCapture ()

@property (nonatomic, strong, null_resettable) AbbyyImageCapture* imageCapture;

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

- (BOOL)createRTREngineWithSettings:(NSDictionary*)settings error:(NSError**)error
{
	if(_rtrEngine != nil) {
		return YES;
	}

	NSString* licenseFilename = settings[RTRLicenseFileNameKey];
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
		*error = [NSError errorWithDomain:RTRPluginErrorDomain code:-1 userInfo:userInfo];
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
	startImageCaptureWithSetings:(NSDictionary*)settings
	resolver:(RCTPromiseResolveBlock)resolve
	rejecter:(RCTPromiseRejectBlock)reject)
{
	[self.imageCapture startImageCaptureWithSetings:settings resolver:resolve rejecter:reject];
}

RCT_REMAP_METHOD(recognizeText,
	recognizeTextWithSettings:(NSDictionary*)settings
	resolver:(RCTPromiseResolveBlock)resolve
	rejecter:(RCTPromiseRejectBlock)reject)
{
	AbbyyCoreAPI* coreAPI = [[AbbyyCoreAPI alloc]
		initWithMobileCapture:self settings:settings resolver:resolve rejecter:reject];
	[coreAPI recognizeText];
}

RCT_REMAP_METHOD(extractData,
	extractDataWithSettings:(NSDictionary*)settings
	resolver:(RCTPromiseResolveBlock)resolve
	rejecter:(RCTPromiseRejectBlock)reject)
{
	AbbyyCoreAPI* coreAPI = [[AbbyyCoreAPI alloc]
		initWithMobileCapture:self settings:settings resolver:resolve rejecter:reject];
	[coreAPI extractData];
}

RCT_REMAP_METHOD(detectDocumentBoundary,
	detectDocumentBoundaryWithSettings:(NSDictionary*)settings
	resolver:(RCTPromiseResolveBlock)resolve
	rejecter:(RCTPromiseRejectBlock)reject)
{
	AbbyyCoreAPI* coreAPI = [[AbbyyCoreAPI alloc]
		initWithMobileCapture:self settings:settings resolver:resolve rejecter:reject];
	[coreAPI detectDocumentBoundary];
}

RCT_REMAP_METHOD(cropImage,
	cropImageWithSettings:(NSDictionary*)settings
	resolver:(RCTPromiseResolveBlock)resolve
	rejecter:(RCTPromiseRejectBlock)reject)
{
	AbbyyCoreAPI* coreAPI = [[AbbyyCoreAPI alloc]
		initWithMobileCapture:self settings:settings resolver:resolve rejecter:reject];
	[coreAPI cropImage];
}

RCT_REMAP_METHOD(rotateImage,
	rotateImageWithSettings:(NSDictionary*)settings
	resolver:(RCTPromiseResolveBlock)resolve
	rejecter:(RCTPromiseRejectBlock)reject)
{
	AbbyyCoreAPI* coreAPI = [[AbbyyCoreAPI alloc]
		initWithMobileCapture:self settings:settings resolver:resolve rejecter:reject];
	[coreAPI rotateImage];
}

RCT_REMAP_METHOD(assessQualityForOcr,
	assessQualityForOcrWithSettings:(NSDictionary*)settings
	resolver:(RCTPromiseResolveBlock)resolve
	rejecter:(RCTPromiseRejectBlock)reject)
{
	AbbyyCoreAPI* coreAPI = [[AbbyyCoreAPI alloc]
		initWithMobileCapture:self settings:settings resolver:resolve rejecter:reject];
	[coreAPI assessQualityForOcr];
}

RCT_REMAP_METHOD(exportImage,
	exportImageWithSettings:(NSDictionary*)settings
	resolver:(RCTPromiseResolveBlock)resolve
	rejecter:(RCTPromiseRejectBlock)reject)
{
	AbbyyCoreAPI* coreAPI = [[AbbyyCoreAPI alloc]
		initWithMobileCapture:self settings:settings resolver:resolve rejecter:reject];
	[coreAPI exportImage];
}

RCT_REMAP_METHOD(exportImagesToPdf,
	exportImagesToPdfWithSettings:(NSDictionary*)settings
	resolver:(RCTPromiseResolveBlock)resolve
	rejecter:(RCTPromiseRejectBlock)reject)
{
	AbbyyCoreAPI* coreAPI = [[AbbyyCoreAPI alloc]
		initWithMobileCapture:self settings:settings resolver:resolve rejecter:reject];
	[coreAPI exportImagesToPdf];
}

@end
