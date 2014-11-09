package com.sisa.droidodds.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import android.content.res.AssetManager;
import android.util.Log;

import com.google.inject.Singleton;
import com.sisa.droidodds.DroidOddsApplication;

/**
 * Class for reading property files in assets folder.
 * 
 * @author Laszlo Sisa
 * 
 */
@Singleton
public class ConfigurationPropertyFileReader {

	private static final String FILE_OPEN_ERROR_MESSAGE = "Failed to load asset with filename %s:";

	/**
	 * Reads the given property file into a map.
	 * 
	 * @param propertyFileName
	 *            the name of the file in assets folder
	 * @return the map loaded with configuration entries
	 */
	public Map<String, String> readPropertyFile(final String propertyFileName) {
		Map<String, String> propertyMap = null;
		try {
			final Properties properties = new Properties();
			final AssetManager assetManager = DroidOddsApplication.getAppContext().getAssets();
			final InputStream inputStream = assetManager.open(propertyFileName);
			properties.load(inputStream);
			propertyMap = buildPropertyMap(properties);
		} catch (final IOException e) {
			Log.e(String.format(FILE_OPEN_ERROR_MESSAGE, propertyFileName), e.toString());
		}
		return propertyMap;
	}

	private Map<String, String> buildPropertyMap(final Properties properties) {
		final Map<String, String> propertyMap = new HashMap<>();
		for (final String key : properties.stringPropertyNames()) {
			propertyMap.put(key, properties.getProperty(key));
		}
		return propertyMap;
	}

}
