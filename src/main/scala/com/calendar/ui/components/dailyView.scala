package com.calendar.ui.components

import com.calendar.models.Event
import com.calendar.ui.constants
import scalafx.geometry.Pos.Center
import scalafx.scene.control.{ Label, ScrollPane }
import scalafx.scene.layout.{ ColumnConstraints, GridPane, RowConstraints }
import scalafx.scene.paint.Color
import scalafx.scene.shape.Line
import scalafx.scene.text.{ Font, FontPosture, FontWeight }

import java.time.{ LocalDate, LocalDateTime, LocalTime }

//Scrollpane to allow user to scroll the 24 hours view

object dailyView extends ScrollPane {

  // The grid is divided into minutes and the full hours are shown
  val dayGrid = new GridPane {
    gridLinesVisible = false
    alignment = Center

    val totalMinutes = 24 * 60 // 24 h * 60 min
    val rowHeight =
      constants.windowHeight / totalMinutes // a minute's height scaled

    // Returns the current hour and minute
    val currentHour = java.time.LocalTime.now().getHour
    val currentMinute = java.time.LocalTime.now().getMinute

    val fontSize = constants.windowWidth * 0.007

    // minute grid
    for minute <- 0 until totalMinutes do
      val row = new RowConstraints()
      row.prefHeight = rowHeight
      this.getRowConstraints.add(row)

      // add lines for hours
    for (hour <- 0 until 24) {
      val hourLine = new Line {
        startX = 0
        endX = constants.windowWidth
        stroke = Color.LightGray
        strokeWidth = 0.5
        this.setStyle("-fx-opacity: 0.7;")
      }

      // current hour line
      if (hour == currentHour) {
        hourLine.stroke = Color.web("#2980b9")
        hourLine.strokeWidth = 2.0
        hourLine.setStyle("-fx-opacity: 1.0;")
      }

      this.add(hourLine, 0, hour * 60, 2, 1)
    }

    // hourGrids: labels for full hours
    for hour <- 0 until 24 do
      val hourLabel = new Label(f"$hour%02d:00") {
        font = Font.font(
          "Montserrat",
          FontWeight.Light,
          FontPosture.Regular,
          fontSize
        )
      }

      this.add(hourLabel, 0, hour * 60)

    // hourColumn
    val hourColumn = new ColumnConstraints()
    // eventColumn
    val eventColumn = new ColumnConstraints()

    // Adds all the columns
    this.getColumnConstraints.addAll(hourColumn, eventColumn)
  }

  // Adds all events to the calendar
  def addEvents(events: Seq[Event], currentDate: LocalDate) =
    events.foreach { event =>
      val eventBox = eventView.createEventDisplay(event)

      // Get event dates
      val eventStartDate = event.startingTime.toLocalDate
      val eventEndDate = event.endingTime.toLocalDate
  
      // Multiple day events handling
  
      // Adjusts start time if event begins before current day
      val adjustedStartTime =
        if (eventStartDate.isBefore(currentDate)) then
          LocalDateTime.of(
            currentDate,
            LocalTime.MIN
          ) // LocalTime.Min =  00:00 of current day
        else event.startingTime
  
      // Adjusts end time if event ends after current day
      val adjustedEndTime =
        if (eventEndDate.isAfter(currentDate)) then
          LocalDateTime.of(
            currentDate,
            LocalTime.MAX
          ) // LocalTime.MAX = 23:59 of current day
        else event.endingTime
  
      val startHour = adjustedStartTime.getHour
      val startMinute = adjustedStartTime.getMinute
      val endHour = adjustedEndTime.getHour
      val endMinute = adjustedEndTime.getMinute
  
      val startRow = startHour * 60 + startMinute // a minute index
      val endRow = endHour * 60 + endMinute
      val duration = endRow - startRow
  
      // Validator for the rowSpan
      val rowSpan = Math.max(1, duration)
  
      dayGrid.add(eventBox, 1, startRow, 1, rowSpan)
    }

  // Method  for the setOnDaySelected:
  // Removes all event displays from the grid and keep only hour labels and lines
  def clearEvents() =

    val children = dayGrid.children
    val toKeep =
      children.filter(node => // The nodes should be either labels or lines
        node.isInstanceOf[javafx.scene.control.Label] ||
          node.isInstanceOf[javafx.scene.shape.Line]
      )
    dayGrid.children.clear()
    dayGrid.children.addAll(toKeep)

  content = dayGrid
  fitToWidth = true
  fitToHeight = false
  this.setStyle(
    "-fx-background-color: transparent; " +
      " -fx-padding: 10px; " +
      " -fx-border-radius: 10px; " +
      " -fx-border-color: #ccc; " +
      " -fx-hbar-policy: never; " +
      " -fx-vbar-policy: always;"
  )
}
