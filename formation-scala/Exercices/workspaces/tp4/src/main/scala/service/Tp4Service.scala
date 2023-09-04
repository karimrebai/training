package service

object Tp4Service {

  val annuaire: Map[Int, String] = Map(1 -> "Louis", 2 -> "Paul", 5 -> "Patrick", 9 -> "Leo", 4 -> "Cedric", 3 -> "Martin", 19 -> "Jean")

  def giveYourChoice(valeur: Int): String = {

    valeur match{
      case 1 =>  "Tout est OK"
      case 2|4 => "Attention à la marche"
      case 10 =>  "10 est un bon choix"
      case _ =>  "la valeur saisie n'est pas reconnue"
    }

  }

}
