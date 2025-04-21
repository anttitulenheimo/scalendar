package com.calendar.ui.components

import com.calendar.models.Event
import com.calendar.ui.constants
import scalafx.geometry.Pos
import scalafx.geometry.Pos.TopCenter
import scalafx.scene.control.OverrunStyle.Clip
import scalafx.scene.control.{Button, Label, ScrollPane}
import scalafx.scene.layout.{GridPane, HBox, VBox}
import scalafx.scene.text.{Font, FontPosture, FontWeight}

import java.time.{DayOfWeek, LocalDate}
import scala.collection.mutable

object weekView extends HBox {
  spacing = 10
  alignment = TopCenter

  val fontSize = constants.windowWidth * 0.015

  // Days grid
  val daysGrid = new GridPane:
    alignment = TopCenter
    hgap = constants.windowWidth * 0.01

    gridLinesVisible = false

  // Current date data
  val today = LocalDate.now
  val month = today.getMonth
  val year = today.getYear
  // with sets the weekday name and date right
  var startOfWeek = today.`with`(DayOfWeek.MONDAY)

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

  // Map to store dayLabels
  private val dayLabels = mutable.Map[String, Label]()

  // Maps to store dateButtons
  private val dateButtons = mutable.Map[String, Button]()

  // Map to store day columns
  private val dayColumns = mutable.Map[String, VBox]()

  // Event handler for day button clicks
  var onDaySelected: (LocalDate) => Unit = _ => {}

  // Method to set the day selection handler
  def setOnDaySelected(handler: (LocalDate) => Unit): Unit = {
    onDaySelected = handler
  }

  // Method to select the right week
  def weekViewDatesRefresher(date: LocalDate) =
    // The new given date from the refreshWeekView()
    startOfWeek = date

    // Updates the dates right
    weekDays.zipWithIndex.foreach { case (day, columnIndex) =>
      val newDate = startOfWeek.plusDays(columnIndex)
      dateButtons
        .get(day)
        .foreach(button =>
          button.setText(newDate.toString) // Change the text in the button
          if (today == newDate) then
            button.setStyle(
              "-fx-background-color: #fff; " +
                "-fx-border-radius: 24px; " +
                "-fx-border-style: none; " +
                "-fx-text-fill: #3c4043; " +
                "-fx-font-family: 'Google Sans', Roboto, Arial, sans-serif; " +
                "-fx-font-size: 16px; " + // Larger Font
                "-fx-font-weight: bold; " +
                "-fx-pref-height: 48px; " +
                "-fx-padding: 2px 24px; " +
                "-fx-alignment: center; " +
                "-fx-transition: box-shadow 280ms cubic-bezier(.4, 0, .2, 1), " +
                "opacity 15ms linear 30ms, " +
                "transform 270ms cubic-bezier(0, 0, .2, 1) 0ms; " +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, .4), 5, 0, 0, 5);"
            )
          else
            button.setStyle(
              "-fx-background-color: #fff; " +
                "-fx-border-radius: 24px; " +
                "-fx-border-style: none; " +
                "-fx-text-fill: #3c4043; " +
                "-fx-font-family: 'Google Sans', Roboto, Arial, sans-serif; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: normal; " +
                "-fx-pref-height: 48px; " +
                "-fx-padding: 2px 24px; " +
                "-fx-alignment: center; " +
                "-fx-transition: box-shadow 280ms cubic-bezier(.4, 0, .2, 1), " +
                "opacity 15ms linear 30ms, " +
                "transform 270ms cubic-bezier(0, 0, .2, 1) 0ms; " +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, .2), 3, 0, 0, 3);"
            ) // Different shadow
          button.setOnAction(_ =>
            onDaySelected(newDate)
          ) // Update the onDaySelected
        )
      dayLabels
        .get(day)
        .foreach(label =>
          if (today == newDate) then
            label.setFont(
              Font.font(
                "Montserrat",
                FontWeight.Bold,
                FontPosture.Regular,
                fontSize
              )
            )
          else
            label.setFont(
              Font.font(
                "Montserrat",
                FontWeight.Light,
                FontPosture.Regular,
                fontSize
              )
            )
            label.setTextOverrun(Clip)
        )

    }

  // Creates a week
  weekDays.zipWithIndex.foreach { case (day, columnIndex) =>
    // VBOX is a dayColumn
    val dayColumn = new VBox {
      spacing = 10
      alignment = TopCenter
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

    // Store to dayLabels map
    dayLabels(day) = dayLabel

    // Label to date of the day
    val dateButton = new Button(startOfWeek.plusDays(columnIndex).toString):
      // Adds a button to buttons map
      dateButtons(day) = this
      // Bold for current date
      if (today == date) then
        this.setStyle(
          "-fx-background-color: #fff; " +
            "-fx-border-radius: 24px; " +
            "-fx-border-style: none; " +
            "-fx-text-fill: #3c4043; " +
            "-fx-font-family: 'Google Sans', Roboto, Arial, sans-serif; " +
            "-fx-font-size: 16px; " + // Larger Font
            "-fx-font-weight: bold; " +
            "-fx-pref-height: 48px; " +
            "-fx-padding: 2px 24px; " +
            "-fx-alignment: center; " +
            "-fx-transition: box-shadow 280ms cubic-bezier(.4, 0, .2, 1), " +
            "opacity 15ms linear 30ms, " +
            "transform 270ms cubic-bezier(0, 0, .2, 1) 0ms; " +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, .4), 5, 0, 0, 5);"
        ) // Different shadow
      else
        this.setStyle(
          "-fx-background-color: #fff; " +
            "-fx-border-radius: 24px; " +
            "-fx-border-style: none; " +
            "-fx-text-fill: #3c4043; " +
            "-fx-font-family: 'Google Sans', Roboto, Arial, sans-serif; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: normal; " +
            "-fx-pref-height: 48px; " +
            "-fx-padding: 2px 24px; " +
            "-fx-alignment: center; " +
            "-fx-transition: box-shadow 280ms cubic-bezier(.4, 0, .2, 1), " +
            "opacity 15ms linear 30ms, " +
            "transform 270ms cubic-bezier(0, 0, .2, 1) 0ms; " +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, .2), 3, 0, 0, 3);"
        )

      // Handle click event
      onAction = _ => onDaySelected(date)

    //  Container for day and date
    val headerBox = new VBox:
      alignment = TopCenter
      children = Seq(dayLabel, dateButton)

    // Container with a scrollPane
    val eventsContainer = new VBox:
      spacing = constants.windowWidth * 0.005
      alignment = TopCenter
      prefHeight = constants.windowHeight * 0.78
      prefWidth = constants.windowWidth * 0.2

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

  // Adds a single event
  def addEvent(event: Event) =
    // Get event dates
    val eventStartDate = event.startingTime.toLocalDate
    val eventEndDate = event.endingTime.toLocalDate
    val endOfWeek = startOfWeek.plusDays(6)

    // Compare: Compares starDates and endDates
    if (
      (eventStartDate.compareTo(endOfWeek) <= 0) &&
      (eventEndDate.compareTo(startOfWeek) >= 0)
    ) then
      // Calculate which days of the week event should show
      for dayRange <- 0 to 6 do
        val currentDate = startOfWeek.plusDays(dayRange)

        // If current date is in event range then it is added
        if (
          (currentDate
            .isEqual(eventStartDate) || currentDate.isAfter(eventStartDate)) &&
          (currentDate
            .isEqual(eventEndDate) || currentDate.isBefore(eventEndDate))
        ) then
          val dayName = weekDays(dayRange)

          // Add event to the column
          dayColumns
            .get(dayName)
            .foreach(column =>
              column.children.add(eventView.createEventDisplay(event))
            )

  // Add all  the events
  def addEvents(events: Seq[Event]) =
    events.foreach(addEvent)

  // Clears the events
  def clearEvents() =
    dayColumns.values.foreach(column => column.children.clear())

}
