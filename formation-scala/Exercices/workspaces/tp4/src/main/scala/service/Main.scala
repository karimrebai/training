package service

object Main extends App{

  def search(i: Int) = (Tp4Service.annuaire.get(i) match {
    case Some(s) => s"Personne trouvée  s"
    case None => "aucune correspondance trouvée"
  })

  println(search(1))
  println(search(10))
  println(search(1000))

}
