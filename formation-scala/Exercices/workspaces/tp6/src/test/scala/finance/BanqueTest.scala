package finance

import finance.Banque.{Compte, _}
import org.scalatest.FunSuite

class BanqueTest extends FunSuite {

  test("Crédit : 10.0 Solde : 10.0, nouveau solde should be 20.0") {
    val cpt= Compte(20.0)
    assert(Banque.credit(cpt, 10.0) == Compte(30.0))
  }

  test("Débit : 10.0 Solde : 10.0, nouveau solde should be 0.0") {
    val cpt= Compte (20.0)
    assert(Banque.debit(cpt, 10.0) == Compte(10.0))
  }


  test("Débit & credit with operations") {
    val cpt= Compte (20.0)

    def add(a:Double, b:Double) = a+b
    def minus(a:Double, b:Double) = a-b
    assert(Banque.operation(add,cpt, 10.0) == Compte(30.0))
    assert(Banque.operation(minus,cpt, 10.0) == Compte(10.0))
  }

  test("Agios 10% résulte should be " ) {
    val cpt= Compte (20.0)

    def formuleCompte1 = percent(0.1)(_)
    def formuleCompte2 = percent(0.3)(_)

    assert(Banque.agios(formuleCompte1,cpt) == Compte(19.98))
    assert(Banque.agios(formuleCompte2,cpt) == Compte(19.94))
  }

  test("intétêt 10% résulte should be " ) {
    val cpt= Compte (20.0)

    def formuleCompte1 = percent(0.1)(_)
    def formuleCompte2 = percent(0.3)(_)

    assert(Banque.interets(formuleCompte1,cpt) == Compte(20.02))
    assert(Banque.interets(formuleCompte2,cpt) == Compte(20.06))
  }

}
