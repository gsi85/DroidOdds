package com.sisa.droidodds.domain.hand;

import java.util.List;

import com.sisa.droidodds.domain.card.Rank;

public class EvaluatedHand {

	private final Hand hand;
	private final List<Rank> ascendingSortedDistinctRanks;

	public EvaluatedHand(final Hand hand, final List<Rank> ascendingSortedDistinctRanks) {
		this.hand = hand;
		this.ascendingSortedDistinctRanks = ascendingSortedDistinctRanks;
	}

	public Hand getHand() {
		return hand;
	}

	public List<Rank> getAscendingSortedDistinctRanks() {
		return ascendingSortedDistinctRanks;
	}

}
