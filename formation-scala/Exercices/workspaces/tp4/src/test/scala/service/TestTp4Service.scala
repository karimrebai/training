package service

import org.scalatest.FunSuite

class TestTp4Service extends FunSuite {

  test("valeur == 1 should return 'Tout est OK'") {
    assert(Tp4Service.giveYourChoice(1) == "Tout est OK")
  }

  test("valeur == 2 | 4 should return 'Attention à la marche'") {
    assert(Tp4Service.giveYourChoice(2) == "Attention à la marche")
    assert(Tp4Service.giveYourChoice(4) == "Attention à la marche")
  }

  test("valeur == 10 should return '10 est un bon choix'") {
    assert(Tp4Service.giveYourChoice(10) == "10 est un bon choix")
  }

  test("valeur out of range [1,2,4,10] should return 'la valeur saisie n'est pas reconnue'") {
    assert(Tp4Service.giveYourChoice(5) == "la valeur saisie n'est pas reconnue")
    assert(Tp4Service.giveYourChoice(9) == "la valeur saisie n'est pas reconnue")
    assert(Tp4Service.giveYourChoice(3) == "la valeur saisie n'est pas reconnue")
  }

}
