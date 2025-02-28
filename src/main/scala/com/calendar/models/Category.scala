package com.calendar.models


class Category(val name: String, val colorCode: String) {


  // Returns a color assigned with the category
  def getColorCode(): String = colorCode

  // Assign a color with the category 
  def assignColorCode(newColorCode: String): Unit = ???

}
