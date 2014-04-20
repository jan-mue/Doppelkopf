package com.eg.cards.model;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.eg.cards.model.Card.CardSymbol;
import com.eg.cards.view.CardGame;

public class Deck extends CardContainer{
	
	private static final long serialVersionUID = -6832741093993975415L;
	private final TextureAtlas cards;

	public Deck(final CardGame game) {
		super(48);
		
		cards = game.getAssets().get("cards.atlas", TextureAtlas.class);
		
		for (int i=0; i<24; i++) {
			add(new Card(cards.getRegions().get(i)));
			add(new Card(cards.getRegions().get(i)));
		}
		
		if(!CardGame.NINES) removeAll(CardSymbol.NINE);
	}
	
	public void dealCards(List<Player> players){
		randomize();
		
		for (int i=0; i<players.size(); i++){
			Player p = players.get(i);
			p.reset();
			p.addAll(subList(i*(CardGame.NINES? 12 : 10), (i+1)*(CardGame.NINES? 12 : 10)));
			p.sort();
		}
	}

}
