<div class="pb"></div>

## TP 3 : Les structures du langage

Pour ce TP nous allons passer dans l'IDE. Pour cela nous pouvons partir du projet que nous avions créé lors du TP1.

Il faut autant que possible que chaque TP soit fait dans son propre package.

Par conséquent, créer un nouveau package *tp3* dans lequel on stockera les classes de ce TP.


### 3.1 Une classe

Déclarer une classe `Ferrari` dans le package `garage`.

Elle aura plusieurs attributs :

- `nbPorte` (privé)
- `couleur` (immutable)
- `nbPassager` (mutable)

Maintenant créer un deuxième constructeur qui n'aura qu'un seul paramètre : couleur (les autres paramètres sont fixés par défaut à `nbporte` : 2 et `nbPassager` : 2)


### 3.2 Sémantique & Égalité

Créer une classe `Main`, qui pourra être exécutée

Nous allons maintenant tenter de comparer des instances entre elle afin de comprendre les mécanismes d'égalité sémantique ou référentielle.

Créer deux instances de la classe `Ferrari` (`f1` et `f2`) avec des paramètres de constructeur différent.

Quel opérateur je peux utiliser pour faire de la comparaison sémantique et à quelles conditions ?

Quelles fonctions me permettent de pouvoir faire une comparaison référentielle indépendamment de l'implémentation de la classe comparée ?

Pour vérifier vos hypothèses créer des instances répondant aux différents critères d'égalité.


### 3.3 Un trait

Déclarer un trait `Vehicule` dans le package `garage`. Ce trait définit une fonction `rouler():Unit`.

Ce trait caractérise un véhicule et lui donne la capacité de rouler.

L'implémentation de cette fonction, doit afficher un message "roule".

Nous allons dans un premier temps faire en sorte que notre Ferrari puisse rouler.

Maintenant, il faut créer une autre classe : `VTT()`. Nous allons lui attribuer la capacité de rouler sans passer pas le mécanisme d'héritage.

Comment peut on faire?


### 3.4 Utilisation d'un implicit

Pour notre exercice, nous allons créer un jeu de classes dans le package `banque`.

Dans un premier temps, nous allons créer une classe nommée `BanqueEuro`, cette classe aura une méthode versement avec le type suivant :

```scala
(DeviseEuro(Int))=> String
```

La méthode renverra le message suivant :  **la somme de [montant] a été versée à la banque**.

*Tips : vous pouvez utiliser l'interpolation de String pour construire la chaîne de caractère*.

Nous allons créer également deux classes `DeviseEuro` et `DeviseUSA` dans un module nommé `devise`.

Chaque classe du module `devise` aura un attribut `montant` public en lecture seule.

Enfin, on va créer une classe Main qui permettra de lancer le programme.

L'idée est de d'abord construite une instance de la classe `BanqueEuro`, ensuite créer une instance de la classe `DeviseEuro` puis une instance de la classe `DeviseUsa` (peu importe les montants).

Une fois les objets créés, il faut verser les différentes montants à la `BanqueEuro`.

Quel problème allons nous avoir ? Comment adresser ce problème sans impacter le code déjà écrit ?
