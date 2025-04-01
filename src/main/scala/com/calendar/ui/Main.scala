package com.calendar.ui

import com.calendar.models.{ Category, Event, Reminder }
import com.calendar.ui.components.*
import scalafx.application.JFXApp3
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.Label
import scalafx.scene.layout.BorderPane
import scalafx.scene.paint.Color.*
import scalafx.scene.text.{ Font, FontPosture, FontWeight }

import java.time.{ LocalDate, LocalDateTime }

object Main extends JFXApp3:

  def start() =

    val primaryStage = new JFXApp3.PrimaryStage():
      title = "SCALENDAR - CALENDAR"

      width = constants.windowWidth
      height = constants.windowHeight

      val fontSize = constants.windowWidth * 0.015

      // TODO: Make the welcome perhaps animated
      val welcomeLabel =
        new Label("Welcome to your calendar") { // ToDO Impelement a better name
          textFill = Black
          font = Font.font(
            "Montserrat",
            FontWeight.Light,
            FontPosture.Regular,
            fontSize
          )
        }

      // Adds events to dailyView
      dailyView.addEvents(eventSeq)
      weekView.addEvents(eventSeq)

      //  Comments to test weekView or dailyVierw
      val borderPane = new BorderPane {
        padding = Insets(constants.windowWidth * 0.01)
        // top = welcomeLabel
        center = weekView
        // center = dailyView
      }

      scene = new Scene(borderPane)

  end start

end Main

// Some already converted test events
// .
// .
val event1 = new Event(
  name = "Time to study",
  date = LocalDate.now(),
  startingTime =
    LocalDateTime.of(2024, 3, 30, 12, 0), // 30. maaliskuuta 2024, klo 12:00
  endingTime =
    LocalDateTime.of(2024, 3, 30, 13, 30), // 30. maaliskuuta 2024, klo 13:30
  category = new Category("Study", "#FF0000"),
  reminder = new Reminder("Lunch", LocalDateTime.now().plusMinutes(30)),
  additionalInfo = Some("Eat at A-bloc"),
  colorCode = "#FF0000"
)

val event2 = new Event(
  name = "Dancing with Team",
  date = LocalDate.now(),
  startingTime =
    LocalDateTime.of(2024, 3, 30, 14, 0), // 30. maaliskuuta 2024, klo 14:00
  endingTime =
    LocalDateTime.of(2024, 3, 30, 16, 0), // 30. maaliskuuta 2024, klo 16:00
  category = new Category("Work", "#FF1111"),
  reminder = new Reminder("Meeting", LocalDateTime.now().plusMinutes(10)),
  additionalInfo = Some("Dance battle"),
  colorCode = "" // No colorcode given so the default code should be displayed
)

val event3 = new Event(
  name = "Project Presentation",
  date = LocalDate.of(2024, 3, 31), // 31. maaliskuuta 2024
  startingTime =
    LocalDateTime.of(2024, 3, 31, 10, 0), // 31. maaliskuuta 2024, klo 10:00
  endingTime =
    LocalDateTime.of(2024, 3, 31, 11, 30), // 31. maaliskuuta 2024, klo 11:30
  category = new Category("Work", "#00FF00"),
  reminder =
    new Reminder("Prepare materials", LocalDateTime.now().plusMinutes(45)),
  additionalInfo = Some("Prepare slides and presentation materials"),
  colorCode = "#00FF00"
)

val event4 = new Event(
  name = "Yoga Class",
  date = LocalDate.of(2024, 4, 1), // 1. huhtikuuta 2024
  startingTime =
    LocalDateTime.of(2024, 4, 1, 7, 30), // 1. huhtikuuta 2024, klo 7:30
  endingTime =
    LocalDateTime.of(2024, 4, 1, 8, 30), // 1. huhtikuuta 2024, klo 8:30
  category = new Category("Wellness", "#0000FF"),
  reminder = new Reminder("Get ready", LocalDateTime.now().plusHours(1)),
  additionalInfo = Some("Wear comfortable clothes"),
  colorCode = "#0000FF"
)

val event5 = new Event(
  name = "Lunch with Friends",
  date = LocalDate.of(2024, 4, 2), // 2. huhtikuuta 2024
  startingTime =
    LocalDateTime.of(2024, 4, 2, 12, 0), // 2. huhtikuuta 2024, klo 12:00
  endingTime =
    LocalDateTime.of(2024, 4, 2, 13, 0), // 2. huhtikuuta 2024, klo 13:00
  category = new Category("Social", "#FF8800"),
  reminder = new Reminder("Time to leave", LocalDateTime.now().plusMinutes(20)),
  additionalInfo = Some("Meet at the park for lunch"),
  colorCode = "#FF8800"
)

val event6 = new Event(
  name = "Evening Run",
  date = LocalDate.of(2024, 4, 3), // 3. huhtikuuta 2024
  startingTime =
    LocalDateTime.of(2024, 4, 3, 18, 0), // 3. huhtikuuta 2024, klo 18:00
  endingTime =
    LocalDateTime.of(2024, 4, 3, 19, 0), // 3. huhtikuuta 2024, klo 19:00
  category = new Category("Fitness", "#00FFFF"),
  reminder = new Reminder("Get your gear", LocalDateTime.now().plusMinutes(10)),
  additionalInfo = Some("Meet at the main entrance"),
  colorCode = "#00FFFF"
)

val eventSeq = Seq(event1, event2, event3, event4, event5, event6)

