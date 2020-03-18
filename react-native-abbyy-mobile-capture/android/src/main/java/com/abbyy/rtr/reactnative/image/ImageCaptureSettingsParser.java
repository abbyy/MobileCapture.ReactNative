// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

package com.abbyy.rtr.reactnative.image;

import android.content.pm.ActivityInfo;

import com.abbyy.mobile.rtr.IImagingCoreAPI.ExportOperation.Compression;
import com.abbyy.mobile.uicomponents.CaptureView.CameraSettings.Resolution;
import com.abbyy.mobile.uicomponents.scenario.ImageCaptureScenario.DocumentSize;
import com.abbyy.rtr.reactnative.image.ImageCaptureSettings.Destination;
import com.abbyy.rtr.reactnative.image.ImageCaptureSettings.ExportType;
import com.facebook.react.bridge.ReadableMap;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ImageCaptureSettingsParser {

	private static final String FLASHLIGHT_BUTTON = "isFlashlightButtonVisible";
	private static final String CAPTURE_BUTTON = "isCaptureButtonVisible";
	private static final String SHOW_PREVIEW = "isShowPreviewEnabled";
	private static final String GALLERY_BUTTON = "isGalleryButtonVisible";

	// All values case insensitive so they are checked in lower case
	private static final String RESOLUTION = "cameraResolution";
	private static final Map<String, Resolution> RESOLUTION_VALUES = new HashMap<String, Resolution>() {{
		put( "hd", Resolution.HD );
		put( "fullhd", Resolution.FULL_HD );
		put( "uhd_4k", Resolution.UHD_4K );
	}};

	private static final String ORIENTATION = "orientation";
	private static final Map<String, Integer> ORIENTATION_VALUES = new HashMap<String, Integer>() {{
		put( "default", ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED );
		put( "portrait", ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
		put( "landscape", ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE );
	}};

	private static final String DESTINATION = "destination";
	private static final Map<String, Destination> DESTINATION_VALUES = new HashMap<String, Destination>() {{
		put( "file", Destination.FILE );
		put( "base64", Destination.BASE64 );
	}};

	private static final String EXPORT_TYPE = "exportType";
	private static final Map<String, ExportType> EXPORT_TYPE_VALUES = new HashMap<String, ExportType>() {{
		put( "jpg", ExportType.JPG );
		put( "png", ExportType.PNG );
		put( "pdf", ExportType.PDF );
	}};

	private static final String COMPRESSION_LEVEL = "compressionLevel";
	private static final Map<String, Compression> COMPRESSION_LEVEL_VALUES = new HashMap<String, Compression>() {{
		put( "low", Compression.Low );
		put( "normal", Compression.Normal );
		put( "high", Compression.High );
		put( "extrahigh", Compression.ExtraHigh );
	}};

	private static final String DOCUMENT_SIZE = "documentSize";
	private static final Map<String, DocumentSize> DOCUMENT_SIZE_VALUES = new HashMap<String, DocumentSize>() {{
		put( "any", DocumentSize.ANY );
		put( "a4", DocumentSize.A4 );
		put( "businesscard", DocumentSize.BUSINESS_CARD );
		put( "letter", DocumentSize.LETTER );
	}};

	private static final String DEFAULT_IMAGE_SETTINGS = "defaultImageSettings";
	private static final String MINIMUM_DOCUMENT_TO_VIEW_RATIO = "minimumDocumentToViewRatio";
	private static final String REQUIRED_IMAGE_COUNT = "requiredPageCount";
	private static final String ASPECT_RATIO_MIN = "aspectRatioMin";
	private static final String ASPECT_RATIO_MAX = "aspectRatioMax";
	private static final String IMAGE_FROM_GALLERY_MAX_SIZE = "imageFromGalleryMaxSize";

	private ImageCaptureSettingsParser()
	{
		// Utility class
	}

	public static ImageCaptureSettings parse( @Nullable ReadableMap map ) throws Exception
	{
		ImageCaptureSettings settings = new ImageCaptureSettings();
		if( map == null ) {
			return settings;
		}

		parsePrimitives( map, settings );
		parseDefaultImageSettingsObject( map, settings );

		settings.cameraResolution = parseEnumValue( map, RESOLUTION, RESOLUTION_VALUES, settings.cameraResolution );
		settings.orientation = parseEnumValue( map, ORIENTATION, ORIENTATION_VALUES, settings.orientation );
		settings.destination = parseEnumValue( map, DESTINATION, DESTINATION_VALUES, settings.destination );
		settings.exportType = parseEnumValue( map, EXPORT_TYPE, EXPORT_TYPE_VALUES, settings.exportType );
		settings.compressionLevel = parseEnumValue( map, COMPRESSION_LEVEL, COMPRESSION_LEVEL_VALUES, settings.compressionLevel );

		checkForBase64DestinationParameters( settings );

		return settings;
	}

	private static void checkForBase64DestinationParameters( ImageCaptureSettings settings ) throws Exception
	{
		if( settings.destination == Destination.BASE64 ) {
			if( settings.requiredPageCount != 1 ) {
				throw new Exception( "Base64 works only with requiredPageCount equals to 1" );
			}

			if( settings.exportType != ExportType.JPG ) {
				throw new Exception( "Base64 works only with exportType Jpg" );
			}
		}
	}

	private static void parsePrimitives( ReadableMap readableMap, ImageCaptureSettings settings )
	{
		if( readableMap.hasKey( FLASHLIGHT_BUTTON ) ) {
			settings.isFlashlightButtonVisible = readableMap.getBoolean( FLASHLIGHT_BUTTON );
		}

		if( readableMap.hasKey( CAPTURE_BUTTON ) ) {
			settings.isCaptureButtonVisible = readableMap.getBoolean( CAPTURE_BUTTON );
		}

		if( readableMap.hasKey( SHOW_PREVIEW ) ) {
			settings.isShowPreviewEnabled = readableMap.getBoolean( SHOW_PREVIEW );
		}

		if( readableMap.hasKey( GALLERY_BUTTON ) ) {
			settings.isGalleryButtonVisible = readableMap.getBoolean( GALLERY_BUTTON );
		}

		if( readableMap.hasKey( REQUIRED_IMAGE_COUNT ) ) {
			settings.requiredPageCount = readableMap.getInt( REQUIRED_IMAGE_COUNT );
		}
	}

	private static <T> T parseEnumValue(
		ReadableMap readableMap,
		String key,
		Map<String, T> values,
		T defaultValue
	) throws Exception
	{
		if( !readableMap.hasKey( key ) ) {
			return defaultValue;
		}

		String value = readableMap.getString( key );
		if( value == null ) {
			throw new Exception( "Null value for key '" + key + "'" );
		}

		// Value is case insensitive
		value = value.toLowerCase();
		if( values.containsKey( value ) ) {
			return values.get( value );
		} else {
			throw new Exception( "Unknown value '" + value + "' for key '" + key + "'" );
		}
	}

	private static void parseDefaultImageSettingsObject(
		ReadableMap readableMap,
		ImageCaptureSettings settings
	) throws Exception
	{
		if( readableMap.hasKey( DEFAULT_IMAGE_SETTINGS ) ) {
			ReadableMap defaultImageSettings = readableMap.getMap( DEFAULT_IMAGE_SETTINGS );
			if( defaultImageSettings != null ) {
				parseDefaultImageSettingsValues( defaultImageSettings, settings );
			}
		}
	}

	private static void parseDefaultImageSettingsValues(
		@NonNull ReadableMap defaultImageSettings,
		ImageCaptureSettings settings
	) throws Exception
	{
		if( defaultImageSettings.hasKey( MINIMUM_DOCUMENT_TO_VIEW_RATIO ) ) {
			if( defaultImageSettings.isNull( MINIMUM_DOCUMENT_TO_VIEW_RATIO ) ) {
				throw new Exception( "minimumDocumentToViewRatio is null" );
			}

			settings.minimumDocumentToViewRatio = (float) defaultImageSettings.getDouble(
				MINIMUM_DOCUMENT_TO_VIEW_RATIO
			);
		}

		if( defaultImageSettings.hasKey( DOCUMENT_SIZE ) ) {
			String documentSize = defaultImageSettings.getString( DOCUMENT_SIZE );
			if( documentSize == null ) {
				throw new Exception( "documentSize is null" );
			}

			parseDocumentSize( defaultImageSettings, documentSize, settings );
		}

		if( defaultImageSettings.hasKey( ASPECT_RATIO_MIN ) ) {
			settings.aspectRatioMin = (float) defaultImageSettings.getDouble( ASPECT_RATIO_MIN );
		}

		if( defaultImageSettings.hasKey( ASPECT_RATIO_MAX ) ) {
			settings.aspectRatioMax = (float) defaultImageSettings.getDouble( ASPECT_RATIO_MAX );
		}

		if( defaultImageSettings.hasKey( IMAGE_FROM_GALLERY_MAX_SIZE ) ) {
			settings.imageFromGalleryMaxSize = defaultImageSettings.getInt( IMAGE_FROM_GALLERY_MAX_SIZE );
		}
	}

	private static void parseDocumentSize(
		@NonNull ReadableMap defaultImageSettings,
		@NonNull String documentSize,
		ImageCaptureSettings settings
	) throws Exception
	{
		String[] parts = documentSize.split( "x" );
		if( parts.length == 1 ) {
			settings.documentSize =
				parseEnumValue( defaultImageSettings, DOCUMENT_SIZE, DOCUMENT_SIZE_VALUES, DocumentSize.ANY );
		} else if( parts.length == 2 ) {
			float width = Float.parseFloat( parts[0] );
			float height = Float.parseFloat( parts[1] );
			settings.documentSize = new DocumentSize( width, height );
		} else {
			throw new Exception( "Can't parse document size: " + documentSize );
		}
	}

}
