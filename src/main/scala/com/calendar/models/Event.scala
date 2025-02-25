package com.calendar.models

import java.time.LocalDateTime

class Event(
  val name: String,
  val date: LocalDateTime,
  val category: Category,
  val reminder: Reminder,
  val additionalInfo: Option[String],
  val colorCode: String
)
