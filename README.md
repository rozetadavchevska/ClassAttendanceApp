# Class Attendance Android Application


## Overview
Class Attendance is a mobile application designed to simplify the often complex task of tracking and managing class attendance. 
The user-friendly interface provided to both professors and students, offers a range of features tailored to their needs.

In addition to these user roles, an administrative function is incorporated to create profiles based on user types, but they do not retain ongoing control or access to these profiles. 
This strategic implementation serves as a robust safeguard, ensuring that only authorized students and professors can participate in classes, preventing students from other faculties from registering and attending classes they are not enrolled in. 
This approach not only enhances security but also contributes to a more efficient and focused learning environment.

The application is developed using Java as the primary programming language. 
The integration of Firebase Database ensures a seamless and dynamic data storage solution, enabling efficient retrieval and management of attendance records. 
Additionally, Firebase Authentication plays a pivotal role in securing user interactions within the app, providing a reliable and scalable authentication mechanism. 


## Features

- **Professor:**
  - Add courses they teach
  - Add classes for courses they teach
  - Mark class attendance
  - View class attendance history
  - View class reviews
    
- **Student:**
  - Enroll in courses
  - View upcoming and past classes
  - Receive notifications for upcoming classes
  - Confirm their class attendance
  - Anonymously leave a review for the classes they attended

- **Admin:**
  - Creates user accounts based on user role (Professor or student)
    
## Installation

### Clone the Repository
```bash
git clone https://github.com/rozetadavchevska/ClassAttendanceApp.git
```

### Open in Android Studio
Navigate to the project's root directory:
```bash
cd ClassAttendanceApp
```

### Build the Application
```bash
# For Linux/macOS
./gradlew build
```
```bash
# For Windows
gradlew build
```

### Run the Application
Once the build is successful, run the application on your emulator or physical device.
