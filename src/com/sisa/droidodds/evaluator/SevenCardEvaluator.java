package com.sisa.droidodds.evaluator;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.Validate;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.sisa.droidodds.domain.card.Card;
import com.sisa.droidodds.domain.card.Rank;
import com.sisa.droidodds.domain.hand.EvaluatedHand;
import com.sisa.droidodds.domain.hand.Hand;

public class SevenCardEvaluator {

	private static final EvaluatedHand LOWEST_POSSIBLE_HAND = new EvaluatedHand(Hand.HIGH_CARD, Arrays.asList(Rank.SEVEN, Rank.FIVE,
			Rank.FOUR, Rank.THREE, Rank.DEUCE));

	private final FiveCardEvaluator fiveCardEvaluator;
	private final LoadingCache<List<Card>, EvaluatedHand> loadingCache;

	public SevenCardEvaluator() {
		fiveCardEvaluator = new FiveCardEvaluator();
		final CacheLoader<List<Card>, EvaluatedHand> cacheLoader = new CacheLoader<List<Card>, EvaluatedHand>() {

			@Override
			public EvaluatedHand load(final List<Card> cards) throws Exception {
				return evaluateNewHand(cards);
			}

		};
		loadingCache = CacheBuilder.newBuilder().maximumSize(179446).build(cacheLoader);
	}

	public EvaluatedHand evaluateBestHand(final List<Card> cards) throws ExecutionException {
		return loadingCache.get(cards);
	}

	private EvaluatedHand evaluateNewHand(final List<Card> cards) {
		Validate.validState(cards.size() == 7);

		EvaluatedHand bestHand = LOWEST_POSSIBLE_HAND;
		for (int cardOutIndex1 = 0; cardOutIndex1 < 6; cardOutIndex1++) {
			for (int cardOutIndex2 = cardOutIndex1; cardOutIndex2 < 6; cardOutIndex2++) {
				final EvaluatedHand currentHand = fiveCardEvaluator.evaluateHand(removeOutCards(cards, cardOutIndex1, cardOutIndex2));
				if (currentHand.compareTo(bestHand) > 0)
					bestHand = currentHand;
			}
		}

		return bestHand;
	}

	private List<Card> removeOutCards(final List<Card> cards, final int cardOutIndex1, final int cardOutIndex2) {
		final List<Card> listOfFive = new LinkedList<>();
		listOfFive.addAll(cards);
		listOfFive.remove(cardOutIndex1);
		listOfFive.remove(cardOutIndex2);
		return listOfFive;
	}
}
