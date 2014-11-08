package com.sisa.droidodds.configuration;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sisa.droidodds.DroidOddsApplication;
import com.sisa.droidodds.domain.card.Rank;
import com.sisa.droidodds.domain.card.Suit;

@Singleton
public class ConfigurationSource {

	private static final String IMAGE_SUIT_WIDTH = "OCR_IMAGE_SUIT_WIDTH";
	private static final String IMAGE_SUIT_HEIGHT = "OCR_IMAGE_SUIT_HEIGHT";
	private static final String IMAGE_RANK_WIDTH = "OCR_IMAGE_RANK_WIDTH";
	private static final String IMAGE_RANK_HEIGHT = "OCR_IMAGE_RANK_HEIGHT";

	private static final String DEFAULT_CONFIGURATION_PROPERTIES = "default_configuration.properties";
	private static final String DRAWABLE_FOLDER = "drawable";
	private static final String DEFAULT_PACKAGE = "com.sisa.droidodds";
	private static final String PROPERTIES_FILE_EXTENSION = ".properties";
	private static final String FILE_TAG_SEPARATOR = "_";

	@Inject
	private ConfigurationPropertyFileReader configurationPropertyFileReader;
	private Map<Suit, Bitmap> suitSampleImageMap;
	private Map<Rank, Bitmap> rankSampleImageMap;
	private Map<String, String> configurationMap;

	public int getInt(final String configurationKey) {
		return Integer.parseInt(configurationMap.get(configurationKey));
	}

	public Map<Suit, Bitmap> getSuitSampleImageMap() {
		return suitSampleImageMap;
	}

	public Map<Rank, Bitmap> getRankSampleImageMap() {
		return rankSampleImageMap;
	}

	public void loadConfiguration(final GameMode gameMode, final int displayWidth, final int displayHeight) {
		final String propertyName = buildFileName(gameMode, displayWidth, displayHeight);
		final String propertyFileName = String.format("%s%s", propertyName, PROPERTIES_FILE_EXTENSION);
		configurationMap = configurationPropertyFileReader.readPropertyFile(DEFAULT_CONFIGURATION_PROPERTIES);
		configurationMap.putAll(configurationPropertyFileReader.readPropertyFile(propertyFileName));
		setSuitSampleImageMap(propertyName);
		setRankSampleImageMap(propertyName);

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
			final int resourceId = getResourceId(resourceString);
			final Bitmap sample = createBitmapFromResource(resourceId, IMAGE_SUIT_WIDTH, IMAGE_SUIT_HEIGHT);
			suitSampleImageMap.put(suit, sample);
		}
		this.suitSampleImageMap = suitSampleImageMap;
	}

	private void setRankSampleImageMap(final String propertyName) {
		final Map<Rank, Bitmap> suitSampleImageMap = new HashMap<>();
		for (final Rank rank : Rank.values()) {
			final String resourceString = String.format("%s_%s", propertyName, Integer.toString(rank.getValue()));
			final int resourceId = getResourceId(resourceString);
			final Bitmap sample = createBitmapFromResource(resourceId, IMAGE_RANK_WIDTH, IMAGE_RANK_HEIGHT);
			suitSampleImageMap.put(rank, sample);
		}
		this.rankSampleImageMap = suitSampleImageMap;
	}

	private Bitmap createBitmapFromResource(final int resourceId, final String width, final String height) {
		return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(DroidOddsApplication.getAppContext().getResources(), resourceId),
				getInt(width), getInt(height), false);
	}

	private int getResourceId(final String resourceString) {
		return DroidOddsApplication.getAppContext().getResources().getIdentifier(resourceString, DRAWABLE_FOLDER, DEFAULT_PACKAGE);
	}

}
