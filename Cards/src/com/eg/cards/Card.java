package com.eg.cards;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.eg.cards.ui.CardGame;

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
	
	private final int id;
	private CardSuit suit;
	private CardSymbol symbol;
	private int trump;
	
	public Card(TextureRegion region, final int id){
		super(region);
		this.id = id;
		
		findType();
		
		addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if (CardGame.debug) System.out.println("click " + id);
				fire(new PutEvent(id));
			}
		});
	}
	
	public CardSuit getSuit(){ return suit; }
	public CardSymbol getSymbol(){ return symbol; }
	public int getTrumpValue(){ return trump; }

	private void findType() {
		int type = id>19? id-20 : id;
		if (type<5) suit = CardSuit.SPADES;
		else{
			switch (type){
			case 5: case  6: case  8: case  12: case  16: suit = CardSuit.HEARTS; break;
			case 7: case  9: case  10: case  13: case  17: suit = CardSuit.DIAMONDS; break;
			default: suit = CardSuit.CLUBS;
			}
		}
		switch (type){
		case 0: case  7: case  8: case  11: symbol = CardSymbol.QUEEN; break;
		case 1: case  9: case  12: case  14: symbol = CardSymbol.KING; break;
		case 2: case  13: case  15: case  16: symbol = CardSymbol.KNAVE; break;
		case 3: case  5: case  17: case  18: symbol = CardSymbol.ACE; break;
		default: symbol = CardSymbol.TEN;
		}
		
		trump = 0;
		if (suit == CardSuit.DIAMONDS) trump = 1;
		if (symbol == CardSymbol.KNAVE) trump = 2;
		else if (symbol == CardSymbol.QUEEN) trump = 3;
		else if (symbol == CardSymbol.TEN && suit == CardSuit.HEARTS) trump = 4;
	}
	
	@Override
	public String toString(){
		return suit+" "+symbol+" #"+id;
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
