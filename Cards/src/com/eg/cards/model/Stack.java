package com.eg.cards.model;

import com.eg.cards.model.Card.CardSuit;
import com.eg.cards.view.CardGame;

public class Stack extends CardContainer{
	
	private static final long serialVersionUID = 3691974013443648149L;
	private Card highcard;
	private Player best;
	private final CardContainer played;
	private final CardContainer unplayed;
	
	public Stack(Deck deck){
		super(4);
		played = new CardContainer(deck.size());
		unplayed = new CardContainer(deck);
	}
	
	public boolean isTrump(){
		if (size()==0) return false;
		return first().getTrumpValue()!=0;
	}
	
	public CardSuit getSuit(){
		if (size()==0) return null;
		return first().getSuit();
	}
	
	public CardContainer getPlayedCards() { return played; }
	public CardContainer getUnplayedCards() { return unplayed; }
	
	public Player getBestPlayer(){ return best; }
	public Card getHighCard(){ return highcard; }
	
	private boolean check(Card c, Player p){
		return size()==0 ||
				(isTrump() && (c.getTrumpValue()>0 || !p.containsTrump()))
				|| (!isTrump() && ((c.getTrumpValue()==0 && getSuit().equals(c.getSuit()))
						|| !p.getColors().contains(getSuit())));
	}
	
	public void next(){
		best.addPoints(getPoints());
		reset();
	}
	
	
	public void playCard(final Card card, Player player) throws IllegalArgumentException{
		if(!check(card, player)) throw new IllegalArgumentException("Illegal Card");
		if(!super.add(card)) throw new IllegalArgumentException("Container is full");
		
		played.add(card);
		unplayed.remove(card);
		
		if (highcard == null || (((card.getTrumpValue()>0) || //trump
				(!isTrump() && card.getSuit().equals(first().getSuit()))) //same color
				&& card.compareTo(highcard)>0)){
			highcard = card;
			best = player;
			if (CardGame.DEBUG) System.out.println(player+" played a higher card");
		}
	}
	
	@Override
	public void reset(){
		super.reset();
		highcard = null;
	}

}
