package com.eg.cards;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ImageCard extends Card{
	public final Image img;
	
	
	public ImageCard(AtlasRegion region){
		super(findSuit(region), findSymbol(region));
		
		img = new Image(region);
		img.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				img.fire(new PutEvent(ImageCard.this));
			}
		});
	}
	
	private static CardSuit findSuit(AtlasRegion region){
		if (region.name.contains("club")) return CardSuit.CLUBS;
		if (region.name.contains("spade")) return CardSuit.SPADES;
		if (region.name.contains("heart")) return CardSuit.HEARTS;
		return CardSuit.DIAMONDS;
	}
	
	private static CardSymbol findSymbol(AtlasRegion region){
		if (region.name.contains("Ace")) return CardSymbol.ACE;
		if (region.name.contains("10")) return CardSymbol.TEN;
		if (region.name.contains("King")) return CardSymbol.KING;
		if (region.name.contains("Queen")) return CardSymbol.QUEEN;
		if (region.name.contains("Jack")) return CardSymbol.KNAVE;
		return CardSymbol.NINE;
	}
}
