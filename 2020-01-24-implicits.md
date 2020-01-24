## Implicits revisited: examples for proposal of given/if/for + (given)

```scala
given for Bar { }

given for Ord[Int] { }

given intOrd for Ord[Int] { }

given [T] if Ord[T] for Ord[List[T]] { }

given listOrd[T] if Ord[T] for Ord[List[T]] { }

given listOrd[T] if (ord: Ord[T]) for Ord[List[T]] { }

given listOrd[T] 
  if (ord: Ord[T]) 
  for Ord[List[T]] {   
}

given for ExecutionContext = new ForkJoinPool()

given global for ExecutionContext = new ForkJoinPool()

given if (outer: Context) for Context = outer.withOwner(currentOwner)

given [Left, Right]
  if (lubLeft: Lub[Right], lubRight: Lub[Right])
  if (lub2: Lub2[lubLeft.Output, lubRight.Output])
  for Lub[Left | Right] { 
}

def foo(given Context) = { }

foo(given context)

def instrumented[D, R, A]
  (given instrument: Instrument[R])
  (block: (given Connector[D]) => A)
  (given connector: Connector[D]): instrument.Instrumented[A] = {
}

instrumented(given instrument) { 1 }

```
