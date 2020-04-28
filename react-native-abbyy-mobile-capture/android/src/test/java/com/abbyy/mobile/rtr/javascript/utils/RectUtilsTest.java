// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

package com.abbyy.mobile.rtr.javascript.utils;

import android.graphics.Point;
import android.graphics.Rect;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RectUtilsTest {

	@Test
	public void test()
	{
		Point[] points = new Point[4];
		points[0] = new Point( 0, 1 );
		points[1] = new Point( 2, 3 );
		points[2] = new Point( 4, 5 );
		points[3] = new Point( 6, 7 );

		Rect rect = RectUtils.fromPoints( points );

		assertEquals( new Rect( 0, 1, 6, 7 ), rect );
	}

}
