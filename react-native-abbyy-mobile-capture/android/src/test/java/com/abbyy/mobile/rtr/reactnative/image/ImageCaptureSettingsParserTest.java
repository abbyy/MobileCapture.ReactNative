// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

package com.abbyy.mobile.rtr.reactnative.image;

import android.content.pm.ActivityInfo;

import com.abbyy.mobile.rtr.IImagingCoreAPI.ExportOperation.Compression;
import com.abbyy.mobile.uicomponents.CaptureView.CameraSettings.Resolution;
import com.abbyy.mobile.uicomponents.scenario.ImageCaptureScenario.DocumentSize;

import org.json.JSONObject;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ImageCaptureSettingsParserTest {

	private static final ImageCaptureSettings DEFAULT_SETTINGS = new ImageCaptureSettings();

	private JSONObject jsonObject = new JSONObject();

	@Test
	public void nullReadableMap_defaultSettings() throws Exception
	{
		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( null );
		assertThat( settings, is( equalTo( DEFAULT_SETTINGS ) ) );
	}

	@Test
	public void emptyReadableMap_defaultSettings() throws Exception
	{
		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );
		assertThat( settings, is( equalTo( DEFAULT_SETTINGS ) ) );
	}

	@Test
	public void isFlashlightButtonVisible_true() throws Exception
	{
		jsonObject.put( "isFlashlightButtonVisible", true );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.isFlashlightButtonVisible, is( true ) );
	}

	@Test
	public void isFlashlightButtonVisible_false() throws Exception
	{
		jsonObject.put( "isFlashlightButtonVisible", false );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.isFlashlightButtonVisible, is( false ) );
	}

	@Test( expected = Exception.class )
	public void isFlashlightButtonVisible_wrongType() throws Exception
	{
		jsonObject.put( "isFlashlightButtonVisible", 42 );

		// Should throw exception
		ImageCaptureSettingsParser.parse( jsonObject );
	}

	@Test
	public void isCaptureButtonVisible_true() throws Exception
	{
		jsonObject.put( "isCaptureButtonVisible", true );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.isCaptureButtonVisible, is( true ) );
	}

	@Test
	public void isCaptureButtonVisible_false() throws Exception
	{
		jsonObject.put( "isCaptureButtonVisible", false );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.isCaptureButtonVisible, is( false ) );
	}

	@Test
	public void isShowPreviewEnabled_true() throws Exception
	{
		jsonObject.put( "isShowPreviewEnabled", true );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.isShowPreviewEnabled, is( true ) );
	}

	@Test
	public void isShowPreviewEnabled_false() throws Exception
	{
		jsonObject.put( "isShowPreviewEnabled", false );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.isShowPreviewEnabled, is( false ) );
	}

	@Test
	public void isGalleryButtonVisible_true() throws Exception
	{
		jsonObject.put( "isGalleryButtonVisible", true );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.isGalleryButtonVisible, is( true ) );
	}

	@Test
	public void isGalleryButtonVisible_false() throws Exception
	{
		jsonObject.put( "isGalleryButtonVisible", false );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.isGalleryButtonVisible, is( false ) );
	}

	@Test
	public void cameraResolution_HD() throws Exception
	{
		jsonObject.put( "cameraResolution", "HD" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.cameraResolution, is( Resolution.HD ) );
	}

	@Test
	public void cameraResolution_FULL_HD() throws Exception
	{
		jsonObject.put( "cameraResolution", "FullHD" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.cameraResolution, is( Resolution.FULL_HD ) );
	}

	@Test
	public void cameraResolution_UHD_4K() throws Exception
	{
		jsonObject.put( "cameraResolution", "UHD_4K" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.cameraResolution, is( Resolution.UHD_4K ) );
	}

	@Test( expected = Exception.class )
	public void enumValues_wrongType() throws Exception
	{
		jsonObject.put( "cameraResolution", false );

		// Should throw exception
		ImageCaptureSettingsParser.parse( jsonObject );
	}

	@Test( expected = Exception.class )
	public void enumValues_unknownValue() throws Exception
	{
		jsonObject.put( "cameraResolution", "8K" );

		// Should throw exception
		ImageCaptureSettingsParser.parse( jsonObject );
	}

	@Test
	public void enumValues_caseInsensitive() throws Exception
	{
		jsonObject.put( "cameraResolution", "fUlLhD" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.cameraResolution, is( Resolution.FULL_HD ) );
	}

	@Test
	public void requiredPageCount_3() throws Exception
	{
		jsonObject.put( "requiredPageCount", 3 );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.requiredPageCount, is( 3 ) );
	}

	@Test
	public void destination_File() throws Exception
	{
		jsonObject.put( "destination", "File" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.destination, is( ImageCaptureSettings.Destination.FILE ) );
	}

	@Test
	public void destination_Base64_requiredPageCount_1_exportType_jpg() throws Exception
	{
		jsonObject.put( "destination", "Base64" );

		jsonObject.put( "requiredPageCount", 1 );
		jsonObject.put( "exportType", "Jpg" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.destination, is( ImageCaptureSettings.Destination.BASE64 ) );
	}

	@Test( expected = Exception.class )
	public void destination_Base64_requiredPageCount_0() throws Exception
	{
		jsonObject.put( "destination", "Base64" );

		jsonObject.put( "requiredPageCount", 0 );
		jsonObject.put( "exportType", "Jpg" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.destination, is( ImageCaptureSettings.Destination.BASE64 ) );
	}

	@Test( expected = Exception.class )
	public void destination_Base64_requiredPageCount_2() throws Exception
	{
		jsonObject.put( "destination", "Base64" );

		jsonObject.put( "requiredPageCount", 2 );
		jsonObject.put( "exportType", "Jpg" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.destination, is( ImageCaptureSettings.Destination.BASE64 ) );
	}

	@Test
	public void destination_Base64_exportType_Png() throws Exception
	{
		jsonObject.put( "destination", "Base64" );

		jsonObject.put( "requiredPageCount", 1 );
		jsonObject.put( "exportType", "Png" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.destination, is( ImageCaptureSettings.Destination.BASE64 ) );
	}

	@Test
	public void destination_Base64_exportType_PDF() throws Exception
	{
		jsonObject.put( "destination", "Base64" );

		jsonObject.put( "requiredPageCount", 1 );
		jsonObject.put( "exportType", "Pdf" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.destination, is( ImageCaptureSettings.Destination.BASE64 ) );
	}

	@Test
	public void exportType_Jpg() throws Exception
	{
		jsonObject.put( "exportType", "Jpg" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.exportType, is( ImageCaptureSettings.ExportType.JPG ) );
	}

	@Test
	public void exportType_Png() throws Exception
	{
		jsonObject.put( "exportType", "Png" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.exportType, is( ImageCaptureSettings.ExportType.PNG ) );
	}

	@Test
	public void exportType_Pdf() throws Exception
	{
		jsonObject.put( "exportType", "Pdf" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.exportType, is( ImageCaptureSettings.ExportType.PDF ) );
	}

	@Test
	public void compressionLevel_Low() throws Exception
	{
		jsonObject.put( "compressionLevel", "Low" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.compressionLevel, is( Compression.Low ) );
	}

	@Test
	public void compressionLevel_Normal() throws Exception
	{
		jsonObject.put( "compressionLevel", "Normal" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.compressionLevel, is( Compression.Normal ) );
	}

	@Test
	public void compressionLevel_High() throws Exception
	{
		jsonObject.put( "compressionLevel", "High" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.compressionLevel, is( Compression.High ) );
	}

	@Test
	public void compressionLevel_ExtraHigh() throws Exception
	{
		jsonObject.put( "compressionLevel", "ExtraHigh" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.compressionLevel, is( Compression.ExtraHigh ) );
	}

	@Test
	public void minimumDocumentToViewRatio_0_2() throws Exception
	{
		jsonObject.put(
			"defaultImageSettings",
			new JSONObject() {{
				put( "minimumDocumentToViewRatio", 0.2 );
			}}
		);

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.minimumDocumentToViewRatio, is( 0.2f ) );
	}

	@Test
	public void aspectRatioMin_1_2() throws Exception
	{
		jsonObject.put(
			"defaultImageSettings",
			new JSONObject() {{
				put( "aspectRatioMin", 1.2f );
			}}
		);

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.aspectRatioMin, is( 1.2f ) );
	}

	@Test
	public void aspectRatioMax_1_3() throws Exception
	{
		jsonObject.put(
			"defaultImageSettings",
			new JSONObject() {{
				put( "aspectRatioMax", 1.3f );
			}}
		);

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.aspectRatioMax, is( 1.3f ) );
	}

	@Test
	public void imageFromGalleryMaxSize_1024() throws Exception
	{
		jsonObject.put(
			"defaultImageSettings",
			new JSONObject() {{
				put( "imageFromGalleryMaxSize", 1024 );
			}}
		);

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.imageFromGalleryMaxSize, is( 1024 ) );
	}

	@Test
	public void documentSize_Any() throws Exception
	{
		jsonObject.put(
			"defaultImageSettings",
			new JSONObject() {{
				put( "documentSize", "Any" );
			}}
		);

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.documentSize, is( DocumentSize.ANY ) );
	}

	@Test
	public void documentSize_BusinessCard() throws Exception
	{
		jsonObject.put(
			"defaultImageSettings",
			new JSONObject() {{
				put( "documentSize", "BusinessCard" );
			}}
		);

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.documentSize, is( DocumentSize.BUSINESS_CARD ) );
	}

	@Test
	public void documentSize_A4() throws Exception
	{
		jsonObject.put(
			"defaultImageSettings",
			new JSONObject() {{
				put( "documentSize", "A4" );
			}}
		);

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.documentSize, is( DocumentSize.A4 ) );
	}

	@Test
	public void documentSize_Letter() throws Exception
	{
		jsonObject.put(
			"defaultImageSettings",
			new JSONObject() {{
				put( "documentSize", "Letter" );
			}}
		);

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.documentSize, is( DocumentSize.LETTER ) );
	}

	@Test
	public void documentSize_custom() throws Exception
	{
		jsonObject.put(
			"defaultImageSettings",
			new JSONObject() {{
				put( "documentSize", "12.34x56.78" );
			}}
		);

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.documentSize, is( equalTo( new DocumentSize( 12.34f, 56.78f ) ) ) );
	}

	@Test( expected = Exception.class )
	public void documentSize_customWrong() throws Exception
	{
		jsonObject.put(
			"defaultImageSettings",
			new JSONObject() {{
				put( "documentSize", "12.34x56.78x123" );
			}}
		);

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.documentSize, is( equalTo( new DocumentSize( 12.34f, 56.78f ) ) ) );
	}

	@Test
	public void orientation_Portrait() throws Exception
	{
		jsonObject.put( "orientation", "Portrait" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.orientation, is( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ) );
	}

	@Test
	public void orientation_Landscape() throws Exception
	{
		jsonObject.put( "orientation", "Landscape" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.orientation, is( ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE ) );
	}

	@Test
	public void orientation_Default() throws Exception
	{
		jsonObject.put( "orientation", "Default" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( jsonObject );

		assertThat( settings.orientation, is( ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED ) );
	}

}
