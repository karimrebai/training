package finance

object Banque {

  case class Compte(solde: Double)

  def debit(cpt: Compte, montant: Double): Compte = Compte(cpt.solde - montant)

  def credit(cpt: Compte, montant: Double): Compte = Compte(cpt.solde + montant)

  def operation(f: (Double, Double) => Double, cpt: Compte, mnt: Double) = Compte(f(cpt.solde, mnt))

  def percent(per: Double)(mnt: Double) = (mnt * per) / 100

  def agios(f: Double => Double, cpt: Compte): Compte = {
    Compte(cpt.solde - f(cpt.solde))
  }

  def interets(f: Double => Double, cpt: Compte): Compte = {
    Compte(cpt.solde + f(cpt.solde))
  }

}
