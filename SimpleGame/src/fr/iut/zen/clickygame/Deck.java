package fr.iut.zen.clickygame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Deck {
	private final ArrayList<CardInterface> deck;
	private final String faction;

	public Deck(String faction) {
		this.deck = new ArrayList<>();
		this.faction = faction;
	}
	
	@Override
	public String toString() {
		int i = 1;
		String s = "Deck\n----\n";
		for (CardInterface card : deck) {
			s += i + ". " + card.toString() + "\n";
			i++;
		}
		return s;
	}
	// Test si le deck contient cette carte
	public boolean contains(CardInterface card) {
		return deck.contains(card);
	}
	// Ajoute la carte dans le deck si elle respecte les conditions
	public boolean add(CardInterface card) {
		if (!contains(card)) {
			if (card.getFaction() == faction || card.getFaction() == "Neutral") {
				deck.add(card);
				return true;
			}
		}
		return false;
	}
	public int getSize() {
		return deck.size();
	}
	// Retourne l'élément en position i
	public CardInterface get(int i) {
		return deck.get(i);
	}
	// Mélange le deck
	public void shuffle() {
		Collections.shuffle(deck);
	}
	// Création d'un deck depuis un fichier
	public static Deck makeDeckFromFile(String fileName) throws IOException {
		File fichier = new File(fileName);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(fichier));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line;
		Deck deck = new Deck("Neutral");
		while ((line = br.readLine()) != null) {
			CardInterface card = CardAbstract.makeCardFromLine(line);
			deck.add(card);
		}
		br.close();
		return deck;
	}
}