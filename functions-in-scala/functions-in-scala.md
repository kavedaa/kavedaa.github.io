# Functions in Scala

Scala is said to be both an *object-oriented* and *functional* programming language. The precise meaning of "object-oriented" and "functional" is stil up for the experts to agree on, but it would at least be resonable to assume that it means that Scala has both *objects* and *functions*. Which it does.

In this article we will take a look at functions in Scala from a beginner's perspective and show what they are and how they can be used.

The simplest way to define a function in Scala is like this:

```scala
def f(x: Int) = x + 1
```

The function can then be invoked like this:

```scala
scala> f(1)
val res0: Int = 2
```

Strictly speaking though, `f` in this example is called a *method* (which is a term used within the context of object-oriented programming and meaning some block of code associated with a class or an object). When we call this a function, it is only in the loose sense in that it corresponds to the mathematical concept of a function.

Another way to define the same function is like this:

```scala
val f = (x: Int) => x + 1
```

It can be invoked in exactly the same way as a method:

```scala
scala> f(1)
val res1: Int = 2
```

This is what a called a *function* in Scala. (Strictly speaking, `f` is a "function value" and the right-hand side of the assignment, `(x: Int) => x + 1`, is a "function literal". But for simplicity's sake we'll just use the term "function" for both.)

A function will have a *type*. If we were to describe `f` informally, we might say that it is "a function that takes an int parameter and returns an int". Or shorter, "a function from int to int". Or even shorter, just "int to int". The latter is what the type of that function is called, and it's written `Int => Int`.

A function is a value that can be assigned to a variable and/or passed around (more on that later). In this example we assign the function `f` to some other variable `g`, just as we would assign any other value:

```scala
val f = (x: Int) => x + 1
val g = f

scala> g(1)
val res5: Int = 2
```

In practice, this can also be done with methods. Strictly speaking though, in the example below, we are not really assigning a method to a variable, instead the method `f` gets transformed into a function value `g`:

```scala
def f(x: Int) = x + 1
val g = f

scala> g(1)
val res4: Int = 2
```

There is also a third way to define a function. In the Scala standard library, there is a trait called [`Function1`](https://www.scala-lang.org/api/current/scala/Function1.html). This is defined like this (simplified):

```scala
trait Function1[-T, +R]:
  def apply(x: T): R
```

It has one abstract method called `apply`. By implementing this, we can define a function like this:

```scala
val f = new Function1[Int, Int]:
  def apply(x: Int) = x + 1
```

And call it like this:

```scala
scala> f(1)
val res0: Int = 2
```

This is actually what `(x: Int) => x + 1` is transformed into. In practice we would rarely use the `new ...` syntax for defining a function. However, it is useful to know that the two constructs mean the same, and that functions in Scala are actually objects (i.e. class instances) with an `apply` method.

## Functions with several parameters

In the simple examples so far, we have just demontrated a function (or method) with a single parameter. Of course, both methods and functions can have several parameters:

```scala
def f(a: Int, b: Int) = a + b
```

And:

```scala
val f = (a: Int, b: Int) => a + b
```

The type of the function `f` in this example is written like `(Int, Int) => Int`.

## Curried functions and methods with several parameter lists

In `def f(a: Int, b: Int) = a + b`, the `(a: Int, b: Int)` part is called a parameter list. In addition to several parameters in a single parameter list, a method can also have several parameter lists:

```scala
def f(a: Int)(b: Int) = a + b
```

This can be called like this:

```scala
scala> f(1)(2)
val res3: Int = 3
```

Interestingly, it can also be called like this:

```scala
scala> val g = f(1)
val g: Int => Int
```

It then returns a new *function*, which in turn can be called like this:

```scala
scala> g(1)
val res5: Int = 2
```

thus ending up at the same result.

Corresponding to methods with several parameters lists are *curried functions*:

```scala
val f = (a: Int) => (b: Int) => a + b
```

Which can be called like this:

```scala
scala> f(1)(2)
val res6: Int = 3
```

As expected, we can also call it like this:

```scala
scala> val g = f(1)
val g: Int => Int
```

```scala
scala> g(2)
val res8: Int = 3
```

The type of the function `f` in this example is `Int => Int => Int`. This can be read as "a function of int, to a function of int to int". It might be easier to understand how to read it if it is written like `Int => (Int => Int)`.

(Note that this is *not* the same as `(Int => Int) => Int`, which would be "a function of a function of int to int, to int". More on that later.)

Stil, "a function of int to a function of int to int", can be a bit hard to understand. Here is another way of looking at it:

If we compare with the alternative way of calling it shown above, where we first write `val g = f(1)`, we can see that when we call `f(1)`, that is "a function of int". That function returns a "function of int to int", which is `g` in the example. That function can it turn be called with an `Int` argument like `g(2)`, finally returning an `Int`. That is also actually exactly what `f(1)(2)` does, just without assigning the intermediate function result to a value `h`.

## Higher-order functions

The first thing we should about higher-order functions (or methods) is that they sound more advanced than they are. A higher-order function (or method) is simply a function (or method) that takes another function as an argument. (Stricly speaking it can also be a function that returns another function as its result, such as in the examples in the previous section.)

If you have used the `.map` method on `Option` or `List`, you have already used a higher-order method. However, let's look at a simpler example:

```scala
def f(g: Int => Int) = g(1) + 2
```

Here we have defined a method `f` that takes a parameter `g` of type "function of int to int". In the method body, that function will be invoked with an argument of `1` and then `2` will be added to the result of that function.

Suppose that we define a function `h` like this:

```scala
val h = (x: Int) => x + 1
```

This is a function of int to int. That means that we can pass it to the method `f`, like this:

```scala
scala> f(h)
val res13: Int = 4
```

This is what is meant when we say that functions can be "passed around" - they can be passed as arguments to higher-order methods or functions.

Instead of assigning the function literal to an intermediate varable `h`, we can also pass it directly to `f`:

```scala
scala> f((x: Int) => x + 1)
val res14: Int = 4
```

Additionally, since the type of the parameter is known, we can take advantage of Scala's type inference mechanism and just write:

```scala
scala> f(x => x + 1)
val res15: Int = 4
```

As expected, it is also possible to define a higher-order function correponding to the method above:

```scala
val f = (g: Int => Int) => g(1) + 2
```

The type of this function is `(Int => Int) => Int`, that is, a function that takes a function of int to int as parameter and produces an int as result.

Also as expected, we can call it the same way we called the higher-order method:

```scala
scala> f(x => x + 1)
val res17: Int = 4
```

There are several syntactical ways to write a function literal argument to a higher-order method or function. Above we have seen one way, `f(x => x + 1)` (or two if we count the version with explicit type ascription of the parameter to the inner function).

Here is an overview of other ways. They all mean the same:

```scala
f(_ + 1)

f { x => x + 1 }

f { x =>
  x + 1
}

f:
  x => x + 1

f: x =>
  x + 1
```

(The latter two only works in Scala 3.)

A good rule of thumb for which syntax to use can be this: use the underscore variant for short functions, and the multi-line ones for long or multi-line functions.

Finally, let's look at a slightly more interesting example then the trivial examples we have used so far.

Suppose we have a class for representing a person:

```scala
case class Person(firstName: String, lastName: String, age: Int)
```

And define some persons like this:

```scala
val persons = List(Person("Tom", "Doe", 23), Person("Bob", "Smith", 34))
```

Now, we want to create a method that prints a list of persons. A possible implementation is like this:

```scala
def printPersons(xs: List[Person]) =
  println("List of persons:")
  xs.zipWithIndex.foreach: (person, index) =>
    println(s"Person ${index + 1}: ${person.firstName} ${person.lastName}")
```

When run, it would print the following:

```
List of persons:
Person 1: Tom Doe
Person 2: Bob Smith
```

Now, suppose that we would want to change the way a person's details are printed. Perhaps we want to print only the first name, or the first name and last name in opposite order, or also include the age. But we would also want to retain the general structure of the print output.

We could create a separate version of `printPersons` for each of the variants. But as you might have guessed, a better solution would be to pass the way we want to print a person's details as a parameter to the `printPersons` method.

What to be the type of that parameter? It would be a function from `Person` to `String`, i.e. `Person => String`. The method could that be rewritten like this:

```scala
def printPersons(xs: List[Person])(render: Person => String) =
  println("List of persons:")
    xs.zipWithIndex.foreach: (person, index) =>
      println(s"Person ${index + 1}: ${render(person)}")
```

(Note that the `foreach` method on `List` used in this example is also a higher-order method!)

And it could be called e.g. like this:

```scala
printPersons(persons)(person => s"${person.lastName}, ${person.firstName} - ${person.age}")
```
```
List of persons:
Person 1: Doe, Tom - 23
Person 2: Smith, Bob - 34
```

Or e.g like this:

```scala
printPersons(persons): person =>
  val birthYear = java.time.LocalDate.now.getYear - person.age
  s"The person's name is ${person.firstName} ${person.lastName} and they were born in $birthYear"
```

```
List of persons:
Person 1: The person's name is Tom Doe and they were born in 2000
Person 2: The person's name is Bob Smith and they were born in 1989
```



