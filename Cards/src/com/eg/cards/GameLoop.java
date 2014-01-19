package com.eg.cards;

import com.badlogic.gdx.utils.Array;
import com.eg.cards.ui.CardGame;
import com.eg.cards.ui.GUI;
import com.eg.cards.ui.UpdateEvent;

public class GameLoop{
	
	private final Deck deck;
	private final Stack stack;
	private final Array<Player> players;
	
	private final GUI gui;
	
	private byte start = 4;
	private Player best;
	private boolean continued;
	
	public GameLoop(Deck deck, Stack stack, Array<Player> players, GUI gui){
		super();
		this.deck=deck;
		this.stack=stack;
		this.players=players;
		this.gui=gui;
		
		best=players.first();
		continued=true;
	}
	
	public byte getStartPlayer(){ return (start==4)? 0 : start; }
	public Array<Player> getPlayers(){ return new Array<Player>(players); }
	public CardContainer getStack(){ return new CardContainer(stack); }
	
	public void playCard(byte id){
		if (!continued){
			nextTrick();
			return;
		}
		
		try{
		if (players.first().playCard(deck.get(id), stack)){
			best = players.first();
			if (CardGame.debug) System.out.println("I played a higher card");
		}
		} catch (IllegalArgumentException e){
			gui.message("Ungültige Karte");
			if (CardGame.debug) System.out.println("Not valid!");
			return;
		}
		
		if (start==0) start=4;
		for (int i=1; i<start; i++) play(players.get(i));
		
		gui.updateTabs();
		
		start = best.getID();
		if (CardGame.debug) System.out.println("Start player: " + start);
		
		continued=false;
		
		gui.updateCards();
	}
	
	public void nextTrick(){
		
		//Best player gets points of the trick
		best.addPoints(stack.count());
		
		short[] points = new short[players.size];
		for(int i=0; i<players.size; i++) points[i]=players.get(i).getPoints();
		gui.fire(new UpdateEvent(points));
		
		stack.reset();
		
		if (players.first().size==0){
			if (CardGame.debug) System.out.println("End of game");
		}else if (start!=0)
			for (int i=start; i<4; i++) play(players.get(i));
		
		gui.updateCards();
		gui.updateTabs();
		
		continued=true;
	}
	
	private void play(Player p){
		if (p.playCard(stack, best)){
			best = p;
			if (CardGame.debug) System.out.println(p+" played a higher card");
		}
	}

}
