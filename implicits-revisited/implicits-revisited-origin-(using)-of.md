## Implicits revisited: examples for proposal of origin/using/for

```scala
origin for Bar { }

origin for Ord[Int] { }

origin intOrd for Ord[Int] { }

origin [T] (using Ord[T]) for Ord[List[T]] { }

origin listOrd[T] (using Ord[T]) for Ord[List[T]] { }

origin listOrd[T] (using ord: Ord[T]) for Ord[List[T]] { }

origin listOrd[T] 
  (using ord: Ord[T]) 
  for Ord[List[T]] {   
}

origin for ExecutionContext = new ForkJoinPool()

origin global for ExecutionContext = new ForkJoinPool()

origin (using outer: Context) for Context = outer.withOwner(currentOwner)

origin for Criteria = Criteria.from

origin [Left, Right]
  (using lubLeft: Lub[Right], lubRight: Lub[Right])
  (using lub2: Lub2[lubLeft.Output, lubRight.Output])
  for Lub[Left | Right] { 
}

```
