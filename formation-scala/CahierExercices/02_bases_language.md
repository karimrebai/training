<div class="pb"></div>

## TP 2 : Les bases du langage

Pour ce TP, nous allons travailler exclusivement dans le REPL.

Il existe 2 façon d'accéder au REPL :

- Au prompt taper la commande : `scala`. Automatiquement, le REPL est lancé
- Via SBT, au prompte taper la commande : `sbt console`.

Lorsque le REPL est lancé, vous obtenez l'invite de commande suivante :

```shell
scala>
```

Une fois ici, vous pouvez commencer à taper votre code Scala, il sera exécuté au moment de la validation par la touche entrée.


### 2.1 Hello world

Afficher dans le REPL le traditionnel : **Hello World!**


### 2.2 Les variables

Déclarer une variable mutable nommée : `unEntierMutable` et affecter lui la valeur : `10`

Déclarer une variable immutable nommée : `unEntierImmutable` et a affecter lui la valeur : `20`

*Par définition la première variable pourra être réaffectée tandis que la deuxième non, faites l'expérience*


### 2.3 Les Fonctions

#### 2.3.1 Une fonction

Déclarer une fonction `f` prenant en paramètre deux entiers (`a` et `b`) et renverra la somme de `a` et `b`.

*Pour cette exercice écrire la déclaration complète sans utiliser l'inférence ou la notation lambda*

#### 2.3.2 Une autre fonction

Déclarer une fonction `f` prenant en paramètre deux entiers (`c` et `d`) et renverra la somme de `c` et `d`. Pour Chaque paramètre on fixera une valeur par défaut.

Exécuter les cas suivants :

- Faire un appel, sans passer de paramètre
- Faire un appel en passant 1 paramètre
- Faire en passant 1 paramètre pour initialiser la valeur de `d`

#### 2.3.3 Une fonction anonyme

Déclarer une fonction `g` utilisant la notation **lambda**.

Cette fonction prend 1 paramètre et lui ajoute 1.


### 2.4 Les tuples

Créer un tuple de 4 éléments :

- `String`
- `Int`
- `Boolean`
- `Double`

Affecter ce tuple à une variable immutable nommée `j`.

Ecrire une fonction qui en fonction de la valeur du 3ème élément affichera un message :

- Si la valeur est `true` afficher "OK"
- Si la valeur est `false` afficher "KO"
