<div class="pb"></div>

## TP 4 : Case class et Pattern matching

Soit la classe ci-dessous.
Elle sera utile pour les exercices du TP4, copier là dans votre projet dans le package service

```scala
package service

object Tp4Service {

  val annuaire: Map[Int, String] = Map(1 -> "Louis", 2 -> "Paul", 5 -> "Patrick", 9 -> "Leo", 4 -> "Cedric", 3 -> "Martin", 19 -> "Jean")

  def giveYourChoice(valeur: Int): String = {

    if (valeur == 1) {
      "Tout est OK"
    } else if (valeur == 2) {
      "Attention à la marche"
    }  else if (valeur == 4) {
      "Attention à la marche"
    } else if (valeur == 10) {
      "10 est un bon choix"
    } else {
      "la valeur saisie n'est pas reconnue"
    }
  }

  println(giveYourChoice(2))
}
```


### 4.1 Refactoring

La première étape va être de faire un refactoring de la méthode `giveYourChoice`.
Pour cela, on se basera sur le pattern matching pour ré-implémenter le corps de la méthode.

Avant de commencer, il faut s'assurer que lorsque l'on aura terminé notre refactoring la méthode fonctionnera à l'identique.

Pour cela, il faut écrire des tests unitaire.
On utilisera la librairie *ScalaTest* qui est la plus proche (en mieux ;) ) de JUnit.

Vérifier que la dépendance est bien présente dans le descripteur de déploiement et créer la classe de tests dans le répertoire dédié selon les conventions de Maven.

Une fois la couverture de test faite, vous pouvez commencer le refactoring.

*Tips : pendant la phase de refactoring, dans une console lancer `sbt` en mode interactif :

```shell
sbt
```

lorsque le prompt apparaît, lancez la commande :

```shell
~test
```

*SBT se lance en continous testing. C'est à dire qu'à chaque modification du code source, sbt relancera les tests et on obtient directement un retour sur les modifications que l'on à faites.*

Une fois le refactoring terminé et que tous les tests sont au vert vous pouvez passer à l'exercice suivant.


### 4.2 pattern matching & Option

Pour ce TP, nous allons utiliser la variable `annuaire` de la classe de service TP4Service.

*Pour information, dans la classe `Map`, la méthode `get` renvoie une variable de type Option. Cette information va nous servir pour l'exercice suivant.*

Créer une classe `Main` à la racine des packages.

Dans cette classe, vous allez devoir implémenter une fonction `search` qui à partir d'un identifiant va interroger l'annuaire (`get`) et renvoyer un message indiquant si une personne a été trouvée en concaténant son nom.
Si personne n'a été trouvé renvoyer le message '*aucune correspondance trouvée*'.

L'utilisation de la structure conditionnelle `if` n'est pas autorisée pour cet exercice.


### 4.3 Pattern Matching & Case classe

Nous allons ici nous pencher sur les cases classes. Nous allons étudier les propriétés de ces classes particulières.

Dans le package `caseclasse`, créer une classe `Main`.

Créer une case classe `Humain`. Cette classe a 3 attributs : `nom`, `prenom`, `age`.

Nous allons voir comment une case classe peut être décomposée lorsqu'elle est utilisée avec du pattern matching.

Toujours dans la classe `Main`, créer une fonction nommée `isMajeur`. Le type de cette fonction est :

```
(Humain) => Boolean
```

En fonction de l'âge, et peu importe ses nom et prénom, la fonction renverra `true` si l'Humain est majeur sinon `false`.
L'implémentation de la fonction doit se baser sur du pattern matching.

Créer une autre fonction nommée `estDeLaFamilleDupont`. Elle est le même type que la précédente.

Si l'Humain est de la famille Dupont, alors la fonction renvoie `true`, sinon `false`
