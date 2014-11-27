package com.sisa.droidodds.evaluator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;

import com.sisa.droidodds.domain.Odds;
import com.sisa.droidodds.domain.card.Card;
import com.sisa.droidodds.domain.card.CompleteDeck;

public class RecognizedCardEvaluator {

	private final CompleteDealEvaluator completeDealEvaluator;

	private int winCount;
	private int splitCount;
	private int totalDealCount;
	private int variationCount;

	public RecognizedCardEvaluator() {
		completeDealEvaluator = new CompleteDealEvaluator();
	}

	public Odds evaluateRecognizedCardOdds(final List<Card> recognizedCards) {
		Validate.validState(recognizedCards.size() >= 5);
		System.out.println(Runtime.getRuntime().availableProcessors());
		resetCounters();
		calculateOdds(recognizedCards);
		return calculateAvarageOddsCount();
	}

	private void calculateOdds(final List<Card> recognizedCards) {
		final List<Card> cardsInHand = recognizedCards.subList(0, 2);
		final List<Card> communityCards = recognizedCards.subList(2, recognizedCards.size());
		if (communityCards.size() == 5) {
			completeDealEvaluator.evaluateOdds(cardsInHand, communityCards);
			variationCount++;
		} else {
			final List<Card> deck = initDeck(recognizedCards);
			evaluateAllPossibleDeals(cardsInHand, deck, communityCards, 0);
		}
	}

	private List<Card> initDeck(final List<Card> recognizedCards) {
		final List<Card> deck = new ArrayList<>();
		deck.addAll(CompleteDeck.getCompleteDeck());
		deck.removeAll(recognizedCards);
		return deck;
	}

	private void evaluateAllPossibleDeals(final List<Card> cardsInHand, final List<Card> reaminingCardsInDeck,
			final List<Card> communityCards, final int startIndex) {
		for (int currentCardIndex = startIndex; currentCardIndex < reaminingCardsInDeck.size(); currentCardIndex++) {

			final List<Card> currentCommunityCard = new ArrayList<>();
			currentCommunityCard.addAll(communityCards);
			currentCommunityCard.add(reaminingCardsInDeck.get(currentCardIndex));

			if (currentCommunityCard.size() < 5) {
				evaluateAllPossibleDeals(cardsInHand, reaminingCardsInDeck, currentCommunityCard, currentCardIndex + 1);
			} else {
				// System.out.println(String.format("#%s - %s", variationCount, currentCommunityCard.toString()));
				completeDealEvaluator.evaluateOdds(cardsInHand, currentCommunityCard);
				variationCount++;
			}
		}
	}

	private void resetCounters() {
		winCount = 0;
		splitCount = 0;
		totalDealCount = 0;
		variationCount = 0;
	}

	private Odds calculateAvarageOddsCount() {
		return new Odds(winCount / variationCount, splitCount / variationCount, totalDealCount / variationCount);
	}

}
