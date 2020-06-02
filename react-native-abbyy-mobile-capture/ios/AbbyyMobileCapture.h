/// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
/// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

#import <React/RCTBridgeModule.h>
#import <AbbyyRtrSDK/AbbyyRtrSDK.h>

NS_ASSUME_NONNULL_BEGIN

@interface AbbyyMobileCapture : NSObject <RCTBridgeModule>

- (UIViewController*)topViewController;
- (BOOL)createRTREngineWithSettings:(NSDictionary*)settings error:(NSError**)error;

@property (nonatomic, strong, nullable, readonly) RTREngine* rtrEngine;

- (void)startImageCaptureWithSetings:(NSDictionary*)settings
	resolver:(RCTPromiseResolveBlock)resolve
	rejecter:(RCTPromiseRejectBlock)reject;

- (void)recognizeTextWithSettings:(NSDictionary*)settings
	resolver:(RCTPromiseResolveBlock)resolve
	rejecter:(RCTPromiseRejectBlock)reject;

- (void)extractDataWithSettings:(NSDictionary*)settings
	resolver:(RCTPromiseResolveBlock)resolve
	rejecter:(RCTPromiseRejectBlock)reject;

- (void)detectDocumentBoundaryWithSettings:(NSDictionary*)settings
	resolver:(RCTPromiseResolveBlock)resolve
	rejecter:(RCTPromiseRejectBlock)reject;

- (void)cropImageWithSettings:(NSDictionary*)settings
	resolver:(RCTPromiseResolveBlock)resolve
	rejecter:(RCTPromiseRejectBlock)reject;

- (void)rotateImageWithSettings:(NSDictionary*)settings
	resolver:(RCTPromiseResolveBlock)resolve
	rejecter:(RCTPromiseRejectBlock)reject;

- (void)assessQualityForOcrWithSettings:(NSDictionary*)settings
	resolver:(RCTPromiseResolveBlock)resolve
	rejecter:(RCTPromiseRejectBlock)reject;

- (void)exportImageWithSettings:(NSDictionary*)settings
	resolver:(RCTPromiseResolveBlock)resolve
	rejecter:(RCTPromiseRejectBlock)reject;

- (void)exportImagesToPdfWithSettings:(NSDictionary*)settings
	resolver:(RCTPromiseResolveBlock)resolve
	rejecter:(RCTPromiseRejectBlock)reject;

@end

NS_ASSUME_NONNULL_END
