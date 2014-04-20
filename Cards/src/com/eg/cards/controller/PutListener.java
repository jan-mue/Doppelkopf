package com.eg.cards.controller;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.eg.cards.model.Card;

public class PutListener implements EventListener{

	@Override
	public boolean handle(Event e) {
		if (!(e instanceof PutEvent)) return false;
		PutEvent event = (PutEvent) e;
		
		put(event.getCard());
		
		return true;
	}
	
	public void put(Card card){
	}

}
