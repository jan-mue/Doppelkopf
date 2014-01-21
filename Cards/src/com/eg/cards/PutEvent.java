package com.eg.cards;

import com.badlogic.gdx.scenes.scene2d.Event;

public class PutEvent extends Event{
	
	private final int id;
	
	public PutEvent(int id){
		this.id = id;
	}
	
	public int getID(){ return id; }

}
