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

def foo(with Context) = { }

foo(with ctx)

class C(with Context) extends A with B

new C(with ctx)

def instrumented[D, R, A]
  (with instrument: Instrument[R])
  (block: Connector[D] ?=> A)
  (with Connector[D])
  : instrument.Instrumented[A] = {
}

instrumented(given instrument) { 1 }

```
