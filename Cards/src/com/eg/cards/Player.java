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
	
	
	public boolean play(final Card card, final Stack stack) throws IllegalArgumentException{
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
	public boolean play(final Stack stack, Player best){
		return play(selectCard(stack, best), stack);
	}
	
	private Card selectCard(final Stack stack, Player best){
		CardContainer colors = getColors();
		CardContainer trumps = new CardContainer(this);
		trumps.removeAll(colors, true);
		
		colors.sort();
		trumps.sort();
		
		Card highcard = stack.getHighCard();
		
		//player leads
		if (highcard == null){
			if (colors.contains(CardSymbol.ACE)){
				CardContainer aces=colors.get(CardSymbol.ACE);
				for (int i=aces.size-1; i>=0; i--){
					Card c=aces.get(i);
					if(colors.count(c.getSuit())<(c.getSuit().equals(CardSuit.HEARTS)? 2 : 4) &&
							stack.getPlayedCards().count(c.getSuit())<3)
						return c;
				}
			}
			
			//no (fitting) ace found
			
			if (trumps.size!=0){
				//play highest card
				CardContainer tmp = trumps.get(CardSuit.HEARTS, CardSymbol.TEN);
				if (tmp.size==2 && stack.getPlayedCards().size>20) return tmp.first();
				
				//play trump with low point value
				if (trumps.contains(CardSymbol.KNAVE)){
					CardContainer knaves = trumps.get(CardSymbol.KNAVE);
					return knaves.random();
				}
				
				//no knave found
				if (trumps.contains(CardSymbol.QUEEN)){
					CardContainer queens = trumps.get(CardSymbol.QUEEN);
					return queens.first();
				}
			}
			
			//no (fitting) trump found
			
			if (colors.contains(CardSymbol.KING)){
				CardContainer kings = colors.get(CardSymbol.KING);
				return kings.first();
			}
			if (colors.size!=0) return colors.first();
		}
		
		//play Color
		if(!stack.isTrump()){
			
			CardSuit suit = stack.getSuit();
			if(colors.contains(suit)){
				
				if (CardGame.debug) System.out.println(this+" has a Color of Suit " + suit);
				
				//remove all other colors
				CardContainer tmp = new CardContainer(colors.size);
				for (Card c : colors) if (!c.getSuit().equals(suit))
					tmp.add(c);
				colors.removeAll(tmp, true);
				
				//highest card is trump or is higher color than all the available cards
				if(highcard.getTrumpValue()!=0 || highcard.compareTo(colors.peek())>=0){
					//same team?
					if (best == partner)
						//pick card with highest value
						return colors.peek();
					else
					//pick lowest possible card
					return colors.first();
				}
				else return colors.peek();
			
			}
			else{
				if (CardGame.debug) System.out.println(this+" has no Color of Suit " + suit);
				
				//play color only to help partner
				if (best == partner){
					
				}
			}
		}
		
		//play trump
		
		if (trumps.size==0){
			if (CardGame.debug) System.out.println(this+" has no trumps");
			return colors.first();
		}
		
		//find lowest fitting trump
		Card lowest = null;
		for (Card c : trumps) if (c.compareTo(highcard)>0){
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
