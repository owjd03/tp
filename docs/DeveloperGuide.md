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
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

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

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


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

### Adding more fields for Add/Edit

### Filter

### Sort

### Add/Edit/Delete/List Insurance Package

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

* works as a Financial Advisor professionally
* has a need to manage a significant number of clients and contacts
* wants to store clients' profile to get applicable financial plans for them
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: help Financial Advisors to manage many contacts faster than a typical mouse/GUI driven app


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                                         | I want to …​                                                                                       | So that I can…​                                                                                               |
|----------|-----------------------------------------------------------------|----------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------|
| `* * *`  | Financial Advisor                                               | store detailed client profiles (including salary, age, what insurance package to offer them, etc.) | quickly assess which insurance plans are best suited for which client                                         |
| `* * *`  | Financial Advisor                                               | edit my client's data                                                                              | update their details if there is any change                                                                   |
| `* * *`  | Financial Advisor                                               | archive or delete old clients                                                                      | remove entries that I no longer need                                                                          |
| `* * *`  | Financial Advisor                                               | assess the application offline                                                                     | work without internet connection.                                                                             |
| `* * *`  | Financial Advisor                                               | add a new client profile with key financial details                                                | provide personalized recommendations later.                                                                   |
| `* * *`  | Financial Advisor                                               | safely store and access sensitive customer data since it is a remote application                   | not worry about data beaches                                                                                  |
| `* * *`  | Financial Advisor                                               | quickly search for clients based on their names                                                    | get their data quickly                                                                                        |
| `* * *`  | Financial Advisor                                               | export all of my client data to a CSV file                                                         | create backups of my client information for safekeeping                                                       |
| `* * *`  | Financial Advisor                                               | quickly view a client by typing their name                                                         | access their information faster than scrolling through a long list                                            |
| `* * *`  | Financial Advisor                                               | sort my list based on category (name, income level, age, etc.)                                     | quickly identify clients at the bottom or top of a category                                                   |
| `* * *`  | Financial Advisor                                               | filter my client list based on specific criteria (age range, income level)                         | quickly assess which insurance plans are best suited for which client                                         |
| `* *`    | Financial Advisor                                               | tag them with custom labels                                                                        | include information that is unique to each client and does not fall into any of the predetermined categories. |
| `* *`    | Financial Advisor that is new to the system                     | see sample client profiles when I open the app                                                     | quickly understand how information is structured and stored                                                   |
| `* *`    | Financial Advisor ready to use the system in my day to day work | delete all samples and test data/start a clean account for myself                                  | input real client information.                                                                                |
| `* *`    | Financial Advisor                                               | add custom insurance packages to the system with custom name and descriptions                      | offer personalized insurance options that aren't in the default catalog                                       |
| `* *`    | Financial Advisor                                               | edit the descriptions of existing insurance packages                                               | update package information with the latest details and provide accurate details to clients                    |
| `* *`    | Financial Advisor                                               | view all available insurance packages in the system                                                | see what options I can offer to my clients                                                                    |
| `* *`    | Financial Advisor                                               | remove outdated insurance packages from the system                                                 | adapt to new insurance products while removing outdated insurance packages                                    |
| `*`      | Financial Advisor with many package options                     | prevent duplicate package names in the system                                                      | avoid confusion when selecting packages for clients                                                           |
| `*`      | Financial Advisor managing a clean system                       | be prevented from deleting essential default packages                                              | ensure the catalog always has basic package options available                                                 |

### Use cases

(For all use cases below, the **System** is the `ClientCore` and the **Actor** is the `FA`, unless specified otherwise)

**Use case: U1. Delete a client**

**Precondition:** FA is logged into the app

**MSS**

1.  FA requests to list the clients
2.  ClientCore shows a list of clients
3.  FA requests to delete a specific client in the list
4.  ClientCore deletes the client

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. ClientCore shows an error message.

      Use case resumes at step 2.


**Use case: U2. Find a client**

**Precondition:** FA is logged into the app

**MSS**

1.  FA requests to list clients
2.  ClientCore shows a list of clients
3.  FA requests to find a list of clients containing the input keyword.
4.  ClientCore finds and shows a new list of clients with matching names.

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given name is invalid.

    * 3a1. ClientCore shows an error message.

      Use case resumes at step 2.
  
* 3b. The given name does not match with anyone.

    * 3b1. ClientCore shows a message stating no one is found.
      
      Use case ends.


**Use case: U3. View a client's full detail**

**Precondition:** FA is logged into the app

**MSS**

1.  FA requests to list clients
2.  ClientCore shows a list of clients
3.  FA requests to view a specific client in the list.
4.  ClientCore shows the client's full detail in a new window.

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. ClientCore shows an error message.

      Use case resumes at step 2.

* 3b. The given name is invalid.

    * 3b1. ClientCore shows an error message.

      Use case resumes at step 2.

* 3c. The given name matches with 2 or more client

    * 3c1. ClientCore shows an error message and 
           prompt FA to view based on index instead of name.

      Use case resumes at step 2.


**Use case: U4. Filter list of clients**

**Precondition:** FA is logged into the app

**MSS**

1.  FA requests to list clients
2.  ClientCore shows a list of clients
3.  FA requests to filter the list based on input field.
4.  ClientCore shows the filtered list.

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given input field does not exist.

    * 3a1. ClientCore shows an error message.

      Use case resumes at step 2.

* 3b. The given input contains duplicate field.

    * 3b1. ClientCore shows an error message.

      Use case resumes at step 2.
  
* 3c. The given input does not match with anyone.

    * 3c1. ClientCore shows a message stating no one is found.
  
      Use case ends.

**Use case: U5. Tag a client into groups**

**Precondition:** 

1. FA is logged into the app
2. Target client is in the list

**MSS**

1.  FA requests to list clients
2.  ClientCore shows a list of clients
3.  FA requests to tag a specific client in the list.
4.  ClientCore tag the client and group them with similar tags.

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given tag input is invalid

    * 3a1. ClientCore shows an error message.

      Use case resumes at step 2.

* 4a. The given tag is a newly created tag
    
    * 4a1. ClientCore tells FA that the tag is new and the
	   tagged client is the first one in the group.

      Use case ends.

**Use case: U6. Sort a client based on a category**

**Precondition:**

1. FA is logged into the app
2. The list contains 2 or more client

**MSS**

1.  FA requests to list clients
2.  ClientCore shows a list of clients
3.  FA requests to sort clients based on existing category, stating ascending/descending
4.  ClientCore sorts the list based on FA's input.

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given input is not based on existing category.
      
    * 3a1. ClientCore shows an error message that says category doesn't exist.

      Use case resumes at step 2.

* 3b. FA does not specify ascending or descending.
  
    * 3b1. ClientCore automatically infer it as ascending and sort ascendingly.

      Use case ends.

**Use case: U7. Adding an insurance package**

**Precondition:** FA is logged into the app

**MSS**

1. FA requests to add a new package with a name and description.
2. ClientCore adds the new package and update the list of packages.

    Use case ends.

**Extensions**

* 1a. The name matches with an existing package.

    * 1a1. ClientCore shows an error message.
  
      Use case resumes at step 1.

* 1b. FA does not want to include a description.

    * 1b1. ClientCore accepts the package and leave the description blank.

      Use case ends.

**Use case: U8. Deleting an insurance package**

**Precondition:**

1. FA is logged into the app.
2. The package list contains 1 or more package.

**MSS**

1. FA requests to list packages.
2. ClientCore shows the list of packages.
3. FA requests to delete one of the packages.
4. ClientCore deletes the package and removes it from the list of packages.

   Use case ends.

**Extensions**

* 3a. FA tries to delete a default package.

    * 3a1. ClientCore cancels the action and state that default packages cannot be deleted.

      Use case resumes at step 2.

* 3b. The given name does not match with any packages.

    * 3b1. ClientCore shows an error message.
  
      Use case resumes at step 2.


*{More to be added}*

### Non-Functional Requirements

#### Platform Compatibility
1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.

#### Performance and Responsiveness 
2.  Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.

#### Scalability 
4. The system should be designed to scale to larger datasets (e.g., ≥10,000 contacts) with minimal architectural changes.

#### Usability 
5. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) 
should be able to accomplish most of the tasks faster using commands than using the mouse.
6. Commands should be concise, memorable, and consistent.
7. Error messages should be clear, instructive, and suggest corrective action.

#### Reliability and Fault Tolerance 
8. The application should not crash in the event of invalid inputs. 
9. Must handle unexpected shutdowns gracefully without loss of stored data. 

#### Security and Privacy 
10. All sensitive data (salary, occupation, contact details, etc) must be stored securely.
11. Must comply with existing data privacy regulations. 

#### Maintainability 
12. Code must be modular and follow standard Java conventions.
13. Build should be automated via Gradle. 
14. Unit and integration tests should provide ample coverage of the codebase. 

#### Extensibility 
15. The system should support the addition of new commands with minimal changes to existing code.

#### Portability 
16. The application should be distributable as a single JAR file and run consistently across
supported platforms without requiring external dependencies. 

#### Documentation 
17. User guide should explain all commands clearly with examples.
18. Developer Guide should include instructions for setting up the development environment.
19. Public classes and methods in the codebase should include Javadoc header comments that describe its purpose, 
parameters, return values, and any exceptions thrown. These comments should follow the official JavaDoc conventions.

*{More to be added}*

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Command Line Interface (CLI)**: A text-based interface where the user types commands instead of 
graphical elements.
* **Private contact detail**: A contact detail that is not meant to be shared with others.
* **Financial Advisor (FA)**: A professional user of the system whose job is to manage client 
relationships and recommend suitable financial products or services.
* **Client**: An individual whose personal and financial details (e.g., age, salary, policies, risk profile) 
are stored in the system. Clients are the primary focus of the Financial Advisor’s work and the main type of
contact being managed. 
* **Client Profile**: A structured record containing personal and financial information of a client (e.g. name,
age, salary, relationship status). 
* **Tag**: A label applied to a client profile to group clients meaningfully.
* **Command**: A typed instruction given to the system to perform an action (e.g. list, add, find, delete).
* **Archiving**: The act of marking a client profile as inactive (not deleted) for long-term storage,
    so that it no longer shows up in day-to-day operations but can be retrieved if needed.
* **Offline**: The ability to use the system without an internet connection. 

*{More to be added}*

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
