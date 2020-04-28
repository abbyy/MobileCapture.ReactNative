// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

package com.abbyy.mobile.rtr.javascript.data;

import com.abbyy.mobile.rtr.Language;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class SettingsParserTest {

	private JSONObject jsonObject = new JSONObject();

	@Test
	public void onEmptySettings_ItShouldReturnDefaultSettings() throws Exception
	{
		Settings settings = SettingsParser.parse( jsonObject );
		assertThat( settings, is( equalTo( new Settings() ) ) );
	}

	@Test
	public void onFullSettings_ItShouldParseProperly() throws Exception
	{
		JSONArray languages = new JSONArray();
		languages.put( "English" );
		languages.put( "Russian" );
		jsonObject.put( "recognitionLanguages", languages );
		jsonObject.put( "isTextOrientationDetectionEnabled", false );
		jsonObject.put( "profile", "test" );

		Settings settings = SettingsParser.parse( jsonObject );

		Settings expectedSettings = new Settings();
		expectedSettings.isTextOrientationDetectionEnabled = false;
		expectedSettings.profile = "test";
		expectedSettings.languages = new Language[] { Language.English, Language.Russian };
		assertEquals( expectedSettings, settings );
	}

}
