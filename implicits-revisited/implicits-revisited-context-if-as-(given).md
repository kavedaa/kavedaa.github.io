## Implicits revisited: examples for proposal of context/if/as + (with)

```scala
context as Bar { }

context as Ord[Int] { }

context intOrd as Ord[Int] { }

context [T] if Ord[T] as Ord[List[T]] { }

context listOrd[T] if Ord[T] as Ord[List[T]] { }

context listOrd[T] if (ord: Ord[T]) as Ord[List[T]] { }

context listOrd[T] 
  if (ord: Ord[T]) 
  as Ord[List[T]] {   
}

context as ExecutionContext = new ForkJoinPool()

context global as ExecutionContext = new ForkJoinPool()

context if (outer: Context) as Context = outer.withOwner(currentOwner)

context [Left, Right]
  if (lubLeft: Lub[Right], lubRight: Lub[Right])
  if (lub2: Lub2[lubLeft.Output, lubRight.Output])
  as Lub[Left | Right] { 
}

def foo(given Context) = { }

foo(given ctx)

class C(given Context) extends A with B

new C(given ctx)


def instrumented[D, R, A]
  (given instrument: Instrument[R])
  (block: (given Connector[D]) => A)
  (given Connector[D])
  : instrument.Instrumented[A] = {
}

instrumented(given instrument) { 1 }

```
