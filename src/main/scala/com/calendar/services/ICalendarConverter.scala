package com.calendar.services

import com.calendar.models.Event
import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.model.property._
import net.fortuna.ical4j.model.property.XProperty
import java.io.FileWriter
import java.time.{ ZoneId, Duration }

class ICalendarConverter(events: List[Event], filename: String) {

  // Creates a new ical4j calendar object
  val calendar = new Calendar()
  calendar.add(new ProdId("-//My Calendar//iCal4j 1.0//EN"))

  // Sets the version to 2.0
  val version = new Version()
  version.setValue("2.0")
  calendar.add(version)

  // Sets the calendar scale to GREGORIAN
  val calScale = new CalScale()
  calScale.setValue("GREGORIAN")
  calendar.add(calScale)

  // Loops through events
  events.foreach(event =>

    // Convert to ZonedDateTime to make sure that the timezone is correct by using the system
    val startZoned = event.startingTime.atZone(ZoneId.systemDefault())
    val endZoned = event.endingTime.atZone(ZoneId.systemDefault())

    // Create DtStart and DtEnd using ZonedDateTime
    val dtStart = new DtStart(startZoned)
    val dtEnd = new DtEnd(endZoned)

    // Create a new VEvent
    val vEvent = new VEvent()
    vEvent.add(dtStart)
    vEvent.add(dtEnd)
    vEvent.add(new Summary(event.name))
    vEvent.add(new Description(event.additionalInfo.getOrElse("")))

    val colorProp =
      new XProperty(
        "X-COLOR",
        event.colorCode
      ) // Extra properties are tagged with the "X"
    vEvent.add(colorProp)

    // Categories are also extra properties
    val categoryProp = new XProperty("X-CATEGORY", event.category.name)
    vEvent.add(categoryProp)

    // If there is a reminder
    event.reminder match {
      case Some(realReminder) => {
        val minutesBefore = Duration
          .between(realReminder.remindAt, event.startingTime)
          .toMinutes

        // Extra properties
        val reminderNameProp =
          new XProperty("X-REMINDERNAME", realReminder.eventId)
        val reminderTimeProp =
          new XProperty("X-REMINDERTIME", minutesBefore.toString)

        vEvent.add(reminderNameProp)
        vEvent.add(reminderTimeProp)
      }
      case None =>
    }

    // Add the created event to the calendar
    calendar.add(vEvent)
  )

  // Write the calendar to a file in .ics
  def writeToFile(): Unit =
    val writer = new FileWriter(filename)
    try
      writer.write(calendar.toString)
    finally
      writer.close()
}
