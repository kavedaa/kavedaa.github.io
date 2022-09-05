# A brief introduction to Scala

This is an overview of the most important concepts and constructs in Scala. It includes both pure language features as well as standard library features. It is not meant as a tutorial for someone learning programming for the first time, but rather as a quick reference for someone coming from other programming languages like e.g. Java or Python. 

The examples are shown using the *Scala console* (aka. REPL) which is a very useful tool for exploring Scala.

## Values and variables

Scala differentiates between *values*, which are immutable:

```scala
scala> val a = 1
val a: Int = 1

scala> a = 2
-- Error:
1 |a = 2
  |^^^^^
  |Reassignment to val a

```

And *variables*, which are mutable:

```scala
scala> var b = 1
var b: Int = 1

scala> b = 2
b: Int = 2
```

## Immutability

In general code with immutable values is easier to reason about than code with mutable variables. It is therefore good practice to use `val` instead of `var` most places. A rule-of-thumb is that `var` should not be used unless one is certain that it is needed. Examples of the latter could be performance optimizations in tight scopes or that a specific algorithm becomes much easier to implement. 

## Type inference

As seen above, the type `Int` was not specified when assigning the value 1 to `a`, instead it was automatically inferred. Type inference is useful as it reduces the need to add type information in the code. However, it is also possible to specify the type. This can be done in two different ways:

```scala
scala> val a: Int = 1
val a: Int = 1
```

```scala
scala> val a = 1: Int
val a: Int = 1
```

The first way is generally preferred.

The good practice is to add type ascriptions whenever it would make the code easier to read, such as when it would be useful to know the type of a value but it is not immediately obvious from looking at the code.

## Methods

*Methods* are defined like this:

```scala
scala> def f(x: Int) = x + 1
def f(x: Int): Int
```

And invoked like this:

```scala
scala> f(1)
val res0: Int = 2
```

As seen above, the parameter(s) to a methods must be given a type, as it can not be inferred. It is also possible to specify the result type:

```scala
scala> def f(x: Int): Int = x + 1
def f(x: Int): Int
```

Methods may have several parameters:

```scala
scala> def add(a: Int, b: Int) = a + b
def add(a: Int, b: Int): Int
```

It may also have zero parameters:

```scala
scala> def speak() = println("Hello")
def speak(): Unit
```

Note that in Scala 2, methods must be part of a class (or trait) or object, while in Scala 3 top-level methods are allowed.

## Classes

*Classes* are defined like this:

```scala
scala> class A
// defined class A
```

And instantiated like this:

```scala
scala> val a = new A
val a: A = A@306b5162
```

In Scala 3, it is also possible to instantiate a class without using the `new` keyword:

```scala
scala> val a = A()
val a: A = A@19aa18c
```

Classes can have values, variables, and methods as members:

```scala
scala> class Person {
     | val name = "Tom"
     | var age = 34
     | def speak() = println(s"Hi,I'm $name")
     | }
// defined class Person
```

Member fields are accessed in this manner:

```scala
scala> val person = new Person
val person: Person = Person@1a6e9f15

scala> person.name
val res1: String = Tom

scala> person.age
val res2: Int = 34

scala> person.speak()
Hi,I'm Tom
```

Classes can have *constructor parameters*:

```scala
scala> class Person(name: String, val age: Int)
// defined class Person

scala> val person = new Person("Bob", 41)
val person: Person = Person@3d6a5f0a
```

Constructor parameters defined with the `val` keywords can be accessed as member fields from outside the class:

```scala
scala> person.name
-- Error:
1 |person.name
  |^^^^^^^^^^^
  |value name cannot be accessed as a member of (person : Person) from module class rs$line$29$.

scala> person.age
val res6: Int = 41
```

Classes can *inherit* other classes:

```scala
scala> class Person(name: String)
// defined class Person

scala> class Employee(name: String, title: String) extends Person(name)
// defined class Employee
```

In Scala 3, classes can also be defined using a "brace-less" syntax:

```scala
scala> class Person:
     |   val name = "Tom"
     |   val age = 34
     |
// defined class Person
```

## Traits

*Traits* are similar to classes:

```scala
scala> trait Worker {
     | def wage: Double
     | }
// defined trait Worker
```

They can be "mixed into" class definitions:

```scala
scala> class Employee(name: String, title: String) extends Person(name) with Worker {
     | def wage = 500000
     | }
// defined class Employee
```

## Objects

*Objects* are like classes with only one single instance, which is referred to directly using the name of the object:

```scala
scala> object B { def f(x: Int) = x + 1 }
// defined object B

scala> B.f(1)
val res9: Int = 2
```

## Case classes

*Case classes* are a specific kind of classes that have some convenient features:

```scala
scala> case class Person(name: String, age: Int)
// defined case class Person

scala> val person = Person("Tom", 23)
val person: Person = Person(Tom,23)
```

They have a nice default string representation, which can be seen above.

Their member fields can be accessed without being defined with the `val` keyword:

```scala
scala> person.name
val res10: String = Tom

scala> person.age
val res11: Int = 23
```

And they have a built-in equality method:

```scala
scala> Person("Tom", 23) == Person("Tom", 23)
val res12: Boolean = true
```

The term `case` comes from a particular usage in so-called "pattern-matching" (which will be covered later), but case classes are typically used as general "data" classes, which might have been a better term.

## Equality

Equality between class instances can mean either that the contents of the instances (i.e., the data they hold), are the same; or that the instances themselves are actually one same instance.

The former must be coded specifically for by the programmer for each class. However, as seen above, it is automatically generated for case classes.

In Scala, this kind of equality is checked for using the `==` method:

```scala
scala> val tom = Person("Tom", 23)
val tom: Person = Person(Tom,23)

scala> val bob = Person("Bob", 34)
val bob: Person = Person(Bob,34)

scala> tom == bob
val res25: Boolean = false
```

For checking instance equality, the `eq` keyword is used:

```scala
scala> val otherTom = Person("Tom", 23)
val otherTom: Person = Person(Tom,23)

scala> tom == otherTom
val res26: Boolean = true

scala> tom eq otherTom
val res27: Boolean = false
```

Here the contents are the same, but they are different instances. But if we create another reference to the same instance, the instance equality check returns true as well:

```scala
scala> val boss = tom
val boss: Person = Person(Tom,23)

scala> boss == tom
val res28: Boolean = true

scala> boss eq tom
val res29: Boolean = true
```

## Collections

### `List`

*Lists* can be defined like this:

```scala
scala> val cities = List("Bergen", "Oslo", "London", "Berlin", "Paris")
val cities: List[String] = List(Bergen, Oslo, London, Berlin, Paris)
```

The type of the list will also include the type of the list's items. Thus the type above is `List[String]` and not just `List`.

Lists are immutable. This means that when an element is added:

```scala
scala> cities :+ "Tokyo"
val res30: List[String] = List(Bergen, Oslo, London, Berlin, Paris, New York, Tokyo)
```

a new list will created, while the original list will remain unchanged:

```scala
scala> cities
val res31: List[String] = List(Bergen, Oslo, London, Berlin, Paris, New York)
```

There is also a mutable list called `Buffer`. Choosing between immutable and mutable collections should follow the same rule of thumb as for values and variables: always choose the immutable version unless you absolutely need mutability.

There is a large number of methods available on `List`. Most functionality needed when working with collections can be achieved by these. Here are some simple examples:

```scala
scala> cities.size
val res32: Int = 6

scala> cities.take(3)
val res33: List[String] = List(Bergen, Oslo, London)

scala> cities.reverse
val res34: List[String] = List(New York, Paris, Berlin, London, Oslo, Bergen)

scala> cities(2)
val res35: String = London
```

A full overview can be found here: https://www.scala-lang.org/api/current/scala/collection/immutable/List.html

### `Map`

*Maps* can be defined like this:

```scala
scala> val capitols = Map("NO" -> "Oslo", "SE" -> "Stockholm", "DE" -> "Copenhagen")
val capitols: Map[String, String] = Map(NO -> Oslo, SE -> Stockholm, DE -> Copenhagen)
```

And accessed like this:

```scala
scala> capitols("NO")
val res36: String = Oslo
```

Many of the methods from `List` are available on `Map` as well, for example:

```scala
scala> capitols.size
val res37: Int = 3
```

### Other collections

There are many other built-in collections in Scala, each with their own specific functionality and/or performance characteristics, both with immutable and mutable variants. Many of the same methods are available on all collections.

## `map` and `filter`

There are a some collections methods that are very commonly used. Amoung those are `map` and `filter`.

`map` will take each element in the collection, apply a function to it, and create a new list:

```scala
scala> cities.map(x => x.length)
val res39: List[Int] = List(6, 4, 6, 6, 5, 8)
```

Here a new list with the lengths of the corresponding strings in the original collection have been created.

The syntax `x => x.length` is called a *function literal*. There are various other ways of writing it as well:

```scala
scala> cities.map(_.length)
val res42: List[Int] = List(6, 4, 6, 6, 5, 8)
```

```scala
scala> cities map { x =>
     | println("getting length")
     | x.length
     | }
getting length
getting length
getting length
getting length
getting length
getting length
val res41: List[Int] = List(6, 4, 6, 6, 5, 8)
```

The last variant above, with braces instead of parentheses, must be used when the mapping function have several lines. Here it is also, purely for illustrative purposes, shown that the mapping function is executed once for each element in the list.

`filter` will create a new list consisting of only those elements in the collection that satisfy a given condition:

```scala
scala> cities.filter(_.length > 5)
val res43: List[String] = List(Bergen, London, Berlin, New York)
```

Similarly as with `map`, the function literal parameter can be written in several ways.

## `Option`

The `Option` is one of the most basic and commonly used built-in types in Scala. It's typically used for modelling a value that may or may not exist.

Let's use the `find` method on collections as an example for illustrating how it works:

```scala
scala> cities.find(_.startsWith("Ber"))
val res44: Option[String] = Some(Bergen)

scala> cities.find(_.startsWith("Rom"))
val res45: Option[String] = None
```

An `Option` can have two possible values: an instance of `Some` wrapping an element value, or the object `None`. In a way, it can be seen as a collection with either one or zero elements. It also has many of the common collections methods:

```scala
scala> val x: Option[Int] = Some(1)
val x: Option[Int] = Some(1)

scala> x.map(_ + 1)
val res46: Option[Int] = Some(2)

scala> x.filter(_ > 10)
val res47: Option[Int] = None
```

## `flatMap`

Suppose the following definitions:

```scala
scala> case class Person(name: String, age: Option[Int])
// defined case class Person

scala> def getBoss: Option[Person] = Some(Person("Tom", Some(50)))
def getBoss: Option[Person]
```

If we want to get the boss' age, we might do:

```scala
scala> getBoss.map(_.age)
val res48: Option[Option[Int]] = Some(Some(50))
```

This gives us a nested `Option`. However if replace `map` with `flatMap`, we get:

```scala
scala> getBoss.flatMap(_.age)
val res49: Option[Int] = Some(50)
```

The result has been "flattened" to a simple `Option`.

It also works with lists:

```scala
scala> case class Person(name: String, age: Option[Int], hobbies: List[String])
// defined case class Person

scala> def getEmployees = List(Person("Tom", Some(50), List("Golf", "Tennis")), Person("Bob", None, List("Chess", "Hiking")))
def getEmployees: List[Person]
```

```scala
scala> getEmployees.map(_.hobbies)
val res50: List[List[String]] = List(List(Golf, Tennis), List(Chess, Hiking))

scala> getEmployees.flatMap(_.hobbies)
val res51: List[String] = List(Golf, Tennis, Chess, Hiking)
```

In general, whenever the mapping function in `map` would return the same type as the type the `map` is called on, `flatMap` can be used instead to give a non-nested result.

A special case is `Option`, which even though it is a different type than `List`, can also be "flap-mapped" from a `List`:

```scala
scala> getEmployees.map(_.age)
val res52: List[Option[Int]] = List(Some(50), None)

scala> getEmployees.flatMap(_.age)
val res53: List[Int] = List(50)
```

## `for` comprehensions

The `map` and `flatMap` examples we have seen can also be written using a different syntax called *for-comprehension*:

```scala
scala> for (city <- cities) yield city.length
val res54: List[Int] = List(6, 4, 6, 6, 5, 8)

scala> for {
     | employee <- getEmployees
     | hobby <- employee.hobbies
     | } yield hobby
val res56: List[String] = List(Golf, Tennis, Chess, Hiking)
```

For these examples the other syntax may not seem like a win. But for more complex scenarios, with many nested `flatMap`s, for-comprehensions can typically be more readable.

## `Try`

The `Try` type is similar to `Option` in that it may or may not hold an element value. However, when there is no value, it will hold an `Exception` instead of simply being `None`:

```scala
scala> def parseInt(x: String) = Try(x.toInt)
def parseInt(x: String): scala.util.Try[Int]

scala> parseInt("34")
val res58: scala.util.Try[Int] = Success(34)

scala> parseInt("abc")
val res59: scala.util.Try[Int] = Failure(java.lang.NumberFormatException: For input string: "abc")
```

`Try` is very useful for simple error handling, as well as for wrapping other code that may throw exceptions (typically Java-defined methods).

## Pattern matching

*Pattern matching* can in its simplest form be illustrated by writing the "mapping" logic on e.g. `Option` in yet another way:

```scala
scala> x match {
     | case Some(value) => Some(value + 1)
     | case None => None
     | }
val res62: Option[Int] = Some(2)
```

The feature is a lot more powerful than this though. Here is a slightly more complex example:

```scala
scala> def getBoss: Try[Person] = Success(Person("Tom", Some(23), List()))
def getBoss: util.Try[Person]

scala> getBoss match {
     | case Success(Person(name, Some(age), _)) => s"The boss is called $name and is $age years old"
     | case Success(Person(name, _, _)) => s"The boss is called $name. We don't know their age"
     | case Failure(ex) => s"We do't know the boss due to ${ex.getMessage}"
     | }
val res63: String = The boss is called Tom and is 23 years old
```

Pattern matching can create very readable code as it allows to lists the various scenarios, or *cases*, in the same manner as one would reason about them.

## Implicits

Suppose we have some methods that use e.g. a `DateTimeFormatter`:

```scala
scala> def renderSomething(dtf: DateTimeFormatter): String = s"Render something that includes a date ${dtf.format(LocalD
ate.now)}"
def renderSomething(dtf: java.time.format.DateTimeFormatter): String

scala> def renderSomethingElse(dtf: DateTimeFormatter): String = s"Render something that includes a date ${dtf.format(Lo
c    te.now)}"
def renderSomethingElse(dtf: java.time.format.DateTimeFormatter): String
```

We could create a `DateTimeFormatter` and pass it to the methods when they are called:

```scala
scala> val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
val dateTimeFormatter: java.time.format.DateTimeFormatter = Value(YearOfEra,4,19,EXCEEDS_PAD)'-'Value(MonthOfYear,2)'-'Value(DayOfMonth,2)

scala> renderSomething(dateTimeFormatter)
val res65: String = Render something that includes a date 2022-09-05

scala> renderSomethingElse(dateTimeFormatter)
val res66: String = Render something that includes a date 2022-09-05
```

However it's a bit tedious, if we have many different such methods and/or are calling them many times.

If we instead define the methods like e.g. this:

```scala
scala> def renderSomething(implicit dtf: DateTimeFormatter): String = s"Render something that includes a date ${dtf.form
a   ocalDate.now)}"
def renderSomething(implicit dtf: java.time.format.DateTimeFormatter): String
```

And also add the `implicit` keyword to the `DateTimeFormatter` value definition:

```scala
scala> implicit val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
val dateTimeFormatter: java.time.format.DateTimeFormatter = Value(YearOfEra,4,19,EXCEEDS_PAD)'-'Value(MonthOfYear,2)'-'Value(DayOfMonth,2)
```

We can then omit the parameter when calling the method:

```scala
scala> renderSomething
val res67: String = Render something that includes a date 2022-09-05
```

*Implicits* are used for various purposes in Scala, among them encoding so-called "type-classes", which form the basis of advanced functional programming.

In Scala 3 the syntax is slightly different. The keywords `given` and `using` are used instead of `implicit`:

```scala
scala> def renderSomething(using dtf: DateTimeFormatter): String = s"Render something that includes a date ${dtf.format(
LocalDate.now)}"
def renderSomething(using dtf: java.time.format.DateTimeFormatter): String

scala> given DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
lazy val given_DateTimeFormatter: java.time.format.DateTimeFormatter
```

## Future

`Future` is a "container" type similar to `Option` and `Try`. It is used for concurrent programming, and will hold a value that may or may not have been computed yet.

In order to use `Future` an `implicit` instance of `ExecutionContext` needs to be in scope. The simplest is to import the default one:

```scala
scala> import concurrent.ExecutionContext.Implicits.global
```

We can then perform a concurrent operation:

```scala
scala> val addition = Future { 1 + 2 }
val addition: scala.concurrent.Future[Int] = Future(<not completed>)
```

We need not wait long for it to complete:

```scala
scala> addition.value
val res68: Option[scala.util.Try[Int]] = Some(Success(3))
```

Typically though, we would not wait until it is finish, but rather to ask it to call us when it is:

```scala
scala> addition onComplete {
     | case Success(result) => s"Got $value"
     | case Failure(ex) => "Something went wrong when adding two numbers: ${ex.getMessage}"
     | }
```

The usual `map` and `flatMap` are available on `Future` as well, making it possible to combine results from multiple `Future`s.

## `null`

Don't use it. Never ever.