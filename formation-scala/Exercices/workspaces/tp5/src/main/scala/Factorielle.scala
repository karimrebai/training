import scala.annotation.tailrec

object Factorielle extends App {

  def factRecNoTerm(n: Int): Int =
    if (n == 0) 1
    else n * factRecNoTerm(n - 1)

  println(factRecNoTerm(10))

  @tailrec
  def factRecTerm(n: Int, acc: Int): Int =
    if (n == 0) acc
    else factRecTerm(n - 1, n * acc)

  println(factRecTerm(10, 1))

}


