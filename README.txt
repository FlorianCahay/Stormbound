*** Projet réalisé par CHEBILI Sami et CAHAY Florian ***


--/ Implémentation

- Prise en compte de la faction de la carte pour l'ajouter dans un deck
- Déplacement d'une unité
- Combat entre deux unités
- Attaque de la base adverse
- Gestion de la ligne de front
- Changement de tour
- Le joueur peut poser une carte de sa main
- Le joueur peut utiliser des sorts en les sélectionnant dans sa main et en cliquant sur le board
- Possibilité de défausser une carte de notre main une fois par tour
- Prise en compte du mana du joueur pour poser une carte
- La main du joueur est remplie au début de son tour
- Les effets des cartes du deck de base
- Possibilité de choisir son deck avant de commencer une partie
- Création d'un deck et d'une carte depuis un fichier
- Ajout d'une zone pour voir la description de la carte en cliquant dessus.


--/ Organisation

Liste des classes du projet:
- CardAbstract (classe abstraite) : représente une carte avec les attributs en communs
- CardInterface (Interface) : représente une carte avec les méthodes minimum
- Cell : représente une case du plateau (couleur du joueur, force de l'unité, type de la carte dessus si il y en a une)
- Coordinates : gère un couple d'entiers qui représente des coordonnées
- Deck : liste de carte disponibles pour les joueurs
- DeckConstruction : LinkedHashMap des deck déjà enregistrés qui se trouvent dans le répertoire courant
- Effect (Enumération) : contient toute la liste des effets qu'une carte peut avoir
- FrontLine : gère les lignes de front des deux joueurs
- Hand : correspond au carte de la main d'un joueur pioché dans le deck
- NeutralCard : liste des cartes neutres créées depuis un fichier
- SimpleGameController : initialise, lance le programme et analyse les actions du joueur
- SimpleGameData : contient toutes les fonctions nécessaires au déroulement du programme
- SimpleGameView : sert à tout afficher
- Spell (extends CardAbstract implements CardInterface): représente un sort
- Structure (extends CardAbstract implements CardInterface): représente une structure
- Unit (extends CardAbstract implements CardInterface): représente une unité

Le deck est une ArrayList de CardInterface, les unités, sorts et structures héritent de CardAbstract et implémentent CardInterface.

--/ Choix techniques/algorithmiques

Nous avons au départ choisi de débuter en mode console pour les premiers tests, afin d'avoir des résultats faciles à appréhender. Les modifications étaient donc plus rapides et plus simples à faire. La matrice, les unités ou les cartes elles-mêmes sont passés par ce processus.
Certains attributs de classes (par exemple dans data) ne sont pas en private car nous les modifions dans une autre classe. Nous avons choisi de les mettre en package au lieu de mettre des getters et des setters.

--/ Problèmes rencontrés

- La gestion des déplacements pour les unités: si une unité gagnait un combat et qui lui restait un point de mouvement, elle se stoppait alors qu'elle aurait dû avancer de nouveau.
- La gestion des cartes de la main du joueur.
- L'éciture des effets des cartes était des fois trop grande pour la case.
- Pouvoir créer un deck depuis l'écran du jeu en sélectionnant une carte et en l'ajoutant.

--/ Remarques

*** Un clic ne suffit pas, il faut double-cliquer (1er clic pour sélectionner la case et 2ème clic pour faire l'action, comme poser une carte). ***

Dans la première version, on pouvait seulement poser une carte qui se déplaçait et attaquait les unités adverses. 
Le joueur rouge, qui doit correspondre a l'IA, n'avait pas encore été implémenté, donc c'était à l'utilisateur de cliquer sur la case orange pour changer de joueur et jouer à sa place.

Dans la seconde version, l'IA a été implémenté, elle joue encore en mode aléatoire. 
Au lieu de s'occuper de cette partie, nous nous sommes appliqués à faire le reste du jeu et d'essayé de rattraper notre retard, le jeu est donc fonctionnel. 
Seulement les effets des sorts sont implémentés dans le jeu.

Dans la troisième version, l'IA utilise les cartes du deck et sa main est gérée comme pour le joueur. Il peut, cependant, seulement jouer des structures ou des unités.
On peut maintenant créer un deck en nommant un fichier texte "Deck_nomdudeck.txt".
Il doit y avoir une carte par ligne en utilisant le même pattern que pour le deck de base ("nomCarte/effetCarte/typeCarte/raretéCarte/manaCarte/forceCarte/mouvementCarte").
Au lancement du jeu, on a le choix entre "Un seul joueur" ou "Construction d'un deck". La partie de construction a seulement été initialisée avec les cartes présentent dans le fichier texte "Neutral_cards.txt", mais il n'est pas possible de créer un deck.
En choisissant "Un seul joueur", vous avez le choix du deck que vous allez utiliser.