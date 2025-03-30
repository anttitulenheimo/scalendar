package com.calendar.ui.components

import com.calendar.ui.components.hourView.{ fontSize, style }
import com.calendar.models.Event
import com.calendar.ui.constants
import scalafx.geometry.Pos.Center
import scalafx.scene.control.Label
import scalafx.scene.layout.{ ColumnConstraints, GridPane }
import scalafx.scene.text.{ Font, FontPosture, FontWeight }

// TODO: Implement dailyview which uses the hourView

object dailyView extends GridPane {
  vgap = constants.windowWidth * 0.01
  gridLinesVisible = true
  alignment = Center

  // Returns the current hour
  val currentHour = java.time.LocalTime.now().getHour

  val fontSize = constants.windowWidth * 0.007

  // hourGrids
  for hour <- 0 until 24 do
    val hourLabel = new Label(f"$hour%02d:00") {
      font =
        Font.font("Montserrat", FontWeight.Light, FontPosture.Regular, fontSize)
    }
    // TODO: Find out why this doesn't work
    if (hour == currentHour) then
      style = "-fx-font-weight: bold; -fx-text-fill: #2980b9;"

    // def add(child: Node, columnIndex: Int, rowIndex: Int): Unit
    this.add(hourLabel, 0, hour)

  // hourColumn to change the prefWidth
  val hourColumn = new ColumnConstraints() {
    // prefWidth = constants.windowWidth
  }
  // eventColumn
  val eventColumn = new ColumnConstraints() {
    // prefWidth = 40
  }
  // Adds all the columns
  this.getColumnConstraints.addAll(hourColumn, eventColumn)

  // Adds all the events
  def addEvents(events: Seq[Event]): Unit =
    events.foreach { event =>
      val eventBox = eventView.createEventDisplay(event)

      val startHour = event.startingTime.getHour
      val endHour = event.endingTime.getHour
      val duration =
        Math.max(1, endHour - startHour) // Duration in hours

      this.add(
        eventBox,
        1,
        startHour,
        1,
        duration
      ) //def add(child: Node, columnIndex: Int, rowIndex: Int, colspan: Int, rowspan: Int): Unit
    }
}
