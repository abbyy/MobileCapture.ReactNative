// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

package com.abbyy.mobile.rtr.javascript.text;

import android.graphics.Rect;

import com.abbyy.mobile.rtr.Language;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SettingsParserTest {

	private JSONObject jsonObject = new JSONObject();

	@Test
	public void onEmptySettingsMap_itShouldReturnDefaultSettings() throws Exception
	{
		Settings settings = SettingsParser.parse( jsonObject );

		assertEquals( new Settings(), settings );
	}

	@Test
	public void onFullSettings_itShouldParseProperly() throws Exception
	{
		JSONObject areaOfInterest = new JSONObject();
		areaOfInterest.put( "top", 1 );
		areaOfInterest.put( "bottom", 2 );
		areaOfInterest.put( "left", 3 );
		areaOfInterest.put( "right", 4 );
		jsonObject.put( "areaOfInterest", areaOfInterest );
		JSONArray languages = new JSONArray();
		languages.put( "English" );
		languages.put( "Russian" );
		jsonObject.put( "recognitionLanguages", languages );
		jsonObject.put( "isTextOrientationDetectionEnabled", false );

		Settings settings = SettingsParser.parse( jsonObject );

		Settings expectedSettings = new Settings();
		expectedSettings.isTextOrientationDetectionEnabled = false;
		expectedSettings.areaOfInterest = new Rect( 3, 1, 4, 2 );
		expectedSettings.languages = new Language[] { Language.English, Language.Russian };
		assertEquals( expectedSettings, settings );
	}

}
