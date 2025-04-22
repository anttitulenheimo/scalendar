package com.calendar.ui.components

import com.calendar.models.Category
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
import scalafx.scene.layout.VBox

import scala.jdk.CollectionConverters._

object categoryFilterPopup {

  // Creates a dialog
  def showDialog(
    parentWindow: Window,
    categories: Seq[Category]
  ): Option[Seq[Category]] = {
    val dialog = new Dialog[Seq[Category]]() {
      initOwner(parentWindow)
      title = "Filter by category"
      headerText = "Hold Ctrl or ⌘ Command to select multiple categories"
      resizable = true
    }

    // Set the button types
    val filterButtonType = new ButtonType("Filter", ButtonBar.ButtonData.OKDone)
    dialog.dialogPane().buttonTypes = Seq(filterButtonType, ButtonType.Cancel)

    // ListView to display categories
    val categoryListView = new ListView[Category]() {
      items = ObservableBuffer.from(categories)

      // cellFactory to enable cell edit/format
      cellFactory = { (_: ListView[Category]) =>
        new ListCell[Category] {
          item.onChange { (_, _, newValue) =>
            if (newValue != null) then text = newValue.name
            else text = null // If empty
          }
        }
      }
    }
    // User can select multiple items within categoryListView
    categoryListView.selectionModel().setSelectionMode(SelectionMode.MULTIPLE)

    val filterButton = dialog.dialogPane().lookupButton(filterButtonType)
    filterButton.disable = true // Filter button is disabled as default

    // Buttons are shown when categories are selected
    categoryListView.selectionModel().selectedItems.onChange { (_, newValue) =>
      filterButton.disable = newValue.isEmpty
    }

    // ViewBox has the categoryListView as a child
    val viewBox = new VBox(10) {
      children = Seq(categoryListView)
      padding = Insets(10)
    }
    dialog.dialogPane().content = viewBox

    // Result converter
    dialog.resultConverter = dialogButton => {
      if (dialogButton == filterButtonType) then
        categoryListView.selectionModel().getSelectedItems.asScala.toSeq
      else null
    }

    val result = dialog.showAndWait()
    result match {
      case Some(categories: Seq[Category]) => Some(categories)
      case _                               => None
    }

  }
}
