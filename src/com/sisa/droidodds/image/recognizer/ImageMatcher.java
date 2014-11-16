package com.sisa.droidodds.image.recognizer;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.Validate;

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

	private boolean configurationsLoaded;
	private int acceptedMinMatchRate;
	private int grayscalMissMatchTreshold;
	private double blueColorWeight;
	private double greenColorWeight;
	private double regColorWeight;

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
	 * @throws {@link IllegalArgumentException} if called before configuration have been loaded
	 */
	public <T> T recognizeCard(final Bitmap imageToRecognize, final Map<T, Bitmap> suitSampleImageMap, final int acceptedMatchRate) {
		Validate.isTrue(configurationsLoaded);
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
			} else if (matchRate > bestMatchRate && matchRate > acceptedMinMatchRate) {
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
		return resourcePixelGrayScaleValue - grayscalMissMatchTreshold < imagePixelGrayScaleValue
				&& resourcePixelGrayScaleValue + grayscalMissMatchTreshold > imagePixelGrayScaleValue;
	}

	private double getGrayScaleValue(final int pixel) {
		final int R = Color.red(pixel);
		final int G = Color.green(pixel);
		final int B = Color.blue(pixel);
		return regColorWeight * R + greenColorWeight * G + blueColorWeight * B;
	}

	/**
	 * Loads values used by this class from {@link ConfigurationSource}, this should be called before calling other methods.
	 */
	public void preLoadConfigurationSourceValues() {
		acceptedMinMatchRate = configurationSource.getInt("OCR_ACCEPTED_MIN_MATCH_RATE_TRESHOLD");
		grayscalMissMatchTreshold = configurationSource.getInt("OCR_GRAYSCALE_VALUE_MISSMATCH_TRESHOLD");
		blueColorWeight = Double.valueOf(configurationSource.getDouble("OCR_IMAGE_BLUE_COLOR_WEIGHT"));
		greenColorWeight = Double.valueOf(configurationSource.getDouble("OCR_IMAGE_GREEN_COLOR_WEIGHT"));
		regColorWeight = Double.valueOf(configurationSource.getDouble("OCR_IMAGE_RED_COLOR_WEIGHT"));
		configurationsLoaded = true;
	}
}
