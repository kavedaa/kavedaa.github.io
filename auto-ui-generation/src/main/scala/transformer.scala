package transformer

import scala.deriving.*
import scala.compiletime.*

import java.time.LocalDate

trait Transformer[T]:
  def f(x: T): T

object Transformer:

  given Transformer[String] with
    def f(x: String) = x.toUpperCase

  given Transformer[LocalDate] with
    def f(x: LocalDate) = x plusDays 1

  given Transformer[Boolean] with
    def f(x: Boolean) = !x

  inline given [A <: Product] (using m: Mirror.ProductOf[A]): Transformer[A] =
    new Transformer[A]:
      val elemTransformers = summonAll[Tuple.Map[m.MirroredElemTypes, Transformer]].toList.asInstanceOf[List[Transformer[Any]]]  
      def f(a: A): A = 
        val elems = a.productIterator.toSeq
        val transformed = elems.zip(elemTransformers) map { case (elem, transformer) => 
          transformer.f(elem) 
        }
        val tuple = transformed.foldRight[Tuple](EmptyTuple)(_ *: _)
        m.fromProduct(tuple)      

end Transformer
