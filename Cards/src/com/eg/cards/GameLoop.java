package com.eg.cards;

import com.badlogic.gdx.utils.Array;
import com.eg.cards.ui.CardGame;
import com.eg.cards.ui.GUI;

public class GameLoop {
	
	private final Deck deck;
	private final Stack<ImageCard> stack;
	private final Array<Player<ImageCard>> players;
	
	private GUI gui;
	
	private int start;
	private Player<ImageCard> best;
	private boolean continued;
	
	public GameLoop(CardGame game){
		deck = new Deck(game);
		stack = new Stack<ImageCard>();		
		players =  new Array<Player<ImageCard>>(4);
		for (int i=0; i<4; i++) players.add(new Player<ImageCard>(i));
	}
	
	public int getStartPlayer(){ return (start==4)? 0 : start; }
	public Array<Player<ImageCard>> getPlayers(){ return new Array<Player<ImageCard>>(players); }
	public CardContainer<ImageCard> getStack(){ return new CardContainer<ImageCard>(stack); }
	
	public void setGUI(GUI gui){
		if (gui != null) this.gui = gui;
	}
	
	public void input(ImageCard card){
		if (!continued){
			nextTrick();
			return;
		}
		
		try{
		if (players.first().play(card, stack)){
			best = players.first();
			if (CardGame.debug) System.out.println("I played a higher card");
		}
		} catch (IllegalArgumentException e){
			gui.message("Ungültige Karte");
			if (CardGame.debug) System.out.println("Not valid!");
			return;
		}
		
		loop(1, start!=0 ? start : 4);
		
		gui.update();
		
		start = best.id;
		if (CardGame.debug) System.out.println("Start player: " + start);
		
		continued=false;
		
	}
	
	public void start(){
		stack.reset(); 
		
		start = 0;
		best=players.first();
		continued=true;
		deck.dealCards(players);
		
		gui.update();
	}
	
	private void nextTrick(){
		
		//Best player gets points of the trick
		best.addPoints(stack.count());
		
		stack.reset();
		
		if (players.first().size==0){
			if (CardGame.debug) System.out.println("End of game");
		}else if (start!=0)
			loop(start, 4);
		
		gui.update();
		
		continued=true;
	}
	
	private void loop(final int start, final int end){
		for (int i=start; i<end; i++){
			if (players.get(i).play(stack, best)){
				best = players.get(i);
				if (CardGame.debug) System.out.println(players.get(i)+" played a higher card");
			}
		}
	}

}
