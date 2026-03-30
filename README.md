# Campus Event Booking System
# 1420-Course-Project-
ENGG*1420- Object Oriented Programming, W26 Course Project 

A Java-based desktop application that can manage campus events, user registrations, bookings and waitlists!

# Team Members

Navina  
Mesk  
Wania  
Aliza  
Mathieu  
Andrew  

# Requirements to Run:
- JDK 21 or later (Project utilizes JDK 25)
- Maven 3.8 or later
- IntelliJ IDEA

# How to Run:
# i) IntelliJ IDEA
  1. Clone Repository
  2. Open IntelliJ IDEA
  3. Click File|Open, and select the project folder
  4. Wait for Maven to finish downloading dependencies     automatically. 
  5. Open the Maven panel on the right-hand side, click plugins|javafx| double click javafx:run.

# ii) Terminal
  1. Clone Repository:
       git clone https://github.com/yourusername/1420-Course-Project-.git
       cd 1420-Course-Project-
  3. Run Application:
       mvn javafx:run
     NOTE: To run the program through the terminal, you must have Maven installed. 
# Phase 1 Features:
User Management: Add and view Student, Staff and Guest Users  
Event Management: Create, update, and cancel Workshop, Seminar, and Concert events.  
Booking Management: Book events for users with confirmed/waitlisted status.  
Waitlist Management: View and manage waitlists with automatic promotion when a confirmed booking is cancelled.
# Phase 2 Features:
File Persistence: System state is automatically saved to CSV on close and restored on startup.  
Search & Filter: Search events by partial, case-insensitive title and filter by event type.  
JUnit Tests: 4 automated tests covering core booking logic.  

# How to Run Tests:
  mvn test

   
