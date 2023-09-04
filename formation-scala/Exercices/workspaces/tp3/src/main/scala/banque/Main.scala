package banque

import banque.convertisseur._
import banque.devise.{DeviseEuro, DeviseUsa}

object Main extends App {

  val de = new DeviseEuro(100)
  val du = new DeviseUsa(100)

  val bank = BanqueEuro()

  bank.versement(de)
  bank.versement(du)

}
