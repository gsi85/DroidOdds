package com.sisa.droidodds.evaluator;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.sisa.droidodds.domain.card.Card;
import com.sisa.droidodds.domain.hand.EvaluatedHand;
import com.sisa.droidodds.evaluator.cache.SevenCardEvaluatorCacheLoader;

public class SevenCardEvaluator {

	private final LoadingCache<List<Card>, EvaluatedHand> loadingCache;

	private long iterCount;

	public SevenCardEvaluator() {

		final CacheLoader<List<Card>, EvaluatedHand> cacheLoader = new SevenCardEvaluatorCacheLoader();
		loadingCache = CacheBuilder.newBuilder().maximumSize(500000).build(cacheLoader);
	}

	public EvaluatedHand evaluateBestHand(final List<Card> cards) {
		Collections.sort(cards);
		iterCount++;
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
