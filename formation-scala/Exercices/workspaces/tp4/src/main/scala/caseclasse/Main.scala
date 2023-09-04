package caseclasse

object Main extends App {

  case class Humain(nom:String, prenom:String, age:Int)

  def  isMajeur(h:Humain):Boolean = h match {
    case Humain(_,_, a) if a>=18 => true
    case _ => false
  }

  def  estDeLaFamilleDupont(h:Humain):Boolean = h match {
    case Humain("Dupont",_,_)   => true
    case _ => false
  }

}
