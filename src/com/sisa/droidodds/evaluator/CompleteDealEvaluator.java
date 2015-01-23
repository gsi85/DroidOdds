package com.sisa.droidodds.evaluator;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.sisa.droidodds.domain.Odds;
import com.sisa.droidodds.domain.card.Card;
import com.sisa.droidodds.domain.card.CompleteDeck;
import com.sisa.droidodds.domain.hand.EvaluatedHand;

public class CompleteDealEvaluator {

	private final SevenCardEvaluator sevenCardEvaluator;

	private final List<Card> deck;
	private int winCount;
	private int splitCount;
	private int totalDealCount;

	private long iterCount;

	public CompleteDealEvaluator() {
		sevenCardEvaluator = new SevenCardEvaluator();
		deck = new LinkedList<>();
	}

	public Odds evaluateOdds(final List<Card> currentDeal) throws ExecutionException {
		resetCounters();

		final EvaluatedHand playersEvaluatedHand = sevenCardEvaluator.evaluateBestHand(currentDeal);
		deck.clear();
		deck.addAll(CompleteDeck.getCompleteDeck());
		deck.removeAll(currentDeal);

		for (int cardsInIndex1 = 0; cardsInIndex1 < deck.size() - 1; cardsInIndex1++) {
			for (int cardsInIndex2 = cardsInIndex1 + 1; cardsInIndex2 < deck.size(); cardsInIndex2++) {
				final List<Card> currentCards = concatenateHandWithCommuninityCards(
						Arrays.asList(deck.get(cardsInIndex1), deck.get(cardsInIndex2)), currentDeal.subList(2, 7));
				matchCurrentCardsToPlayersHand(playersEvaluatedHand, currentCards);
				iterCount++;
			}
		}
		final Odds odds = new Odds(winCount, splitCount, totalDealCount);

		return odds;
	}

	private void matchCurrentCardsToPlayersHand(final EvaluatedHand playersEvaluatedHand, final List<Card> currentCards)
			throws ExecutionException {
		final EvaluatedHand currentHand = sevenCardEvaluator.evaluateBestHand(currentCards);
		final int compare = playersEvaluatedHand.compareTo(currentHand);
		if (compare > 0) {
			winCount++;
		} else if (compare == 0) {
			splitCount++;
		}
		totalDealCount++;
	}

	private List<Card> concatenateHandWithCommuninityCards(final List<Card> cardsInHand, final List<Card> communitiyCards) {
		return Lists.newArrayList(Iterables.concat(cardsInHand, communitiyCards));
	}

	private void resetCounters() {
		winCount = 0;
		splitCount = 0;
		totalDealCount = 0;
	}

}
