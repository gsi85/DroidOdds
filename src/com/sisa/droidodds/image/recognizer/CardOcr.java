package com.sisa.droidodds.image.recognizer;

import java.util.Map;

import roboguice.RoboGuice;
import android.graphics.Bitmap;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sisa.droidodds.DroidOddsApplication;
import com.sisa.droidodds.configuration.ConfigurationReader;
import com.sisa.droidodds.domain.card.Card;
import com.sisa.droidodds.domain.card.Rank;
import com.sisa.droidodds.domain.card.Suit;

/**
 * 
 * TODO: yet another big TODO: change all harcoded values with values from congigruation
 * 
 * 
 */
@Singleton
public class CardOcr {

	private static final String IMAGE_SUIT_WIDTH = "OCR_IMAGE_SUIT_WIDTH";
	private static final String IMAGE_SUIT_HEIGHT = "OCR_IMAGE_SUIT_HEIGHT";
	private static final String IMAGE_RANK_WIDTH = "OCR_IMAGE_RANK_WIDTH";
	private static final String IMAGE_RANK_HEIGHT = "OCR_IMAGE_RANK_HEIGHT";

	// TODO: configuration source
	private static final int ACCEPTED_RANK_MATCH_RATE_TRESHOLD = 95;
	private static final int ACCEPTED_SUIT_MATCH_RATE_TRESHOLD = 99;

	@Inject
	private ConfigurationReader configurationReader;

	@Inject
	private ImageMatcher imageMatcher;

	public CardOcr() {
		RoboGuice.injectMembers(DroidOddsApplication.getAppContext(), this);
	}

	public Card recognizeImage(final Bitmap image) {
		Card recognizedCard = null;
		final Suit suit = imageMatcher.recognizeCard(cutSuitFromImage(image), getSuitSampleImageMap(), ACCEPTED_SUIT_MATCH_RATE_TRESHOLD);
		if (suit != null) {
			final Rank rank = imageMatcher.recognizeCard(cutRankFromImage(image), getRankSampleImageMap(),
					ACCEPTED_RANK_MATCH_RATE_TRESHOLD);
			if (rank != null) {
				recognizedCard = new Card(rank, suit);
			}
		}
		return recognizedCard;
	}

	private Bitmap cutSuitFromImage(final Bitmap image) {
		return Bitmap.createBitmap(image, 0, 38, getInt(IMAGE_SUIT_WIDTH), getInt(IMAGE_SUIT_HEIGHT));
	}

	private Bitmap cutRankFromImage(final Bitmap image) {
		return Bitmap.createBitmap(image, 0, 0, getInt(IMAGE_RANK_WIDTH), getInt(IMAGE_RANK_HEIGHT));
	}

	public int getInt(final String configurationKey) {
		return configurationReader.getInt(configurationKey);
	}

	private Map<Suit, Bitmap> getSuitSampleImageMap() {
		return configurationReader.getSuitSampleImageMap();
	}

	private Map<Rank, Bitmap> getRankSampleImageMap() {
		return configurationReader.getRankSampleImageMap();
	}

}
