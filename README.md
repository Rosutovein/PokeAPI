# Application Pokemon: PokeAPI

## Créateur
Aleandre Piga, étudiant en 3ème année à l'école d'ingénieur ESIEA

## Présentation
Projet consistant en la création d'une application android codé en Java sur l'univers de Pokemon.
Cette application affiche le pokedex des quatres première génération de Pokemon et utilise une API de Github
(JSON stocké sur ce compte github)

## Prérequis
* Installation du logiciel Android Studio (disponible sur Mac, Linux et Windows)
* Récupérer le programme grâce au lien Github suivant:
```
https://github.com/Rosutovein/Projet3A.git
```

## Consignes respectées:
* Ecran avec une liste d'éléments (de Pokemon)
* Ecran avec le détail d'un élément (types, statistiques, etc.)
* Appel WebService à une API Rest (```https://github.com/Rosutovein/Projet3A/blob/master/pokedex.json```)
* Stockage de données en cache
* Fonctions supplémentaires:
  * Barre de recherche pour trouver un Pokemon en particulier
  * Splash screen au démarrage de l'application
  * Ajout d'un boutton share pour partager le dépôt Github de l'application
  * Notification Push (Firebase)

## Fonctionalités:

### Première écran
* Splash Screen au démarage de l'application
<img src="https://github.com/Rosutovein/Projet3A/blob/master/img_readme/SplashScreen.jpg" width="360" height="640" />

### Ecran Home
* Accéder au pokedex
* Partager le lien du projet Github ou l'ouvrir direcetement sur le navigateur par défaut
* Animer l'image de la pokeball (une seule animation actuellement)
<img src="https://raw.githubusercontent.com/Rosutovein/Projet3A/master/img_readme/HomeActivity.jpg" width="360" height="640" />
<img src="https://github.com/Rosutovein/Projet3A/blob/master/img_readme/Share.jpg" width="360" height="640" />

### Ecran du pokedex
* Afficher la liste des pokemon avec une barre de recherche
* Possibilité de raffraichir la liste avec un glissement vers le bas (swipe refresh)
<img src="https://github.com/Rosutovein/Projet3A/blob/master/img_readme/PokedexActivity.jpg" width="360" height="640" />
<img src="https://github.com/Rosutovein/Projet3A/blob/master/img_readme/Search.jpg" width="360" height="640" />

### Ecran du pokemon
* Affiche les détails du pokemon sélectionné dans le pokedex
<img src="https://github.com/Rosutovein/Projet3A/blob/master/img_readme/PokemonActivity.jpg" width="360" height="640" />

### Notification push
* Exemple de notification (Firebase)
<img src="https://github.com/Rosutovein/Projet3A/blob/master/img_readme/NotifiationFirebase.jpg" width="360" height="640" />
