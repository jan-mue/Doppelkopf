package com.eg.cards.ui;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

public class UpdateListener implements EventListener{

	@Override
	public boolean handle(Event e) {
		if (!(e instanceof UpdateEvent)) return false;
		UpdateEvent event = (UpdateEvent) e;
		update(event);
		return true;
	}
	
	public void update(UpdateEvent evenet){		
	}

}
