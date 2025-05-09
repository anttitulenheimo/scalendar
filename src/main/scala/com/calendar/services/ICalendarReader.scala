package com.calendar.services

import net.fortuna.ical4j.data.CalendarBuilder
import com.calendar.models.{ Category, Event, Reminder }
import com.calendar.ui.constants
import net.fortuna.ical4j.model.{ DateTime, Calendar as ICal4jCalendar }
import net.fortuna.ical4j.model.component.VEvent

import scala.collection.JavaConverters.asScalaBufferConverter
import java.io.{ FileInputStream, FileNotFoundException }
import java.time.{ LocalDateTime, ZoneId }

class ICalendarReader(filename: String) {

  def readEvents(): Seq[Event] =
    var inputStream: FileInputStream = null

    try {
      inputStream = new FileInputStream(filename)
      val builder = new CalendarBuilder()
      val calendar: ICal4jCalendar = builder.build(inputStream)

      // Get events using .getComponents
      // Convert them to a buffer
      val events =
        calendar.getComponents("VEVENT").asScala.map(_.asInstanceOf[VEvent])

      // Map all vEvents to Seq[Event]
      events.map { vEvent =>

        // Name is the summary
        val vEventname = Option(vEvent.getSummary)
          .map(_.getValue)
          .getOrElse("Untitled event")

        // Extracting times and convert them to correct dateType
        val vEventStartingTime: LocalDateTime =
          convertToLocalDateTime(vEvent.getDateTimeStart.getDate)

        val vEventEndingTime: LocalDateTime =
          convertToLocalDateTime(vEvent.getDateTimeEnd.getDate)

        // Convert the LocalDateTime to LocalDate
        val vEventDate = vEventStartingTime.toLocalDate

        // AdditionalInfo is  the eventDescription
        val vEventDescription = Option(vEvent.getDescription).map(_.getValue)

        val vEventReminder =
          Option(vEvent.getProperty("X-REMINDERNAME")).flatMap { reminderProp =>
            if (reminderProp.isPresent) then
              // There is a reminder so the remindertime is read too
              Option(vEvent.getProperty("X-REMINDERTIME")).flatMap {
                minutesProp =>
                  if (minutesProp.isPresent) then
                    try {
                      val minutesParsed: Int = minutesProp.get.getValue.toInt
                      Some(new Reminder(vEventname, vEventStartingTime))
                    } catch {
                      case _: Exception => None
                    }
                  else None
              }
            else None
          }

        // getProperty returns the Java Optional so we need to extract it using isPresent
        val colorCodeOpt = Option(vEvent.getProperty("X-COLOR")).flatMap {
          javaOpt =>
            if (javaOpt.isPresent) then Some(javaOpt.get.getValue)
            else None
        }
        val vEventColorCode = colorCodeOpt.getOrElse("#A0B8D9")

        val vEventCategoryName = Option(vEvent.getProperty("X-CATEGORY"))
          .flatMap { javaOpt =>
            if (javaOpt.isPresent) then Some(javaOpt.get.getValue)
            else None
          }
          .getOrElse("")

        // If the category is not found then the default is used
        val vEventCategory = constants.defaultCategories
          .find(_.name == vEventCategoryName)
          .getOrElse(new Category(vEventCategoryName, vEventColorCode))

        // Creates a new Event
        new Event(
          name = vEventname,
          date = vEventDate,
          startingTime = vEventStartingTime,
          endingTime = vEventEndingTime,
          category = vEventCategory,
          reminder = vEventReminder,
          additionalInfo = vEventDescription,
          colorCode = vEventColorCode
        )
      }.toSeq
    } catch {
      case _: FileNotFoundException =>
        println(s"File not found: $filename")
        Seq.empty[Event]
      case e: Exception =>
        println(s"Error reading calendar file: ${e.getMessage}")
        Seq.empty[Event]
    } finally {
      if (inputStream != null) then
        inputStream
          .close() // If the inputstream is null then the NullPointerException would occur
    }

// Helper method to convert iCal4j dateType to LocalDateTime
  private def convertToLocalDateTime(date: Any): LocalDateTime =
    date match
      case z: java.time.ZonedDateTime =>
        z.toLocalDateTime

      case d: net.fortuna.ical4j.model.DateTime =>
        val instant = d.toInstant
        val zone = ZoneId.systemDefault()
        LocalDateTime.ofInstant(instant, zone)

      case d: java.util.Date =>
        val instant = d.toInstant
        val zone = ZoneId.systemDefault()
        LocalDateTime.ofInstant(instant, zone)

      case d: java.time.LocalDate =>
        d.atStartOfDay()

      case e =>
        throw new IllegalArgumentException(
          s"Unsupported dateType: ${e.getClass.getName}"
        )
}
