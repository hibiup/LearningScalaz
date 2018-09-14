package LearningScalaz

import scalaz._
import Scalaz._

package day_3 {
    object Test_Tagged {
        def apply() {
            /**
              * Tagged 类型定义
              *
                type Tagged[U] = { type Tag = U }
                type @@[T, U] = T with Tagged[U]
              *
              */
            sealed trait KiloGram
            def KiloGram[A](a: A): A @@ KiloGram = Tag[A, KiloGram](a)  // “A @@ KiloGram” 是中缀表达式(infix)，等价于 “@@[A, KiloGram]”

            val mass = KiloGram(20.0)
            val t = 2 * Tag.unwrap(mass)
            println(t)
        }
    }
}
