Architecture :
L'ensemble des fichiers java sont dans app/src/main/java/com.exemple.sfpn.google_sfpn/

    Menu :
        Activity à l'ouverture avec le menu.
        3 boutons de jeu -> Game
        1 boutons score -> ScoreActivity
    Game :
        Le jeu, a la fin du jeu on retourne sur Menu
    CustomPosition:

    ScoreActivity:
        Utilise la class Score pour la structure des donnes, la ScoresAdapter pour un Adapter
        personaliser et ScoreDataBase.
    Score :
        Structure d'un score (points,niveau et date).
        Permet l'affichage sous forme de chaine de caractère et valeurs.
    ScoreDataBase :
        Persistance des données avec ajout et suppression d'un ou plusieurs scores. Creation de
        la table et requetes. Ajouts fait par Game et suppression par ScoreActivity.
    Custom Positions:
        Structure de postions (nom, Lat, Lng)

Les fichiers xml sont dans app/src/main/res/, les menu dans menu et layouts dans layout.









Options implémentées:
-Rotation
    Utilisation de fragment et des méthodes fournies pour éviter le rechargement de la map et de la street view
    Sauvegarder les variables nécéssaires à la reprise du jeu dans le bundle et les recharger au bon moment
    Gérer la rotation quand les alert Dialog sont visibles, implémenter manuellemnt un rechargement des alert dialog car nous
    n'avons pas utilisé de fragments pour eux.

-Persistance des scores

    Je ne savais pas comment communiquer les scores a la fin d'une partie à l'activité
    du score. Pour le faire j'ai utiliser la persistance des données, je crée une base de données
    avec le score (int) le niveau (int) et la date (string). On ecrit dedans a la fin de chaque partie
    et on la charge lorsqu'on ouvre l'activité du score. Comme ça aucun score n'est perdu.
    Dans la ListView j'ai ajouté un popupmenu pour supprimer un score si je le souhaite. Et dans
    la toolbar pour effacer tous les scores.

-Tri des scores

    Le tri des scores se fait avec la fonction "compare" des ArrayList, ou je compare soit un int ou
    un Date ce qui se fait bien de base. La différence entre un tri croissant ou decroissant se fait
    avec la multiplication par 1 ou -1 en fonction du mode choisis.
    Au niveau de la vue, j'ai ajouté deux boutons up and down dans la toolbar pour inverser l'ordre
    du tri et un menu dans la tool bar pour choisir la variable à trier.

-Mode inverse

    Le mode inverse est juste un flag envoyé dans l'intent qu'on récupere. Pour differencier lors
    de l'affichage des scores j'ajoute des niveaux : 0 = novice, 1 = novice inverse, 2 = medium, 3=
    medium inverse, 4 = expert et 5 = expert inverse.
    Le choix du mode inverse se fait avec un switch.

-Partage des scores

    Le partage d'un score depuis la listview se fait avec un popup menu, soit on supprime soit on
    partage. A la fin d'une partie une AlertDialog nous propose de partager. Au début j'ai utiliser
    SharingIntent mais l'utilisation d'un intent.createChooser est plus adapter.

Quelques difficultés :

    Personalisation de la toolbar en terme de couleur -> pas de couleurs
    Personalisation des cellules de la ListView -> creation d'un xml pour une cellule et un
    adaptateur personaliser.