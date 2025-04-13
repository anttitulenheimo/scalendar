package com.calendar.ui.components

import com.calendar.models.{ Category, Event, Reminder }
import com.calendar.services.EventValidator
import com.calendar.ui.constants
import scalafx.beans.Observable
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Insets
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.ButtonBar.ButtonData.OKDone
import scalafx.scene.control.{
  Alert,
  Button,
  ButtonBar,
  ButtonType,
  CheckBox,
  ColorPicker,
  ComboBox,
  DatePicker,
  Dialog,
  Label,
  TextArea,
  TextField
}
import scalafx.stage.Window
import scalafx.scene.control.ControlIncludes.jfxDialogPane2sfx
import scalafx.scene.SceneIncludes.jfxNode2sfx
import scalafx.scene.paint.PaintIncludes.jfxColor2sfx
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.layout.GridPane
import scalafx.scene.paint.Color

import scala.util.{ Failure, Success, Try }
import java.time.format.DateTimeFormatter
import java.time.{ LocalDate, LocalDateTime, LocalTime }

// TODO: Make possible for the events to last days 


//The popup is a dialog box
object addEventPopup {

  val today = LocalDate.now()

  // Creates a dialog
  // Returns null instead of None because scalafx is java based
  def showDialog(parentWindow: Window, categories: Seq[Category]): Any = {
    val dialog = new Dialog[Event]() {
      initOwner(parentWindow)
      title = "Add a new event"
      headerText = "Create a new calendar event"
      resizable = true
    }

    // Sets the button types
    val addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OKDone)
    dialog.dialogPane().buttonTypes = Seq(addButtonType, ButtonType.Cancel)

    // Gridlayout for the dialog
    val grid = new GridPane() {
      hgap = 10
      vgap = 10
      padding = Insets(20)
    }

    // Event's date and time handling
    // Cretes a new datepicker to pick the date either writing or c hoosing from the mini calendar
    val datePicker = new DatePicker(today)
    grid.add(new Label("Date:"), 0, 0)
    grid.add(datePicker, 1, 0)
    // The pattern to use with java.time.format
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val startingTimeText = LocalTime.now().withSecond(0).withNano(0)
    val startingTimeTextField = new TextField() {
      promptText = startingTimeText.toString
      text = startingTimeText.format(
        timeFormatter
      ) // The current hour is set as default
    }
    grid.add(new Label("Start Time:"), 0, 2) // (0,2)
    grid.add(startingTimeTextField, 1, 2) // (1,2)

    val endingTimeText = startingTimeText.plusHours(1)
    val endingTimeTextField = new TextField() {
      promptText = endingTimeText.toString
      text = endingTimeText.format(
        timeFormatter
      ) // The current hour plus 1 is set as default
    }
    grid.add(new Label("End Time:"), 0, 3) // (0,3)
    grid.add(endingTimeTextField, 1, 3) // (1,2)

    // Create the eventData labels and fields
    val eventName = new TextField() {
      promptText = "name"
    }
    grid.add(new Label("Event Name:"), 0, 1)
    grid.add(eventName, 1, 1)

    // Sequence of categories for the combobox
    val categoriesOptionsSeq =
      if (categories.isEmpty) then Seq(constants.defaultCategory)
      else categories

    // The instance of category combobox
    val categoryComboBox = new ComboBox[String] {
      items = ObservableBuffer.from(categoriesOptionsSeq.map(_.name))
      // Combobox requires ObservableBuffer as a list
      value = categoriesOptionsSeq.head.name // Default value
    }
    grid.add(new Label("Category:"), 0, 4) // (0,4)
    grid.add(categoryComboBox, 1, 4) // (1,4)

    // The color will be added with a ColorPicker
    val eventColorPicker = new ColorPicker(
      Color.web(constants.eventDefaultColor)
    ) // Default color will be added
    grid.add(new Label("Color:"), 0, 5) // (0,5)
    grid.add(eventColorPicker, 1, 5) // (1,5)

    // Additional info field
    val additionalInfoField = new TextArea() {
      promptText = "Additional Information (optional)"
      prefRowCount = 3
      wrapText = true
    }

    grid.add(new Label("Additional Info:"), 0, 6)
    grid.add(additionalInfoField, 1, 6)

    // Possibility to add a reminder for the event using a checkBox
    val addReminderCheckBox = new CheckBox("Add Reminder")
    val reminderTextField = new TextField() {
      promptText = "Minutes before event"
      disable = true // Reminder checkbox default is disabled
    }
    grid.add(addReminderCheckBox, 0, 7) // (0,7)
    grid.add(new Label("Reminder:"), 1, 7) // (1,7)
    grid.add(reminderTextField, 1, 8) // (1,8)

    // Enable / Disable reminderTextField
    addReminderCheckBox.selected.onChange {
      (observableValue, oldValue, newValue) =>
        reminderTextField.disable = !newValue
    }

    dialog.dialogPane().content = grid

    // Enable/Disable login button depending on whether an eventName was
    val addButton = dialog.dialogPane().lookupButton(addButtonType)
    addButton.disable = eventName.text().isEmpty

    // Do some validation (disable when eventName is empty).
    eventName.text.onChange { (observableValue, oldValue, newValue) =>
      addButton.disable = newValue.trim().isEmpty
    }

    // Result converter
    dialog.resultConverter = dialogButton => {
      if (dialogButton == addButtonType) then
        try {
          val name = eventName.text().trim // Trim the excess spaces
          val date = datePicker.value()

          val startTimeLocal = LocalTime.parse(
            startingTimeTextField.text(),
            timeFormatter
          ) // Parse events
          val endTimeLocal =
            LocalTime.parse(endingTimeTextField.text(), timeFormatter)

          val startDateTime = LocalDateTime.of(
            date,
            startTimeLocal
          ) // Converts LocalTime to LocalDateTime
          val endDateTime = LocalDateTime.of(date, endTimeLocal)

          // TODO: Events can last days so the Eventvalidator logic needs to be changed 
          // Use eventValidator to validate user inputs
          if (!EventValidator.validateTime(startDateTime, endDateTime)) then
            ErrorDialog("End time must be after start time")
            return null

          // Find selected category from categoryCombobox or take the default category
          val selectedCategory = categoriesOptionsSeq
            .find(_.name == categoryComboBox.value())
            .getOrElse(constants.defaultCategory)
          val colorCode =
            constants.toCssColor(eventColorPicker.value()) // Parse to hexString

          // Additional info is optional
          val additionalInfo =
            if (additionalInfoField.text().trim.isEmpty)
            then None
            else Some(additionalInfoField.text().trim)

          val eventReminder =
            if (addReminderCheckBox.selected()) then
              Try(reminderTextField.text().toInt) match // Try to parse toInt
                case Success(minutes) =>
                  Some(new Reminder(name, startDateTime.minusMinutes(minutes)))
                case Failure(
                      parsingFailed
                    ) => // If parsing fails then the reminder is set 30 minutes before the event starts
                  Some(new Reminder(name, startDateTime.minusMinutes(30)))
            else None

          // Creates the new event after parsing
          new Event(
            name = name,
            date = date,
            startingTime = startDateTime,
            endingTime = endDateTime,
            category = selectedCategory,
            reminder = eventReminder,
            additionalInfo = additionalInfo,
            colorCode = colorCode
          )
        } catch
          case e: Exception =>
            ErrorDialog(e.getMessage)
            null
      else null
    }

    val result = dialog.showAndWait()
    result

  }
  // Dialogs an Error
  private def ErrorDialog(msg: String): Unit = {
    new Alert(AlertType.Error) {
      title = "Error"
      headerText = "Error creating event"
      contentText = msg
    }.showAndWait()
  }

}
