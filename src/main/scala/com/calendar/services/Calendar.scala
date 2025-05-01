package com.calendar.services

import com.calendar.models.{ Category, Event }

class Calendar(

  // Map of events
  var eventMap: Map[String, Event],

  // Categories which the calendar has
  var categories: Seq[Category]
) {

  // Add an event to the calendar
  def addEvent(newEvent: Event): Unit =
    val eventName = newEvent.name
    if (!eventMap.contains(eventName)) then
      eventMap += eventName -> newEvent
    

  // Delete an event from the calendar
  def deleteEvent(eventName: String): Boolean =
    val possibleEvent = eventMap.get(eventName)
    possibleEvent match
      case Some(_) =>
        eventMap -= eventName
        true
      case None =>
        false

  // Filter events by categories
  def filterEventsByCategory(categorySeq: Seq[Category]): Seq[Event] = {
    val categoryNames = categorySeq.map(_.name).toSet // Set removes duplicates
    eventMap
      .filter((_, event) => categoryNames.contains(event.category.name))
      .map((_, event) => event)
      .toSeq
  }

  // Load events from a file
  // Returns the Seq for now
  def loadFromFile(
    filename: String,
    fileReminderManager: Option[ReminderManager] = None
  ): Seq[Event] =
    val icsReader = new ICalendarReader(filename)
    val loadEvents = icsReader.readEvents()
    loadEvents.foreach(event =>
      addEvent(event)
      if event.reminder.isDefined then // Requires to be wrapped
        fileReminderManager.foreach(_.setReminder(event))
    )
    loadEvents

  // Save events to a file
  def saveToFile(filename: String): Unit =
    val iConverter = new ICalendarConverter(getAllEvents, filename)
    iConverter.writeToFile()

  // Returns all events
  def getAllEvents: List[Event] = eventMap.values.toList

}
