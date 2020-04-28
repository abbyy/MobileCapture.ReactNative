package com.abbyy.mobile.rtr.reactnative.utils;

import com.facebook.react.bridge.ReadableMap;

import org.json.JSONObject;

import androidx.annotation.Nullable;

public class JsonConverter {

	@Nullable
	public static JSONObject toJSON( @Nullable ReadableMap readableMap )
	{
		if( readableMap == null ) {
			return null;
		}
		return new JSONObject( readableMap.toHashMap() );
	}
}
