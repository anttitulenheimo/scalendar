package com.calendar.ui.components

import com.calendar.models.Event
import com.calendar.ui.constants
import scalafx.geometry.Pos.Center
import scalafx.scene.control.{ Label, ScrollPane }
import scalafx.scene.layout.{ ColumnConstraints, GridPane, RowConstraints }
import scalafx.scene.paint.Color
import scalafx.scene.shape.Line
import scalafx.scene.text.{ Font, FontPosture, FontWeight }

//Scrollpane to allow user to scroll the 24 hours view
// The events finally show where they should
// TODO: Implement a way to show the correct weekday on top of the screen
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
        style = "-fx-opacity: 0.7;"
      }

      // current hour line
      if (hour == currentHour) {
        hourLine.stroke = Color.web("#2980b9")
        hourLine.strokeWidth = 2.0
        hourLine.style = "-fx-opacity: 1.0;"
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

      // def add(child: Node, columnIndex: Int, rowIndex: Int): Unit
      this.add(hourLabel, 0, hour * 60)

    // hourColumn
    val hourColumn = new ColumnConstraints()
    // eventColumn
    val eventColumn = new ColumnConstraints()

    // Adds all the columns
    this.getColumnConstraints.addAll(hourColumn, eventColumn)
  }

  // Adds all events to the calendar
  def addEvents(events: Seq[Event]): Unit =
    events.foreach { event =>
      val eventBox = eventView.createEventDisplay(event)

      val startHour = event.startingTime.getHour
      val startMinute = event.startingTime.getMinute
      val endHour = event.endingTime.getHour
      val endMinute = event.endingTime.getMinute

      val startRow = startHour * 60 + startMinute // a minute index
      val endRow = endHour * 60 + endMinute
      val duration = endRow - startRow

      // Validator for the rowSpan
      val rowSpan = Math.max(1, duration)

      dayGrid.add(eventBox, 1, startRow, 1, rowSpan)
    }

  content = dayGrid
  fitToWidth = true
  fitToHeight = false
  style =
    "-fx-background-color: transparent; " +
    " -fx-padding: 10px; " +
    " -fx-border-radius: 10px; " +
    " -fx-border-color: #ccc; " +
    " -fx-hbar-policy: never; " +  
    " -fx-vbar-policy: always;"    
}
