package com.calendar.ui.components

import com.calendar.models.Event
import com.calendar.ui.constants
import scalafx.geometry.Pos.Center
import scalafx.scene.control.Label
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color
import scalafx.scene.text.{ Font, FontWeight }

import java.time.format.DateTimeFormatter
import scala.util.{ Failure, Success, Try }

object eventView {

  val eventHeadingfontSize = constants.windowWidth * 0.01
  val eventNormalFont = constants.windowWidth * 0.008

  // Formats the given inputs
  private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
  private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

  // Creates an event display
  def createEventDisplay(event: Event): VBox = {
    // Tries to set a background color otherwise sets a default color
    val background =
      Try(Color.web(event.colorCode)) match
        case Success(realColor) =>
          realColor
        case Failure(exception) =>
          constants.eventDefaultColor

    // Creates an event box where event's parameters are shown
    val eventBox = new VBox() {
      alignment = Center
    }

    // Displays event's name
    val eventHeading = new Label(event.name) {
      font = Font.font("Montserrat", FontWeight.Bold, eventHeadingfontSize)
    }

    // Displays timedata
    val timeLabel = new Label(
      s"${event.startingTime.format(timeFormatter)} - ${event.endingTime.format(timeFormatter)}"
    ) {
      font = Font.font("Montserrat", FontWeight.Light, eventNormalFont)
    }

    // Displays category name
    val categoryLabel = new Label(event.category.name) {
      font = Font.font("Montserrat", FontWeight.Light, eventNormalFont)
    }

    // Displays additional info
    val additionalInfoLabel = event.additionalInfo match
      case Some(additionalInfo) =>
        new Label(additionalInfo) {
          font = Font.font("Montserrat", FontWeight.Light, eventNormalFont)
        }
      case _ =>
        new Label("No additional info") {
          font = Font.font("Montserrat", FontWeight.Light, eventNormalFont)
        }

    eventBox.children.addAll(eventHeading, timeLabel, categoryLabel)
    eventBox

  }

}
