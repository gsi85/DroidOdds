package com.sisa.droidodds.evaluator;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.sisa.droidodds.domain.card.Card;
import com.sisa.droidodds.domain.hand.EvaluatedHand;
import com.sisa.droidodds.evaluator.cache.FiveCardEvaluatorCacheLoader;

/**
 * Class for evaluating the best hand of given 5 cards.
 * 
 * @author Laszlo Sisa
 * 
 */
public class FiveCardEvaluator {

	private final LoadingCache<List<Card>, EvaluatedHand> loadingCache;

	public FiveCardEvaluator() {
		final CacheLoader<List<Card>, EvaluatedHand> cacheLoader = new FiveCardEvaluatorCacheLoader();
		loadingCache = CacheBuilder.newBuilder().maximumSize(500000).build(cacheLoader);
	}

	public EvaluatedHand evaluateHand(final List<Card> cards) {
		EvaluatedHand evaluatedHand;
		try {
			evaluatedHand = loadingCache.get(cards);
		} catch (final ExecutionException e) {
			evaluatedHand = null;
			e.printStackTrace();
		}
		return evaluatedHand;
	}

}
