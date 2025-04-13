package com.calendar.ui

import com.calendar.models.{ Category, Event, Reminder }
import com.calendar.ui.components.*
import com.calendar.services.Calendar
import scalafx.application.JFXApp3
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.{ Button, Label }
import scalafx.scene.layout.{ BorderPane, VBox, HBox }
import scalafx.scene.paint.Color.*
import scalafx.scene.text.{ Font, FontPosture, FontWeight }

import java.time.temporal.IsoFields
import java.time.{ DayOfWeek, LocalDate, LocalDateTime }
import scalafx.scene.SceneIncludes.jfxScene2sfx
import javax.print.attribute.standard.MediaSize.ISO
import scala.compiletime.uninitialized

object Main extends JFXApp3:

  // Set the dateHeader to be uninitialized
  private var dateHeader: Label = uninitialized

  // Some default categories
  private val defaultCategories = Seq[Category](
    new Category("Work", "#1E90FF"), // Blue
    new Category("Personal", "#32CD32"), // Green
    new Category("Health", "#FF4500"), // Orange
    new Category("Hobbies", "#9370DB"), // Purple
    new Category("Urgent", "#DC143C"), // Red
    new Category("School", "#FFD700") // Yellow
  )

  // TODO: Implement save button to save the event to file

  // Calendar instance to handle events
  private val calendar = new Calendar(Map(), Map(), defaultCategories)

  // Load public holidays
  private val eventSeqPublicHolidays =
    calendar.loadFromFile("src/main/resources/finland.ics")

  // Load own events
  private val eventSeqMyCalendar =
    calendar.loadFromFile("src/main/resources/myCalendar.ics")

  var allEvents = eventSeqPublicHolidays ++ eventSeqMyCalendar

  private val today = LocalDate.now
  private var startOfWeek = today.`with`(DayOfWeek.MONDAY)
  private var weekNumber = LocalDate.now.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)

  // Handles the week navigation
  private def navigateBetweenWeeks(amount: Int): Unit =
    // Adds 1 or -1 weeks
    startOfWeek = startOfWeek.plusWeeks(amount)
    // Updates weekNumber
    weekNumber = startOfWeek.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)
    // Refresh the weekView
    refreshWeekView()
    // Creates a new weekViewScene and switches scenes
    switchScenes(createWeekViewScene(constants.windowWidth * 0.01))

  // Handles the refreshing a selected week
  private def refreshWeekView(): Unit =
    weekView.clearEvents()
    weekView.weekViewDatesRefresher(startOfWeek)
    weekView.addEvents(allEvents)

  // TODO: Create addEventButton
  private def createAddEventButton(): Button = new Button("Add Event") {
    onAction = event => {
      val result = addEventPopup.showDialog(stage, defaultCategories)

      result match
        case Some(event: Event) => // If the event is type of Event
          // Adds the event to the calendar
          calendar.addEvent(event)
          // TODO: Change the events to come str4 from the calendar not locally
          allEvents = allEvents :+ event

          // Navigate to the week containing the new created event
          startOfWeek = event.date.`with`(DayOfWeek.MONDAY)
          weekNumber = startOfWeek.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)

          refreshWeekView()

          // Adds the new event also for the dailyView
          if (stage.scene().root.value == dailyView) then
            val eventsForDay =
              allEvents.filter(_.startingTime.toLocalDate == event.date)
            dailyView.clearEvents()
            dailyView.addEvents(eventsForDay)
            // Refresh
            switchScenes(createWeekViewScene(constants.windowWidth * 0.01))

        case _ => // The add event was canceled
    }

    this.setStyle(
      "-fx-background-color: #fff; " +
        "-fx-border-radius: 24px; " +
        "-fx-border-style: none; " +
        "-fx-text-fill: #3c4043; " +
        "-fx-font-family: 'Google Sans', Roboto, Arial, sans-serif; " +
        "-fx-font-size: 14px; " +
        "-fx-font-weight: 500; " +
        "-fx-pref-height: 48px; " +
        "-fx-padding: 2px 24px; " +
        "-fx-alignment: center; " +
        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, .2), 3, 0, 0, 3);"
    )
  }

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

      // Filter based on startingTime
      val eventsForDay = allEvents.filter(event => {
        // Check if event's startTime date matches selected date
        val startTimeDate = event.startingTime.toLocalDate
        val matches =
          startTimeDate.getYear == date.getYear &&
            startTimeDate.getMonth == date.getMonth &&
            startTimeDate.getDayOfMonth == date.getDayOfMonth
        matches
      })

      // Clears previous events and add filtered events
      dailyView.clearEvents()
      dailyView.addEvents(eventsForDay)

      // Switch to dailyView
      switchScenes(dailyViewScene)
    })

    // Adds events to dailyView and to weekView
    dailyView.addEvents(allEvents)
    weekView.addEvents(allEvents)

    val primaryStage = new JFXApp3.PrimaryStage():
      title = "SCALENDAR - CALENDAR"
      width = constants.windowWidth
      height = constants.windowHeight
      scene = weekViewScene

    stage = primaryStage
  }

  // Switches to the given scene
  def switchScenes(scene: Scene) =
    stage.setScene(scene)

  // Creates the weekViewScene
  private def createWeekViewScene(fontSize: Double): Scene = {
    val welcomeLabel = new Label("Welcome to your calendar") {
      textFill = Black
      font = Font.font(
        "Montserrat",
        FontWeight.Light,
        FontPosture.Italic,
        fontSize * 0.7
      )
    }

    // Label for the weeknumber
    val weekLabel = new Label("Week " + weekNumber.toString) {
      font = Font.font("Montserrat", FontWeight.Medium, fontSize * 1.5)
    }
    // Button to navigate to previous week
    val preWeekButton = new Button("◀") {
      onAction = event => navigateBetweenWeeks(-1)
      this.setStyle(
        "-fx-background-color: #fff; " +
          "-fx-border-radius: 24px; " +
          "-fx-border-style: none; " +
          "-fx-text-fill: #3c4043; " +
          "-fx-font-family: 'Google Sans', Roboto, Arial, sans-serif; " +
          "-fx-font-size: 14px; " +
          "-fx-font-weight: 500; " +
          "-fx-pref-height: 40px; " +
          "-fx-padding: 2px 24px; " +
          "-fx-alignment: center; " +
          "-fx-transition: box-shadow 280ms cubic-bezier(.4, 0, .2, 1), " +
          "opacity 15ms linear 30ms, " +
          "transform 270ms cubic-bezier(0, 0, .2, 1) 0ms; " +
          "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, .2), 3, 0, 0, 3);"
      )
    }
    // Button to navigate to next week
    val nextWeekButton = new Button("▶") {
      onAction = event => navigateBetweenWeeks(+1)
      this.setStyle(
        "-fx-background-color: #fff; " +
          "-fx-border-radius: 24px; " +
          "-fx-border-style: none; " +
          "-fx-text-fill: #3c4043; " +
          "-fx-font-family: 'Google Sans', Roboto, Arial, sans-serif; " +
          "-fx-font-size: 14px; " +
          "-fx-font-weight: 500; " +
          "-fx-pref-height: 40px; " +
          "-fx-padding: 2px 24px; " +
          "-fx-alignment: center; " +
          "-fx-transition: box-shadow 280ms cubic-bezier(.4, 0, .2, 1), " +
          "opacity 15ms linear 30ms, " +
          "transform 270ms cubic-bezier(0, 0, .2, 1) 0ms; " +
          "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, .2), 3, 0, 0, 3);"
      )
    }
    // Contains buttons to navigate between weeks
    val navigationContainer = new HBox {
      spacing = 4
      children = Seq(weekLabel, preWeekButton, nextWeekButton)
    }

    // Container for weekView
    val weekViewborderPane = new BorderPane {
      padding = Insets(constants.windowWidth * 0.01)
      top = new HBox(10) {
        children = Seq(welcomeLabel, createAddEventButton())
      }
      center = weekView
      right = navigationContainer
      this.setStyle(
        "-fx-background-color: linear-gradient(to bottom, #f5f7fa, #e4e8f0);"
      )
    }
    // Returns weekViewScene
    new Scene(weekViewborderPane)
  }

  // Creates the dailyViewScene
  private def createDailyViewScene(fontSize: Double): Scene = {
    // Add date header for dailyView
    dateHeader = new Label("") {
      font = Font.font("Montserrat", FontWeight.Bold, fontSize * 1.5)
      this.setStyle("-fx-padding: 10 0 10 0;")
    }

    // Back button for navigation
    val backButton = new Button("Back to Week View") {
      onAction = event => switchScenes(createWeekViewScene(fontSize))
      this.setStyle(
        "-fx-background-color: #fff; " +
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
      )
    }

    // Add header container
    val headerContainer = new VBox {
      spacing = 10
      children = Seq(
        new HBox(10) { children = Seq(backButton, createAddEventButton()) },
        dateHeader
      )
    }

    // Container for dailyView
    val dailyViewborderPane = new BorderPane {
      padding = Insets(constants.windowWidth * 0.01)
      top = headerContainer
      center = dailyView
      this.setStyle(
        "-fx-background-color: linear-gradient(to bottom, #f5f7fa, #e4e8f0);"
      )
    }
    // Returns dailyViewScene
    new Scene(dailyViewborderPane)
  }

end Main
