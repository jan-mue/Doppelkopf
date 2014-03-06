package com.eg.cards;

import com.eg.cards.Card.CardSuit;
import com.eg.cards.Card.CardSymbol;

public class Simulation {
	private final Stack<Card> stack;
	private final Player<Card> player;
	private final CardContainer<Card> deck;
	
	public Simulation(Stack<Card> stack, Player<Card> player){
		this.stack = stack;
		this.player = player;
		deck =  new CardContainer<Card>(40);
		for (CardSuit suit : CardSuit.values())
			for (CardSymbol symbol : CardSymbol.values()){
				deck.add(new Card(suit, symbol));
				deck.add(new Card(suit, symbol));
			}
		deck.removeAll(stack.getPlayedCards(), true);
		deck.removeAll(stack, true);
		deck.removeAll(player, true);
	}

}
