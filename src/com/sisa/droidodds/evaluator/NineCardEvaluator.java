package com.sisa.droidodds.evaluator;

import java.util.List;

import com.google.common.collect.Lists;
import com.sisa.droidodds.domain.Odds;
import com.sisa.droidodds.domain.card.Card;
import com.sisa.droidodds.domain.hand.EvaluatedHand;

public class NineCardEvaluator {

	private final SevenCardEvaluator sevenCardEvaluator;

	public NineCardEvaluator() {
		sevenCardEvaluator = new SevenCardEvaluator();
	}

	public Odds evaluateOdds(final List<Card> cards) {
		final EvaluatedHand playersHand = sevenCardEvaluator.evaluateBestHand(Lists.newArrayList(cards.subList(0, 7)));
		final EvaluatedHand opponentHand = sevenCardEvaluator.evaluateBestHand(Lists.newArrayList(cards.subList(2, 9)));

		return null;
	}

}
