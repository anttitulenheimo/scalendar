package com.calendar.ui.components

import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.Label
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color
import scalafx.scene.shape.Line
import scalafx.scene.text.{Font, FontPosture, FontWeight}

object hourView extends VBox:
  spacing = 10
  padding = Insets(10)
  alignment = Pos.TopLeft

  // Returns the current hour
  val currentHour = java.time.LocalTime.now().getHour

  // Every hour have a different label
  for hour <- 0 until 24 do

    val hourContainer = new VBox() {
      spacing = 2
    }

    val hourLabel = new Label(f"$hour%02d:00") {
      font = Font.font("Montserrat", FontWeight.Light, FontPosture.Regular, 14)
    }

    if (hour == currentHour) then
      style = "-fx-font-weight: bold; -fx-text-fill: #2980b9;"

    hourContainer.children.add(hourLabel)

    // Adds a line after every hour
    if (hour < 24) then
      val separator = new Line {
        startX = 0
        endX = 60
        stroke = Color.Grey
      }
      hourContainer.children.add(separator)

      children.add(hourContainer)

