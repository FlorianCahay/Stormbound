package fr.iut.zen.clickygame;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class DeckConstruction {
	private LinkedHashMap<String, Deck> deckList;
	
	public DeckConstruction() {
		deckList = new LinkedHashMap<String, Deck>();
	}
	
	public LinkedHashMap<String, Deck> getDeckList() {
		return deckList;
	}
	public Set<String> keySet() {
		return deckList.keySet();
	}
	public Deck get(Object key) {
		return deckList.get(key);
	}
	@Override
	public String toString() {
		String s = "Liste des deck\n----\n";
		for (Map.Entry<String, Deck> entry : deckList.entrySet()) {
			s += entry.getKey();
			s += entry.getValue().toString() + "\n";
		}
		return s;
	}
	public boolean contains(String name) {
		return deckList.containsKey(name);
	}
	public int size() {
		return deckList.size();
	}
	public boolean add(String name, Deck deck) {
		if (!contains(name)) {
			deckList.put(name, deck);
			return true;
		}
		return false;
	}
	// Construit une liste contenant tous les deck déjà enregistrés dans le répertoire courant
	public static DeckConstruction makeDeckList() throws IOException {
		DeckConstruction listD = new DeckConstruction();
		// Permet de trier les fichiers du répertoire courant et de garder seulement les deck
		DirectoryStream<Path> f = Files.newDirectoryStream(Paths.get("."), path -> path.toString().startsWith(".\\Deck"));
		for (Path path2 : f) {
			String s = path2.toString().replace(".\\", "");
			Deck d = Deck.makeDeckFromFile(s);
			s = s.replace(".txt", "");
			listD.add(s, d);
		}
		return listD;
	}
}