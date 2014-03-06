package com.eg.cards.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.eg.cards.Card;
import com.eg.cards.GameLoop;
import com.eg.cards.PutListener;

public class GUI extends Table {
	
	public static final Color LIGHT = new Color(237/255f, 237/255f, 237/255f, 1);
	public static final Color DARK = new Color(0.2f, 0.2f, 0.2f, 0.6f);
	public static final Color RED = new Color(212/255f, 0, 0, 1);
	
	private final Table handTable, stackTable;
	private final TextureAtlas atlas;
	private final Dialog error;
	private final LabelStyle msgStyle;
	private final Image cIcon;
	private final BitmapFont font, msgFont;
	private final TabBar tabs;
	private final Menu menu;
	
	private final GameLoop loop;
	private final CardGame game;
	
	public GUI(final CardGame game, final GameLoop loop){
		super();
		
		this.game = game;
		this.loop = loop;
        
        setFillParent(true);
        setClip(true);
        if (CardGame.debug) debug();
        
        stackTable = new Table();
		handTable = new Table();
		
		addListener(new PutListener(){
			public void put(Card card){
				loop.playCard(card);
			}
		});
		
		font = game.getAssets().get("fonts/Roboto-Regular.ttf", BitmapFont.class);
        msgFont = game.getAssets().get("fonts/Roboto-Medium.ttf", BitmapFont.class);
        

        atlas = game.getAssets().get("ui.atlas", TextureAtlas.class);
        AtlasRegion but = atlas.findRegion("button");
        AtlasRegion msgbackground = atlas.findRegion("dialog");
        AtlasRegion cards = atlas.findRegion("cards");
        
        /** Warning Dialog */
        
        WindowStyle dStyle = new WindowStyle();
        dStyle.titleFont = font;
        dStyle.titleFontColor = Color.WHITE;
        dStyle.background = new TextureRegionDrawable(msgbackground);
        
        error = new Dialog("", dStyle);
        if (CardGame.debug) error.debug();
        
        TextButtonStyle buttonStyle = new TextButtonStyle();
        buttonStyle.up = new TextureRegionDrawable(but);
        buttonStyle.font = msgFont;
        buttonStyle.fontColor = Color.WHITE;
        
        msgStyle = new LabelStyle();
        msgStyle.font = msgFont;
        msgStyle.fontColor = Color.WHITE;
        
        TextButton button = new TextButton("OK", buttonStyle);
        button.addListener(new ClickListener(){
        	public void clicked (InputEvent event, float x, float y) {
        		error.hide();
        	}
        });
        
        cIcon = new Image(cards);
        
        error.getCell(error.getContentTable()).height(550).fill(true, false);
        error.getCell(error.getButtonTable()).height(250).fill(true, false);
        
        error.button(button);
        
        /** Tab bar */
        tabs = new TabBar(loop, this, game);
        
        menu = new Menu(game, this, tabs);
        
        /** Content */
        
        ScrollPane stackPane = new ScrollPane(stackTable);
        ScrollPane handPane = new ScrollPane(handTable);
        
        stackTable.pad(400/3, 0, 400/3, 0).defaults().expandX().spaceRight(4.5f);
        handTable.pad(40).defaults().expandX().space(10);
        
        
        add(tabs).height(220).minWidth(1080).left();
        row();
        add(stackPane).height(800);
        row();
        add(handPane).height(900);
        
        update();
        
	}
	
	public GameLoop getLoop() { return loop; }
	
	public void toggleMenu(){
		if (menu.hasParent()) menu.hide();
		else menu.show();
	}
	
	public void message(String text){
		error.getContentTable().clear();
		error.getContentTable().add(cIcon).space(100).row();
		error.text(text, msgStyle);
		error.show(game.stage);
	}
	
	public void update(){
		tabs.update();
		
		stackTable.clear();
		
		for (Card card : loop.getStack()){
	        card.setScaling(Scaling.fit);
	        stackTable.add(card).width(400).height(1600/3);
		}
		
		handTable.clear();
		for (Card card : loop.getPlayers().first()) {
	        card.setScaling(Scaling.fit);
	        handTable.add(card).width(600).height(800);	        
		}
	}	

}
