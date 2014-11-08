package com.sisa.droidodds.calculator;

import java.util.List;

import com.google.inject.Singleton;
import com.sisa.droidodds.domain.card.Card;

@Singleton
public class OddsCalculatorService {

	public String getOdds(final List<Card> cardsInHand) {
		return "ja";
	}

}
