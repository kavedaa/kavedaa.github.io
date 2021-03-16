package manual

import java.time.*

import javafx.scene.layout.*
import javafx.scene.control.*

case class Person(firstName: String, lastName: String, birthDate: LocalDate)

class PersonDialog(person0: Option[Person]) extends Dialog[Person] {

  val firstNameField, lastNameField = new TextField
  val birthDatePicker = new DatePicker

  val content = new VBox(10) {
    getChildren.addAll(
      new VBox(5, new Label("First name"), firstNameField),
      new VBox(5, new Label("Last name"), lastNameField),
      new VBox(5, new Label("Birth date"), birthDatePicker)
    )
  }

  getDialogPane.setContent(content)

  getDialogPane.getButtonTypes.add(ButtonType.OK)

  setResultConverter { (dialogButtonType: ButtonType) =>
    if (dialogButtonType == ButtonType.OK) 
      Person(
        firstNameField.getText, 
        lastNameField.getText,
        birthDatePicker.getValue)
    else null
  }

  person0 foreach { person =>
    firstNameField.setText(person.firstName)
    lastNameField.setText(person.lastName)
    birthDatePicker.setValue(person.birthDate)
  }

}