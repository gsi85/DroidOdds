package com.sisa.droidodds.evaluator;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.roboguice.shaded.goole.common.collect.Lists;

import com.sisa.droidodds.domain.card.Card;
import com.sisa.droidodds.domain.card.CompleteDeck;
import com.sisa.droidodds.domain.hand.EvaluatedHand;

public class AllCombinations {

	private final NineCardEvaluator nineCardEvaluator;
	private final SevenCardEvaluator sevenCardEvaluator;
	private long iterCount;

	public AllCombinations() {
		nineCardEvaluator = new NineCardEvaluator();
		sevenCardEvaluator = new SevenCardEvaluator();
	}

	public long evaluateRecognizedCardOdds(final List<Card> recognizedCards) {

		final List<Card> remainingDeck = new ArrayList<>(CompleteDeck.getCompleteDeck());
		remainingDeck.removeAll(recognizedCards);

		final Stack<Card> cards = new Stack<>();
		for (final Card card : recognizedCards) {
			cards.push(card);
		}

		final int remainingDeckSize = remainingDeck.size();

		for (int index1 = 0; index1 < remainingDeckSize - 3; index1++) {
			cards.push(remainingDeck.get(index1));
			for (int index2 = index1 + 1; index2 < remainingDeckSize - 2; index2++) {
				cards.push(remainingDeck.get(index2));
				final EvaluatedHand playersHand = sevenCardEvaluator.evaluateBestHand(cards);
				for (int index3 = index2 + 1; index3 < remainingDeckSize - 1; index3++) {
					cards.push(remainingDeck.get(index3));
					for (int index4 = index3 + 1; index4 < remainingDeckSize; index4++) {
						cards.push(remainingDeck.get(index4));
						final EvaluatedHand opponentHand = sevenCardEvaluator.evaluateBestHand(Lists.newArrayList(cards.subList(2, 9)));
						iterCount++;
						cards.pop();
					}
					cards.pop();
				}
				cards.pop();
			}
			cards.pop();
		}

		return iterCount;
	}

}
