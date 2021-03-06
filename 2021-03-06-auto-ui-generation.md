# Automatic UI generation with Scala 3's type class derivation

Scala 3 has a lot of cool new features. Among those is so-called [type class derivation](http://dotty.epfl.ch/docs/reference/contextual/derivation.html), which isn't really a single feature in itself, but rather a set of low-level technologies that enables the automatic implementation of type classes.

Basically if we have a type class (or trait) like `trait Foo[A]`, we can use these techonologies to write code that implements `Foo` for any given `A`, if `A` is a product type (e.g. `case class`) or sum type (e.g. `enum`).

Can we (ab)use this for automatic user interface generation? For instance, say we have:

```scala
case class Person(firstName: String, lastName: String, birthDate: LocalDate)
```

can we write code that enables us to use a simple one-liner like:

```scala
val dialog = new DerivedDialog[Person]
```

and have that generate a dialog like this?

(image)

Yes, we can, and it this article we will see how.

## Type class derivation basics

Let's first see how this type class derivation machinery works by implementing something simpler. Consider the following type class:

```scala
trait Transformer[T]:
  def f(t: T): T
```

This has a single method that simply transforms a value into a another value of the same type. We use this just as an example, but it also has some similarity with the process of editing a value with a user interface, so it's not a bad place to start.

Now we can create given instances of this for some primitive types:

```scala
given Transformer[String] with
  def f(t: String) = t.toUpperCase

given Transformer[Int] with
  def f(t: Int) = t * 2
```

If we also create a utility method like:

```scala
def transform[A : Transformer](a: A) = summon[Transformer[A]].f(a)
```

we can transform primitive values like this:

```scala
scala> transform("foo")
val res0: String = FOO

scala> transform(123)
val res1: Int = 246
```

What if we want to transform the `Person` class defined above? We could of course quite easily write a `Transformer` for it. But it's tedious, and programmers hate tedious work. So we want a way to create a `Transformer` for any case class, given that we already have `Transformer`s for the individual elements of the class.

Here's how we can do that with the new Scala 3 features:

```scala
  inline given [A <: Product] (using m: Mirror.ProductOf[A]): Transformer[A] =
    new Transformer[A]:
      val elemTransformers = summonAll[Tuple.Map[m.MirroredElemTypes, Transformer]].toList.asInstanceOf[List[Transformer[Any]]]  
      def f(a: A): A = 
        val elems = a.productIterator.toList
        val transformed = elems.zip(elemTransformers) map { case (elem, transformer) => 
          transformer.f(elem) }
        val tuple = transformed.foldRight[Tuple](EmptyTuple)(_ *: _)
        m.fromProduct(tuple)      
```

There's a bit of stuff going on here. Notable new features being used are:

* `inline`
* `Mirror`
* some `Tuple` features

Let's look at it in a little more detail. `Mirror.ProductOf` is synthesized automatically by the compiler for all product types (e.g. case classes), so it will always be available here. This class contains meta-information about the case class that we can use for inspecting it.

First we summon given instances for `Transformer` each of the element types of the case class. For the `Person` class `m.MirroredElemTypes` will be `(String, String, LocalDate)`. Using `Tuple.Map` we turn that into `(Transformer[String], Transformer[String], Transformer[LocalDate])` which we in turn pass to Scala's `summonAll` method. This returns a `Tuple` containing corresponding given instances for these types. We turn that tuple into a list, (and explicitly remind the compiler of the type of that list).

Then we can implement the transformation method `f`. This takes an actual instance of the class we are operating on, such as a `Person`, and returns a new instance of that class, with the elements transformed by the individual transformers. First we put all the elements of the class into a list. Then we map each element with the corresponding transformer instance. Finally we put the resulting list back into a tuple, and use the `fromProduct` method on the `Mirror` to create an instance of the case class from the tuple.

And that's it! I have to admit to not completely understand how the `inline` stuff works, but it does work which is the most important. So now we can do:

```scala
scala> transform(Person("John", "Smith", LocalDate.of(2000, 1, 1)))
val res1: Person = Person(JOHN,SMITH,2000-01-02)
```

## User interfaces in JavaFX

For the actual user interface generation we will use JavaFX, but the technique could probably be easily ported to other toolkits. Let's first take a quick look at how to create user interfaces in JavaFX manually. 

JavaFX uses a "scene graph" which is a tree-like hierarchy of `Node`s. Nodes can be controls like text fields and check boxes, or layout containers (or other elements not relevant here). For editing a `Person` like the one defined above, we could write the following code:

```scala
  val firstNameField, lastNameField = new TextField
  val birthDatePicker = new DatePicker

  val content = new VBox(10) {
    getChildren.addAll(
      new VBox(5, new Label("First name"), firstNameField),
      new VBox(5, new Label("Last name"), lastNameField),
      new VBox(5, new Label("Birth date"), birthDatePicker)
    )
  }
```

and then put the `content` inside a `Dialog`, which is not shown here.

Not too much hassle, to be sure, but it would still great if we could automate it.




