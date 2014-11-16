package com.sisa.droidodds.configuration;

import roboguice.RoboGuice;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sisa.droidodds.DroidOddsApplication;
import com.sisa.droidodds.image.recognizer.CardOcr;
import com.sisa.droidodds.image.recognizer.DeckRecognizer;
import com.sisa.droidodds.image.recognizer.HandRecognizer;
import com.sisa.droidodds.image.recognizer.ImageMatcher;
import com.sisa.droidodds.image.transformer.BlackAndWhiteImageTransformer;

/**
 * Responsible for pre-loading values from configuration source into objects, required to enhance performance.
 * 
 * @author Laszlo Sisa
 * 
 */
@Singleton
public class ConfigurationPreloader {

	@Inject
	private ImageMatcher imageMatcher;
	@Inject
	private HandRecognizer handRecognizer;
	@Inject
	private DeckRecognizer deckRecognizer;
	@Inject
	private BlackAndWhiteImageTransformer blackAndWhiteImageTransformer;
	@Inject
	private CardOcr cardOcr;

	/**
	 * DI constructor.
	 */
	public ConfigurationPreloader() {
		RoboGuice.injectMembers(DroidOddsApplication.getAppContext(), this);
	}

	/**
	 * Pre-loads OCR classes with values from configuration source.
	 */
	public void preloadOcrConfigurationValues() {
		imageMatcher.preLoadConfigurationSourceValues();
		handRecognizer.preLoadConfigurationSourceValues();
		deckRecognizer.preLoadConfigurationSourceValues();
		blackAndWhiteImageTransformer.preLoadConfigurationSourceValues();
		cardOcr.preLoadConfigurationSourceValues();
	}

}
