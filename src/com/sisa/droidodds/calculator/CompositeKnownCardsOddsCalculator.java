package com.sisa.droidodds.calculator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;

import roboguice.RoboGuice;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sisa.droidodds.DroidOddsApplication;
import com.sisa.droidodds.domain.Odds;
import com.sisa.droidodds.domain.card.Card;

@Singleton
public class CompositeKnownCardsOddsCalculator implements KnownCardsOddsCalculator {

	private static final int MINIMUM_NUMBER_OF_RECGONIZED_CARDS = 5;

	@Inject
	private FiveKnownCardsOddsCalculator fiveKnownCardsOddsCalculator;
	@Inject
	private SixKnownCardsOddsCalculator sixKnownCardsOddsCalculator;
	@Inject
	private SevenKnownCardsOddsCalculator sevenKnownCardsOddsCalculator;

	private final List<AbstractKnownCardsOddsCalculator> oddsCalculators;

	public CompositeKnownCardsOddsCalculator() {
		RoboGuice.injectMembers(DroidOddsApplication.getAppContext(), this);
		oddsCalculators = new ArrayList<>();
		oddsCalculators.add(fiveKnownCardsOddsCalculator);
		oddsCalculators.add(sixKnownCardsOddsCalculator);
		oddsCalculators.add(sevenKnownCardsOddsCalculator);
	}

	@Override
	public Odds evaluateRecognizedCardOdds(final List<Card> recognizedCards) {
		Validate.validState(recognizedCards.size() >= MINIMUM_NUMBER_OF_RECGONIZED_CARDS);
		Odds odds = null;
		for (final AbstractKnownCardsOddsCalculator oddsCalculator : oddsCalculators) {
			if (oddsCalculator.expectedNumberOfKnownCards() == recognizedCards.size()) {
				odds = oddsCalculator.evaluateRecognizedCardOdds(recognizedCards);
			}
		}
		return odds;
	}
}
