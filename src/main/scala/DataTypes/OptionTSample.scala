package DataTypes

import scala.concurrent.Future


import scala.concurrent.ExecutionContext.Implicits.global

// 先にEitherTを見た方が分かりやすいぞ

// 一旦mapでFutureを剥がさないとだめなので、めんどくさいの図
val customGreeting: Future[Option[String]] = Future.successful(Some("welcome back, Lola"))

val excitedGreeting: Future[Option[String]] = customGreeting.map(_.map(_ + "!"))
val hasWelcome: Future[Option[String]] = customGreeting.map(_.filter(_.contains("welcome")))
val noWelcome: Future[Option[String]] = customGreeting.map(_.filterNot(_.contains("welcome")))
val withFallback: Future[String] = customGreeting.map(_.getOrElse("hello, there!"))

import cats.data.OptionT

// そこでOptionTなんです
val customGreetingT: OptionT[Future, String] = OptionT(customGreeting)

val excitedGreeting_OptionT: OptionT[Future, String] = customGreetingT.map(_ + "!")
val withWelcome_OptionT: OptionT[Future, String] = customGreetingT.filter(_.contains("welcome"))
val noWelcome_OptionT: OptionT[Future, String] = customGreetingT.filterNot(_.contains("welcome"))
val withFallback_OptionT: Future[String] = customGreetingT.getOrElse("hello, there!")













