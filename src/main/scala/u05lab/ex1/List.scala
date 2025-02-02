package u05lab.ex1

import u05lab.ex1.List

import scala.annotation.tailrec

// Ex 1. implement the missing methods both with recursion or with using fold, map, flatMap, and filters
// List as a pure interface
enum List[A]:
  case ::(h: A, t: List[A])
  case Nil()
  def ::(h: A): List[A] = List.::(h, this)

  def head: Option[A] = this match
    case h :: t => Some(h)
    case _ => None

  def tail: Option[List[A]] = this match
    case h :: t => Some(t)
    case _ => None

  def append(list: List[A]): List[A] = this match
    case h :: t => h :: t.append(list)
    case _ => list

  def foreach(consumer: A => Unit): Unit = this match
    case h :: t => consumer(h); t.foreach(consumer)
    case _ =>

  def get(pos: Int): Option[A] = this match
    case h :: t if pos == 0 => Some(h)
    case h :: t if pos > 0 => t.get(pos - 1)
    case _ => None

  def filter(predicate: A => Boolean): List[A] = this match
    case h :: t if predicate(h) => h :: t.filter(predicate)
    case _ :: t => t.filter(predicate)
    case _ => Nil()

  def map[B](fun: A => B): List[B] = this match
    case h :: t => fun(h) :: t.map(fun)
    case _ => Nil()

  def flatMap[B](f: A => List[B]): List[B] =
    foldRight[List[B]](Nil())(f(_) append _)

  def foldLeft[B](z: B)(op: (B, A) => B): B = this match
    case h :: t => t.foldLeft(op(z, h))(op)
    case Nil() => z

  def foldRight[B](z: B)(f: (A, B) => B): B = this match
    case h :: t => f(h, t.foldRight(z)(f))
    case _ => z

  def length: Int = foldLeft(0)((l, _) => l + 1)

  def isEmpty: Boolean = this match
    case Nil() => true
    case _ => false

  def reverse(): List[A] = foldLeft[List[A]](Nil())((l, e) => e :: l)

  /** EXERCISES */
  def zipRight: List[(A, Int)] =
    var l: List[(A, Int)] = Nil()
    for i <- 0 until length do l = (get(i).get, i) :: l
    l.reverse()

  def zipRightWithFold: List[(A, Int)] =
    foldLeft((Nil[(A, Int)](), 0))((tuple, e) => (tuple._1 append List((e, tuple._2)), tuple._2+1))._1

  def zipRightWithMap: List[(A, Int)] =
    var c = -1
    def createTuple(e: A): (A, Int) =
      c = c + 1
      (e, c)
    map(createTuple)

  def partition(pred: A => Boolean): (List[A], List[A]) =
    (this.filter(pred), this.filter(!pred(_)))

  def partitionWithFold(pred: A => Boolean): (List[A], List[A]) =
    foldRight((Nil(), Nil()))((e, tuple) => if pred(e) then (e :: tuple._1, tuple._2) else (tuple._1, e :: tuple._2))

  def span(pred: A => Boolean): (List[A], List[A]) =
    @tailrec
    def _span(l: List[A], temp: List[A]): (List[A], List[A]) = l match
      case h :: t if pred(h) => _span(t, temp append List(h))
      case h :: t => (temp, h :: t)
      case _ => (this, Nil())

    _span(this, Nil())

  def spanWithFold(pred: A => Boolean): (List[A], List[A]) =
    var flag = true
    def foldElementInTuple(tuple: (List[A], List[A]), e: A): (List[A], List[A]) = tuple match
      case (tr, fa) if flag && pred(e) => (tr append List(e), fa)
      case (tr, fa) => flag = false; (tr, fa append List(e))

    foldLeft((Nil(), Nil()))(foldElementInTuple)

  /** @throws UnsupportedOperationException if the list is empty */
  def reduce(op: (A, A) => A): A = this match
    case h :: t => t.foldLeft(h)(op)
    case Nil() => throw UnsupportedOperationException()

  def takeRight(n: Int): List[A] =
    reverse().zipRight.filter((_,c) => c < n).map((e,_) => e).reverse()

  def takeRightWithNoReverse(n: Int): List[A] =
    zipRight.filter((_,c) => c > length - n - 1).map((e,_) => e)

  def takeRightWithCollect(n: Int): List[A] =
    val f: PartialFunction[(A, Int), A] = {
      case (e, c) if c > length - n - 1 => e
    }
    zipRight.collect(f)

  def collect[B](f: PartialFunction[A, B]): List[B] = this match
    case h :: t if f.isDefinedAt(h) => f(h) :: t.collect(f)
    case h :: t => t.collect(f)
    case _ => Nil()

  def collectWithFold[B](f: PartialFunction[A, B]): List[B] =
    foldRight(Nil())((e, l) => if f.isDefinedAt(e) then f(e) :: l else l)

// Factories
object List:

  def apply[A](elems: A*): List[A] =
    var list: List[A] = Nil()
    for e <- elems.reverse do list = e :: list
    list

  def of[A](elem: A, n: Int): List[A] =
    if n == 0 then Nil() else elem :: of(elem, n - 1)

@main def checkBehaviour(): Unit =
  val reference = List(1, 2, 3, 4)
  println(reference.zipRight) // List((1, 0), (2, 1), (3, 2), (4, 3))
  println(reference.partition(_ % 2 == 0)) // (List(2, 4), List(1, 3))
  println(reference.span(_ % 2 != 0)) // (List(1), List(2, 3, 4))
  println(reference.span(_ < 3)) // (List(1, 2), List(3, 4))
  println(reference.reduce(_ + _)) // 10
  try Nil.reduce[Int](_ + _)
  catch case ex: Exception => println(ex) // prints exception
  println(List(10).reduce(_ + _)) // 10
  println(reference.takeRight(3)) // List(2, 3, 4)
