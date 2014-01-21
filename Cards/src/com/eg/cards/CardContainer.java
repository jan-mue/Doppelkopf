package com.eg.cards;

import java.util.Comparator;

import com.badlogic.gdx.utils.Array;
import com.eg.cards.Card.CardSuit;
import com.eg.cards.Card.CardSymbol;

public class CardContainer extends Array<Card>{
	
	private class SuitComparator implements Comparator<Card>{
		
		private CardSuit suit;
		
		private SuitComparator(CardSuit suit){
			super();
			this.suit = suit;
		}

		@Override
		public int compare(Card c1, Card c2) {
			int result = c1.getSuit().compareTo(c2.getSuit());
			if (result == 0) return c1.compareTo(c2);
			else if (c1.getSuit()==suit) return 1;
			else if (c2.getSuit()==suit) return -1;
			return result;
		}
		
	}
	
	private final int capacity;
	
	public CardContainer(int capacity){
		super(capacity);
		this.capacity =  capacity;
	}
	
	public CardContainer(final CardContainer cards){
		super(cards);
		capacity=cards.getCapacity();
	}
	
	public int getCapacity(){ return capacity; }
	
	public boolean addCard(final Card card){
		if (contains(card, true) || size>=capacity) return false;
		add(card);
		return true;
	}
	
	public boolean remove(Card card) {
		return removeValue(card, true);
	}
	
	public boolean contains(final CardSuit suit, final CardSymbol symbol){
		if (size==0) return false;
		for (Card c : this)
			if (c.getSymbol().equals(symbol) && c.getSuit().equals(suit))
				return true;
		
		return false;
	}
	
	public boolean contains(final CardSuit suit){
		if (size==0) return false;
		for (Card c : this)
			if (c.getSuit().equals(suit)) return true;
		return false;
	}
	
	public boolean contains(final CardSymbol symbol){
		if (size==0) return false;
		for (Card c : this)
			if (c.getSymbol().equals(symbol)) return true;
		return false;
	}
	
	public boolean containsTrump(){
		if (size==0) return false;
		for (Card c : this)
			if (c.getTrumpValue()>0) return true;
		return false;
	}
	
	public int count(final CardSuit suit){
		int result=0;
		if (size==0) return result;
		for (Card c : this)
			if (c.getSuit().equals(suit)) result++;
		return result;
	}
	
	public int count(final CardSymbol symbol){
		int result=0;
		if (size==0) return result;
		for (Card c : this)
			if (c.getSymbol().equals(symbol)) result++;
		return result;
	}
	
	public CardContainer get(final CardSuit suit){		
		CardContainer result=new CardContainer(count(suit));
		if (size==0) return result;
		
		for (Card c : this)
			if (c.getSuit().equals(suit)) result.add(c);
		return result;
	}
	
	public CardContainer get(final CardSymbol symbol){
		CardContainer result=new CardContainer(count(symbol));
		if (size==0) return result;
		
		for (Card c : this)
			if (c.getSymbol().equals(symbol)) result.add(c);
		return result;
	}
	
	public CardContainer get(final CardSuit suit, final CardSymbol symbol){
		CardContainer result=new CardContainer(2);
		if (size==0) return result;
		
		for (Card c : this)
			if (c.getSymbol().equals(symbol) && c.getSuit().equals(suit))
				result.add(c);
		
		return result;
	}
	
	public CardContainer getColors(){
		CardContainer result=new CardContainer(this.size);
		
		for (Card c : this) if (c.getTrumpValue()==0)
			result.addCard(c);
		
		return result;
	}
	
	public void sortBy(final CardSuit suit){
		sort(new SuitComparator(suit));
	}
	
	public void reset(){
		clear();
	}

}
