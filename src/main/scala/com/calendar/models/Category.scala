package com.calendar.models


class Category(val name: String, val colorCode: String) {

  def getColorCode(): String = colorCode

  def assignColorCode(newColorCode: String): Unit = ???

}
