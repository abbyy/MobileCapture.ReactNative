// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

package com.abbyy.mobile.rtr.javascript.image;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.Size;

import com.abbyy.mobile.rtr.IImagingCoreAPI.ExportOperation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static com.abbyy.mobile.rtr.IImagingCoreAPI.DetectDocumentBoundaryOperation.DetectionMode.Default;
import static com.abbyy.mobile.rtr.IImagingCoreAPI.DetectDocumentBoundaryOperation.DetectionMode.Fast;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class SettingsParserTest {

	private static final JSONArray DOCUMENT_BOUNDARY_JSON_ARRAY;
	private static final Point[] DOCUMENT_BOUNDARY;

	private static final JSONObject SIZE_JSON_OBJECT;
	private static final Size SIZE;

	static {
		DOCUMENT_BOUNDARY = new Point[4];
		DOCUMENT_BOUNDARY[0] = new Point( 0, 1 );
		DOCUMENT_BOUNDARY[1] = new Point( 2, 3 );
		DOCUMENT_BOUNDARY[2] = new Point( 4, 5 );
		DOCUMENT_BOUNDARY[3] = new Point( 6, 7 );

		DOCUMENT_BOUNDARY_JSON_ARRAY = new JSONArray();
		for( Point point : DOCUMENT_BOUNDARY ) {
			JSONObject pointObject = new JSONObject();
			try {
				pointObject.put( "x", point.x );
				pointObject.put( "y", point.y );
			} catch( JSONException e ) {
				throw new RuntimeException( "Can't create point json object", e );
			}
			DOCUMENT_BOUNDARY_JSON_ARRAY.put( pointObject );
		}

		SIZE = new Size( 30, 40 );
		SIZE_JSON_OBJECT = new JSONObject();
		try {
			SIZE_JSON_OBJECT.put( "width", SIZE.getWidth() );
			SIZE_JSON_OBJECT.put( "height", SIZE.getHeight() );
		} catch( JSONException e ) {
			throw new RuntimeException( "Can't create size json object", e );
		}
	}

	private JSONObject jsonObject = new JSONObject();

	@Test
	public void detect_onEmptySettings_itShouldReturnDefaultSettings() throws Exception
	{
		DetectDocumentBoundarySettings settings = SettingsParser.parseDetectDocumentBoundarySettings( jsonObject );
		assertEquals( new DetectDocumentBoundarySettings(), settings );
	}

	@Test
	public void detect_onDefaultDetectionMode_itShouldParseIt() throws Exception
	{
		jsonObject.put( "detectionMode", "Default" );
		DetectDocumentBoundarySettings settings = SettingsParser.parseDetectDocumentBoundarySettings( jsonObject );
		assertEquals( Default, settings.detectionMode );
	}

	@Test
	public void detect_onFastDetectionMode_itShouldParseIt() throws Exception
	{
		jsonObject.put( "detectionMode", "Fast" );
		DetectDocumentBoundarySettings settings = SettingsParser.parseDetectDocumentBoundarySettings( jsonObject );
		assertEquals( Fast, settings.detectionMode );
	}

	@Test( expected = Exception.class )
	public void detect_onUnknownDetectionMode_itShouldThrowException() throws Exception
	{
		jsonObject.put( "detectionMode", "Unknown" );
		SettingsParser.parseDetectDocumentBoundarySettings( jsonObject );
	}

	@Test
	public void detect_onNotEmptyAreaOfInterest_itShouldParseIt() throws Exception
	{
		JSONObject areaOfInterestObject = new JSONObject();
		areaOfInterestObject.put( "top", 1 );
		areaOfInterestObject.put( "bottom", 2 );
		areaOfInterestObject.put( "left", 3 );
		areaOfInterestObject.put( "right", 4 );
		jsonObject.put( "areaOfInterest", areaOfInterestObject );

		DetectDocumentBoundarySettings settings = SettingsParser.parseDetectDocumentBoundarySettings( jsonObject );

		assertEquals( new Rect( 3, 1, 4, 2 ), settings.areaOfInterest );
	}

	@Test
	public void detect_onDocumentSize_itShouldParseIt() throws Exception
	{

		jsonObject.put( "documentSize", SIZE_JSON_OBJECT );

		DetectDocumentBoundarySettings settings = SettingsParser.parseDetectDocumentBoundarySettings( jsonObject );

		assertEquals( SIZE.getWidth(), settings.documentWidth, 1e-5 );
		assertEquals( SIZE.getHeight(), settings.documentHeight, 1e-5 );
	}

	@Test
	public void export_onEmptySettings_itShouldReturnDefaultSettings() throws Exception
	{
		ExportSettings settings = SettingsParser.parseExportSettings( jsonObject );

		assertEquals( new ExportSettings(), settings );
	}

	@Test
	public void export_onEmptyResultObjectInSettings_itShouldReturnDefaultSettings() throws Exception
	{
		JSONObject resultObject = new JSONObject();
		jsonObject.put( "result", resultObject );
		ExportSettings settings = SettingsParser.parseExportSettings( jsonObject );

		assertEquals( new ExportSettings(), settings );
	}

	@Test
	public void export_onHighCompressionLevel_itShouldParseIt() throws Exception
	{
		JSONObject resultObject = new JSONObject();
		resultObject.put( "compressionLevel", "high" );
		jsonObject.put( "result", resultObject );
		ExportSettings settings = SettingsParser.parseExportSettings( jsonObject );

		assertEquals( ExportOperation.Compression.High, settings.compression );
	}

	@Test
	public void export_onFileDestinationWithFilePath_itShouldParseIt() throws Exception
	{
		JSONObject resultObject = new JSONObject();
		resultObject.put( "destination", "File" );
		resultObject.put( "filePath", "path" );
		jsonObject.put( "result", resultObject );
		ExportSettings settings = SettingsParser.parseExportSettings( jsonObject );

		assertEquals( Destination.File, settings.destination );
		assertEquals( "path", settings.filePath );
	}

	@Test
	public void export_onBase64Destination_itShouldParseIt() throws Exception
	{
		JSONObject resultObject = new JSONObject();
		resultObject.put( "destination", "Base64" );
		jsonObject.put( "result", resultObject );
		ExportSettings settings = SettingsParser.parseExportSettings( jsonObject );

		assertEquals( Destination.Base64, settings.destination );
	}

	@Test
	public void export_onJpgExportType_itShouldParseIt() throws Exception
	{
		JSONObject resultObject = new JSONObject();
		resultObject.put( "exportType", "Jpg" );
		jsonObject.put( "result", resultObject );
		ExportSettings settings = SettingsParser.parseExportSettings( jsonObject );

		assertEquals( ExportType.JPG, settings.exportType );
	}

	@Test
	public void export_onPngExportType_itShouldParseIt() throws Exception
	{
		JSONObject resultObject = new JSONObject();
		resultObject.put( "exportType", "Png" );
		jsonObject.put( "result", resultObject );
		ExportSettings settings = SettingsParser.parseExportSettings( jsonObject );

		assertEquals( ExportType.PNG, settings.exportType );
	}

	@Test( expected = Exception.class )
	public void export_onUnknownExportType_itThrowException() throws Exception
	{
		JSONObject resultObject = new JSONObject();
		resultObject.put( "exportType", "Pdf" );
		jsonObject.put( "result", resultObject );
		SettingsParser.parseExportSettings( jsonObject );
	}

	@Test( expected = Exception.class )
	public void crop_onEmptySettings_itShouldThrowException() throws Exception
	{
		SettingsParser.parseCropSettings( jsonObject );
	}

	@Test
	public void crop_onDocumentBoundary_itShouldParseIt() throws Exception
	{
		jsonObject.put( "documentBoundary", DOCUMENT_BOUNDARY_JSON_ARRAY );
		CropSettings settings = SettingsParser.parseCropSettings( jsonObject );
		assertArrayEquals( DOCUMENT_BOUNDARY, settings.documentBoundary );
	}

	@Test
	public void crop_onDocumentBoundaryWithDocumentSize_itShouldParseIt() throws Exception
	{
		jsonObject.put( "documentBoundary", DOCUMENT_BOUNDARY_JSON_ARRAY );
		jsonObject.put( "documentSize", SIZE_JSON_OBJECT );

		CropSettings settings = SettingsParser.parseCropSettings( jsonObject );

		assertEquals( SIZE.getWidth(), settings.documentWidth, 1e-5 );
		assertEquals( SIZE.getHeight(), settings.documentHeight, 1e-5 );
	}

	@Test( expected = Exception.class )
	public void rotate_onEmptySettings_itShouldThrowException() throws Exception
	{
		SettingsParser.parseRotateSettings( jsonObject );
	}

	@Test
	public void rotate_onAngle_itShouldParseIt() throws Exception
	{
		jsonObject.put( "angle", 90 );

		RotateSettings settings = SettingsParser.parseRotateSettings( jsonObject );

		assertEquals( 90, settings.angle );
	}

	@Test
	public void quality_onEmptySettings_itShouldReturnDefaultSettings() throws Exception
	{
		QualityAssessmentForOcrSettings settings = SettingsParser.parseQualityAssessmentForOcrSettings( jsonObject );

		assertEquals( new QualityAssessmentForOcrSettings(), settings );
	}

	@Test
	public void quality_onDocumentBoundary_itShouldParseIt() throws Exception
	{
		jsonObject.put( "documentBoundary", DOCUMENT_BOUNDARY_JSON_ARRAY );

		QualityAssessmentForOcrSettings settings = SettingsParser.parseQualityAssessmentForOcrSettings( jsonObject );

		assertArrayEquals( DOCUMENT_BOUNDARY, settings.documentBoundary );
	}

	@Test( expected = Exception.class )
	public void pdf_onEmptySettings_itShouldThrowException() throws Exception
	{
		SettingsParser.parsePdfSettings( jsonObject );
	}

	@Test
	public void pdf_onImages_itShouldParseIt() throws Exception
	{
		JSONArray imagesArray = new JSONArray();
		JSONObject imageObject = new JSONObject();
		imageObject.put( "compressionLevel", "Normal" );
		imageObject.put( "imageUri", "uri" );
		imageObject.put( "pageSize", SIZE_JSON_OBJECT );
		imagesArray.put( imageObject );
		jsonObject.put( "images", imagesArray );

		PdfSettings settings = SettingsParser.parsePdfSettings( jsonObject );

		PdfSettings.ImageSettings imageSettings = settings.images[0];
		assertEquals( ExportOperation.Compression.Normal, imageSettings.compression );
		assertEquals( "uri", imageSettings.imageUri );
		assertEquals( SIZE.getWidth(), imageSettings.pageWidth );
		assertEquals( SIZE.getHeight(), imageSettings.pageHeight );
	}

	@Test( expected = Exception.class )
	public void pdf_onEmptyImages_itShouldThrowException() throws Exception
	{
		JSONArray imagesArray = new JSONArray();
		jsonObject.put( "images", imagesArray );

		SettingsParser.parsePdfSettings( jsonObject );
	}

	@Test( expected = Exception.class )
	public void pdf_onImageWithoutUri_itShouldThrowException() throws Exception
	{
		JSONArray imagesArray = new JSONArray();
		JSONObject imageObject = new JSONObject();
		imagesArray.put( imageObject );
		jsonObject.put( "images", imagesArray );

		SettingsParser.parsePdfSettings( jsonObject );
	}

	@Test
	public void pdf_onPdfInfo_itShouldParseIt() throws Exception
	{
		JSONArray imagesArray = new JSONArray();
		JSONObject imageObject = new JSONObject();
		imageObject.put( "imageUri", "uri" );
		imagesArray.put( imageObject );
		jsonObject.put( "images", imagesArray );
		JSONObject pdfInfoObject = new JSONObject();
		pdfInfoObject.put( "title", "1" );
		pdfInfoObject.put( "subject", "2" );
		pdfInfoObject.put( "keywords", "3" );
		pdfInfoObject.put( "author", "4" );
		pdfInfoObject.put( "company", "5" );
		pdfInfoObject.put( "creator", "6" );
		pdfInfoObject.put( "producer", "7" );
		jsonObject.put( "pdfInfo", pdfInfoObject );

		PdfSettings settings = SettingsParser.parsePdfSettings( jsonObject );
		assertEquals( "1", settings.pdfInfoTitle );
		assertEquals( "2", settings.pdfInfoSubject );
		assertEquals( "3", settings.pdfInfoKeywords );
		assertEquals( "4", settings.pdfInfoAuthor );
		assertEquals( "5", settings.pdfInfoCompany );
		assertEquals( "6", settings.pdfInfoCreator );
		assertEquals( "7", settings.pdfInfoProducer );
	}

	@Test
	public void pdf_onFilePath_itShouldParseIt() throws Exception
	{
		JSONArray imagesArray = new JSONArray();
		JSONObject imageObject = new JSONObject();
		imageObject.put( "imageUri", "uri" );
		imagesArray.put( imageObject );
		jsonObject.put( "images", imagesArray );
		JSONObject resultObject = new JSONObject();
		resultObject.put( "destination", "File" );
		resultObject.put( "filePath", "path" );
		jsonObject.put( "result", resultObject );

		PdfSettings settings = SettingsParser.parsePdfSettings( jsonObject );
		assertEquals( Destination.File, settings.destination );
		assertEquals( "path", settings.filePath );
	}

	@Test( expected = Exception.class )
	public void pdf_onBase64AndTwoImages_itShouldThrowException() throws Exception
	{
		JSONArray imagesArray = new JSONArray();
		JSONObject imageObject = new JSONObject();
		imageObject.put( "imageUri", "uri" );
		imagesArray.put( imageObject );
		imagesArray.put( imageObject );
		jsonObject.put( "images", imagesArray );
		JSONObject resultObject = new JSONObject();
		resultObject.put( "destination", "Base64" );
		jsonObject.put( "result", resultObject );

		SettingsParser.parsePdfSettings( jsonObject );
	}

}
