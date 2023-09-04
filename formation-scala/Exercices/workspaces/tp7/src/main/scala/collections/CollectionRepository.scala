package collections


object CollectionRepository {

  case class Person(nom: String, age: Int, ville: String)
  case class Eleve(nom: String, prenom:String)

  def getNoteEleves(): List[List[Double]] = List(List(5, 15, 12, 3), List(15, 11, 12, 9), List(14, 16, 13, 9), List(6, 4, 10, 7), List(15, 9, 11, 7))
  def getEleves(): List[Eleve] = List(Eleve("Harry", "Cover"), Eleve("Louis", "LaBraocant"), Eleve("Michel","Tartuf"), Eleve("Andre","Bat"), Eleve("Jean","Batiste"))

  def getBigText(): String = "Suis prudens et liberis liberis frugi retinacula suis frugi gentium permisit frugi Caesaribus latasque regenda iura superbas gentium superbas gentium latasque post sempiterna superbas velut cervices superbas velut patrimonii tamquam tamquam patrimonii suis venerabilis leges urbs tamquam libertatis liberis regenda patrimonii leges dives velut permisit et parens frugi superbas frugi et fundamenta leges Caesaribus frugi efferatarum fundamenta iura gentium efferatarum liberis sempiterna velut et permisit latasque tamquam efferatarum liberis prudens urbs superbas parens efferatarum patrimonii Caesaribus iura venerabilis retinacula velut parens sempiterna Caesaribus et fundamenta cervices liberis fundamenta sempiterna frugi leges latasque velut iura liberis et suis urbs tamquam latasque.Est eventu pendentem haec textum est memorabile mirum nihil memorabile agi videre ergo videre plebem pendentem mirum agi similiaque plebem ergo permittunt quodam eventu haec ardore similiaque plebem videre vel ergo videre serium innumeram nihil nihil mentibus est ardore innumeram mentibus ardore pendentem admodum redeundum innumeram pendentem plebem ergo curulium memorabile ad innumeram plebem est videre vel nihil agi admodum similiaque agi redeundum agi permittunt quodam plebem agi mentibus memorabile Romae vel Romae ardore plebem textum redeundum nihil permittunt similiaque haec Romae videre permittunt memorabile eventu Romae haec nihil infuso mirum pendentem nihil nihil admodum Romae cum agi ardore eventu."

  def personFactory(): List[Person] = List(Person("Martin", 10, "Paris"), Person("Michel", 18, "Lyon")
    , Person("Patrick", 28, "Lille"), Person("Fabrice", 47, "Lille"), Person("Odile", 24, "Lille")
    , Person("Olivier", 21, "Nantes"), Person("Stéphanie", 15, "Reims")
    , Person("géraldine", 21, "Turin"), Person("Stéphane", 45, "Saint-Etienne")
    , Person("Remi", 5, "Lille"), Person("Nicolas", 31, "Lille"), Person("Severine", 27, "Lille"), Person("Romain", 12, "Lille")
    , Person("Nizard", 5, "Paris"), Person("Etienne", 32, "Loix"), Person("Mike", 24, "Bordeaux"), Person("Louis", 19, "Bordeaux")
    , Person("Akram", 29, "Limoge"), Person("Sebastien", 30, "Nice"), Person("Franck", 23, "Orange"), Person("Micheline", 54, "Puteau"))

  def getStringNumber():List[String] = List("5", "15", "12", "3","100","23","1","32")
}
