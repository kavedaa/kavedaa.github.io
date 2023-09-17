### Working with `Option` in Scala

The `Option` class is one of the most important classes in Scala and is used in almost every Scala program. In this article we will describe basic usage of `Option` from a beginner's perspective.

Let's start with a motivating example. Suppose we have the following model:

```scala
case class Person(
  name: String,
  age: Int)
```

We can create instances of the `Person` class like this:

```scala
val tom = Person("Tom", 34)
val bob = Person("Bob", 45)
```

But what if we don't know the person's age?

One solution could be to say that `age` is a required field on `Person`, and if we don't have a value for the age we would not be able to create a `Person`. Instead, in the context of our program, we would have to throw an exception or return some other kind of error.

Another way would be to use some "magic" value to represent the absence of an age, such as `0`, or perhaps better, `-1`. Surely no-one would be -1 years old. The downside of this, however, is that we would have to litter our code with `if` checks everywhere that we would be accessing the person's age.

Now you might say, why don't we just use `null`? Well, first, it is not possible in Scala to assign `null` to a value of type `Int`. And even if it were, we would again have to add checks everywhere that the `age` field is accessed, so that we don't accidentally operate on a `null` value and get the dreaded `NullPointerException` at runtime. In general, using `null` is considered bad practice in Scala.

So as you might have guessed, a better option - pun intended - is to use `Option`:

```scala
case class Person(
  name: String,
  age: Option[Int])
```

`Option` is a class that takes one type parameter - the type of the underlying value that we will be wrapping inside the `Option`, in this case `Int`. Now we can create instances of `Person` like this:

```scala
val tom = Person("Tom", Some(34))
val bob = Person("Bob", None)
```

`Option` has exactly to possible sub-types: `Some`, which indicates the presence of a value, and `None`, which indicates the absence of a value. `Some` is a class that will wrap a value of the underlying type, like  `Some(34)` in the example above, while `None` is an object.

Now the question is, now that we may or may not have a value wrapped inside an `Option`, how do we get the value out again? Suppose we want to create a method that prints out a person'a age, how would we do that?

A naive approach would be like this:

```scala
def printPersonsAge(person: Person) =
  if person.age.isDefined then println(person.age.get)
  else println("The age is unknown")
```

Here we are using `.isDefined` to check whether the `Option` has a value. This will return `true` for `Some` and `false` for `None`. Then we are using `.get` to retrieve the wrapped value. This will return the value for a `Some` and throw an excpetion for a `None`.

This is hardly any better than the "magic number" or even `null` approaches outlined above though, since we need to remember to call `.isDefined` before calling `.get`. Using `.get` on `Option` is unsafe since it may throw an exception at runtime and in general should be avoided.

A better way is to use *pattern mathcing*:

```scala
def printPersonsAge(person: Person) =
  person.age match
    case Some(age) => println(age)
    case None => println("The age is unknown")
```      
The benefit of using pattern matching is that we can both check if a value is present and extract the value at the same time. This way we are sure to not operate on "missing" values. (Pattern matching also has many other features that will not be discussed here.)

As another example, let's create a method that calculates a person's birth year based on their age. This should return an `Option[Int]` representing the birth year, if it can be known.

```scala
def calculateBirthYear(person: Person): Option[Int] =
  val currentYear = LocalDate.now.getYear
  person.age.map(x => currentYear - x)
```

Here we use the `.map` method on the `Option` instance, which will return a new `Option` instance. If the value of the `Option` is a `Some`, it will apply the function literal given as a parameter to the `.map` method to the wrapped value inside the `Some`, and if the value is a `None`, it will just return `None`. Suppose that the `age` field on a `Person` is `Some(34)`, then in the example above it will take the current year, which as of the time of writing is 2023, subtract 34 and get 1989, and thus return `Some(1989)`.

Let's show yet another example. Suppose we have a `List` of persons, and we want to calculate the average age of all the persons whose age is known. For this we will create a method with the following signature:

```scala
def calculateAverageAge(persons: List[Person]): Int
```

We can use the `.map` method in `List` to extract all the `age` fields from the persons:

```scala
val ages = persons.map(_.age)
```

This will return a `List[Option[Int]]`. However, we would need to have a `List[Int]` in order to be able to calculate the average. We can use the `.flatten` method on `List` to achieve this:

```scala
val knownAges = ages.flatten
```

This will discard all the `None` values, and retain all the wrapped `Int` values inside the `Some` values, and thus return a `List[Int]`.

We could also have accomplished the mapping and flattening operations with a single `.flatMap` method:

```scala
val knownAges = persons.flatMap(_.age)
```

Now we can easily calculate the average of the `Int` values in the list, and the whole method would look like this:

```scala
def calculateAverageAge(persons: List[Person]): Int =
  val knownAges = persons.flatMap(_.age)
  knownAges.sum / knownAges.size
```

There is a bug in this method, though. What if the list of persons is empty? Or we don't know the ages of any of the persons in the list? Then `knownAges.size` would be zero, and we would be dividing by zero, which would throw an exception.

Now we need to decide again: how do we represent the lack of an average value? What should we return from our method? As you might have guessed: we will use `Option`.

```scala
def calculateAverageAge(persons: List[Person]): Option[Int] =
  val knownAges = persons.flatMap(_.age)
  if knownAges.size match
    case x if x > 0 => Some(knownAges.sum / x)
    case _ => None
```

Here we are only calculating the average when the number of known ages is greater than zero, and the we are wrapping the result in a `Some`. Otherwise we just return `None`.

Let's move on to a slightly more complex example:

```scala
case class Person(
  name: String,
  age: Option[Int])

case class Company(
  name: String,
  boss: Option[Person])
```

Here, the type inside the `Option` is the composite type `Person`, which in turn has a member that is an `Option` of the primitive type `Int`. 

Suppose we want to find the boss' age. We could do it like this:

```scala
  def getCompanyBossAge(company: Company): Option[Option[Int]] =
    company.boss.map(_.age)
```

That returns something interesting. An `Option` within an `Option`! But what does that mean?

Let's generalize that question: what does `Option` *mean*?

On a technical level, the meaning of an `Option` is only the presence or absence of some piece of information. On a semantic level, it has no particular meaning except what we decide to assign to it in the context of our application domain.

In the `Company` example, the absence of a `boss` could mean one of two things:

1. The company has no boss
2. The company has a boss, but we don't know who it is

The `Option[Person]` type can not distinguish between these two scenarios. It simply indicates the presence or absence of a `boss`.

On the other hand, `age` as `Option[Int]` would typically only mean that we know or don't know the person's age. It wouldn't make sense to say that a person "has no age".

When we compute the age (which may or not be present) of the boss (which may or may not be present) of a company, we get the type of `Option[Option[Int]]`. On a technical level, this has three possible values, which on a semantic level corresponds to these scenarios:

1. We know the boss and their age
2. We know the boss but not their age
3. We don't know the boss or the company has no boss

(One could of course argue that there could be a scenario in which we knew the age of the company's boss, but without knowing who the boss was. However the data models above would not be suffient for representing that.)

If we were to print the information we have on the boss and their age, we could do it like this:

```scala
  def printCompanyBossAge(company: Company) =
    getCompanyBossAge(company) match
      case Some(Some(age)) => println(s"The boss is known and their age is $age")
      case Some(None) => println("The boss is known but their age is unknown")
      case None => println("The boss is unknown or the company has no boss")
```

Here we also demonstrate a nice benefit of the pattern matching syntax: it very clearly corresponds to the three scenarios listed above.

However, sometimes we need to reduce a type like `Option[Option[Int]]` into just `Option[Int]`. Suppose for instance that we were to pass the age of the boss of a company over to some other system that only cared about the mere presence or absence of a value, and thus required an `Option[Int]` as input.

We could then compute the boss' age like this:

```scala
  def getCompanyBossAge(company: Company): Option[Int] =
    company.boss.flatMap(_.age)
```

The difference from the first version is that `.map` has been replaced with `.flatMap`. We say that we are "flattening" the data.

Doing so, we are also loosing information. We would no longer be able to distinguish between all of the three scenarios above, as two of them will have been "collapsed" into one:

```scala
  def printCompanyBossAge(company: Company) =
    getCompanyBossAge(company) match
      case Some(age) => println(s"The boss' age is $age")
      case None => println("The boss' age is unknown or the boss is unknown or the company has no boss")
```

Whether we want to work with and pass around a type like `Option[Option[Int]]` or a simpler type like `Option[Int]`, depends entirely on what we might need in a given application. Sometimes we might end up with a type like `Option[Option[Option[Int]]]` or even more complex variants. In such scenarios it would be better to create a specialized datatype instead of using generic container like `Option`.








 

