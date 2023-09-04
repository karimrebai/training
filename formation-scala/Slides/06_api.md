# Les principales API

<!-- .slide: class="page-title" -->



## Sommaire

- Entrées/Sorties
- Gestion du XML



## Entrées/Sorties - Généralités

- Très simples et efficaces
- Basées sur certaines classes du JDK
  - Apportent de la concision et de l'efficience dans le développement
- Notamment la classe `Source`



## Entrées/Sorties - Lecture d'un fichier

- Utilisation de la classe **Source**

```scala
println("Enter a path to the file to be read")

val path = Console.readLine()

Source.fromFile(path).getLines()
  .foreach{ x:String => println("line read " + x) }
```

- En une ligne, on extrait l'ensemble de ligne d'un fichier et on les affiche
- Pas d'exceptions obligatoires
- Certaines classes helper comme `Source` allègent le travail



## Gestion du XML - Généralités

- Support natif
- Pas besoin de librairie pour lire/manipuler du XML
- Un simple import et une syntaxe *XPath* suffisent



## Gestion du XML - Exemple

- Soit le fichier XML suivant :

```scala
val xmlAsString =
"""
<library>
  <collection name="Computers">
    <book name="Scala for the Impatient" author="" />|
    <book name="" author="Donald Knutz"/>
  </collection>
  <collection name="heroic fantasy">
    <book name="Elric of Melnibony" author=""/>
    <book name="Call of Cthulu" author=""/>
  </collection>
  <collection name="fun">
    <book name="H2G2" author="Crazy English Guy"/>
  </collection>
</library>
"""
```



## Gestion du XML - Exemple

- Inspection du fichier à partir du parser Scala

```scala
val xml = XML.loadString(xmlAsString)

 // extracting firt level of the DOM document
def collection = xml \ "collection"
println("collection found ? " + collection)

// extracting second level - first try
val books1 = xml \ "book"
println("Books found ? "+ books1)

// second try - recursive search
val books2 = xml \\ "book"
println("Books found ? "+ books2)

for (book <- xml \\ "book" if ((book \ "@author") .text=="")) println("Unknown author book is = " + book )
```

- Code concis et efficace



## TP8 : IO & XML

<!-- .slide: class="page-tp8" -->
