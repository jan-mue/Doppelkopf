package com.eg.cards.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.utils.Array;
import com.eg.cards.Deck;
import com.eg.cards.GameLoop;
import com.eg.cards.Player;
import com.eg.cards.PutEvent;
import com.eg.cards.PutListener;
import com.eg.cards.Stack;

public class GameScreen implements Screen {
	
	private final CardGame game;
	private GameLoop loop;
	private GUI gui;
	private Deck deck;
	private Stack stack;
	private Array<Player> players;
	
	public GameScreen(CardGame game) {
		
		this.game = game;
        
        deck = new Deck();
		stack = new Stack();
		
		players =  new Array<Player>(4);
		for (int i=0; i<4; i++) players.add(new Player(i));
		
		deck.dealCards(players);
        
        gui = new GUI(game);
		
		loop = new GameLoop(deck, stack, players, gui);
		
		gui.init(loop);
		gui.addListener(new PutListener(){
			public void put(PutEvent event, int id){
				loop.playCard(id);
			}
		});
        
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(242/255f, 242/255f, 242/255f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	}

	@Override
	public void show() {
		game.stage.addActor(gui);
	}

	@Override
	public void hide() {
		game.stage.clear();
	}
	
	@Override
	public void dispose() {
		gui.dispose();
		deck.dispose();
	}
}
