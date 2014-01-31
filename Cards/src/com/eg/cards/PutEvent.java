package com.eg.cards;

import com.badlogic.gdx.scenes.scene2d.Event;

public class PutEvent extends Event{
	
	private final Card card;
	
	public PutEvent(Card card){
		this.card = card;
	}
	
	public Card getCard(){ return card; }

}
