//> using jvm 20
//> using scala 3
//> using dep org.openjfx:javafx-controls:21

import javafx.application.*
import javafx.stage.*
import javafx.scene.*
import javafx.scene.control.Label

@main def main =
  Application.launch(classOf[Intro])
  
class Intro extends Application:
  def start(stage: Stage) =
    val root = Label("Hello JavaFX!")
    val scene = Scene(root)
    stage.setScene(scene)
    stage.show()