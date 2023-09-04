package banque

import banque.devise.{DeviseEuro, DeviseUsa}

object convertisseur {

  implicit def Euro2Dollar(deviseUsa: DeviseUsa):DeviseEuro = new DeviseEuro(deviseUsa.montant)

}
