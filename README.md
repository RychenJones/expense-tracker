# Expense Tracker

Program written in Java to record expenses and view helpful analytics to assist with financial awareness and budgeting.

## Instructions for Build and Use

### Steps to build and/or run the software:

1. Install Java JDK 25 or later.
2. Open the project in VSCode or another Java-compatible IDE.
3. Compile all application source files from the project folder:

   ```bash
   javac -d out Main.java classes/*.java gui/*.java
   ```

4. Start the graphical application:

   ```bash
   java -cp out Main
   ```

### Instructions for using the software:

1. Start the program using the build and run commands above.
2. Log in using an existing account, or select **Create an account**.
3. From the expense screen, use the available buttons to:
   - Add a regular or recurring expense
   - View saved expenses in the table
   - Delete a selected expense
   - View spending analytics
   - Open account settings
   - Log out
4. When adding an expense, enter its name, price, category, date, and frequency if it is recurring.
5. Account settings allow users to change their username, change their password, or delete their account.
6. Expense data is automatically saved to a file named after the user's username. Usernames and hashed passwords are stored in `users.txt`.

## Development Environment

To recreate the development environment, you need the following software and/or libraries with the specified versions:

* Java JDK 25
* A Java-compatible IDE or terminal
* Extension Pack for Java (latest version, optional; used for the VS Code Run button)

## Useful Websites to Learn More

I found these websites useful in developing this software:

* [YouTube](https://youtu.be/vOmZ4JFhRds?si=fUtwdACVfcvLiimz)
* [W3Schools](https://www.w3schools.com/java/default.asp)
* [Wikipedia](https://en.wikipedia.org/wiki/Java_(programming_language))

## Future Work

The following items I plan to fix, improve, and/or add to this project in the future:

* [ ] More robust input validation and restrictions
* [ ] More advanced analytics
* [ ] Improve graphical UI styling and layout
