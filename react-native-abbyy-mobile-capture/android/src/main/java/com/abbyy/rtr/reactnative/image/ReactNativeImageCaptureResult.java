// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

package com.abbyy.rtr.reactnative.image;

import com.abbyy.rtr.reactnative.image.ImageCaptureSettings.ExportType;
import com.abbyy.rtr.reactnative.utils.FileUtils;
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

	// It is required to create an object every time, because React Native consumes objects.
	public static ReadableMap getUserCancelledResult()
	{
		WritableNativeMap userCancelledResult = new WritableNativeMap();
		WritableMap resultInfo = new WritableNativeMap();
		resultInfo.putString( "userAction", "Canceled" );
		userCancelledResult.putMap( "resultInfo", resultInfo );
		return userCancelledResult;
	}

	public static ReadableMap getResult(
		@NonNull ImageCaptureResult result,
		@NonNull ImageCaptureSettings settings
	) throws Exception
	{
		WritableMap map = new WritableNativeMap();

		if( settings.exportType == ExportType.PDF ) {
			WritableMap pdfInfo = getPdfInfo( result );
			map.putMap( "pdfInfo", pdfInfo );
		} else {
			ReadableArray pagesPaths = getImagesInfo( result, settings );

			map.putArray( "images", pagesPaths );
		}
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
			if( shouldReturnBase64( settings, pageCount ) ) {
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
			sizeMap.putInt( "width", page.getFrameSize().getWidth() );
			sizeMap.putInt( "height", page.getFrameSize().getHeight() );
			pageResultInfo.putMap( "size", sizeMap );
		}
		pageInfo.putMap( "resultInfo", pageResultInfo );
		return pageInfo;
	}

	private static boolean shouldReturnBase64( ImageCaptureSettings settings, int pageCount )
	{
		return settings.destination == ImageCaptureSettings.Destination.BASE64 &&
			settings.exportType != ExportType.PDF &&
			pageCount == 1;
	}

	private static WritableMap getPdfInfo( ImageCaptureResult result )
	{
		File pdfFile = result.getPdfFile();
		WritableMap pdfInfo = new WritableNativeMap();
		pdfInfo.putString( "filePath", pdfFile.getPath() );
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

}
