package com.sisa.droidodds.image.recognizer;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import roboguice.RoboGuice;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sisa.droidodds.DroidOddsApplication;
import com.sisa.droidodds.configuration.ConfigurationSource;
import com.sisa.droidodds.domain.card.Card;

/**
 * Image matcher to recognize the {@link Card} on a given {@link Bitmap}.
 * 
 * @author Laszlo Sisa
 * 
 */
@Singleton
public class ImageMatcher {

	private static final String MIN_MATCH_RATE_TRESHOLD = "OCR_ACCEPTED_MIN_MATCH_RATE_TRESHOLD";
	private static final String GRAYSCALE_VALUE_MISSMATCH_TRESHOLD = "OCR_GRAYSCALE_VALUE_MISSMATCH_TRESHOLD";
	private static final String BLUE_COLOR_WEIGHT = "OCR_IMAGE_BLUE_COLOR_WEIGHT";
	private static final String GREEN_COLOR_WEIGHT = "OCR_IMAGE_GREEN_COLOR_WEIGHT";
	private static final String RED_COLOR_WEIGHT = "OCR_IMAGE_RED_COLOR_WEIGHT";

	@Inject
	private ConfigurationSource configurationSource;

	/**
	 * DI constructor.
	 */
	public ImageMatcher() {
		RoboGuice.injectMembers(DroidOddsApplication.getAppContext(), this);
	}

	/**
	 * Matches the image with the given samples.
	 * 
	 * @param imageToRecognize
	 *            {@link Bitmap} the image to be recognized
	 * @param suitSampleImageMap
	 *            the map containing the T type as key and the sample image as value
	 * @param acceptedMatchRate
	 *            the percent as integer describing the accepted match rate, if above no further samples will be compared
	 * @return the recognized T type if found accepted match or best matching types match rate is above minimum accepted match rate
	 *         described in {@link ConfigurationSource}, otherwise returns null
	 */
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
			} else if (matchRate > bestMatchRate && matchRate > getInt(MIN_MATCH_RATE_TRESHOLD)) {
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
		final int grayscaleMissmatchTreshold = getInt(GRAYSCALE_VALUE_MISSMATCH_TRESHOLD);
		final double imagePixelGrayScaleValue = getGrayScaleValue(imagePixel);
		final double resourcePixelGrayScaleValue = getGrayScaleValue(resourcePixel);
		return resourcePixelGrayScaleValue - grayscaleMissmatchTreshold < imagePixelGrayScaleValue
				&& resourcePixelGrayScaleValue + grayscaleMissmatchTreshold > imagePixelGrayScaleValue;
	}

	private double getGrayScaleValue(final int pixel) {
		final int R = Color.red(pixel);
		final int G = Color.green(pixel);
		final int B = Color.blue(pixel);
		return getDouble(RED_COLOR_WEIGHT) * R + getDouble(GREEN_COLOR_WEIGHT) * G + getDouble(BLUE_COLOR_WEIGHT) * B;
	}

	private int getInt(final String configurationKey) {
		return configurationSource.getInt(configurationKey);
	}

	private double getDouble(final String configurationKey) {
		return configurationSource.getDouble(configurationKey);
	}

}
