## Implicits revisited: examples for proposal of given/using/for + (using)

```scala
given for Bar { }

given for Ord[Int] { }

given intOrd for Ord[Int] { }

given [T] (using Ord[T]) for Ord[List[T]] { }

given listOrd[T] (using Ord[T]) for Ord[List[T]] { }

given listOrd[T] (using ord: Ord[T]) for Ord[List[T]] { }

given listOrd[T] 
  (using ord: Ord[T]) 
  for Ord[List[T]] {   
}

given for ExecutionContext = new ForkJoinPool()

given global for ExecutionContext = new ForkJoinPool()

given (using outer: Context) for Context = outer.withOwner(currentOwner)

given [Left, Right]
  (using lubLeft: Lub[Right], lubRight: Lub[Right])
  (using lub2: Lub2[lubLeft.Output, lubRight.Output])
  for Lub[Left | Right] { 
}

def foo(using Context) = { }

foo(using ctx)

class C(using Context) extends A with B

new C(using ctx)

new C(using ctx) with D

def instrumented[D, R, A]
  (using instrument: Instrument[R])
  (block: Connector[D] ?=> A)
  (using Connector[D])
  : instrument.Instrumented[A] = {
}

instrumented(using instrument) { 1 }

```
