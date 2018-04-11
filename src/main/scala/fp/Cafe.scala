package fp


case class CreditCard()

case class Coffee() {
  def price = {
    10d
  }
}

case class Charge(cc: CreditCard, price: Double) {

  def combine(other: Charge): Charge = {
    if (cc == other.cc) {
      Charge(cc, price + other.price)
    } else {
      throw new Exception("Can't combine charges to different credit cards.")
    }
  }

}

class Cafe {

  def buyCoffee(cc: CreditCard) :(Coffee, Charge) = {
    val cup = Coffee()
    (cup, Charge(cc, cup.price))
  }

  def buyCoffees(cc: CreditCard, n: Int): (List[Coffee], Charge) = {

    val purchases: List[(Coffee, Charge)] = List.fill(n)(buyCoffee(cc))
    val (coffees, charges) = purchases.unzip
    (coffees, charges.reduce((c1, c2) => c1.combine(c2)))
  }

}

object Cafe extends App {
  val cafe = new Cafe()

  cafe.buyCoffees(CreditCard(), 10)
}
