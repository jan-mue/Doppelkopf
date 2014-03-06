package com.eg.cards;

import com.eg.cards.Card.CardSuit;
import com.eg.cards.Card.CardSymbol;
import com.eg.cards.ui.CardGame;

public class Player<T extends Card> extends CardContainer<T>{
	
	private int points=0;
	public final int id;
	private Player<T> partner;
	private boolean team;
	
	public Player(int id){
		super(10);
		this.id=id;
		partner = null;
		team = false;
	}
	
	public Player<T> getPartner(){ return partner; }
	
	public boolean getTeamVisible(){ return team; }
	
	public int getPoints(){ return points; }
	
	public void addPoints(int points){
		this.points += Math.abs(points);
	}
	
	@Override
	public void reset(){
		super.reset();
		points=0;		
		partner = null;
		team = false;
	}
	
	
	public boolean play(final T card, final Stack<T> stack) throws IllegalArgumentException{
		if (CardGame.debug) System.out.println(card + " played by " + this);
		
		if (card.getSymbol().equals(CardSymbol.QUEEN) && card.getSuit().equals(CardSuit.CLUBS))
			team = true;
		
		if (!stack.check(card, this)) 
			throw new IllegalArgumentException();
		
		if (!remove(card)) 
			throw new IllegalArgumentException();
		
		try{
			return stack.addCard(card);
		}
		catch (IllegalArgumentException e){
			addCard(card);
			throw e;
		}
	}
	
	//Overloaded for AI-play
	public boolean play(final Stack<T> stack, Player<T> best){
		return play(selectCard(stack, best), stack);
	}
	
	private T selectCard(final Stack<T> stack, Player<T> best){
		CardContainer<T> colors = getColors();
		CardContainer<T> trumps = new CardContainer<T>(this);
		trumps.removeAll(colors, true);
		
		colors.sort();
		trumps.sort();
		
		Card highcard = stack.getHighCard();
		
		//player leads
		if (highcard == null){
			if (colors.contains(CardSymbol.ACE)){
				CardContainer<T> aces=colors.get(CardSymbol.ACE);
				for (int i=aces.size-1; i>=0; i--){
					T c=aces.get(i);
					if(colors.count(c.getSuit())<(c.getSuit().equals(CardSuit.HEARTS)? 2 : 4) &&
							stack.getPlayedCards().count(c.getSuit())<3)
						return c;
				}
			}
			
			//no (fitting) ace found
			
			if (trumps.size!=0){
				//play highest card
				CardContainer<T> tmp = trumps.get(CardSuit.HEARTS, CardSymbol.TEN);
				if (tmp.size==2 && stack.getPlayedCards().size>20) return tmp.first();
				
				//play trump with low point value
				if (trumps.contains(CardSymbol.KNAVE)){
					CardContainer<T> knaves = trumps.get(CardSymbol.KNAVE);
					return knaves.random();
				}
				
				//no knave found
				if (trumps.contains(CardSymbol.QUEEN)){
					CardContainer<T> queens = trumps.get(CardSymbol.QUEEN);
					return queens.first();
				}
			}
			
			//no (fitting) trump found
			
			if (colors.contains(CardSymbol.KING)){
				CardContainer<T> kings = colors.get(CardSymbol.KING);
				return kings.first();
			}
			if (colors.size!=0) return colors.first();
		}
		
		//play color
		if(!stack.isTrump()){
			
			CardSuit suit = stack.getSuit();
			if(colors.contains(suit)){
				
				if (CardGame.debug) System.out.println(this+" has a Color of Suit " + suit);
				
				//remove all other colors
				CardContainer<T> tmp = new CardContainer<T>(colors.size);
				for (T c : colors) if (!c.getSuit().equals(suit))
					tmp.add(c);
				colors.removeAll(tmp, true);
				
				//highest Card is Trump or is higher Color than all the available cards
				if(highcard.getTrumpValue()!=0 || highcard.compareTo(colors.peek())>=0){
					//Same team?
					if (best == partner)
						//Pick card with highest value
						return colors.peek();
					else
					//Pick lowest possible card
					return colors.first();
				}
				else return colors.peek();
			
			}
			else{
				if (CardGame.debug) System.out.println(this+" has no Color of Suit " + suit);
				
				//Play color only to help partner
				if (best == partner){
					
				}
			}
		}
		
		//Play Trump
		
		if (trumps.size==0){
			if (CardGame.debug) System.out.println(this+" has no trumps");
			return colors.first();
		}
		
		//Find lowest fitting trump
		T lowest = null;
		for (T c : trumps) if (c.compareTo(highcard)>0){
			lowest = c;
			break;
		}
		
		return (lowest==null)? trumps.first() : lowest;
	}
	
	@Override
	public String toString(){
		return "Player "+id;
	}

}
