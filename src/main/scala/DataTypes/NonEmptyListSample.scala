package DataTypes

import cats.data.NonEmptyList

// 要素が一つ必ずあるListのことだよ
// NonEmptyList[+A](head: A, tail: List[A])のことだぞ

// 要素一つだけのリストをつくる
val oneElement = NonEmptyList.one(42)

// ofでも作れるやんけ
val oneElement2: NonEmptyList[Int] = NonEmptyList.of(42)
val twoElement                     = NonEmptyList.of(1, 2)

// 第２引数がheadになるのに注意
val fooooooElement = NonEmptyList.ofInitLast(List(1, 2, 3), 4)
