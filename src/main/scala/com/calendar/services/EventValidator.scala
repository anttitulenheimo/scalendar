package com.calendar.services

import com.calendar.models.Event

import java.time.LocalDateTime

object EventValidator {

  // Check if the events overlap
  def checkOverlap(firstEvent: Event, secondEvent: Event): Boolean =
    (secondEvent.startingTime.isBefore(
      firstEvent.endingTime
    )) && (secondEvent.endingTime.isAfter(firstEvent.startingTime))

  // Validate that the end time is after start time
  def validateTime(startTime: LocalDateTime, endTime: LocalDateTime): Boolean =
    startTime.isAfter(endTime)

  // Validate that the input is correct
  def validateInput(input: String, dateAndTime: LocalDateTime): Boolean = ???

}
