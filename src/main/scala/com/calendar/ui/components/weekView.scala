package com.calendar.ui.components

import com.calendar.models.Event
import com.calendar.ui.constants
import scalafx.geometry.Pos
import scalafx.geometry.Pos.TopCenter
import scalafx.scene.control.OverrunStyle.Clip
import scalafx.scene.control.{ Button, Label, ScrollPane }
import scalafx.scene.layout.{ GridPane, HBox, VBox }
import scalafx.scene.text.{ Font, FontPosture, FontWeight }

import java.time.*
import scala.collection.mutable

// TODO: Impelement a button which changes to the dailyView

object weekView extends HBox {
  spacing = 10
  alignment = TopCenter

  val fontSize = constants.windowWidth * 0.015

  // Days grid
  val daysGrid = new GridPane:
    alignment = TopCenter
    hgap = constants.windowWidth * 0.01

    gridLinesVisible = true

  // Current date data
  val today = LocalDate.now
  val month = today.getMonth
  val year = today.getYear
  val daysInThisMonth =
    YearMonth.of(year, month).lengthOfMonth()
  // with sets the weekday name and date right
  val startOfWeek = today.`with`(DayOfWeek.MONDAY)

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

  // Event handler for day button clicks
  var onDaySelected: (LocalDate) => Unit = event => {}

  // Method to set the day selection handler
  def setOnDaySelected(handler: (LocalDate) => Unit): Unit = {
    onDaySelected = handler
  }

  weekDays.zipWithIndex.foreach { case (day, columnIndex) =>
    // VBOX is a dayColumn
    val dayColumn = new VBox {
      spacing = 10
      alignment = TopCenter
      style =
        "-fx-border-color: black; -fx-border-width: 2px; -fx-border-radius: 5px;"
    }

    val date = startOfWeek.plusDays(columnIndex)

    // Label to name of the day
    val dayLabel = new Label(day):
      // Bold for current day
      if (today == date) then
        font = Font.font(
          "Montserrat",
          FontWeight.Bold,
          FontPosture.Regular,
          fontSize
        )
      else
        font = Font.font(
          "Montserrat",
          FontWeight.Light,
          FontPosture.Regular,
          fontSize
        )
        textOverrun = Clip

    // Label to date of the day
    val dateButton = new Button(startOfWeek.plusDays(columnIndex).toString):
      // Bold for current date
      if (today == date) then
        font =
          Font.font("Montserrat", FontWeight.Bold, constants.windowWidth * 0.01)
      else
        font = Font.font(
          "Montserrat",
          FontWeight.Light,
          constants.windowWidth * 0.01
        )
        // Handle click event
        onAction = event => onDaySelected(date)

      style = "-fx-background-color: " +
        "#c3c4c4, " +
        "linear-gradient(#d6d6d6 50%, white 100%), " +
        "radial-gradient(center 50% -40%, radius 200%, #e6e6e6 45%, rgba(230,230,230,0) 50%); " +
        "-fx-background-radius: 30; " +
        "-fx-background-insets: 0,1,1; " +
        "-fx-text-fill: black; " +
        "-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 3, 0.0 , 0 , 1 );"

    val dateLabel = new Label(date.toString) {
      font =
        Font.font("Montserrat", FontWeight.Light, constants.windowWidth * 0.01)
    }

    //  Container for day and date
    val headerBox = new VBox:
      alignment = TopCenter
      children = Seq(dayLabel, dateButton)

    // Container with a scrollPane
    val eventsContainer = new VBox:
      spacing = constants.windowWidth * 0.005
      alignment = TopCenter
      prefHeight = constants.windowHeight * 0.8

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
