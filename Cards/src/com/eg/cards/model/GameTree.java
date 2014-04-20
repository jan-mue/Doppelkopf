package com.eg.cards.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GameTree {
	
	private final Player player;
	private final Stack stack;
	private final Map<Card, float[]> hasCard;
	private CardContainer remains;
	
	private int depth;
	private int startPlayer;
	
	private class Node{
		private final Card data;
		private final CardContainer remainingCards;
		
		private int playerSum, trickSum, best, start;
		private Card[] trick;
		
		private float p, s;
		
		public Node(Card data, CardContainer remainingCards, int playerSum,
				int trickSum, int best, int start, Card[] trick){
			this.data = data;
			
			this.remainingCards = new CardContainer(remainingCards);
			this.remainingCards.remove(data, true);
			
			this.playerSum = playerSum;
			this.trickSum = trickSum;
			this.best = best;
			this.start = start;
			
			this.trick = Arrays.copyOf(trick, 4);
		}
		
		public Node(Card data, Node node){
			this(data, node.remainingCards, node.playerSum, node.trickSum,
					node.best, node.start, node.trick);
		}
		
		private void update(int depth){
			int currentPlayer = start + (depth - startPlayer) % 4;
			
			p = hasCard.get(data)[currentPlayer];
			
			trickSum += data.getValue();
			
			//add points to player if he played the highest card
			if ((depth - startPlayer) % 4 == 3){
				if (best==player.id) playerSum += trickSum;
				trickSum = 0;
				start = best;
			}
			
			//points of all children
			s = remainingCards.size()==0? playerSum : 0;
			
			for (Card c : remainingCards){
				Node node = new Node(c, this);
				s += node.expectedPoints();
			}
		}
		
		//recursive method to calculate the expected points for a path
		//should start with 0 player points and points of cards already played in the trick
		public float expectedPoints(){
			depth++;
			
			update(depth);
			
			depth--;
			return s*p;
		}
	}
	
	public GameTree(Player player, Stack stack){
		this.player = player;
		this.stack = stack;
		hasCard = new HashMap<Card, float[]>(48);
		
		depth = 0;
	}
	
	public Card getBestCard(CardContainer start){
		if(start.size() == 1) return start.first();
		//for (Card card : start) calcTree(card);
		return start.first();
	}
	
	public Card getBestCard(){
		return getBestCard(player);
	}
	
	private float calcTree(final Card card){
		remains = stack.getUnplayedCards();
		for (Card c : remains)
			hasCard.put(c, calcOdds(c));
		
		Card[] trick = new Card[4];
		for (int i=0; i<stack.size(); i++) trick[i]=stack.get(i);
		Node root = new Node(card, remains, 0, stack.getPoints(),
				stack.getBestPlayer().id, 0, trick);
		return root.expectedPoints();
	}
	
	private float[] calcOdds(Card c){
		float[] result = new float[4];
		if (player.contains(c)) result[0] = 1/player.size();
		else result[0] = 0;
		return result;
	}

}
