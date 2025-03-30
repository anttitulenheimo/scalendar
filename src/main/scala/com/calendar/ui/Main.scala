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

      //Adds events to dailyView
      dailyView.addEvents(eventSeq)

      //  The weekview and welcomeLabel is for now commented to test dailyView
      val borderPane = new BorderPane {
        padding = Insets(constants.windowWidth * 0.01)
        // top = welcomeLabel
        // center = weekView
        center = dailyView
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
val eventSeq = Seq(event1, event2)
