package banque

import banque.devise.DeviseEuro

class BanqueEuro {

  def versement(devise: DeviseEuro) = s" la somme de $devise.montant à été versée à la banque"

}

object BanqueEuro {
        def apply()= new BanqueEuro
}
