## Implicits revisited: examples for proposal of given/if/for + with

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

def foo with Context = { }

foo with ctx

class C with Context extends A with B

new C with ctx

new C with ctx with D

def instrumented[D, R, A]
  with instrument: Instrument[R]
  (block: Connector[D] ?=> A)
  with Connector[D] 
  : instrument.Instrumented[A] = {
}

instrumented with instrument { 1 }

```
