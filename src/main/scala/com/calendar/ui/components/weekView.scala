package com.calendar.ui.components

import scalafx.geometry.{ Insets, Pos }
import scalafx.geometry.Pos.TopCenter
import scalafx.scene.control.Label
import scalafx.scene.layout.{ GridPane, VBox }
import scalafx.scene.text.{ Font, FontPosture, FontWeight }
import scalafx.scene.layout.ColumnConstraints

import java.time.*

object weekView extends GridPane:
  alignment = TopCenter
  hgap = 150
  vgap = 20
  padding = Insets(10)

  style =
    "-fx-border-color: black; -fx-border-width: 2px; -fx-border-radius: 5px;" // Style to see the borders

  // Creates a new gridBox for weekdays
  val weekDaysGridBox = new GridPane:
    alignment = Pos.Center
    hgap = 10
    vgap = 10

    padding = Insets(10)

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

  weekDays.zipWithIndex.foreach { case (day, columnIndex) =>
    // VBOX is a dayColumn
    val dayColumn = new VBox {
      spacing = 10
      alignment = TopCenter
    }

    // Label to name the day with a Montserrat font
    val dayLabel = new Label(day):
      font = Font.font("Montserrat", FontWeight.Light, FontPosture.Regular, 20)

    // ColumnConstraits helps to distribute space equally
    val columnConstraints = new ColumnConstraints:
      percentWidth = 100.0 / weekDays.size

    this.columnConstraints.add(columnConstraints)

    dayColumn.children.add(dayLabel)

    this.add(dayColumn, columnIndex, 0)
  }
