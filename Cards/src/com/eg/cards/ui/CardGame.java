package com.eg.cards.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class CardGame extends Game{
	
	public final static boolean debug = false;

	private MainMenuScreen mainMenuScreen;
	private GameScreen gameScreen;
	public Stage stage;
	
	private TextureAtlas uiResources;

	@Override
	public void create() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		uiResources = new TextureAtlas(Gdx.files.internal("ui.atlas"));
		
		gameScreen = new GameScreen(this);
		mainMenuScreen = new MainMenuScreen(this);
        setScreen(mainMenuScreen);
	}
	
	public TextureAtlas getUIAtlas(){ return uiResources; }
	
	public void toggleMainMenu(){
		if (getScreen().equals(mainMenuScreen))
			setScreen(gameScreen);
		else setScreen(mainMenuScreen);
	}
	
	@Override
	public void render() {		
		super.render();
		
		stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        
        if (debug) Table.drawDebug(stage);
	}

	@Override
	public void resize(int width, int height) {
		getScreen().resize(width, height);
		stage.setViewport(1080, 1920, true);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
	
	@Override
	public void dispose() {	
		stage.dispose();
		uiResources.dispose();
	}

}
