// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

package com.abbyy.mobile.rtr.reactnative.image;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.abbyy.mobile.rtr.javascript.JSCallback;
import com.abbyy.mobile.rtr.javascript.SharedEngine;
import com.abbyy.mobile.rtr.reactnative.utils.JsonConverter;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableMap;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ImageCapture {

	private static final int IMAGE_CAPTURE_REQUEST_CODE = 0;

	private static final String IMAGE_CAPTURE_RESULT_ERROR_CODE = "ImageCaptureResult";
	private static final String IMAGE_CAPTURE_START_ERROR_CODE = "ImageCaptureStart";

	private static class ImageCaptureResultListener extends BaseActivityEventListener {

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

	private ImageCapture()
	{
		// Utility
	}

	public static void startImageCapture(
		@NonNull ReactApplicationContext reactContext,
		@Nullable ReadableMap settings,
		@NonNull final Promise promise
	)
	{
		Application application = (Application) reactContext.getApplicationContext();
		JSONObject jsonSettings = JsonConverter.toJSON( settings );
		String licenseFilename;
		try {
			licenseFilename = com.abbyy.mobile.rtr.javascript.utils.SettingsParserUtils.parseLicenseFilename( jsonSettings );
		} catch( JSONException e ) {
			promise.reject( IMAGE_CAPTURE_START_ERROR_CODE, "Settings parse error: " + e.getMessage() );
			return;
		}
		if( !SharedEngine.initializeEngineIfNeeded( application, licenseFilename, new JSCallback() {
			@Override
			public void onSuccess( JSONObject result )
			{
			}

			@Override
			public void onError( String errorCode, String message, Throwable exception )
			{
				promise.reject( errorCode, message, exception );
			}
		} ) ) {
			return;
		}

		Activity activity = reactContext.getCurrentActivity();
		if( activity == null ) {
			promise.reject( IMAGE_CAPTURE_START_ERROR_CODE, "Current Activity is null" );
		} else {
			ImageCaptureSettings imageCaptureSettings;
			try {
				imageCaptureSettings = ImageCaptureSettingsParser.parse( jsonSettings );
			} catch( Exception e ) {
				promise.reject( IMAGE_CAPTURE_START_ERROR_CODE, "Settings parse error: " + e.getMessage() );
				return;
			}

			activity.startActivityForResult(
				ImageCaptureActivity.getIntent( activity, imageCaptureSettings ),
				IMAGE_CAPTURE_REQUEST_CODE
			);

			reactContext.addActivityEventListener(
				new ImageCaptureResultListener( promise, imageCaptureSettings, reactContext )
			);
		}
	}

	private static void onImageCaptureResult(
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

	private static void onImageCaptureResultOk(
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

	private static void onImageCaptureResultCancel( @NonNull Promise promise, @Nullable Intent data )
	{
		if( data == null ) {
			promise.resolve( ReactNativeImageCaptureResult.getUserCancelledResult() );
		} else {
			String error = data.getStringExtra( ImageCaptureActivity.ERROR_DESCRIPTION_RESULT_KEY );
			String notEmptyError = TextUtils.isEmpty( error ) ? "Unknown error" : error;
			promise.reject( IMAGE_CAPTURE_RESULT_ERROR_CODE, notEmptyError );
		}
	}

}
