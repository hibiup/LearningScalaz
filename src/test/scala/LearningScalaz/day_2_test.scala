package LearningScalaz

import org.scalatest.FlatSpec

import LearningScalaz.day_2._

class day_2_test extends FlatSpec{
    "Functor with Function" should "" in {
        test_Functor_with_Function()
        test_Functor_with_List()
    }

    "Applicative" should "" in{
        test_Applicative_typeclass()
    }
}
