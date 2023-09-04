# Scala & Java

<!-- .slide: class="page-title" -->



## Scala & Java : Interaction entre les 2 langages

- En théorie, c'est assez simple
- Bytecode oblige :
  - Utiliser du Scala depuis du Java est assez naturel
  - Implémentation d'interfaces Java en Scala
  - Utilisation de librairies Java depuis Scala est courante
- Cependant il faut rester vigilant sur certains points



## Scala & Java : Gestion des collections Java en Scala

- Depuis du code Scala, utilisation d'une API Java renvoyant une collection Java.
- Solutions : plusieurs possibilités existent
  - Le projet *scalaj-collection* disponible à l'url suivante https://github.com/scalaj/scalaj-collection
  - Utiliser la classe utilitaire de conversions intégrée dans les versions récentes de Scala `scala.collection.JavaConversions`
  - Autre possibilité via une autre classe helper `JavaConverters` dans le même package



## Scala & Java : Gestion de Scala en Java

- Utilisation de code Scala dans une classe Java :
- Généralement, pas de problème particulier
- Quelques cas assez rares peuvent poser problème...
  - Se reporter à cette entrée de la FAQ pour plus de détails
- http://www.scala-lang.org/faq/4



## Scala & Java : Gestion de Scala en Java

- Il faut être vigilant dans le cas où le code Scala manipulé utilise certaines fonctionnalités avancées
  - Généricité
  - Traits
  - Types abstraits
- **L'arme absolue** dans un tel cas de figure :
  - `javap`
  - Donne la vue bytecode de votre code Scala et permet de choisir la bonne façon de faire votre appel.
