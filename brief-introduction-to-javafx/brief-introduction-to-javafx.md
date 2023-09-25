# Brief introduction to JavaFX

JavaFX is a GUI framework/toolkit for the Java Platform. JavaFX applications can be written using any language that compile to the JVM. In this introduction we will be using Scala, but the examples could easily be translated to other languages.

## Pre-requisites

In order to run the examples, you must have [Scala CLI](https://scala-cli.virtuslab.org/) installed.

## Minimal application

The following is the minimal code for running a JavaFX application:

```scala
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
```

In order to run it, save the code to a file e.g. *intro1.scala*, and run it with `scala-cli intro1.scala` on the console. It should show a small window with the text "Hello JavaFX!"

Let's look at what happens in this code:

1. A dependency on the JavaFX library is declared. After Java 8, JavaFX is not part of the standard Java distribution and must be explicitly depended on.
2. In the `main` method, the JavaFX method `Application.launch` is starting the JavaFX framework, using the `Intro` class as entry point.
3. A class named `Intro` extending the JavaFX class `Application` is defined, implementing the abstract `start` method.
4. A JavaFX `Scene` is created, with a root node that in our example happens to be simply a `Label` displaying some text.
5. The scene is attached to the `Stage` that has already been created by the framework, and then the stage is shown.

JavaFX is a framework only in the sense that an application built on it must inherit a special class `Application` and implement a special method `start` on that class, that serves as the entry point of the application. Outside of that, JavaFX does not have any requirements for how an application should be structured.

## Basic concepts

### Scene graph

JavaFX is built around what is known as the Scene Graph. For anyone familiar with web programming, this can be compared to the DOM of a web page - a tree-like structure with a root node, and child nodes, and child nodes of child nodes, and so on.

In JavaFX, nodes will be of different kinds, like layout containers, controls, media containers, graphic shapes, and more. Some of these will by their nature have child nodes, and some will not.

In the example above, we used one single `Label` root node.

### Observable data

The other main concept in JavaFX is that of "observable data". These include single-value data as *observable values* (read) and *properties* (read/write), as well as *observable collections*. 

The idea of observable data is that one can, on a low level, register listeners on the data and get notified when it changes, and on a higher level, directly "bind" data to the user interface and vice versa. This ability is used throughout the JavaFX API and means that JavaFX can be called a *reactive* GUI toolkit.

## Layout containers

If we want to have more than one node in a scene, which we ususally do, we have to place the nodes inside some node that accepts child nodes. A simple example of such would be a layout container like `VBox`:

```scala
val label1 = Label("This label")
val label2 = Label("That label")

val vbox = new VBox:
  getChildren.addAll(label1, label2)
  setSpacing(10)
  setPadding(Insets(10))

val scene = Scene(vbox)
```

A layout container is a node that does not have any visual content on its own, but is only used for arranging its child nodes in different ways. A `VBox` arranges all nodes vertically, with optional spacing and padding values. The corresponding horizontal container is called `HBox`. Other layout containers include `GridPane`, `BorderPane`, and more.

Full example [here](intro2.scala).

## Controls

A control is a node that has some visual appearance and typically allows for user interaction. Controls range from simple ones like `Button` and `TextField`, to more complex ones like `TableView` and `DatePicker`. Here are some examples:

```scala
val label = Label("This is a label")
val button = Button("This is a button")
val textField = new TextField
val datePicker = new DatePicker

val vbox = new VBox:
  getChildren.addAll(label, button, textField, datePicker)
```

Controls typically have methods for reading and writing the data represented by the control, as well as methods for responding to user interaction. For example, this is how we can set the label's text based on what the user has entered in the textfield whenever the user clicks the button:

```scala
button.setOnAction: _ =>
  label.setText(s"You entered ${textField.getText}")    
```

Full example [here](intro3.scala).

## Observables and binding

It is possible to react directly to data changes, as long as the data is wrapped in an `ObservableValue`. This includes all the "properties" fields on the controls. For example, this is how we can "listen" to changes on the text in a textfield:

```scala
textField.textProperty.addListener(_ => println(textField.getText))
```

This will print the text in the textfield every time it changes.

It also possible to connect two properties directly. The first property will then always have the same value as the other. This is called "binding". For example, this is how we can bind the text of the label control to the text from the textfield control:

```scala
label.textProperty.bind(textField.textProperty)
```

Full example [here](intro4.scala).


