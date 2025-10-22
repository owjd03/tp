---
layout: page
title: User Guide
---

AddressBook Level 3 (AB3) is a **desktop app for managing contacts, optimized for use via a Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, AB3 can get your contact management tasks done faster than traditional GUI apps.

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/se-edu/addressbook-level3/releases).

1. Copy the file to the folder you want to use as the _home folder_ for your AddressBook.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar addressbook.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all contacts.

   * `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01` : Adds a contact named `John Doe` to the Address Book.

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
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Viewing help : `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Adding a person: `add`

Adds a person to the address book.

Format: `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS s/SALARY dob/DATE_OF_BIRTH ms/MARITAL_STATUS dep/NUMBER_OF_DEPENDENTS occ/OCCUPATION ip/INSURANCE_PACKAGE [t/TAG]…​`

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
A person can have any number of tags (including 0)
</div>

Examples:
* `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01 s/120000 dob/2001-01-01 ms/Married dep/2 occ/Engineer ip/Gold`
* `add n/Betsy Crowe t/friend e/betsycrowe@example.com a/Newgate Prison p/1234567 dob/1992-12-12 ms/Single occ/Criminal dep/0 ip/Undecided t/criminal`

### Listing all persons : `list`

Shows a list of all persons in the address book.

Format: `list`

### Editing a person : `edit`

Edits an existing person in the address book.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [s/SALARY] [dob/DATE_OF_BIRTH] [ms/MARITAL_STATUS] [dep/NUMBER_OF_DEPENDENTS] [occ/OCCUPATION] [ip/INSURANCE_PACKAGE] [t/TAG]…​`

* Edits the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing tags, the existing tags of the person will be removed i.e adding of tags is not cumulative.
* You can remove all the person’s tags by typing `t/` without
    specifying any tags after it.

Examples:
*  `edit 1 p/91234567 e/johndoe@example.com` Edits the phone number and email address of the 1st person to be `91234567` and `johndoe@example.com` respectively.
*  `edit 3 a/John street, block 321, #01-01 s/150000` Edits the address and salary of the 3rd person to be `John street, block 321, #01-01` and `150000` respectively.
*  `edit 4 ip/Silver dep/0` Edits the insurance package and number of dependents of the 4th person to be `Silver` and `0` respectively.
*  `edit 2 n/Betsy Crower t/` Edits the name of the 2nd person to be `Betsy Crower` and clears all existing tags.

### Locating persons by name: `find`

Finds persons whose names contain any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Only the name is searched.
* Only full words will be matched e.g. `Han` will not match `Hans`
* Persons matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

Examples:
* `find John` returns `john` and `John Doe`
* `find alex david` returns `Alex Yeoh`, `David Li`<br>
  ![result for 'find alex david'](images/findAlexDavidResult.png)

### Viewing persons details: `view`

Views persons whose names contains the given keyword or is in the given index.

Format: `view [NAME-KEYWORD]` `view [INDEX]`

* The search is case-insensitive. e.g. `alex` will match `Alex`
* The command will not work if it finds more than 1 name with given keyword.
* The order of the keywords matter. e.g. `Alex Yo` will not match `Yo Alex` 
* Keywords will match with any name that contains the keyword. e.g. `ale` will match `Alex` and `Bale`
* The index **must be a positive integer** 1, 2, 3, …​
* The index must be within the size of the current list that is being displayed.

Examples:
* `view Alex` opens up a new window containing `Alex Yeoh` details.
* `view 1` opens up a new window containing details of the first person in the list.

### Filtering persons: `filter`

Filters the list of persons to show only those who match all specified criteria.

Format: `filter [n/NAME] [a/ADDRESS] [p/PHONE] [e/EMAIL] [s/SALARY] [dob/DATE_OF_BIRTH] [ms/MARITAL_STATUS] [dep/NUMBER_OF_DEPENDENTS] [occ/OCCUPATION] [t/TAG]…​`

* At least one of the optional fields must be provided.
* The filter is case-insensitive. e.g `josh` will match `Josh`
* Keywords do not need to be complete words. The command matches any entry that **contains** the keyword. e.g. `n/jo` will match names like `John` or `Joseph`
* Keywords can be single words or phrases (e.g. `a/changi village` is allowed).
* Only persons who match **all** specified criteria will be shown.
* If you specify the same prefix multiple times, only the last one will be used for filtering.

Examples:
* `filter n/josh` displays all persons whose name contains `josh`.
* `filter n/josh a/kent ridge` displays all persons whose name contains `josh` **AND** whose address contains `kent ridge`.
* `filter n/josh n/david` is equivalent to `filter n/david`. It will display all persons whose name contains `david`.

Invalid Usages:
* `filter` (no parameters)
* `filter some random text` (preamble is not allowed)
* `filter n/` (empty description for a prefix)
* `filter n/ a/changi` (empty description for a prefix)

### Sorting persons: `sort`

Sorts the list of persons by the specified field in specified order.

Format: `sort FIELD DIRECTION`

* The `FIELD` must be one of the following: `name`, `phone`, `email`, `address`, `salary`, `dateofbirth`, `maritalstatus`, `occupation` or `dependent`
* The `DIRECTION` must be one of the following: `ascending` or `descending`. If not specified, defaults to `ascending`
* The sort is case-insensitive for text fields (e.g., `name`, `email`, `address`, `maritalstatus`, `occupation`)
* Numerical fields (`salary`, `dependent`) are sorted numerically
* Date fields (`dateofbirth`) are sorted chronologically
* The entire list will be sorted and displayed in the main window
* Invalid direction parameters are ignored and will default to ascending
* Extra parameters after the direction will be ignored

Examples:
* `sort name` sorts all persons alphabetically by name (ascending by default)
* `sort name ascending` sorts all persons alphabetically by name from A to Z
* `sort name descending` sorts all persons alphabetically by name from Z to A
* `sort salary` sorts all persons by salary from lowest to highest (ascending by default)
* `sort dependent descending` sort all persons by dependent from highest to lowest
* `sort dateofbirth` sorts all persons by date of birth from oldest to youngest

### Exporting all persons: `export`

Exports all contacts from the address book into a single CSV (Comma-Separated Values) file. This is useful for creating backups 
for external safekeeping or for importing contacts into other applications like spreadsheet software (e.g. Microsoft Excel).

Format: `export`

* After running the command, a file named  `addressbook.csv` will be created in the `data` folder.
* **Caution**: If an `addressbook.csv` file already exists in the `data` folder, it will be **overwritten** with the current data from the address book. 

### Deleting a person : `delete`

Deletes the specified person from the address book.

Format: `delete INDEX`

* Deletes the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `delete 2` deletes the 2nd person in the address book.
* `find Betsy` followed by `delete 1` deletes the 1st person in the results of the `find` command.

### Adding an insurance package: `addp`

Adds an new custom insurance package in the address book.

Format: `addp [i/PACKAGE_NAME] [d/NEW_PACKAGE_DESCRIPTION]`

* Creates a new insurance package with the specified `PACKAGE_NAME` and `PACKAGE_DESCRIPTION`
* The `i/` (package name) and `d/` (package description) fields are **both mandatory**.
* Package names are automatically formatted with proper capitalization (e.g., "premium package" becomes "Premium Package").
* Package names cannot be empty after removing whitespace
* You can set an empty description by typing `d/` followed by a space (e.g., `d/ `).
* Duplicate package names are not allowed (case-insensitive check).

Examples:
* `addp i/Premium Package d/Our top-tier insurance with comprehensive coverage and benefits.` Adds a "Premium Package" with the specified description.
* `addp i/basic plan d/Essential coverage at an affordable price point.` Adds a "Basic Plan" (auto-formatted) with the description.
* `addp i/Student Package d/` Adds a "Student Package" with an empty description.

### Editing an insurance package: `editp`

Edits an existing insurance package in the address book.

Format: `editp INDEX [i/PACKAGE_NAME] [d/NEW_PACKAGE_DESCRIPTION]`

* Finds the insurance package specified by `PACKAGE_NAME`. The search is case-insensitive.
* Updates the description of the found package to the `NEW_DESCRIPTION`.
* The `i/` (package name) and `d/` (new description) fields are **both mandatory**.
* The package name itself cannot be changed.
* You can set an empty description by typing `d/` followed by a space (e.g., `d/ `).

Examples:
* `editp n/Gold d/New description.` Edits the description of "Gold" package to be `New description.`
* `editp n/Basic Plan d/ ` Edits the description of "Basic Plan" to be empty.

### Listing existing insurance packages: `listp`

Shows all existing insurance package in the address book.

![list package message](images/listPackage.png)

Format: `listp`

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
**Add** | `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS s/SALARY dob/DATE_OF_BIRTH ms/MARITAL_STATUS dep/NUMBER_OF_DEPENDENTS occ/OCCUPATION ip/INSURANCE_PACKAGE [t/TAG]…​` <br> e.g., `add n/James Ho p/22224444 e/jamesho@example.com a/123, Clementi Rd, 1234665 s/5000 dob/1993-02-02 ms/Married dep/3 occ/Artist ip/Bronze t/friend t/colleague`
**Clear** | `clear`
**Delete** | `delete INDEX`<br> e.g., `delete 3`
**Edit** | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [s/SALARY] [dob/DATE_OF_BIRTH] [ms/MARITAL_STATUS] [dep/NUMBER_OF_DEPENDENTS] [occ/OCCUPATION] [ip/INSURANCE_PACKAGE] [t/TAG]…​`<br> e.g.,`edit 2 n/James Lee e/jameslee@example.com`
**Export** | `export`
**Filter** | `filter [n/NAME] [a/ADDRESS] [p/PHONE] [e/EMAIL] [s/SALARY] [dob/DATE_OF_BIRTH] [ms/MARITAL_STATUS] [dep/NUMBER_OF_DEPENDENTS] [occ/OCCUPATION] [t/TAG]…​`<br> e.g., `filter n/James Lee e/jameslee`
**Find** | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`
**View** | `view NAME-KEYWORD` `view INDEX` <br> e.g. `view Alex` `view 1`
**List** | `list`
**Help** | `help`
**Sort** | `sort FIELD`<br> e.g., `sort name`, `sort salary`
**List Package** | `listp`
