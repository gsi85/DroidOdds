package com.sisa.droidodds.shared;

import java.util.Collections;
import java.util.List;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import com.google.inject.Singleton;
import com.sisa.droidodds.domain.card.Card;

@ThreadSafe
@Singleton
public class LatestRecognizedCards {

	@GuardedBy("this")
	private List<Card> recognizedCards;
	@GuardedBy("this")
	private boolean cardsUpdated;

	public synchronized List<Card> getRecognizedCards() {
		return recognizedCards != null ? Collections.unmodifiableList(recognizedCards) : null;
	}

	public synchronized void setRecognizedCards(final List<Card> recognizedCards) {
		this.recognizedCards = recognizedCards;
	}

	public synchronized boolean isCardsUpdated() {
		return cardsUpdated;
	}

	public synchronized void setCardsUpdated(final boolean cardsUpdated) {
		this.cardsUpdated = cardsUpdated;
	}

}
