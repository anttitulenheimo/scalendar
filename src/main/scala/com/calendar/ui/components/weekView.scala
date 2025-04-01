package com.calendar.ui.components

import com.calendar.models.Event
import com.calendar.ui.constants
import scalafx.geometry.Pos
import scalafx.geometry.Pos.TopCenter
import scalafx.scene.control.OverrunStyle.Clip
import scalafx.scene.control.{ Label, ScrollPane }
import scalafx.scene.layout.{ GridPane, HBox, Priority, VBox }
import scalafx.scene.text.{ Font, FontPosture, FontWeight }

import java.time.*
import scala.collection.mutable

// TODO: Impelement a button which changes to the dailyView

object weekView extends HBox {
  spacing = 10
  alignment = TopCenter

  val fontSize = constants.windowWidth * 0.015

  // Days grid: right side
  val daysGrid = new GridPane:
    alignment = TopCenter
    hgap = 5
    vgap = 5
    gridLinesVisible = true

  // Current date data
  val today = LocalDate.now
  val month = today.getMonth
  val year = today.getYear
  val daysInThisMonth =
    YearMonth.of(year, month).lengthOfMonth()
  val currentDayOfWeek =
    today.getDayOfWeek.getValue % 7 // Returns an Int
  val startOfWeek = today.minusDays(currentDayOfWeek)

  // a list of weekdays
  val weekDays = List(
    "Monday",
    "Tuesday",
    "Wednesday",
    "Thursday",
    "Friday",
    "Saturday",
    "Sunday"
  )

  // Map to store day columns
  private val dayColumns = mutable.Map[String, VBox]()

  weekDays.zipWithIndex.foreach { case (day, columnIndex) =>
    // VBOX is a dayColumn
    val dayColumn = new VBox {
      spacing = 10
      alignment = TopCenter
      style =
        "-fx-border-color: black; -fx-border-width: 2px; -fx-border-radius: 5px;"
    }

    // Label to name of the day
    val dayLabel = new Label(day):
      font =
        Font.font("Montserrat", FontWeight.Light, FontPosture.Regular, fontSize)
      textOverrun = Clip

    // Label to date of the day
    val dateLabel = new Label(startOfWeek.plusDays(columnIndex).toString):
      font =
        Font.font("Montserrat", FontWeight.Light, constants.windowWidth * 0.01)

    //  Container for day and date
    val headerBox = new VBox:
      alignment = TopCenter
      children = Seq(dayLabel, dateLabel)

    // Container with a scrollPane
    val eventsContainer = new VBox:
      spacing = 5
      alignment = TopCenter
      minHeight = constants.windowHeight // TODO: Implement a better minHeight

    // Allows the user to scroll the content
    val scrollPane = new ScrollPane:
      content = eventsContainer
      fitToWidth = true

    dayColumn.children = Seq(headerBox, scrollPane)
    dayColumns(day) = eventsContainer
    daysGrid.add(dayColumn, columnIndex, 0)
  }

  // Adds components to main container
  children = Seq(daysGrid)

  // Adds a single event: For the user to do
  def addEvent(event: Event): Unit = {
    val eventDisplay = eventView.createEventDisplay(event)

    val eventDay = event.date.getDayOfWeek.toString.toLowerCase.capitalize
    dayColumns.get(eventDay).foreach(_.children.add(eventDisplay))
  }

  // Add all  the events
  def addEvents(events: Seq[Event]): Unit = {
    events.foreach(addEvent)
  }
}
