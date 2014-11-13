package com.sisa.droidodds.image.recognizer;

import java.util.List;

import roboguice.RoboGuice;
import android.graphics.Bitmap;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sisa.droidodds.DroidOddsApplication;
import com.sisa.droidodds.configuration.ConfigurationSource;
import com.sisa.droidodds.domain.card.Card;
import com.sisa.droidodds.image.transformer.BlackAndWhiteImageTransformer;
import com.sisa.droidodds.image.transformer.ImageCutter;

/**
 * Recognizer for the cards on the deck.
 * 
 * @author Laszlo Sisa
 * 
 */
@Singleton
public class DeckRecognizer {

	private static final String CARD_HEIGHT = "OCR_IMAGE_CARD_HEIGHT";
	private static final String CARD_WIDTH = "OCR_IMAGE_CARD_WIDTH";
	private static final String EDGE_TRIM_WIDTH = "OCR_IMAGE_EDGE_TRIM_WIDTH";
	private static final String FLOP_1_X = "OCR_IMAGE_FLOP_1_X";
	private static final String FLOP_1_Y = "OCR_IMAGE_FLOP_1_Y";
	private static final String FLOP_2_X = "OCR_IMAGE_FLOP_2_X";
	private static final String FLOP_2_Y = "OCR_IMAGE_FLOP_2_Y";
	private static final String FLOP_3_X = "OCR_IMAGE_FLOP_3_X";
	private static final String FLOP_3_Y = "OCR_IMAGE_FLOP_3_Y";
	private static final String RIVER_X = "OCR_IMAGE_RIVER_X";
	private static final String RIVER_Y = "OCR_IMAGE_RIVER_Y";
	private static final String TURN_X = "OCR_IMAGE_TURN_X";
	private static final String TURN_Y = "OCR_IMAGE_TURN_Y";

	private static final int EXPECTED_SIZE_AFTER_HANDS_RECOGNIZED = 2;
	private static final int EXPECTED_SIZE_AFTER_FIRST_CARD_RECOGNIZED = 3;
	private static final int EXPECTED_SIZE_AFTER_TURN_CARD_RECOGNIZED = 6;

	@Inject
	private BlackAndWhiteImageTransformer blackAndWhiteImageTransformer;
	@Inject
	private CardOcr cardOcr;
	@Inject
	private ImageCutter imageCutter;
	@Inject
	private ConfigurationSource configurationSource;

	/**
	 * DI constructor.
	 */
	public DeckRecognizer() {
		RoboGuice.injectMembers(DroidOddsApplication.getAppContext(), this);
	}

	/**
	 * Recognizes all cards on the deck.
	 * 
	 * @param latestScreenshot
	 *            the {@link Bitmap} to be recognized
	 * @param cardsInHand
	 *            the list previously recognized {@link Card} in hand
	 * @return the lit of recognized {@link Card}
	 */
	public List<Card> recognizeDeck(final Bitmap latestScreenshot, final List<Card> cardsInHand) {
		recognizeFirstCardInFlop(latestScreenshot, cardsInHand);
		return cardsInHand;
	}

	private void recognizeFirstCardInFlop(final Bitmap latestScreenshot, final List<Card> cardsInHand) {
		if (isHandRecognized(cardsInHand)) {
			final Bitmap firstCard = blackAndWhiteImageTransformer.transformImage(cutFirstCardInFlop(latestScreenshot));
			final Card recognizedCard = cardOcr.recognizeImage(firstCard);
			if (recognizedCard != null) {
				cardsInHand.add(recognizedCard);
				recognizeRestOfFlop(latestScreenshot, cardsInHand);
			}
		}
	}

	private void recognizeRestOfFlop(final Bitmap latestScreenshot, final List<Card> cardsInHand) {
		if (isFlopOnDeck(cardsInHand)) {
			final Bitmap secondCard = blackAndWhiteImageTransformer.transformImage(cutSecondCardInFlop(latestScreenshot));
			final Bitmap thirdCard = blackAndWhiteImageTransformer.transformImage(cutThirdCardInFlop(latestScreenshot));
			final Card recognizedSecondCard = cardOcr.recognizeImage(secondCard);
			if (recognizedSecondCard != null) {
				cardsInHand.add(recognizedSecondCard);
				final Card recognizedThirdCard = cardOcr.recognizeImage(thirdCard);
				if (recognizedThirdCard != null) {
					cardsInHand.add(recognizedThirdCard);
					recognizeTurn(latestScreenshot, cardsInHand);
				}
			}
		}
	}

	private void recognizeTurn(final Bitmap latestScreenshot, final List<Card> cardsInHand) {
		final Bitmap turn = blackAndWhiteImageTransformer.transformImage(cutTurn(latestScreenshot));
		final Card recognizedTurn = cardOcr.recognizeImage(turn);
		if (recognizedTurn != null) {
			cardsInHand.add(recognizedTurn);
			recognizeRiver(latestScreenshot, cardsInHand);
		}
	}

	private void recognizeRiver(final Bitmap latestScreenshot, final List<Card> cardsInHand) {
		if (isTurnRecognized(cardsInHand)) {
			final Bitmap river = blackAndWhiteImageTransformer.transformImage(cutRiver(latestScreenshot));
			final Card recognizedTurn = cardOcr.recognizeImage(river);
			if (recognizedTurn != null)
				cardsInHand.add(recognizedTurn);
		}
	}

	private Bitmap cutFirstCardInFlop(final Bitmap latestScreenshot) {
		return imageCutter.trimEdges(
				Bitmap.createBitmap(latestScreenshot, getInt(FLOP_1_X), getInt(FLOP_1_Y), getInt(CARD_WIDTH), getInt(CARD_HEIGHT)),
				getInt(EDGE_TRIM_WIDTH));
	}

	private Bitmap cutSecondCardInFlop(final Bitmap latestScreenshot) {
		return imageCutter.trimEdges(
				Bitmap.createBitmap(latestScreenshot, getInt(FLOP_2_X), getInt(FLOP_2_Y), getInt(CARD_WIDTH), getInt(CARD_HEIGHT)),
				getInt(EDGE_TRIM_WIDTH));
	}

	private Bitmap cutThirdCardInFlop(final Bitmap latestScreenshot) {
		return imageCutter.trimEdges(
				Bitmap.createBitmap(latestScreenshot, getInt(FLOP_3_X), getInt(FLOP_3_Y), getInt(CARD_WIDTH), getInt(CARD_HEIGHT)),
				getInt(EDGE_TRIM_WIDTH));
	}

	private Bitmap cutTurn(final Bitmap latestScreenshot) {
		return imageCutter.trimEdges(
				Bitmap.createBitmap(latestScreenshot, getInt(TURN_X), getInt(TURN_Y), getInt(CARD_WIDTH), getInt(CARD_HEIGHT)),
				getInt(EDGE_TRIM_WIDTH));
	}

	private Bitmap cutRiver(final Bitmap latestScreenshot) {
		return imageCutter.trimEdges(
				Bitmap.createBitmap(latestScreenshot, getInt(RIVER_X), getInt(RIVER_Y), getInt(CARD_WIDTH), getInt(CARD_HEIGHT)),
				getInt(EDGE_TRIM_WIDTH));
	}

	private boolean isHandRecognized(final List<Card> cardsInHand) {
		return cardsInHand.size() == EXPECTED_SIZE_AFTER_HANDS_RECOGNIZED;
	}

	private boolean isFlopOnDeck(final List<Card> cardsInHand) {
		return cardsInHand.size() == EXPECTED_SIZE_AFTER_FIRST_CARD_RECOGNIZED;
	}

	private boolean isTurnRecognized(final List<Card> cardsInHand) {
		return cardsInHand.size() == EXPECTED_SIZE_AFTER_TURN_CARD_RECOGNIZED;
	}

	private int getInt(final String configurationKey) {
		return configurationSource.getInt(configurationKey);
	}
}
