// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

package com.abbyy.mobile.rtr.reactnative;

import android.app.Application;
import android.os.AsyncTask;

import com.abbyy.mobile.rtr.javascript.JSCallback;
import com.abbyy.mobile.rtr.javascript.data.DataCapture;
import com.abbyy.mobile.rtr.javascript.image.ImagingCore;
import com.abbyy.mobile.rtr.javascript.text.TextRecognition;
import com.abbyy.mobile.rtr.reactnative.image.ImageCapture;
import com.abbyy.mobile.rtr.reactnative.utils.JsonConverter;
import com.abbyy.mobile.rtr.reactnative.utils.ReactNativeConverter;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AbbyyMobileCaptureModule extends ReactContextBaseJavaModule {

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
	public void startImageCapture( @Nullable ReadableMap settings, @NonNull Promise promise )
	{
		ImageCapture.startImageCapture( getReactApplicationContext(), settings, promise );
	}

	@ReactMethod
	public void recognizeText( @Nullable ReadableMap settings, @NonNull final Promise promise )
	{
		runMethodAsync( settings, promise, new SdkMethodRunnable() {
			@Override
			public void run( Application applicationContext, JSONObject settings, JSCallback jsCallback )
			{
				TextRecognition.recognizeTextSync( applicationContext, settings, jsCallback );
			}
		} );
	}

	@ReactMethod
	public void extractData( @Nullable ReadableMap settings, @NonNull Promise promise )
	{
		runMethodAsync( settings, promise, new SdkMethodRunnable() {
			@Override
			public void run( Application applicationContext, JSONObject settings, JSCallback jsCallback )
			{
				DataCapture.extractDataSync( applicationContext, settings, jsCallback );
			}
		} );
	}

	@ReactMethod
	public void detectDocumentBoundary( @Nullable ReadableMap settings, @NonNull Promise promise )
	{
		runMethodAsync( settings, promise, new SdkMethodRunnable() {
			@Override
			public void run( Application applicationContext, JSONObject settings, JSCallback jsCallback )
			{
				ImagingCore.detectDocumentBoundarySync( applicationContext, settings, jsCallback );
			}
		} );
	}

	@ReactMethod
	public void cropImage( @Nullable final ReadableMap settings, @NonNull Promise promise )
	{
		runMethodAsync( settings, promise, new SdkMethodRunnable() {
			@Override
			public void run( Application applicationContext, JSONObject settings, JSCallback jsCallback )
			{
				ImagingCore.cropImageSync( applicationContext, settings, jsCallback );
			}
		} );
	}

	@ReactMethod
	public void rotateImage( @Nullable final ReadableMap settings, @NonNull Promise promise )
	{
		runMethodAsync( settings, promise, new SdkMethodRunnable() {
			@Override
			public void run( Application applicationContext, JSONObject settings, JSCallback jsCallback )
			{
				ImagingCore.rotateImageSync( applicationContext, settings, jsCallback );
			}
		} );
	}

	@ReactMethod
	public void assessQualityForOcr( @Nullable ReadableMap settings, @NonNull Promise promise )
	{
		runMethodAsync( settings, promise, new SdkMethodRunnable() {
			@Override
			public void run( Application applicationContext, JSONObject settings, JSCallback jsCallback )
			{
				ImagingCore.assessQualityForOcrSync( applicationContext, settings, jsCallback );
			}
		} );
	}

	@ReactMethod
	public void exportImage( @Nullable ReadableMap settings, @NonNull Promise promise )
	{
		runMethodAsync( settings, promise, new SdkMethodRunnable() {
			@Override
			public void run( Application applicationContext, JSONObject settings, JSCallback jsCallback )
			{
				ImagingCore.exportImageSync( applicationContext, settings, jsCallback );
			}
		} );
	}

	@ReactMethod
	public void exportImagesToPdf( @Nullable final ReadableMap settings, @NonNull final Promise promise )
	{
		runMethodAsync( settings, promise, new SdkMethodRunnable() {
			@Override
			public void run( Application applicationContext, JSONObject settings, JSCallback jsCallback )
			{
				ImagingCore.exportImagesToPdfSync( applicationContext, settings, jsCallback );
			}
		} );
	}

	private void runMethodAsync( final ReadableMap settings, final Promise promise, final SdkMethodRunnable runnable )
	{
		AsyncTask.execute( new Runnable() {
			@Override
			public void run()
			{
				JSONObject jsonObject = JsonConverter.toJSON( settings );
				Application applicationContext = (Application) getReactApplicationContext().getApplicationContext();
				runnable.run( applicationContext, jsonObject, new ReactNativeJSCallback( promise ) );
			}
		} );
	}

	private interface SdkMethodRunnable {
		void run( Application applicationContext, JSONObject settings, JSCallback jsCallback );
	}

	private static class ReactNativeJSCallback implements JSCallback {

		private final Promise promise;

		public ReactNativeJSCallback( Promise promise )
		{
			this.promise = promise;
		}

		@Override
		public void onSuccess( JSONObject result )
		{
			ReadableMap readableMap = ReactNativeConverter.fromJson( result );
			promise.resolve( readableMap );
		}

		@Override
		public void onError( String errorCode, String message, Throwable exception )
		{
			if( exception == null ) {
				promise.reject( errorCode, message );
			} else {
				promise.reject( errorCode, message, exception );
			}
		}
	}

}
