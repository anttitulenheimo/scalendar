package com.calendar.models

import java.time.LocalDateTime
import scala.io.StdIn
import scala.util.{ Try, Success, Failure }
import java.time.format.DateTimeFormatter

class Event(
  val name: String,
  val date: LocalDateTime,
  val category: Category,
  val reminder: Reminder,
  val additionalInfo: Option[String],
  val colorCode: String
) {
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

  def validateTimes(): Boolean = ???

}
