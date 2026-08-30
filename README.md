# Alpha Task Manager

Alpha is a command-line task manager written in Java. You can create todos, deadlines, and events, mark tasks as complete, and delete tasks. Tasks are saved automatically and restored the next time the application starts.

## Requirements

- Java Development Kit (JDK) 25
- IntelliJ IDEA, or a terminal with `javac` and `java`

## Running the application in IntelliJ IDEA

1. Open this project in IntelliJ IDEA.
2. Set the project SDK to JDK 25 and use the SDK default language level.
3. Open `src/main/java/alpha/Alpha.java`.
4. Right-click the file and select **Run `Alpha.main()`**.

## Running from a terminal

From the project root, compile the source files and start Alpha:

```bash
mkdir -p out
javac -d out $(find src/main/java -name '*.java')
java -cp out alpha.Alpha
```

On macOS, if you use SDKMAN, select Java 25 before compiling:

```bash
sdk use java 25.0.3.fx-zulu
```

## Commands

### Add a todo

```text
todo <description>
```

Example:

```text
todo read the software engineering textbook
```

### Add a deadline

Use `/by` to specify when the task is due:

```text
deadline <description> /by <date or time>
```

Example:

```text
deadline return library book /by 2019-06-06
```

### Add an event

Use `/from` and `/to` to specify the event's start and end times:

```text
event <description> /from <start time> /to <end time>
```

Example:

```text
event project meeting /from 2019-08-06 1400 /to 2019-08-06 1600
```

Dates can be entered as `yyyy-MM-dd`, such as `2019-10-15`. Date/time values can use `yyyy-MM-dd HHmm`, such as `2019-10-15 1800`, or the day-first format `d/M/yyyy HHmm`, such as `2/12/2019 1800`. Alpha displays parsed values in a readable format such as `Dec 02 2019 18:00`.

### View and update tasks

```text
list
mark <task number>
unmark <task number>
delete <task number>
```

Task numbers are shown by the `list` command. For example:

```text
list
mark 2
delete 1
```

### Exit

```text
bye
```

## Saving tasks

Alpha saves the task list automatically after every add, delete, mark, and unmark operation. The data is stored in the relative path:

```text
data/duke.txt
```

The `data` folder and file are created automatically the first time a task is saved. Do not delete this file if you want to keep your tasks.

## Example session

```text
todo read a book
deadline submit assignment /by 2019-12-06
event team meeting /from 2019-12-06 1000 /to 2019-12-06 1100
list
mark 1
bye
```
