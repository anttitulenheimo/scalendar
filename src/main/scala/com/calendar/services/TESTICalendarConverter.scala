package com.calendar.services

import com.calendar.models.{ Event, Category, Reminder }
import java.time.{ LocalDate, LocalDateTime }

object TESTICalendarConverter {

  @main def testProgram(): Unit = {

    // Some test events
    // .
    // .
  val event1 = new Event(
    name = "Time to study",
    date = LocalDate.of(2025, 3, 30),
    startingTime = LocalDateTime.of(2025, 4, 7, 12, 0),
    endingTime = LocalDateTime.of(2025, 4, 7, 13, 30),
    category = new Category("Study", "#FF0000"),
    reminder = new Reminder("Lunch", LocalDateTime.now().plusMinutes(30)),
    additionalInfo = Some("Eat at A-bloc"),
    colorCode = "#FF0000"
  )

  val event2 = new Event(
    name = "Dancing with Team",
    date = LocalDate.of(2025, 3, 30),
    startingTime = LocalDateTime.of(2025, 4, 7, 14, 0),
    endingTime = LocalDateTime.of(2025, 4, 7, 16, 0),
    category = new Category("Work", "#FF1111"),
    reminder = new Reminder("Meeting", LocalDateTime.now().plusMinutes(10)),
    additionalInfo = Some("Dance battle"),
    colorCode = "" // No colorcode given so the default code should be displayed
  )

  val event3 = new Event(
    name = "Project Presentation",
    date = LocalDate.of(2025, 3, 31),
    startingTime = LocalDateTime.of(2025, 4, 8, 10, 0),
    endingTime = LocalDateTime.of(2025, 4, 8, 11, 30),
    category = new Category("Work", "#00FF00"),
    reminder =
      new Reminder("Prepare materials", LocalDateTime.now().plusMinutes(45)),
    additionalInfo = Some("Prepare slides and presentation materials"),
    colorCode = "#00FF00"
  )

  val event4 = new Event(
    name = "Yoga Class",
    date = LocalDate.of(2025, 4, 1),
    startingTime = LocalDateTime.of(2025, 4, 9, 7, 30),
    endingTime = LocalDateTime.of(2025, 4, 9, 8, 30),
    category = new Category("Wellness", "#0000FF"),
    reminder = new Reminder("Get ready", LocalDateTime.now().plusHours(1)),
    additionalInfo = Some("Wear comfortable clothes"),
    colorCode = "#0000FF"
  )

  val event5 = new Event(
    name = "Lunch with Friends",
    date = LocalDate.of(2025, 4, 2),
    startingTime = LocalDateTime.of(2025, 4, 10, 12, 0),
    endingTime = LocalDateTime.of(2025, 4, 10, 13, 0),
    category = new Category("Social", "#FF8800"),
    reminder = new Reminder("Time to leave", LocalDateTime.now().plusMinutes(20)),
    additionalInfo = Some("Meet at the park for lunch"),
    colorCode = "#FF8800"
  )

  val event6 = new Event(
    name = "Evening Run",
    date = LocalDate.of(2025, 4, 3),
    startingTime = LocalDateTime.of(2025, 4, 10, 18, 0),
    endingTime = LocalDateTime.of(2025, 4, 10, 19, 0),
    category = new Category("Fitness", "#00FFFF"),
    reminder = new Reminder("Get your gear", LocalDateTime.now().plusMinutes(10)),
    additionalInfo = Some("Meet at the main entrance"),
    colorCode = "#00FFFF"
  )

  // Write
  val converter =
    new ICalendarConverter(
      List(event1, event2, event3, event4, event5, event6),
      "src/main/resources/myCalendar.ics"
    )
  converter.writeToFile()

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
      println(event.additionalInfo.getOrElse("No additional info"))
      println(event.colorCode)
    )
}
}
