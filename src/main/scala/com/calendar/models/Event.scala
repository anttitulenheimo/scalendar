package com.calendar.models

import java.time.{ LocalDateTime, LocalDate }
import scala.io.StdIn
import scala.util.{ Try, Success, Failure }
import java.time.format.DateTimeFormatter
import com.calendar.services.EventValidator

//ToDO implement a better addReminder method

class Event(
  val name: String,
  val date: LocalDate,
  val startingTime: LocalDateTime,
  val endingTime: LocalDateTime,
  val category: Category,
  val reminder: Option[Reminder],
  val additionalInfo: Option[String],
  var colorCode: String = "#808080" // a default color
) {

  // Add a reminder for the event

}
