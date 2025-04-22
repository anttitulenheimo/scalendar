package com.calendar.services

import com.calendar.models.Event

import java.time.LocalDateTime

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

}
