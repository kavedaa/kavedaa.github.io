package basic

import scala.deriving.*
import scala.compiletime.*

import java.time.LocalDate

import javafx.scene.Node
import javafx.scene.layout.*
import javafx.scene.control.*

enum Container(label: String):
  case Primitive(label: String, node: Node) extends Container(label)
  case Composite(label: String, containers: Seq[Container]) extends Container(label)

trait Editor[T]: 
  def getValue: T
  def setValue(x: T): Unit
  def container(label: String): Container

object Editor:

  trait FreshInstance

  object FreshInstance:
    given FreshInstance = new FreshInstance {}

  given (using FreshInstance): Editor[String] with
    val textField = new TextField
    def getValue = textField.getText
    def setValue(x: String) = textField.setText(x)
    def container(label: String) = Container.Primitive(label, textField)

  given (using FreshInstance): Editor[LocalDate] with
    val datePicker = new DatePicker
    def getValue = datePicker.getValue
    def setValue(x: LocalDate) = datePicker.setValue(x)
    def container(label: String) = Container.Primitive(label, datePicker)

  inline given [A <: Product] (using m: Mirror.ProductOf[A]): Editor[A] =
    new Editor[A]:
      val labels = constValueTuple[m.MirroredElemLabels].toList.asInstanceOf[List[String]]
      type ElemEditors = Tuple.Map[m.MirroredElemTypes, Editor]
      val elemEditors = summonAll[ElemEditors].toList.asInstanceOf[List[Editor[Any]]]
      val containers = labels.zip(elemEditors) map { (label, editor) => editor.container(label) }
      def getValue = 
        val values = elemEditors.map(_.getValue)
        val tuple = values.foldRight[Tuple](EmptyTuple)(_ *: _)
        m.fromProduct(tuple)            
      def setValue(a: A) =
        val elems = a.productIterator.toList
        elems.zip(elemEditors) foreach { (elem, editor) => 
          editor.setValue(elem)
        }
      def container(label: String) = Container.Composite(label, containers)

end Editor


trait Layouter:
  def layoutPrimitive(primitive: Container.Primitive): Node
  def layoutComposite(composite: Container.Composite): Node
  def layout(container: Container): Node = container match 
    case primitive: Container.Primitive => layoutPrimitive(primitive)
    case composite: Container.Composite => layoutComposite(composite)

object Layouter:

  object Default extends Layouter:
    def layoutPrimitive(primitive: Container.Primitive) =
      VBox(5, new Label(primitive.label), primitive.node)
    def layoutComposite(composite: Container.Composite) =      
      VBox(10, composite.containers.map(layout)*)

end Layouter


class DerivedDialog[A >: Null : Editor](value0: Option[A] = None) extends Dialog[A]:

  val editor = summon[Editor[A]]
  val layouter = Layouter.Default

  val content = layouter.layout(editor.container(""))

  getDialogPane.setContent(content)

  getDialogPane.getButtonTypes.add(ButtonType.OK)

  setResultConverter { (dialogButtonType: ButtonType) =>
    if (dialogButtonType == ButtonType.OK) editor.getValue
    else null
  }

  value0 foreach editor.setValue

end DerivedDialog