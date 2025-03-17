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

object Main extends JFXApp3:

  def start() =

    val primaryStage = new JFXApp3.PrimaryStage():
      title = "SCALENDAR - CALENDAR"

      width = constants.windowWidth
      height = constants.windowHeight

      val fontSize = constants.windowWidth * 0.015

      val welcomeLabel =
        new Label("Welcome to your calendar") { // ToDO Impelement a better name
          textFill = Black
          font = Font.font(
            "Montserrat",
            FontWeight.Light,
            FontPosture.Regular,
            fontSize
          )
        }

      val hourViewContainer = new VBox:
        padding = Insets(constants.windowWidth * 0.01)
        children += hourView
        
        
      // Ensure hourView
      HBox.setHgrow(hourViewContainer, Priority.Never)

      // weekViewContainer
      val weekViewContainer = new VBox:
        prefHeight = constants.windowHeight * 0.5
        alignment = TopCenter
        children += weekView

      // Allow weekView to grow
      HBox.setHgrow(weekViewContainer, Priority.Always)

      // Main container
      val calendarContainer = new HBox:
        spacing = constants.windowWidth * 0.02
        padding = Insets(
          constants.windowWidth * 0.02,
          constants.windowWidth * 0.02,
          constants.windowWidth * 0.02,
          constants.windowWidth * 0.15
        )
        children ++= Seq(hourViewContainer, weekViewContainer)

      val borderPane = new BorderPane { // Main pane
        padding = Insets(constants.windowWidth * 0.01)
        top = welcomeLabel
        center = calendarContainer
      }

      scene = new Scene(borderPane)

  end start

end Main


