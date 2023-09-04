package garage

object Main extends App {

  val f1 = new Ferrari("bleu")
  val f2 = new Ferrari("bleu")

  val v1 = new VTT with Vehicule

  println(f1.eq(f2))
  println(f1.ne(f2))
  println(f1 == f2)

  println(v1.rouler())

}
