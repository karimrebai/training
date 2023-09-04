<div class="pb"></div>

## TP 6 : Hight Order Function

### 6.1 Finances

Dans le package `finance` créé une case classe nommée Compte. Elle aura un attribut `solde`

Ensuite créer créer la classe Banque. Cette nouvelle classe a deux méthodes :

- débit : (Compte, montant) => Compte
- crédit : (Compte, montant) => Compte

La responsabilité respective de chacune des méthodes est soustraire le montant du solde du compte et ajouter le montant au solde du compte.

*note : Pour simplifier et se concentrer sur la syntaxe du langage, on ne mettra pas en place de règle de gestion
sur les valeurs que peuvent prendre le solde du compte*

Entre les deux méthodes ont trouve des similitudes. On devrait pouvoir faire une abstraction de l'opération réaliser.

Dans un premier temps, écrivez les tests unitaires permettant de tester les deux méthodes.

Ensuite trouver une implémentation permettant de faire cette abstraction.

### 6.2 agios et intérêts

Sur la classe banque on souhaite ajouter deux méthodes permettant de calculer les agios d'un compte ou les intérêts.

Généralement, ils sont calculés à partir d'un pourcentage. Celui-ci peut varier en fonction de la formule de compte souscrite.

Il existe 2 formules de compte bancaire :

- formule 1 : agios 0,5%, intérêt : 0,75%
- formule 2 : agios 0,2%, intérêt : 0,10%

Chaque valeur décrite ci-dessus est fixée et n'a pas vocation à changer régulièrement.

Nous allons commencer pas déclarer une fonction permettant d'appliquer un pourcentage sur une valeur.
En écrivant cette fonction, il faut tenir compte que les taux ne changeront pas dans le temps.

Créons les fonctions pour chaque formule (en prenant soit de fixer le taux)

Ensuite écrivez les deux fonctions agios et intérêt en utilisant la fonction de calcul de pourcentage.

Pour vérifier votre implémentation, écrivez les tests unitaires permettant de valider de code produit ci-dessus.
