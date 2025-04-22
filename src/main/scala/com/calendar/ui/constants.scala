package com.calendar.ui

import com.calendar.models.Category
import scalafx.scene.paint.Color
import scalafx.stage.Screen

//Constants object for the UI components to use
object constants {

  lazy val screenBounds =
    Screen.primary.visualBounds // Returns the primary screenbounds
  lazy val windowWidth = screenBounds.width * 0.8 // Scaled
  lazy val windowHeight = screenBounds.height * 0.8 // Scaled

  val eventDefaultColor = "#808080" // Grey

  val defaultCategory = new Category("default", eventDefaultColor)

  // Some default categories
  val defaultCategories = Seq[Category](
    new Category("Work", "#1E90FF"), // Blue
    new Category("Personal", "#32CD32"), // Green
    new Category("Health", "#FF4500"), // Orange
    new Category("Hobbies", "#9370DB"), // Purple
    new Category("Urgent", "#DC143C"), // Red
    new Category("School", "#FFD700") // Yellow
  )

  // Helper method to convert to CSS color
  def toCssColor(color: Color): String =
    // Colors to 0-255 range
    val redInt = (color.red * 255).toInt
    val greenInt = (color.green * 255).toInt
    val blueInt = (color.blue * 255).toInt
    // Compononents as 2 digis
    val redHex = f"$redInt%02X"
    val greenHex = f"$greenInt%02X"
    val blueHex = f"$blueInt%02X"
    // Combined components
    f"#$redHex$greenHex$blueHex"

}
