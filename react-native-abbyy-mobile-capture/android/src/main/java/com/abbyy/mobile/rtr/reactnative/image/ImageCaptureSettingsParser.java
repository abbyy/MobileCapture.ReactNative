// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

package com.abbyy.mobile.rtr.reactnative.image;

import android.content.pm.ActivityInfo;

import com.abbyy.mobile.rtr.IImagingCoreAPI.ExportOperation.Compression;
import com.abbyy.mobile.rtr.reactnative.image.ImageCaptureSettings.Destination;
import com.abbyy.mobile.rtr.reactnative.image.ImageCaptureSettings.ExportType;
import com.abbyy.mobile.uicomponents.CaptureView.CameraSettings.Resolution;
import com.abbyy.mobile.uicomponents.scenario.ImageCaptureScenario.DocumentSize;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.abbyy.mobile.rtr.javascript.utils.SettingsParserUtils.parseEnumValue;

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

	public static ImageCaptureSettings parse( @Nullable JSONObject json ) throws Exception
	{
		ImageCaptureSettings settings = new ImageCaptureSettings();
		if( json == null ) {
			return settings;
		}

		parsePrimitives( json, settings );
		parseDefaultImageSettingsObject( json, settings );

		settings.cameraResolution = parseEnumValue( json, RESOLUTION, RESOLUTION_VALUES, settings.cameraResolution );
		settings.orientation = parseEnumValue( json, ORIENTATION, ORIENTATION_VALUES, settings.orientation );
		settings.destination = parseEnumValue( json, DESTINATION, DESTINATION_VALUES, settings.destination );
		settings.exportType = parseEnumValue( json, EXPORT_TYPE, EXPORT_TYPE_VALUES, settings.exportType );
		settings.compressionLevel = parseEnumValue( json, COMPRESSION_LEVEL, COMPRESSION_LEVEL_VALUES, settings.compressionLevel );

		checkForBase64DestinationParameters( settings );

		return settings;
	}

	private static void checkForBase64DestinationParameters( ImageCaptureSettings settings ) throws Exception
	{
		if( settings.destination == Destination.BASE64 ) {
			if( settings.requiredPageCount != 1 ) {
				throw new Exception( "Base64 works only with requiredPageCount equals to 1" );
			}
		}
	}

	private static void parsePrimitives( @NonNull JSONObject jsonSettings, ImageCaptureSettings settings ) throws JSONException
	{
		if( jsonSettings.has( FLASHLIGHT_BUTTON ) ) {
			settings.isFlashlightButtonVisible = jsonSettings.getBoolean( FLASHLIGHT_BUTTON );
		}

		if( jsonSettings.has( CAPTURE_BUTTON ) ) {
			settings.isCaptureButtonVisible = jsonSettings.getBoolean( CAPTURE_BUTTON );
		}

		if( jsonSettings.has( SHOW_PREVIEW ) ) {
			settings.isShowPreviewEnabled = jsonSettings.getBoolean( SHOW_PREVIEW );
		}

		if( jsonSettings.has( GALLERY_BUTTON ) ) {
			settings.isGalleryButtonVisible = jsonSettings.getBoolean( GALLERY_BUTTON );
		}

		if( jsonSettings.has( REQUIRED_IMAGE_COUNT ) ) {
			settings.requiredPageCount = jsonSettings.getInt( REQUIRED_IMAGE_COUNT );
		}
	}

	private static void parseDefaultImageSettingsObject(
		@NonNull JSONObject jsonSettings,
		ImageCaptureSettings settings
	) throws Exception
	{
		if( jsonSettings.has( DEFAULT_IMAGE_SETTINGS ) ) {
			JSONObject defaultImageSettings = jsonSettings.getJSONObject( DEFAULT_IMAGE_SETTINGS );
			if( defaultImageSettings != null ) {
				parseDefaultImageSettingsValues( defaultImageSettings, settings );
			}
		}
	}

	private static void parseDefaultImageSettingsValues(
		@NonNull JSONObject defaultImageSettings,
		ImageCaptureSettings settings
	) throws Exception
	{
		if( defaultImageSettings.has( MINIMUM_DOCUMENT_TO_VIEW_RATIO ) ) {
			if( defaultImageSettings.isNull( MINIMUM_DOCUMENT_TO_VIEW_RATIO ) ) {
				throw new Exception( "minimumDocumentToViewRatio is null" );
			}

			settings.minimumDocumentToViewRatio = (float) defaultImageSettings.getDouble(
				MINIMUM_DOCUMENT_TO_VIEW_RATIO
			);
		}

		if( defaultImageSettings.has( DOCUMENT_SIZE ) ) {
			String documentSize = defaultImageSettings.getString( DOCUMENT_SIZE );
			if( documentSize == null ) {
				throw new Exception( "documentSize is null" );
			}

			parseDocumentSize( defaultImageSettings, documentSize, settings );
		}

		if( defaultImageSettings.has( ASPECT_RATIO_MIN ) ) {
			settings.aspectRatioMin = (float) defaultImageSettings.getDouble( ASPECT_RATIO_MIN );
		}

		if( defaultImageSettings.has( ASPECT_RATIO_MAX ) ) {
			settings.aspectRatioMax = (float) defaultImageSettings.getDouble( ASPECT_RATIO_MAX );
		}

		if( defaultImageSettings.has( IMAGE_FROM_GALLERY_MAX_SIZE ) ) {
			settings.imageFromGalleryMaxSize = defaultImageSettings.getInt( IMAGE_FROM_GALLERY_MAX_SIZE );
		}
	}

	private static void parseDocumentSize(
		@NonNull JSONObject defaultImageSettings,
		@NonNull String documentSize,
		ImageCaptureSettings settings
	) throws Exception
	{
		String[] parts = documentSize.split( "x" );
		if( parts.length == 1 ) {
			settings.documentSize = parseEnumValue( defaultImageSettings, DOCUMENT_SIZE, DOCUMENT_SIZE_VALUES, DocumentSize.ANY );
		} else if( parts.length == 2 ) {
			float width = Float.parseFloat( parts[0] );
			float height = Float.parseFloat( parts[1] );
			settings.documentSize = new DocumentSize( width, height );
		} else {
			throw new Exception( "Can't parse document size: " + documentSize );
		}
	}

}
