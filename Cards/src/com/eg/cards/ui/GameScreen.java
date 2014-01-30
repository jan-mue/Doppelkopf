package com.eg.cards.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.eg.cards.GameLoop;
import com.eg.cards.PutEvent;
import com.eg.cards.PutListener;

public class GameScreen implements Screen {
	
	private final CardGame game;
	private GameLoop loop;
	private GUI gui;
	
	public GameScreen(CardGame game) {
		
		this.game = game;
		
		loop = new GameLoop();
        gui = new GUI(game, loop);
		gui.addListener(new PutListener(){
			public void put(PutEvent event, int id){
				loop.playCard(id);
			}
		});
		loop.setGUI(gui);
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(GUI.LIGHT.r, GUI.LIGHT.g, GUI.LIGHT.b, GUI.LIGHT.a);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	}

	@Override
	public void show() {
		game.stage.addActor(gui);
		loop.start();
	}

	@Override
	public void hide() {
		game.stage.clear();
	}
	
	@Override
	public void dispose() {
		gui.dispose();
		loop.dispose();
	}
}
