---
layout: page
title: User Guide
---

MediSaveContact is designed for nurses and healthcare workers who provide care outside traditional hospital settings. 
The application focuses on quick data entry and retrieval through a command-line interface, making it faster to manage 
patient information during busy schedules.

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/AY2526S1-CS2103T-F14b-2/tp/tags).

1. Copy the file to the folder you want to use as the _home folder_ for your AddressBook.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar addressbook.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png) <br>

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

    * `help` : Shows all commands available in the application.

    * `list` : Lists all contacts.

   * `add n/John Tan p/91234567 a/Blk 123 Clementi Ave 3 [t/High]` : Adds a contact named `John Tan` to the Address Book.

   * `appointment 1 d/15-11-2025 t/20:03` : Adds a new appointment to patient 1 scheduled at `15-11-2025 20:03`.
   
   * `note 3 note/Patient is stable` : Add notes `Patient is stable` to patient 3's record.
   
   * `find John` : Finds and displays patients that contains `John` in their names.
   
   * `view 1` : Views a patient's details
   
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
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/High` or as `n/John Doe`.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `list 123`, it will be interpreted as `list`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Viewing help : `help`

Shows all commands available in the application.

<img src="images/helpMessage.png" alt="help message" width="50%">

Format: `help`

### Listing all patients : `list`

Shows a list of all patients in MediSaveContact, even if it is empty.

#### Command Format: 
```
list
```

#### Outputs

- Success: "Listed all persons"
- Failure: List command would never result in failure

### Adding a patient: `add`

Adds a patient to the address book.

#### Command Format 
`add n/NAME p/PHONE_NUMBER a/ADDRESS [tag/TAG]`

#### Example Commands
`add n/John Tan p/91234567 a/Blk 123 Clementi Ave 3 t/high  `

`add n/Amy Lee p/82345678 a/456 Bedok North Street 2`

Note: A patient can have 0 or 1 tag
#### Parameters & Validation Rules 
| Parameter      | Validation Rules                                                                                                                                                                                                                                            | Rationale                                                 | 
|----------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------|
| NAME           | - Cannot be blank<br/> - Must be alphanumeric (letters, numbers, spaces) <br/> -Hyphens (-) and apostrophes (') allowed (e.g., O’Connor, Mary-Anne) <br/> - Leading/trailing spaces are trimmed <br/> - Case-insensitive for equality (John Doe = john doe) | Ensures consistent, clean names; avoids malformed entries |
| PHONE          | - Digits only <br/>  At least 3 digits (configurable, usually 3–15) <br/> No spaces, +, -, or brackets                                                                                                                                                      | Keeps phone numbers simple and valid                      |
| ADDRESS        | - Cannot be blank <br/> - Any characters allowed (letters, numbers, punctuation) <br/> - Leading/trailing spaces trimmed                                                                                                                                    | Allows flexibility for varied address formats             | 
| TAG (optional) | - Optional <br/>- Only allowed values: "high", "medium", "low" (case-insensitive) <br/>- Multiple tags allowed, each must be one of the three values                                                                                                        | Tags are lightweight labels for filtering/searching       | 

#### Validation Rules 
| Parameter      | Validation Rule      | Error Message if Invalid                                                                                        | 
|----------------|----------------------|-----------------------------------------------------------------------------------------------------------------| 
| NAME           | Cannot be blank      | Name cannot be blank.                                                                                           | 
| NAME           | Invalid characters   | Name contains invalid characters. Only letters, numbers, spaces, hyphens (-), and apostrophes (') are allowed.  |                   
| PHONE          | Cannot be blank      | Phone number cannot be blank                                                                                    |
| PHONE          | Non-digit characters | Phone number must contain digits only                                                                           |
| PHONE          | Too short / too long | Phone number must be between 3 and 15 digits                                                                    |
| ADDRESS        | Cannot be blank      | Address cannot be blank.                                                                                        |
| TAG (optional) | Invalid character s  | Invalid value: "Invalid tag. Only 'high', 'medium', or 'low' are allowed"                                       |

#### Outputs 

- Success: 
  - In GUI: New Patient appears in Patient list 
  - In Command Feedback Box: **New patient added: John Tan, Phone: 91234567, Address: Blk 123 Clementi Ave 3, Tag: High Priority**     
- Failure: Error Messages above

#### Duplicate handling
- Patients are duplicates if both name and phone number match (case-insensitive). 
- If attempting to add a duplicate patient: **This person already exists in the address book**



### Adding an appointment: `appointment`

Schedule an appointment for a patient.

Format: `appointment INDEX d/DATE t/TIME`

Examples:
* `appointment 1 d/15-11-2025 t/20:03`

#### Parameters & Validation Rules

| Parameter | Validation Rules | Error Message if Invalid | Rationale |
| --- | --- | --- | --- |
| INDEX | Must exist in patient list<br>Must be a positive integer | "Index number does not exist in address book list!"<br>"Index number must be a positive integer!" | Ensures correct patient reference |
| DATE | Must follow DD-MM-YYYY format<br>Must be today or later | "Invalid date. Must follow DD-MM-YYYY format!"<br>"Appointment cannot be set in the past!" | Prevents scheduling in the past |
| TIME | Must follow HH:MM 24-hour format<br>If the appointment is today, time must be later than the current time | "Invalid time. Must follow HH:MM 24-hour format!"<br>"Appointment cannot be set in the past!" | Prevents scheduling in the past |

#### Outputs

- Success: Appointment Created at 15-11-2025 20:03!

- Failure: Error messages above

#### Duplicate handling: 

- Not Applicable

#### Possible errors:

- Missing date/time
- Missing index
- More than one date/time or index
- Wong argument order

### Editing a person : `edit`

Edits an existing person in the address book.

Format: `edit INDEX [n/NAME] [p/PHONE] [a/ADDRESS] [t/TAG]`

* Edits the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing tags, the existing tags of the person will be removed i.e adding of tags is not cumulative.
* You can remove all the person’s tags by typing `t/` without
    specifying any tags after it.

Examples:
*  `edit 1 p/91234567` Edits the phone number of the 1st person to be `91234567`.
*  `edit 2 n/Betsy Crower t/` Edits the name of the 2nd person to be `Betsy Crower` and clears all existing tags.

### Adding a note `note`

Adds a note to a patient's record for tracking medical observations, treatment updates, or other important information.

Format: `note INDEX note/NOTES`

* Adds a note to the patient at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* Notes can only be added to patients, not regular contacts.
* Each note is appended to the patient's existing notes list, allowing you to build a comprehensive medical history.
* Notes must not be empty or contain only whitespace.
* Notes are limited to a maximum of 200 characters to keep entries concise and manageable.
* Notes can contain any characters including special symbols, numbers, and punctuation.

Examples:
* `note 1 note/Patient shows improved blood sugar levels today.`
* `note 3 note/Allergic reaction to penicillin - avoid in future treatments.`
* `note 2 note/Follow-up appointment scheduled for next week.`

**Possible Error Messages:**
* `The person at index [INDEX] is not a patient. Notes can only be added to patients.` - Occurs when trying to add a note to a regular contact instead of a patient.
* `Note cannot be empty.` - Occurs when the note field is left blank or contains only spaces.
* `Note exceeds maximum length of 200 characters.` - Occurs when the note is too long.
* `Invalid command format!` - Occurs when the command format is incorrect (e.g., missing `note/` prefix).

<div markdown="span" class="alert alert-warning">:exclamation: **Important:**
Notes can only be added to patients. If you try to add a note to a regular contact, you will receive an error message.
</div>

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
Notes are appended to existing notes, so you can add multiple notes to build a complete medical history for each patient.
</div>

### Adding a note `view`

View a patient’s record.

Format: `view INDEX`

Examples:
* `view 1`


### Locating persons by name: `find`

Finds persons whose names contain any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Only the name is searched.
* Only full words will be matched e.g. `Han` will not match `Hans`
* Keywords only accept alphabets.
* Persons matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

Examples:
* `find John` returns `john` and `John Doe`
* `find alex david` returns `Alex Yeoh`, `David Li`<br>
  ![result for 'find alex david'](images/findAlexDavidResult.png)


### Deleting a person : `delete`

Deletes the specified person from the address book.

Format: `delete INDEX`

* Deletes the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `delete 2` deletes the 2nd person in the address book.
* `find Betsy` followed by `delete 1` deletes the 1st person in the results of the `find` command.

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
**Add** | `add n/NAME p/PHONE_NUMBER a/ADDRESS [t/TAG]` <br> e.g., `add n/James Ho p/22224444 a/123, Clementi Rd, 1234665 t/High`
**Clear** | `clear`
**Delete** | `delete INDEX`<br> e.g., `delete 3`
**Edit** | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [a/ADDRESS] [t/TAG]`<br> e.g.,`edit 2 n/James Lee p/99998888`
**Find** | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`
**Appointment** | `appointment INDEX d/DATE t/TIME`<br> e.g., `appointment 1 d/15-11-2025 t/20:03`
**Note** | `note INDEX note/NOTES`<br> e.g., `note 1 note/Patient shows improved blood sugar levels today.`
**View** | `view INDEX`<br> e.g., `view 1`
**List** | `list`
**Help** | `help`
