/// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
/// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

#import "AbbyyImageCapture.h"
#import <React/RCTLog.h>
#import <React/RCTUtils.h>
#import <AbbyyUI/AbbyyUI.h>
#import <AbbyyRtrSDK/AbbyyRtrSDK.h>
#import "AbbyyImageCaptureSettings.h"
#import "RTRCoreApiPluginAdapter.h"

@interface AbbyyImageCaptureController: AUICaptureController
@property (nonatomic, assign) UIInterfaceOrientationMask orientationMask;
@end

@implementation AbbyyImageCaptureController

- (instancetype) initWithOrientationMask:(UIInterfaceOrientationMask)orientationMask
{
	self = [super init];
	if(self) {
		self.orientationMask = orientationMask;
	}
	return self;
}

- (UIInterfaceOrientationMask) supportedInterfaceOrientations
{
	return self.orientationMask;
}

- (BOOL)shouldAutorotate
{
	return YES;
}

@end

#pragma mark -
@interface AbbyyImageCapture () <AUIMultiPageImageCaptureScenarioDelegate, AUIMultiPageCaptureSettings>
	@property (nonatomic, weak) AbbyyMobileCapture* mobileCapture;
	@property (nonatomic, strong, nullable) AUIMultiPageImageCaptureScenario* captureScenario;
	@property (nonatomic, strong, nullable) AbbyyImageCaptureSettings* settings;
	@property (nonatomic, copy, nullable) RCTPromiseResolveBlock rctResolve;
	@property (nonatomic, copy, nullable) RCTPromiseRejectBlock rctReject;
	@property (nonatomic, weak) AUICaptureController* captureController;
	@property (nonatomic, readonly) NSBundle* uiComponentsLocalizationBundle;
@end

@implementation AbbyyImageCapture

@synthesize uiComponentsLocalizationBundle = _uiComponentsLocalizationBundle;

- (instancetype)initWithMobileCapture:(AbbyyMobileCapture*)mobileCapture
{
	self = [super init];
	if(self != nil) {
		_mobileCapture = mobileCapture;
	}
	return self;
}

- (void)startImageCaptureWithSetings:(NSDictionary*)settings resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject
{
	NSError* error;
	self.rctResolve = resolve;
	self.rctReject = reject;

	if(self.mobileCapture.rtrEngine == nil && ![self.mobileCapture createRTREngineWithSettings:settings error:&error]) {
		NSParameterAssert(error != nil);
		[self reactRejectWithError:error];
		return;
	}

	self.settings = [[AbbyyImageCaptureSettings alloc] initWithReactSettings:settings error:&error];
	if(self.settings == nil) {
		NSParameterAssert(error != nil);
		[self reactRejectWithError:error];
		return;
	}

	self.captureScenario = nil;
	if(![self createCaptureScenario:&error]) {
		NSParameterAssert(error != nil);
		[self reactRejectWithError:error];
		return;
	}

	[self presentCaptureController];
}

- (BOOL)createCaptureScenario:(NSError**)error
{
	NSString* storagePath = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES).firstObject;
	_captureScenario = [[AUIMultiPageImageCaptureScenario alloc]
		initWithEngine:self.mobileCapture.rtrEngine
		storagePath:storagePath
		error:error];

	_captureScenario.delegate = self;
	_captureScenario.captureSettings = self;

	return _captureScenario != nil;
}

- (AUIMultiPageImageCaptureScenario*)captureScenario
{
	if(_captureScenario == nil && self.mobileCapture.rtrEngine != nil) {
		[self createCaptureScenario:nil];
	}
	return _captureScenario;
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

- (void)presentCaptureController
{
	dispatch_async(dispatch_get_main_queue(), ^{
		NSParameterAssert(self.captureController == nil);
		AUICaptureController* controller = [[AbbyyImageCaptureController alloc] initWithOrientationMask:self.settings.captureControllerOrientationMask];
		AUICaptureController.localizedStringsBundle = self.uiComponentsLocalizationBundle;
		controller.modalPresentationStyle = UIModalPresentationFullScreen;
		controller.flashButton.hidden = !self.settings.showFlashlightButton;
		controller.captureButton.hidden = !self.settings.showCaptureButton;
		controller.galleryButton.hidden = !self.settings.showGalleryButton;
		controller.cameraSettings.preferredResolution = self.settings.preferredResolution;

		NSParameterAssert(controller != nil);
		self.captureController = controller;
		if(self.captureScenario != nil) {
			[self applyCaptureScenarioSettings];
			controller.captureScenario = self.captureScenario;
		}

		[[self.mobileCapture topViewController] presentViewController:controller animated:YES completion:nil];

		[UIApplication.sharedApplication setStatusBarHidden:YES animated:YES];
	});
}

- (void)applyCaptureScenarioSettings
{
	AUIMultiPageImageCaptureScenario* scenario = self.captureScenario;
	scenario.requiredPageCount = self.settings.requiredPageCount;
	scenario.isShowPreviewEnabled = self.settings.isShowPreviewEnabled;
}

- (void)dismmissCaptureControllerWithCompletion:(void(^)(void))completion
{
	NSParameterAssert(self.captureController != nil);
	[self.captureController.presentingViewController dismissViewControllerAnimated:YES completion:completion];
	[UIApplication.sharedApplication setStatusBarHidden:NO animated:YES];
}

- (void)reactResolve:(NSDictionary*)result
{
	NSError* error;
	[self deleteAllImages:&error];

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

- (void)handleCaptureResult:(id<AUIMultiPageImageCaptureResult>)result
{
	__weak typeof(self) wSelf = self;
	[self exportResult:result withCompletion:^(NSDictionary* reactResult, NSError* error) {
		[wSelf dismmissCaptureControllerWithCompletion:^{
			__strong typeof(self) sSelf = wSelf;
			if(error != nil) {
				[sSelf reactRejectWithError:error];
			} else {
				[sSelf reactResolve:reactResult];
			}
		}];
	}];
}

- (BOOL)deleteAllImages:(NSError**)error
{
	id<AUIMultiPageImageCaptureResult> result = self.captureScenario.result;
	return [result clearWithError:error];
}

#pragma mark - AUIMultiPageImageCaptureScenarioDelegate
- (void)captureScenario:(AUIMultiPageImageCaptureScenario*)captureScenario didFinishWithResult:(id<AUIMultiPageImageCaptureResult>)result
{
	[self handleCaptureResult:result];
}

static NSString* localized(NSString* string)
{
	return NSLocalizedStringFromTableInBundle(string, @"MobileCapture", [NSBundle mainBundle], @"");
}

- (void)captureScenario:(AUIMultiPageImageCaptureScenario*)captureScenario
	onCloseWithResult:(id<AUIMultiPageImageCaptureResult>)result
{
	__weak typeof(self) wSelf = self;
	NSError* error;
	if([result pagesWithError:&error].count == 0) {
		[self dismmissCaptureControllerWithCompletion:^{
			[wSelf reactResolve:@{@"resultInfo": @{@"userAction": @"Canceled" } }];
		}];
		return;
	}

	UIAlertController* alertController = [UIAlertController alertControllerWithTitle:localized(@"You will lose captured pages")
																			 message:localized(@"Discard pages?")
																	  preferredStyle:UIAlertControllerStyleAlert];
	[alertController addAction:[UIAlertAction actionWithTitle:localized(@"Cancel") style:UIAlertActionStyleCancel handler:nil]];
	[alertController addAction:[UIAlertAction actionWithTitle:localized(@"Discard") style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
		[self dismmissCaptureControllerWithCompletion:^{
			[wSelf reactResolve:@{@"resultInfo": @{@"userAction": @"Canceled" } }];
		}];
	}]];
	[[self.mobileCapture topViewController] presentViewController:alertController animated:YES completion:nil];
}

- (void)captureScenario:(AUIMultiPageImageCaptureScenario*)captureScenario
	didFailWithError:(NSError*)error
	result:(id<AUIMultiPageImageCaptureResult>)result
{
	__weak typeof(self) wSelf = self;
	[self dismmissCaptureControllerWithCompletion:^{
		[wSelf reactRejectWithError:error];
	}];
}

#pragma mark - UIMultiPageCaptureSettings
- (void)captureScenario:(AUIMultiPageImageCaptureScenario*)captureScenario
	onConfigureImageCaptureSettings:(id<AUIImageCaptureSettings>)settings
	forPageAtIndex:(NSUInteger)index
{
	settings.minimumDocumentToViewRatio = self.settings.minimumDocumentToViewRatio;
	settings.documentSize = self.settings.documentSize;
	settings.aspectRatioMin = self.settings.aspectRatioMin;
	settings.aspectRatioMax = self.settings.aspectRatioMax;
	settings.imageFromGalleryMaxSize = self.settings.galleryImageMaxSize;
}

- (NSDictionary*)errorWithMessage:(NSString*)message
{
	return RCTMakeError(NSLocalizedString(message, nil), nil, nil);
}

#pragma mark - export
- (void)exportResult:(id<AUIMultiPageImageCaptureResult>)result withCompletion:(void(^)(NSDictionary*, NSError*))completion
{
	NSError* error;
	NSArray<AUIPageId>* ids = [result pagesWithError:&error];
	if(ids == nil) {
		completion(nil, error);
		return;
	}

	NSMutableDictionary* exportDict = @{}.mutableCopy;
	AICExportFormat exportType = self.settings.exportFormat;
	if(exportType == AICExportFormatPdf) {
		NSDictionary* pdfDict = [self exportAsPdf:result error:&error];
		if(pdfDict == nil) {
			completion(nil, error);
			return;
		}
		exportDict[@"pdfInfo"] = pdfDict;
	} else {
		NSArray* images = [self exportImagesFrom:result exportType:exportType error:&error];
		if(images == nil) {
			completion(nil, error);
			return;
		}
		exportDict[@"images"] = images;
	}
	exportDict[@"resultInfo"] = @{
		@"uriPrefix": [RTRCoreApiPluginAdapter
			uriPrefixForDestination:self.settings.destinationString
			extension:self.settings.exportFormatString],
	};
	completion(exportDict, nil);
}

- (NSString*)exportDirectory
{
	NSString* storagePath = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES).firstObject;
	storagePath = [storagePath stringByAppendingPathComponent:@"mobile_capture_export"];
	NSFileManager* fileManager = NSFileManager.defaultManager;
	if(![fileManager fileExistsAtPath:storagePath]) {
		[fileManager createDirectoryAtPath:storagePath withIntermediateDirectories:YES attributes:nil error:nil];
	}
	return storagePath;
}

- (NSArray*)exportImagesFrom:(id<AUIMultiPageImageCaptureResult>)result exportType:(AICExportFormat)exportType error:(NSError**)error
{
	NSMutableArray* images = @[].mutableCopy;
	NSArray<AUIPageId>* ids = [result pagesWithError:error];
	if(ids == nil) {
		return nil;
	}

	AICDestintationType destination = self.settings.destinationType;

	if(destination == AICDestintationTypeBase64) {
		if(ids.count != 1) {
			RCTLog(@"Warning: If more then one image are captured, base64 export option value will be ignored and the result will be saved to a file anyway");
		} else {
			return [self exportSingleImageBase64:result identifier:ids.firstObject error:error];
		}
	}

	for(AUIPageId identifier in ids) {
		UIImage* image = [result loadImageWithId:identifier error:error];
		if(image == nil) {
			return nil;
		}
		CGSize imageSize = image.size;

		NSString* filePath = [[self exportDirectory] stringByAppendingPathComponent:[NSString stringWithFormat:@"%@.jpg", identifier]];
		RTRFileOutputStream* stream = [[RTRFileOutputStream alloc] initWithFilePath:filePath];
		if(stream.error != nil) {
			*error = stream.error;
			return nil;
		}

		id<RTRCoreAPIExportOperation> export = [self exporterWithOutput:stream];
		if(![export addPageWithImage:image]) {
			if(error != nil) {
				*error = export.error;
			}
			return nil;
		}

		if(![export close]) {
			if(error != nil) {
				*error = export.error;
			}
			return nil;
		}

		NSFileManager* fileManager = NSFileManager.defaultManager;
		NSDictionary* fileAttributes = [fileManager attributesOfItemAtPath:filePath error:nil];
		[images addObject:@{
			@"filePath": filePath,
			@"resultInfo": @{
				@"imageSize": @{ @"width": @((NSInteger) imageSize.width), @"height": @((NSInteger) imageSize.height) },
				@"exportType": [self.settings exportFormatString],
				@"fileSize": ((NSNumber*)fileAttributes[NSFileSize]).stringValue,
			}
		}];
	}
	return images;
}

- (NSDictionary*)exportAsPdf:(id<AUIMultiPageImageCaptureResult>)result error:(NSError**)error
{
	NSArray<AUIPageId>* ids = [result pagesWithError:error];
	if(ids == nil) {
		return nil;
	}
	NSString* filepath = [[self exportDirectory] stringByAppendingPathComponent:@"somepdf.pdf"];
	RTRFileOutputStream* stream = [[RTRFileOutputStream alloc] initWithFilePath:filepath];
	id<RTRCoreAPI> coreApi = [self.mobileCapture.rtrEngine createCoreAPI];
	id<RTRCoreAPIExportToPdfOperation> op = [coreApi createExportToPdfOperation:stream];
	op.compression = (RTRCoreAPIExportCompressionLevel)self.settings.compressionLevel;

	for(AUIPageId identifier in ids) {
		UIImage* image = [result loadImageWithId:identifier error:error];
		if(image == nil) {
			return nil;
		}
		if(![op addPageWithImage:image]) {
		  if(error != nil) {
			  *error = op.error;
		  }
		  return nil;
		}
	}

	if(![op close]) {
		if(error != nil) {
			*error = op.error;
		}
		return nil;
	}

	NSFileManager* fileManager = NSFileManager.defaultManager;
	NSDictionary* fileAttributes = [fileManager attributesOfItemAtPath:filepath error:nil];

	return @{
		@"pagesCount": @(ids.count),
		@"filePath": filepath,
		@"fileSize": ((NSNumber*)fileAttributes[NSFileSize]).stringValue,
	};
}

- (NSArray*)exportSingleImageBase64:(id<AUIMultiPageImageCaptureResult>)result identifier:(AUIPageId)identifier error:(NSError**)error
{
	UIImage* image = [result loadImageWithId:identifier error:error];
	if(image == nil) {
		return nil;
	}
	CGSize imageSize = image.size;

	NSArray<NSValue*>* boudnary = [result loadBoundaryWithId:identifier error:error];
	if(boudnary == nil) {
		return nil;
	}

	return [self exportSingleImageBase64:image info:@{
		@"exportType": [self.settings exportFormatString],
		@"imageSize": @{ @"width": @((NSInteger) imageSize.width), @"height": @((NSInteger) imageSize.height) },
	} error:error];
}

- (NSArray*)exportSingleImageBase64:(UIImage*)image info:(NSDictionary*)info error:(NSError**)error
{
	RTRMemoryOutputStream* stream = [RTRMemoryOutputStream new];
	id<RTRCoreAPIExportOperation> exporter = [self exporterWithOutput:stream];
	[exporter addPageWithImage:image];
	if(![exporter close]) {
		if(error != nil) {
			*error = exporter.error;
		}
		return nil;
	}
	NSString* base64String = [stream.data base64EncodedStringWithOptions:0];
	return @[@{
		@"resultInfo": info,
		@"stringLength": @(base64String.length),
		@"base64": base64String,
	}];
}

- (BOOL)exportImage:(UIImage*)image stream:(id<RTROutputStream>)stream error:(NSError**)error
{
	id<RTRCoreAPIExportOperation> operation = [self exporterWithOutput:stream];
	[operation addPageWithImage:image];
	if(![operation close]) {
		if(error != nil) {
			*error = operation.error;
		}
		return NO;
	}
	return YES;
}

- (id<RTRCoreAPIExportOperation>)exporterWithOutput:(id<RTROutputStream>)stream
{
	return [self exporterWithEncoding:self.settings.exportFormat output:stream];
}

- (id<RTRCoreAPIExportOperation>)exporterWithEncoding:(AICExportFormat)format output:(id<RTROutputStream>)stream
{
	id<RTRCoreAPI> coreApi = [self.mobileCapture.rtrEngine createCoreAPI];
	switch(format) {
		case AICExportFormatJpg: {
			id<RTRCoreAPIExportToJpgOperation> op = [coreApi createExportToJpgOperation:stream];
			op.compression = (RTRCoreAPIExportCompressionLevel)self.settings.compressionLevel;
			return op;
		}
		case AICExportFormatPng:
			return [coreApi createExportToPngOperation:stream];

		case AICExportFormatPdf: {
			id<RTRCoreAPIExportToPdfOperation> op = [coreApi createExportToPdfOperation:stream];
			op.compression = (RTRCoreAPIExportCompressionLevel)self.settings.compressionLevel;
			op.pageSize = self.settings.documentSize;
			return op;
		}
	}
	return nil;
}

@end
