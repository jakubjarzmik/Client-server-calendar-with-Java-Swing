# Client-server-calendar-with-Java-Swing
A project of a desktop calendar application written in JAVA using Swing. The application is written in a client-server architecture. It has the ability to add events and display them in chronological order.

When running the program, it checks if the server is running and depending on whether or not the user's name has been saved, the login window or the main program window will be displayed. During the login, we enter any string of characters and depending on whether or not that has already been entered, events for that user will be retrieved or a new user with the entered name will be created.

After the main window opens, we can see a menu, a list of events, a clock, a panel with today's name days and an unusual holiday - this is also the "List of events" tab. In the "Calendar" tab, we can change the months using the arrows on the UI or simply on the keyboard. We can add a new event by clicking on a given day. The "New event" window will be displayed, where we can choose to add an event lasting a selected period of time or a full-day event. We save it with the Save button or the CTRL + S combination. After saving, the list of events with the added event is displayed.

When closing the program with the "Log out" button, the local user file with the saved name is deleted and the next time the program is opened, the user will have to log in again. When closing the program normally (with the X icon), the file will be left (if it was saved) and the next time the program is opened, the user will not have to log in.
