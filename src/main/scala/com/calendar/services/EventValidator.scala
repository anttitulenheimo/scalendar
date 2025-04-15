package com.calendar.services

import com.calendar.models.{ Event, Reminder }

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import scala.util.{ Failure, Success, Try }

object EventValidator {

  // Check if the events overlap
  def checkOverlap(firstEvent: Event, secondEvent: Event): Boolean =
    (secondEvent.startingTime.isBefore(
      firstEvent.endingTime
    )) && (secondEvent.endingTime.isAfter(firstEvent.startingTime))

  // Validate that the end time is after start time and end date is after start date
  def validateTime(startTime: LocalDateTime, endTime: LocalDateTime): Boolean =
    // Multi day events
    if (startTime.toLocalDate() != endTime.toLocalDate()) then
      !endTime.toLocalDate().isBefore(startTime.toLocalDate())
    else
      // Single day events
      endTime.isAfter(startTime)

  // Validate that the input is correct (=The input is non-empty) and parsing works
  def validateInput(input: String, dateAndTime: String): Boolean =
    input.nonEmpty && (
      Try(
        LocalDateTime
          .parse(dateAndTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
      ) match {
        case Success(dateAndTime) =>
          true
        case Failure(exception) =>
          false
      }
    )

}
