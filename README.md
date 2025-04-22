
![SCALENDAR](https://github.com/anttitulenheimo/scalendar/blob/master/DEMOPICS/weekView.png)

# Calendar application

[![GitHub issues](https://img.shields.io/github/issues-raw/navendu-pottekkat/awesome-readme)](https://github.com/anttitulenheimo/scalendar/issues)
[![GitHub pull requests](https://img.shields.io/github/issues-pr/navendu-pottekkat/awesome-readme)](https://github.com/anttitulenheimo/scalendar/pulls)

## About

ScalaFX Calendar is a desktop application that allows you to create and manage your own calendar with various types of events.

You can store:
- Event name, date, start and end time (e.g., lectures, meetings, exercises)
- Categorized entries such as study-related tasks, hobbies, work shifts, holidays, and personal reminders
- Public holidays and tasks with deadlines

### Features

- Week view: each day is shown in its own column with all scheduled items listed vertically
- Day view: full 24-hour layout, with items shown in their respective time slots
- Long time spans supported (events across midnight or multiple days)
- Add, edit, and delete calendar items via user input
- Navigate forward/backward by week
- Data is saved to and loaded from a file
- Events can be imported/exported in a standard format for compatibility with other calendars

### With graphical user interface:
- Color-coded events based on categories (e.g., lectures in dark red, hobbies in green)
- Select time slots with the mouse and add entries directly
- Add extra information to events: location, course, link, participants, etc.
- Filter calendar view to show only selected categories (e.g., study or work)
- Search for all events related to a course, hobby, or person and display them in a list




# Installation

To build and run the project locally:

### 1. Requirements

- Java JDK 11 or newer
- [sbt (Scala Build Tool)](https://www.scala-sbt.org/)

### 2. Clone the repository

Clone the repository and navigate to the project directory:

```bash
git clone https://github.com/anttitulenheimo/scalendar.git
```

```bash
cd scalendar
```
3. To run the application execute the following command:

```bash
sbt run
```

4. Create a runnable .jar file
To generate a standalone .jar file, run:

```bash
sbt assembly
```
If you don't have the sbt-assembly plugin configured, you can add it by modifying your project/plugins.sbt file:

```scala
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "2.1.5")
```
And in your build.sbt, add the following:

```scala
enablePlugins(AssemblyPlugin)
```
```scala
mainClass in assembly := Some("your.package.MainClass") // replace with the actual main class of your project
```

After running sbt assembly, the JAR file will be created in the target/scala-<version>/<project-name>-assembly-<version>.jar directory.

5. Run the generated JAR
To run the .jar file, use the following command:

```bash
java -jar target/scala-<version>/scalendar-assembly-<version>.jar
```


# Usage

This calendar allows you to manage your events, view your schedule in different formats, and filter/search through your events. Below are the main features and how to use them:

### 1. Add an Event
You can easily add events to the calendar by specifying the event name, date, start and end time. This will allow you to keep track of meetings, lectures, personal tasks, and more.

To add an event, click the "Add Event" button and fill out the required details.

![Add Event](https://github.com/anttitulenheimo/scalendar/blob/master/DEMOPICS/addEvent.png)

### 2. Daily View
The daily view allows you to see all the events scheduled for a specific day. Each hour of the day is shown on its own row, making it easy to view and manage your day's schedule.

To switch to the daily view, click on the "Daily View" button.

![Daily View](https://github.com/anttitulenheimo/scalendar/blob/master/DEMOPICS/dailyView.png)

### 3. Weekly View
The weekly view shows your schedule for the entire week, with each day represented in its own column. This view is great for planning the entire week ahead.

To switch to the weekly view, click on the "Week View" button.

![Week View](https://github.com/anttitulenheimo/scalendar/blob/master/DEMOPICS/weekView.png)

### 4. Filter Events
You can filter events based on categories such as meetings, study sessions, and personal tasks. This allows you to focus only on specific types of events.

To use the filter, click on the "Filter" button and select the desired category.

![Filter](https://github.com/anttitulenheimo/scalendar/blob/master/DEMOPICS/filter.png)

### 5. Search for Events
The search feature lets you find specific events quickly by searching for keywords such as event names, dates, or other details.

To search for an event, click on the "Search" button and enter the keyword or date you're looking for.

![Search](https://github.com/anttitulenheimo/scalendar/blob/master/DEMOPICS/search.png)


