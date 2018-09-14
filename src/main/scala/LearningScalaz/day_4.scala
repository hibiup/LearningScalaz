package LearningScalaz

import scalaz._
import Scalaz._

package day_4 {
    object BreakingFunctorLaw {
        sealed trait COption[+A] {}
        case class CSome[A](counter: Int, a: A) extends COption[A]
        case object CNone extends COption[Nothing]

        implicit def coptionEqual[A]: Equal[COption[A]] = Equal.equalA

        implicit val coptionFunctor = new Functor[COption] {
            def map[A, B](fa: COption[A])(f: A => B): COption[B] = fa match {
                case CNone => CNone
                case CSome(c, a) => CSome(c + 1, f(a))
            }
        }

        def apply(implicit cp:COption[String]): Unit = {
            (CSome(0, "ho"): COption[String]) map {(_: String) + "ha"}
            (CSome(0, "ho"): COption[String]) map {identity}
        }
    }
}
