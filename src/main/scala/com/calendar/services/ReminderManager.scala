package com.calendar.services

import com.calendar.models.{ Event, Reminder }
import javafx.util.Duration
import org.controlsfx.control.Notifications
import scalafx.application.Platform

import java.time.LocalDateTime

class ReminderManager(
  var reminders: Seq[Reminder],
  private var eventMap: Map[String, Event] = Map()
) {

  val timeInterval = 1

  // Set a reminder for the event
  def setReminder(event: Event): Unit =
    event.reminder match
      case Some(realReminder) =>
        reminders = reminders :+ realReminder
        eventMap = eventMap + (realReminder.eventId -> event)
      case None =>

  // Check for the upcoming events
  def checkUpcomingEvents(): Seq[Event] =
    val currentTime = LocalDateTime.now()
    reminders
      .filter(reminder =>
        reminder.getReminderTime
          .isAfter(currentTime) && reminder.getReminderTime
          .isBefore(currentTime.plusMinutes(timeInterval))
      )
      .flatMap(reminder => eventMap.get(reminder.eventId))

  // Removes old reminders
  def cleanReminders(): Unit =
    val currentTime = LocalDateTime.now()
    val oldReminderIds = reminders // Get the old Reminders by their ids
      .filter(_.getReminderTime.isBefore(currentTime))
      .map(_.eventId)

    reminders = reminders.filter(_.getReminderTime.isAfter(currentTime))
    eventMap = eventMap -- oldReminderIds

  // Creates the notification template
  def showNotification(event: Event) =
    Platform
      .runLater {
        Notifications
          .create()
          .title(s"Reminder ${event.name}")
          .text(
            event.additionalInfo
              .getOrElse("Upcoming event") + "\n" + event.category.name
          )
          .hideAfter(
            Duration.seconds(30)
          ) // Hides the notification after 30 seconds
          .showInformation()
      }

  // Uses the checkUpcomingEvents and showNotification to notify the user
  def notifyEvents() =
    // Get the upcoming events and create a notification for each event
    checkUpcomingEvents().foreach(showNotification)

}
