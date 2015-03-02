package com.sisa.droidodds.calculator;

import java.util.List;
import java.util.Stack;

import org.roboguice.shaded.goole.common.collect.Lists;

import com.google.inject.Singleton;
import com.sisa.droidodds.domain.card.Card;
import com.sisa.droidodds.domain.hand.EvaluatedHand;

@Singleton
public class FiveKnownCardsOddsCalculator extends AbstractKnownCardsOddsCalculator {

	public static final int EXPECTED_NUMBER_OF_KNOWN_CARDS = 5;

	private static final int OPPONENT_HAND_START_INDEX = 2;
	private static final int OPPONENT_HAND_END_INDEX = 9;

	@Override
	protected void itarateOverAllPossibleCombination(final List<Card> remainingDeck, final Stack<Card> cards) {
		final int remainingDeckSize = remainingDeck.size();
		for (int index1 = 0; index1 < remainingDeckSize - 3 && !isNewCardsAvailable(); index1++) {
			cards.push(remainingDeck.get(index1));
			for (int index2 = index1 + 1; index2 < remainingDeckSize - 2 && !isNewCardsAvailable(); index2++) {
				cards.push(remainingDeck.get(index2));
				final EvaluatedHand playersHand = evaluateHand(cards);
				for (int index3 = index2 + 1; index3 < remainingDeckSize - 1 && !isNewCardsAvailable(); index3++) {
					cards.push(remainingDeck.get(index3));
					for (int index4 = index3 + 1; index4 < remainingDeckSize; index4++) {
						cards.push(remainingDeck.get(index4));
						final EvaluatedHand opponentHand = evaluateHand(Lists.newArrayList(cards.subList(OPPONENT_HAND_START_INDEX,
								OPPONENT_HAND_END_INDEX)));
						compareCurrentHand(playersHand, opponentHand);
						cards.pop();
					}
					cards.pop();
					publishProgress(index1);
				}
				cards.pop();
			}
			cards.pop();
		}
	}

	@Override
	public int expectedNumberOfKnownCards() {
		return EXPECTED_NUMBER_OF_KNOWN_CARDS;
	}

}
