<div style="height:24.7cm; position: relative; border: 1px solid black;">
    <h1 style="position:absolute; top: 33%; width:100%; text-align: center;">{Titre-Formation}</h1>
    <h1 style="position:absolute; top: 50%; width:100%; text-align: center;">Travaux Pratiques</h1>
    <img src="ressources/logo-zenika-small.png" style="position: absolute; bottom: 20px; right: 20px; height: 150px;">
</div>

<div class="pb"></div>


## Pré-requis

### Installation

Pour l'ensemble des TPs qui vont suivre il est nécessaire d'installer :

- SBT : [procédure d'installation](http://www.scala-sbt.org/release/tutorial/Setup.html)
- Un IDE au choix :
  - Scala IDE
  - Eclipse
  - Intellij

*Concernant le choix de IDE, il est préférable d'installer la version d'Eclipse customisée pour développer en Scala [Scala IDE](http://scala-ide.org/).*
*Elle met à disposition un environnement configurer et prêt à l'emploi*

Pour valider son environnement, créer dans un répertoire nommé `firstProject` un fichier nommé `Hi.scala`.

Copier le code suivant dedans :

```scala
 object Hw {
   def main(args: Array[String]) =
     println("Hello World")
 }
```

Si tout est correctement configuré, vous devriez obtenir le message suivant :

```
...
[info] Running Hw
Hello World
[success] Total time: 4 s, completed 18 oct. 2015 16:47:26
```
