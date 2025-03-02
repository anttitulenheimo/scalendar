package com.calendar.services

import com.calendar.models.{ Category, Event }

class Calendar(
  // A map of specific calendar events where the String is the calendars name
  var events: Map[String, List[Event]],

  // Map of events
  var eventMap: Map[String, Event],

  // Categories which a specific calendar has
  var categories: Map[String, Category]
) {

  // Add an event to the calendar
  def addEvent(newEvent: Event): Boolean =
    val eventName = newEvent.name
    if (!eventMap.contains(eventName)) then
      eventMap += eventName -> newEvent
      true
    else false

  // Delete an event from the calendar
  def deleteEvent(eventName: String): Boolean =
    val possibleEvent = eventMap.get(eventName)
    possibleEvent match
      case Some(existingEvent) =>
        eventMap -= eventName
        true
      case None =>
        false

  // Filter events by category
  def filterEventsByCategory(category: Category): List[Event] =
    eventMap
      .filter((EventName, Event) => Event.category.name == category.name)
      .map((EventName, Event) => Event)
      .toList

  // Load events from a file
  def loadFromFile(filename: String): Unit = ???

  // Save events to a file
  def saveToFile(filename: String): Unit = ???

}
