package com.sisa.droidodds.calculator.evaluator.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.cache.CacheLoader;
import com.sisa.droidodds.calculator.evaluator.FiveCardEvaluator;
import com.sisa.droidodds.domain.card.Card;
import com.sisa.droidodds.domain.card.Rank;
import com.sisa.droidodds.domain.hand.EvaluatedHand;
import com.sisa.droidodds.domain.hand.Hand;

public class SevenCardEvaluatorCacheLoader extends CacheLoader<List<Card>, EvaluatedHand> {

	private static final int MAX_CARDS_OUT_INDEX = 6;
	private static final EvaluatedHand LOWEST_POSSIBLE_HAND = new EvaluatedHand(Hand.HIGH_CARD, Arrays.asList(Rank.SEVEN, Rank.FIVE,
			Rank.FOUR, Rank.THREE, Rank.DEUCE));

	private final FiveCardEvaluator fiveCardEvaluator;
	private final List<Card> listOfFive;

	public SevenCardEvaluatorCacheLoader() {
		fiveCardEvaluator = new FiveCardEvaluator();
		listOfFive = new ArrayList<>();
	}

	@Override
	public EvaluatedHand load(final List<Card> cards) throws Exception {
		return evaluateNewHand(cards);
	}

	public EvaluatedHand evaluateNewHand(final List<Card> cards) {
		EvaluatedHand bestHand = LOWEST_POSSIBLE_HAND;
		for (int cardOutIndex1 = 0; cardOutIndex1 < MAX_CARDS_OUT_INDEX; cardOutIndex1++) {
			for (int cardOutIndex2 = cardOutIndex1; cardOutIndex2 < MAX_CARDS_OUT_INDEX; cardOutIndex2++) {
				final EvaluatedHand currentHand = fiveCardEvaluator.evaluateHand(removeOutCards(cards, cardOutIndex1, cardOutIndex2));
				bestHand = evaluateCurrentHand(bestHand, currentHand);
			}
		}
		return bestHand;
	}

	private List<Card> removeOutCards(final List<Card> cards, final int cardOutIndex1, final int cardOutIndex2) {
		listOfFive.clear();
		listOfFive.addAll(cards);
		listOfFive.remove(cardOutIndex1);
		listOfFive.remove(cardOutIndex2);
		return listOfFive;
	}

	private EvaluatedHand evaluateCurrentHand(EvaluatedHand bestHand, final EvaluatedHand currentHand) {
		if (currentHand.compareTo(bestHand) > 0)
			bestHand = currentHand;
		return bestHand;
	}

}
