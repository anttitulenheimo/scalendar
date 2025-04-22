package com.calendar.models

import java.time.{ LocalDate, LocalDateTime }

class Event(
  val name: String,
  val date: LocalDate,
  val startingTime: LocalDateTime,
  val endingTime: LocalDateTime,
  val category: Category,
  val reminder: Option[Reminder],
  val additionalInfo: Option[String],
  var colorCode: String = "#808080" // a default color
) {}
