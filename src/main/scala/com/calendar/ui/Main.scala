package com.calendar.ui

import com.calendar.ui.components.weekView
import scalafx.application.JFXApp3
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.Label
import scalafx.scene.layout.BorderPane
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

      val borderPane = new BorderPane {
        padding = Insets(constants.windowWidth * 0.01)
        top = welcomeLabel
        center = weekView
      }

      scene = new Scene(borderPane)

  end start

end Main
