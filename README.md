# Student Management System
### Java Swing + JDBC + MySQL 5.x | VS Code Setup Guide

---

## Project Structure

```
StudentManagementSystem/
├── lib/
│   └── mysql-connector-java-5.x.jar
├── src/
│   └── com/sms/
│       ├── Main.java
│       ├── db/
│       │   └── DBConnection.java
│       ├── model/
│       │   └── Student.java
│       ├── dao/
│       │   ├── StudentDAO.java
│       │   └── AdminDAO.java
│       ├── ui/
│       │   ├── LoginFrame.java
│       │   ├── MainFrame.java
│       │   └── AddEditStudentDialog.java
│       └── util/
│           ├── UITheme.java
│           └── Validator.java
├── student_db.sql
├── README.md   
└── .gitignore 

## Step-by-Step Setup

### 1. Install Required Tools

| Tool | Download |
|------|----------|
| JDK 8+ | https://www.oracle.com/java/technologies/downloads/ |
| VS Code | https://code.visualstudio.com/ |
| MySQL 5.7+ | https://dev.mysql.com/downloads/mysql/ |
| MySQL Workbench | https://dev.mysql.com/downloads/workbench/ |

---

### 2. Install VS Code Extension

Open VS Code → `Ctrl+Shift+X` → search:

**Extension Pack for Java** (by Microsoft) → Install

This includes the Java compiler, debugger, and IntelliSense.

---

### 3. Set Up the MySQL Database

1. Open **MySQL Workbench**
2. Connect to your local server
3. Go to **File → Open SQL Script** → select `student_db.sql`
4. Press **Ctrl+Shift+Enter** to run
5. You should see: `Database setup complete!` and `10` students

---

### 4. Add the MySQL Connector JAR

1. Download from: https://dev.mysql.com/downloads/connector/j/5.1.html
   - Choose: **Platform Independent (Architecture Independent), ZIP Archive**
2. Extract the zip — find the file named:
   `mysql-connector-java-5.1.49.jar` (version may vary)
3. Copy that `.jar` file into the **`lib/`** folder of this project:
   ```
   SMS_VSCode/
   └── lib/
       └── mysql-connector-java-5.1.49.jar   ← paste here
   ```
4. The `.vscode/settings.json` already tells VS Code to use `lib/**/*.jar`

---

### 5. Update Your MySQL Password

Open `src/com/sms/db/DBConnection.java` and change:

```java
private static final String PASSWORD = "root";   // ← your MySQL password
```

---

### 6. Run the Application

**Option A — Click Run (recommended):**
1. Open `src/com/sms/Main.java` in VS Code
2. A **▶ Run** button appears above the `main` method
3. Click it → Login window opens

**Option B — Terminal (from project root):**

**macOS / Linux:**
```bash
# Compile
javac -cp "lib/*" -d bin $(find src -name "*.java")

# Run
java -cp "bin:lib/*" com.sms.Main
```

**Windows (Command Prompt):**
```cmd
# Compile
for /r src %f in (*.java) do javac -cp "lib\*" -d bin "%f"

# Run
java -cp "bin;lib\*" com.sms.Main
```

---

## Login Credentials

| Field    | Value      |
|----------|------------|
| Username | `admin`    |
| Password | `admin123` |

---

## Features & How to Use

| Feature | How |
|---------|-----|
| **View all students** | Click "View Students" in sidebar |
| **Add student** | Click "Add Student" or "+ Add New" button |
| **Edit student** | Go to View Students → select a row → click "Edit Selected" |
| **Delete student** | Go to View Students → select a row → click "Delete Selected" |
| **Search** | Click "Search" in sidebar → type name/roll no/dept → press Enter |
| **Dashboard** | Click "Dashboard" — shows count, stats, and recent records |
| **Logout** | Click "Logout" in sidebar or top-right |

---

## Common Errors & Fixes

| Error Message | Cause | Fix |
|---|---|---|
| `ClassNotFoundException: com.mysql.jdbc.Driver` | JAR missing | Add JAR to `lib/` folder (Step 4) |
| `Access denied for user 'root'@'localhost'` | Wrong password | Update `DBConnection.java` (Step 5) |
| `Unknown database 'student_db'` | SQL not run | Run `student_db.sql` in Workbench (Step 3) |
| `Communications link failure` | MySQL not running | Start MySQL service from MySQL Workbench or Services |
| `▶ Run button not visible` | Extension not installed | Install "Extension Pack for Java" (Step 2) |
| `cannot find symbol` compile error | Package mismatch | Make sure `.vscode/settings.json` has `"java.project.sourcePaths": ["src"]` |

---

## Package Explanations

| Package | File | Purpose |
|---------|------|---------|
| `com.sms` | `Main.java` | Entry point — starts the app |
| `com.sms.db` | `DBConnection.java` | Opens and manages MySQL connection via JDBC |
| `com.sms.model` | `Student.java` | Plain Java object holding student data |
| `com.sms.dao` | `StudentDAO.java` | INSERT / SELECT / UPDATE / DELETE via PreparedStatement |
| `com.sms.dao` | `AdminDAO.java` | Login check — SELECT from admin table |
| `com.sms.ui` | `LoginFrame.java` | Login window (shown first) |
| `com.sms.ui` | `MainFrame.java` | Main app: sidebar + dashboard + view + search |
| `com.sms.ui` | `AddEditStudentDialog.java` | Popup dialog for adding and editing students |
| `com.sms.util` | `UITheme.java` | Colors, fonts, styled button/field/table factories |
| `com.sms.util` | `Validator.java` | Validates name, email, phone, year before DB call |
