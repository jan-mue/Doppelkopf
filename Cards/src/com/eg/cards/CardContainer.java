package com.eg.cards;

import java.util.Comparator;

import com.badlogic.gdx.utils.Array;
import com.eg.cards.Card.CardSuit;
import com.eg.cards.Card.CardSymbol;

public class CardContainer<T extends Card> extends Array<T>{
	
	private class SuitComparator implements Comparator<T>{
		
		private CardSuit suit;
		
		private SuitComparator(CardSuit suit){
			super();
			this.suit = suit;
		}

		@Override
		public int compare(T c1, T c2) {
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
	
	public CardContainer(final CardContainer<T> cards){
		super(cards);
		capacity=cards.getCapacity();
	}
	
	public int getCapacity(){ return capacity; }
	
	public boolean addCard(final T card){
		if (contains(card, true) || size>=capacity) return false;
		add(card);
		return true;
	}
	
	public boolean remove(T card) {
		return removeValue(card, true);
	}
	
	public boolean contains(final CardSuit suit, final CardSymbol symbol){
		if (size==0) return false;
		for (T c : this)
			if (c.getSymbol().equals(symbol) && c.getSuit().equals(suit))
				return true;
		
		return false;
	}
	
	public boolean contains(final CardSuit suit){
		if (size==0) return false;
		for (T c : this)
			if (c.getSuit().equals(suit)) return true;
		return false;
	}
	
	public boolean contains(final CardSymbol symbol){
		if (size==0) return false;
		for (T c : this)
			if (c.getSymbol().equals(symbol)) return true;
		return false;
	}
	
	public boolean containsColor(){
		if (size==0) return false;
		for (T c : this)
			if (c.getTrumpValue()==0) return true;
		return false;
	}
	
	public boolean containsTrump(){
		if (size==0) return false;
		for (T c : this)
			if (c.getTrumpValue()>0) return true;
		return false;
	}
	
	public int count(final CardSuit suit){
		int result=0;
		if (size==0) return result;
		for (T c : this)
			if (c.getSuit().equals(suit)) result++;
		return result;
	}
	
	public int count(final CardSymbol symbol){
		int result=0;
		if (size==0) return result;
		for (T c : this)
			if (c.getSymbol().equals(symbol)) result++;
		return result;
	}
	
	public int count(final CardSuit suit, final CardSymbol symbol){
		int result=0;
		if (size==0) return result;
		for (T c : this)
			if (c.getSuit().equals(suit) && c.getSymbol().equals(symbol)) result++;
		return result;
	}
	
	public CardContainer<T> get(final CardSuit suit){		
		CardContainer<T> result=new CardContainer<T>(count(suit));
		if (size==0) return result;
		
		for (T c : this)
			if (c.getSuit().equals(suit)) result.add(c);
		return result;
	}
	
	public CardContainer<T> get(final CardSymbol symbol){
		CardContainer<T> result=new CardContainer<T>(count(symbol));
		if (size==0) return result;
		
		for (T c : this)
			if (c.getSymbol().equals(symbol)) result.add(c);
		return result;
	}
	
	public CardContainer<T> get(final CardSuit suit, final CardSymbol symbol){
		CardContainer<T> result=new CardContainer<T>(2);
		if (size==0) return result;
		
		for (T c : this)
			if (c.getSymbol().equals(symbol) && c.getSuit().equals(suit))
				result.add(c);
		
		return result;
	}
	
	public CardContainer<T> getColors(){
		CardContainer<T> result=new CardContainer<T>(this.size);
		
		for (T c : this) if (c.getTrumpValue()==0)
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
