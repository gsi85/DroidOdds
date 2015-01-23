package com.sisa.droidodds.calculator.evaluator;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.sisa.droidodds.calculator.evaluator.cache.SevenCardEvaluatorCacheLoader;
import com.sisa.droidodds.domain.card.Card;
import com.sisa.droidodds.domain.hand.EvaluatedHand;

public class SevenCardEvaluator {

	private final LoadingCache<List<Card>, EvaluatedHand> loadingCache;
	private final SevenCardEvaluatorCacheLoader cacheLoader;

	public SevenCardEvaluator() {
		cacheLoader = new SevenCardEvaluatorCacheLoader();
		loadingCache = CacheBuilder.newBuilder().maximumSize(500000).build(cacheLoader);
	}

	public EvaluatedHand evaluateBestHand(final List<Card> cards) {
		Collections.sort(cards);
		EvaluatedHand evaluatedHand;
		try {
			evaluatedHand = loadingCache.get(cards);
		} catch (final ExecutionException e) {
			evaluatedHand = cacheLoader.evaluateNewHand(cards);
			e.printStackTrace();
		}
		return evaluatedHand;
	}
}
