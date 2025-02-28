package com.calendar.services

import com.calendar.models.{ Category, Event }

class Calendar(
  var events: Map[String, List[Event]],
  var categories: Map[String, Category]
) {


  // Add an event to the calendar
  def addEvent(newEvent: Event): Boolean = ???

  // Delete an event from the calendar
  def deleteEvent(eventName: String): Boolean = ???

  // Filter events by category
  def filterEventsByCategory(category: Category): List[Event] = ???

  // Load events from a file
  def loadFromFile(filename: String): Unit = ???

  // Save events to a file 
  def saveToFile(filename: String): Unit = ???

}
