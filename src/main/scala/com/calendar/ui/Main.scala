package com.calendar.ui

import com.calendar.ui.components.calendarView
import scalafx.application.JFXApp3
import scalafx.geometry.Insets
import scalafx.geometry.Pos.Center
import scalafx.scene.Scene
import scalafx.scene.control.Label
import scalafx.scene.layout.{ HBox, Pane }
import scalafx.scene.shape.Rectangle
import scalafx.scene.paint.Color.*
import scalafx.stage.Screen
import scalafx.scene.layout.{ HBox, VBox }
import scalafx.scene.text.{ Font, FontPosture, FontWeight }

object Main extends JFXApp3:

  def start() =
    val primaryStage = new JFXApp3.PrimaryStage():
      title = "SCALENDAR - CALENDAR"
      val screenBounds =
        Screen.primary.visualBounds // Returns the primary screenbounds

      height = screenBounds.height * 0.8
      width = screenBounds.width * 0.8

      val welcomeLabel = new Label("Welcome to your calendar") //ToDO Impelement a better name
      welcomeLabel.textFill = Blue
      welcomeLabel.font = Font.font("Times New Roman")

      val root = new VBox():
        padding = Insets(10)
        alignment = Center
        
      
      root.children += welcomeLabel
      root.children += calendarView
      

      scene = new Scene(root)

  end start

end Main
