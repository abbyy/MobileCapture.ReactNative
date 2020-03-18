// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

package com.abbyy.rtr.reactnative;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.abbyy.mobile.rtr.Engine;
import com.abbyy.rtr.reactnative.image.ImageCaptureActivity;
import com.abbyy.rtr.reactnative.image.ImageCaptureResult;
import com.abbyy.rtr.reactnative.image.ImageCaptureSettings;
import com.abbyy.rtr.reactnative.image.ImageCaptureSettingsParser;
import com.abbyy.rtr.reactnative.image.ReactNativeImageCaptureResult;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AbbyyMobileCaptureModule extends ReactContextBaseJavaModule {

	private static final String LICENSE_FILE_NAME_KEY = "licenseFileName";
	private static final int IMAGE_CAPTURE_REQUEST_CODE = 0;
	private static final String ENGINE_INIT_ERROR_CODE = "EngineInit";
	private static final String IMAGE_CAPTURE_RESULT_ERROR_CODE = "ImageCaptureResult";
	private static final String IMAGE_CAPTURE_START_ERROR_CODE = "ImageCaptureStart";

	private class ImageCaptureResultListener extends BaseActivityEventListener {

		private final Promise promise;
		private final ImageCaptureSettings settings;
		private final ReactContext reactContext;

		public ImageCaptureResultListener(
			@NonNull Promise promise,
			ImageCaptureSettings settings,
			@NonNull ReactContext reactContext
		)
		{
			this.promise = promise;
			this.settings = settings;
			this.reactContext = reactContext;
		}

		@Override
		public void onActivityResult(
			@NonNull Activity activity,
			int requestCode,
			int resultCode,
			@Nullable Intent data
		)
		{
			if( requestCode == IMAGE_CAPTURE_REQUEST_CODE ) {
				reactContext.removeActivityEventListener( ImageCaptureResultListener.this );
				onImageCaptureResult( promise, settings, resultCode, data );
			}
		}
	}

	public AbbyyMobileCaptureModule( @NonNull ReactApplicationContext reactContext )
	{
		super( reactContext );
	}

	@NonNull
	@Override
	public String getName()
	{
		return "AbbyyMobileCapture";
	}

	@ReactMethod
	public void startImageCapture( @Nullable ReadableMap readableMap, @NonNull Promise promise )
	{
		if( !initializeEngineIfNeeded( readableMap, promise ) ) {
			return;
		}

		Activity activity = getCurrentActivity();
		if( activity == null ) {
			promise.reject( IMAGE_CAPTURE_START_ERROR_CODE, "Current Activity is null" );
		} else {
			ImageCaptureSettings settings;
			try {
				settings = ImageCaptureSettingsParser.parse( readableMap );
			} catch( Exception e ) {
				promise.reject( IMAGE_CAPTURE_START_ERROR_CODE, "Settings parse error: " + e.getMessage() );
				return;
			}

			activity.startActivityForResult(
				ImageCaptureActivity.getIntent( activity, settings ),
				IMAGE_CAPTURE_REQUEST_CODE
			);

			getReactApplicationContext().addActivityEventListener(
				new ImageCaptureResultListener( promise, settings, getReactApplicationContext() )
			);
		}
	}

	private void onImageCaptureResult(
		@NonNull Promise promise,
		@NonNull ImageCaptureSettings settings,
		int resultCode,
		@Nullable Intent data
	)
	{
		if( resultCode == Activity.RESULT_OK ) {
			onImageCaptureResultOk( promise, settings, data );
		} else {
			onImageCaptureResultCancel( promise, data );
		}
	}

	private void onImageCaptureResultOk(
		@NonNull final Promise promise,
		@NonNull final ImageCaptureSettings settings,
		@Nullable Intent data
	)
	{
		final ImageCaptureResult imageCaptureResult;
		if( data == null ) {
			imageCaptureResult = null;
		} else {
			imageCaptureResult = data.getParcelableExtra( ImageCaptureActivity.IMAGE_CAPTURE_RESULT_KEY );
		}

		if( imageCaptureResult == null ) {
			promise.reject( IMAGE_CAPTURE_RESULT_ERROR_CODE, "Returned result is null" );
		} else {
			AsyncTask.execute( new Runnable() {
				@Override
				public void run()
				{
					try {
						ReadableMap rnResult = ReactNativeImageCaptureResult.getResult( imageCaptureResult, settings );
						promise.resolve( rnResult );
					} catch( Exception e ) {
						promise.reject( IMAGE_CAPTURE_RESULT_ERROR_CODE, "Can't construct React Native result object", e );
					}
				}
			} );
		}
	}

	private void onImageCaptureResultCancel( @NonNull Promise promise, @Nullable Intent data )
	{
		if( data == null ) {
			promise.resolve( ReactNativeImageCaptureResult.getUserCancelledResult() );
		} else {
			String error = data.getStringExtra( ImageCaptureActivity.ERROR_DESCRIPTION_RESULT_KEY );
			String notEmptyError = TextUtils.isEmpty( error ) ? "Unknown error" : error;
			promise.reject( IMAGE_CAPTURE_RESULT_ERROR_CODE, notEmptyError );
		}
	}

	/**
	 * Returns true an engine is created successfully, false otherwise.
	 *
	 * @param readableMap Map with license name value.
	 * @param promise Promise on which {@link Promise#reject(Throwable) } is called in case of an error.
	 */
	private boolean initializeEngineIfNeeded( @Nullable ReadableMap readableMap, @NonNull Promise promise )
	{
		String licenseFileName = null;
		if( readableMap != null && readableMap.hasKey( LICENSE_FILE_NAME_KEY ) ) {
			licenseFileName = readableMap.getString( LICENSE_FILE_NAME_KEY );
		}

		if( licenseFileName == null ) {
			licenseFileName = "MobileCapture.License";
		}

		try {
			Application application = (Application) getReactApplicationContext().getApplicationContext();
			SharedEngine.initialize( application, licenseFileName );
			return true;
		} catch( IOException exception ) {
			promise.reject(
				ENGINE_INIT_ERROR_CODE,
				"Could not load some required resource files. Make sure to configure " +
					"'assets' directory in your application and specify correct 'license file name'. " +
					"See logcat for details.",
				exception
			);
			return false;
		} catch( Engine.LicenseException exception ) {
			promise.reject(
				ENGINE_INIT_ERROR_CODE,
				"License not valid. Make sure you have a valid license file in the " +
					"'assets' directory and specify correct 'license file name' and 'application id'. " +
					"See logcat for details." + exception.getMessage(),
				exception
			);
			return false;
		} catch( Throwable throwable ) {
			promise.reject(
				ENGINE_INIT_ERROR_CODE,
				"Unspecified error while loading the engine. See logcat for details.",
				throwable
			);
			return false;
		}
	}
}
