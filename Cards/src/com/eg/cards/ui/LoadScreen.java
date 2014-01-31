package com.eg.cards.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;

public class LoadScreen implements Screen{
	
	private final CardGame game;
	private final LoadingIndicator indicator;
	
	public LoadScreen(CardGame game){
		this.game=game;
		
		indicator = new LoadingIndicator(game);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1f, 1f, 1f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		indicator.update();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		game.stage.addActor(indicator);
	}

	@Override
	public void hide() {
		game.stage.clear();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}