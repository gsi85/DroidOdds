package com.sisa.droidodds.image.transformer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.google.inject.Singleton;

/**
 * Provides image trim related functionalities.
 * 
 * @author Laszlo Sisa
 * 
 */
@Singleton
public class ImageCutter {

	private static final int WHITE = Color.WHITE;

	private final Paint paint;

	/**
	 * DI constructor.
	 */
	public ImageCutter() {
		paint = new Paint();
		paint.setColor(WHITE);
		paint.setStyle(Paint.Style.FILL);
	}

	/**
	 * Sets the border line of a given image to white.
	 * 
	 * @param image
	 *            {@link Bitmap} the image to edited
	 * @param pixelsToTrim
	 *            line width in pixels
	 * @return the modified {@link Bitmap}
	 */
	public Bitmap trimEdges(final Bitmap image, final int pixelsToTrim) {
		final Canvas canvas = new Canvas(image);

		final int width = image.getWidth();
		final int height = image.getHeight();

		canvas.drawRect(0, 0, pixelsToTrim, height, paint);
		canvas.drawRect(width - pixelsToTrim, 0, width, height, paint);
		canvas.drawRect(0, 0, width, pixelsToTrim, paint);
		return image;
	}

}
