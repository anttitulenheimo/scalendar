package com.calendar.ui

import com.calendar.models.Event
import com.calendar.services.{ Calendar, ReminderManager }
import com.calendar.ui.components.*
import com.calendar.ui.constants.defaultCategories
import scalafx.application.JFXApp3
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.SceneIncludes.jfxScene2sfx
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{ Alert, Button, Label }
import scalafx.scene.layout.{ BorderPane, HBox, VBox }
import scalafx.scene.paint.Color.*
import scalafx.scene.text.{ Font, FontPosture, FontWeight }
import scalafx.stage.FileChooser

import java.time.temporal.IsoFields
import java.time.{ DayOfWeek, LocalDate, LocalDateTime }
import java.util.{ Timer, TimerTask }
import scala.compiletime.uninitialized

object Main extends JFXApp3:

  // Set the dateHeader to be uninitialized
  private var dateHeader: Label = uninitialized

  // For the category filtering and search filtering
  private var filteredEventsOn = false
  private var currentFilteredEvents: Seq[Event] = Seq()

  private def createDeleteEventButton(): Button = new Button("Delete") {
    onAction = _ =>
      if calendar.getAllEvents.nonEmpty then
        val result = deleteEventPopup.showDialog(stage, allEvents)
        result match
          case Some(eventToDelete: Event) =>
            if (calendar.deleteEvent(eventToDelete.name)) then
              // Update the weekView and dailyView
              weekView.clearEvents()
              weekView.addEvents(calendar.getAllEvents)
              dailyView.clearEvents()

              // Update allEvents
              allEvents = allEvents.filter(_.name != eventToDelete.name)
              eventSeqMyCalendar = eventSeqMyCalendar.filter(_.name != eventToDelete.name) // Deletes the event from the eventSeqMyCalendar

          case _ =>

      else
        // Alert because no events
        new Alert(AlertType.Information) {
          title = "No events"
          headerText = "No events to delete"
        }.showAndWait()

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

  private def createSaveButton(): Button = new Button("Save") {
    onAction = _ =>
      val allUserEvents =
        calendar.getAllEvents.filterNot(eventSeqPublicHolidays.contains)
      // Filter the userEvents
      eventSeqMyCalendar = allUserEvents
      // Temporary calendar for saving
      val tempCal = new Calendar(Map(), defaultCategories)
      // Adds current events from eventSeqMyCalendar to the tempCal
      allUserEvents.foreach(event => tempCal.addEvent(event))
      // Uses tempCal's saveToFile method to save to myCalendar.ics
      tempCal.saveToFile("src/main/resources/myCalendar.ics")
      // Alerts that events were saved
      new Alert(AlertType.Information) {
        title = "Events saved"
        headerText = "Calendar saved successfully"
        contentText = s"${eventSeqMyCalendar.size} events saved"
      }.showAndWait()

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

  // Calendar instance to handle events
  private val calendar = new Calendar(Map(), defaultCategories)

  // Initialize empty seq
  private var eventSeqPublicHolidays: Seq[Event] = Seq()

  // Initialize empty seq
  private var eventSeqMyCalendar: Seq[Event] = Seq()

  // Initialize empty seq
  private var allEvents: Seq[Event] = Seq()

  // Initialize the reminderManager with an empty seq
  private val reminderManager = new ReminderManager(Seq())

  // Set up the timer
  private def reminderTimer() =
    val reminderTimer = new Timer(true) // True if the timer is repeating
    val timerDelayBeforeRepeat = 60000 // ms
    val remindTask = new TimerTask {
      override def run(): Unit =
        // Check and notify
        reminderManager.notifyEvents()
      // Clean the expired events
      reminderManager.cleanReminders()
    }
    // Shchedules the task
    reminderTimer.schedule(remindTask, 0, timerDelayBeforeRepeat)

  private val today = LocalDate.now
  private var startOfWeek = today.`with`(DayOfWeek.MONDAY)
  private var weekNumber = LocalDate.now.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)

  // Button to navigate to this day
  private def createTodayButton(): Button = new Button("Current week") {
    onAction = _ =>
      startOfWeek = LocalDate.now.`with`(DayOfWeek.MONDAY)
      weekNumber = startOfWeek.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)
      refreshWeekView()
      switchScenes(createWeekViewScene(constants.windowWidth * 0.01, None))

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

  // Handles the week navigation
  private def navigateBetweenWeeks(amount: Int): Unit =
    // Adds 1 or -1 weeks
    startOfWeek = startOfWeek.plusWeeks(amount)
    // Updates weekNumber
    weekNumber = startOfWeek.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)
    // Refresh the weekView
    refreshWeekView()
    // Creates a new weekViewScene and switches scenes depening is the filtering on
    switchScenes(
      if (filteredEventsOn) then
        createWeekViewScene(
          constants.windowWidth * 0.01,
          Some(createResetFilterButton())
        )
      else createWeekViewScene(constants.windowWidth * 0.01, None)
    )

  // Handles the refreshing of a selected week
  private def refreshWeekView(): Unit =
    weekView.clearEvents()
    weekView.weekViewDatesRefresher(startOfWeek)
    if (filteredEventsOn) then weekView.addEvents(currentFilteredEvents)
    else weekView.addEvents(allEvents)

  // For the createAddEventButton's args
  val defaultStartTime = LocalDateTime.now().withSecond(0).withNano(0)
  val defaultEndTime = defaultStartTime.plusHours(1)

  private def createAddEventButton(): Button = new Button("Add Event") {
    onAction = _ =>
      val result = addEventPopup.showDialog(
        stage,
        defaultCategories,
        defaultStartTime,
        defaultEndTime
      )

      result match
        case Some(event: Event) => // If the object is type of Event
          // Adds the event to the calendar
          calendar.addEvent(event)
          // Set a reminder
          event.reminder match
            case Some(_) => reminderManager.setReminder(event)
            case None    =>
          // Updates
          allEvents = allEvents :+ event
          eventSeqMyCalendar = eventSeqMyCalendar :+ event

          // Navigate to the week containing the newly created event
          startOfWeek = event.date.`with`(DayOfWeek.MONDAY)
          weekNumber = startOfWeek.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)

          refreshWeekView()

          // Adds the new event also for the dailyView
          if (stage.scene().root.value == dailyView) then
            val eventsForDay =
              allEvents.filter(_.startingTime.toLocalDate == event.date)
            dailyView.clearEvents()
            dailyView.addEvents(eventsForDay, event.date)
            // Refresh
            switchScenes(
              createWeekViewScene(constants.windowWidth * 0.01, None)
            )

        case _ => // The added event was canceled
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

  def addEventByMouse(event: Event) = {
    // Adds the event to the calendar
    calendar.addEvent(event)

    // Set a reminder
    event.reminder match
      case Some(_) => reminderManager.setReminder(event)
      case None    =>

    // Updates
    allEvents = allEvents :+ event
    eventSeqMyCalendar = eventSeqMyCalendar :+ event

    refreshWeekView()

    // Update dailyView
    val eventsForDay =
      allEvents.filter(_.startingTime.toLocalDate == event.date)
    dailyView.clearEvents()
    dailyView.addEvents(eventsForDay, event.date)

  }

  private def createCategoryFilterButton(): Button = new Button(
    "Filter by category"
  ) {
    onAction = _ => {
      val result = categoryFilterPopup.showDialog(stage, defaultCategories)
      result match {
        case Some(realCategories) =>
          val filteredEvents = calendar.filterEventsByCategory(realCategories)
          currentFilteredEvents = filteredEvents
          filteredEventsOn = true
          // Update the weekView and dailyView
          weekView.clearEvents()
          weekView.addEvents(filteredEvents)
          // Reset filter button is shown when filtered events are shown
          val resetFilterButton = createResetFilterButton()
          // Show the filtered weekView with a reset button
          switchScenes(
            createWeekViewScene(
              constants.windowWidth * 0.01,
              Some(resetFilterButton)
            )
          )
        case _ =>
      }
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

  private def createResetFilterButton(): Button = new Button("Reset filter") {
    onAction = _ => {
      filteredEventsOn = false
      currentFilteredEvents = Seq[Event]() // Filtered events are emptied
      // Refresh and load weekView again
      refreshWeekView()
      switchScenes(createWeekViewScene(constants.windowWidth * 0.01, None))
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

  private def createSearchEventButton(): Button = new Button("Search events") {
    onAction = _ => {
      val result = searchEventPopup.showDialog(stage, allEvents)
      result match {
        case Some(realEventSeq) =>
          if realEventSeq.nonEmpty then
            currentFilteredEvents = realEventSeq
            filteredEventsOn = true
            // Update the weekView to show searched events
            weekView.clearEvents()
            weekView.addEvents(realEventSeq)
            // Reset filter button is shown when filtered events are shown
            val resetFilterButton = createResetFilterButton()
            // Show the filtered weekView with a reset button
            switchScenes(
              createWeekViewScene(
                constants.windowWidth * 0.01,
                Some(resetFilterButton)
              )
            )
          else
            new Alert(AlertType.Information) { // No events found alert
              title = "Search Results"
              headerText = "No events found"
              contentText = "No events found by your searchword."
            }.showAndWait()

        case _ =>
      }
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

  private def createChooseFromFileButton(): Button = new Button(
    "Load from file"
  ) {
    onAction = _ => {
      val fileChooser = new FileChooser() {
        title = "Open a calendar file"
      }
      fileChooser.getExtensionFilters.add(
        new FileChooser.ExtensionFilter("Calendar files", "*.ics")
      )
      val selectedFile = fileChooser.showOpenDialog(stage)

      // Events are loaded from a file if the file is selected
      if (selectedFile != null) then
        try {
          val loadedEvents =
            calendar.loadFromFile(selectedFile.getAbsolutePath, None)
          allEvents = allEvents ++ loadedEvents // Update the event seq
          eventSeqMyCalendar = eventSeqMyCalendar ++ loadedEvents
          refreshWeekView()
          // Alerts that events were loaded
          new Alert(AlertType.Information) {
            title = "Events loaded"
            headerText = "Events loaded successfully"
            contentText = s"${loadedEvents.size} events saved"
          }.showAndWait()
        } catch {
          case e: Exception =>
            new Alert(AlertType.Error) {
              title = "Error loading file"
              headerText = "Error when loading file"
              contentText = e.getMessage
            }.showAndWait()
        }

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

    // Load public holidays
    eventSeqPublicHolidays =
      calendar.loadFromFile("src/main/resources/finland.ics")

    // Load user events
    eventSeqMyCalendar = calendar.loadFromFile(
      "src/main/resources/myCalendar.ics",
      Some(reminderManager)
    )

    allEvents = eventSeqMyCalendar

    // Uncomment if you want to include Finland's public holidays
    //allEvents = eventSeqPublicHolidays ++ eventSeqMyCalendar



    reminderTimer()

    val fontSize = constants.windowWidth * 0.01

    val weekViewScene: Scene = createWeekViewScene(fontSize, None)
    val dailyViewScene: Scene = createDailyViewScene(fontSize)

    // Set up a day selection handler in weekView
    weekView.setOnDaySelected(date =>
      // Header is now day - date
      val dateString =
        s"${date.getDayOfWeek.toString.capitalize} - ${date.toString}"
      dateHeader.text = dateString

      // Filter based on startingTime
      val eventsForDay = allEvents.filter(event =>
        // Get the start and end dates of the event
        val eventStartDate = event.startingTime.toLocalDate
        val eventEndDate = event.endingTime.toLocalDate
        // Filters now if the date falls in the right range
        (date.isEqual(eventStartDate) || date.isAfter(eventStartDate)) &&
        (date.isEqual(eventEndDate) || date.isBefore(eventEndDate))
      )

      // Clears previous events and add filtered events
      dailyView.clearEvents()
      dailyView.addEvents(eventsForDay, date)

      // Switch to dailyView
      switchScenes(dailyViewScene)
    )

    // Adds events to dailyView and to weekView
    dailyView.addEvents(allEvents, today)
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
  private def createWeekViewScene(
    fontSize: Double,
    resetFilterButton: Option[Button]
  ): Scene = {
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
    // Button to navigate to the previous week
    val preWeekButton = new Button("◀") {
      onAction = _ => navigateBetweenWeeks(-1)
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
      onAction = _ => navigateBetweenWeeks(+1)
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
        children = Seq(
          welcomeLabel,
          createAddEventButton(),
          createSaveButton(),
          createDeleteEventButton(),
          createTodayButton(),
          createCategoryFilterButton(),
          createSearchEventButton(),
          createChooseFromFileButton()
        ) ++ resetFilterButton // resetFilterButton is added if it is given
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
    // Add a date header for dailyView
    dateHeader = new Label("") {
      font = Font.font("Montserrat", FontWeight.Bold, fontSize * 1.5)
      this.setStyle("-fx-padding: 10 0 10 0;")
    }

    // Back button for navigation
    val backButton = new Button("Back to Week view") {
      onAction = _ => switchScenes(createWeekViewScene(fontSize, None))
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

    // Add a header container
    val headerContainer = new VBox {
      spacing = 10
      children = Seq(
        new HBox(10) {
          children = Seq(
            backButton,
            createAddEventButton(),
            createSaveButton(),
            createDeleteEventButton(),
            createCategoryFilterButton()
          )
        },
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
