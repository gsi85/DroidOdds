package com.sisa.droidodds.image.recognizer;

import java.util.List;

import org.apache.commons.lang3.Validate;

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

	private static final int EXPECTED_SIZE_AFTER_HANDS_RECOGNIZED = 2;
	private static final int EXPECTED_SIZE_AFTER_FIRST_CARD_RECOGNIZED = 3;
	private static final int EXPECTED_SIZE_AFTER_TURN_CARD_RECOGNIZED = 6;

	private boolean configurationsLoaded;
	private int cardHeight;
	private int cardWidth;
	private int edgeTrimWidth;
	private int flop1X;
	private int flop1Y;
	private int flop2X;
	private int flop2Y;
	private int flop3X;
	private int flop3Y;
	private int riverX;
	private int riverY;
	private int turnX;
	private int turnY;

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
	 * @throws {@link IllegalStateException} if called before configuration have been loaded
	 */
	public List<Card> recognizeDeck(final Bitmap latestScreenshot, final List<Card> cardsInHand) {
		Validate.validState(configurationsLoaded);
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
		return imageCutter.trimEdges(Bitmap.createBitmap(latestScreenshot, flop1X, flop1Y, cardWidth, cardHeight), edgeTrimWidth);
	}

	private Bitmap cutSecondCardInFlop(final Bitmap latestScreenshot) {
		return imageCutter.trimEdges(Bitmap.createBitmap(latestScreenshot, flop2X, flop2Y, cardWidth, cardHeight), edgeTrimWidth);
	}

	private Bitmap cutThirdCardInFlop(final Bitmap latestScreenshot) {
		return imageCutter.trimEdges(Bitmap.createBitmap(latestScreenshot, flop3X, flop3Y, cardWidth, cardHeight), edgeTrimWidth);
	}

	private Bitmap cutTurn(final Bitmap latestScreenshot) {
		return imageCutter.trimEdges(Bitmap.createBitmap(latestScreenshot, turnX, turnY, cardWidth, cardHeight), edgeTrimWidth);
	}

	private Bitmap cutRiver(final Bitmap latestScreenshot) {
		return imageCutter.trimEdges(Bitmap.createBitmap(latestScreenshot, riverX, riverY, cardWidth, cardHeight), edgeTrimWidth);
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

	/**
	 * Loads values used by this class from {@link ConfigurationSource}, this should be called before calling other methods.
	 */
	public void preLoadConfigurationSourceValues() {
		cardHeight = configurationSource.getInt("OCR_IMAGE_CARD_HEIGHT");
		cardWidth = configurationSource.getInt("OCR_IMAGE_CARD_WIDTH");
		edgeTrimWidth = configurationSource.getInt("OCR_IMAGE_EDGE_TRIM_WIDTH");
		flop1X = configurationSource.getInt("OCR_IMAGE_FLOP_1_X");
		flop1Y = configurationSource.getInt("OCR_IMAGE_FLOP_1_Y");
		flop2X = configurationSource.getInt("OCR_IMAGE_FLOP_2_X");
		flop2Y = configurationSource.getInt("OCR_IMAGE_FLOP_2_Y");
		flop3X = configurationSource.getInt("OCR_IMAGE_FLOP_3_X");
		flop3Y = configurationSource.getInt("OCR_IMAGE_FLOP_3_Y");
		riverX = configurationSource.getInt("OCR_IMAGE_RIVER_X");
		riverY = configurationSource.getInt("OCR_IMAGE_RIVER_Y");
		turnX = configurationSource.getInt("OCR_IMAGE_TURN_X");
		turnY = configurationSource.getInt("OCR_IMAGE_TURN_Y");
		configurationsLoaded = true;
	}
}
