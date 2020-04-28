/// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
/// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

#import <UIKit/UIKit.h>
#import <AbbyyRtrSDK/AbbyyRtrSDK.h>
#import "RTRPluginConstants.h"

NS_ASSUME_NONNULL_BEGIN

@interface NSDictionary (rtr_Mapping)

- (UIInterfaceOrientationMask)rtr_orientationMaskForKey:(NSString*)key;

- (RTRCoreAPIExportCompressionLevel)rtr_exportCompressionLevelForKey:(NSString*)key;

- (RTRCoreAPIPdfExportCompressionType)rtr_exportCompressionTypeForKey:(NSString*)key;

+ (NSDictionary*)rtr_exportCompressionLevelToString;
+ (NSDictionary*)rtr_stringToExportCompressionLevel;

+ (NSDictionary*)rtr_exportCompressionTypeToString;
+ (NSDictionary*)rtr_stringToExportCompressionType;

+ (NSDictionary*)rtr_auiCameraResolutionToString;
+ (NSDictionary*)rtr_stringToauiCameraResolution;

+ (NSDictionary*)rtr_stringToDetectionMode;
+ (NSDictionary*)rtr_detectionModeToString;

+ (NSDictionary*)rtr_stringToExportType;
+ (NSDictionary*)rtr_exportTypeToString;

+ (NSDictionary*)rtr_stringToDestinationType;

+ (instancetype)rtr_dictionaryFromRect:(CGRect)areaOfInterest;
- (CGRect)rtr_asRect:(NSError**)error;

+ (instancetype)rtr_dictionaryFromPoint:(CGPoint)point;

+ (instancetype)rtr_imageSizeDictionaryFromSize:(CGSize)size;
+ (instancetype)rtr_resolutionDictionaryFromSize:(CGSize)size;
+ (instancetype)rtr_dictionaryFromSize:(CGSize)size;
- (BOOL)rtr_asSize:(CGSize*)size error:(NSError**)error;

+ (instancetype)rtr_dictionaryFromDataField:(RTRDataField*)dataField;
+ (instancetype)rtr_dictionaryFromTextBlock:(RTRTextBlock*)textBlock;
+ (instancetype)rtr_dictionaryFromTextLine:(RTRTextLine*)textLine;
+ (instancetype)rtr_dictionaryWithCharInfo:(RTRCharInfo*)charInfo;

@end

@interface NSString (rtr_Mapping)

+ (instancetype)rtr_stringFromStabilityStatus:(RTRResultStabilityStatus)status;
+ (instancetype)rtr_stringFromWarningCode:(RTRCallbackWarningCode)warningCode;
+ (instancetype)rtr_stringFromOcrQualityBlockType:(RTRQualityAssessmentForOCRBlockType)blockType;

@end

@interface NSArray (rtr_Mapping)

- (nullable NSArray*)rtr_tryMap:(nullable id(^)(id, NSError**))transform error:(NSError**)error;
- (NSArray*)rtr_map:(id(^)(id))transform;

/// mapping from @{x:y:} NSDictionary to NSValues with elements count check. required count: 4
- (NSArray*)rtr_transformToNSValuesQuadrangle:(NSError**)error;

/// mapping from NSValues to @{x:y:} NSDictionary with elements count check. required count: 4
- (NSArray*)rtr_transformToDictionaryQuadrangle:(NSError**)error;

@end

@interface RTRTextBlock (rtr_Text)

@property (nonatomic, readonly) NSString* rtr_text;

@end

NS_ASSUME_NONNULL_END
