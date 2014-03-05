package com.eg.cards.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
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
		if (CardGame.debug) debug();
		
		atlas = game.getAssets().get("ui.atlas", TextureAtlas.class);
        AtlasRegion region = atlas.findRegion("menubutton");
		
		FreeTypeFontGenerator generator =
        		new FreeTypeFontGenerator(Gdx.files.internal("fonts/dosis.medium.ttf"));
		FreeTypeFontParameter titleParam = new FreeTypeFontParameter();
		titleParam.size = 150;
        titleFont = generator.generateFont(titleParam);
        generator.dispose();
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/RobotoCondensed-Regular.ttf"));
        FreeTypeFontParameter param = new FreeTypeFontParameter();
        param.size = 80;
        font = generator.generateFont(param);
        generator.dispose();
		
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
