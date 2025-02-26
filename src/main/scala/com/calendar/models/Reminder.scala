package com.calendar.models
import java.time.LocalDateTime

class Reminder(val eventId: String, val remindAt: LocalDateTime) {

  def getReminderTime(): String = ???
  
}
