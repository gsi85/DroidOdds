package com.sisa.droidodds.ocr.transformer;

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
	 *            {@link Bitmap} the image to be edited
	 * @param edgeTrimWidth
	 *            line width in pixels
	 * @return the modified {@link Bitmap}
	 */
	public Bitmap trimEdges(final Bitmap image, final int edgeTrimWidth) {
		final Canvas canvas = new Canvas(image);
		final int width = image.getWidth();
		final int height = image.getHeight();
		canvas.drawRect(0, 0, edgeTrimWidth, height, paint);
		canvas.drawRect(width - edgeTrimWidth, 0, width, height, paint);
		canvas.drawRect(0, 0, width, edgeTrimWidth, paint);
		return image;
	}

	/**
	 * Sets each corner of a given image to white in a diagonal area.
	 * 
	 * @param image
	 *            {@link Bitmap} the image to be edited
	 * @param cornerTrimWidth
	 *            radius to trim in pixels
	 * @return the modified {@link Bitmap}
	 */
	public Bitmap trimCorners(final Bitmap image, final int cornerTrimWidth) {
		// TODO: consider whether drawing rectangle would perform better
		final int width = image.getWidth();
		final int height = image.getHeight();
		for (int y = cornerTrimWidth; y >= 1; y--) {
			for (int x = 1; x <= y; x++) {
				image.setPixel(x - 1, cornerTrimWidth - y, WHITE);
				image.setPixel(width - x, cornerTrimWidth - y, WHITE);
				image.setPixel(x - 1, height - (cornerTrimWidth + 1) + y, WHITE);
				image.setPixel(width - (cornerTrimWidth + 1) + y, height - x, WHITE);
			}
		}
		return image;
	}

}
