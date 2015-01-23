package com.sisa.droidodds.ocr.recognizer;

import java.util.Map;

import org.apache.commons.lang3.Validate;

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

	private boolean configurationsLoaded;
	Map<Suit, Bitmap> suitSampleImageMap;
	Map<Rank, Bitmap> rankSampleImageMap;
	private int suitWidth;
	private int suitWidthOffset;
	private int suitHeight;
	private int suitHeightOffset;
	private int rankWidth;
	private int rankHeigth;
	private int rankWidthOffset;
	private int rankHeightOffset;
	private int acceptedRankMatchRateThreshold;
	private int acceptedSuitMatchRateThreshold;

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
	 * Recognizes all {@link Card} on a given {@link Bitmap}.
	 * 
	 * @param image
	 *            the image to recognize
	 * @return the {@link Card} or null if the image is not recognized
	 * @throws {@link IllegalStateException} if called before configuration have been loaded
	 */
	public Card recognizeImage(final Bitmap image) {
		Validate.validState(configurationsLoaded);
		Card recognizedCard = null;
		final int suitMatchRateThreshold = acceptedSuitMatchRateThreshold;
		final int rankMatchRateThreshold = acceptedRankMatchRateThreshold;
		final Suit suit = imageMatcher.recognizeCard(cutSuitFromImage(image), suitSampleImageMap, suitMatchRateThreshold);
		if (suit != null) {
			final Rank rank = imageMatcher.recognizeCard(cutRankFromImage(image), rankSampleImageMap, rankMatchRateThreshold);
			if (rank != null) {
				recognizedCard = new Card(rank, suit);
			}
		}
		return recognizedCard;
	}

	private Bitmap cutSuitFromImage(final Bitmap image) {
		return Bitmap.createBitmap(image, suitWidthOffset, suitHeightOffset, suitWidth, suitHeight);
	}

	private Bitmap cutRankFromImage(final Bitmap image) {
		return Bitmap.createBitmap(image, rankWidthOffset, rankHeightOffset, rankWidth, rankHeigth);
	}

	/**
	 * Loads values used by this class from {@link ConfigurationSource}, this should be called before calling other methods.
	 */
	public void preLoadConfigurationSourceValues() {
		suitWidth = configurationSource.getInt("OCR_IMAGE_SUIT_WIDTH");
		suitWidthOffset = configurationSource.getInt("OCR_IMAGE_SUIT_WIDTH_OFFSET");
		suitHeight = configurationSource.getInt("OCR_IMAGE_SUIT_HEIGHT");
		suitHeightOffset = configurationSource.getInt("OCR_IMAGE_SUIT_HEIGHT_OFFSET");
		rankWidth = configurationSource.getInt("OCR_IMAGE_RANK_WIDTH");
		rankHeigth = configurationSource.getInt("OCR_IMAGE_RANK_HEIGHT");
		rankWidthOffset = configurationSource.getInt("OCR_IMAGE_RANK_WIDTH_OFFSET");
		rankHeightOffset = configurationSource.getInt("OCR_IMAGE_RANK_HEIGHT_OFFSET");
		acceptedRankMatchRateThreshold = configurationSource.getInt("OCR_ACCEPTED_RANK_MATCH_RATE_TRESHOLD");
		acceptedSuitMatchRateThreshold = configurationSource.getInt("OCR_ACCEPTED_SUIT_MATCH_RATE_TRESHOLD");
		suitSampleImageMap = configurationSource.getSuitSampleImageMap();
		rankSampleImageMap = configurationSource.getRankSampleImageMap();
		configurationsLoaded = true;
	}
}
