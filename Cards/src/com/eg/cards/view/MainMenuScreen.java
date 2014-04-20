package com.eg.cards.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public class MainMenuScreen implements Screen{
	
	private final CardGame game;
	private MainMenu menu;
	
	public MainMenuScreen(CardGame game){
		this.game=game;
        
        menu = new MainMenu(game);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1f, 1f, 1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		game.stage.addActor(menu);		
	}

	@Override
	public void hide() {
		game.stage.clear();
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		
	}

}
