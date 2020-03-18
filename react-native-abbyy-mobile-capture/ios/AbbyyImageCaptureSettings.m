/// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
/// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

#import "AbbyyImageCaptureSettings.h"
#import "NSDictionary+parseReactTypes.h"
#import <AbbyyUI/AbbyyUI.h>
#import <React/RCTLog.h>

@implementation AbbyyImageCaptureSettings

- (instancetype)initWithReactSettings:(NSDictionary*)settings error:(NSError**)error;
{
	self = [super init];
	if(self != nil) {
		[self parseReactSettings:settings error:error];
		if(*error != nil) {
			return nil;
		}
	}
	return self;
}

- (void)parseReactSettings:(NSDictionary*)settings error:(NSError**)error;
{
	[self parseInputSettings:settings error:error];
	[self parseOutputSettings:settings error:error];

	if(error != nil && *error == nil) {
		[self checkSettingsValid:error];
	}
}

- (void)parseInputSettings:(NSDictionary*)input  error:(NSError**)error;
{
	_showFlashlightButton = [input rtr_parseReactBool:@"isFlashlightButtonVisible" defaultValue:YES error:error];
	_showCaptureButton = [input rtr_parseReactBool:@"isCaptureButtonVisible" defaultValue:YES error:error];
	_showGalleryButton = [input rtr_parseReactBool:@"isGalleryButtonVisible" defaultValue:YES error:error];
	[self parseOrientation:input error:error];

	_preferredResolution = [input rtr_parseReactEnum:@"cameraResolution" variants:@[@"HD", @"FullHD", @"UHD_4K"] defaultValue:1 error:error];
	_requiredPageCount = [input rtr_parseReactInt:@"requiredPageCount" defaultValue:0 error:error];
	_isShowPreviewEnabled = [input rtr_parseReactBool:@"isShowPreviewEnabled" defaultValue:NO error:error];

	NSDictionary* imageSettings = input[@"defaultImageSettings"];
	_minimumDocumentToViewRatio =  [imageSettings rtr_parseReactFloat:@"minimumDocumentToViewRatio" defaultValue:0.15 error:error];
	_documentSize = [self documentSizeFromString:imageSettings[@"documentSize"] error:error];
	_aspectRatioMin = [imageSettings rtr_parseReactFloat:@"aspectRationMin" defaultValue:0. error:error];
	_aspectRatioMax = [imageSettings rtr_parseReactFloat:@"aspectRationMax" defaultValue:0. error:error];
	_galleryImageMaxSize = [imageSettings rtr_parseReactInt:@"imageFromGalleryMaxSize" defaultValue:4096 error:error];
}

- (void)parseOrientation:(NSDictionary*)input error:(NSError**)error
{
	NSInteger variant = [input rtr_parseReactEnum:@"orientation" variants:@[@"Default", @"Portrait", @"Landscape"] defaultValue:0 error:error];
	switch (variant) {
		case 1:
			_captureControllerOrientationMask = UIInterfaceOrientationMaskPortrait | UIInterfaceOrientationMaskPortraitUpsideDown;
			break;

		case 2:
			_captureControllerOrientationMask = UIInterfaceOrientationMaskLandscape;
			break;

		default:
			_captureControllerOrientationMask = UIInterfaceOrientationMaskAll;
			break;
	}
}

- (void)parseOutputSettings:(NSDictionary*)output error:(NSError**)error;
{
	_destinationType = [output rtr_parseReactEnum:@"destination" variants:@[@"File", @"Base64"] defaultValue:0 error:error];
	_exportFormat = [output rtr_parseReactEnum:@"exportType" variants:@[@"Jpg", @"Png", @"Pdf"] defaultValue:0 error:error];
	_compressionLevel = [output rtr_parseReactEnum:@"compressionLevel" variants:@[@"Low", @"Normal", @"High", @"ExtraHigh"] defaultValue:1 error:error];
}

- (BOOL)checkSettingsValid:(NSError**)error
{
	NSString* description;
	if(self.destinationType == AICDestintationTypeBase64 && self.requiredPageCount != 1) {
		description = [NSString stringWithFormat:@"Base64 works only with requiredPageCount equals to 1"];
	}
	if(self.destinationType == AICDestintationTypeBase64 && self.exportFormat != AICExportFormatJpg) {
		description = [NSString stringWithFormat:@"Base64 works only with Jpg export format"];
	}
	if(self.minimumDocumentToViewRatio < 0 || self.minimumDocumentToViewRatio > 1) {
		description = [NSString stringWithFormat:@"Min document to view ratio should be from 0 to 1"];
	}
	if(self.aspectRatioMin < self.aspectRatioMax) {
		description = [NSString stringWithFormat:@"Aspect ratio min should be less or equal aspect ratio max"];
	}
	if(description != nil) {
		NSDictionary* userInfo = @{NSLocalizedDescriptionKey: description};
		*error = [NSError errorWithDomain:@"ABBYY" code:-1 userInfo:userInfo];
		return NO;
	}
	return YES;
}

- (CGSize)documentSizeFromString:(NSString*)string error:(NSError**)error
{
	static NSDictionary* predefined = nil;
	if(predefined == nil) {
		predefined = @{
			@"Any": @(AUIDocumentSizeAny),
			@"A4": @(AUIDocumentSizeA4),
			@"BusinessCard": @(AUIDocumentSizeBusinessCard),
			@"Letter": @(AUIDocumentSizeLetter),
		};
	}
	if(string.length == 0) {
		return AUIDocumentSizeAny;
	}
	if([predefined valueForKey:string] != nil) {
		return [predefined[string] CGSizeValue];
	} else {
		NSCharacterSet* separators = [NSCharacterSet characterSetWithCharactersInString:@"Xx/ "];
		NSArray<NSString*>* parts = [string componentsSeparatedByCharactersInSet:separators];
		float nom = 0;
		float denom = 0;
		BOOL badValue = NO;
		if(parts.count == 2) {
			badValue = ![[NSScanner scannerWithString:parts.firstObject] scanFloat:&nom];
			if(!badValue) {
				badValue &= ![[NSScanner scannerWithString:parts.lastObject] scanFloat:&denom];
			}
		} else {
			badValue = YES;
		}
		if(!badValue) {
			return CGSizeMake(nom, denom);
		} else {
			NSString* description = @"Failed to parse document size.";
			RCTLog(@"%@", description);
			if(error != nil) {
				NSDictionary* userInfo = @{NSLocalizedDescriptionKey: description};
				*error = [NSError errorWithDomain:@"ABBYY" code:-1 userInfo:userInfo];
			}
			return AUIDocumentSizeAny;
		}
	}
}

- (NSString*)destinationString
{
	if(self.destinationType == AICDestintationTypeFile) {
		return @"File";
	}
	return @"Base64";
}

- (NSString*)exportFormatString
{
	switch(self.exportFormat) {
		case AICExportFormatJpg:
			return @"Jpg";

		case AICExportFormatPng:
			return @"Png";

		case AICExportFormatPdf:
			break;
	}

	return @"Pdf";
}

@end
