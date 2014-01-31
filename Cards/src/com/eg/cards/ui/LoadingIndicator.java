package com.eg.cards.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class LoadingIndicator extends Table{
	
	private BitmapFont titleFont, font;
	private Label title, progress;
	
	private CardGame game;

	public LoadingIndicator(final CardGame game){
		super();
		
		this.game = game;
		
		setFillParent(true);
		if (CardGame.debug) debug();
		
		FreeTypeFontGenerator generator =
        		new FreeTypeFontGenerator(Gdx.files.internal("fonts/dosis.medium.ttf"));
        titleFont = generator.generateFont(150);
        generator.dispose();
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto-Regular.ttf"));
        font = generator.generateFont(40);
        generator.dispose();
		
		LabelStyle titleStyle = new LabelStyle();
		titleStyle.font = titleFont;
		titleStyle.fontColor = Color.BLACK;
        title = new Label("DOPPELKOPF", titleStyle);
        
        LabelStyle progressStyle = new LabelStyle();
        progressStyle.font = font;
        progressStyle.fontColor = Color.BLACK;
        progress = new Label("", progressStyle);
        
        add(title);
        row();
        add(progress).space(200);
	}
	
	public void update(){
		progress.setText(Math.round(game.getAssets().getProgress()*100)+"% geladen");
	}

}
