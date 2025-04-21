package com.calendar.ui.components

import com.calendar.models.Event
import com.calendar.ui.constants
import scalafx.geometry.Pos.Center
import scalafx.scene.control.{ Label, ScrollPane }
import scalafx.scene.layout.*
import scalafx.scene.paint.Color
import scalafx.scene.shape.{ Line, Rectangle }
import scalafx.scene.text.{ Font, FontPosture, FontWeight }

import java.time.{ LocalDate, LocalDateTime, LocalTime }

//Scrollpane to allow user to scroll the 24 hours view

object dailyView extends ScrollPane {

  // Stores the date for the event handling
  private var currentDisplayDate: LocalDate = LocalDate.now()

  val totalMinutes = 24 * 60 // 24 h * 60 min
  val rowHeight =
    constants.windowHeight / totalMinutes // a minute's height scaled

  // The grid is divided into minutes and the full hours are shown
  val dayGrid = new GridPane {
    gridLinesVisible = false
    alignment = Center

    // Returns the current hour and minute
    val currentHour = LocalTime.now().getHour

    val fontSize = constants.windowWidth * 0.007

    // minute grid
    for _ <- 0 until totalMinutes do
      val row = new RowConstraints()
      row.prefHeight = rowHeight
      this.getRowConstraints.add(row)

      // add lines for hours
    for hour <- 0 until 24 do
      val hourLine = new Line {
        startX = 0
        endX = constants.windowWidth
        stroke = Color.LightGray
        strokeWidth = 0.5
        this.setStyle("-fx-opacity: 0.7;")
      }

      // current hour line
      if (hour == currentHour) {
        hourLine.stroke = Color.web("#2980b9")
        hourLine.strokeWidth = 2.0
        hourLine.setStyle("-fx-opacity: 1.0;")
      }

      this.add(hourLine, 0, hour * 60, 2, 1)

    // hourGrids: labels for full hours
    for hour <- 0 until 24 do
      val hourLabel = new Label(f"$hour%02d:00") {
        font = Font.font(
          "Montserrat",
          FontWeight.Light,
          FontPosture.Regular,
          fontSize
        )
      }

      this.add(hourLabel, 0, hour * 60)

    // hourColumn
    val hourColumn = new ColumnConstraints()
    // eventColumn
    val eventColumn = new ColumnConstraints()

    // Adds all the columns
    this.getColumnConstraints.addAll(hourColumn, eventColumn)
  }

  // Adds all events to the calendar
  def addEvents(events: Seq[Event], currentDate: LocalDate) =
    // Stores the date for the drag to add
    currentDisplayDate = currentDate
    events.foreach { event =>
      val eventBox = eventView.createEventDisplay(event)

      // Get event dates
      val eventStartDate = event.startingTime.toLocalDate
      val eventEndDate = event.endingTime.toLocalDate

      // Multiple day events handling

      // Adjusts start time if event begins before current day
      val adjustedStartTime =
        if (eventStartDate.isBefore(currentDate)) then
          LocalDateTime.of(
            currentDate,
            LocalTime.MIN
          ) // LocalTime.Min =  00:00 of current day
        else event.startingTime

      // Adjusts end time if event ends after current day
      val adjustedEndTime =
        if (eventEndDate.isAfter(currentDate)) then
          LocalDateTime.of(
            currentDate,
            LocalTime.MAX
          ) // LocalTime.MAX = 23:59 of current day
        else event.endingTime

      val startHour = adjustedStartTime.getHour
      val startMinute = adjustedStartTime.getMinute
      val endHour = adjustedEndTime.getHour
      val endMinute = adjustedEndTime.getMinute

      val startRow = startHour * 60 + startMinute // a minute index
      val endRow = endHour * 60 + endMinute
      val duration = endRow - startRow

      // Validator for the rowSpan
      val rowSpan = Math.max(1, duration)

      dayGrid.add(eventBox, 1, startRow, 1, rowSpan)
    }

  // Method  for the setOnDaySelected:
  // Removes all event displays from the grid and keep only hour labels and lines
  def clearEvents() =

    val children = dayGrid.children
    val toKeep =
      children.filter(node => // The nodes should be either labels or lines
        node.isInstanceOf[javafx.scene.control.Label] ||
          node.isInstanceOf[javafx.scene.shape.Line]
      )
    dayGrid.children.clear()
    dayGrid.children.addAll(toKeep)

  // Transparent Pane to add rectangle on top of the daygrid
  private val overlayPane = new Pane {
    this.setStyle("-fx-background-color: transparent;")
  }
  // stackpane has the all content
  private val stackPane = new StackPane {
    children = Seq(dayGrid, overlayPane)
  }

  // Drag to add a new event handling

  var startY = 0.0
  var selected: Option[Rectangle] = None
  // When leftMouseButton is pressed it creates a rectangle for visualization and calculates the args for the addEventPopup
  overlayPane.onMousePressed = event => {
    // If the left mouse button is pressed
    if (event.isPrimaryButtonDown) then {

      startY = event.getY

      // Rectangle's width is the same as daygrid's width
      val rectangle = new Rectangle {
        x = 0
        y = startY
        width = dayGrid.getWidth
        height = 1
        fill = Color.LightSkyBlue
        opacity = 0.5

      }
      overlayPane.children.add(rectangle)
      selected = Some(rectangle)

    }
  }

  // If the left mouse button is hold and the mouse is dragged
  overlayPane.onMouseDragged = event => {
    selected match {
      case Some(realRectangle: Rectangle) =>
        val currentY = event.getY

        // Update start and end points of the rectangle
        val topY = math.min(startY, currentY)
        val height = currentY - startY

        // Update rectangle's y coordinate is updated by the mouse's position
        realRectangle.y = topY
        realRectangle.height = height
      case _ =>
    }
  }

  // If the mouse button is released then the rectangle disappears
  overlayPane.onMouseReleased = _ => {
    selected match {
      case Some(realRectangle: Rectangle) =>
        // Get the real daygrid height
        val totalHeight = dayGrid.layoutBounds.value.getHeight
        // The row calculations require scaledHeight
        val scaledHeight = totalHeight / totalMinutes

        val startY = realRectangle.y.value
        val endY = realRectangle.y.value + realRectangle.height.value

        val startRow = (startY / scaledHeight).toInt
        val endRow = (endY / scaledHeight).toInt

        val restrictedStartRow = Math.max(
          0,
          Math.min(totalMinutes - 1, startRow)
        ) // The startRow cant be smaller than 0
        val restrictedEndRow =
          Math.max(
            restrictedStartRow + 1,
            Math.min(totalMinutes - 1, endRow)
          ) // The endRow cant be more than 23:59

        // The hours are type of int
        val startHour = restrictedStartRow / 60
        val startMinute = restrictedStartRow % 60
        val endHour = restrictedEndRow / 60
        val endMinute = restrictedEndRow % 60

        // The currentDisplayDate ensures a right date for the addEventPopup
        val startDateTime = LocalDateTime.of(
          currentDisplayDate,
          LocalTime.of(startHour, startMinute)
        )
        val endDateTime =
          LocalDateTime.of(currentDisplayDate, LocalTime.of(endHour, endMinute))

        // Removes the rectangle after releasing the mouse
        overlayPane.getChildren.remove(realRectangle)
        selected = None

        val result = addEventPopup.showDialog(
          com.calendar.ui.Main.stage,
          constants.defaultCategories,
          startDateTime,
          endDateTime
        )

        result match { // Adds the event
          case Some(realResult: Event) =>
            com.calendar.ui.Main.addEventByMouse(realResult)
          case _ =>
        }

      case _ =>

    }

  }

  // Scrollpane uses stackpane as the content
  content = stackPane

  fitToWidth = true
  fitToHeight = false
  this.setStyle(
    "-fx-background-color: transparent; " +
      " -fx-padding: 10px; " +
      " -fx-border-radius: 10px; " +
      " -fx-border-color: #ccc; " +
      " -fx-hbar-policy: never; " +
      " -fx-vbar-policy: always;"
  )

}
