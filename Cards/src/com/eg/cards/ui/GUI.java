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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;
import com.eg.cards.Card;
import com.eg.cards.Card.CardSuit;
import com.eg.cards.Card.CardSymbol;
import com.eg.cards.Player;
import com.eg.cards.GameLoop;

public class GUI extends Table implements Disposable{
	
	private final Table handTable, stackTable, tabTable;
	private GameLoop loop;
	private BitmapFont defaultFont, msgFont;
	private Array<Label> labels;
	private Array<Image> avatars, icons;
	private Array<Table> info, tabs;
	private TextureAtlas atlas;
	private Dialog error;
	private LabelStyle scoreStyle, msgStyle;
	private Image cIcon;
	
	private Array<AtlasRegion> iconRegions;
	
	private final CardGame game;
	
	public GUI(CardGame game){
		super();
		
		this.game = game;
		
		FreeTypeFontGenerator generator =
        		new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto-Regular.ttf"));
		defaultFont = generator.generateFont(60);
        generator.dispose();
        
        generator =
        		new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto-Medium.ttf"));
        msgFont = generator.generateFont(60);
        generator.dispose();
        
        setFillParent(true);
        if (CardGame.debug) debug();
        
        stackTable = new Table();
		handTable = new Table();
		tabTable = new Table();
		
		if (CardGame.debug) tabTable.debug();
        
	}
	
	public void init(GameLoop loop){
		this.loop = loop;
		
		final Array<Player> players = loop.getPlayers();
		
        atlas = game.getUIAtlas();
        AtlasRegion avatar = atlas.findRegion("avatar");
        AtlasRegion tabBg = atlas.findRegion("tab");
        AtlasRegion but = atlas.findRegion("button");
        AtlasRegion msgbackground = atlas.findRegion("dialog");
        AtlasRegion cards = atlas.findRegion("cards");
        
        iconRegions = new Array<AtlasRegion>(10);
        iconRegions.add(atlas.findRegion("9"));
        iconRegions.add(atlas.findRegion("10"));
        iconRegions.add(atlas.findRegion("ace"));
        iconRegions.add(atlas.findRegion("helbard"));
        iconRegions.add(atlas.findRegion("king"));
        iconRegions.add(atlas.findRegion("queen"));
        iconRegions.add(atlas.findRegion("club"));
        iconRegions.add(atlas.findRegion("spade"));
        iconRegions.add(atlas.findRegion("heart"));
        iconRegions.add(atlas.findRegion("diamond"));
        
        labels = new Array<Label>(4);
        avatars = new Array<Image>(4);
        info = new Array<Table>(4);
        tabs = new Array<Table>(4);
        icons = new Array<Image>(8);
        
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
        
        scoreStyle = new LabelStyle();
        scoreStyle.font = defaultFont;
        scoreStyle.fontColor = Color.BLACK;
        
        for(Player p : players){
        	Image img = new Image(avatar);
        	if (!p.isTeamRe()) img.setColor(212/255f, 0, 0, 1);
        	else img.setColor(Color.BLACK);
        	Label l = new Label(Short.toString(p.getPoints()), scoreStyle);
        	l.setAlignment(Align.center);
        	avatars.add(img);
        	labels.add(l);
        }
        
        addListener(new UpdateListener(){
        	public void update(UpdateEvent e){
        		for (int i=0; i<players.size; i++)
        			labels.get(i).setText(Short.toString(players.get(i).getPoints()));
        	}
        });
        
        tabTable.center();
        tabTable.defaults().expandX().space(0);
        
        for (int i=0; i<8; i++) icons.add(new Image());
        
        for (int i=0; i<4; i++){
        	Table t = new Table();
        	Table top = new Table();
        	Table bottom = new Table();
        	
        	t.setBackground(new TextureRegionDrawable(tabBg));
        	
        	top.defaults().expandX().spaceRight(20);
        	top.add(avatars.get(i));
        	top.add(labels.get(i));
        	
        	bottom.defaults().expandX().spaceRight(20);
        	bottom.add(icons.get(2*i));
        	bottom.add(icons.get(2*i+1));
        	tabs.add(bottom);
        	
        	t.add(top).height(120).padLeft(20).row();
        	t.add(bottom).height(100).padRight(20);
        	
        	if (CardGame.debug) t.debug();
        	info.add(t);
        }
        
        for (Table t : info) tabTable.add(t);
        
        /** Content */
        
        ScrollPane stackPane = new ScrollPane(stackTable);
        ScrollPane handPane = new ScrollPane(handTable);
        
        stackTable.pad(400/3, 0, 400/3, 0).defaults().expandX().spaceRight(4.5f);
        handTable.pad(40).defaults().expandX().space(10);
        
        
        add(tabTable).fillX().height(220).minWidth(1080);
        row();
        add(stackPane).height(800);
        row();
        add(handPane).height(900);
        
        updateCards();
	}
	
	public void message(String text){
		error.getContentTable().clear();
		error.getContentTable().add(cIcon).space(100).row();
		error.text(text, msgStyle);
		error.show(game.stage);
	}
	
	public void updateCards(){
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
	
	public void updateTabs(){
		for (Image img : icons) img.setDrawable(null);
		for (int i=0; i<loop.getStack().size; i++){
			int index = loop.getStartPlayer()+i;
			index = index>3? index-4 : index;
			icons.get(2*index).setDrawable(findSuit(loop.getStack().get(i)));
			icons.get(2*index+1).setDrawable(findSymbol(loop.getStack().get(i)));
		}
	}
	
	private Drawable findSuit(Card card){
		if (card.getSuit().equals(CardSuit.CLUBS))
			return new TextureRegionDrawable(iconRegions.get(6));
		else if (card.getSuit().equals(CardSuit.SPADES))
			return new TextureRegionDrawable(iconRegions.get(7));
		else if (card.getSuit().equals(CardSuit.HEARTS))
			return new TextureRegionDrawable(iconRegions.get(8));
		return new TextureRegionDrawable(iconRegions.get(9));
	}
	
	private Drawable findSymbol(Card card){
		if (card.getSymbol().equals(CardSymbol.TEN))
			return new TextureRegionDrawable(iconRegions.get(1));
		else if (card.getSymbol().equals(CardSymbol.ACE))
			return new TextureRegionDrawable(iconRegions.get(2));
		else if (card.getSymbol().equals(CardSymbol.KNAVE))
			return new TextureRegionDrawable(iconRegions.get(3));
		else if (card.getSymbol().equals(CardSymbol.KING))
			return new TextureRegionDrawable(iconRegions.get(4));
		else
			return new TextureRegionDrawable(iconRegions.get(5));
	}

	@Override
	public void dispose() {
		defaultFont.dispose();
		msgFont.dispose();
	}
	
	

}
