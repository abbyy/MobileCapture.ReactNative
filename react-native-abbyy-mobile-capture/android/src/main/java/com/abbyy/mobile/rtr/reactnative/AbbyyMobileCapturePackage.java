// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

package com.abbyy.mobile.rtr.reactnative;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;

public class AbbyyMobileCapturePackage implements ReactPackage {

	@NonNull
	@Override
	public List<NativeModule> createNativeModules( @NonNull ReactApplicationContext reactContext )
	{
		return Collections.<NativeModule>singletonList( new AbbyyMobileCaptureModule( reactContext ) );
	}

	@NonNull
	@Override
	public List<ViewManager> createViewManagers( @NonNull ReactApplicationContext reactContext )
	{
		return Collections.emptyList();
	}
}
