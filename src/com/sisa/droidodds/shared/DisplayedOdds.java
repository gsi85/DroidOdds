package com.sisa.droidodds.shared;

import java.util.Collections;
import java.util.List;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import com.google.inject.Singleton;
import com.sisa.droidodds.domain.Odds;
import com.sisa.droidodds.domain.card.Card;

@ThreadSafe
@Singleton
public class DisplayedOdds {

	@GuardedBy("this")
	private Odds odds;
	@GuardedBy("this")
	private List<Card> recognizedCards;
	@GuardedBy("this")
	private int processedOuterCards;

	public synchronized Odds getOdds() {
		return odds;
	}

	public synchronized void setOdds(final Odds odds) {
		this.odds = odds;
	}

	public synchronized List<Card> getRecognizedCards() {
		return recognizedCards != null ? Collections.unmodifiableList(recognizedCards) : null;
	}

	public synchronized void setRecognizedCards(final List<Card> recognizedCards) {
		this.recognizedCards = recognizedCards;
	}

	public synchronized int getProcessedOuterCards() {
		return processedOuterCards;
	}

	public synchronized void setProcessedOuterCards(final int processedOuterCards) {
		this.processedOuterCards = processedOuterCards;
	}

}
