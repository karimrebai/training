<div class="pb"></div>

## TP 7 : Collections

L'API collection de Scala est riche de fonctions pour la manipulation d'ensemble de données. L'objectif de ce TP est de manipuler cette API sur des cas d'usage.

Le formateur doit vous fournir une archive contenant le squelette du projet avec un jeu de classe. Dans cette archive on trouve également des tests unitaires pour valider les réponses.

*Tips : lancer sbt en mode continuous testing...*


### 7.1 Word count

Nous allons faire un exercice classique : compter des mots dans un texte.

Sur la classe `CollectionRepository`, utilisez la méthode `getBigText` ; elle fournit un texte a partir duquel on va compter les mots.

Objectif de cet exercice est de compter le nombre de mot contenu dans le texte en se basant principalement sur l'API des collections Scala.

Méthode à implémenter : `wordCount`

### 7.2 Moyenne générale

Ici nous allons devoir calculer la moyenne générale d'une classe. La classe est représentée sous la forme d'une liste type comme suit `List[List[Double]]`
(Elle est fournie par la classe utilitaire `CollectionRepository`).

Toujours en se basant sur l'API, il faut calculer la moyenne des notes contenues dans cette structure.

Méthode à implémenter : `moyenneGenerale`

*tips : Définir une fonction qui calcule la moyenne peut être utile*

### 7.3 Regrouper les mots

Ici nous allons devoir trier des mots.

Pour cet exercice, il faudra, à partir d'une chaîne de caractères classer les mots dans 2 groupes selon un critère de longueur.

Le résultat de la fonction doit créer un tuple de 2 éléments.
Chaque élément est une liste de mots.
La répartition se fait selon la règle suivante : si un mot est d'une longueur supérieur à 4.

Méthode à implémenter : `groupWord`

### 7.4  Lister, filtrer, transformer

Ici nous allons manipuler une liste de personnes. Cette liste contient un ensemble d'instance de la case Classe `person(nom:String,age:Int,ville:String)`.

Ci-dessous une liste d'information que l'on souhaite extraire, pour chaque demande un squelette de méthode est disponible.

- Liste des personnes ayant plus de 30 ans
- Calcule de la moyenne d'age de l'ensemble des personnes
- Lister les villes représentées dans la collection (chaque ville doit être unique dans le résultat)
- Transformer la liste initiale en une liste de chaîne de caractères formatée selon le template suivant : `nom-ville`
- Ordonnez la liste des personnes de manière ascendante par rapport à l'âge

### 7.5  Somme

L'idée de cet exercice est simplement de faire la somme des entiers d'une liste.
Petite particularité, les éléments de la liste sont de type String.

Pour cette exercice, l'utilisation de la méthode sum n'est pas autorisé.

Méthode à implémenter : `sum`

### 7.6  Premier élément et les autres

Ici, il va falloir renvoyer le premier élement d'une liste.

Méthode à implémenter : `fistOne`

Et aussi renvoyer l'ensemble des éléments restant

Méthode à implémenter : `others`

### 7.7  Eleves-Notes

Ici, nous allons faire une association entre les éléves et leurs notes.
Nous avons deux listes, une liste d'élève et une liste de liste de notes.
Chaque occurence de chaque liste se correspondent.

Il faut créer une nouvelle liste qui contiendra un ensemble de tuple `(Eleve, List[Double])`

Méthode à implémenter : `attributionNoteEleve`

*Note : Pour cet exercice aucune structure de boucle est nécessaire.*
