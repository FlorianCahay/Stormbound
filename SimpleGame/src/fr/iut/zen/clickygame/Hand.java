package fr.iut.zen.clickygame;

import java.util.ArrayList;

public class Hand {
	private final ArrayList<CardInterface> hand;
	private int nbCards;
	private int nbCardsPick = 0;
	
	public Hand() {
		hand = new ArrayList<CardInterface>();
		nbCards = 0;
	}
	
	@Override
	public String toString() {
		int i = 1;
		String s = "";
		for (CardInterface card : hand) {
			s += i + ". " + card.toString() + "\n";
			i++;
		}
		return s;
	}
	
	public boolean contains(CardInterface card) {
		return hand.contains(card);
	}
	
	// Remplie la main avec des cartes du deck
	public void fillHand(Deck deck) {
		while (!fullHand()) {
			if (nbCardsPick==deck.getSize()) {
				nbCardsPick = 0;
				deck.shuffle();
			}
			if (contains(deck.get(nbCardsPick))) {
				nbCardsPick++;
			}
			else {
				hand.add(deck.get(nbCardsPick));
				nbCards++;
				nbCardsPick++;
			}
		}
	}
	
	// Permet d'échanger une carte de la main 1 fois par tour
	public boolean discard(CardInterface card, Deck deck) {
		int pos = getPosition(card);
		if (remove(pos)) {
			if (nbCardsPick==deck.getSize()) {
				nbCardsPick = 0;
				deck.shuffle();
			}
			while (contains(deck.get(nbCardsPick))) {
				nbCardsPick++;
			}
			hand.add(deck.get(nbCardsPick));
			nbCards++;
			nbCardsPick++;

			return true;
		}
		return false;
	}
	
	public int getNumber() {
		return hand.size();
	}

	public boolean remove(int i) {
		if (nbCards>0) {
			hand.remove(i);
			nbCards--;
			return true;
		}
		return false;
	}
		
	// Vide la main
	public void emptyHand() {
		while (nbCards != 0) {
			hand.remove(0);
			nbCards--;
		}
	}
	
	// Test si la main est remplie
	public boolean fullHand() {
		if (nbCards == 4) {
			return true;
		}
		return false;
	}
	
	public ArrayList<CardInterface> getHand() {
		return hand;
	}
	
	public int getPosition(CardInterface card) {
		int i = 0;
		for (CardInterface card2 : hand) {
			if (card.equals(card2)) {
				return i;
			}
			i++;
		}
		return i;
	}
	
	public CardInterface get(int i) {
		return hand.get(i);
	}
}