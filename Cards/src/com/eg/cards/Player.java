package com.eg.cards;

import com.eg.cards.Card.CardSuit;
import com.eg.cards.Card.CardSymbol;
import com.eg.cards.ui.CardGame;

public class Player extends CardContainer{
	
	private short points=0;
	private final byte id;
	private boolean team;
	
	public Player(byte id){
		super(10);
		this.id=id;
		team = false;
	}
	
	public byte getID(){ return id; }
	public boolean isTeamRe(){ return team; }
	
	public boolean playCard(final Card card, final Stack stack) throws IllegalArgumentException{
		if (CardGame.debug) System.out.println(card + " played by " + this);
		
		if (!stack.check(card, this)) throw new IllegalArgumentException();
		
		removeCard(card);
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
			
			//no ace found
			
			if (trumps.size!=0)
				return trumps.random();
			
			return colors.first();
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
					if (best.isTeamRe()==team)
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
				if (best.isTeamRe()==team){
					
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
	
	public void addPoints(short points){
		this.points += Math.abs(points);
	}
	
	public short getPoints(){ return points; }
	
	@Override
	public boolean addCard(final Card card){
		if (card.getSymbol().equals(CardSymbol.QUEEN) && card.getSuit().equals(CardSuit.CLUBS))
			team = true;
		return super.addCard(card);
	}
	
	@Override
	public void reset(){
		super.reset();
		points=0;
	}
	
	@Override
	public String toString(){
		return "Player "+id;
	}

}
