package com.sisa.droidodds.image.recognizer;

import java.util.Map;

import roboguice.RoboGuice;
import android.graphics.Bitmap;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sisa.droidodds.DroidOddsApplication;
import com.sisa.droidodds.configuration.ConfigurationSource;
import com.sisa.droidodds.domain.card.Card;
import com.sisa.droidodds.domain.card.Rank;
import com.sisa.droidodds.domain.card.Suit;

/**
 * Card recognizer class.
 * 
 * @author Laszlo Sisa
 * 
 */
@Singleton
public class CardOcr {

	private static final String SUIT_WIDTH = "OCR_IMAGE_SUIT_WIDTH";
	private static final String SUIT_WIDTH_OFFSET = "OCR_IMAGE_SUIT_WIDTH_OFFSET";
	private static final String SUIT_HEIGHT = "OCR_IMAGE_SUIT_HEIGHT";
	private static final String SUIT_HEIGHT_OFFSET = "OCR_IMAGE_SUIT_HEIGHT_OFFSET";
	private static final String RANK_WIDTH = "OCR_IMAGE_RANK_WIDTH";
	private static final String RANK_HEIGHT = "OCR_IMAGE_RANK_HEIGHT";
	private static final String RANK_WIDTH_OFFSET = "OCR_IMAGE_RANK_WIDTH_OFFSET";
	private static final String RANK_HEIGHT_OFFSET = "OCR_IMAGE_RANK_HEIGHT_OFFSET";
	private static final String ACCEPTED_RANK_MATCH_RATE_TRESHOLD = "OCR_ACCEPTED_RANK_MATCH_RATE_TRESHOLD";
	private static final String ACCEPTED_SUIT_MATCH_RATE_TRESHOLD = "OCR_ACCEPTED_SUIT_MATCH_RATE_TRESHOLD";

	@Inject
	private ConfigurationSource configurationSource;

	@Inject
	private ImageMatcher imageMatcher;

	/**
	 * DI constructor.
	 */
	public CardOcr() {
		RoboGuice.injectMembers(DroidOddsApplication.getAppContext(), this);
	}

	/**
	 * Recognizes a given image as {@link Card}.
	 * 
	 * @param image
	 *            the image to recognize
	 * @return the {@link Card} or null if the image is not recognized
	 */
	public Card recognizeImage(final Bitmap image) {
		Card recognizedCard = null;
		final int suitMatchRateThreshold = getInt(ACCEPTED_SUIT_MATCH_RATE_TRESHOLD);
		final int rankMatchRateThreshold = getInt(ACCEPTED_RANK_MATCH_RATE_TRESHOLD);
		final Suit suit = imageMatcher.recognizeCard(cutSuitFromImage(image), getSuitSampleImageMap(), suitMatchRateThreshold);
		if (suit != null) {
			final Rank rank = imageMatcher.recognizeCard(cutRankFromImage(image), getRankSampleImageMap(), rankMatchRateThreshold);
			if (rank != null) {
				recognizedCard = new Card(rank, suit);
			}
		}
		return recognizedCard;
	}

	private Bitmap cutSuitFromImage(final Bitmap image) {
		return Bitmap.createBitmap(image, getInt(SUIT_WIDTH_OFFSET), getInt(SUIT_HEIGHT_OFFSET), getInt(SUIT_WIDTH), getInt(SUIT_HEIGHT));
	}

	private Bitmap cutRankFromImage(final Bitmap image) {
		return Bitmap.createBitmap(image, getInt(RANK_WIDTH_OFFSET), getInt(RANK_HEIGHT_OFFSET), getInt(RANK_WIDTH), getInt(RANK_HEIGHT));
	}

	public int getInt(final String configurationKey) {
		return configurationSource.getInt(configurationKey);
	}

	private Map<Suit, Bitmap> getSuitSampleImageMap() {
		return configurationSource.getSuitSampleImageMap();
	}

	private Map<Rank, Bitmap> getRankSampleImageMap() {
		return configurationSource.getRankSampleImageMap();
	}

}
