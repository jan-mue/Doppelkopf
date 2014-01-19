package com.eg.cards.ui;

import com.badlogic.gdx.scenes.scene2d.Event;

public class UpdateEvent extends Event{
	
	private short[] points;
	
	public UpdateEvent(short[] points){
		this.points = points;
	}
	
	public short[] getPoints(){ return points; }

}
