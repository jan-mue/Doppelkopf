package com.eg.cards.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;
import com.eg.cards.Card;
import com.eg.cards.GameLoop;

public class GUI extends Table implements Disposable{
	
	private final Table handTable, stackTable;
	private GameLoop loop;
	private TextureAtlas atlas;
	private Dialog error;
	private LabelStyle msgStyle;
	private Image cIcon;
	private BitmapFont defaultFont, msgFont;
	private TabBar tabs;
	
	private final CardGame game;
	
	public GUI(CardGame game){
		super();
		
		this.game = game;
        
        setFillParent(true);
        if (CardGame.debug) debug();
        
        stackTable = new Table();
		handTable = new Table();
		
		FreeTypeFontGenerator generator =
        		new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto-Regular.ttf"));
		defaultFont = generator.generateFont(60);
        generator.dispose();
        
        generator =
        		new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto-Medium.ttf"));
        msgFont = generator.generateFont(60);
        generator.dispose();
        
	}
	
	public void init(GameLoop loop){
		this.loop = loop;
		
        atlas = game.getUIAtlas();
        AtlasRegion but = atlas.findRegion("button");
        AtlasRegion msgbackground = atlas.findRegion("dialog");
        AtlasRegion cards = atlas.findRegion("cards");
        
        /** Warning Dialog */
        
        WindowStyle dStyle = new WindowStyle();
        dStyle.titleFont = defaultFont;
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
        tabs = new TabBar(loop, atlas, defaultFont);
        
        /** Content */
        
        ScrollPane stackPane = new ScrollPane(stackTable);
        ScrollPane handPane = new ScrollPane(handTable);
        
        stackTable.pad(400/3, 0, 400/3, 0).defaults().expandX().spaceRight(4.5f);
        handTable.pad(40).defaults().expandX().space(10);
        
        
        add(tabs).fillX().height(220).minWidth(1080);
        row();
        add(stackPane).height(800);
        row();
        add(handPane).height(900);
        
        update();
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

	@Override
	public void dispose() {
		defaultFont.dispose();
		msgFont.dispose();
	}
	
	

}
