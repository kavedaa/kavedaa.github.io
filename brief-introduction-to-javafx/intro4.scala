//> using jvm 20
//> using scala 3
//> using dep org.openjfx:javafx-controls:21

import javafx.application.*
import javafx.stage.*
import javafx.scene.*
import javafx.scene.control.*
import javafx.scene.layout.VBox
import javafx.geometry.Insets

@main def main =
  Application.launch(classOf[Intro])
  
class Intro extends Application:

  def start(stage: Stage) =

    val label = Label("This is a label")
    val button = Button("This is a button")
    val textField = new TextField
    val datePicker = new DatePicker

    val vbox = new VBox:
      getChildren.addAll(label, button, textField, datePicker)
      setSpacing(10)
      setPadding(Insets(10))

    textField.textProperty.addListener(_ => println(textField.getText))

    label.textProperty.bind(textField.textProperty)

    val scene = Scene(vbox)
    stage.setScene(scene)
    stage.show()