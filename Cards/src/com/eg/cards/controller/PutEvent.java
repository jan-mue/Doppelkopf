package com.eg.cards.controller;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.eg.cards.model.Card;

public class PutEvent extends Event{
	
	private final Card card;
	
	public PutEvent(Card card){
		this.card = card;
	}
	
	public Card getCard(){ return card; }

}
