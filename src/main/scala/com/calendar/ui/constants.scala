package com.calendar.ui

import com.calendar.models.Category
import scalafx.scene.paint.Color
import scalafx.stage.Screen
//Constants object for the UI components to use

object constants {

  val screenBounds =
    Screen.primary.visualBounds // Returns the primary screenbounds
  val windowWidth = screenBounds.width * 0.8 // Scaled
  val windowHeight = screenBounds.height * 0.8 // Scaled

  val eventDefaultColor = "#808080" // Grey

  val defaultCategory = new Category("default", eventDefaultColor)

  // Helper method to convert to CSS color
  def toCssColor(color: Color): String =
    f"#${(color.red * 255).toInt}%02X${(color.green * 255).toInt}%02X${(color.blue * 255).toInt}%02X"

}
