/// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
/// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>

#import "AbbyyMobileCapture.h"

NS_ASSUME_NONNULL_BEGIN

@interface AbbyyImageCapture : NSObject

- (instancetype)initWithMobileCapture:(AbbyyMobileCapture*)mobileCapture;

- (void)startImageCaptureWithSetings:(NSDictionary*)settings resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject;

@end

NS_ASSUME_NONNULL_END
