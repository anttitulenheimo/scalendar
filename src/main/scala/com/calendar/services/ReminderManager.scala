package com.calendar.services

import com.calendar.models.Reminder
import com.calendar.models.Event

class ReminderManager(var reminders: Seq[Reminder]) {

  // Set a reminder for the event
  def setReminder(event: Event): Unit =
    ???

  // Check for the upcoming events
  def checkUpcomingEvent(): List[Event] = ???

}
