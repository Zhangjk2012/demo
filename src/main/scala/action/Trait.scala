package action

//abstract class Pet(name: String) {
//  val greeting: String
//  var age: Int
//  def sayHello { println(greeting)}
//
//  override def toString: String = s"I say $greeting, and I'm $age"
//}
//
//class Dog(name: String) extends Pet(name) {
//  val greeting: String = "Woof"
//  var age: Int = 2
//}
//
//class Cat(name: String) extends Pet(name) {
//  override val greeting: String = "Meow"
//  override var age: Int = 5
//}


//abstract class Animal(name: String) {
//  var greeting = "Hello"
//  var age = 0
//
//  override def toString: String = s"I say $greeting, and I'm $age years old."
//}
//
//class Dog1(name: String) extends Animal(name) {
//  greeting = {
//    "Woof"
//  }
//  age = 2
//
//  def testDefaultParam(timeout: Int = 5000, protocol: String): Unit = {
//
//  }
//
//}
//

trait TestTrait1 {
  def test = {println("========")}
}

trait TestTrait2 extends TestTrait1{
  override def test = {
    println("++++++++++")
  }
}

class TestTrait extends TestTrait1 with TestTrait2 {
}

object AbstractFieldsDemo extends App {
  val plusOne = (i: Int) => {
    println(i)
  }
}
