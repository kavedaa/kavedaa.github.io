package app

import java.time.LocalDate

import javafx.application.*
import javafx.stage.*
import javafx.scene.*
import javafx.scene.control.*

object App:
  def main(args: Array[String]) =
    Application.launch(classOf[App])

class App extends Application:

  def start(stage: Stage) =

    val manualButton = new Button("Manual")
    val basicButton = new Button("Basic")
    val enhancedButton = new Button("Enhanced")
    val toolbar = new ToolBar(manualButton, basicButton, enhancedButton)
    val scene = new Scene(toolbar)

    stage setScene scene
    stage.show()

    manualButton setOnAction { _ =>
      val person = manual.Person("John", "Smith", LocalDate.now)
      val dialog = new manual.PersonDialog(Some(person))
      val res = dialog.showAndWait()
      res.ifPresent { person =>
        println(s"Got $person")
      }
    }

    basicButton setOnAction { _ =>
      val person = manual.Person("John", "Smith", LocalDate.now)
      val dialog = new basic.DerivedDialog[manual.Person](Some(person))
      val res = dialog.showAndWait()
      res.ifPresent { person =>
        println(s"Got $person")
      }
    }        

    enhancedButton setOnAction { _ =>
      case class Pet(name: String, isMammal: Boolean)
      case class Person(firstName: String, lastName: String, birthDate: LocalDate, pet: Pet)
      import enhanced.*
      given (using Editor.FreshInstance): Editor[Boolean] with
        val checkBox = new CheckBox
        def getValue = checkBox.isSelected
        def setValue(t: Boolean) = { checkBox setSelected t }
        def container(label: String) = Container.SinglePrimitive(label, checkBox)
      val person = Person("John", "Smith", LocalDate.now, Pet("Fido", true))
      val dialog = new DerivedDialog(Some(person))
      val res = dialog.showAndWait()
      res.ifPresent { person =>
        println(s"Got $person")
      }
    }        

end App