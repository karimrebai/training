<div class="pb"></div>

## TP 8 : API XML

Dans ce TP nous allons récupérer des informations depuis un fichier XML en utilisant l'api proposée dans le langage Scala.

Le fichier XML est fourni par le formateur.


### 8.1 Chargement du fichier

Dans un premier temps, il va falloir charger le fichier dans une variable nommée `xml`.


### 8.2 Liste des titres

Il faut récupérer ici, la liste des titres des livres sous la forme d'une collection.

Méthode à implémenter : `getAllTitles`


### 8.3 Liste des livres Fantasy

Il faut récupérer ici, la liste des livres ayant le genre `fantasy`.

Méthode à implémenter : `getFantasyBooks`


### 8.4 Rechercher un livre par son ID

Il faut récupérer un livre via la valeur de son attribut `ID`.

Méthode à implémenter : `getBookTitleById`


### 8.5 Nombre de livre écrit pas O'Brien

Il faut retrouver le nombre de livre écrit par l'auteur O'Brien.

Méthode à implémenter : `getNumberOfBookForOBrien`


### 8.6 Nombre de livre contenant un mot en particulier dans sa description

Il faut parser les descriptions de chaque livre et donner le nombre de livre ayant le mot passé en paramètre dans sa description.

Méthode à implémenter : `getNumberOfBookWithdescriptionContainWord`

Une série de tests unitaires permettent de valider votre implémentation.
