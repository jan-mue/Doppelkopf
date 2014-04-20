package com.eg.cards.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.eg.cards.model.Card.CardSuit;
import com.eg.cards.model.Card.CardSymbol;

public class CardContainer extends ArrayList<Card>{
	
	private static final long serialVersionUID = -5795151125322862666L;

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
	
	private class SymbolComparator implements Comparator<Card>{
		
		private CardSymbol symbol;
		
		private SymbolComparator(CardSymbol symbol){
			super();
			this.symbol = symbol;
		}

		@Override
		public int compare(Card c1, Card c2) {
			int result = c1.getSymbol().compareTo(c2.getSymbol());
			if (result == 0) return c1.getSuit().compareTo(c2.getSuit());
			else if (c1.getSymbol()==symbol) return 1;
			else if (c2.getSymbol()==symbol) return -1;
			return result;
		}
		
	}
	
	private final int capacity;
	
	public CardContainer(int capacity){
		super(capacity);
		this.capacity = capacity;
	}
	
	public CardContainer(final CardContainer cards){
		super(cards);
		capacity = cards.getCapacity();
	}
	
	public int getCapacity(){ return capacity; }
	
	@Override
	public boolean add(final Card card){
		if (size() >= capacity) return false;
		boolean result = super.add(card);
		sort();
		return result;
	}
	
	public boolean remove(Card card, boolean identity) {
		if (identity || card == null) {
			for (int i = 0, n = size(); i < n; i++) {
				if (get(i) == card) {
					remove(i);
					return true;
				}
			}
		} else {
			remove(card);
			return true;
		}
		
		return false;
    }
	
	public boolean removeAll(CardSuit suit, CardSymbol symbol){
		return removeAll(get(suit, symbol));
	}
	
	public boolean removeAll(CardSuit suit){
		return removeAll(get(suit));
	}
	
	public boolean removeAll(CardSymbol symbol){
		return removeAll(get(symbol));
	}
	
	public boolean contains(final CardSuit suit, final CardSymbol symbol){
		if (size()==0) return false;
		for (Card c : this)
			if (c.getSymbol().equals(symbol) && c.getSuit().equals(suit))
				return true;
		
		return false;
	}
	
	public boolean contains(final CardSuit suit){
		if (size()==0) return false;
		for (Card c : this)
			if (c.getSuit().equals(suit)) return true;
		return false;
	}
	
	public boolean contains(final CardSymbol symbol){
		if (size()==0) return false;
		for (Card c : this)
			if (c.getSymbol().equals(symbol)) return true;
		return false;
	}
	
	public boolean containsColor(){
		if (size()==0) return false;
		for (Card c : this)
			if (c.getTrumpValue()==0) return true;
		return false;
	}
	
	public boolean containsTrump(){
		if (size()==0) return false;
		for (Card c : this)
			if (c.getTrumpValue()>0) return true;
		return false;
	}
	
	@Override
	public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size(); i++)
                if (get(i) == null)
                    return i;
        } else {
            for (int i = 0; i < size(); i++)
                if (o == get(i))
                    return i;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size()-1; i >= 0; i--)
                if (get(i) == null)
                    return i;
        } else {
            for (int i = size()-1; i >= 0; i--)
                if (o == get(i))
                    return i;
        }
        return -1;
    }
    
    public int getPoints(){
    	int pts = 0;
		for (Card c : this) pts += c.getValue();
		return pts;
    }
	
	public int count(final CardSuit suit){
		int result=0;
		if (size()==0) return result;
		for (Card c : this)
			if (c.getSuit().equals(suit)) result++;
		return result;
	}
	
	public int count(final CardSymbol symbol){
		int result=0;
		if (size()==0) return result;
		for (Card c : this)
			if (c.getSymbol().equals(symbol)) result++;
		return result;
	}
	
	public int count(final CardSuit suit, final CardSymbol symbol){
		int result=0;
		if (size()==0) return result;
		for (Card c : this)
			if (c.getSuit().equals(suit) && c.getSymbol().equals(symbol)) result++;
		return result;
	}
	
	public CardContainer get(final CardSuit suit){		
		CardContainer result=new CardContainer(count(suit));
		if (size()==0) return result;
		
		for (Card c : this)
			if (c.getSuit().equals(suit)) result.add(c);
		return result;
	}
	
	public CardContainer get(final CardSymbol symbol){
		CardContainer result=new CardContainer(count(symbol));
		if (size()==0) return result;
		
		for (Card c : this)
			if (c.getSymbol().equals(symbol)) result.add(c);
		return result;
	}
	
	public CardContainer get(final CardSuit suit, final CardSymbol symbol){
		CardContainer result=new CardContainer(2);
		if (size()==0) return result;
		
		for (Card c : this)
			if (c.getSymbol().equals(symbol) && c.getSuit().equals(suit))
				result.add(c);
		
		return result;
	}
	
	public CardContainer getColors(){
		CardContainer result = new CardContainer(this.size());
		
		for (Card c : this) if (c.getTrumpValue() == 0)
			result.add(c);
		
		return result;
	}
	
	public void randomize(){
		Collections.shuffle(this);
	}
	
	public Card first(){ return get(0); }
	
	public Card peek(){ return get(size()-1); }
	
	public void sort(){
		Collections.sort(this);
	}
	
	public void sort(final CardSuit suit){
		Collections.sort(this, new SuitComparator(suit));
	}
	
	public void sort(final CardSymbol symbol){
		Collections.sort(this, new SymbolComparator(symbol));
	}
	
	public void reset(){
		clear();
	}

}
