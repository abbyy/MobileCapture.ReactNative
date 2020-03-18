/// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
/// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

#import <UIKit/UIKit.h>
#import <AbbyyUI/AbbyyUI.h>

NS_ASSUME_NONNULL_BEGIN

typedef NS_ENUM(NSUInteger, AICDestintationType) {
	AICDestintationTypeFile,
	AICDestintationTypeBase64,
};

typedef NS_ENUM(NSUInteger, AICExportFormat) {
	AICExportFormatJpg,
	AICExportFormatPng,
	AICExportFormatPdf,
};

typedef NS_ENUM(NSUInteger, AICCompressionLevel) {
	AICCompressionLevelLow,
	ICCompressionLevelNormal,
	AICCompressionLevelHight,
	ICCompressionLevelExtraHight
};

@interface AbbyyImageCaptureSettings : NSObject

// UI
@property (nonatomic, readonly) BOOL showCaptureButton;
@property (nonatomic, readonly) BOOL showFlashlightButton;
@property (nonatomic, readonly) BOOL showGalleryButton;
@property (nonatomic, readonly) UIInterfaceOrientationMask captureControllerOrientationMask;

// input
@property (nonatomic, readonly) AUICameraResolution preferredResolution;
@property (nonatomic, readonly) NSUInteger requiredPageCount;
@property (nonatomic, readonly) BOOL isShowPreviewEnabled;
@property (nonatomic, readonly) CGFloat minimumDocumentToViewRatio;
@property (nonatomic, readonly) CGSize documentSize;
@property (nonatomic, readonly) CGFloat aspectRatioMin;
@property (nonatomic, readonly) CGFloat aspectRatioMax;
@property (nonatomic, readonly) NSUInteger galleryImageMaxSize;

// output
@property (nonatomic, readonly) AICDestintationType destinationType;
@property (nonatomic, readonly) AICExportFormat exportFormat;
@property (nonatomic, readonly) AICCompressionLevel compressionLevel;

- (instancetype)initWithReactSettings:(NSDictionary*)settings error:(NSError**)error;

- (NSString*)destinationString;
- (NSString*)exportFormatString;

@end

NS_ASSUME_NONNULL_END
