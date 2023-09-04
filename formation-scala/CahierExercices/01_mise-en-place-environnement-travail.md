<div class="pb"></div>

## TP 1 : Mise en place de l'environnement de travail

L'objectif du premier TP, va être de mettre en place son environnement de travail.

Nous allons commencé par initié un projet. Ensuite ajouter les plugins nécessaires pour générer la configuration de son IDE préféré.

Enfin nous ajouterons les dépendances nécessaires pour écrire des tests et pouvoir les lancer.



### 1.1 Création du descripteur SBT

Sur votre disque, dans le répertoire de travail de votre choix, créer un répertoire `formationScala`.

Dans ce répertoire créer un fichier nommé `build.sbt`.

Nous venons de créer le répertoire racine de notre premier projet.

Maintenant, nous allons fournir à notre descripteur de déploiement quelques éléments d'information sur le projet :

- nom du projet : `formationScala`
- version : `0.0.1`
- version du compilateur Scala : `2.10.5`

Ecrire les informations précédentes dans le fichier selon la syntaxe définie par l'outil.

Une fois le descripteur créé, vous pouvez vérifié qu'il a été correctement écrit en tapant les commandes suivantes :

- `sbt name` : renvoie le nom du projet
- `sbt version` : renvoie la version du projet
- `sbt scalaVersion` : renvoie la version de Scala utilisée par le projet


### 1.2 Création de la structure du projet

Nous allons créer la structure des répertoires du projet.

Comme nous l'avons vu dans les slides SBT respecte la même arborescence que celle définie par maven :

```
formationScala/
    ├── src
    │   ├── main
    │   │   ├── java
    │   │   ├── resources
    │   │   └── scala
    │   └── test
    │       ├── java
    │       ├── resources
    │       └── scala
    └── build.sbt
```

Pas de grande difficulté dans cette étape, donc une vérification visuelle suffira :)


### 1.3 Générer les fichiers de configuration pour l'IDE

Il est possible de rajouter des plugins pour enrichir les possibilités de SBT.

Ici nous allons rajouter un plugin (selon l'IDE choisi) pour générer les fichiers de configuration correspondant.

L'ajout de plugin se fait dans un fichier nommé `plugins.sbt` qui se trouve dans un répertoire nommé projet comme décrit ci-dessous :

```
formationScala/
    └── project
          └── plugins.sbt
```


Dans ce fichier, on va ajouter la déclaration du plugin qui nous intéresse.

L'ajout du plugin se fait via la fonction : `addSbtPlugin`

Vous trouverez la liste des plugins [ici](http://www.scala-sbt.org/release/docs/Community-Plugins.html)

Une fois configuré, vous pouvez lancer la commande correspondante pour générer les fichier de configuration et ouvrir le projet dans votre IDE (préalablement téléchargé)

*Rappel : Il est préférable d'utiliser ScalaIDE qui prendre en charge pas mal de choses par défaut.*


### 1.4 Premier programme

Nous allons ajouter notre programme 'Hello World' dans la structure du projet.

Reprenez le code utilisé lors de l'installation de SBT et copier le dans le répertoire dédié au code Scala dans le package `tp1`.

Ajouter en début du fichier la ligne suivante :

```shell
package tp1
```

Ensuite, placez vous dans la console et positionnez le prompt à la racine du projet (en l'occurrence TP1/). Lancez la commande

```shell
sbt run
```

Le programme doit se lancer et afficher le traditionnel 'Hello World'.

Ensuite repasser dans votre IDE et lancer la classe `Hw.scala` selon les commandes habituelles de l'IDE.

Vous devriez voir le `Hello World` s'afficher dans la console de l'IDE.

A ce point, vous avez un environnement de travail installé, configurer et un IDE ouvert sur le projet courant.


### 1.5 Ajouter une dépendance au projet

L'objectif de cette partie est de rajouter des dépendances au projet.

Nous allons ajouter deux librairies permettant d'écrire des tests unitaires.

L'ajout de dépendances se fait directement dans le fichier `build.sbt` qui se trouve à la racine du projet.

Les artefacts à rajouter sont :

- ScalaTest
- Spec2

*Tips : La bonne pratique est d'ajouter ces dépendances en passant par des variables intermédiaires*

Lorsque les dépendances ont été ajoutée, on peut reprendre le code suivant et l'ajouter dans les répertoires dédiés aux tests en Scala dans une fichier nommé : `TestScala.scala`.

Basé sur ScalaTest et Specs2 :

```scala
import org.scalatest.FunSuite

class SetSuite extends FunSuite {

  test("An empty Set should have size 0") {
    assert(Set.empty.size == 0)
  }

  test("Invoking head on an empty Set should produce NoSuchElementException") {
    intercept[NoSuchElementException] {
    Set.empty.head
    }
  }
}
```

```scala
import org.specs2._


class HelloWorldSpec extends Specification {
  def is = s2"""

  This is a specification for the 'Hello world' string

  The 'Hello world' string should
    contain 11 characters                             $e1
    start with 'Hello'                                $e2
    end with 'world'                                  $e3
                                                      """

  def e1 = "Hello world" must haveSize(13)

  def e2 = "Hello world" must startWith("HelloE")

  def e3 = "Hello world" must endWith("world")
}
```


Une fois enregistrer vous pouvez lancer la commande suivante pour lancer les tests :

```shell
sbt test
```

*info : Tous les tests ne sont pas valides, observer les résultats en fonction du code et tenter de corriger les tests défaillants.


Nous sommes arrivé à la fin du premier TP :

- notre environnement est configuré
- Des tests peuvent être écris et exécutés
- Nous savons ajouter des dépendances et de plugins
- Nous avons un IDE configuré et opérationnel
