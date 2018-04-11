package fp.chapter3

sealed trait Tree[+A]

case class Leaf[A](value: A) extends Tree[A]

case class Branch[A](l: Tree[A], r: Tree[A]) extends Tree[A]

object TreeTest extends App {

  def size[A](t: Tree[A]): Int = t match {
    case Leaf(_) => 1
    case Branch(l, r) => size(l) + size(r) + 1
  }

  def maximum(t: Tree[Int], max:(Int, Int) => Int): Int = t match {
    case Leaf(h) => h
    case Branch(l, r) => max(maximum(l, max), maximum(r, max))
  }

  def depth[A](t: Tree[A]): Int = t match {
    case Leaf(_) => 1
    case Branch(l, r) => math.max(depth(l), depth(r)) + 1
  }

  def map[A, B](t: Tree[A], f:(A)=>B): Tree[B] = t match {
    case Leaf(v) => Leaf(f(v))
    case Branch(l, r) => Branch(map(l, f), map(r, f))
  }

  def fold[A,B](t: Tree[A])(f: A => B)(g: (B,B) => B): B = t match {
    case Leaf(h) => f(h)
    case Branch(l, r) => g(fold(l)(f)(g), fold(r)(f)(g))
  }

  def mapViaFold[A,B](tree: Tree[A])(f: A => B): Tree[B] = fold(tree)(a => Leaf(f(a)):Tree[B])((x: Tree[B],y)=> Branch(x, y))

  def sizeViaFold[A](tree: Tree[A]): Int = fold(tree)(_ => 1)(_ + _ + 1)

  def depthViaFold[A](tree: Tree[A]): Int = fold(tree)(_ => 1)((x,y)=> x max y + 1)

  def maximumViaFold(tree: Tree[Int]): Int = fold(tree)(identity)(_ max _)

  val t: Tree[Int] = Branch(Leaf(1), Branch(Leaf(3), Leaf(4)))

  println(maximumViaFold(t))

}
