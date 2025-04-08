package com.calendar.services

import com.calendar.models.{ Event, Category, Reminder }
import java.time.{ LocalDate, LocalDateTime }

object TESTICalendarConverter {

  @main def testProgram(): Unit =

    /*    // Some test events
    // .
    // .
    val event1 = new Event(
      name = "Time to study",
      date = LocalDate.now(),
      startingTime = LocalDateTime.now().plusHours(1),
      endingTime = LocalDateTime.now().plusHours(2),
      category = new Category("Study", "#FF0000"),
      reminder = new Reminder("Lunch", LocalDateTime.now().plusMinutes(30)),
      additionalInfo = Some("Eat at A-bloc"),
      colorCode = "#FF0000"
    )

    val event2 = new Event(
      name = "Dancing with Team",
      date = LocalDate.now(),
      startingTime = LocalDateTime.now().plusHours(4),
      endingTime = LocalDateTime.now().plusHours(8),
      category = new Category("Work", "#FF1111"),
      reminder = new Reminder("Meeting", LocalDateTime.now().plusMinutes(10)),
      additionalInfo = Some("Dance battle"),
      colorCode = "#FF1111"
    )

    // Write
    val converter =
      new ICalendarConverter(
        List(event1, event2),
        "src/main/resources/myCalendar.ics"
      )
    converter.writeToFile()*/

    val testCalendar = new Calendar(Map(), Map(), Map())
    val eventSeq =
      testCalendar.loadFromFile("src/main/resources/myCalendar.ics")
    eventSeq.foreach(event =>
      println(event.name)
      println(event.date)
      println(event.startingTime)
      println(event.endingTime)
      println(event.category.name)
      println(event.category.colorCode)
      println(event.reminder.eventId)
      println(event.reminder.remindAt)
      println(event.additionalInfo)
      println(event.colorCode)
    )
}
