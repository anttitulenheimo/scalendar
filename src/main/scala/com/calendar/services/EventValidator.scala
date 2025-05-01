package com.calendar.services

import java.time.LocalDateTime

object EventValidator {

  // Validate that the end time is after start time and end date is after start date
  def validateTime(startTime: LocalDateTime, endTime: LocalDateTime): Boolean =
    // Multi day events
    if (startTime.toLocalDate() != endTime.toLocalDate()) then
      !endTime.toLocalDate().isBefore(startTime.toLocalDate())
    else
      // Single day events
      endTime.isAfter(startTime)

}
