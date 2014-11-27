package com.sisa.droidodds.evaluator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.Validate;

import com.sisa.droidodds.domain.Odds;
import com.sisa.droidodds.domain.card.Card;
import com.sisa.droidodds.domain.card.CompleteDeck;
import com.sisa.droidodds.domain.hand.EvaluatedHand;

public class CompleteDealEvaluator {

	private final SevenCardEvaluator sevenCardEvaluator;

	private int winCount;
	private int splitCount;
	private int totalDealCount;

	public CompleteDealEvaluator() {
		sevenCardEvaluator = new SevenCardEvaluator();
	}

	public Odds evaluateOdds(final List<Card> cardsInHand, final List<Card> communitiyCards) {
		Validate.validState(cardsInHand.size() == 2);

		if (communitiyCards.size() != 5)
			Validate.validState(communitiyCards.size() == 5);

		resetCounters();

		final List<Card> playersCard = concatenateHandWithCommuninityCards(cardsInHand, communitiyCards);
		final EvaluatedHand playersEvaluatedHand = sevenCardEvaluator.evaluateBestHand(playersCard);
		final List<Card> deck = new ArrayList<>();
		deck.addAll(CompleteDeck.getCompleteDeck());
		deck.removeAll(playersCard);

		for (int cardsInIndex1 = 0; cardsInIndex1 < deck.size() - 1; cardsInIndex1++) {
			for (int cardsInIndex2 = cardsInIndex1 + 1; cardsInIndex2 < deck.size(); cardsInIndex2++) {
				final List<Card> currentCards = concatenateHandWithCommuninityCards(
						Arrays.asList(deck.get(cardsInIndex1), deck.get(cardsInIndex2)), communitiyCards);
				matchCurrentCardsToPlayersHand(playersEvaluatedHand, currentCards);
			}
		}

		return new Odds(winCount, splitCount, totalDealCount);
	}

	private void matchCurrentCardsToPlayersHand(final EvaluatedHand playersEvaluatedHand, final List<Card> currentCards) {
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
		final List<Card> cards = new ArrayList<>();
		cards.addAll(cardsInHand);
		cards.addAll(communitiyCards);
		return cards;
	}

	private void resetCounters() {
		winCount = 0;
		splitCount = 0;
		totalDealCount = 0;
	}

}
