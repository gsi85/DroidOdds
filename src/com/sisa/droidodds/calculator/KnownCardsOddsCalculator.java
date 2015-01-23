package com.sisa.droidodds.calculator;

import java.util.List;

import com.sisa.droidodds.domain.Odds;
import com.sisa.droidodds.domain.card.Card;

public interface KnownCardsOddsCalculator {

	Odds evaluateRecognizedCardOdds(final List<Card> recognizedCards);

}
