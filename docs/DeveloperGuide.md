---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* {list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams are in this document `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point).

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)


The `Model` component

<div markdown="span" class="alert alert-info">
<br>
<img src="images/BetterModelClassDiagram.png" width="450" />
</div>

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

The `Patient` class

* `Patient` extends from `Person`.
* `Patient` would have additional fields `Note` and `Appointment`.
* A `Patient` can have any number of `Note` and `Appointment`.
* `Patient` can have 0 or 1 number of `Caretaker`.

The `Caretaker` class
* `Caretaker` extends from `Person`.
* `Caretaker` must have 1 `Relationship`

### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Logic.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

Similarly, how an undo operation goes through the `Model` component is shown below:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Model.png)

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* nurses who support patients outside of hospital settings
* juggle multiple patients, each with unique care needs
* rely on quick access to essential information
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: manage patient records faster than a typical mouse/GUI driven app


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​             | I want to …​                                         | So that I can…​                                         |
|----------|---------------------|------------------------------------------------------|---------------------------------------------------------|
| `* * *`  | home-visiting nurse | view the records without needing internet            | work at places with no cellular signal                  |
| `* * *`  | new user            | view all the commands of the MediSaveContact         | onboarding in the application is easy                   |
| `* * *`  | nurse               | add a new patient with personal details              | I can keep track of who I’m caring for                  |
| `* * *`  | nurse               | view a patient’s details quickly                     | I can recall important info during visits               |
| `* * *`  | nurse               | delete a patient’s record                            | I can remove patients I no longer manage                |
| `* * *`  | nurse               | list all patients                                    | I can get an overview of my caseload                    |
| `* * *`  | nurse               | add medical notes to a patient                       | I can record observations and treatment history         |
| `* * *`  | nurse               | add an appointment for a patient                     | I can remember when to visit them                       |
| `* *`    | nurse               | receive reminders for appointments                   | I won’t forget important visits                         |
| `* *`    | nurse               | filter patients by condition/notes                   | I can prioritize certain groups of patients             |
| `* *`    | nurse               | undo my last action                                  | I can recover from mistakes                             |
| `*`      | new user            | view detailed error message                          | I know if I am using the product wrongly and how to fix |
| `*`      | nurse               | update a patient's details                           | I can keep information accurate                         |
| `*`      | nurse               | search for a patient by name                         | I can find records quickly                              |
| `*`      | nurse               | view upcoming appointments                           | I can plan my schedule efficiently                      |
| `*`      | nurse               | delete an appointment                                | I can manage changes in patient schedules               |
| `*`      | nurse               | mark an appointment as completed                     | I can track which visits I've done                      |
| `*`      | nurse               | sort patients by name                                | I can find them more easily                             |
| `*`      | nurse               | add medication info to a patient                     | I can track prescriptions and dosages                   |
| `*`      | nurse               | view patients with ongoing medication                | I can check who needs regular follow-ups                |
| `*`      | nurse               | export patient records to a text file                | I can back up my data                                   |
| `*`      | nurse               | import patient records from a text file              | I can restore data if needed                            |
| `*`      | nurse               | tag patients with labels (e.g., "diabetes", "rehab") | I can organise them by health needs                     |
| `*`      | nurse               | search patients by tag                               | I can quickly find patients with similar conditions     |



### Use cases

(For all use cases below, the **System** is `MediSaveContact` and the **Actor** is the nurse user unless specified otherwise.)

---

**Use case: UC01 – Add a patient**

**Actor:** Nurse

**Preconditions:** MediSaveContact is running and the nurse has the new patient’s name, phone number, address and optional priority tag.

**Guarantees:** A new patient record is stored and displayed in the patient list.

**Main success scenario (MSS):**

1. Nurse enters `patient n/NAME p/PHONE a/ADDRESS [tag/TAG]`.
2. MediSaveContact validates every field.
3. MediSaveContact adds the patient, refreshes the patient list and shows a confirmation message.

    Use case ends.

**Extensions:**

* 1a. The nurse omits a mandatory prefix (e.g. `n/`).

      * 1a1. MediSaveContact displays the command usage message.

         Use case ends.

* 2a. The phone number contains non-digits or has an invalid length.

      * 2a1. MediSaveContact rejects the command and highlights the phone constraint.

         Use case ends.

* 2b. An existing patient has the same name and phone number.

      * 2b1. MediSaveContact shows “This patient already exists in MediSaveContact.”

         Use case ends.

* 3a. Saving to `data/addressbook.json` fails (e.g. disk is read-only).

      * 3a1. MediSaveContact informs the nurse that the data could not be saved and reverts the addition.

         Use case ends.

---

**Use case: UC02 – Assign a caretaker to a patient**

**Actor:** Nurse

**Preconditions:** The target patient exists and currently has no caretaker linked.

**Guarantees:** The patient record is updated with the caretaker details.

**Main success scenario (MSS):**

1. Nurse lists patients (optional) and identifies the desired index.
2. Nurse enters `caretaker INDEX n/NAME p/PHONE a/ADDRESS r/RELATIONSHIP`.
3. MediSaveContact links the caretaker to the patient, preserves the patient’s other data and shows a success message.

    Use case ends.

**Extensions:**

* 1a. The patient list is empty.

      Use case ends.

* 2a. `INDEX` is out of bounds of the displayed patient list.

      * 2a1. MediSaveContact shows “The patient index provided is invalid …”.

         Use case resumes at step 1.

* 2b. The person at `INDEX` is not a patient (e.g. already a caretaker entry).

      * 2b1. MediSaveContact shows “The person index provided is not a patient”.

         Use case resumes at step 1.

* 2c. The patient already has a caretaker.

      * 2c1. MediSaveContact explains that the patient already has a caretaker and aborts the command.

         Use case ends.

* 2d. The caretaker details match an existing patient record.

      * 2d1. MediSaveContact warns that the caretaker already exists as a patient and rejects the command.

         Use case ends.

---

**Use case: UC03 – Schedule an appointment for a patient**

**Actor:** Nurse

**Preconditions:** The patient exists. The proposed appointment slot is not duplicated for the patient.

**Guarantees:** The appointment is appended to the patient’s appointment list.

**Main success scenario (MSS):**

1. Nurse enters `appt INDEX d/DATE t/TIME [note/DESCRIPTION]`.
2. MediSaveContact validates the index, date, time and optional note.
3. MediSaveContact adds the appointment and shows the scheduled slot.

    Use case ends.

**Extensions:**

* 1a. `INDEX` is invalid.

      * 1a1. MediSaveContact shows “The patient index provided is invalid …”.

         Use case resumes at step 1 with a new index.

* 1b. The target person is not a patient.

      * 1b1. MediSaveContact displays “The person index provided is not a patient”.

         Use case resumes at step 1.

* 2a. The date or time format is invalid or represents a past slot.

      * 2a1. MediSaveContact explains the constraint violation.

         Use case resumes at step 1.

* 2b. The patient already has an appointment at the same date and time.

      * 2b1. MediSaveContact shows “This appointment already exists in the address book”.

         Use case ends.

---

**Use case: UC04 – Record a medical note for a patient**

**Actor:** Nurse

**Preconditions:** The patient exists and the nurse has the observation to log.

**Guarantees:** The note is appended to the patient’s note list.

**Main success scenario (MSS):**

1. Nurse enters `note INDEX note/CONTENT`.
2. MediSaveContact validates that the content is non-empty and within 200 characters.
3. MediSaveContact adds the note, shows the confirmation and displays the updated patient summary.

    Use case ends.

**Extensions:**

* 1a. `INDEX` is invalid.

      * 1a1. MediSaveContact reports the invalid patient index.

         Use case resumes at step 1.

* 1b. The target entry is not a patient.

      * 1b1. MediSaveContact shows “The person index provided is not a patient”.

         Use case resumes at step 1.

* 2a. The note is empty or consists only of whitespace.

      * 2a1. MediSaveContact shows the note constraint message and rejects the command.

         Use case ends.

* 2b. The note exceeds 200 characters.

      * 2b1. MediSaveContact shows “Note exceeds maximum length of 200 characters.”

         Use case ends.

---

**Use case: UC05 – Update caretaker details**

**Actor:** Nurse

**Preconditions:** The patient already has a caretaker linked.

**Guarantees:** The caretaker information is updated with the provided fields.

**Main success scenario (MSS):**

1. Nurse enters `editcaretaker INDEX [n/NAME] [p/PHONE] [a/ADDRESS] [r/RELATIONSHIP]`.
2. MediSaveContact verifies the index, checks that a caretaker exists and confirms that at least one field is provided.
3. MediSaveContact updates the caretaker information and displays a success message with the new details.

    Use case ends.

**Extensions:**

* 1a. `INDEX` is invalid.

      * 1a1. MediSaveContact indicates the invalid index.

         Use case resumes at step 1.

* 1b. The target entry is not a patient.

      * 1b1. MediSaveContact reports that only patients can have caretakers edited.

         Use case resumes at step 1.

* 1c. The patient has no caretaker yet.

      * 1c1. MediSaveContact informs the nurse that there is no caretaker to edit and aborts.

         Use case ends.

* 2a. No fields are supplied after the index.

      * 2a1. MediSaveContact shows “At least one field to edit must be provided.”

         Use case resumes at step 1.

* 2b. The edited details match an existing patient (name + phone).

      * 2b1. MediSaveContact warns that the caretaker already exists as a patient and rejects the command.

         Use case ends.

### Non-Functional Requirements

#### Usability
1. Core workflows (add/list/delete patient, appointment, note, caretaker) are executable with keyboard-only navigation; acceptance testers must complete the scripted scenario without touching the mouse.
2. A first-time user following the User Guide can add a patient within 3 minutes and with no more than 2 command validation errors during usability testing.

#### Performance
1. During performance testing with up to 1000 sample patients, rendering the first screen takes less than 300 ms, and subsequent filter or search commands re-render results within 150 ms.
2. From executing `java -jar medisavecontact.jar` to the command box becoming responsive, startup completes within 2.5 seconds under typical team testing conditions.

#### Reliability
1. The application autosaves after every mutating command; in a forced termination test, at most the last command’s changes may be lost.
2. During a 2-hour exploratory test session covering at least 200 mixed commands, the application must not crash or hang; any defects are logged with reproduction steps.

#### Security and Privacy
1. Patient data remains on the local machine only. No external network connections are initiated during normal operation.
2. Saved data files inherit the operating system’s default user permissions; documentation instructs nurses to secure the data directory if using shared machines.

#### Maintainability
1. `gradlew checkstyleMain checkstyleTest` runs clean with zero warnings before every release.
2. `gradlew jacocoTestReport` shows at least 80% line coverage for the `logic` and `model` packages on the main branch.
3. All public classes and methods added in each iteration include Javadoc describing purpose, parameters, and error cases.

#### Portability
1. The application runs on Windows 10+, macOS 12+, and Ubuntu 22.04+ provided Java 17 or later is installed; smoke tests for core commands are executed on each platform before release.

#### Deployment Constraints
1. The distributable artifact is a single executable JAR ≤ 100 MB that runs via `java -jar` without extra dependencies.
2. All features operate fully offline; there are no hard dependencies on external REST services or cloud storage.

#### Process
1. The team performs brownfield increments with time-boxed weekly releases; each Friday’s release candidate passes the regression suite and is archived.

#### Product Scope Notes
1. MediSaveContact maintains a single local nurse profile per installation; concurrent access to the same data file by multiple users is out of scope.

### Glossary


* **Mainstream OS**: Windows, Linux, Unix, macOS
* **JAR**: Java Archive -- A package file format used to aggregate many Java class files, associated metadata, and resources into a single file for distribution
* **GUI**: Graphical User Interface -- A form of user interface that allows users to interact with their devices through graphical icons and visual indicators
* **Patient records**: Information related to a patient (personal details, notes, appointments)
* **Medical note**: A string field with a maximum length of 200 characters, intended for storing patient specific notes such as diagnosis and medications
* **Appointment**: Patient's next appointment. Stores a DateTime object, and cannot be set to the past

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually. They cover the core MediSaveContact features and a handful of negative scenarios. After completing these scripts, continue with exploratory testing to uncover edge cases specific to your environment.

### Launch and shutdown

1. **Initial launch**
   1. Download the release JAR and place it in an empty folder (e.g. `~/medisavecontact-test`).
   2. Double-click (Windows) or run `java -jar medisavecontact.jar` (Linux/macOS).<br>
      **Expected:** The GUI opens with sample patients loaded. Status bar shows the data file path.
2. **Saving window preferences**
   1. Resize the window and drag it to a different monitor corner.
   2. Close the app using the window close button.
   3. Re-launch the app.<br>
      **Expected:** The remembered size and position are restored.
3. **Exit command**
   1. Enter `exit`.<br>
      **Expected:** Application shuts down cleanly with no lingering Java processes.

### Managing patients

1. **Adding a patient**
   1. Ensure the list view shows the full patient list via `list`.
   2. Execute `patient n/Lim Siew Mei p/93334444 a/88 Redhill Lane tag/high`.<br>
      **Expected:** Patient is appended to the list with the supplied details and `tag/HIGH`. Feedback panel confirms creation.
2. **Preventing duplicates**
   1. Repeat the exact command above.<br>
      **Expected:** Error message “This patient already exists in MediSaveContact.”
3. **Editing a patient**
   1. Run `editpatient INDEX p/98887777 tag/medium`, replacing `INDEX` with the position of the patient added earlier.<br>
      **Expected:** Phone number and tag update; other details remain unchanged.
   2. Run `editpatient INDEX` with no additional prefixes.<br>
      **Expected:** Error “At least one field to edit must be provided.”
4. **Deleting and clearing**
   1. Execute `deletepatient INDEX` on the same patient.<br>
      **Expected:** Patient disappears from the list; success message shows the removed details.
   2. Execute `deletepatient 0` and `deletepatient 999`.<br>
      **Expected:** Both fail with invalid index messages.
   3. (Optional) Run `clear` to wipe all data.<br>
      **Expected:** Patient list becomes empty. Restore data manually from a backup JSON file after this test.

### Managing caretakers

1. **Assigning a caretaker**
   1. With patients listed, pick one without a caretaker (or add a fresh patient).
   2. Execute `caretaker INDEX n/Lee Wei Jun p/90001234 a/Blk 22 Pasir Ris r/Brother`.<br>
      **Expected:** Patient card displays a caretaker section with the supplied details.
2. **Validation**
   1. Run `caretaker INDEX n/` with a blank name.<br>
      **Expected:** Validation error referencing the name constraint.
   2. Run `caretaker INDEX ...` on the same patient again.<br>
      **Expected:** Error stating the patient already has a caretaker.
3. **Editing and removing caretaker**
   1. Execute `editcaretaker INDEX p/98889999`.<br>
      **Expected:** Caretaker phone updates in the UI and success message.
   2. Execute `deletecaretaker INDEX`.<br>
      **Expected:** Caretaker section disappears. Running the command again should return an error indicating no caretaker exists.

### Managing appointments

1. **Adding an appointment**
   1. Prepare a patient (INDEX) and run `appt INDEX d/2025-12-01 t/14:30 note/Follow-up blood test`.<br>
      **Expected:** Appointment list for the patient shows the new entry. Feedback confirms addition.
2. **Rejecting invalid dates/times**
   1. Run `appt INDEX d/2020-01-01 t/09:00` (past date).<br>
      **Expected:** Error describing date/time constraint.
   2. Run `appt INDEX d/2025-13-40 t/25:61`.<br>
      **Expected:** Parsing error for invalid date/time.
3. **Editing and deleting appointments**
   1. Add a second appointment, then run `editappt INDEX ITEM_INDEX t/16:00 note/Updated timing`.<br>
      **Expected:** Specified appointment updates the time and note.
   2. Execute `deleteappt INDEX ITEM_INDEX` on the updated appointment.<br>
      **Expected:** Appointment is removed. Trying to delete the same index again should fail.

### Managing notes

1. **Adding a note**
   1. Run `note INDEX note/Patient responded well to medication.`<br>
      **Expected:** Note appears under the patient with a green success message.
2. **Character limit enforcement**
   1. Attempt `note INDEX note/` followed by 250 characters.<br>
      **Expected:** Error “Note exceeds maximum length of 200 characters.”
   2. Execute `note INDEX note/   ` (whitespace only).<br>
      **Expected:** Error about blank notes.
3. **Editing and deleting notes**
   1. Run `editnote INDEX ITEM_INDEX note/Stable vitals recorded.`<br>
      **Expected:** Note content updates in place.
   2. Run `deletenote INDEX ITEM_INDEX`.<br>
      **Expected:** Note disappears; repeated deletion of the same `ITEM_INDEX` fails gracefully.

### Searching and filtering

1. Execute `find tan` after the sample data is restored.<br>
   **Expected:** Only patients whose names contain “tan” (case-insensitive) remain in the list, with indices renumbered.
2. Run `list`.<br>
   **Expected:** Full patient list is restored.
3. Combine with other commands: run `find high`, then try `deletepatient 1`.<br>
   **Expected:** Deletes the first patient in the filtered view; confirm via `list` that the correct patient was removed.

### Data persistence and autosave

1. Add a unique patient and caretaker as above.
2. Close the app using `exit`.
3. Reopen MediSaveContact.<br>
   **Expected:** The newly added patient and caretaker are still present, demonstrating autosave.
4. Navigate to `data/medisavecontact.json` and confirm the new patient entry exists in the JSON.

### Handling invalid commands

1. Enter a random string such as `foobar`.<br>
   **Expected:** Error “Unknown command” with a hint to use `help`.
2. Enter a valid command with missing prefixes, e.g. `patient John`.<br>
   **Expected:** Error message showing the correct usage format.
3. Enter extra prefixes for commands that should ignore them, e.g. `list 123`.<br>
   **Expected:** Command succeeds and extraneous tokens are ignored.

### Recovering from corrupted storage files

1. Close the app. Open `data/medisavecontact.json` in a text editor.
2. Remove a closing brace or truncate the file intentionally.
3. Relaunch MediSaveContact.<br>
   **Expected:** Application starts with an empty data set and warns about the corrupted file.
4. Close the app, restore the JSON from a backup (or delete it to let the app regenerate sample data), then relaunch.<br>
   **Expected:** Normal startup with data restored.

### Resetting between test runs

* To return to the default sample data, delete `data/medisavecontact.json` before launching the application.
* Alternatively, keep a copy of a known-good JSON file and overwrite the data directory after each scenario.
