//> using jvm 20
//> using scala 3
//> using dep org.openjfx:javafx-controls:21

import javafx.application.*
import javafx.stage.*
import javafx.scene.*
import javafx.scene.control.Label
import javafx.scene.layout.VBox
import javafx.geometry.Insets

@main def main =
  Application.launch(classOf[Intro])
  
class Intro extends Application:

  def start(stage: Stage) =

    val label1 = Label("This label")
    val label2 = Label("That label")

    val vbox = new VBox:
      getChildren.addAll(label1, label2)
      setSpacing(10)
      setPadding(Insets(10))

    val scene = Scene(vbox)
    stage.setScene(scene)
    stage.show()