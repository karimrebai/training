package actor.formation.scala.pingpong


object Main extends App {

  def getVal(l:List[String], idx:Int):String = {

    try {
      //Try(l(idx))
      l(idx)
    }catch{

     case e:IndexOutOfBoundsException => "Erreur"
    }
  }

  println(if("Erreur" == getVal(List("Fab", "Geraldine"), 10)) {
    println("Une erreur c'est produite!")
  } else {
    println("Yes ok")

  }) //faire un test

  // println(getVal(List("Fab", "Geraldine"), 1).map(s => s).getOrElse("Errur"))

}
