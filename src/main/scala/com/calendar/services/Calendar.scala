package com.calendar.services

import com.calendar.models.{ Category, Event }

class Calendar(
  var events: Map[String, List[Event]],
  var categories: Map[String, Category]
) {

  def addEvent(newEvent: Event): Boolean = ???

  def deleteEvent(eventName: String): Boolean = ???

  def filterEventsByCategory(category: Category): List[Event] = ???

  def loadFromFile(filename: String): Unit = ???

  def saveToFile(filename: String): Unit = ???

}
