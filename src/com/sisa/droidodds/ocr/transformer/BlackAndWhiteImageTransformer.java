package com.sisa.droidodds.ocr.transformer;

import org.apache.commons.lang3.Validate;

import roboguice.RoboGuice;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sisa.droidodds.DroidOddsApplication;
import com.sisa.droidodds.configuration.ConfigurationSource;

/**
 * Transformer for creating black and white images.
 * 
 * @author Laszlo Sisa
 * 
 */
@Singleton
public class BlackAndWhiteImageTransformer {

	private static final int BLACK_COLOR = Color.BLACK;
	private static final int WHITE_COLOR = Color.WHITE;

	private boolean configurationsLoaded;
	private int grayThreshold;
	private int thresholdCorrection;
	private double blueColorWeight;
	private double greenColorWeight;
	private double redColorWeight;

	@Inject
	private ConfigurationSource configurationSource;

	/**
	 * DI constructor.
	 */
	public BlackAndWhiteImageTransformer() {
		RoboGuice.injectMembers(DroidOddsApplication.getAppContext(), this);
	}

	/**
	 * Transforms any {@link Bitmap} to a black and white (monochrome) {@link Bitmap}.
	 * 
	 * @param originalImage
	 *            the {@link Bitmap} to be transformed
	 * @return the monochrome {@link Bitmap}
	 * @throws {@link IllegalStateException} if called before configuration have been loaded
	 */
	public Bitmap transformImage(final Bitmap originalImage) {
		Validate.validState(configurationsLoaded);
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
				if (gray > grayThreshold + thresholdCorrection)
					monoChromeImage.setPixel(x, y, WHITE_COLOR);
				else
					monoChromeImage.setPixel(x, y, BLACK_COLOR);
			}
		}
		return monoChromeImage;

	}

	private double calculateGrayScaleValue(final int R, final int G, final int B) {
		return redColorWeight * R + greenColorWeight * G + blueColorWeight * B;
	}

	/**
	 * Loads values used by this class from {@link ConfigurationSource}, this should be called before calling other methods.
	 */
	public void preLoadConfigurationSourceValues() {
		blueColorWeight = Double.valueOf(configurationSource.getDouble("OCR_IMAGE_BLUE_COLOR_WEIGHT"));
		greenColorWeight = Double.valueOf(configurationSource.getDouble("OCR_IMAGE_GREEN_COLOR_WEIGHT"));
		redColorWeight = Double.valueOf(configurationSource.getDouble("OCR_IMAGE_RED_COLOR_WEIGHT"));
		grayThreshold = configurationSource.getInt("OCR_BW_TRANSFORMER_GRAY_THRESHOLD");
		thresholdCorrection = configurationSource.getInt("OCR_BW_TRANFSOFRME_THRESHOLD_CORRECTION");
		configurationsLoaded = true;
	}
}
