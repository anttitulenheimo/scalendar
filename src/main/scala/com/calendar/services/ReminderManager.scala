package com.calendar.services

import com.calendar.models.Reminder
import com.calendar.models.Event

class ReminderManager(var reminders: Map[String, List[Reminder]]) {

  def setReminder(event: Event): Unit = ???

  def checkUpcomingEvent(): List[Event] = ???

}
