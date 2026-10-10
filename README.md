# 📱 PresentPlus - Student Attendance Management System

**PresentPlus** is an Android-based student attendance management application developed to simplify and digitize the attendance process in schools and colleges. This user-friendly app allows students to mark their daily attendance and access various academic features from a single platform.

Built as a diploma capstone project by a team of three, and awarded **2nd place at a district-level hackathon** (2024).

---

## 🔑 Key Features

- ✅ **Daily Attendance**  
  Mark daily attendance easily through the app.

- 🗓️ **Daily Routine**  
  View class schedules and daily academic routines.

- 📝 **Leave Application**  
  Apply for leaves digitally and track application status.

- 📊 **Class Test Marks**  
  Check and monitor class test scores and performance.

- 📅 **Attendance Report**  
  See the number of days you were present and analyze attendance trends.

---

## 👥 Roles

Each role logs in separately and gets its own dashboard.

| Role | What they can do |
|---|---|
| **Student** | View attendance, timetable, classroom, academic calendar and announcements; apply for leave; edit profile |
| **Teacher** | Take attendance, view attendance records, manage students, handle leave requests, view timetable, send notifications |
| **HOD** | Department-wide attendance records, student management, leave approvals, timetable and notifications |
| **Admin** | Administrator dashboard, profile and notifications |

Dashboards are organised by department (CE, CO, EE, EJ, ME, MS) and by year (first, second and third year).

---

## 🎯 Purpose

The purpose of **PresentPlus** is to enhance the academic experience for students by providing an efficient and reliable way to manage attendance and academic updates, replacing traditional paper-based methods with a digital solution.

---

## 🏗️ How It Works

```
Android app (Java)  ──HTTP (Volley / Retrofit)──►  PHP scripts  ──►  MySQL database
```

The app sends requests to PHP endpoints for login, registration, OTP verification and leave management (`login_student.php`, `login_teacher.php`, `register_student.php`, `send_otp.php`, `verify_otp.php`, `submit_leave.php`, `fetch_leave.php`, `update_leave_status.php`). The PHP server and database are hosted separately and are **not included in this repository**; during development they were exposed through an ngrok tunnel.

---

## 🛠️ Tech Stack

- **Language:** Java  
- **Platform:** Android (XML Layouts, Android SDK; min SDK 24, target SDK 35)  
- **Networking:** Volley, Retrofit, OkHttp  
- **Backend:** PHP  
- **Database:** Mysql  
- **IDE:** Android Studio

---

## 🚀 Getting Started

1. Clone the repository and open the project folder in **Android Studio**.
2. Let Gradle sync the dependencies.
3. Set up the PHP + MySQL backend on your own server, then replace the ngrok server URL with your server's address in `loginstudent.java`, `loginteacher.java`, `loginadmin.java` and `cothirdyaerstudleavemanagementsystem.java`. On the Android emulator, `http://10.0.2.2` reaches a server running on your own computer.
4. Run the app on an emulator or an Android device (Android 7.0 or newer).

---

## 📂 Project Structure

```
├── app/
│   ├── build.gradle.kts            App module settings and dependencies
│   └── src/main/
│       ├── AndroidManifest.xml     Activities and permissions
│       ├── java/com/example/capstoneproject/
│       │   ├── loginstudent / loginteacher / loginadmin    Login screens per role
│       │   ├── *Dashboard.java     Dashboards per department, year and role
│       │   ├── teacher*, hod*      Attendance, leave, timetable and student management
│       │   ├── RequestHandler.java HTTP requests to the backend
│       │   └── SharedPrefsManager  Keeps the user logged in
│       └── res/                    XML layouts, drawables and strings
├── build.gradle.kts                Project-level Gradle settings
└── settings.gradle.kts
```
