package com.eg.cards;

import com.badlogic.gdx.scenes.scene2d.Event;

public class PutEvent extends Event{
	
	private final byte id;
	
	public PutEvent(byte id){
		this.id = id;
	}
	
	public byte getID(){ return id; }

}
