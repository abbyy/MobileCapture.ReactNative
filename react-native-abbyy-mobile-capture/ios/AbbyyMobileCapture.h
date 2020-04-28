/// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
/// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

#import <React/RCTBridgeModule.h>
#import <AbbyyRtrSDK/AbbyyRtrSDK.h>

NS_ASSUME_NONNULL_BEGIN

@interface AbbyyMobileCapture : NSObject <RCTBridgeModule>

- (UIViewController*)topViewController;
- (BOOL)createRTREngineWithSettings:(NSDictionary*)settings error:(NSError**)error;

@property (nonatomic, strong, nullable, readonly) RTREngine* rtrEngine;

@end

NS_ASSUME_NONNULL_END
