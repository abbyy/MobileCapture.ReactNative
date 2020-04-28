// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

package com.abbyy.mobile.rtr.javascript.utils;

import android.graphics.Rect;

import com.abbyy.mobile.rtr.Language;
import com.facebook.react.bridge.JavaOnlyArray;
import com.facebook.react.bridge.WritableArray;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class SettingsParserUtilsTest {

	private static final Language[] DEFAULT_LANGUAGES = new Language[] { Language.English };
	private static final Rect DEFAULT_AREA_OF_INTEREST = new Rect( 1, 2, 3, 4 );
	private static final String DEFAULT_LICENSE_FILENAME = "MobileCapture.License";

	private JSONObject settings = new JSONObject();

	@Test
	public void parseAreaOfInterest_onNullAreaSettings_ItReturnsDefaultValue() throws Exception
	{
		Rect rect = SettingsParserUtils.parseAreaOfInterest( settings, DEFAULT_AREA_OF_INTEREST );

		assertThat( rect, is( equalTo( DEFAULT_AREA_OF_INTEREST ) ) );
	}

	@Test
	public void parseAreaOfInterest_onCorrectArea_ItShouldParse() throws Exception
	{
		JSONObject map = new JSONObject();
		map.put( "top", 20 );
		map.put( "left", 10 );
		map.put( "right", 100 );
		map.put( "bottom", 110 );

		settings.put( "areaOfInterest", map );

		Rect rect = SettingsParserUtils.parseAreaOfInterest( settings, DEFAULT_AREA_OF_INTEREST );

		assertThat( rect, is( equalTo( new Rect( 10, 20, 100, 110 ) ) ) );
	}

	@Test( expected = Exception.class )
	public void parseAreaOfInterest_onIncorrectArea_ItShouldThrowException() throws Exception
	{
		settings.put( "areaOfInterest", new JSONObject() );

		SettingsParserUtils.parseAreaOfInterest( settings, DEFAULT_AREA_OF_INTEREST );
	}

	@Test( expected = IllegalArgumentException.class )
	public void parseImageUri_onNotExistingParameter_ItShouldThrowException() throws Exception
	{
		SettingsParserUtils.parseImageUri( settings );
	}

	@Test( expected = IllegalArgumentException.class )
	public void parseImageUri_onNullParameter_ItShouldThrowException() throws Exception
	{
		settings.put( "imageUri", null );
		SettingsParserUtils.parseImageUri( settings );
	}

	@Test
	public void parseImageUri_onExistingParameter_ItShouldNotThrow() throws Exception
	{
		settings.put( "imageUri", "test" );
		String imageUri = SettingsParserUtils.parseImageUri( settings );

		assertEquals( imageUri, "test" );
	}

	@Test
	public void parseLicenseFilename_onNullSettings_ItShouldReturnDefaultValue() throws Exception
	{
		String licenseFilename = SettingsParserUtils.parseLicenseFilename( null );

		Assert.assertThat( licenseFilename, is( equalTo( DEFAULT_LICENSE_FILENAME ) ) );
	}

	@Test
	public void parseLicenseFilename_onEmptySettings_ItShouldReturnDefaultValue() throws Exception
	{
		String licenseFilename = SettingsParserUtils.parseLicenseFilename( settings );

		Assert.assertThat( licenseFilename, is( equalTo( DEFAULT_LICENSE_FILENAME ) ) );
	}

	@Test
	public void parseLicenseFilename_onCorrectLicenseSettings_ItShouldReturnDefaultValue() throws Exception
	{
		settings.put( "licenseFileName", "license" );

		String licenseFilename = SettingsParserUtils.parseLicenseFilename( settings );

		Assert.assertThat( licenseFilename, is( equalTo( "license" ) ) );
	}

	@Ignore // false -> "false". Is it okay?
	@Test( expected = Exception.class )
	public void parseLicenseFilename_onIncorrectLicenseSettings_ItShouldReturnDefaultValue() throws Exception
	{
		settings.put( "licenseFileName", false );

		SettingsParserUtils.parseLicenseFilename( settings );
	}

	@Test
	public void parseLanguages_onCorrectLanguages_itShouldParseThem() throws Exception
	{
		JSONArray languages = new JSONArray();
		languages.put( "English" );
		languages.put( "Russian" );
		settings.put( "recognitionLanguages", languages );

		Language[] parsedLanguages = SettingsParserUtils.parseLanguages( settings, DEFAULT_LANGUAGES );

		assertArrayEquals( parsedLanguages, new Language[] { Language.English, Language.Russian } );
	}

	@Test( expected = Exception.class )
	public void parseLanguages_onUnknownLanguages_itShouldThrowException() throws Exception
	{
		WritableArray languages = new JavaOnlyArray();
		languages.pushString( "Język Wspólny" );
		settings.put( "recognitionLanguages", languages );

		SettingsParserUtils.parseLanguages( settings, DEFAULT_LANGUAGES );
	}

	@Test( expected = Exception.class )
	public void parseLanguages_onZeroLanguages_itShouldThrowException() throws Exception
	{
		WritableArray languages = new JavaOnlyArray();
		settings.put( "recognitionLanguages", languages );

		SettingsParserUtils.parseLanguages( settings, DEFAULT_LANGUAGES );
	}

	@Test( expected = Exception.class )
	public void checkForNullSettings_onNullSettings_itShouldThrowException() throws Exception
	{
		SettingsParserUtils.checkForNullSettings( null );
	}

	@Test
	public void checkForNullSettings_onNotNullSettings_itShouldNotThrowException() throws Exception
	{
		SettingsParserUtils.checkForNullSettings( settings );
	}

}
