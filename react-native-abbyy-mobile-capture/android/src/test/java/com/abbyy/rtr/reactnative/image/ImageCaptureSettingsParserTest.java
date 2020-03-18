// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

package com.abbyy.rtr.reactnative.image;

import android.content.pm.ActivityInfo;

import com.abbyy.mobile.rtr.IImagingCoreAPI.ExportOperation.Compression;
import com.abbyy.mobile.uicomponents.CaptureView.CameraSettings.Resolution;
import com.abbyy.mobile.uicomponents.scenario.ImageCaptureScenario.DocumentSize;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ImageCaptureSettingsParserTest {

	private static final ImageCaptureSettings DEFAULT_SETTINGS = new ImageCaptureSettings();

	private RNMapMock map;

	@Before
	public void setUp()
	{
		map = new RNMapMock();
	}

	@Test
	public void nullReadableMap_defaultSettings() throws Exception
	{
		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( null );
		assertThat( settings, is( equalTo( DEFAULT_SETTINGS ) ) );
	}

	@Test
	public void emptyReadableMap_defaultSettings() throws Exception
	{
		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( new RNMapMock() );
		assertThat( settings, is( equalTo( DEFAULT_SETTINGS ) ) );
	}

	@Test
	public void isFlashlightButtonVisible_true() throws Exception
	{
		map.putBoolean( "isFlashlightButtonVisible", true );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.isFlashlightButtonVisible, is( true ) );
	}

	@Test
	public void isFlashlightButtonVisible_false() throws Exception
	{
		map.putBoolean( "isFlashlightButtonVisible", false );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.isFlashlightButtonVisible, is( false ) );
	}

	@Test( expected = Exception.class )
	public void isFlashlightButtonVisible_null() throws Exception
	{
		map.putNull( "isFlashlightButtonVisible" );

		// Should throw exception
		ImageCaptureSettingsParser.parse( map );
	}

	@Test( expected = Exception.class )
	public void isFlashlightButtonVisible_wrongType() throws Exception
	{
		map.putDouble( "isFlashlightButtonVisible", 42 );

		// Should throw exception
		ImageCaptureSettingsParser.parse( map );
	}

	@Test
	public void isCaptureButtonVisible_true() throws Exception
	{
		map.putBoolean( "isCaptureButtonVisible", true );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.isCaptureButtonVisible, is( true ) );
	}

	@Test
	public void isCaptureButtonVisible_false() throws Exception
	{
		map.putBoolean( "isCaptureButtonVisible", false );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.isCaptureButtonVisible, is( false ) );
	}

	@Test( expected = Exception.class )
	public void isCaptureButtonVisible_null() throws Exception
	{
		map.putNull( "isCaptureButtonVisible" );

		// Should throw exception
		ImageCaptureSettingsParser.parse( map );
	}

	@Test
	public void isShowPreviewEnabled_true() throws Exception
	{
		map.putBoolean( "isShowPreviewEnabled", true );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.isShowPreviewEnabled, is( true ) );
	}

	@Test
	public void isShowPreviewEnabled_false() throws Exception
	{
		map.putBoolean( "isShowPreviewEnabled", false );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.isShowPreviewEnabled, is( false ) );
	}

	@Test( expected = Exception.class )
	public void isShowPreviewEnabled_null() throws Exception
	{
		map.putNull( "isShowPreviewEnabled" );

		// Should throw exception
		ImageCaptureSettingsParser.parse( map );
	}

	@Test
	public void isGalleryButtonVisible_true() throws Exception
	{
		map.putBoolean( "isGalleryButtonVisible", true );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.isGalleryButtonVisible, is( true ) );
	}

	@Test
	public void isGalleryButtonVisible_false() throws Exception
	{
		map.putBoolean( "isGalleryButtonVisible", false );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.isGalleryButtonVisible, is( false ) );
	}

	@Test( expected = Exception.class )
	public void isGalleryButtonVisible_null() throws Exception
	{
		map.putNull( "isGalleryButtonVisible" );

		// Should throw exception
		ImageCaptureSettingsParser.parse( map );
	}

	@Test
	public void cameraResolution_HD() throws Exception
	{
		map.putString( "cameraResolution", "HD" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.cameraResolution, is( Resolution.HD ) );
	}

	@Test
	public void cameraResolution_FULL_HD() throws Exception
	{
		map.putString( "cameraResolution", "FullHD" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.cameraResolution, is( Resolution.FULL_HD ) );
	}

	@Test
	public void cameraResolution_UHD_4K() throws Exception
	{
		map.putString( "cameraResolution", "UHD_4K" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.cameraResolution, is( Resolution.UHD_4K ) );
	}

	@Test( expected = Exception.class )
	public void cameraResolution_null() throws Exception
	{
		map.putNull( "cameraResolution" );

		// Should throw exception
		ImageCaptureSettingsParser.parse( map );
	}

	@Test( expected = Exception.class )
	public void enumValues_wrongType() throws Exception
	{
		map.putBoolean( "cameraResolution", false );

		// Should throw exception
		ImageCaptureSettingsParser.parse( map );
	}

	@Test( expected = Exception.class )
	public void enumValues_unknownValue() throws Exception
	{
		map.putString( "cameraResolution", "8K" );

		// Should throw exception
		ImageCaptureSettingsParser.parse( map );
	}

	@Test
	public void enumValues_caseInsensitive() throws Exception
	{
		map.putString( "cameraResolution", "fUlLhD" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.cameraResolution, is( Resolution.FULL_HD ) );
	}

	@Test
	public void requiredPageCount_3() throws Exception
	{
		map.putInt( "requiredPageCount", 3 );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.requiredPageCount, is( 3 ) );
	}

	@Test( expected = Exception.class )
	public void requiredPageCount_null() throws Exception
	{
		map.putNull( "requiredPageCount" );

		// Should throw exception
		ImageCaptureSettingsParser.parse( map );
	}

	@Test
	public void destination_File() throws Exception
	{
		map.putString( "destination", "File" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.destination, is( ImageCaptureSettings.Destination.FILE ) );
	}

	@Test
	public void destination_Base64_requiredPageCount_1_exportType_jpg() throws Exception
	{
		map.putString( "destination", "Base64" );

		map.putInt( "requiredPageCount", 1 );
		map.putString( "exportType", "Jpg" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.destination, is( ImageCaptureSettings.Destination.BASE64 ) );
	}

	@Test( expected = Exception.class )
	public void destination_Base64_requiredPageCount_0() throws Exception
	{
		map.putString( "destination", "Base64" );

		map.putInt( "requiredPageCount", 0 );
		map.putString( "exportType", "Jpg" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.destination, is( ImageCaptureSettings.Destination.BASE64 ) );
	}

	@Test( expected = Exception.class )
	public void destination_Base64_requiredPageCount_2() throws Exception
	{
		map.putString( "destination", "Base64" );

		map.putInt( "requiredPageCount", 2 );
		map.putString( "exportType", "Jpg" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.destination, is( ImageCaptureSettings.Destination.BASE64 ) );
	}

	@Test( expected = Exception.class )
	public void destination_Base64_exportType_Png() throws Exception
	{
		map.putString( "destination", "Base64" );

		map.putInt( "requiredPageCount", 1 );
		map.putString( "exportType", "Png" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.destination, is( ImageCaptureSettings.Destination.BASE64 ) );
	}

	@Test( expected = Exception.class )
	public void destination_Base64_exportType_PDF() throws Exception
	{
		map.putString( "destination", "Base64" );

		map.putInt( "requiredPageCount", 1 );
		map.putString( "exportType", "Pdf" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.destination, is( ImageCaptureSettings.Destination.BASE64 ) );
	}

	@Test( expected = Exception.class )
	public void destination_null() throws Exception
	{
		map.putNull( "destination" );

		// Should throw exception
		ImageCaptureSettingsParser.parse( map );
	}

	@Test
	public void exportType_Jpg() throws Exception
	{
		map.putString( "exportType", "Jpg" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.exportType, is( ImageCaptureSettings.ExportType.JPG ) );
	}

	@Test
	public void exportType_Png() throws Exception
	{
		map.putString( "exportType", "Png" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.exportType, is( ImageCaptureSettings.ExportType.PNG ) );
	}

	@Test
	public void exportType_Pdf() throws Exception
	{
		map.putString( "exportType", "Pdf" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.exportType, is( ImageCaptureSettings.ExportType.PDF ) );
	}

	@Test( expected = Exception.class )
	public void exportType_null() throws Exception
	{
		map.putNull( "exportType" );

		// Should throw exception
		ImageCaptureSettingsParser.parse( map );
	}

	@Test
	public void compressionLevel_Low() throws Exception
	{
		map.putString( "compressionLevel", "Low" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.compressionLevel, is( Compression.Low ) );
	}

	@Test
	public void compressionLevel_Normal() throws Exception
	{
		map.putString( "compressionLevel", "Normal" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.compressionLevel, is( Compression.Normal ) );
	}

	@Test
	public void compressionLevel_High() throws Exception
	{
		map.putString( "compressionLevel", "High" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.compressionLevel, is( Compression.High ) );
	}

	@Test
	public void compressionLevel_ExtraHigh() throws Exception
	{
		map.putString( "compressionLevel", "ExtraHigh" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.compressionLevel, is( Compression.ExtraHigh ) );
	}

	@Test( expected = Exception.class )
	public void compressionLevel_null() throws Exception
	{
		map.putNull( "compressionLevel" );

		// Should throw exception
		ImageCaptureSettingsParser.parse( map );
	}

	@Test
	public void minimumDocumentToViewRatio_0_2() throws Exception
	{
		map.putMap(
			"defaultImageSettings",
			new RNMapMock() {{
				putDouble( "minimumDocumentToViewRatio", 0.2 );
			}}
		);

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.minimumDocumentToViewRatio, is( 0.2f ) );
	}

	@Test( expected = Exception.class )
	public void minimumDocumentToViewRatio_null() throws Exception
	{
		map.putMap(
			"defaultImageSettings",
			new RNMapMock() {{
				putNull( "minimumDocumentToViewRatio" );
			}}
		);

		// Should throw exception
		ImageCaptureSettingsParser.parse( map );
	}

	@Test
	public void aspectRatioMin_1_2() throws Exception
	{
		map.putMap(
			"defaultImageSettings",
			new RNMapMock() {{
				putDouble( "aspectRatioMin", 1.2f );
			}}
		);

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.aspectRatioMin, is( 1.2f ) );
	}

	@Test( expected = Exception.class )
	public void aspectRatioMin_null() throws Exception
	{
		map.putMap(
			"defaultImageSettings",
			new RNMapMock() {{
				putNull( "aspectRatioMin" );
			}}
		);

		// Should throw exception
		ImageCaptureSettingsParser.parse( map );
	}

	@Test
	public void aspectRatioMax_1_3() throws Exception
	{
		map.putMap(
			"defaultImageSettings",
			new RNMapMock() {{
				putDouble( "aspectRatioMax", 1.3f );
			}}
		);

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.aspectRatioMax, is( 1.3f ) );
	}

	@Test( expected = Exception.class )
	public void aspectRatioMax_null() throws Exception
	{
		map.putMap(
			"defaultImageSettings",
			new RNMapMock() {{
				putNull( "aspectRatioMax" );
			}}
		);

		// Should throw exception
		ImageCaptureSettingsParser.parse( map );
	}

	@Test
	public void imageFromGalleryMaxSize_1024() throws Exception
	{
		map.putMap(
			"defaultImageSettings",
			new RNMapMock() {{
				putInt( "imageFromGalleryMaxSize", 1024 );
			}}
		);

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.imageFromGalleryMaxSize, is( 1024 ) );
	}

	@Test( expected = Exception.class )
	public void imageFromGalleryMaxSize_null() throws Exception
	{
		map.putMap(
			"defaultImageSettings",
			new RNMapMock() {{
				putNull( "imageFromGalleryMaxSize" );
			}}
		);

		// Should throw exception
		ImageCaptureSettingsParser.parse( map );
	}

	@Test
	public void documentSize_Any() throws Exception
	{
		map.putMap(
			"defaultImageSettings",
			new RNMapMock() {{
				putString( "documentSize", "Any" );
			}}
		);

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.documentSize, is( DocumentSize.ANY ) );
	}

	@Test
	public void documentSize_BusinessCard() throws Exception
	{
		map.putMap(
			"defaultImageSettings",
			new RNMapMock() {{
				putString( "documentSize", "BusinessCard" );
			}}
		);

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.documentSize, is( DocumentSize.BUSINESS_CARD ) );
	}

	@Test
	public void documentSize_A4() throws Exception
	{
		map.putMap(
			"defaultImageSettings",
			new RNMapMock() {{
				putString( "documentSize", "A4" );
			}}
		);

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.documentSize, is( DocumentSize.A4 ) );
	}

	@Test
	public void documentSize_Letter() throws Exception
	{
		map.putMap(
			"defaultImageSettings",
			new RNMapMock() {{
				putString( "documentSize", "Letter" );
			}}
		);

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.documentSize, is( DocumentSize.LETTER ) );
	}

	@Test
	public void documentSize_custom() throws Exception
	{
		map.putMap(
			"defaultImageSettings",
			new RNMapMock() {{
				putString( "documentSize", "12.34x56.78" );
			}}
		);

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.documentSize, is( equalTo( new DocumentSize( 12.34f, 56.78f ) ) ) );
	}

	@Test( expected = Exception.class )
	public void documentSize_customWrong() throws Exception
	{
		map.putMap(
			"defaultImageSettings",
			new RNMapMock() {{
				putString( "documentSize", "12.34x56.78x123" );
			}}
		);

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.documentSize, is( equalTo( new DocumentSize( 12.34f, 56.78f ) ) ) );
	}

	@Test( expected = Exception.class )
	public void documentSize_null() throws Exception
	{
		map.putMap(
			"defaultImageSettings",
			new RNMapMock() {{
				putNull( "documentSize" );
			}}
		);

		// Should throw exception
		ImageCaptureSettingsParser.parse( map );
	}

	@Test
	public void orientation_Portrait() throws Exception
	{
		map.putString( "orientation", "Portrait" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.orientation, is( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ) );
	}

	@Test
	public void orientation_Landscape() throws Exception
	{
		map.putString( "orientation", "Landscape" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.orientation, is( ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE ) );
	}

	@Test
	public void orientation_Default() throws Exception
	{
		map.putString( "orientation", "Default" );

		ImageCaptureSettings settings = ImageCaptureSettingsParser.parse( map );

		assertThat( settings.orientation, is( ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED ) );
	}

	@Test( expected = Exception.class )
	public void orientation_null() throws Exception
	{
		map.putNull( "orientation" );

		// Should throw exception
		ImageCaptureSettingsParser.parse( map );
	}

}
