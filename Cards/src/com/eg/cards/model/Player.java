package com.eg.cards.model;

import com.eg.cards.model.Card.CardSuit;
import com.eg.cards.model.Card.CardSymbol;
import com.eg.cards.view.CardGame;

public class Player extends CardContainer{
	
	private static final long serialVersionUID = 2752820128599523366L;
	public final int id;
	private final GameTree tree;
	private final Stack stack;
	
	private int points;
	private Player partner;
	private boolean team;
	
	public Player(int id, Stack stack){
		super(10);
		this.id=id;
		this.stack = stack;
		reset();
		
		tree = new GameTree(this, stack);
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
		points = 0;		
		partner = null;
		team = false;
	}
	
	
	public void play(final Card card) throws IllegalArgumentException{
		if (CardGame.DEBUG) System.out.println(card + " played by " + this);
		
		//set team if club queen is played
		if (card.getSymbol().equals(CardSymbol.QUEEN) && card.getSuit().equals(CardSuit.CLUBS))
			team = true;
		
		if (!remove(card, true)) 
			throw new IllegalArgumentException("Could not remove card");
		
		try{
			stack.playCard(card, this);
		}
		catch (IllegalArgumentException e){
			add(card);
			throw e;
		}
	}
	
	//Overloaded for AI-play
	public void play(){
		play(selectCard(stack.getBestPlayer()));
	}
	
	private Card selectCard(Player best){
		CardContainer colors = getColors();
		CardContainer trumps = new CardContainer(this);
		trumps.removeAll(colors);
		
		colors.sort();
		trumps.sort();
		
		Card highcard = stack.getHighCard();
		
		//player leads
		if (highcard == null){
			
			if (colors.contains(CardSymbol.ACE)){
				CardContainer aces = colors.get(CardSymbol.ACE);
				CardContainer tmp = new CardContainer(aces);
				for (Card c: aces)
					if(colors.count(c.getSuit())>(c.getSuit().equals(CardSuit.HEARTS)? 1 : 3) ||
							stack.getPlayedCards().count(c.getSuit())>(c.getSuit().equals(CardSuit.HEARTS)? 0 : 2))
						tmp.add(c);
				aces.removeAll(tmp);
				if(aces.size()>0) return tree.getBestCard(aces);
			}
			
			//no (fitting) ace found
			
			if (trumps.size()!=0){
				//play highest card (dulle)
				CardContainer tmp = trumps.get(CardSuit.HEARTS, CardSymbol.TEN);
				if (tmp.size()==2 && stack.getPlayedCards().size()>20) return tmp.first();
				
				//play trump with low point value
				if (trumps.contains(CardSymbol.KNAVE))
					return tree.getBestCard(trumps.get(CardSymbol.KNAVE));
				
				//no knave found
				if (trumps.contains(CardSymbol.QUEEN))
					return tree.getBestCard(trumps.get(CardSymbol.QUEEN));
			}
			
			//no (fitting) trump found
			
			if (colors.contains(CardSymbol.KING)) return tree.getBestCard(colors.get(CardSymbol.KING));
			return tree.getBestCard(this);
		}
		
		//play Color
		if(!stack.isTrump()){
			
			CardSuit suit = stack.getSuit();
			if(colors.contains(suit)){
				
				if (CardGame.DEBUG) System.out.println(this+" has a Color of Suit " + suit);
				
				//remove all other colors
				CardContainer tmp = new CardContainer(colors.size());
				for (Card c : colors) if (!c.getSuit().equals(suit))
					tmp.add(c);
				colors.removeAll(tmp);
				
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
				if (CardGame.DEBUG) System.out.println(this+" has no Color of Suit " + suit);
				
				//play color only to help partner
				if (best == partner){
					
				}
			}
		}
		
		//play trump
		
		if (trumps.size()==0){
			if (CardGame.DEBUG) System.out.println(this+" has no trumps");
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
