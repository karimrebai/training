<div class="pb"></div>

## TP 9 : Actors

Dans ce TP nous allons coder un dialogue entre deux acteurs. Le principe général sera une échange à la manière d'une partie de ping-pong.

Les deux acteurs échangeront plusieurs aller-retour de messages ; au bout d'un certain nombre le dialogue s'arrêtera.

Pour mettre en place cette application, nous allons procéder pas à pas.

### avant propos

Il faudra ajouter la dépendance sur la librairie actors :`"com.typesafe.akka" % "akka-actor_2.10" % "2.3.14"` dans le descripteur de build (`build.sbt`).

Le projet aura comme racine de package : `actor.formation.scala`. A chaque étape sera précisé le sous package à créer pour les nouveaux composants.


### 9.1 Les messages échangés

Nous allons commencer par mettre en place un module (object) qui contiendra l'ensemble des messages échangés par les deux parties.

Dans le package `pingpong`créer une classe object nommée `msg`. Dans ce module ajouter 4 messages sous forme de `case object` :

Les messages sont :

- `InitMsg`
- `Ping`
- `Pong`
- `HaltMsg`


### 9.2 Actor Pong

La classe Actor Pong sera créée dans le package `actors`.

Nous allons implémenter l'actor `Pong`. Cet actor est à l'écoute du type de message `PingMsg`.

Lors de la réception d'un message de ce type il devra :

- Incrémenter un compteur.
- Selon la valeur du compteur :
  - compteur < 3 : réponse au `sender` le message `PongMsg`
  - compteur >= 3 : réponse au `sender` le message `HaltMsg`
- Si le message reçu par l'acteur Pong, ne fait pas partie de la fonction partielle `receive` alors on affichera le message : `message inconnu`

* note : rajouter des messages. Ils seront utile à l'exécution pour tracer les échanges.*


### 9.2 Actor Ping

La classe Actor `Ping` sera créée dans le package `actors`.

Nous allons implémenter l'actor `Ping`. Cet actor est à l'écoute du type de message `InitMsg`, `PongMsg`.

- Lors de la réception d'un message de type `InitMsg`, il répondra par le renvoi d'un message `PingMsg`
- Lors de la réception d'un message de type `PongMsg`, il répondra par le renvoi d'un message `PingMsg`
- Lors de la réception d'un message de type `HaltMsg`, il arrêta l'actor

* note : rajouter des messages. Ils seront utile à l'exécution pour tracer les échanges.*


### 9.3 Classe Main : Que le partie commence !

Maintenant que nous avons tous les actors et messages, nous pouvons commencer la partie de ping pong.

Pour cela, il fait créer une classe `Main`.

Dans cette classe, il faudra d'abord créer un système d'acteurs.

Ensuite créer une référence sur l'acteur `Pong` puis une référence sur l'acteur `Ping` en injectant la référence précédemment crée.

Et enfin lancer la partie en envoyant un message d'initialisation à l'acteur `Ping`

Une fois l'implémentation terminée, lancer le programme et observer les logs.
