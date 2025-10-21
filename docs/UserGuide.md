---
layout: page
title: User Guide
---

TutorTrack is a **desktop app for managing contacts, optimized for use via the Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, TutorTrack can help you efficiently track students, organise classes and assignments faster than traditional GUI apps.

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/AY2526S1-CS2103T-F14b-3/tp/releases).

1. Copy the file to the folder you want to use as the _home folder_ for TutorTrack.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar tutortrack.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all contacts.

   * `add n/John Doe p/98765432 l/2 c/Physics-1800 a/Assignment 1` : Adds a student contact named `John Doe` to TutorTrack.

   * `delete 3` : Deletes the 3rd contact shown in the current list.

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [c/CLASS] [a/ASSIGNMENT]` can be used as `n/John Doe c/Physics-1800 a/Assignment 1` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[c/CLASS]…​` can be used as ` ` (i.e. 0 times), `c/Physics-1800`, `c/Math-1400 c/Physics-1800` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Viewing help : `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessageNew.png)

Format: `help`


### Adding a student: `add`

Adds a student to TutorTrack.

Format: `add n/NAME p/PHONE_NUMBER l/LEVEL [c/CLASS]…​ [a/ASSIGNMENT]…​`

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
A student can have any number of classes and assignments (including 0)
</div>

Examples:
* `add n/John Doe p/98765432 l/2`
* `add n/Betsy Crowe a/Assignment 1 p/1234567 c/Chemistry-1400`

### Listing all students : `list`

Shows a list of all students in the address book.

Format: `list`

### Editing a student : `edit`

Edits an existing student in the address book.

Format: `edit INDEX [n/NAME] [p/PHONE_NUMBER] [l/LEVEL] [c/CLASS]…​ [a/ASSIGNMENT]…​`

* Edits the student at the specified `INDEX`. The index refers to the index number shown in the displayed student list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing classes/assignments, the existing classes/assignments of the student will be removed i.e adding of classes/assignments is not cumulative.
* You can remove all the person’s classes/assignments by typing `c/`/ `a/` without
    specifying any classes/assignments after it.

Examples:
*  `edit 1 p/91234567 ` Edits the phone number of the 1st student to be `91234567`.
*  `edit 2 n/Betsy Crower a/` Edits the name of the 2nd person to be `Betsy Crower` and clears all existing assignments.

### Locating students by name: `find`

Finds students whose names contain any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Only the name is searched.
* Only full words will be matched e.g. `Han` will not match `Hans`
* Students matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

Examples:
* `find John` returns `john` and `John Doe`
* `find alex john` returns `alex`, `john`<br>
  ![result for 'find alex john'](images/findAlexJohnResult.png)

### Deleting a student : `delete`

Deletes the specified student from TutorTrack.

Format: `delete INDEX`

* Deletes the student at the specified `INDEX`.
* The index refers to the index number shown in the displayed student list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `delete 2` deletes the 2nd student in the address book.
* `find Betsy` followed by `delete 1` deletes the 1st student in the results of the `find` command.


### Adding assignment(s) to a student: `assign`

Adds one or more assignments to the specified student in TutorTrack.

Format: `assign INDEX a/ASSIGNMENT [a/ASSIGNMENT]...`

* Adds assignment(s) to the student at the specified `INDEX`.
* The index refers to the index number shown in the displayed student list.
* The index **must be a positive integer** 1, 2, 3, …​
* At least one assignment must be provided.
* Duplicate assignments will not be added.

Examples:
* `list` followed by `assign 1 a/MathHW1 a/ScienceTopic2` adds two assignments to the 1st student in the address book.
* `find John` followed by `assign 2 a/ProjectDraft` adds an assignment to the 2nd student in the results of the `find` command.

### Deleting assignment(s) from a student: `unassign`

Deletes one or more assignments from the specified student in TutorTrack.

Format: `unassign INDEX a/ASSIGNMENT [a/ASSIGNMENT]...`

* Deletes assignment(s) from the student at the specified `INDEX`.
* The index refers to the index number shown in the displayed student list.
* The index **must be a positive integer** 1, 2, 3, …​
* At least one assignment must be provided.

Examples:
* `list` followed by `unassign 1 a/MathHW1 a/ScienceTopic2` deletes two assignments from the 1st student in the address book.
* `find John` followed by `unassign 2 a/ProjectDraft` deletes an assignment from the 2nd student in the results of the `find` command.

### Adding class(es) to a student: `addclass`

Adds one or more classes to the specified student in TutorTrack.

Format: `addclass INDEX c/CLASS [c/CLASS]...`

* Adds class(es) to the student at the specified `INDEX`.
* The index refers to the index number shown in the displayed student list.
* The index **must be a positive integer** 1, 2, 3, …​
* At least one class must be provided.

Examples:
* `list` followed by `addclass 1 c/Math-1000 c/Physics-2000` adds two classes to the 1st student in the address book.
* `find John` followed by `addclass 2 c/Chemistry-1400` adds a class to the 2nd student in the results of the `find` command.


### Clearing all entries : `clear`

Clears all entries from the address book.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

AddressBook data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

AddressBook data are saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file makes its format invalid, AddressBook will discard all data and start with an empty data file at the next run. Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the AddressBook to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</div>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous AddressBook home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Add** | `add n/NAME p/PHONE_NUMBER a/ADDRESS [t/TAG]…​` <br> e.g., `add n/James Ho p/22224444 e/jamesho@example.com a/123, Clementi Rd, 1234665 t/friend t/colleague`
**Clear** | `clear`
**Delete** | `delete INDEX`<br> e.g., `delete 3`
**Edit** | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [a/ADDRESS] [t/TAG]…​`<br> e.g.,`edit 2 n/James Lee e/jameslee@example.com`
**Find** | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`
**List** | `list`
**Help** | `help`
