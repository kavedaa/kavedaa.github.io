//> using scala 3

import java.time.LocalDate

object Example1:

  case class Person(
    name: String,
    age: Int)

  val tom = Person("Tom", 34)
  val bob = Person("Bob", 45)

//  val joe = Person("Joe", null)

object Example2:

  case class Person(
    name: String,
    age: Option[Int])

  val tom = Person("Tom", Some(34))
  val bob = Person("Bob", None)

  object SubExample1a:

    def printPersonsAge(person: Person) =
      if person.age.isDefined then println(person.age.get)
      else println("The age is unknown")

  object SubExample1b:

    def printPersonsAge(person: Person) =
      person.age match
        case Some(age) => println(age)
        case None => println("The age is unknown")

  def calculateBirthYear(person: Person): Option[Int] =
    val currentYear = LocalDate.now.getYear
    person.age.map(age => currentYear - age)

  object SubExample2a:

    def calculateAverageAge(persons: List[Person]): Int =
      val ages = persons.map(_.age)
      val knownAges = ages.flatten
      knownAges.sum / knownAges.size

  object SubExample2b:

    def calculateAverageAge(persons: List[Person]): Int =
      val knownAges = persons.flatMap(_.age)
      knownAges.sum / knownAges.size

  object SubExample2c:

    def calculateAverageAge(persons: List[Person]): Option[Int] =
      val knownAges = persons.flatMap(_.age)
      knownAges.size match
        case x if x > 0 => Some(knownAges.sum / x)
        case _ => None

object Example3:

  case class Person(
    name: String,
    age: Option[Int])

  case class Company(
    name: String,
    boss: Option[Person])

  object SubExample3a:

    def getCompanyBossAge(company: Company): Option[Option[Int]] =
      company.boss.map(_.age)

    def printCompanyBossAge(company: Company) =
      getCompanyBossAge(company) match
        case Some(Some(age)) => println(s"The boss' age is $age")
        case Some(None) => println("The boss' age is unknown")
        case None => println("The boss is unknown or the company has no boss")

  object SubExample3b:

    def getCompanyBossAge(company: Company): Option[Int] =
      company.boss.flatMap(_.age)

    def printCompanyBossAge(company: Company) =
      getCompanyBossAge(company) match
        case Some(age) => println(s"The boss' age is $age")
        case None => println("The boss' age is unknown or the boss is unknown or the company has no boss")

    
