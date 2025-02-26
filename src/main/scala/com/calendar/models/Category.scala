package com.calendar.models

import scalafx.scene.paint.Color

class Category(val name: String, val colorCode: String) {

  def getColorCode(): String = colorCode

  def assignColorCode(newColorCode: String): Unit = ???

}
