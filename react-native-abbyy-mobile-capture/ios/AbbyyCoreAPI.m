/// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
/// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

#import "AbbyyCoreAPI.h"
#import "RTRCoreApiPluginAdapter.h"

@interface AbbyyCoreAPI ()

@property (nonatomic, weak) AbbyyMobileCapture* mobileCapture;

@end

@implementation AbbyyCoreAPI

- (instancetype)initWithMobileCapture:(AbbyyMobileCapture*)mobileCapture
	settings:(NSDictionary*)settings
	resolver:(RCTPromiseResolveBlock)resolve
	rejecter:(RCTPromiseRejectBlock)reject
{
	self = [super init];
	if(self!= nil) {
		NSError* error = nil;
		if(mobileCapture.rtrEngine == nil && ![mobileCapture createRTREngineWithSettings:settings error:&error]) {
			NSParameterAssert(error != nil);
			reject(@(error.code).stringValue, error.localizedDescription, error);
			return nil;
		}

		_mobileCapture = mobileCapture;
		_settings = settings;
		_rctReject = reject;
		_rctResolve = resolve;
	}
	return self;
}

- (void)recognizeText
{
	dispatch_async(dispatch_get_global_queue(QOS_CLASS_DEFAULT, 0), ^{
		[self recognizeText_sync];
	});
}

- (void)recognizeText_sync
{
	RTRCoreApiPluginAdapter* coreAPIAdapter = [[RTRCoreApiPluginAdapter alloc] initWithEngine:self.mobileCapture.rtrEngine];
	__weak typeof(self) weakSelf = self;
	[coreAPIAdapter recognizeText:self.settings
		onError:^(NSError* error)
		{
			[weakSelf reactRejectWithError:error];
		}
		onSuccess:^(NSDictionary* result)
		{
			[weakSelf reactResolve:result];
		}
	];
}

- (void)extractData
{
	dispatch_async(dispatch_get_global_queue(QOS_CLASS_DEFAULT, 0), ^{
		[self extractData_sync];
	});
}

- (void)extractData_sync
{
	RTRCoreApiPluginAdapter* coreAPIAdapter = [[RTRCoreApiPluginAdapter alloc] initWithEngine:self.mobileCapture.rtrEngine];
	__weak typeof(self) weakSelf = self;
	[coreAPIAdapter extractData:self.settings
		onError:^(NSError* error)
		{
			[weakSelf reactRejectWithError:error];
		}
		onSuccess:^(NSDictionary* result)
		{
			[weakSelf reactResolve:result];
		}
	];
}

- (void)detectDocumentBoundary
{
	dispatch_async(dispatch_get_global_queue(QOS_CLASS_DEFAULT, 0), ^{
		[self detectDocumentBoundary_sync];
	});
}

- (void)detectDocumentBoundary_sync
{
	RTRCoreApiPluginAdapter* coreAPIAdapter = [[RTRCoreApiPluginAdapter alloc] initWithEngine:self.mobileCapture.rtrEngine];
	__weak typeof(self) weakSelf = self;
	[coreAPIAdapter detectBoundaryOnImage:self.settings
		onError:^(NSError* error)
		{
			[weakSelf reactRejectWithError:error];
		}
		onSuccess:^(NSDictionary* result)
		{
			[weakSelf reactResolve:result];
		}
	];
}

- (void)cropImage
{
	dispatch_async(dispatch_get_global_queue(QOS_CLASS_DEFAULT, 0), ^{
		[self cropImage_sync];
	});
}

- (void)cropImage_sync
{
	RTRCoreApiPluginAdapter* coreAPIAdapter = [[RTRCoreApiPluginAdapter alloc] initWithEngine:self.mobileCapture.rtrEngine];
	__weak typeof(self) weakSelf = self;
	[coreAPIAdapter cropImage:self.settings
		onError:^(NSError* error)
		{
			[weakSelf reactRejectWithError:error];
		}
		onSuccess:^(NSDictionary* result)
		{
			[weakSelf reactResolve:result];
		}
	];
}

- (void)rotateImage
{
	dispatch_async(dispatch_get_global_queue(QOS_CLASS_DEFAULT, 0), ^{
		[self rotateImage_sync];
	});
}

- (void)rotateImage_sync
{
	RTRCoreApiPluginAdapter* coreAPIAdapter = [[RTRCoreApiPluginAdapter alloc] initWithEngine:self.mobileCapture.rtrEngine];
	__weak typeof(self) weakSelf = self;
	[coreAPIAdapter rotateImage:self.settings
		onError:^(NSError* error)
		{
			[weakSelf reactRejectWithError:error];
		}
		onSuccess:^(NSDictionary* result)
		{
			[weakSelf reactResolve:result];
		}
	];
}

- (void)assessQualityForOcr
{
	dispatch_async(dispatch_get_global_queue(QOS_CLASS_DEFAULT, 0), ^{
		[self assessQualityForOcr_sync];
	});
}

- (void)assessQualityForOcr_sync
{
	RTRCoreApiPluginAdapter* coreAPIAdapter = [[RTRCoreApiPluginAdapter alloc] initWithEngine:self.mobileCapture.rtrEngine];
	__weak typeof(self) weakSelf = self;
	[coreAPIAdapter assessOCRQualityOnImage:self.settings
		onError:^(NSError* error)
		{
			[weakSelf reactRejectWithError:error];
		}
		onSuccess:^(NSDictionary* result)
		{
			[weakSelf reactResolve:result];
		}
	];
}

- (void)exportImage
{
	dispatch_async(dispatch_get_global_queue(QOS_CLASS_DEFAULT, 0), ^{
		[self exportImage_sync];
	});
}

- (void)exportImage_sync
{
	RTRCoreApiPluginAdapter* coreAPIAdapter = [[RTRCoreApiPluginAdapter alloc] initWithEngine:self.mobileCapture.rtrEngine];
	__weak typeof(self) weakSelf = self;
	[coreAPIAdapter exportImage:self.settings
		onError:^(NSError* error)
		{
			[weakSelf reactRejectWithError:error];
		}
		onSuccess:^(NSDictionary* result)
		{
			[weakSelf reactResolve:result];
		}
	];
}

- (void)exportImagesToPdf
{
	dispatch_async(dispatch_get_global_queue(QOS_CLASS_DEFAULT, 0), ^{
		[self exportImagesToPdf_sync];
	});
}

- (void)exportImagesToPdf_sync
{
	RTRCoreApiPluginAdapter* coreAPIAdapter = [[RTRCoreApiPluginAdapter alloc] initWithEngine:self.mobileCapture.rtrEngine];
	__weak typeof(self) weakSelf = self;
	[coreAPIAdapter exportImagesToPdf:self.settings
		onError:^(NSError* error)
		{
			[weakSelf reactRejectWithError:error];
		}
		onSuccess:^(NSDictionary* result)
		{
			[weakSelf reactResolve:result];
		}
	];
}

#pragma mark -

- (void)reactResolve:(NSDictionary*)result
{
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

@end
