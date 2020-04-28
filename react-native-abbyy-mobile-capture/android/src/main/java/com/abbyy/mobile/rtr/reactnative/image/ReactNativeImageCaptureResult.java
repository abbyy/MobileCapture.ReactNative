// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

package com.abbyy.mobile.rtr.reactnative.image;

import com.abbyy.mobile.rtr.javascript.JSConstants;
import com.abbyy.mobile.rtr.javascript.utils.FileUtils;
import com.abbyy.mobile.rtr.javascript.utils.UriScheme;
import com.abbyy.mobile.rtr.reactnative.image.ImageCaptureSettings.ExportType;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;

import java.io.File;
import java.io.IOException;

import androidx.annotation.NonNull;

public class ReactNativeImageCaptureResult {

	private static final String RESULT_INFO = "resultInfo";
	private static final String URI_PREFIX = "uriPrefix";

	// It is required to create an object every time, because React Native consumes objects.
	public static ReadableMap getUserCancelledResult()
	{
		WritableNativeMap userCancelledResult = new WritableNativeMap();
		WritableMap resultInfo = new WritableNativeMap();
		resultInfo.putString( "userAction", "Canceled" );
		userCancelledResult.putMap( RESULT_INFO, resultInfo );
		return userCancelledResult;
	}

	public static ReadableMap getResult(
		@NonNull ImageCaptureResult result,
		@NonNull ImageCaptureSettings settings
	) throws Exception
	{
		WritableMap map = new WritableNativeMap();

		if( settings.exportType == ExportType.PDF ) {
			WritableMap pdfInfo = getPdfInfo( settings, result );
			map.putMap( "pdfInfo", pdfInfo );
		} else {
			ReadableArray pagesPaths = getImagesInfo( result, settings );

			map.putArray( "images", pagesPaths );
		}
		map.putMap( RESULT_INFO, getResultInfo( settings ) );
		return map;
	}

	@NonNull
	private static ReadableArray getImagesInfo(
		@NonNull ImageCaptureResult result,
		@NonNull ImageCaptureSettings settings
	) throws IOException
	{
		int pageCount = result.getPages().length;
		WritableArray pagesPaths = new WritableNativeArray();
		for( int pageIndex = 0; pageIndex < pageCount; ++pageIndex ) {
			Page page = result.getPages()[pageIndex];
			WritableMap pageInfo = getPageInfo( settings, page );
			if( settings.destination == ImageCaptureSettings.Destination.BASE64 ) {
				pageInfo.putString( "base64", FileUtils.convertFileToBase64( page.getFile() ) );
				if( !page.getFile().delete() ) {
					throw new IOException( "Can't delete page file" );
				}
			} else {
				pageInfo.putString( "filePath", page.getFile().getPath() );
			}
			pagesPaths.pushMap( pageInfo );
		}
		return pagesPaths;
	}

	private static WritableMap getPageInfo( ImageCaptureSettings settings, Page page )
	{
		WritableMap pageInfo = new WritableNativeMap();

		WritableMap pageResultInfo = new WritableNativeMap();
		pageResultInfo.putString( "exportType", getExportTypeResultString( settings.exportType ) );
		if( page.getFrameSize() != null ) {
			WritableMap sizeMap = new WritableNativeMap();
			sizeMap.putInt( JSConstants.WIDTH, page.getFrameSize().getWidth() );
			sizeMap.putInt( JSConstants.HEIGHT, page.getFrameSize().getHeight() );
			pageResultInfo.putMap( JSConstants.IMAGE_SIZE, sizeMap );
		}
		pageInfo.putMap( "resultInfo", pageResultInfo );
		return pageInfo;
	}

	private static WritableMap getPdfInfo( ImageCaptureSettings settings, ImageCaptureResult result ) throws Exception
	{
		File pdfFile = result.getPdfFile();
		WritableMap pdfInfo = new WritableNativeMap();
		if( settings.destination == ImageCaptureSettings.Destination.BASE64 ) {
			pdfInfo.putString( "filePath", pdfFile.getPath() );
		} else {
			pdfInfo.putString( "base64", FileUtils.convertFileToBase64( pdfFile ) );
		}
		pdfInfo.putInt( "pagesCount", result.getPages().length );
		return pdfInfo;
	}

	private static String getExportTypeResultString( @NonNull ExportType exportType )
	{
		switch( exportType ) {
			case JPG:
				return "Jpg";
			case PNG:
				return "Png";
			case PDF:
				return "Pdf";
			default:
				throw new RuntimeException( "Unknown export type" );
		}
	}

	private static ReadableMap getResultInfo( ImageCaptureSettings settings )
	{
		String uriPrefix;
		if( settings.destination == ImageCaptureSettings.Destination.FILE ) {
			uriPrefix = UriScheme.FILE;
		} else {
			switch( settings.exportType ) {
				case JPG:
					uriPrefix = UriScheme.BASE_64_JPEG;
					break;
				case PNG:
					uriPrefix = UriScheme.BASE_64_PNG;
					break;
				case PDF:
					uriPrefix = UriScheme.BASE_64_PDF;
					break;
				default:
					throw new IllegalStateException( "Unknown export type" );
			}
		}
		WritableMap resultInfo = new WritableNativeMap();
		resultInfo.putString( URI_PREFIX, uriPrefix );
		return resultInfo;
	}

}
