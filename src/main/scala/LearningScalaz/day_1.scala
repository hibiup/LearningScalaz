package LearningScalaz

import scalaz._
import Scalaz._

package day_1 {
    object test_Equal {
        /**
          * Scalaz equivalent for the Eq typeclass is called Equal */
        def apply() {

            // 相当于 1 == 1
            assert(1 === 1)
            assert(1 == 1)

            // 相当于 !=
            assert(1 =/= 2 && true)
            assert(1 != 2 && true)
        }
    }

    object test_Ord {
        /** Scalaz equivalent for the Ord typeclass is Order
          * */
        def apply(): Unit = {
            /** Order enables ?|? syntax which returns Ordering: LT, GT, and EQ */
            println(1.0 ?|? 2.0)  // 输出 "LT"，表示 左边 "Lower Then" 右边

            /** It also enables lt, gt, lte, gte, min, and max operators */
            println(1.0 lt 2.0)   // true
            println(1.0 max 2.0)  // 2.0
        }
    }

    object test_Show {
        /** Scalaz equivalent for the Show typeclass is Show: */
        def apply(): Unit = {
            "hello".println      // 等价于 println("hello")
            3.show.println       // scalaz.Cord  3  // Cord apparently is a purely functional data structure for potentially long Strings.
            3.shows.println      // String 3
        }
    }

    object test_Enum {
        def apply(): Unit = {
            ('a' |-> 'e')   // Instead of "to"
                    .map(println)

            (2 |=> 5)   // Instead of "to"
                    .map(println)

            println('B'.succ)  // 'C'
        }
    }

    /**
      * 以下这个例子很精巧！Scalaz 的 Monad 大多遵循这个例子的过程生成隐式对象。特别是第 1 到第 4 步包含的逻辑。
      * */
    object test_YesNo {
        trait CanTruthy[A] { self =>
            /** @return true, if `a` is truthy. */
            def truthy(a: A): Boolean
        }
        object CanTruthy {
            /** 在 CanTruthy 被调用时，直接返回当前调用环境里面提供的那个隐式实例 */
            def apply[A](implicit ev: CanTruthy[A]): CanTruthy[A] = ev

            /**
              * 以下过程的逻辑：
              *
              * 1）object CanTruthy 不是来自 trait CanTruthy，它们只是同名同构。(虽然可以 extends 自 trait CanTruthy，但是不是必须的)
              *
              * 2）object CanTruthy 的 truthys 方法是一个工厂方法，它生成一个真正的 trait CanTruthy 实例。
              *
              * 3) 新的 trait CanTruthy 实例不同于 object CanTruthy！它的 truthys 方法也不是工厂方法。
              *
              * 4）新的 trait CanTruthy 实例的 truthys 方法调用来自工厂方法的参数 f = {...} 用于自己的参数的处理。
              *
              * */
            def truthy[A](f: A => Boolean): CanTruthy[A] = new CanTruthy[A] {
                def truthy(a: A): Boolean = f(a)
            }
        }

        def apply(): Unit = {
            /**
              * 5) 在当前环境中调用 object CanTruthy 实例，生成 trait CanTruthy 的隐式实例。并经由 object CanTruthy
              * 的 trythys 工厂方法将业务逻辑作为函数参数 f = {...} 传递给新的 trait CanTruthy 实例的 trythy 方法。
              * 至此，系统中已经存在了一个能够用于执行 truthy 方法的 trait CanTruthy 实例。*/
            implicit val intCanTruthy: CanTruthy[Int] = CanTruthy.truthy({
                case 0 => false
                case _ => true
            })

            // 测试方法：隐式传递第 5 步生成的 trait CanTruthy 实例。然后执行第 6 步
            truthyInteger
        }

        trait CanTruthyOps[A] {
            def self: A                   // 这个 self 是要转换的对象，并不是 CanTruthyOps 实例自己。
            implicit def F: CanTruthy[A]  // 这个 F 变量定义成 implicit 似乎没有什么意义

            /**
              * 8）trait CanTruthyOps 具有另一个 truthy 方法，而这个方法又调用 CanTruthy.truthy 方法由 object CanTruthy
              * 生成的 trait CanTruthy 实例在这里被调用。*/
            final def truthy: Boolean = F.truthy(self)
        }

        implicit def toCanTruthyOps[A](v: A)(implicit ev: CanTruthy[A]) =
            /**
              * 7) 生成一个 CanTruthyOps 实例，并将隐式转换目标和 trait CanTruthy 隐式实例传入。*/
            new CanTruthyOps[A] {
                def self = v
                implicit def F: CanTruthy[A] = ev
            }

        def truthyInteger(implicit intCanTruthy: CanTruthy[Int]): Unit = {
            /**
              * 6) 隐式触发上面的 toCanIsTruthyOps 方法（第 7 步），并将 v=10， ev=intCanTruthy 作为参数传入 这个 intCanTruthy 就是
              * 第 5 步生成并经由参数传入的隐式 trait CanTruthy 实例 */
            println(10.truthy)    // 10) 调用 trait CanTruthyOps 的 truthy 方法，间接调用 trait CanTruthy 的 truthy 方法.s
        }

    }

}
