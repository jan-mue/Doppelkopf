package com.eg.cards;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.eg.cards.ui.CardGame;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Doko";
		cfg.width = 360;
		cfg.height = 640;
		
		new LwjglApplication(new CardGame(), cfg);
	}
}
