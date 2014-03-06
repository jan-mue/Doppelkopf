package com.eg.cards.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
		
		titleFont = game.getAssets().get("fonts/dosis.medium.ttf", BitmapFont.class);
        font = game.getAssets().get("fonts/Roboto-Regular.ttf", BitmapFont.class);
        font.setScale(2/3f);
		
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
