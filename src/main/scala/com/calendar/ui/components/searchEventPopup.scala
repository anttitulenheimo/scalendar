package com.calendar.ui.components

import com.calendar.models.Event
import javafx.scene.control.SelectionMode
import scalafx.Includes.{ jfxDialogPane2sfx, jfxNode2sfx }
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Insets
import scalafx.scene.control.*
import scalafx.scene.layout.VBox
import scalafx.stage.Window

import scala.jdk.CollectionConverters.*

object searchEventPopup {

  // Creates a dialog
  def showDialog(parentWindow: Window, events: Seq[Event]): Option[Seq[Event]] =
    val dialog = new Dialog[Seq[Event]]() {
      initOwner(parentWindow)
      title = "Search events"
      headerText = "Select an events to filter"
      resizable = true
    }

    // Sets the button types
    val searchButtonType = new ButtonType("Search", ButtonBar.ButtonData.OKDone)
    dialog.dialogPane().buttonTypes = Seq(searchButtonType, ButtonType.Cancel)

    // TextField where for the user input
    val searchTextField = new TextField {
      promptText = "Enter keywoard to search an event"
    }

    // ListView to display events
    val eventListView = new ListView[Event]() {
      items = ObservableBuffer.from(events)

      // cellFactory to  enable cell edit/format
      cellFactory = { (_: ListView[Event]) =>
        new ListCell[Event] {
          item.onChange { (_, _, newValue) =>
            if (newValue != null) then
              // Events formatted for the display
              val dateString = newValue.date.toString
              val timeString =
                s"${newValue.startingTime.toLocalTime} - ${newValue.endingTime.toLocalTime}"
              val categoryString = newValue.category.name
              val additionalInfoString = newValue.additionalInfo.getOrElse("")

              text =
                s"Event name: ${newValue.name}\nEvent date and time:($dateString, $timeString)" +
                  s"\nEvent category:$categoryString\nEvent additional info: $additionalInfoString"
            else text = null // If empty
          }
        }
      }
    }

    // Contains the items that match the searchWord
    val searchedEvents = ObservableBuffer[Event]()
    eventListView.items = searchedEvents

    // Disable search button on if an searchTextField is empty
    val searchButton = dialog.dialogPane().lookupButton(searchButtonType)
    searchButton.disable = searchTextField.text().isEmpty

    // Do some validation
    searchTextField.text.onChange { (_, _, newValue) =>
      searchButton.disable = newValue.trim().isEmpty
    }

    // Search functionality
    searchTextField.text.onChange { (_, _, newValue) =>
      if (newValue != null && newValue.nonEmpty) then
        val searchWord = newValue.toLowerCase
        val filteredEvents = events.filter(event =>
          event.name.toLowerCase.contains(searchWord) || event.additionalInfo
            .exists(
              _.toLowerCase.toLowerCase.contains(searchWord)
            ) || event.category.name.toLowerCase.contains(searchWord)
        )
        // Update the observableBuffer
        searchedEvents.clear()
        searchedEvents.addAll(filteredEvents)

        // Automatically select all the events matching the search word
        filteredEvents.indices.foreach(event =>
          eventListView.selectionModel().select(event)
        )
      else searchedEvents.clear() // If no events then cleared
    }

    // Multiple items are selected
    eventListView.selectionModel().setSelectionMode(SelectionMode.MULTIPLE)

    // ViewBox has the eventListView as a child
    val viewBox = new VBox(10) {
      children = Seq(searchTextField, eventListView)
      padding = Insets(10)
    }

    dialog.dialogPane().content = viewBox

    // Result converter
    dialog.resultConverter = dialogButton => {
      if (dialogButton == searchButtonType) then {
        eventListView.selectionModel().getSelectedItems.asScala.toSeq
      } else null
    }

    val result = dialog.showAndWait()

    result match {
      case Some(events) => Some(events.asInstanceOf[Seq[Event]])
      case _                        => None
    }

}
