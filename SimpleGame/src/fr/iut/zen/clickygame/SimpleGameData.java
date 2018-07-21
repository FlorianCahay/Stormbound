package fr.iut.zen.clickygame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;


public class SimpleGameData{
	private final Cell[][] matrix;
	private Coordinates selected;
	private int hp1 = 10; // bleu
	private int hp2 = 10; // rouge
	private FrontLine frontLine = new FrontLine();
	private Deck deck = new Deck("Neutral");
	private Hand handRed = new Hand();
	private Hand handBlue = new Hand();
	int manaBlue = 3;
	int manaRed = 3;
	int nbTurn = 0;

	public SimpleGameData(int nbLines, int nbColumns) {
		matrix = new Cell[nbLines][nbColumns];
	}
	
	public int getHp1() {
		return hp1;
	}
	public int getHp2() {
		return hp2;
	}
	
	public void dataFillHand(Color c) {
		if (c == Color.RED) {
			handRed.fillHand(deck);
		}
		else {
			handBlue.fillHand(deck);
		}
	}
	
	public ArrayList<CardInterface> getHandBlue() {
		return handBlue.getHand();
	}
	
	public ArrayList<CardInterface> getHandRed() {
		return handRed.getHand();
	}
	
	public int getRedLine() {
		return frontLine.getRed();
	}
	public int getBlueLine() {
		return frontLine.getBlue();
	}
	
	public void selectDeck(Deck d) {
		deck = d;
		deck.shuffle();
		handRed.fillHand(deck);
		handBlue.fillHand(deck);
	}
	
	public boolean dataDiscard(CardInterface card) {
		if (handBlue.discard(card, deck)) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * The number of lines in the matrix contained in this GameData.
	 * @return the number of lines in the matrix.
	 */
	public int getNbLines() {
		return matrix.length;
	}
	
	/**
	 * The number of columns in the matrix contained in this GameData.
	 * @return the number of columns in the matrix.
	 */
	public int getNbColumns() {
		return matrix[0].length;
	}
	/**
	 * The color of the cell specified by its coordinates.
	 * @param i the first coordinate of the cell.
	 * @param j the second coordinate of the cell.
	 * @return the color of the cell specified by its coordinates
	 */
	public Color getCellColor(int i, int j) {
		return matrix[i][j].getColor();
	}
	/**
	 * The value of the cell specified by its coordinates.
	 * @param i the first coordinate of the cell.
	 * @param j the second coordinate of the cell.
	 * @return the value of the cell specified by its coordinates
	 */
	public int getCellValue(int i, int j) {
		return matrix[i][j].getValue();
	}
	/**
	 * The coordinates of the cell selected, if a cell is selected.
	 * @return the coordinates of the selected cell; null otherwise.
	 */
	public Coordinates getSelected() {
		return selected;
	}
	/**
	 * Tests if at least one cell is selected.
	 * @return true if and only if at least one cell is selected; false otherwise.
	 */
	public boolean hasASelectedCell() {
		return selected != null;
	}
	/**
	 * Selects, as the first cell, the one identified by the specified coordinates.
	 * @param i the first coordinate of the cell.
	 * @param j the second coordinate of the cell.
	 * @throws IllegalStateException if a first cell is already selected.
	 */
	public void selectCell(int i, int j) {
		if (selected != null) {
			throw new IllegalStateException("First cell already selected");
		}
		selected = new Coordinates(i, j);
	}
	/**
	 * Unselects both the first and the second cell (whether they were selected or not).
	 */
	public void unselect() {
		selected = null;
	}

	public void setRandomMatrix() { // initialise une matrice aléatoire
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				matrix[i][j] = Cell.randomGameCell();
			}
		}
	}

	public void setEmptyMatrix() { // initialise une matrice vide
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				matrix[i][j] = Cell.emptyGameCell();
			}
		}
	}
	/**
	 * Updates the data contained in the GameData.
	 */
	public void updateData() {
		// update (attention traitement different si des cases sont
		// selectionnées ou non...)
	}
		
	// on pose une carte aléatoire pour le joueur IA (rouge)
	public boolean playARandomCard() {
		System.out.println("Création d'une unité aléatoire");
		Random r = new Random();
		int valeurI;
		int valeurJ;
		// position aléatoire sur le board
		do {
			do {
				valeurI = r.nextInt(5);
			} while (valeurI>frontLine.getRed()-1);
			valeurJ = r.nextInt(4);
		} while (!matrix[valeurI][valeurJ].isEmpty());

		// joue une carte si on a assez de mana
		for (CardInterface card : getHandRed()) {
			if (haveEnoughMana(card, Color.RED) && !(card instanceof Spell)) {
				if (card instanceof Unit) {
					useEffect(valeurI, valeurJ, (Unit)card, Color.RED);
				}
				matrix[valeurI][valeurJ] = new Cell(0, card.getStrength(), 1);
				move(card.getStrength(), Color.RED, valeurI, valeurJ, card.getMovement(), card);
				handRed.remove(handRed.getPosition(card));
				manaRed -= card.getMana();
				return true;
			}
		}
		return false;	
	}
	
	// renvoie la carte de la main que le joueur a séléctionné
	public CardInterface selectCard() {
		CardInterface cardSelected = handBlue.get(selected.getJ()-5);
		return cardSelected;
	}
	
	// renvoie true si le joueur a assez de mana pour jouer la carte
	public boolean haveEnoughMana(CardInterface card, Color col) {
		int manaNeeded = card.getMana();
		if (col == Color.RED) {
			if (manaNeeded <= manaRed) {
				return true;
			}
		}
		else if (col == Color.BLUE) {
			if (manaNeeded <= manaBlue) {
				return true;
			}
		}
		return false;
	}
	
	// on pose une carte sur une case disponible
	public boolean playACard(Color col, CardInterface card) {
		int i = selected.getI();
		int j = selected.getJ();
		
		if (haveEnoughMana(card, col)) {
		
			int typeCard = 1;
			if (card instanceof Structure) {
				typeCard = 2;
			}
			
			if (card instanceof Spell) {
				System.out.println("Utilisation d'un sort");
				if (useSpell(i, j, (Spell)card, col)) {
					handBlue.remove(handBlue.getPosition(card));
					return true;
				}
				
			}
			else {
				if (matrix[i][j].isEmpty()) { // si case disoinible
					if (col == Color.BLUE) {
						if (i>=frontLine.getBlue() && matrix[i][j].getColor() == Color.WHITE) { // si le clic est derrière la frontline et que la case est vide
							if (card instanceof Unit) {
								useEffect(i, j, (Unit)card, col);
							}
							matrix[i][j] = new Cell(1, card.getStrength(), typeCard);
							move(card.getStrength(), Color.BLUE, i, j, card.getMovement(), card);
							handBlue.remove(handBlue.getPosition(card));
						}
					}
					else if (col == Color.RED) {
						if (i<frontLine.getRed() && matrix[i][j].getColor() == Color.WHITE) {
							if (card instanceof Unit) {
								useEffect(i, j, (Unit)card, col);
							}
							matrix[i][j] = new Cell(0, card.getStrength(), typeCard);
							move(card.getStrength(), Color.RED, i, j, card.getMovement(), card);
							handRed.remove(handRed.getPosition(card));
						}
					}
					return true;
				}
			}
		}
		return false;
	}
	
	public void useEffect(int i, int j, Unit unit, Color col) {
		if (unit.getEffect() == Effect.three.getEquivalent()) {
			// Regarde autour de la case de l'unité
			for (int k = -1; k < 2; k++) {
				for (int l = -1; l < 2; l++) {
					// Si la case testée est dans le board
					if ((i+k >= 0 && i+k < getNbLines()) && (j+l >= 0 && j+l < getNbColumns())) {
						//System.out.println("i="+i+" j="+j+" k="+k+" l="+l+" i+k="+(i+k)+" j+l="+(j+l));
						if (matrix[i+k][j+l].getColor() != col && matrix[i+k][j+l].getColor() != Color.WHITE) {
							if (matrix[i+k][j+l].getValue() > 2) {
								matrix[i+k][j+l] = new Cell(matrix[i+k][j+l].getNumberColor(), matrix[i+k][j+l].getValue()-2, matrix[i+k][j+l].getNumberType());
								return;
							}
							else {
								matrix[i+k][j+l] = Cell.emptyGameCell();
								return;
							}
						}	
					}
					
				}
			}
		}
		else if (unit.getEffect() == Effect.five.getEquivalent()) {
			for (int k = -1; k < 2; k++) {
				for (int l = -1; l < 2; l++) {
					// Si la case testée est dans le board
					if ((i+k >= 0 && i+k < getNbLines()) && (j+l >= 0 && j+l < getNbColumns())) {
						if (matrix[i+k][j+l].getColor() == col) {
							matrix[i+k][j+l] = new Cell(matrix[i+k][j+l].getNumberColor(), matrix[i+k][j+l].getValue()+2, matrix[i+k][j+l].getNumberType());
							return;
						}
					}
				}
			}
		}
		else if (unit.getEffect() == Effect.seven.getEquivalent()) {
			for (int k = -1; k < 2; k++) {
				for (int l = -1; l < 2; l++) {
					// Si la case testée est dans le board
					if ((i+k >= 0 && i+k < getNbLines()) && (j+l >= 0 && j+l < getNbColumns())) {
						if (matrix[i+k][j+l].getType() == "Structure") {
							if (matrix[i+k][j+l].getValue() > 4) {
								matrix[i+k][j+l] = new Cell(matrix[i+k][j+l].getNumberColor(), matrix[i+k][j+l].getValue()-4, matrix[i+k][j+l].getNumberType());
							}
							else {
								matrix[i+k][j+l] = Cell.emptyGameCell();
							}
						}
					}
				}
			}
		}
	}
	
	// Utilisation d'un sort
	public boolean useSpell(int i, int j, Spell spell, Color col) {
		if (spell.getEffect() == Effect.two.getEquivalent() && matrix[i][j].getColor() != col) {
			if (matrix[i][j].getColor() != Color.WHITE) {
				System.out.println("Attaque magique sur ("+i+", "+j+")");
				magicAttack(i, j, 3);
				return true;
			}
		}
		else if (spell.getEffect() == Effect.one.getEquivalent()) {
			System.out.println("Tornade de lames");
			for (int k = 0; k < matrix.length; k++) {
				for (int l = 0; l < matrix[0].length; l++) {
					if (matrix[k][l].getColor() != col && matrix[k][l].getColor() != Color.WHITE) {
						int typeTarget = matrix[k][l].getNumberType();
						int player = matrix[k][l].getNumberColor();
						if (matrix[k][l].getValue() == 1) {
							matrix[k][l] = Cell.emptyGameCell();
						}
						else {
							matrix[k][l] = new Cell(player, matrix[k][l].getValue()-1, typeTarget);
						}
					}
				}
			}
			return true;
		}
		return false;
	}
	
	// Permet d'attaquer avec un sort
	public void magicAttack(int i, int j, int damage) {
		int targetLife = matrix[i][j].getValue();
		int typeTarget = matrix[i][j].getNumberType();
		int player = matrix[i][j].getNumberColor();
		System.out.println(player);
		if (damage >= targetLife) {
			matrix[i][j] = Cell.emptyGameCell();
		}
		else {
			matrix[i][j] = new Cell(player, matrix[i][j].getValue()-damage, typeTarget);
		}
	}
	
	// Déplacement d'une unité
	public void move(int value, Color col, int i, int j, int mov, CardInterface card) {
		Coordinates c = new Coordinates(i, j);
		
		if (col == Color.BLUE) {
			for (int k = 0; k < mov; k++) { // pour chaque déplacement
				if (c.getI()-1<0) { // si la case suivante est la base adverse
					hp1 = hp1-matrix[c.getI()][c.getJ()].getValue(); // retire les hp à la base
					matrix[c.getI()][j] = Cell.emptyGameCell(); // reset de l'ancienne case
					System.out.println("La base rouge a été attaqué.");
					break;
				}
				else if (!hit(c, col, card)) { // si la case suivante est vide
					//System.out.println("Pas de colision, déplacement d'une case");
					matrix[c.getI()-1][j] = new Cell(1, matrix[c.getI()][c.getJ()].getValue(), 1); // maj de la nouvelle case
					matrix[c.getI()][j] = Cell.emptyGameCell(); // reset de l'ancienne case
					c = new Coordinates(c.getI()-1, c.getJ()); // maj des nouvelles coordonnées de l'unité
				}
				else { // si la case suivante est déjà prise
					if (matrix[c.getI()-1][c.getJ()].getColor() == Color.RED) { // si la case suivante est occupée par un adversaire
						if (fight(c, new Coordinates(c.getI()-1, c.getJ()))) { // si le combat a été gagné
							c = new Coordinates(c.getI()-1, c.getJ()); // maj des nouvelles coordonnées de l'unité
						}
						else { // si le combat a été perdu ou si il y a eu une égalité
							break; // on stop le déplacement
						}
					}
					else { // si la case est occupé par un allié
						break; // on stop le déplacement
					}
				}

			}			
		}
		else if (col == Color.RED) {
			for (int k = 0; k < mov; k++) { // pour chaque déplacement
				if (c.getI()+1>getNbLines()-1) { // si la case suivante est la base adverse
					hp2 = hp2-matrix[c.getI()][c.getJ()].getValue(); // retire les hp à la base
					matrix[c.getI()][j] = Cell.emptyGameCell(); // reset de l'ancienne case
					System.out.println("La base bleue a été attaqué.");
					break;
				}
				else if (!hit(c, col, card)) { // si la case suivante est vide
					//System.out.println("Pas de colision, déplacement d'une case");
					matrix[c.getI()+1][j] = new Cell(0, matrix[c.getI()][c.getJ()].getValue(), 1); // maj de la nouvelle case
					matrix[c.getI()][j] = Cell.emptyGameCell(); // reset de l'ancienne case
					c = new Coordinates(c.getI()+1, c.getJ()); // maj des nouvelles coordonnées de l'unité
				}
				else { // si la case suivante est déjà prise
					if (matrix[c.getI()+1][c.getJ()].getColor() == Color.BLUE) { // si la case suivante est occupée par un adversaire
						if (fight(c, new Coordinates(c.getI()+1, c.getJ()))) { // si le combat a été gagné
							c = new Coordinates(c.getI()+1, c.getJ()); // maj des nouvelles coordonnées de l'unité
						}
						else { // si le combat a été perdu ou si il y a eu une égalité
							break; // on stop le déplacement
						}
					}
					else { // si la case est occupé par un allié
						break; // on stop le déplacement
					}
				}
			}
		}
	}

	// Test si une unité est présente sur la trajectoire
	public boolean hit(Coordinates c, Color col, CardInterface card) {
		if (col == Color.RED) {	
			if (matrix[c.getI()+1][c.getJ()].getColor() == Color.WHITE) {
				//System.out.println("Case vide");
				return false;
			}
			else if (matrix[c.getI()+1][c.getJ()].getColor() == col) {
				//System.out.println("Case de même couleur");
				return true;
			}
			else if (matrix[c.getI()+1][c.getJ()].getColor() == Color.BLUE) {
				if (card != null) {
					if (card.getEffect() == Effect.four.getEquivalent()) {
						if (col == Color.RED) {
							if (matrix[c.getI()+1][c.getJ()].getValue() <= 3) {
								matrix[c.getI()+1][c.getJ()] = Cell.emptyGameCell();
								return false;
							}
							else {
								matrix[c.getI()+1][c.getJ()] = new Cell(matrix[c.getI()+1][c.getJ()].getNumberColor(), matrix[c.getI()+1][c.getJ()].getValue()-3, matrix[c.getI()+1][c.getJ()].getNumberType());
							}
						}
					}
				}
				System.out.println("Lancement d'un combat");
				return true;
			}
		}
		else if (col == Color.BLUE) {
			if (matrix[c.getI()-1][c.getJ()].getColor() == Color.WHITE) {
				//System.out.println("Case vide");
				return false;
			}
			else if (matrix[c.getI()-1][c.getJ()].getColor() == col) {
				//System.out.println("Case de même couleur");
				return true;
			}
			else if (matrix[c.getI()-1][c.getJ()].getColor() == Color.RED) {
				if (card != null) {
					if (card.getEffect() == Effect.four.getEquivalent()) {
						if (col == Color.BLUE) {
							if (matrix[c.getI()-1][c.getJ()].getValue() <= 3) {
								matrix[c.getI()-1][c.getJ()] = Cell.emptyGameCell();
								return false;
							}
							else {
								matrix[c.getI()-1][c.getJ()] = new Cell(matrix[c.getI()-1][c.getJ()].getNumberColor(), matrix[c.getI()-1][c.getJ()].getValue()-3, matrix[c.getI()-1][c.getJ()].getNumberType());
							}
						}
					}
				}
				System.out.println("Lancement d'un combat");
				return true;
			}
		}
		return false;
	}

	// Combat entre deux unités ou structures
	public boolean fight(Coordinates att, Coordinates def) {
		int attaquant = matrix[att.getI()][att.getJ()].getValue();
		int defenseur = matrix[def.getI()][def.getJ()].getValue();
		
		// Sert pour la couleur de l'unité qui survivra
		int attCol = 1;
		int defCol = 0;
		if (matrix[att.getI()][att.getJ()].getColor() == Color.RED) {
			System.out.println("Attaquant rouge");
			attCol = 0;
			defCol = 1;
		}
		
		int typeDef = 1;
		if (matrix[def.getI()][def.getJ()].getType() == "Structure") {
			typeDef = 2;
		}
		
		if (attaquant > defenseur) {
			System.out.println("L'attaquant a gagné le combat");
			matrix[def.getI()][def.getJ()] = new Cell(attCol, attaquant-defenseur, 1);
			matrix[att.getI()][att.getJ()] = Cell.emptyGameCell();
			return true;
		}
		else if (attaquant < defenseur) {
			System.out.println("L'attaquant a perdu le combat");
			matrix[def.getI()][def.getJ()] = new Cell(defCol, defenseur-attaquant, typeDef);
			matrix[att.getI()][att.getJ()] = Cell.emptyGameCell();
			return false;
		}
		else if (attaquant == defenseur) {
			System.out.println("Egalité");
			matrix[att.getI()][att.getJ()] = Cell.emptyGameCell();
			matrix[def.getI()][def.getJ()] = Cell.emptyGameCell();
			return false;
		}
		return false;
	}
	
	/**
	 * Update the position of the front line.
	 * @param color the player's color.
	 */
	public void updateFrontLine(Color color) {
		int compteur = 0;
		if (color == Color.RED) {
			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix[0].length; j++) {
					if (matrix[i][j].getColor() == color && i<=matrix.length-1) {
						compteur++;
						frontLine.setRed(i+1);
						if (i == matrix.length-1) { // si unité dans la dernière ligne avant la base adverse
							frontLine.setRed(matrix.length-1);
						}
					}
				}
			}
			if (compteur == 0) {
				frontLine.setRed(1);
			}
		}
		else if (color == Color.BLUE) {
			for (int i = matrix.length-1; i >= 0; i--) {
				for (int j = matrix[0].length-1; j >= 0 ; j--) {
					if (matrix[i][j].getColor() == color && i>=0) {
						compteur++;
						frontLine.setBlue(i);
						if (i == 0) { // si unité dans la dernière ligne avant la base adverse
							frontLine.setBlue(1);
						}
					}
				}
			}
			if (compteur == 0) {
				frontLine.setBlue(matrix.length-1);
			}
		}
	}
	
	// Fait avancer les unités de la couleur choisie d'une case en début de tour
	public void updateBoard(Color color) {
		if (color == Color.RED) {
			for (int i = matrix.length-1; i >= 0; i--) {
				for (int j = matrix[0].length-1; j >= 0 ; j--) {
					if (matrix[i][j].getColor() == color && matrix[i][j].getType() == "Unit") {
						move(matrix[i][j].getValue(), color, i, j, 1, null);
					}
				}
			}
		}
		else if (color == Color.BLUE) {
			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix[0].length; j++) {
					if (matrix[i][j].getColor() == color && matrix[i][j].getType() == "Unit") {
						move(matrix[i][j].getValue(), color, i, j, 1, null);
					}
				}
			}
		}
	}
	
	// Test si la case sélectionnée est dans le board
	public boolean testCoordBoard(Coordinates c) { 
		if (c.getI()>getNbLines()-1 || c.getI()<0) {
			return false;
		}
		if (c.getJ()>getNbColumns()-1 || c.getJ()<0) {
			return false;
		}
		return true;
	}
	// Test si les coordonnées de la case sélectionnée se trouvent où sont affiché les cartes
	public boolean testCoordHand(Coordinates c) {
		if (c.getI() == 5 && (c.getJ() >= 5)&&(c.getJ()<5+handBlue.getNumber())) {
			return true;
		}
		return false;
	}
	
	// Test si les coordonnées de la case sélectionnée correspondent à celle pour échanger de carte
	public boolean testCordDiscard(Coordinates c) {
		if (c.getI() == 5 && c.getJ() == 10) {
			return true;
		}
		return false;
	}
	
	// Test si les coordonnées de la case sélectionnée correspondent à celle pour finir son tour
	public boolean testCoordNextTurn(Coordinates c) {
		if (c.getI() == 2 && c.getJ() == 4) {
			return true;
		}
		return false;
	}
	
}
