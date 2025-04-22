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

//The popup is a dialog box
object addEventPopup {

  // Current date
  val today = LocalDate.now()

  // Creates a dialog
  // Returns null instead of None because scalafx is java based
  def showDialog(
    parentWindow: Window,
    categories: Seq[Category],
    startDateTime: LocalDateTime,
    endDateTime: LocalDateTime
  ): Option[Event] = {
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
    val startDatePicker = new DatePicker(today)
    val endingDatePicker = new DatePicker(today)

    grid.add(new Label("Starting date:"), 0, 0)
    grid.add(startDatePicker, 1, 0)
    grid.add(new Label("Ending date:"), 2, 0)
    grid.add(endingDatePicker, 3, 0)
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

    // Default values for the next if
    val defaultStartTime = LocalDateTime.now().withSecond(0).withNano(0)
    val defaultEndTime = defaultStartTime.plusHours(1)
    // Formatting and setting the arguments if the values are from the dragToAddEvent
    if (
      (startDateTime.toLocalDate != LocalDate.now() ||
      startDateTime.toLocalTime != defaultStartTime.toLocalTime ||
      endDateTime.toLocalDate != LocalDate.now() ||
      endDateTime.toLocalTime != defaultEndTime.toLocalTime)
    ) then
      startDatePicker.value = startDateTime.toLocalDate
      endingDatePicker.value = endDateTime.toLocalDate
      startingTimeTextField.text = startDateTime.format(timeFormatter)
      endingTimeTextField.text = endDateTime.format(timeFormatter)

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
    addReminderCheckBox.selected.onChange { (_, _, newValue) =>
      reminderTextField.disable = !newValue
    }

    dialog.dialogPane().content = grid

    // Enable/Disable login button depending on whether an eventName was empty
    val addButton = dialog.dialogPane().lookupButton(addButtonType)
    addButton.disable = eventName.text().isEmpty

    // Do some validation (disable when eventName is empty).
    eventName.text.onChange { (_, _, newValue) =>
      addButton.disable = newValue.trim().isEmpty
    }

    // Result converter
    dialog.resultConverter = dialogButton => {
      if (dialogButton == addButtonType) then
        try {
          val name = eventName.text().trim // Trim the excess spaces
          val startDate = startDatePicker.value()
          val endDate = endingDatePicker.value()

          val startTimeLocal = LocalTime.parse(
            startingTimeTextField.text(),
            timeFormatter
          ) // Parse events
          val endTimeLocal =
            LocalTime.parse(endingTimeTextField.text(), timeFormatter)

          val startDateTime = LocalDateTime.of(
            startDate,
            startTimeLocal
          ) // Converts LocalTime to LocalDateTime
          val endDateTime = LocalDateTime.of(endDate, endTimeLocal)

          // Use eventValidator to validate user inputs
          if (!EventValidator.validateTime(startDateTime, endDateTime)) then
            if (startDateTime.toLocalDate() != endDateTime.toLocalDate()) then
              // Multi day event
              ErrorDialog("End date must be after start date")
            else
              // Single day event
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
                      _
                    ) => // If parsing fails then the reminder is set 30 minutes before the event starts
                  Some(new Reminder(name, startDateTime.minusMinutes(30)))
            else None

          // Creates the new event after parsing
          new Event(
            name = name,
            date = startDate,
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
    result match
      case Some(event) =>
        Some(
          event.asInstanceOf[com.calendar.models.Event]
        ) // The right event type specified
      case _ => None

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
