package com.abbyy.mobile.rtr.reactnative.utils;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class ReactNativeConverter {

	public static ReadableMap fromJson( JSONObject jsonObject )
	{
		WritableMap map = new WritableNativeMap();

		Iterator<String> iterator = jsonObject.keys();
		try {
			while( iterator.hasNext() ) {
				String key = iterator.next();
				Object value = jsonObject.get( key );
				if( value instanceof JSONObject ) {
					map.putMap( key, fromJson( (JSONObject) value ) );
				} else if( value instanceof JSONArray ) {
					map.putArray( key, fromJson( (JSONArray) value ) );
				} else if( value instanceof Boolean ) {
					map.putBoolean( key, (Boolean) value );
				} else if( value instanceof Integer ) {
					map.putInt( key, (Integer) value );
				} else if( value instanceof Double ) {
					map.putDouble( key, (Double) value );
				} else if( value instanceof String ) {
					map.putString( key, (String) value );
				} else {
					map.putString( key, value.toString() );
				}
			}
		} catch( JSONException e ) {
			e.printStackTrace();
		}
		return map;
	}

	private static WritableArray fromJson( JSONArray jsonArray ) throws JSONException
	{
		WritableArray array = new WritableNativeArray();

		for( int i = 0; i < jsonArray.length(); i++ ) {
			Object value = jsonArray.get( i );
			if( value instanceof JSONObject ) {
				array.pushMap( fromJson( (JSONObject) value ) );
			} else if( value instanceof JSONArray ) {
				array.pushArray( fromJson( (JSONArray) value ) );
			} else if( value instanceof Boolean ) {
				array.pushBoolean( (Boolean) value );
			} else if( value instanceof Integer ) {
				array.pushInt( (Integer) value );
			} else if( value instanceof Double ) {
				array.pushDouble( (Double) value );
			} else if( value instanceof String ) {
				array.pushString( (String) value );
			} else {
				array.pushString( value.toString() );
			}
		}
		return array;
	}
}
