package com.calendar.services

import com.calendar.models.Event

import java.time.LocalDateTime

object EventValidator {

  def checkOverlap(firstEvent: Event, secondEvent: Event): Boolean = ???

  def validateTime(startTime: LocalDateTime, endTime: LocalDateTime): Boolean =
    ???

  def validateInput(input: String, dateAndTime: LocalDateTime): Boolean = ???

}
