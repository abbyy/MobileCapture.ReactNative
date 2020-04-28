package com.abbyy.mobile.rtr.javascript;

import org.json.JSONObject;

public interface JSCallback {
	void onSuccess( JSONObject result );
	void onError( String errorCode, String message, Throwable exception );
}
