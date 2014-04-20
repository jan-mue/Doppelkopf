package com.eg.cards.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MainMenu extends Table{
	
	private BitmapFont titleFont, font;
	private Label title;
	private TextureAtlas atlas;

	public MainMenu(final CardGame game){
		super();
		
		setFillParent(true);
		if (CardGame.DEBUG) debug();
		
		atlas = game.getAssets().get("ui.atlas", TextureAtlas.class);
        AtlasRegion region = atlas.findRegion("menubutton");
		
        titleFont = game.getAssets().get("fonts/dosis.medium.ttf", BitmapFont.class);
        font = game.getAssets().get("fonts/RobotoCondensed-Regular.ttf", BitmapFont.class);
		
		LabelStyle style = new LabelStyle();
        style.font = titleFont;
        style.fontColor = Color.BLACK;
        title = new Label("DOPPELKOPF", style);
        
        add(title);
        row();

        TextButtonStyle buttonStyle = new TextButtonStyle();
        buttonStyle.up = new TextureRegionDrawable(region);
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        
        TextButton button = new TextButton("Spielen", buttonStyle);
        button.addListener(new ClickListener(){
        	public void clicked (InputEvent event, float x, float y) {
        		game.toggleMainMenu();
        	}
        });
        
        add(button).space(200);
	}

}
