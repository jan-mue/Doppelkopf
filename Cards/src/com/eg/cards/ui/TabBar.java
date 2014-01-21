package com.eg.cards.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.eg.cards.Card;
import com.eg.cards.GameLoop;
import com.eg.cards.Card.CardSuit;
import com.eg.cards.Card.CardSymbol;

public class TabBar extends Table{
	
	private Array<Label> labels;
	private Array<Image> avatars, icons;
	private Array<Table> iconWrappers, tabs;
	private LabelStyle scoreStyle;
	private Array<AtlasRegion> iconRegions;
	private GameLoop loop;
	
	public TabBar(final GameLoop loop, TextureAtlas atlas, final BitmapFont font){
		super();
		
		this.loop = loop;
		
		if (CardGame.debug) debug();
		center();
        defaults().expandX();
        
        AtlasRegion avatar = atlas.findRegion("avatar");
        AtlasRegion tabBg = atlas.findRegion("tab");
        
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
        
        tabs = new Array<Table>(4);
        
        avatars = new Array<Image>(4);
        labels = new Array<Label>(4);
        iconWrappers = new Array<Table>(4);
        icons = new Array<Image>(8);
		
		scoreStyle = new LabelStyle();
        scoreStyle.font = font;
        scoreStyle.fontColor = Color.BLACK;
        
        for(int i=0; i<4; i++){
        	Image img = new Image(avatar);
        	img.setColor(Color.BLACK);
        	Label l = new Label("0", scoreStyle);
        	l.setAlignment(Align.center);
        	avatars.add(img);
        	labels.add(l);
        }
        
        for (int i=0; i<8; i++) icons.add(new Image());
        
        for (int i=0; i<4; i++){
        	Table tab = new Table();
        	Table top = new Table();
        	Table bottom = new Table();
        	
        	tab.setBackground(new TextureRegionDrawable(tabBg));
        	
        	top.defaults().expandX().spaceRight(20);
        	top.add(avatars.get(i));
        	top.add(labels.get(i));
        	
        	bottom.defaults().expandX().spaceRight(20);
        	bottom.add(icons.get(2*i));
        	bottom.add(icons.get(2*i+1));
        	iconWrappers.add(bottom);
        	
        	tab.add(top).height(120).padLeft(20).row();
        	tab.add(bottom).height(100).padRight(20);
        	
        	if (CardGame.debug) tab.debug();
        	tabs.add(tab);
        }
        
        for (Table t : tabs) add(t);
	}
	
	public void update(){
		for (int i=0; i<loop.getPlayers().size; i++)
			labels.get(i).setText(Integer.toString(loop.getPlayers().get(i).getPoints()));
		
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

}
