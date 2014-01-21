package com.eg.cards;

import com.eg.cards.Card.CardSuit;

public class Stack extends CardContainer{
	
	private Card highcard;
	
	public Stack(){
		super(4);
	}
	
	public int count(){
		int pts = 0;
		for (Card c : this) pts += c.getSymbol().getValue();
		return pts;
	}
	
	public boolean isTrump(){
		if (size==0) return false;
		return first().getTrumpValue()!=0;
	}
	
	public CardSuit getSuit(){
		if (size==0) return null;
		return first().getSuit();
	}
	
	public Card getHighCard(){ return highcard; }
	
	public boolean check(Card c, Player p){
		return size==0 ||
				(isTrump() && (c.getTrumpValue()>0 || !p.containsTrump()))
				|| (!isTrump() && ((c.getTrumpValue()==0 && getSuit().equals(c.getSuit()))
						|| !p.getColors().contains(getSuit())));
	}
	
	@Override
	//returns true if card was higher
	public boolean addCard(final Card card) throws IllegalArgumentException{			
		if(!super.addCard(card)) throw new IllegalArgumentException();
		
		else if (highcard == null || (((card.getTrumpValue()>0) || //Trump
				(!isTrump() && card.getSuit().equals(first().getSuit()))) //Same Color
				&& card.compareTo(highcard)>0)){
			highcard = card;
			return true;
		}
		return false;
	}
	
	@Override
	public void reset(){
		super.reset();
		highcard=null;
	}

}
