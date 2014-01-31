package com.eg.cards;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Card extends Image implements Comparable<Card>{
	
	public enum CardSuit {
		DIAMONDS, HEARTS, SPADES, CLUBS;
	}

	public enum CardSymbol{
		NINE(0), KNAVE(2), QUEEN(3), KING(4), TEN(10), ACE(11);
		
		private int value;
		
		private CardSymbol(int value){
			this.value = value;
		}
		
		public int getValue(){ return value; }
	}
	
	private final CardSuit suit;
	private final CardSymbol symbol;
	private final int trump;
	
	public Card(AtlasRegion region){
		super(region);
		
		suit = findSuit(region);
		symbol = findSymbol(region);
		trump = findTrump();
		
		addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				fire(new PutEvent(Card.this));
			}
		});
	}
	
	public CardSuit getSuit(){ return suit; }
	public CardSymbol getSymbol(){ return symbol; }
	public int getTrumpValue(){ return trump; }
	
	private CardSuit findSuit(AtlasRegion region){
		if (region.name.contains("club")) return CardSuit.CLUBS;
		if (region.name.contains("spade")) return CardSuit.SPADES;
		if (region.name.contains("heart")) return CardSuit.HEARTS;
		return CardSuit.DIAMONDS;
	}
	
	private CardSymbol findSymbol(AtlasRegion region){
		if (region.name.contains("Ace")) return CardSymbol.ACE;
		if (region.name.contains("10")) return CardSymbol.TEN;
		if (region.name.contains("King")) return CardSymbol.KING;
		if (region.name.contains("Queen")) return CardSymbol.QUEEN;
		if (region.name.contains("Jack")) return CardSymbol.KNAVE;
		return CardSymbol.NINE;
	}
	
	private int findTrump(){
		if (symbol == CardSymbol.KNAVE) return 2;
		if (symbol == CardSymbol.QUEEN) return 3;
		if (symbol == CardSymbol.TEN && suit == CardSuit.HEARTS) return 4;
		if (suit == CardSuit.DIAMONDS) return 1;
		return 0;
	}
	
	@Override
	public String toString(){
		return symbol+" of "+suit;
	}

	@Override
	public int compareTo(Card otherCard) {
		if (equals(otherCard)) return 0;
		int result = getTrumpValue() - otherCard.getTrumpValue();
		if (result==0 && getTrumpValue()>1) return getSuit().compareTo(otherCard.getSuit());
		result = result==0? getSuit().compareTo(otherCard.getSuit()) : result;
		return result==0? getSymbol().compareTo(otherCard.getSymbol()) : result;
	}
	
	@Override
	public boolean equals(Object anObject) {
		if (anObject == null) return false;
        if (this == anObject) return true;
        if (getClass() != anObject.getClass()) return false;
        
        final Card anotherCard = (Card) anObject;
        return getSuit().equals(anotherCard.getSuit()) && getSymbol().equals(anotherCard.getSymbol());
    }

}
