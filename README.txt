*** Projet r�alis� par CHEBILI Sami et CAHAY Florian ***


--/ Impl�mentation

- Prise en compte de la faction de la carte pour l'ajouter dans un deck
- D�placement d'une unit�
- Combat entre deux unit�s
- Attaque de la base adverse
- Gestion de la ligne de front
- Changement de tour
- Le joueur peut poser une carte de sa main
- Le joueur peut utiliser des sorts en les s�lectionnant dans sa main et en cliquant sur le board
- Possibilit� de d�fausser une carte de notre main une fois par tour
- Prise en compte du mana du joueur pour poser une carte
- La main du joueur est remplie au d�but de son tour
- Les effets des cartes du deck de base
- Possibilit� de choisir son deck avant de commencer une partie
- Cr�ation d'un deck et d'une carte depuis un fichier
- Ajout d'une zone pour voir la description de la carte en cliquant dessus.


--/ Organisation

Liste des classes du projet:
- CardAbstract (classe abstraite) : repr�sente une carte avec les attributs en communs
- CardInterface (Interface) : repr�sente une carte avec les m�thodes minimum
- Cell : repr�sente une case du plateau (couleur du joueur, force de l'unit�, type de la carte dessus si il y en a une)
- Coordinates : g�re un couple d'entiers qui repr�sente des coordonn�es
- Deck : liste de carte disponibles pour les joueurs
- DeckConstruction : LinkedHashMap des deck d�j� enregistr�s qui se trouvent dans le r�pertoire courant
- Effect (Enum�ration) : contient toute la liste des effets qu'une carte peut avoir
- FrontLine : g�re les lignes de front des deux joueurs
- Hand : correspond au carte de la main d'un joueur pioch� dans le deck
- NeutralCard : liste des cartes neutres cr��es depuis un fichier
- SimpleGameController : initialise, lance le programme et analyse les actions du joueur
- SimpleGameData : contient toutes les fonctions n�cessaires au d�roulement du programme
- SimpleGameView : sert � tout afficher
- Spell (extends CardAbstract implements CardInterface): repr�sente un sort
- Structure (extends CardAbstract implements CardInterface): repr�sente une structure
- Unit (extends CardAbstract implements CardInterface): repr�sente une unit�

Le deck est une ArrayList de CardInterface, les unit�s, sorts et structures h�ritent de CardAbstract et impl�mentent CardInterface.

--/ Choix techniques/algorithmiques

Nous avons au d�part choisi de d�buter en mode console pour les premiers tests, afin d'avoir des r�sultats faciles � appr�hender. Les modifications �taient donc plus rapides et plus simples � faire. La matrice, les unit�s ou les cartes elles-m�mes sont pass�s par ce processus.
Certains attributs de classes (par exemple dans data) ne sont pas en private car nous les modifions dans une autre classe. Nous avons choisi de les mettre en package au lieu de mettre des getters et des setters.

--/ Probl�mes rencontr�s

- La gestion des d�placements pour les unit�s: si une unit� gagnait un combat et qui lui restait un point de mouvement, elle se stoppait alors qu'elle aurait d� avancer de nouveau.
- La gestion des cartes de la main du joueur.
- L'�citure des effets des cartes �tait des fois trop grande pour la case.
- Pouvoir cr�er un deck depuis l'�cran du jeu en s�lectionnant une carte et en l'ajoutant.

--/ Remarques

*** Un clic ne suffit pas, il faut double-cliquer (1er clic pour s�lectionner la case et 2�me clic pour faire l'action, comme poser une carte). ***

Dans la premi�re version, on pouvait seulement poser une carte qui se d�pla�ait et attaquait les unit�s adverses. 
Le joueur rouge, qui doit correspondre a l'IA, n'avait pas encore �t� impl�ment�, donc c'�tait � l'utilisateur de cliquer sur la case orange pour changer de joueur et jouer � sa place.

Dans la seconde version, l'IA a �t� impl�ment�, elle joue encore en mode al�atoire. 
Au lieu de s'occuper de cette partie, nous nous sommes appliqu�s � faire le reste du jeu et d'essay� de rattraper notre retard, le jeu est donc fonctionnel. 
Seulement les effets des sorts sont impl�ment�s dans le jeu.

Dans la troisi�me version, l'IA utilise les cartes du deck et sa main est g�r�e comme pour le joueur. Il peut, cependant, seulement jouer des structures ou des unit�s.
On peut maintenant cr�er un deck en nommant un fichier texte "Deck_nomdudeck.txt".
Il doit y avoir une carte par ligne en utilisant le m�me pattern que pour le deck de base ("nomCarte/effetCarte/typeCarte/raret�Carte/manaCarte/forceCarte/mouvementCarte").
Au lancement du jeu, on a le choix entre "Un seul joueur" ou "Construction d'un deck". La partie de construction a seulement �t� initialis�e avec les cartes pr�sentent dans le fichier texte "Neutral_cards.txt", mais il n'est pas possible de cr�er un deck.
En choisissant "Un seul joueur", vous avez le choix du deck que vous allez utiliser.