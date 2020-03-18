// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

package com.abbyy.rtr.reactnative.image;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Size;

import java.io.File;

public class Page implements Parcelable {

	private File file;
	private Size frameSize;

	public Page() {}

	private Page( Parcel source )
	{
		file = (File) source.readSerializable();
		frameSize = new Size( source.readInt(), source.readInt() );
	}

	public void setFile( File file )
	{
		this.file = file;
	}

	public File getFile()
	{
		return file;
	}

	public Size getFrameSize()
	{
		return frameSize;
	}

	public void setFrameSize( Size frameSize )
	{
		this.frameSize = frameSize;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel( Parcel dest, int flags )
	{
		dest.writeSerializable( file );
		dest.writeInt( frameSize.getWidth() );
		dest.writeInt( frameSize.getHeight() );
	}

	public static final Creator<Page> CREATOR = new Creator<Page>() {
		@Override
		public Page createFromParcel( Parcel source )
		{
			return new Page( source );
		}

		@Override
		public Page[] newArray( int size )
		{
			return new Page[size];
		}
	};

}
