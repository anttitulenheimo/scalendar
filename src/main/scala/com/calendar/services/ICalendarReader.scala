package com.calendar.services

import net.fortuna.ical4j.data.CalendarBuilder
import com.calendar.models.{ Category, Event, Reminder }
import net.fortuna.ical4j.model.{ DateTime, Calendar as ICal4jCalendar }
import net.fortuna.ical4j.model.component.VEvent

import scala.collection.JavaConverters.asScalaBufferConverter
import java.io.{ FileInputStream, FileNotFoundException }
import java.time.*

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

        // Use the default category
        val vEventCategory = new Category("default", "#808080")

        val vEventReminder = new Reminder(vEventname, vEventStartingTime)

        // getProperty returns the Java Optional so we need to extract it using isPresent
        val colorCodeOpt = Option(vEvent.getProperty("X-COLOR")).flatMap { javaOpt =>
        if (javaOpt.isPresent) then
          Some(javaOpt.get.getValue)
        else
          None
        }
        val vEventColorCode = colorCodeOpt.getOrElse("#808080")


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
      case e: FileNotFoundException =>
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
      case e =>
        throw new IllegalArgumentException(
          s"Unsupported dateType: ${e.getClass.getName}"
        )
}
