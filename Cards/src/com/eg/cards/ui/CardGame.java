package com.eg.cards.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.eg.cards.ui.FreeTypeFontLoader.FontParameter;

public class CardGame extends Game{
	
	public final static boolean debug = false;

	private LoadScreen loadScreen;
	private MainMenuScreen mainMenuScreen;
	private GameScreen gameScreen;
	public Stage stage;
	
	private AssetManager manager;
	private boolean loaded;

	@Override
	public void create() {
		InternalFileHandleResolver resolver = new InternalFileHandleResolver();
		manager = new AssetManager(resolver);
		manager.setLoader(BitmapFont.class, new FreeTypeFontLoader(resolver));
		manager.load("fonts/dosis.medium.ttf", BitmapFont.class, new FontParameter(150));
		manager.load("fonts/Roboto-Regular.ttf", BitmapFont.class, new FontParameter(60));
		manager.finishLoading();
		manager.load("fonts/RobotoCondensed-Regular.ttf", BitmapFont.class, new FontParameter(80));
		manager.load("fonts/Roboto-Medium.ttf", BitmapFont.class, new FontParameter(60));
		
		manager.load("ui.atlas", TextureAtlas.class);
		manager.load("cards.atlas", TextureAtlas.class);
		loaded = false;
		
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		loadScreen = new LoadScreen(this);
		setScreen(loadScreen);
	}
	
	public AssetManager getAssets(){ return manager; }
	
	public void toggleMainMenu(){
		if (getScreen().equals(mainMenuScreen))
			setScreen(gameScreen);
		else setScreen(mainMenuScreen);
	}
	
	@Override
	public void render() {		
		super.render();
		
		if(manager.update() && !loaded) {
			gameScreen = new GameScreen(this);
			mainMenuScreen = new MainMenuScreen(this);
			setScreen(mainMenuScreen);
			loaded = true;
		}
		
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
		manager.dispose();
	}

}
