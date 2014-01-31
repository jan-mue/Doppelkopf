package com.eg.cards;

import com.badlogic.gdx.utils.Array;
import com.eg.cards.ui.CardGame;
import com.eg.cards.ui.GUI;

public class GameLoop {
	
	private final Deck deck;
	private final Stack stack;
	private final Array<Player> players;
	
	private GUI gui;
	
	private int start;
	private Player best;
	private boolean continued;
	
	public GameLoop(CardGame game){
		deck = new Deck(game);
		stack = new Stack();		
		players =  new Array<Player>(4);
		for (int i=0; i<4; i++) players.add(new Player(i));
	}
	
	public int getStartPlayer(){ return (start==4)? 0 : start; }
	public Array<Player> getPlayers(){ return new Array<Player>(players); }
	public CardContainer getStack(){ return new CardContainer(stack); }
	
	public void setGUI(GUI gui){
		if (gui != null) this.gui = gui;
	}
	
	public void playCard(Card card){
		if (!continued){
			nextTrick();
			return;
		}
		
		try{
		if (players.first().playCard(card, stack)){
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
		
		gui.update();
		
		start = best.id;
		if (CardGame.debug) System.out.println("Start player: " + start);
		
		continued=false;
		
	}
	
	public void nextTrick(){
		
		//Best player gets points of the trick
		best.addPoints(stack.count());
		
		stack.reset();
		
		if (players.first().size==0){
			if (CardGame.debug) System.out.println("End of game");
		}else if (start!=0)
			for (int i=start; i<4; i++) play(players.get(i));
		
		gui.update();
		
		continued=true;
	}
	
	public void start(){
		stack.reset(); 
		
		start = 4;
		best=players.first();
		continued=true;
		deck.dealCards(players);
		
		gui.update();
	}
	
	private void play(Player p){
		if (p.playCard(stack, best)){
			best = p;
			if (CardGame.debug) System.out.println(p+" played a higher card");
		}
	}

}
