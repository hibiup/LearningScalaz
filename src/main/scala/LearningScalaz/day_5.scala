package LearningScalaz

import scalaz._
import Scalaz._

package day_5 {
    object Test_Point {
        /**
          * Scalaz 的 Monad 扩展自 Applicative 和 Bind
          *
            trait Monad[F[_]] extends Applicative[F] with Bind[F] { self =>
              ...
            }
          */
        def apply(): Unit = {
            /** Monad 的 point 用于生成一个 Monoid */
            val m1 = Monad[Option].point(2)
            println(m1)  // Some(2)

            val m2 = Monad[Option].point("Hello!")
            println(m2)  // Some("Hello!")
        }
    }

    object Test_Bind {
         /** Bind 扩展自 Apply:
          *
            trait Bind[F[_]] extends Apply[F] { self =>
              def bind[A, B](fa: F[A])(f: A => F[B]): F[B]
            }
          *
          * Bind 定义了一系列操作（Ops）：
          *
            trait BindOps[F[_],A] extends Ops[F[A]] {
              implicit def F: Bind[F]
              ...
              def flatMap[B](f: A => F[B]) = F.bind(self)(f)
              // 以下等同于 flatMap
              ...
              def join[B](implicit ev: A <~< F[B]): F[B] = F.bind(self)(ev(_))
              // 以下等同于 join
              ...s
            }
          * 最关键的是 flatMap 和 join
          *
          * */
         def apply() = {
             val s1 = 3.some flatMap { x => (x + 2).some }  // 用隐式得到 Some
             println(s1)  // Some(5)

             val s2 = 3.some flatMap { x => Monad[Option].point(x + 2) }   // 等同于用 Moind 的 point 生成 Some
             println(s2)  // Some(5)

             val s0 = (none: Option[Int]) flatMap { x => Monad[Option].point(x * 10) }  // none会被自动过滤掉
             println(s0)  // None
         }
    }

    /**
      * 一只天平， 假设 Pierre 站在左边，右边的鸟如果在 3只以内那么天平将保持平衡，如果 Pierre 这边加了一支鸟，那么右边也要添加一支才能继续保持平衡
      * 否则 Pierre 就会掉下去。
      *
      * 或者直接给 Pierre 丢一根香蕉让他划下去。
      *
      * */
    object PoleSample {
        type Birds = Int   // 定义鸟

        case class Pole(left: Birds, right: Birds) {    // 定义天平
            /** Pole 的返回值必须是 Monoid 类型 比如 Option，才可以执行 Monad 运算。 */
            def landLeft(n: Birds):  Option[Pole] = {   // landLeft 函数修改左边的值
                if ((left +n) - right < 4)
                    Some(copy(left = left + n))         // copy 是 Scala case class 内建函数，相当于 clone，但是它可以只给出部分参数。
                else None
            }

            def landRight(n: Birds):  Option[Pole] = {
                if ((right + n) - left < 4)
                    Some(copy(right = right + n))       // copy 是 Scala case class 内建函数，相当于 clone，但是它可以只给出部分参数。
                else None
            }

            def banana: Option[Pole] = none[Pole]       // 香蕉，一根 Option[Pole] 类型的 None
        }

        def apply(): Unit = {
            assert(Some(Pole(2,1)) == (Pole(0, 0).landRight(1) flatMap {_.landLeft(2)}))  // 2:1  平衡得到 Pole(2,1)
            assert(None == (Pole(0, 3).landLeft(10)))   // 10：3 失衡得到 None

            /**
              * 之所以要为 Pole 定义 banana 是因为类型存在到达的路径问题，Monad[T] 接受的类型参数是当前类型，也就是 T: Option[Pole]。
              * 如果将下面的 _.banana 替换成 none[Pole] 那么在这个位子上会得到 Option[PoleSample.Pole]。在Scala 类型系统看来
              * PoleSample.Pole 类型不等于 Pole 类型
              * */
            println(Pole(0, 0).landLeft(1) flatMap {_.banana})  // 丢香蕉。 >>= 是 flatMap 的 alias

            /** 可以玩语法糖了 */
            def routine: Option[Pole] =
                for {
                    start <- Monad[Option].point(Pole(0, 0))    // Some(Pole(0,0))
                    first <- start.landLeft(2)
                    second <- first.landRight(2)
                    third <- second.landLeft(1)
                } yield third
            println(routine)  //Some(Pole(3,2))
        }
    }
}
