package com.eg.cards;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class Deck extends CardContainer implements Disposable{
	
	private final CardBuilder cardBuilder;

	public Deck() {
		super(40);
		
		cardBuilder = new CardBuilder();
		
		for (int i=0; i<40; i++) addCard(cardBuilder.createCard(i));
	}
	
	public void dealCards(Array<Player> players){
		Array<Card> deck = new Array<Card>(this);
		
		for (Player p : players){
			p.reset();
			if (deck.size==10)
				for (Card c : deck) p.addCard(c);
			else for (int i=0; i<10; i++){
				Card c = deck.random();
				deck.removeValue(c, true);
				p.addCard(c);
			}
			p.sort();
		}
	}

	@Override
	public void dispose() {
		cardBuilder.dispose();		
	}

}
