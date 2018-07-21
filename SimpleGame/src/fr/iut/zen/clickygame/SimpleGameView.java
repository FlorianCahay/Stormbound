package fr.iut.zen.clickygame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.Map;

import fr.umlv.zen5.ApplicationContext;

public class SimpleGameView{
	private final int xOrigin;
	private final int yOrigin;
	private final int width;
	private final int length;
	private final int squareSize;

	private SimpleGameView(int xOrigin, int yOrigin, int length, int width, int squareSize) {
		this.xOrigin = xOrigin;
		this.yOrigin = yOrigin;
		this.length = length;
		this.width = width;
		this.squareSize = squareSize;
	}

	public static SimpleGameView initGameGraphics(int xOrigin, int yOrigin, int length, SimpleGameData data) {
		int squareSize = (int) (length * 1.0 / data.getNbLines()); // calcule la taille d'une case en divisant la longueur de plateau par le nb de lignes
		return new SimpleGameView(xOrigin, yOrigin, length, data.getNbColumns()*squareSize, squareSize); //créer l'affichage du plateau
	}

	private int indexFromReaCoord(float coord, int origin) { // attention, il manque des test de validité des coordonnées!
		float calcul = (coord - origin) / squareSize;
		if (calcul<0) {
			return (int) ((coord - origin)/squareSize)-1; // marche like a boss
		}
		return (int) (calcul);
	}

	/**
	 * Transforms a real y-coordinate into the index of the corresponding line.
	 * @param y a float y-coordinate
	 * @return the index of the corresponding line.
	 * @throws IllegalArgumentException if the float coordinate doesn't fit in the game board.
	 */
	public int lineFromY(float y) {
		return indexFromReaCoord(y, yOrigin);
	}
	
	/**
	 * Transforms a real x-coordinate into the index of the corresponding column.
	 * @param x a float x-coordinate
	 * @return the index of the corresponding column.
	 * @throws IllegalArgumentException if the float coordinate doesn't fit in the game board.
	 */
	public int columnFromX(float x) {
		return indexFromReaCoord(x, xOrigin);
	}

	private float realCoordFromIndex(int index, int origin) { // transforme coordonnées index en réelles
		return origin + index * squareSize;
	}

	private float xFromI(int i) {
		return realCoordFromIndex(i, xOrigin);
	}

	private float yFromJ(int j) {
		return realCoordFromIndex(j, yOrigin);
	}

	private RectangularShape drawCell(int i, int j) {
		return new Rectangle2D.Float(xFromI(j) + 2, yFromJ(i) + 2, squareSize - 4, squareSize - 4);
	}

	private RectangularShape drawSelectedCell(int i, int j) {
		return new Rectangle2D.Float(xFromI(j), yFromJ(i), squareSize, squareSize);
	}
	
	/**
	 * Draws the game board from its data, using an existing Graphics2D object.
	 * @param graphics a Graphics2D object provided by the default method {@code draw(ApplicationContext, GameData)}
	 * @param data the GameData containing the game data.
	 */
	private void draw(Graphics2D graphics, SimpleGameData data, CardInterface cardSelected) {
		graphics.setColor(Color.LIGHT_GRAY);
		graphics.fill(new Rectangle2D.Float(xOrigin, yOrigin, width*5, length*5));
		graphics.setColor(Color.DARK_GRAY);
		graphics.fill(new Rectangle2D.Float(xOrigin, yOrigin, width, length)); // fond du plateau
		graphics.fill(new Rectangle2D.Float(xOrigin+data.getNbColumns()*squareSize, yOrigin+2*squareSize, squareSize, squareSize)); // fond du bouton next turn
		graphics.fill(new Rectangle2D.Float(xFromI(5), yFromJ(5), squareSize*4, squareSize)); // fond de la main du joueur
		graphics.fill(new Rectangle2D.Float(xFromI(10), yFromJ(5), squareSize, squareSize)); // fond du bouton échanger

		graphics.setColor(Color.WHITE);// couleur des lignes et colonnes
		for (int i = 0; i <= data.getNbLines(); i++) { // affiche les lignes
			graphics.draw(
					new Line2D.Float(xOrigin, yOrigin + i * squareSize, xOrigin + width, yOrigin + i * squareSize));
		}

		for (int i = 0; i <= data.getNbColumns(); i++) { // affiche les colonnes
			graphics.draw(
					new Line2D.Float(xOrigin + i * squareSize, yOrigin, xOrigin + i * squareSize, yOrigin + length));
		}

		Coordinates c = data.getSelected();
		if (c != null) { // si une case est sélectionnée
			if (data.testCoordBoard(c) || data.testCoordNextTurn(c) || data.testCoordHand(c) || data.testCordDiscard(c)) {
				graphics.setColor(Color.WHITE); 
				graphics.fill(drawSelectedCell(c.getI(), c.getJ())); // remplir la case en blanc
			}
		}
		
		// Affichage les cases du plateau
		for (int i = 0; i < data.getNbLines(); i++) {
			for (int j = 0; j < data.getNbColumns(); j++) {
				graphics.setColor(Color.LIGHT_GRAY); // couleur des cases du plateau
				graphics.fill(drawCell(i, j));
				graphics.setColor(data.getCellColor(i, j));
				graphics.drawString(Integer.toString(data.getCellValue(i, j)), xFromI(j) + squareSize / 2,
						yFromJ(i) + +squareSize / 2); // écrit au mileu des cases la valeur
			}
		}
		
		// Chateau rouge
		graphics.setColor(new Color(255, 50, 50)); 
		graphics.fill(new Rectangle2D.Float(xOrigin, yOrigin-squareSize, width, squareSize));
		graphics.draw(new Line2D.Float(xOrigin, yOrigin + data.getRedLine() * squareSize+1, xOrigin + width, yOrigin + data.getRedLine() * squareSize+1)); // ligne de front rouge

		// Chateau bleu
		graphics.setColor(new Color(23, 64, 227)); 
		graphics.fill(new Rectangle2D.Float(xOrigin, yOrigin+data.getNbLines()*squareSize, width, squareSize));
		graphics.draw(new Line2D.Float(xOrigin, yOrigin + data.getBlueLine() * squareSize-1, xOrigin + width, yOrigin + data.getBlueLine() * squareSize-1)); // ligne de front bleue

				
		// Bouton tour suivant
		graphics.setColor(Color.ORANGE); 
		graphics.fill(drawCell(2, 4));
		graphics.setColor(Color.BLACK);
		graphics.drawString("Tour suivant", xFromI(4) + squareSize/4, yFromJ(2) + squareSize/2);
		
		// Bouton échanger carte
		graphics.setColor(Color.ORANGE);
		graphics.fill(drawCell(5, 10));
		graphics.setColor(Color.BLACK);
		graphics.drawString("Défausser carte", xFromI(10) + squareSize/6, yFromJ(5) + squareSize/2);
		
		
		// Affichage de la main du joueur
		int z = 5;
		for (CardInterface card : data.getHandBlue()) {
			graphics.setColor(Color.LIGHT_GRAY);
			graphics.fill(drawCell(5, z));
			graphics.setColor(Color.BLACK);
			if (card instanceof Unit) {
				graphics.drawString(card.getName(), xFromI(z) + squareSize / 10, yFromJ(5) + squareSize / 5);
				graphics.drawString(Integer.toString(((Unit) card).getStrength())+" force", xFromI(z) + squareSize / 10 ,
						yFromJ(5) + (squareSize / 5)*2);
				graphics.drawString(Integer.toString(((Unit) card).getMovement())+" mouvement", xFromI(z) + squareSize / 10 ,
						yFromJ(5) + (squareSize / 5)*3);
			}
			else if (card instanceof Spell) {
				graphics.setColor(Color.MAGENTA);
				graphics.drawString(card.getName(), xFromI(z) + squareSize / 10, yFromJ(5) + squareSize / 5);
				//graphics.drawString(((Spell) card).getEffect(), xFromI(z) + squareSize / 10 ,
				//		yFromJ(5) + (squareSize / 5)*2);
			}
			else if (card instanceof Structure) {
				graphics.setColor(Color.GREEN);
				graphics.drawString(card.getName(), xFromI(z) + squareSize / 10, yFromJ(5) + squareSize / 5);
				graphics.drawString(Integer.toString(((Structure) card).getStrength())+" force", xFromI(z) + squareSize / 10 ,
						yFromJ(5) + (squareSize / 5)*2);
			}
			
			graphics.drawString(Integer.toString(card.getMana())+" mana", xFromI(z) + squareSize / 10,
					yFromJ(5) + (squareSize / 5)*4);
			z += 1;
		}
		
		// Affichage carte sélectionnée
		graphics.setColor(Color.LIGHT_GRAY);
		graphics.fill(new Rectangle2D.Float(xOrigin+6*squareSize, yOrigin, squareSize*4, squareSize*2)); // emplacement carte zoom
		if (cardSelected!=null) {
			graphics.setColor(Color.BLACK);
			graphics.fill(new Rectangle2D.Float(xOrigin+6*squareSize, yOrigin, squareSize*4, squareSize*2));
			graphics.setColor(Color.WHITE);
			graphics.fill(new Rectangle2D.Float(xOrigin+6*squareSize+2, yOrigin+2, squareSize*4-4, squareSize*2-4));
			
			graphics.setColor(Color.BLACK);
			graphics.drawString("Nom : " + cardSelected.getName(), xOrigin+6*squareSize+10, yOrigin+(squareSize/4));
			graphics.drawString("Effect : " + cardSelected.getEffect(), xOrigin+6*squareSize+10, yOrigin+(squareSize/2));
			graphics.drawString("Mana : " + cardSelected.getMana(), xOrigin+6*squareSize+10, yOrigin+(squareSize-(squareSize/4)));
			if (cardSelected instanceof Unit) {
				graphics.drawString("Force : " + cardSelected.getStrength(), xOrigin+6*squareSize+10, yOrigin+squareSize);
				graphics.drawString("Mouvement : " + cardSelected.getMovement(), xOrigin+6*squareSize+10, yOrigin+(squareSize+squareSize/4));
			}
			else if (cardSelected instanceof Structure) {
				graphics.drawString("Force : " + cardSelected.getStrength(), xOrigin+6*squareSize+10, yOrigin+squareSize);
			}
		}
		
		// Affichage des points de vie
		Font myFont = new Font ("TimesRoman", 1, 20);
		graphics.setColor(Color.WHITE);
		graphics.setFont(myFont);
		graphics.drawString(Integer.toString(data.getHp1()), xOrigin+(width/2)-15, yOrigin-(squareSize/2));
		graphics.drawString(Integer.toString(data.getHp2()), xOrigin+(width/2)-15, (yOrigin+data.getNbLines()*squareSize)+(squareSize/2));
		
		graphics.drawString("Mana restant : " + Integer.toString(data.manaRed), xOrigin+(width/2)-squareSize/2, yOrigin-(squareSize/4));
		graphics.drawString("Mana restant : " + Integer.toString(data.manaBlue), xOrigin+(width/2)-squareSize/2, (yOrigin+data.getNbLines()*squareSize)+(squareSize/4));
		
	}
	
	/**
	 * Draws the game board from its data, using an existing {@code ApplicationContext}.
	 * @param context the {@code ApplicationContext} of the game
	 * @param data the GameData containing the game data.
	 * @param view the GameView on which to draw.
	 */
	public static void draw(ApplicationContext context, SimpleGameData data, SimpleGameView view, CardInterface card) {
		context.renderFrame(graphics -> view.draw(graphics, data, card)); // do not modify
	}
	
	/**
	 * Draws only the cell specified by the given coordinates in the game board from its data, using an existing Graphics2D object.
	 * @param graphics a Graphics2D object provided by the default method {@code draw(ApplicationContext, GameData)}
	 * @param data the GameData containing the game data.
	 * @param x the float x-coordinate of the cell.
	 * @param y the float y-coordinate of the cell.
	 */
	private void drawOnlyOneCell(Graphics2D graphics, SimpleGameData data, float x, float y) {
		graphics.setColor(Color.ORANGE);
		graphics.fill(drawCell(columnFromX(x), lineFromY(y)));
	}
	
	/**
	 * Draws only the cell specified by the given coordinates in the game board from its data, using an existing {@code ApplicationContext}.
	 * @param context the {@code ApplicationContext} of the game
	 * @param data the GameData containing the game data.
	 * @param view the GameView on which to draw.
	 * @param x the float x-coordinate of the cell.
	 * @param y the float y-coordinate of the cell.
	 */
	public static void drawOnlyOneCell(ApplicationContext context, SimpleGameData data, SimpleGameView view, float x, float y) {
		context.renderFrame(graphics -> view.drawOnlyOneCell(graphics, data, x, y)); // do not modify
	}
	
	public static void drawMenu(ApplicationContext context, SimpleGameData data, SimpleGameView view) {
		context.renderFrame(graphics -> view.drawMenu(graphics, data)); 
	}
	
	private void drawMenu(Graphics2D graphics, SimpleGameData data) {
		graphics.setColor(Color.BLACK);
		for (int i = 0; i <= 5; i++) { // affiche les lignes
			graphics.draw(
					new Line2D.Float(xOrigin, yOrigin + i * squareSize, xOrigin + width*3, yOrigin + i * squareSize));
		}

		for (int i = 0; i <= 12; i++) { // affiche les colonnes
			graphics.draw(
					new Line2D.Float(xOrigin + i * squareSize, yOrigin, xOrigin + i * squareSize, yOrigin + length));
		}
		graphics.setColor(Color.ORANGE);
		graphics.fill(new Rectangle2D.Float(xOrigin+3*squareSize, yOrigin, width+squareSize, squareSize));
		graphics.fill(new Rectangle2D.Float(xOrigin+3*squareSize, yOrigin+2*squareSize, 2*squareSize, squareSize*2));
		graphics.fill(new Rectangle2D.Float(xOrigin+6*squareSize, yOrigin+2*squareSize, 2*squareSize, squareSize*2));
		graphics.setColor(Color.BLACK);
		Font myFont = new Font ("TimesRoman", 1, 23);
		graphics.setFont(myFont);
		graphics.drawString("Un seul joueur", (xOrigin+3*squareSize)+squareSize/2,  yOrigin+3*squareSize);
		graphics.drawString("Construction de deck", (xOrigin+6*squareSize)+squareSize/5, yOrigin+3*squareSize);
		Font myFont2 = new Font ("TimesRoman", 1, 30);
		graphics.setFont(myFont2);
		graphics.drawString("Stormbound", (xOrigin+5*squareSize)-squareSize/10, yOrigin+squareSize/2);
	}
	
	public static void drawBeforeGame(ApplicationContext context, SimpleGameData data, SimpleGameView view, DeckConstruction listDeck) {
		context.renderFrame(graphics -> view.drawBeforeGame(graphics, data, listDeck)); // do not modify
	}
	
	private void drawBeforeGame(Graphics2D graphics, SimpleGameData data, DeckConstruction listDeck) {
		graphics.setColor(Color.LIGHT_GRAY);
		graphics.fill(new Rectangle2D.Float(xOrigin, yOrigin, width*5, length*5));
		graphics.setColor(Color.BLACK);
		for (int i = 0; i <= 5; i++) { // affiche les lignes
			graphics.draw(
					new Line2D.Float(xOrigin, yOrigin + i * squareSize, xOrigin + width*3, yOrigin + i * squareSize));
		}

		for (int i = 0; i <= 12; i++) { // affiche les colonnes
			graphics.draw(
					new Line2D.Float(xOrigin + i * squareSize, yOrigin, xOrigin + i * squareSize, yOrigin + length));
		}
		graphics.setColor(Color.ORANGE);
		graphics.fill(new Rectangle2D.Float(xOrigin+3*squareSize, yOrigin, width+squareSize, squareSize));
		graphics.setColor(Color.BLACK);
		Font myFont2 = new Font ("TimesRoman", 1, 30);
		graphics.setFont(myFont2);
		graphics.drawString("Choix du deck", (xOrigin+5*squareSize)-squareSize/10, yOrigin+squareSize/2);
		int i = 1;
		int j = 2;
		for (Map.Entry<String, Deck> entry : listDeck.getDeckList().entrySet()) {
			if (i == 11) {
				i = 1;
				j += 2;
			}
			graphics.setColor(Color.ORANGE);
			graphics.fill(new Rectangle2D.Float(xOrigin+i*squareSize, yOrigin+j*squareSize, squareSize, squareSize));
			graphics.setColor(Color.BLACK);
			Font myFont = new Font ("TimesRoman", 1, 15);
			graphics.setFont(myFont);
			graphics.drawString(entry.getKey(), (xOrigin+i*squareSize)+squareSize/5,  yOrigin+j*squareSize+(squareSize/2));
			i += 2;
		}
	}
	
	public static void drawDeckBuilding(ApplicationContext context, SimpleGameData data, SimpleGameView view, NeutralCard nC) {
		context.renderFrame(graphics -> view.drawDeckBuilding(graphics, data, nC));
	}
	
	private void drawDeckBuilding(Graphics2D graphics, SimpleGameData data, NeutralCard nC) {
		graphics.setColor(Color.LIGHT_GRAY);
		graphics.fill(new Rectangle2D.Float(xOrigin, yOrigin, width*5, length*5));
		graphics.setColor(Color.BLACK);
		for (int i = 0; i <= 5; i++) { // affiche les lignes
			graphics.draw(
					new Line2D.Float(xOrigin, yOrigin + i * squareSize, xOrigin + width*3, yOrigin + i * squareSize));
		}

		for (int i = 0; i <= 12; i++) { // affiche les colonnes
			graphics.draw(
					new Line2D.Float(xOrigin + i * squareSize, yOrigin, xOrigin + i * squareSize, yOrigin + length));
		}
		int i = 0;
		int j = 0;
		int k = i;
		for (CardInterface card : nC.getNeutral()) {
			if (i == 8 || i == 7) {
				if (j == 4 && i == 8) {
					i = 1;
					j = 1;
					k++;
				}
				else {
					i = k;
					j += 2;
				}
			}		
			graphics.setColor(Color.ORANGE);
			graphics.fill(new Rectangle2D.Float(xOrigin+i*squareSize, yOrigin+j*squareSize, squareSize, squareSize));
			graphics.setColor(Color.BLACK);
			Font myFont = new Font ("TimesRoman", 1, 13);
			graphics.setFont(myFont);
			graphics.drawString(card.getName(), (xOrigin+i*squareSize)+squareSize/6,  yOrigin+j*squareSize+(squareSize/2));
			i += 2;
		}
		
	}
}

















