package LearningScalaz

/**
  * day_1 最后一个 test_YesNo 的例子展示了如何给任意对象加上 truthy 方法。Scalaz 用同样的方法给对象加上 map 方法来实现 Monad 。
  *
  * 以下是 Scalaz 的 Functor trait 的定义：
  *
    trait Functor[F[_]]  { self =>
      def map[A, B](fa: F[A])(f: A => B): F[B]
      ...
    }

    trait FunctorOps[F[_],A] extends Ops[F[A]] {
      implicit def F: Functor[F]
      import Leibniz.===
      ...
      final def map[B](f: A => B): F[B] = F.map(self)(f)
      ...
    }
  *
  * */

import scalaz._
import Scalaz._

package day_2 {
    /** 这个 map 方法就是由 Scalaz 用前例的方式加上的，并且它甚至可以作用于函数。*/
    object test_Functor_with_Function {
        def apply(): Unit = {
            val f1 = ((x: Int) => x + 1)map{_ * 7}
            println(f1(3))  // x=3 => (3+1)*7 = 28

            /** 注意，后面的 2 是 前面的 (_: Int) * 3) 的参数，因此执行在 map 之前。*/
            val f2 = (((_: Int) * 3) map {_ + 100})(2)
            println(f2)     // _=2 => (2*3)+100=106
        }
    }

    /** 也可以作用于其他类型 */
    object test_Functor_with_List {
        def apply(): Unit = {
            val res = List(1, 2, 3) map {(_:Int) * (_:Int)}.curried
            println(res map {_(9)} )
        }
    }
}
