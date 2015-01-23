package com.sisa.droidodds.facade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import roboguice.RoboGuice;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sisa.droidodds.DroidOddsApplication;
import com.sisa.droidodds.calculator.CompositeKnownCardsOddsCalculator;
import com.sisa.droidodds.domain.card.Card;
import com.sisa.droidodds.domain.card.Rank;
import com.sisa.droidodds.domain.card.Suit;
import com.sisa.droidodds.service.OddsCalculatorService;
import com.sisa.droidodds.ui.layout.OddsOverlayView;
import com.sisa.droidodds.util.measurement.StopWatch;

/**
 * Main facade called by activity for calculating the odds of winning with user's cards.
 * 
 * @author Laszlo Sisa
 * 
 */
@Singleton
public class OddsCalculatorFacade {

	@Inject
	private ImageRecognizerFacade imageRecognizerFacade;
	@Inject
	private OddsCalculatorService oddsCalculatorService;
	@Inject
	private StopWatch stopWatch;
	@Inject
	private CompositeKnownCardsOddsCalculator oddsCalculator;

	/**
	 * DI constructor.
	 */
	public OddsCalculatorFacade() {
		RoboGuice.injectMembers(DroidOddsApplication.getAppContext(), this);
	}

	/**
	 * Calculates the odds of winning with the current hand by reading the latest available screenshot.
	 * 
	 * @param currentTetx
	 *            The current text of overlay view's info text view
	 * @return String which will be set as the text of {@link OddsOverlayView} info text view
	 */
	public String getOdds(final String currentTetxt) {
		// stopWatch.start();
		// final List<Card> recognizedCards = imageRecognizerFacade.recognizeLatestScreenshot();
		// oddsCalculatorService.getOdds(recognizedCards);
		// stopWatch.stop();
		//
		// if (recognizedCards.size() != 0) {
		// return buildResultStrgin(recognizedCards, stopWatch.elapsedInMiliSeconds());
		// } else {
		// return (currentTetxt);
		// }
		stopWatch.start();
		oddsCalculator.evaluateRecognizedCardOdds(Arrays.asList(new Card(Rank.EIGHT, Suit.CLUBS), new Card(Rank.NINE, Suit.CLUBS),
				new Card(Rank.SEVEN, Suit.HEARTS), new Card(Rank.JACK, Suit.DIAMONDS), new Card(Rank.TEN, Suit.SPADES), new Card(
						Rank.DEUCE, Suit.SPADES), new Card(Rank.FIVE, Suit.CLUBS)));
		stopWatch.stop();
		return buildResultStrgin(new ArrayList<Card>(), stopWatch.elapsedInMiliSeconds());
	}

	private String buildResultStrgin(final List<Card> recognizedCards, final double milliSeconds) {
		final StringBuilder resultStringBulider = new StringBuilder();
		for (final Card card : recognizedCards) {
			resultStringBulider.append(card.getAbbreviatedName()).append(" ");
		}
		resultStringBulider.append("\n Took: ").append(milliSeconds).append(" ms");
		return resultStringBulider.toString();
	}

}
