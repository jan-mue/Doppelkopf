package com.eg.cards.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.eg.cards.Card;
import com.eg.cards.GameLoop;
import com.eg.cards.Card.CardSuit;
import com.eg.cards.Card.CardSymbol;

public class TabBar extends WidgetGroup{
	
	private Array<Label> labels;
	private Array<Image> icons, avatars;
	private Array<Table> tabs;
	private Image nav, drawer;
	private LabelStyle scoreStyle;
	private Array<AtlasRegion> iconRegions;
	private GameLoop loop;
	
	public TabBar(final GameLoop loop, TextureAtlas atlas, final BitmapFont font){
		super();
		
		this.loop = loop;
        
        AtlasRegion avatar = atlas.findRegion("avatar");
        AtlasRegion tabBg = atlas.findRegion("tab");
        AtlasRegion navBg = atlas.findRegion("nav");
        AtlasRegion navIcon = atlas.findRegion("drawer");
        
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
        labels = new Array<Label>(4);
        icons = new Array<Image>(8);
        avatars = new Array<Image>(4);
		
		scoreStyle = new LabelStyle();
        scoreStyle.font = font;
        scoreStyle.fontColor = Color.WHITE;
        
        for(int i=0; i<4; i++){
        	Image img = new Image(avatar);
        	img.setColor(Color.BLACK);
        	Label l = new Label("0", scoreStyle);
        	l.setAlignment(Align.center);
        	l.setColor(Color.BLACK);
        	avatars.add(img);
        	labels.add(l);
        }
        
        for (int i=0; i<8; i++) icons.add(new Image());
        
        nav = new Image(navBg);
        drawer = new Image(navIcon);
        
        nav.setBounds(0, 0, 96, 220);
        drawer.setBounds(0, 89, 30, 44);
        addActor(nav);
        addActor(drawer);
        
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
        	
        	tab.add(top).height(120).padLeft(20).row();
        	tab.add(bottom).height(100).padRight(20);
        	
        	if (CardGame.debug) tab.debug();
        	tab.setBounds(71+246*i, 0, 270, 220);
        	
        	addActor(tab);
        	tabs.add(tab);
        }
	}
	
	public void update(){
		for (int i=0; i<loop.getPlayers().size; i++){
			labels.get(i).setText(Integer.toString(loop.getPlayers().get(i).getPoints()));
			if(loop.getPlayers().get(i).getTeamVisible()){
				tabs.get(i).setColor(Color.BLACK);
				labels.get(i).setColor(Color.WHITE);
				avatars.get(i).setColor(Color.WHITE);
				icons.get(2*i).setColor(Color.WHITE);
				icons.get(2*i+1).setColor(Color.WHITE);
			}
			else{
				icons.get(2*i).setColor(Color.BLACK);
				icons.get(2*i+1).setColor(Color.BLACK);
			}
		}
		
		for (Image img : icons) img.setDrawable(null);
		for (int i=0; i<loop.getStack().size; i++){
			int index = loop.getStartPlayer()+i;
			index = index>3? index-4 : index;
			Card c = loop.getStack().get(i);
			if (c.getSuit().equals(CardSuit.DIAMONDS) || c.getSuit().equals(CardSuit.HEARTS)){
				icons.get(2*index).setColor(Color.WHITE);
				if (!c.getSymbol().equals(CardSymbol.KING) && !c.getSymbol().equals(CardSymbol.QUEEN) && !c.getSymbol().equals(CardSymbol.KNAVE))
					icons.get(2*index+1).setColor(GUI.RED);
			}
			icons.get(2*index).setDrawable(findSuit(c));
			icons.get(2*index+1).setDrawable(findSymbol(c));
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
