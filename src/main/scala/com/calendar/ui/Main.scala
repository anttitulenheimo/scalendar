package com.calendar.ui

import com.calendar.ui.components.{ hourView, weekView }
import scalafx.application.JFXApp3
import scalafx.geometry.Insets
import scalafx.geometry.Pos.TopCenter
import scalafx.scene.Scene
import scalafx.scene.control.Label
import scalafx.scene.layout.{ BorderPane, HBox, Priority, VBox }
import scalafx.scene.paint.Color.*
import scalafx.scene.text.{ Font, FontPosture, FontWeight }
import scalafx.stage.Screen

object Main extends JFXApp3:

  def start() =
    val primaryStage = new JFXApp3.PrimaryStage():
      title = "SCALENDAR - CALENDAR"
      val screenBounds =
        Screen.primary.visualBounds // Returns the primary screenbounds

      height = screenBounds.height * 0.8
      width = screenBounds.width * 0.8

      val welcomeLabel =
        new Label("Welcome to your calendar") { // ToDO Impelement a better name
          textFill = Black
          font =
            Font.font("Montserrat", FontWeight.Light, FontPosture.Regular, 10)
        }

      val hourViewContainer = new VBox:
        padding = Insets(10)
        children += hourView

      //Ensure hourView
      HBox.setHgrow(hourViewContainer, Priority.Never)

      //weekViewContainer
      val weekViewContainer = new VBox:
        prefHeight = 400
        alignment = TopCenter
        children += weekView

      //Allow weekView to grow
      HBox.setHgrow(weekViewContainer, Priority.Always)

      // Main container
      val calendarContainer = new HBox:
        spacing = 20
        padding = Insets(10, 10, 10, 200)
        children ++= Seq(hourViewContainer, weekViewContainer)

      val borderPane = new BorderPane { // Main pane
        padding = Insets(10)
        top = welcomeLabel
        center = calendarContainer
      }

      scene = new Scene(borderPane)

  end start

end Main
