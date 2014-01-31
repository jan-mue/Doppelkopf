package com.eg.cards;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.eg.cards.Card.CardSymbol;
import com.eg.cards.ui.CardGame;

public class Deck extends CardContainer{
	
	private final TextureAtlas cards;

	public Deck(final CardGame game) {
		super(48);
		
		cards = game.getAssets().get("cards.atlas", TextureAtlas.class);
		
		for (int i=0; i<24; i++) {
			addCard(new Card(cards.getRegions().get(i)));
			addCard(new Card(cards.getRegions().get(i)));
		}
		CardContainer nines = get(CardSymbol.NINE);
		
		removeAll(nines, true);
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

}
