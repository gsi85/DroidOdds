package com.sisa.droidodds.image;

import java.util.List;

import com.sisa.droidodds.domain.card.Card;

public interface ImageRecognizer {

	List<Card> recognizeLatestScreenshot();

}
