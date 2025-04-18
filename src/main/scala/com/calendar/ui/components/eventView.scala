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

  // TODO: Make events clickable and therefore implementing a possibility for event to be deleted 
  // Creates an event display
  def createEventDisplay(event: Event): VBox = {
    // Tries to set a background color otherwise sets a default color
    val backgroundColor =
      Try(Color.web(event.colorCode)) match
        case Success(realColor) =>
          constants.toCssColor(realColor)
        case Failure(exception) =>
          constants.eventDefaultColor

    // Creates an event box where event's parameters are shown
    val eventBox = new VBox() {
      alignment = Center
      style = s"-fx-background-color: $backgroundColor ;"
    }

    // Displays event's name
    val eventHeading = new Label(event.name) {
      font = Font.font("Montserrat", FontWeight.Bold, eventHeadingfontSize)
    }

    // Display time data
    val timeLabel =
      // Get the event dates
      val eventStartDate = event.startingTime.toLocalDate
      val eventEndDate = event.endingTime.toLocalDate

      // Parse timedata
      // Single day event
      val timeText =
        if (eventStartDate.equals(eventEndDate)) then
          // Show times
          s"${event.startingTime.format(timeFormatter)} - ${event.endingTime.format(timeFormatter)}"
        else // Multi day event
          // Show dates and times
          s"${eventStartDate.format(dateFormatter)} - ${eventEndDate.format(dateFormatter)}\n" +
            s"${event.startingTime.format(timeFormatter)} - ${event.endingTime.format(timeFormatter)}"

      new Label(timeText) {
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

    eventBox.children.addAll(
      eventHeading,
      timeLabel,
      categoryLabel,
      additionalInfoLabel
    )
    eventBox

  }

}
