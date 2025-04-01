package com.calendar.ui

import com.calendar.models.{ Category, Event, Reminder }
import com.calendar.ui.components.*
import scalafx.application.JFXApp3
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.{ Button, Label }
import scalafx.scene.layout.{ BorderPane, VBox }
import scalafx.scene.paint.Color.*
import scalafx.scene.text.{ Font, FontPosture, FontWeight }

import java.time.{ LocalDate, LocalDateTime }

object Main extends JFXApp3:

  // Set the dateHeader to be NULL
  private var dateHeader: Label = _

  def start() = {

    val fontSize = constants.windowWidth * 0.01

    val weekViewScene: Scene = createWeekViewScene(fontSize)
    val dailyViewScene: Scene = createDailyViewScene(fontSize)

    // Set up day selection handler in weekView
    weekView.setOnDaySelected(date => {
      // Header is now day - date
      val dateString =
        s"${date.getDayOfWeek.toString.capitalize} - ${date.toString}"
      dateHeader.text = dateString

      // Switch to dailyView
      switchScenes(dailyViewScene)
    })

    // TODO: Implement a way to add events to the correct days in dayView
    // Adds events to dailyView and to weekView
    dailyView.addEvents(eventSeq)
    weekView.addEvents(eventSeq)

    val primaryStage = new JFXApp3.PrimaryStage():
      title = "SCALENDAR - CALENDAR"
      width = constants.windowWidth
      height = constants.windowHeight
      scene = weekViewScene

    stage = primaryStage
  }

  // Switches to the given scene
  def switchScenes(scene: Scene): Unit =
    stage.setScene(scene)

  // Creates the weekViewScene
  private def createWeekViewScene(fontSize: Double): Scene = {
    val welcomeLabel = new Label("Welcome to your calendar") {
      textFill = Black
      font =
        Font.font("Montserrat", FontWeight.Light, FontPosture.Regular, fontSize)
    }

    // Container for weekView
    val weekViewborderPane = new BorderPane {
      padding = Insets(constants.windowWidth * 0.01)
      top = welcomeLabel
      center = weekView
      style =
        "-fx-background-color: linear-gradient(to bottom, #f5f7fa, #e4e8f0);"
    }
    // Returns weekViewScene
    new Scene(weekViewborderPane)
  }

  // Creates the dailyViewScene
  private def createDailyViewScene(fontSize: Double): Scene = {
    // Add date header for dailyView
    dateHeader = new Label("") {
      font = Font.font("Montserrat", FontWeight.Bold, fontSize * 1.5)
      style = "-fx-padding: 10 0 10 0;"
    }

    // Back button for navigation
    val backButton = new Button("Back to Week View") {
      onAction = _ => switchScenes(createWeekViewScene(fontSize))
      style = "-fx-background-color: #fff; " +
        "-fx-border-radius: 24px; " +
        "-fx-border-style: none; " +
        "-fx-text-fill: #3c4043; " +
        "-fx-font-family: 'Google Sans', Roboto, Arial, sans-serif; " +
        "-fx-font-size: 14px; " +
        "-fx-font-weight: 500; " +
        "-fx-pref-height: 48px; " +
        "-fx-padding: 2px 24px; " +
        "-fx-alignment: center; " +
        "-fx-transition: box-shadow 280ms cubic-bezier(.4, 0, .2, 1), " +
        "opacity 15ms linear 30ms, " +
        "transform 270ms cubic-bezier(0, 0, .2, 1) 0ms; " +
        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, .2), 3, 0, 0, 3);"

    }

    // Add header container
    val headerContainer = new VBox {
      spacing = 10
      children = Seq(backButton, dateHeader)
    }

    // Container for dailyView
    val dailyViewborderPane = new BorderPane {
      padding = Insets(constants.windowWidth * 0.01)
      top = headerContainer
      center = dailyView
      style =
        "-fx-background-color: linear-gradient(to bottom, #f5f7fa, #e4e8f0);"
    }
    // Returns dailyViewScene
    new Scene(dailyViewborderPane)
  }

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

/*val event3 = new Event(
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

val eventSeq = Seq(event1, event2, event3, event4, event5, event6)*/
val eventSeq = Seq(event1, event2)
