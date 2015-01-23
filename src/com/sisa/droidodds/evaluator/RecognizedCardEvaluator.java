package com.sisa.droidodds.evaluator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;

import com.google.inject.Singleton;
import com.sisa.droidodds.domain.Odds;
import com.sisa.droidodds.domain.card.Card;
import com.sisa.droidodds.domain.card.CompleteDeck;

@Singleton
public class RecognizedCardEvaluator {

	private static final int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();

	private int winCount;
	private int splitCount;
	private int totalDealCount;
	private int variationCount;
	private List<List<Card>> possibleDeals;
	private CompleteDealEvaluator completeDealEvaluator;

	public Odds evaluateRecognizedCardOdds(final List<Card> recognizedCards) {
		Validate.validState(recognizedCards.size() >= 5);
		initState();
		calculateOdds(recognizedCards);
		return calculateAvarageOddsCount();
	}

	private void calculateOdds(final List<Card> recognizedCards) {
		if (recognizedCards.size() == 7) {
			// DO SMTH
		} else {
			final List<Card> deck = initDeck(recognizedCards);
			buildListOfPossibleDeals(recognizedCards, deck, 0);
			evalutePossibleHands();
		}
	}

	private List<Card> initDeck(final List<Card> recognizedCards) {
		final List<Card> deck = new ArrayList<>();
		deck.addAll(CompleteDeck.getCompleteDeck());
		deck.removeAll(recognizedCards);
		return deck;
	}

	private void buildListOfPossibleDeals(final List<Card> cardsInHand, final List<Card> reaminingCardsInDeck, final int startIndex) {
		for (int currentCardIndex = startIndex; currentCardIndex < reaminingCardsInDeck.size(); currentCardIndex++) {
			final List<Card> currentDeal = new ArrayList<>();
			currentDeal.addAll(cardsInHand);
			currentDeal.add(reaminingCardsInDeck.get(currentCardIndex));

			if (currentDeal.size() < 7) {
				buildListOfPossibleDeals(currentDeal, reaminingCardsInDeck, currentCardIndex + 1);
			} else {
				possibleDeals.add(currentDeal);
				variationCount++;
			}
		}
	}

	private void initState() {
		winCount = 0;
		splitCount = 0;
		totalDealCount = 0;
		variationCount = 0;
		possibleDeals = new ArrayList<>();
	}

	private Odds calculateAvarageOddsCount() {
		return new Odds(winCount / variationCount, splitCount / variationCount, totalDealCount / variationCount);
	}

	private void evalutePossibleHands() {
		completeDealEvaluator = new CompleteDealEvaluator();

		try {
			for (final List<Card> currentDeal : possibleDeals) {
				completeDealEvaluator.evaluateOdds(currentDeal);
			}
		} catch (final Exception ex) {
			// SHOULD BE ExecutionException
			ex.printStackTrace();
		}
	}
}
