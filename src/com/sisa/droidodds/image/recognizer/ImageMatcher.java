package com.sisa.droidodds.image.recognizer;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.graphics.Bitmap;
import android.graphics.Color;

public class ImageMatcher {

	// TODO to configuration source
	private static final int MIN_MATCH_RATE_TRESHOLD = 60;
	private static final int GRAYSCALE_VALUE_MISSMATCH_TRESHOLD = 30;
	private static final double BLUE_COLOR_WEIGHT = Double.valueOf(0.1140);
	private static final double GREEN_COLOR_WEIGHT = Double.valueOf(0.5870);
	private static final double RED_COLOR_WEIGHT = Double.valueOf(0.2989);

	public <T> T recognizeCard(final Bitmap imageToRecognize, final Map<T, Bitmap> suitSampleImageMap, final int acceptedMatchRate) {
		boolean match = false;
		int bestMatchRate = 0;
		T bestMatch = null;
		T matched = null;

		final Iterator<Entry<T, Bitmap>> iterator = suitSampleImageMap.entrySet().iterator();
		while (iterator.hasNext() && !match) {
			final Entry<T, Bitmap> currentSample = iterator.next();

			final int matchRate = getMatchRate(imageToRecognize, currentSample.getValue());
			if (matchRate >= acceptedMatchRate) {
				matched = currentSample.getKey();
				match = true;
			} else if (matchRate > bestMatchRate && matchRate > MIN_MATCH_RATE_TRESHOLD) {
				bestMatch = currentSample.getKey();
				bestMatchRate = matchRate;
			}

		}
		return matched != null ? matched : bestMatch;
	}

	private int getMatchRate(final Bitmap image, final Bitmap resource) {
		int matchCount = 0;
		int pixelcount = 0;
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				if (isPixelColorMatch(image.getPixel(x, y), resource.getPixel(x, y))) {
					matchCount++;
				}
				pixelcount++;
			}
		}
		return Math.round((float) matchCount / pixelcount * 100);
	}

	private boolean isPixelColorMatch(final int imagePixel, final int resourcePixel) {
		final double imagePixelGrayScaleValue = getGrayScaleValue(imagePixel);
		final double resourcePixelGrayScaleValue = getGrayScaleValue(resourcePixel);
		return resourcePixelGrayScaleValue - GRAYSCALE_VALUE_MISSMATCH_TRESHOLD < imagePixelGrayScaleValue
				&& resourcePixelGrayScaleValue + GRAYSCALE_VALUE_MISSMATCH_TRESHOLD > imagePixelGrayScaleValue;
	}

	private double getGrayScaleValue(final int pixel) {
		final int R = Color.red(pixel);
		final int G = Color.green(pixel);
		final int B = Color.blue(pixel);
		return RED_COLOR_WEIGHT * R + GREEN_COLOR_WEIGHT * G + BLUE_COLOR_WEIGHT * B;
	}

}
