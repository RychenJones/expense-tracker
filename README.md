# Expense Tracker

Program written in Java to record expenses and view helpful analytics to assist with financial awareness and budgeting.

## Instructions for Build and Use

### Steps to build and/or run the software:

1. Install a Java JDK
2. Open project in VSCode or another IDE
3. Run `Main.java` by entering these commands in the terminal:

   ```bash
   javac -d out Main.java classes/*.java
   java -cp out Main
   ```

### Instructions for using the software:

1. Start the program using the build and run commands above.
2. From the main menu, choose one of the following:
   - `1` to log in
   - `2` to create a new account
   - `3` to exit
3. After logging in, choose an option:
   - `1` to record a single or recurring expense
   - `2` to view saved expenses
   - `3` to view spending analytics
   - `4` to delete an expense
   - `5` to quit
4. When recording an expense, enter its name, price, category, date, and frequency if it is recurring.
5. Expense data is automatically saved to a file named after the user's username. Usernames and hashed passwords are stored in `users.txt`.

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

* [ ] Add logout feature
* [ ] More robust input validation and restrictions
* [ ] More advanced analytics
* [ ] Graphical UI
