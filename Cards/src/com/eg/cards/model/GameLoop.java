package com.eg.cards.model;

import java.util.ArrayList;
import java.util.List;

import com.eg.cards.view.CardGame;
import com.eg.cards.view.GUI;

public class GameLoop {
	
	private final Deck deck;
	private final Stack stack;
	private final List<Player> players;
	
	private GUI gui;
	
	private int start;
	private boolean continued;
	
	public GameLoop(CardGame game){
		deck = new Deck(game);
		stack = new Stack(deck);		
		players =  new ArrayList<Player>(4);
		for (int i=0; i<4; i++) players.add(new Player(i, stack));
	}
	
	public int getStartPlayer(){ return (start==4)? 0 : start; }
	public List<Player> getPlayers(){ return new ArrayList<Player>(players); }
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
			players.get(0).play(card);
		} catch (IllegalArgumentException e){
			gui.message("Ungültige Karte");
			if (CardGame.DEBUG) System.out.println("Not valid!");
			return;
		}
		
		loop(1, start!=0 ? start : 4);
		
		gui.update();
		
		start = stack.getBestPlayer().id;
		if (CardGame.DEBUG) System.out.println("Start player: " + start);
		
		continued=false;
		
	}
	
	public void start(){
		stack.reset(); 
		
		start = 0;
		continued = true;
		deck.dealCards(players);
		
		gui.update();
	}
	
	private void nextTrick(){
		
		stack.next();
		
		if (players.get(0).size() == 0){
			if (CardGame.DEBUG) System.out.println("End of game");
		}else if (start!=0)
			loop(start, 4);
		
		gui.update();
		
		continued=true;
	}
	
	private void loop(final int start, final int end){
		for (int i=start; i<end; i++) players.get(i).play();
	}

}
