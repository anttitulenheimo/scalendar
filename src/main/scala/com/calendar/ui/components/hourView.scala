package com.calendar.ui.components

import scalafx.geometry.{ Insets, Pos }
import scalafx.scene.control.Label
import scalafx.scene.layout.VBox
import scalafx.scene.text.{ Font, FontPosture, FontWeight }

object hourView extends VBox:
  spacing = 10
  padding = Insets(10)
  alignment = Pos.TopLeft
  style =
    "-fx-border-color: black; -fx-border-width: 2px; -fx-border-radius: 5px;"

  // Every hour have a different label
  for hour <- 0 until 24 do
    val hourLabel = new Label(f"$hour%02d:00") {
      font = Font.font("Montserrat", FontWeight.Light, FontPosture.Regular, 14)
    }
    children.add(hourLabel)
