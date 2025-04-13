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

  def addReminder(): Option[Reminder] =
    val remindTimeString: String = StdIn.readLine(
      "Enter reminder time (yyyy-MM-dd HH:mm): "
    ) // Time to remind at
    Try(
      LocalDateTime.parse(
        remindTimeString,
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
      )
    ) match {
      case Success(remindTime) =>
        println(s"Reminder set at ${remindTime}")
        Some(new Reminder(name, remindTime)) // Creates a new reminder object
      case Failure(exception) =>
        println(
          s"Invalid date format. Please use 'yyyy-MM-dd HH:mm'. Error: ${exception.getMessage}"
        )
        None
    }

  // Validate that the event start and end times are correct
  // Might be useless because the class EventValidator already has methods to validate an event
  def validateTimes(): Boolean =
    EventValidator.validateTime(startingTime, endingTime)

}
