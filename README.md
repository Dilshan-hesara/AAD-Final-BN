# Hospital Management System (HMS)

A comprehensive, multi-user Hospital Management System designed to streamline hospital operations, from patient management to administrative oversight. This project features a robust role-based access control system, serving Super Admins, Branch Admins, Receptionists, and Online Users (Patients) through a modern, decoupled architecture.

The system is built with a **Spring Boot REST API** backend and a separate **HTML, Bootstrap, and JavaScript (AJAX/Fetch)** frontend, ensuring a modular and scalable solution.

---

## ✨ Key Features

The system is divided into four distinct user portals, each with a tailored set of features.

### 👨‍💼 Super Admin Portal
The Super Admin has the highest level of authority with a global overview of the entire hospital network.
- **Global Dashboard:** View aggregate statistics for all branches, including total patients, active doctors, and monthly revenue.
- **Branch Management:** Full CRUD functionality (Create, Read, Update, Delete) for all hospital branches, including the ability to activate and deactivate them.
- **Staff Management:**
    -   **Branch Admins:** Add, edit, delete, and manage the accounts of all Branch Admins and assign them to branches.
    -   **Receptionists:** View a complete list of all receptionists across the network with advanced filtering and search.
- **System-Wide Reporting:** Access comprehensive, paginated reports for all patients, doctors, and appointments with advanced filtering capabilities.
- **Internal Messaging:** A dedicated chat system to communicate with all staff members across all branches.

### 🏥 Branch Admin Portal
Each Branch Admin manages their specific hospital branch.
- **Advanced Analytics Dashboard:** A detailed dashboard showing branch-specific statistics like today's earnings, new patient registrations, and appointment status breakdowns.
- **Staff Management (for their branch):**
    -   **Doctors:** Full CRUD functionality, search with pagination, and the ability to activate/deactivate doctors.
    -   **Receptionists:** Full management including **adding new receptionists**, **editing their details**, searching with pagination, and activating/deactivating their accounts.
- **Patient Management:** A complete module to manage patient records with search, pagination, and the ability to add and update patient details.
- **Appointment Management:** A detailed report of all branch appointments with advanced filters and pagination. Includes features to print bills and update appointment statuses interactively.
- **Internal Messaging:** A real-time chat system to communicate with the Super Admin and other staff within their branch, complete with notifications.

### 📋 Receptionist Portal
The Receptionist handles the day-to-day operations of a branch.
- **Dashboard:** A simplified dashboard showing today's appointments and key statistics for their branch.
- **Patient Management:** Ability to search for, add, and edit patient records within their branch.
- **Appointment Booking:** A dedicated interface to book appointments for patients, with searchable dropdowns for doctors in their branch.
- **Internal Messaging:** A chat system to communicate with their Branch Admin and other receptionists in the same branch.

### 💻 Online User (Patient) Portal
A public-facing portal for patients to interact with the hospital.
- **Secure Registration & Login:** Users can create an account with a standard form or log in seamlessly using **Google OAuth2**.
- **Password Reset:** A full-featured "Forgot Password" system with OTP verification via email.
- **Personal Dashboard:** A personalized dashboard showing the user's upcoming appointment and a history of recent activity, with options to view bills and reports.
- **Appointment Booking:** An easy-to-use form to book appointments at any branch, including a simulated payment flow.
- **My Profile:** Users can update their personal details, change their password, and upload a profile picture.
- **AI Health Assistant:** An integrated AI chat assistant (powered by Google Gemini) to provide preliminary, non-diagnostic health advice.

---

## 🛠️ Technology Stack

| Area          | Technology                                                                          |
|---------------|-------------------------------------------------------------------------------------|
| **Backend** | Spring Boot, Spring Security (JWT & OAuth2), Spring Data JPA, Hibernate, MySQL, Maven |
| **Frontend** | HTML5, CSS3, Bootstrap 5, JavaScript (AJAX/Fetch), jQuery, Chart.js                 |
| **External APIs** | OPEN Ai API (for AI Assistant)                                                |

---

## 🚀 Getting Started

### Prerequisites
-   Java JDK 17 or newer
-   Apache Maven
-   MySQL Server
-   An IDE (like IntelliJ IDEA or VS Code)
-   A modern web browser with a live server extension

### Backend Setup (BN)
1.  Clone the backend repository.
2.  Open the project in your IDE.
3.  Configure `src/main/resources/application.properties` with your MySQL, email, and Google/OPEN AI API credentials.
4.  Run the application. The server will start on `http://localhost:8080`.

### Frontend Setup (FN)
1.  Clone the frontend repository.
2.  Open the project folder in your IDE or code editor.
3.  Run the `index.html` file using a live server.
4.  The frontend will be accessible, typically at `http://localhost:63342` or a similar address.

---

## 📸 Screenshots



Super Admin - Branch Management


Branch Admin - Advanced Dashboard


Online User - Appointment Booking
