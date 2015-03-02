package com.sisa.droidodds.calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import roboguice.RoboGuice;

import com.google.inject.Inject;
import com.sisa.droidodds.DroidOddsApplication;
import com.sisa.droidodds.calculator.evaluator.SevenCardEvaluator;
import com.sisa.droidodds.domain.Odds;
import com.sisa.droidodds.domain.card.Card;
import com.sisa.droidodds.domain.card.CompleteDeck;
import com.sisa.droidodds.domain.hand.EvaluatedHand;
import com.sisa.droidodds.shared.DisplayedOdds;
import com.sisa.droidodds.shared.LatestRecognizedCards;

public abstract class AbstractKnownCardsOddsCalculator implements KnownCardsOddsCalculator {

	private final SevenCardEvaluator sevenCardEvaluator;
	@Inject
	private DisplayedOdds displayedOdds;
	@Inject
	private LatestRecognizedCards latestRecognizedCards;
	private int winCount;
	private int splitCount;
	private int totalDealCount;

	public AbstractKnownCardsOddsCalculator() {
		RoboGuice.injectMembers(DroidOddsApplication.getAppContext(), this);
		sevenCardEvaluator = new SevenCardEvaluator();
	}

	@Override
	public Odds evaluateRecognizedCardOdds(final List<Card> recognizedCards) {
		resetCounter();
		final List<Card> remainingDeck = getRemainingDeck(recognizedCards);
		final Stack<Card> cards = initializeEvaluatedCardsStack(recognizedCards);
		itarateOverAllPossibleCombination(remainingDeck, cards);
		return buildOdds();
	}

	abstract public int expectedNumberOfKnownCards();

	abstract protected void itarateOverAllPossibleCombination(final List<Card> remainingDeck, final Stack<Card> cards);

	protected EvaluatedHand evaluateHand(final List<Card> arrayList) {
		return sevenCardEvaluator.evaluateBestHand(arrayList);
	}

	protected void publishProgress(final int outerCardCount) {
		displayedOdds.setOdds(buildOdds());
		displayedOdds.setProcessedOuterCards(outerCardCount);
	}

	protected boolean isNewCardsAvailable() {
		return latestRecognizedCards.isCardsUpdated();
	}

	protected void compareCurrentHand(final EvaluatedHand playersHand, final EvaluatedHand opponentHand) {
		final int compare = playersHand.compareTo(opponentHand);
		if (compare > 0) {
			winCount++;
		} else if (compare == 0) {
			splitCount++;
		}
		totalDealCount++;
	}

	private void resetCounter() {
		winCount = 0;
		splitCount = 0;
		totalDealCount = 0;
	}

	private List<Card> getRemainingDeck(final List<Card> recognizedCards) {
		final List<Card> remainingDeck = new ArrayList<>(CompleteDeck.getCompleteDeck());
		remainingDeck.removeAll(recognizedCards);
		return remainingDeck;
	}

	private Stack<Card> initializeEvaluatedCardsStack(final List<Card> recognizedCards) {
		final Stack<Card> cards = new Stack<>();
		for (final Card card : recognizedCards) {
			cards.push(card);
		}
		return cards;
	}

	private Odds buildOdds() {
		return new Odds(winCount, splitCount, totalDealCount);
	}

}
