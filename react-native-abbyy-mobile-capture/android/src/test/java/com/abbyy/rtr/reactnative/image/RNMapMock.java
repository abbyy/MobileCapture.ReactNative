// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

package com.abbyy.rtr.reactnative.image;

import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.NoSuchKeyException;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.WritableMap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

@SuppressWarnings( "ConstantConditions" ) // Null is allowed in HashMap
public class RNMapMock implements WritableMap {

	private Map<Object, Object> map = new HashMap<>();

	@Override
	public void putNull( @NonNull String key )
	{
		map.put( key, null );
	}

	@Override
	public void putBoolean( @NonNull String key, boolean value )
	{
		map.put( key, value );
	}

	@Override
	public void putDouble( @NonNull String key, double value )
	{
		map.put( key, value );
	}

	@Override
	public void putInt( @NonNull String key, int value )
	{
		map.put( key, value );
	}

	@Override
	public void putString( @NonNull String key, @Nullable String value )
	{
		map.put( key, value );
	}

	@Override
	public void putArray( @NonNull String key, @Nullable ReadableArray value )
	{
		map.put( key, value );
	}

	@Override
	public void putMap( @NonNull String key, @Nullable ReadableMap value )
	{
		map.put( key, value );
	}

	@Override
	public void merge( @NonNull ReadableMap source )
	{
		throw new RuntimeException( "Not implemented" );
	}

	@Override
	public WritableMap copy()
	{
		throw new RuntimeException( "Not implemented" );
	}

	@Override
	public boolean hasKey( @NonNull String name )
	{
		return map.containsKey( name );
	}

	@Override
	public boolean isNull( @NonNull String name )
	{
		return getWithKeyCheck( name ) == null;
	}

	@Override
	public boolean getBoolean( @NonNull String name )
	{
		return (boolean) getWithKeyCheck( name );
	}

	@Override
	public double getDouble( @NonNull String name )
	{
		return (double) getWithKeyCheck( name );
	}

	@Override
	public int getInt( @NonNull String name )
	{
		return (int) getWithKeyCheck( name );
	}

	@Nullable
	@Override
	public String getString( @NonNull String name )
	{
		return (String) getWithKeyCheck( name );
	}

	@Nullable
	@Override
	public ReadableArray getArray( @NonNull String name )
	{
		return (ReadableArray) getWithKeyCheck( name );
	}

	@Nullable
	@Override
	public ReadableMap getMap( @NonNull String name )
	{
		return (ReadableMap) getWithKeyCheck( name );
	}

	private Object getWithKeyCheck( @NonNull String name )
	{
		if( hasKey( name ) ) {
			return map.get( name );
		} else {
			throw new NoSuchKeyException( "Map doesn't contain key: " + name );
		}
	}

	@NonNull
	@Override
	public Dynamic getDynamic( @NonNull String name )
	{
		throw new RuntimeException( "Not implemented" );
	}

	@NonNull
	@Override
	public ReadableType getType( @NonNull String name )
	{
		throw new RuntimeException( "Not implemented" );
	}

	@NonNull
	@Override
	public Iterator<Map.Entry<String, Object>> getEntryIterator()
	{
		throw new RuntimeException( "Not implemented" );
	}

	@NonNull
	@Override
	public ReadableMapKeySetIterator keySetIterator()
	{
		throw new RuntimeException( "Not implemented" );
	}

	@NonNull
	@Override
	public HashMap<String, Object> toHashMap()
	{
		throw new RuntimeException( "Not implemented" );
	}
}
