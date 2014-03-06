package com.eg.cards;

import com.badlogic.gdx.scenes.scene2d.Event;

public class PutEvent extends Event{
	
	private final ImageCard card;
	
	public PutEvent(ImageCard card){
		this.card = card;
	}
	
	public ImageCard getCard(){ return card; }

}
