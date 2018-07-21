package fr.iut.zen.clickygame;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.umlv.zen5.Application;
import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.Event;
import fr.umlv.zen5.ScreenInfo;
import fr.umlv.zen5.Event.Action;

public class SimpleGameController {
	
	// Affiche le menu et g�re les clics
	static Deck menu(SimpleGameData data, SimpleGameView view, ApplicationContext context) {
		Point2D.Float location;
		SimpleGameView.drawMenu(context, data, view);
		DeckConstruction z = null;
		// Cr�ation de tous les decks d�j� enregistr�s dans le r�pertoire courant
		try {
			z = DeckConstruction.makeDeckList();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		while (true) {
			Event event = context.pollOrWaitEvent(10);
			if (event == null) {
				continue;
			}
			
			Action action = event.getAction(); 
			if (action == Action.KEY_PRESSED || action == Action.KEY_RELEASED) { 
				System.out.println("Fermeture du jeu !");
				context.exit(0); 
				return null;
			}
			
			if (action != Action.POINTER_DOWN) {
				continue;
			}

			if (!data.hasASelectedCell()) { 
				location = event.getLocation(); 
				data.selectCell(view.lineFromY(location.y), view.columnFromX(location.x));
				
				Coordinates c = data.getSelected();
				System.out.println(c);
				List<Integer> x1 = new ArrayList<Integer>();
				List<Integer> y1 = new ArrayList<Integer>();
				List<Integer> y2 = new ArrayList<Integer>();
				x1.add(2);
				x1.add(3);
				y1.add(3);
				y1.add(4);
				y2.add(6);
				y2.add(7);
				if (x1.contains(c.getI()) && y1.contains(c.getJ())) {
					Deck deckChoice = beforeGame(data, view, context, z);
					return deckChoice;
				}
				else if (x1.contains(c.getI()) && y2.contains(c.getJ())) {
					buildDeck(data, view, context);
					return null;
				}
			}
			else { 
				data.unselect(); 
			}
		}
	}
	
	// Retourne le deck choisie
	static Deck beforeGame(SimpleGameData data, SimpleGameView view, ApplicationContext context, DeckConstruction z) {
		Point2D.Float location;
		SimpleGameView.drawBeforeGame(context, data, view, z);
		
		while (true) {
			Event event = context.pollOrWaitEvent(10);
			if (event == null) {
				continue;
			}
			
			Action action = event.getAction(); 
			if (action == Action.KEY_PRESSED || action == Action.KEY_RELEASED) { 
				System.out.println("Fermeture du jeu !");
				context.exit(0); 
				return null;
			}
			
			if (action != Action.POINTER_DOWN) {
				continue;
			}

			if (!data.hasASelectedCell()) { 
				location = event.getLocation(); 
				data.selectCell(view.lineFromY(location.y), view.columnFromX(location.x));
				
				Coordinates c = data.getSelected();
				// En fonction de la position du clic s�lectionne le deck correspondant
				if (c.getI() == 2 || c.getI() == 4) { // Sur la 1�re ou 2�me ligne
					int x = 0;
					if (c.getI() == 4) {
						x += 5;	
					}
					switch (c.getJ()) {
					case 1:
						if (x < z.size()) {
							break;
						}
					case 3:
						if (x+1 < z.size()) {
							x++;
							break;
						}
					case 5:
						if (x+2 < z.size()) {
							x += 2;
							break;
						}
					case 7:
						if (x+3 < z.size()) {
							x += 3;
							break;
						}
					case 9:
						if (x+4 < z.size()) {
							x += 4;
							break;
						}
					default:
						continue;
					}
					// Retourne le deck en position x
					return z.get( (z.keySet().toArray())[ x ] );
				}
				
			}
			else { 
				data.unselect(); 
			}
		}
		
	}
	
	static Deck buildDeck(SimpleGameData data, SimpleGameView view, ApplicationContext context) {
		
		NeutralCard nC = null;
		Point2D.Float location;
		// Cr�ation de toutes les cartes neutres
		try {
			System.out.println("test");
			nC = NeutralCard.makeCardFromFile("Neutral_cards.txt");
			System.out.println("test 2");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		SimpleGameView.drawDeckBuilding(context, data, view, nC );
		
		while (true) {
			Event event = context.pollOrWaitEvent(10);
			if (event == null) {
				continue;
			}
			
			Action action = event.getAction(); 
			if (action == Action.KEY_PRESSED || action == Action.KEY_RELEASED) { 
				System.out.println("Fermeture du jeu !");
				context.exit(0); 
				return null;
			}
			
			if (action != Action.POINTER_DOWN) {
				continue;
			}

			if (!data.hasASelectedCell()) { 
				location = event.getLocation(); 
				data.selectCell(view.lineFromY(location.y), view.columnFromX(location.x));

			}
			else { 
				data.unselect(); 
			}
		}
	}

	static void simpleGame(ApplicationContext context) {
		System.out.println("Lancement du jeu !");
		System.out.println("Initialisation...");
		ScreenInfo screenInfo = context.getScreenInfo(); //renvoie les infos de l'�cran
		float width = screenInfo.getWidth(); // largeur de l'�cran
		float height = screenInfo.getHeight(); // hauteur de l'�cran
		System.out.println("size of the screen (" + width + " x " + height + ")");

		SimpleGameData data = new SimpleGameData(5,4); // cr�er une matrice de 5 lignes et 4 colonnes
		data.setEmptyMatrix(); // initialise une matrice vide
		SimpleGameView view = SimpleGameView.initGameGraphics(200, (int)height/5, (int)width/3, data); // initialise la position du plateau sur l'�cran

		Point2D.Float location;
				
		// S�l�ction du deck pour la partie
		Deck deckChoice = menu(data, view,context);
		data.selectDeck(deckChoice);	
			
		// Initialisation des variables
		int x = 1;
		Color[] colors = { Color.RED, Color.BLUE};
		Color playerTurn = colors[x];
		CardInterface card = null;
		int nbDiscard = 0;
		SimpleGameView.draw(context, data, view, card); // affiche le plateau � l'�cran

		while (true) {
			
			if (data.getHp1()<=0 || data.getHp2()<=0) {
				System.out.println("Fin du jeu, un joueur a perdu");
				context.exit(0);
				return;
			}
			
			if (playerTurn == Color.RED) { // si c'est au tour de l'ordinateur (rouge)
				System.out.println("\nTour de l'ordinateur (rouge)");
				data.playARandomCard();
				
				x += 1;
				x %= 2;
				playerTurn = colors[x];
				data.updateBoard(playerTurn);
				data.updateFrontLine(Color.RED);
				data.updateFrontLine(Color.BLUE);
				
				try {
					Thread.sleep(1000); // on patiente 1 seconde
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				
				// On pr�pare les variables pour le joueur suivant
				data.dataFillHand(Color.BLUE);
				data.nbTurn++;
				data.manaBlue = 3+data.nbTurn;
				nbDiscard = 0;
				
				System.out.println("Fin du tour (rouge)\n");
			}
			
			else { // si c'est au tour du joueur humain (bleu)
				Event event = context.pollOrWaitEvent(10); // attend qu'un �v�nement se produise
				if (event == null) { // si pas d'�v�nement recommence
					continue;
				}
				
				Action action = event.getAction(); //retourne l'action faite par l'utilisateur
				if (action == Action.KEY_PRESSED || action == Action.KEY_RELEASED) { //si une touche est appuy�e ou relach�e
					System.out.println("Fermeture du jeu !");
					context.exit(0); // ferme l'application correctement
					return;
				}
				
				if (action != Action.POINTER_DOWN) { // si il n'y a pas eu de clic
					continue; // on recommence
				}
	
				if (!data.hasASelectedCell()) { // si une case est s�lectionn�e
					location = event.getLocation(); // retourne la position du curseur sur l'�cran !!! attention aucune vérfifcation des coordonnées!!!
					data.selectCell(view.lineFromY(location.y), view.columnFromX(location.x)); // s�lectionne la case(i,j); transforme la position du curseur en ligne et colonne
					
					Coordinates c = data.getSelected();
					if (c != null) { // si la case s�lectionn�e n'est pas null
						if (data.testCoordBoard(c) && card!=null) { // si la case s�lectionn�e est dans le board et qu'une carte a d�j� �t� s�lectionn�e
							if (data.playACard(playerTurn, card)) {
								data.manaBlue -= card.getMana();
							}
							card = null; // d�s�lectionne la carte
						}
						else if (data.testCoordNextTurn(c)) { // si le joueur � cliqu� sur la case pour passer son tour
							x += 1;
							x %= 2;
							playerTurn = colors[x];
							data.updateBoard(playerTurn);
							data.dataFillHand(Color.RED);
							data.manaRed++;
							System.out.println("Fin du tour (bleu)");
						}
						else if (data.testCoordHand(c)) { // si le joueur a cliqu� sur une de ses cartes
							card = data.selectCard();
						}
						else if (data.testCordDiscard(c) && card!=null && nbDiscard<1) { // si le joueur � cliqu� pour �changer une carte et qu'il a le droit
							if (data.dataDiscard(card)) {
								nbDiscard++;
							}
						}
					}
					data.updateFrontLine(Color.RED);
					data.updateFrontLine(Color.BLUE);
					
				}else { 
					data.unselect(); //d�s�lectionne la case d�j� s�lectionn�e
				}
			}

			SimpleGameView.draw(context, data, view, card); // affiche la plateau � l'�cran apr�s les modifications
			
		}
		
	}

	public static void main(String[] args) {
		// pour changer de jeu, remplacer stupidGame par le nom de la m�thode de jeu (elle doit avoir extaement la m�me en-t�te).
		Application.run(Color.LIGHT_GRAY, SimpleGameController::simpleGame); // attention, utilisation d'une lambda.
	}
}
