package fr.iut.zen.clickygame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class NeutralCard {
	private ArrayList<CardInterface> neutral;
	
	public NeutralCard() {
		neutral = new ArrayList<>();
	}
	
	@Override
	public String toString() {
		return neutral.toString();
	}
	
	public ArrayList<CardInterface> getNeutral() {
		return neutral;
	}
	
	public boolean contains(CardInterface card) {
		return neutral.contains(card);
	}
	
	public boolean add(CardInterface card) {
		if (!contains(card)) {
			neutral.add(card);
			return true;
		}
		return false;
	}
	
	// Créer toutes les cartes qui se trouvent dans un fichier
	public static NeutralCard makeCardFromFile(String fileName) throws IOException {
		File fichier = new File(fileName);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(fichier));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line;
		NeutralCard listNeutral = new NeutralCard();
		while ((line = br.readLine()) != null) {
			CardInterface card = CardAbstract.makeCardFromLine(line);
			listNeutral.add(card);
		}
		br.close();
		return listNeutral;
	}
}