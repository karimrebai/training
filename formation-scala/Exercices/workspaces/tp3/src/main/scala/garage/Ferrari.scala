package garage

class Ferrari(nbPorte: Int, val couleur: String, var nbPassager: Int) {

  def this(couleur: String)
  {
    this(2, couleur, 2)
  }


  def canEqual(other: Any): Boolean = other.isInstanceOf[Ferrari]

  override def equals(other: Any): Boolean = other match {
    case that: Ferrari =>
      (that canEqual this) &&
        couleur == that.couleur &&
        nbPassager == that.nbPassager
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(couleur, nbPassager)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}
