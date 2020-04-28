/// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
/// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>

#import "AbbyyMobileCapture.h"

NS_ASSUME_NONNULL_BEGIN

@interface AbbyyCoreAPI : NSObject

@property (nonatomic, copy, nullable) NSDictionary* settings;
@property (nonatomic, copy, nullable) RCTPromiseResolveBlock rctResolve;
@property (nonatomic, copy, nullable) RCTPromiseRejectBlock rctReject;

- (instancetype)initWithMobileCapture:(AbbyyMobileCapture*)mobileCapture
	settings:(NSDictionary*)settings
	resolver:(RCTPromiseResolveBlock)resolve
	rejecter:(RCTPromiseRejectBlock)reject;

- (void)recognizeText;

- (void)extractData;

- (void)detectDocumentBoundary;

- (void)cropImage;

- (void)rotateImage;

- (void)assessQualityForOcr;

- (void)exportImage;

- (void)exportImagesToPdf;

@end

NS_ASSUME_NONNULL_END
