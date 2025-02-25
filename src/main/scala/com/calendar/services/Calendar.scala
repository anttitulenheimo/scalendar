package com.calendar.services

import com.calendar.models.{ Category, Event }

class Calendar(
  var events: Map[String, List[Event]],
  var categories: Map[String, Category]
)
