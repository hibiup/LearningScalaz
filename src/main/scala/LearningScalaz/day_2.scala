package LearningScalaz

/**
  * day_1 最后一个 test_YesNo 的例子展示了如何给任意对象加上 truthy 方法。Scalaz 用同样的方法给对象加上 map 方法来实现 Monad 。
  *
  * */

import scalaz._
import Scalaz._

package day_2 {
    /**
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
      *  这个 map 方法就是由 Scalaz 用类似前例的方式加上的，并且它甚至可以作用于函数：
      * */
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
            /** 如果 map 超过一个以上的参数，那么需要使用 curried，生成 partially applied function */
            val res = List(1, 2, 3) map {(_:Int) + (_:Int)}.curried // 得到 List({1+_},{2+_},{3+_}) Applicative
            /** 赋值给 {x+_} 中的占位副，这里 map 中的 _ 是 {x+_} 部分应用函数本身，*/
            println(res map {_(9)} )                                // 得到：1+9, 2+9, 3+9 = 10, 11, 12
        }
    }

    /**
      * 以下是 Applicative 的定义
      *
        trait Applicative[F[_]] extends Apply[F] { self =>
          def point[A](a: => A): F[A]

          /** alias for `point` */
          def pure[A](a: => A): F[A] = point(a)
          ...
        }
      *
      * Applicative 扩展自 Apply, Apply 扩展自 Functor
      *
        trait Apply[F[_]] extends Functor[F] { self =>
          def ap[A,B](fa: => F[A])(f: => F[A => B]): F[B]
        }
      *
      * Applicative 包含 point 方法，pure 是 point 的别名。它们的作用是将一个东西放进容器中去。Scalaz中喜欢用 point，参数的代表符在数学中是：η("tho")
      */
    object test_Applicative_typeclass {
        def apply(): Unit = {
            assert(List(1) == 1.point[List])

            assert(Some(1) == 1.point[Option])        // 从 Option 得到 Some
            assert(Some(None) == None.point[Option])  // ??

            assert(List(3) == (1.point[List] map {_ + 2}))

            println(9.some)  // 一个便捷地将数据放进 Some 的方法。

            assert(Some(8) == ^(3.some, 5.some) {_ + _})   // ^ 方法将容器内的数据拿出来运算后再装回去。
            assert(None == ^(3.some, none[Int]) {_ + _})   // 如果遇到 none 就返回 None 容器
        }
    }
}
