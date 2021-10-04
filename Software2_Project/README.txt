Scheduling Desktop Application README

**Database provided by the University appears to be no longer usable after graduation.**

Usage: 
-Use logins provided from sql Database.
-Use Customer Screen - New or Existing to create or update Customer Information.
-Use Appointment Screen - New or Existing to create or update Appointment Information.
-Use Calendar to check appointments on a weekly or monthly basis - Can Filter by Appointment Type, User Schedule, and Customer Appointments
If filtering, use reset function after each filter use.


Application should complete the following requirements: 

A. Create a log-in form that can determine the user’s location and translate log-in and error control
messages (e.g., “The username and password did not match.”) into two languages.

B. Provide the ability to enter and maintain customer records in the database, including name, address,
and phone number.

C. Provide the ability to add, update, and delete appointments, capturing the type of appointment and a link to the specific customer record in the database.

D. Provide the ability to view the calendar by month and by week.

E. Provide the ability to automatically adjust appointment times based on user time zones and daylight
saving time.

F. Write exception controls to prevent each of the following. You may use the same mechanism of
exception control more than once, but you must incorporate at least two different mechanisms of
exception control.
• scheduling an appointment outside business hours
• scheduling overlapping appointments
• entering nonexistent or invalid customer data
• entering an incorrect username and password
(used Try/Catch and Throw/Throws with custom exceptions located in Utils folder)

G. Write two or more lambda expressions to make your program more efficient, justifying the use of each lambda expression with an in-line comment.

H. Write code to provide reminders and alerts 15 minutes in advance of an appointment, based on the
user’s log-in.

I. Provide the ability to generate each of the following reports:
• number of appointment types by month
• the schedule for each consultant
• one additional report of your choice

J. Provide the ability to track user activity by recording timestamps for user log-ins in a .txt file. Each
new record should be appended to the log file, if the file already exists.
