package com.eg.cards;

import com.eg.cards.Card.CardSuit;
import com.eg.cards.Card.CardSymbol;
import com.eg.cards.ui.CardGame;

public class Player extends CardContainer{
	
	private int points=0;
	public final int id;
	private Player partner;
	private boolean team;
	
	public Player(int id){
		super(10);
		this.id=id;
		partner = null;
		team = false;
	}
	
	public Player getPartner(){ return partner; }
	public boolean getTeamVisible(){ return team; }
	
	
	public boolean playCard(final Card card, final Stack stack) throws IllegalArgumentException{
		if (CardGame.debug) System.out.println(card + " played by " + this);
		
		if (card.getSymbol().equals(CardSymbol.QUEEN) && card.getSuit().equals(CardSuit.CLUBS))
			team = true;
		
		if (!stack.check(card, this)) throw new IllegalArgumentException();
		
		if (!remove(card)) throw new IllegalArgumentException();
		
		try{
			return stack.addCard(card);
		}
		catch (IllegalArgumentException e){
			addCard(card);
			throw e;
		}
	}
	
	//Overloaded for AI-play
	public boolean playCard(final Stack stack, Player best){
		return playCard(selectCard(stack, best), stack);
	}
	
	private Card selectCard(final Stack stack, Player best){
		CardContainer colors = getColors();
		CardContainer trumps = new CardContainer(this);
		trumps.removeAll(colors, true);
		
		colors.sort();
		trumps.sort();
		
		Card highcard = stack.getHighCard();
		
		//Player leads
		if (highcard == null){
			if (colors.contains(CardSymbol.ACE)){
				CardContainer aces=colors.get(CardSymbol.ACE);
				for (int i=aces.size-1; i>=0; i--){
					Card c=aces.get(i);
					if(colors.count(c.getSuit())<(c.getSuit().equals(CardSuit.HEARTS)? 2 : 4))
						return c;
				}
			}
			
			//no (fitting) ace found
			
			if (trumps.size!=0){
				CardContainer tmp = trumps.get(CardSuit.HEARTS, CardSymbol.TEN);
				if (tmp.size==2) return tmp.first();
				tmp = trumps.get(CardSymbol.TEN);
				trumps.removeAll(tmp, true);
				tmp = trumps.get(CardSymbol.ACE);
				trumps.removeAll(tmp, true);
				
				if (trumps.size>0){
					if (trumps.contains(CardSymbol.KNAVE)){
						CardContainer knaves = trumps.get(CardSymbol.KNAVE);
						return knaves.random();
					}
					return trumps.first();
				}
			}
			
			//no (fitting) trump found
			
			if (colors.contains(CardSymbol.KING)){
				CardContainer kings = colors.get(CardSymbol.KING);
				return kings.first();
			}
			if (colors.size!=0) return colors.first();
		}
		
		//Play Color
		if(!stack.isTrump()){
			
			CardSuit suit = stack.getSuit();
			if(colors.contains(suit)){
				
				if (CardGame.debug) System.out.println(this+" has a Color of Suit " + suit);
				
				//Remove all other colors
				CardContainer tmp = new CardContainer(colors.size);
				for (Card c : colors) if (!c.getSuit().equals(suit))
					tmp.add(c);
				colors.removeAll(tmp, true);
				
				//Highest Card is Trump or is higher Color than all the available cards
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
		Card lowest = null;
		for (Card c : trumps) if (c.compareTo(highcard)>0){
			lowest = c;
			break;
		}
		
		return (lowest==null)? trumps.first() : lowest;
	}
	
	public void addPoints(int points){
		this.points += Math.abs(points);
	}
	
	public int getPoints(){ return points; }
	
	@Override
	public void reset(){
		super.reset();
		points=0;		
		partner = null;
		team = false;
	}
	
	@Override
	public String toString(){
		return "Player "+id;
	}

}
