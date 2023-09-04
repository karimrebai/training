# Programmation fonctionnelle : Introduction

<!-- .slide: class="page-title" -->



## Sommaire

- Généralité sur la programmation fonctionnelle
- Récursivité
- Higher Order Fonction (HOF)



## Généralités : Définition

- Modèle de programmation s'opposant à la programmation impérative
- Issu du Lambda Calculus
  - Haskell Curry notamment dans les années 30
- Perception très mathématique de la programmation
- Paradigme opposé à l'utilisation de la mutation d'état
  - Etat mutable : dangereux (multi threading) même si plus simple à aborder
- **Certains langages fonctionnels ne permettent pas la mutation de variables**
  - Scala est plus souple à ce niveau



## Généralités : Historique des langages fonctionnels

- 1958 : Lisp
- 1975 : Scheme
- 1983 : 1ère standardisation de ML
- 1983 : Common Lisp
- 1987 : Haskell
- Par la suite sont apparus :
  - Erlang
  - Ocaml
- Puis les Langages au dessus de la JVM :
  - **2003 : Scala**



## Généralités : Caractéristiques de la programmation fonctionnelle

- Dissuader entre autre de l'usage de la mutation de variable
- Permettre la manipulation de fonctions en tant que variable
- Programme visant à composer des fonctions, chacune fonctionnant à la manière d'une boîte noire  **1 entrée (n paramètres) → 1 sortie**
- Propriété de *transparence référentielle*



## Généralité : Transparence référentielle

- Cette propriété signifie qu'une expression (telle qu'un appel de fonction)
  peut être remplacée par sa valeur sans affecter le comportement du programme.
- L'expression doit donc ne pas avoir d'effet de bord et être *une fonction pure*,
  c’est-à-dire retourner toujours la même valeur quand on l'appelle avec les mêmes arguments




## Généralités : Avantages vs inconvénients de la prog. fonctionnelle

- Avantages :
  - Caractère reproductible de l'exécution d'un programme
  - Plus de synchronisation en environnement multi threads
- Inconvénients :
  - Pas d'effet de bord
  - Pas de mutation d'état



## Généralités : Classification des langages fonctionnels

- 2 grandes familles :
  - Langages fonctionnels **purs** : pas de mutation de variables possible (exemple Erlang)
  - Langages fonctionnels **impurs** : La mutation de variable autorisée.
- Par conséquent Scala peut être vu comme un langage fonctionnel impur.



## Généralités : Langages impératifs vs Langages fonctionnels

- **Langages impératifs** utilisent des machines à état (Turing ou van Neuman)
- **Langages fonctionnels** sont simplement un flot d'exécution de fonctions mathématiques
- Certains algorithmes s'écrivent beaucoup plus facilement en disposant de machines à états
- D'où **les langages fonctionnels impurs** supportant quelques exceptions à la règle



## Récursivité : Introduction

- Bannie de l'esprit des développeurs ayant pratiqué les langages impératifs
- Problème de la convergence de la solution
- **Soucis de performance** dus à la stack pouvant aller jusqu'à exploser dans certains cas
- Existence d'un mécanisme d'optimisation en Scala : **tail recursion**



## Récursivité : Cas d'usages

- Suite de Fibonacci
- Parcours d'arbre
- Factorielle
- etc



## Récursivité : Tail recursion ou récursion terminal

- Optimisation d'un algorithme récursif permettant in-fine d'obtenir un code proche de celui
  généré par un compilateur d'un langage impératif
  - Dernière instruction de la fonction est un appel à cette fonction
  - Cet appel ne peut dépendre d'éléments extérieurs : **n*fact(n-1)** ne peut être donc candidat



## Récursivité : Suite de Fibonacci

**\>**  Rappel sur la suite de Fibonacci :

> Chaque terme de cette suite est la somme des deux termes précédents;
>
> Pour obtenir chacun de ces deux termes il faut faire la somme de leurs termes précédents et ainsi de suite,
> jusqu'à ce que ces deux termes soient les deux termes initiaux.

- Liste des cas rencontrés dans le calcul de la suite

```
F(0)=1  //terme initial
F(1)=1  //terme initial
F(n)=F(n-1)+F(n-2) //cas général
```



## Récursivité : Exemple

- Implémentation en Scala

``` scala
object Fibo extends App {
  def fib(n: Int): Int = {
    if (n == 1)
      1
    else if (n == 2)
      1
    else
      fib(n - 1) + fib(n - 2)
  }
  assert(fib(5) > 1 && fib(5) > fib(4))
  println(fib(5))
}
```

- Quels soucis pose cette façon (naturelle) d'écrire la suite de Fibonacci ?




## Récursivité : Commentaires

- L'écriture précédente induit une vaste dépense de cycles CPU :
  - Pour calculer F(10) on devra calculer F(2) … F(10)
  - Par la suite calculer F(5) induira un nouveau calcul de F(2)..F(5)
  - Quel gâchis!
- On peut être alors tenté d'utiliser la technique dite de la mémoisation (garder un cache des valeurs précédemment calculées pour accélérer les traitements)

- Exemple

```
F(5)
= F(4)+ F(3)
= F(3)+ F(2) + F(1) + F(2)
= F(2) + F(1) + 1 + 1 + 1
```



## Récursivité : Exemple

```
F(10)
= F(9) + F(8)
= F(8) + F(7) + F(7) + F(6)
= F(7) + F(6) + F(6) + F(5) + F(6) + F(5) + F(5) + F(4)
= F(6)+ F(5) + F(5) + F(4) + F(5) + F(4) + F(4) + F(3) + F(5) + F(4) + F(4) + F(3)
+ F(4) + F(3) + F(3) + F(2)
= F(5) + F(4) + F(4) + F(3) + F(4) + F(3)
+ F(3) + F(2) + F(4) + F(3) + F(3) + F(2) + F(3) + F(2) + F(2) + F(1) + F(4) + F(3) + F(3)
+ F(2) + F(3) + F(2) + F(2) + F(1) + F(3) + F(2) + F(2) + F(1) + F(2) + F(1) + F(2)
...
= F(1) + F(2) + F(2) + F(1) + F(2) + F(4) + F(1) + F(2) + F(2) + F(1) + F(2) + F(1) + F(2) + F(2) + F(1) + F(2) + F(1) + F(2) + F(2) + F(1) + F(2) + F(2) + F(1) + F(2) + F(1) + F(2) + F(2) + F(1) + F(2) + F(2) + F(2)
+ F(1) + F(1) + F(2) + F(2) + F(1) + F(2) + F(1) + F(2) + F(2) + F(1) + F(2) + F(2) + F(2) + F(1) + F(1) + F(2) + F(2) + F(2) + F(1) + F(2) + F(1) + F(2)
```



## Récursivité : Exemple

- Implémentation en **Java** avec stockage des valeurs calculées

```java
private static Map<Integer, Long> map = new HashMap<Integer, Long>();

public Long fibonacci(Integer n) {
  if (n == 1 || n == 2) {
    return 1L;
  }
  Long valeur = map.get(n);
  if (valeur != null)
    return valeur;
  valeur = fibonacci(n - 1) + fibonacci(n - 2);
  map.put(n, valeur);
  return valeur;
}
```



## Récursivité : Commentaires

- Limitations sur le code précédent ?

  - Heap overflow
    - Utilisation de softs référence pour un fonctionnement serein avec le GC
  - Que se passe-t-il en environnement multi threads ?
    - Nécessité de protéger la section critique (`Map`)
    - Impossible de tirer parti de plusieurs coeurs pour un calcul parallèle



## Récursivité : Autre exemple

- Autre approche en **Scala**

```scala
def fib( n:Int) = fib_tr( n, 1, 0)

@tailrec
def fib_tr( n: Int, b: Int, a: Int): Int =
  n match {
    case 0 => a
    case _ => fib_tr( n -1, a + b, b)
  }
```

- Approche utilisant **la récursion terminale**
  - Pas d'état conservé
  - Pas de buffer overflow
  - Extrêmement rapide et optimisée par le compilateur Scala
  - Utilisation du pattern matching (détaillé plus tard)
  -  Approche courante dans les langages fonctionnels

<!-- .element: class="reduce-size" -->



## Récursivité : Remarques

- Dans l'exemple précédent l'annotation **@tailrec** est utilisée :
  - Elle indique au compilateur de vérifier la possibilité d'optimiser la fonction
  en remplaçant la *récursion* par une *itération*
  - l'annotation n'est pas indispensable, mais fortement pertinent



## TP5 : Récursivité

<!-- .slide: class="page-tp5" -->



## Higher Order Function : Fonctions d'ordre supérieur

- Les fonctions sont traitées dans les langages fonctionnels comme n'importe quelle autre variable
  - On peut les passer en paramètre
  - On peut retourner une fonction en résultat d'une autre
    fonction
- Les fonctions d'ordre supérieur permettent de factoriser des traitements,
  comme la généricité permet d'appliquer un traitement avec une abstraction du type de donnée traitée



## Higher Order Function : Exemple

- Exemple d'utilisation d'une HOF standard Scala : *filter*
- On lui passe en paramètre une fonction dite *Predicat*
- Dans notre cas, c'est *isOdd*

```scala
object FindEventNumber extends App {

def isEven(a: Int): Boolean = {
  a % 2 == 0
}

def isOdd(a: Int) = !isEven(a)

val values = List(1, 3, 4, 6, 9)
    println(values.filter((isOdd)))
}
```

- Résultat :

```
[info] Running org.FindEventNumber
List(1, 3, 9)
[success] Total time: 2 s, completed 11 oct. 2015 00:09:59
```



## Higher Order Function : Commentaires sur l'exemple

- La méthode `filter` de la classe `List` (en Scala) est d'ordre supérieur car elle accepte une fonction en paramètre
  - Ce paramètre est un prédicat (renvoie un booléen)
  - Il permet de filtrer les occurrences à inclure dans la liste résultante
- Intérêt :
  - Si nous voulons filtrer les nombres pairs et non plus impairs, il me suffit de passer `isEven()` en paramètre de la fonction



## Higher Order Function : Typage d'une fonction

- Extension de la notion de signature des méthodes dans les langages impératifs
- Notation générale :
```
<nom_fonction> : (<param_type>) => (type_retour)
```
- Exemple :
```
f : (Int) => (Int)
```



## Higher Order Function : Fonctions anonymes

- Certaines fonctions n'ont qu'un intérêt très local
- Aucun intérêt de les exposer dans nos APIs
- Peu d'espoir de les réutiliser
- Fonctions candidates idéales :
  - `isEven`
  - `isOdd`



## Higher Order Function : Exemple

```scala
package progfunc
import scala.annotation.tailrec

object Count extends App{

  def doubleFn(x:Int)=x*2

  def count(f: Int => Int ,max : Int):Int ={

  @tailrec
  def countRec(f:Int => Int, from:Int, to:Int ,acc :Int):Int ={
    println("countRec with function = "+ f + " from = " + from + " to = "+ to + "
      accumulator = "+ acc)
    if(from>to) {
      acc
    } else {
      countRec(f,from+1,to,acc+f(from))
    }
    countRec(f,1,max,0)
}
println(count(doubleFn,5))
```



## Higher Order Function : Exemple

```scala
package progfunc
import scala.annotation.tailrec

object Count extends App {

  //def doubleFn(x:Int)=x*2

  def count(f: Int => Int ,max : Int):Int ={

  @tailrec
  def countRec(f:Int => Int, from:Int, to:Int ,acc :Int):Int ={
    println("countRec with function = "+ f + " from = " + from + " to = "+ to + "
      accumulator = "+ acc)
    if(from>to) {
      acc
    } else {
      countRec(f,from+1,to,acc+f(from))
    }
    countRec(f,1,max,0)
}
println(count(x=>x*2,5))  //<<<
```



## Higher Order Function : Fonctions appliquées partiellement

- Permettent de préparer un cadre d'exécution à la fonction en attendant la complétude des paramètres
- Ces fonctions peuvent être passées en paramètre à une autre fonction
- Syntaxe utilisant un `_` pour désigner les paramètres restant à renseigner



## Higher Order Function : Fonctions appliquées partiellement

```scala
def sum(a:Int, b: Int):Int = a+b
val a = 10
val partialFunction = sum(a,_:Int) //fonction appliquée partiellement
```

- `partialFunction` est une fonction attendant encore un paramètre :

```scala
val resultat = partialFunction(2) // resultat = 12
```



## Higher Order Function : Currying

- Transformation d'une fonction à n paramètres en une chaîne de n fonctions prenant chacune un seul paramètre

```scala
def fnA(a:Int, b:Int):Int ==> def fnA (a:Int) (b:Int) : Int
```

- Permet une notation plus explicite pour une application partielle
- Rendue possible au runtime par une fonction spéciale en Scala
- Issu de travaux mathématiques
- Currying provient de l'héritage fonctionnel de Scala vis à vis d'Haskell
  - qui privilégiait l'utilisation de fonctions *mono* à 1 seul paramètre...



## Higher Order Function : Currying

- Équivalence des diverses notations proposées ci-dessous

```scala
f1(x:Int,y:Int):Int=x+y

f1(x:Int)(y:Int):Int=x+y

f1(x:int)=(y:Int) => x+y // préférée dans une approche fonctionnelle
```



## Higher Order Function : Currying

- Exemples de *currification* de fonction

```scala
def add(x:Int,y:Int):Int=x+y
def addCurried(x:Int)= (y:Int) => x+y

val i=add(1,2)// no mystery
val j=addCurried(1)(2)

val add2 = addCurried(2)
val values=List(1,2,3,4)

//transformation d'une collection en utilisant la fonction map
println(values.map(add2)) // résultat : List(3, 4, 5, 6)
```



## Higher Order Function : Open terms & closures

- 'Open term' : fonction contenant au moins une variable 'libre' dans sa définition
- Nécessité pour le compilateur de rechercher dans sa portée lexicale un binding possible
- Le fait de fournir ce binding à cette fonction va la *fermer* c'est ce que l'on appelle une closure
- Terme souvent galvaudé
- **Une fonction dont les paramètres suffisent à couvrir tous les besoins vis à vis du compilateur n'est pas une closure : **
<br><br>

```scala
def isEven(x:Int)=(x%2==0) // ce n'est pas une closure

def isUnderSpeedLimit(speed:Int)= (speed<maxSpeed) // c'est une closure
```



## Higher Order Function : Open terms & closures

- Que se passe t'il au runtime ?

```scala
var rate = 0.196
def computeValueAdded(x:Float)={
 x*rate
}

val price1 = computeValueAdded(100.0f)
rate=0.07

val price2 = computeValueAdded(100.0f)
println(price1==price2)
```

- <!-- .element class="fragment" -->Résultat : False



## Higher Order Function : Open terms & closures

<!-- .element class="alert alert-danger"-->
*Attention* dans un contexte fonctionnel,
le fait de dépendre de variables définies en dehors du contexte de la fonction entraîne la possibilité de non reproductibilité des tests.

- Il est prudent que les **open terms** ne dépendent que de values et non de variables sous réserve de bugs



## Higher Order Function : Open terms & closures

- L'approche la plus fonctionnelle dans l'utilisation des closures est d'en faire des fonctions locales

  - Non exposées
  - Aucun risque d'effet de bord



## Higher Order Function : Open terms & closures

- Exemple :

```scala
val belowFirst  =  ( xs : List[Int] )  =>  {

    val first = xs( 0 )

    val isBelow  =  ( y : Int )  =>   y < first

    for(  x <- xs;  if(isBelow( x ))) yield x
}

belowFirst( List( 5, 1, 7, 4, 9, 11, 3 ) ) //> res0: List[Int] = List(1, 4, 3)
```



## Introduction à la programmation fonctionnelle : Points clés

- Langage fonctionnelle prône l'immutabilité
- Fonctions de haut niveau (Higher Order Function)
  - Permettent de la réutilisation
  - Factorisation du code et de sa maintenance
- Fonctions partielles & closures
- Composition de fonctions



## TP6 : Hight Order function - Currying

<!-- .slide: class="page-tp6" -->
