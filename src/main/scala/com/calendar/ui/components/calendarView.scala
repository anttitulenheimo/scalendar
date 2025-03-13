package com.calendar.ui.components

import scalafx.geometry.Insets
import scalafx.geometry.Pos.Center
import scalafx.scene.layout.GridPane

import java.time.*
import scalafx.scene.control.Label

object calendarView extends GridPane:
  alignment = Center
  padding = Insets(100)

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
  weekDays.zipWithIndex.foreach { case (day, index) =>
    this.add(new Label(" " + day + " "), index, 0)
  }
