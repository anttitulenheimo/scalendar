package com.calendar.ui.components

import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.Label
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color
import scalafx.scene.shape.Line
import scalafx.scene.text.{Font, FontPosture, FontWeight}
import com.calendar.ui.constants

object hourView extends VBox:
  spacing = 10
  padding = Insets(constants.windowWidth * 0.01)
  alignment = Pos.TopLeft

  // Returns the current hour
  val currentHour = java.time.LocalTime.now().getHour

  val fontSize = constants.windowWidth * 0.007

  // Every hour have a different label
  for hour <- 0 until 24 do

    val hourContainer = new VBox() {
      spacing = constants.windowWidth * 0.001
    }

    val hourLabel = new Label(f"$hour%02d:00") {
      font = Font.font("Montserrat", FontWeight.Light, FontPosture.Regular, fontSize)
    }

    if (hour == currentHour) then
      style = "-fx-font-weight: bold; -fx-text-fill: #2980b9;"

    hourContainer.children.add(hourLabel)

    // Adds a line after every hour
    if (hour < 24) then
      val separator = new Line {
        startX = 0
        endX = 60 //ToDo needs to be scalable
        stroke = Color.Grey
      }
      hourContainer.children.add(separator)

      //Adds the hours
      children.add(hourContainer)
      

