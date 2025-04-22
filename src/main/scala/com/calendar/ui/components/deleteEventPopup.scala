package com.calendar.ui.components

import com.calendar.models.Event
import javafx.scene.control.SelectionMode
import scalafx.scene.control.{
  ButtonBar,
  ButtonType,
  Dialog,
  ListCell,
  ListView
}
import scalafx.stage.Window
import scalafx.Includes.jfxDialogPane2sfx
import scalafx.Includes.jfxNode2sfx
import scalafx.scene.SceneIncludes.jfxMultipleSelectionModel2sfx
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Insets
import scalafx.scene.layout.{ GridPane, VBox }

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object deleteEventPopup {

  // Current date
  val today = LocalDate.now()

  // The pattern to use with java.time.format
  val dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

  // Creates a dialog
  def showDialog(parentWindow: Window, events: Seq[Event]) =
    val dialog = new Dialog[Event]() {
      initOwner(parentWindow)
      title = "Delete event"
      headerText = "Select an event to delete"
      resizable = true
    }

    // Sets the button types
    val deleteButtonType = new ButtonType("Delete", ButtonBar.ButtonData.OKDone)
    dialog.dialogPane().buttonTypes = Seq(deleteButtonType, ButtonType.Cancel)

    // ListView to display events in the calendar
    val eventListView = new ListView[Event]() {
      items = ObservableBuffer.from(events)

      // cellFactory to enable cell edit/format
      cellFactory = { (_ : ListView[Event]) =>
        new ListCell[Event] {
          item.onChange { (_, _, newValue) =>
            if (newValue != null) then
              text =
                s"${newValue.name} ${newValue.startingTime.format(dateTimeFormatter)}"
            else text = null // If empty
          }
        }
      }
    }

    // User can select single item within eventListView
    eventListView.selectionModel().setSelectionMode(SelectionMode.SINGLE)

    val deleteButton = dialog.dialogPane().lookupButton(deleteButtonType)
    deleteButton.disable = true // Delete button is disabled as default

    // Deletebutton is shown when events are selected
    eventListView.selectionModel().selectedItem.onChange {
      (_, _, newValue) =>
        deleteButton.disable =
          newValue == null // Delete button is shown if some events are selected
    }
    // ViewBox has the listView as a child
    val viewBox = new VBox(10) {
      children = Seq(eventListView)
      padding = Insets(10)
    }
    dialog.dialogPane().content = viewBox

    // Result converter
    dialog.resultConverter = dialogButton => {
      if (dialogButton == deleteButtonType) then
        eventListView.selectionModel().getSelectedItem
      else null
    }
    val result = dialog.showAndWait()
    result match
      case Some(event) => Some(event)
      case _           => None
}
