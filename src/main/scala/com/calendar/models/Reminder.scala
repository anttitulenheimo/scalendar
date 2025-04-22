package com.calendar.models
import java.time.LocalDateTime

class Reminder(val eventId: String, val remindAt: LocalDateTime) {

  // Returns the time of the reminder
  def getReminderTime: LocalDateTime = remindAt

}
