package com.sisa.droidodds.calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.sisa.droidodds.calculator.evaluator.SevenCardEvaluator;
import com.sisa.droidodds.domain.Odds;
import com.sisa.droidodds.domain.card.Card;
import com.sisa.droidodds.domain.card.CompleteDeck;
import com.sisa.droidodds.domain.hand.EvaluatedHand;

public abstract class AbstractKnownCardsOddsCalculator implements KnownCardsOddsCalculator {

	private final SevenCardEvaluator sevenCardEvaluator;

	private int winCount;
	private int splitCount;
	private int totalDealCount;

	public AbstractKnownCardsOddsCalculator() {
		sevenCardEvaluator = new SevenCardEvaluator();
	}

	@Override
	public Odds evaluateRecognizedCardOdds(final List<Card> recognizedCards) {
		resetCounter();
		final List<Card> remainingDeck = getRemainingDeck(recognizedCards);
		final Stack<Card> cards = initializeEvaluatedCardsStack(recognizedCards);
		itarateOverAllPossibleCombination(remainingDeck, cards);
		return getFinalOdds();
	}

	abstract public int expectedNumberOfKnownCards();

	abstract protected void itarateOverAllPossibleCombination(final List<Card> remainingDeck, final Stack<Card> cards);

	protected EvaluatedHand evaluateHand(final List<Card> arrayList) {
		return sevenCardEvaluator.evaluateBestHand(arrayList);
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

	private Odds getFinalOdds() {
		return new Odds(winCount, splitCount, totalDealCount);
	}

}
