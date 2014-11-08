package com.sisa.droidodds.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.inject.Singleton;
import com.sisa.droidodds.DroidOddsApplication;
import com.sisa.droidodds.domain.card.Rank;
import com.sisa.droidodds.domain.card.Suit;

@Singleton
public class ConfigurationReader {

	private static final String IMAGE_SUIT_WIDTH = "OCR_IMAGE_SUIT_WIDTH";
	private static final String IMAGE_SUIT_HEIGHT = "OCR_IMAGE_SUIT_HEIGHT";
	private static final String IMAGE_RANK_WIDTH = "OCR_IMAGE_RANK_WIDTH";
	private static final String IMAGE_RANK_HEIGHT = "OCR_IMAGE_RANK_HEIGHT";

	private static final String DRAWABLE_FOLDER = "drawable";
	private static final String DEFAULT_PACKAGE = "com.sisa.droidodds";
	private static final String PROPERTIES_FILE_EXTENSION = ".properties";
	private static final String FILE_OPEN_ERROR_MESSAGE = "Failed to load asset with filename %s:";
	private static final String FILE_TAG_SEPARATOR = "_";

	private final Properties properties;
	private Map<Suit, Bitmap> suitSampleImageMap;
	private Map<Rank, Bitmap> rankSampleImageMap;

	public ConfigurationReader() {
		properties = new Properties();
	}

	public int getInt(final String configurationKey) {
		return Integer.parseInt(properties.getProperty(configurationKey));
	}

	public Map<Suit, Bitmap> getSuitSampleImageMap() {
		return suitSampleImageMap;
	}

	public Map<Rank, Bitmap> getRankSampleImageMap() {
		return rankSampleImageMap;
	}

	public void loadConfiguration(final GameMode gameMode, final int displayWidth, final int displayHeight) {
		final String propertyName = buildFileName(gameMode, displayWidth, displayHeight);
		try {
			final AssetManager assetManager = DroidOddsApplication.getAppContext().getAssets();
			final InputStream inputStream = assetManager.open(String.format("%s%s", propertyName, PROPERTIES_FILE_EXTENSION));
			properties.load(inputStream);
			setSuitSampleImageMap(propertyName);
			setRankSampleImageMap(propertyName);
		} catch (final IOException e) {
			Log.e(String.format(FILE_OPEN_ERROR_MESSAGE, propertyName), e.toString());
		}
	}

	private String buildFileName(final GameMode gameMode, final int displayWidth, final int displayHeight) {
		final StringBuilder propertyFileNameBuilder = new StringBuilder().append(gameMode.getName());
		propertyFileNameBuilder.append(FILE_TAG_SEPARATOR).append(displayWidth).append(FILE_TAG_SEPARATOR).append(displayHeight);
		return propertyFileNameBuilder.toString();
	}

	private void setSuitSampleImageMap(final String propertyName) {
		final Map<Suit, Bitmap> suitSampleImageMap = new HashMap<>();
		for (final Suit suit : Suit.values()) {
			final String resourceString = String.format("%s_%s", propertyName, suit.getName());
			final int resourceId = DroidOddsApplication.getAppContext().getResources()
					.getIdentifier(resourceString, DRAWABLE_FOLDER, DEFAULT_PACKAGE);
			final Bitmap sample = Bitmap.createScaledBitmap(
					BitmapFactory.decodeResource(DroidOddsApplication.getAppContext().getResources(), resourceId),
					getInt(IMAGE_SUIT_WIDTH), getInt(IMAGE_SUIT_HEIGHT), false);
			suitSampleImageMap.put(suit, sample);
		}
		this.suitSampleImageMap = suitSampleImageMap;
	}

	private void setRankSampleImageMap(final String propertyName) {
		final Map<Rank, Bitmap> suitSampleImageMap = new HashMap<>();
		for (final Rank rank : Rank.values()) {
			final String resourceString = String.format("%s_%s", propertyName, Integer.toString(rank.getValue()));
			final int resourceId = DroidOddsApplication.getAppContext().getResources()
					.getIdentifier(resourceString, DRAWABLE_FOLDER, DEFAULT_PACKAGE);
			final Bitmap sample = Bitmap.createScaledBitmap(
					BitmapFactory.decodeResource(DroidOddsApplication.getAppContext().getResources(), resourceId),
					getInt(IMAGE_RANK_WIDTH), getInt(IMAGE_RANK_HEIGHT), false);
			suitSampleImageMap.put(rank, sample);
		}
		this.rankSampleImageMap = suitSampleImageMap;
	}
}
