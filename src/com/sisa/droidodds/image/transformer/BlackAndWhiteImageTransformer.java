package com.sisa.droidodds.image.transformer;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.inject.Singleton;

@Singleton
public class BlackAndWhiteImageTransformer {

	private static final int BLACK_COLOR = Color.BLACK;
	private static final int WHITE_COLOR = Color.WHITE;

	// TODO: should come from configuration
	private static final double BLUE_COLOR_WEIGHT = Double.valueOf(0.1140);
	private static final double GREEN_COLOR_WEIGHT = Double.valueOf(0.5870);
	private static final double RED_COLOR_WEIGHT = Double.valueOf(0.2989);
	private static final int GRAY_THRESHOLD = Integer.valueOf(128);
	private static final int THRESHOLD_CORRECTION = Integer.valueOf(70);

	public Bitmap transformImage(final Bitmap originalImage) {
		final int width = originalImage.getWidth();
		final int height = originalImage.getHeight();
		final Bitmap monoChromeImage = Bitmap.createBitmap(width, height, originalImage.getConfig());
		int R, G, B;
		int pixel;

		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				pixel = originalImage.getPixel(x, y);
				R = Color.red(pixel);
				G = Color.green(pixel);
				B = Color.blue(pixel);
				final int gray = (int) calculateGrayScaleValue(R, G, B);
				if (gray > GRAY_THRESHOLD + THRESHOLD_CORRECTION)
					monoChromeImage.setPixel(x, y, WHITE_COLOR);
				else
					monoChromeImage.setPixel(x, y, BLACK_COLOR);
			}
		}
		return monoChromeImage;

	}

	private double calculateGrayScaleValue(final int R, final int G, final int B) {
		return RED_COLOR_WEIGHT * R + GREEN_COLOR_WEIGHT * G + BLUE_COLOR_WEIGHT * B;
	}
}
