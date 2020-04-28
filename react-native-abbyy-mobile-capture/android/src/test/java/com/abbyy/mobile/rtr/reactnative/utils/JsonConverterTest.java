// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

package com.abbyy.mobile.rtr.reactnative.utils;

import com.facebook.react.bridge.JavaOnlyArray;
import com.facebook.react.bridge.JavaOnlyMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JsonConverterTest {

	@Test
	public void toJson_nullObject() throws Exception
	{
		assertNull( JsonConverter.toJSON( null ) );
	}

	@Test
	public void toJson_int() throws Exception
	{
		WritableMap writableMap = new JavaOnlyMap();
		writableMap.putInt( "int", 42 );

		JSONObject jsonObject = JsonConverter.toJSON( writableMap );

		assertEquals( 42, jsonObject.getInt( "int" ) );
	}

	@Test
	public void toJson_string() throws Exception
	{
		WritableMap writableMap = new JavaOnlyMap();
		writableMap.putString( "string", "abc" );

		JSONObject jsonObject = JsonConverter.toJSON( writableMap );

		assertEquals( "abc", jsonObject.getString( "string" ) );
	}

	@Test
	public void toJson_boolean() throws Exception
	{
		WritableMap writableMap = new JavaOnlyMap();
		writableMap.putBoolean( "bool", true );

		JSONObject jsonObject = JsonConverter.toJSON( writableMap );

		assertTrue( jsonObject.getBoolean( "bool" ) );
	}

	@Test
	public void toJson_double() throws Exception
	{
		WritableMap writableMap = new JavaOnlyMap();
		writableMap.putDouble( "double", 42d );

		JSONObject jsonObject = JsonConverter.toJSON( writableMap );

		assertEquals( 42d, jsonObject.getInt( "double" ), 1e-8 );
	}

	@Test
	public void toJson_null() throws Exception
	{
		WritableMap writableMap = new JavaOnlyMap();
		writableMap.putNull( "null" );

		JSONObject jsonObject = JsonConverter.toJSON( writableMap );

		assertEquals( JSONObject.NULL, jsonObject.get( "null" ) );
	}

	@Ignore // JavaOnlyMap doesn't support deep copy in toHashMap
	@Test
	public void toJson_array() throws Exception
	{
		WritableMap writableMap = new JavaOnlyMap();
		WritableArray writableArray = new JavaOnlyArray();
		writableArray.pushString( "a" );
		writableArray.pushInt( 1 );
		writableMap.putArray( "array", writableArray );

		JSONObject jsonObject = JsonConverter.toJSON( writableMap );

		JSONArray expected = new JSONArray();
		expected.put( "a" );
		expected.put( 1 );
		assertEquals( expected, jsonObject.getJSONArray( "array" ) );
	}

	@Ignore // JavaOnlyMap doesn't support deep copy in toHashMap
	@Test
	public void toJson_innerObject() throws Exception
	{
		WritableMap writableMap = new JavaOnlyMap();
		WritableMap innerObject = new JavaOnlyMap();
		innerObject.putBoolean( "bool", false );
		innerObject.putInt( "int", 1 );
		writableMap.putMap( "object", innerObject );

		JSONObject jsonObject = JsonConverter.toJSON( writableMap );

		JSONObject expected = new JSONObject();
		expected.put( "int", 1 );
		expected.put( "bool", true );
		assertEquals( 42, jsonObject.getInt( "int" ) );
	}

}
