// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

package com.abbyy.rtr.reactnative.image;

import android.view.ContextThemeWrapper;

import com.abbyy.rtr.reactnative.AbbyyMobileCaptureModule;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;

import org.junit.Before;
import org.junit.Test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LicenseParseTest {

	private AbbyyMobileCaptureModule module;
	private Promise promise;

	@Before
	public void setUp()
	{
		module = new AbbyyMobileCaptureModule( new ReactApplicationContext( new ContextThemeWrapper() ) );
		promise = new Promise() {
			@Override
			public void resolve( @Nullable Object value ) { }

			@Override
			public void reject( String code, String message ) { }

			@Override
			public void reject( String code, Throwable throwable ) { }

			@Override
			public void reject( String code, String message, Throwable throwable ) {}

			@Override
			public void reject( Throwable throwable ) {}

			@Override
			public void reject( Throwable throwable, WritableMap userInfo ) {}

			@Override
			public void reject( String code, @NonNull WritableMap userInfo ) { }

			@Override
			public void reject( String code, Throwable throwable, WritableMap userInfo ) { }

			@Override
			public void reject( String code, String message, @NonNull WritableMap userInfo ) { }

			@Override
			public void reject( String code, String message, Throwable throwable, WritableMap userInfo ) { }

			@Override
			public void reject( String message ) { }
		};
	}

	@Test
	public void onNullSettings_shouldNotThrowException()
	{
		module.startImageCapture( null, promise );
	}

	@Test
	public void onEmptySettings_shouldNotThrowException()
	{
		module.startImageCapture( new RNMapMock(), promise );
	}

}
