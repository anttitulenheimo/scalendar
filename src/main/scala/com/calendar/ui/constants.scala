package com.calendar.ui

import scalafx.stage.Screen
import scalafx.scene.paint.Color
//Constants object for the UI components to use

object constants {

  val screenBounds =
    Screen.primary.visualBounds // Returns the primary screenbounds
  val windowWidth = screenBounds.width * 0.8 // Scaled
  val windowHeight = screenBounds.height * 0.8 // Scaled
  
  val eventDefaultColor = Color.LightGreen
  

}
