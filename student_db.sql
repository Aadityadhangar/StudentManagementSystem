-- ================================================================
--  Student Management System — MySQL Database Setup Script
--  HOW TO RUN:
--    Option A: Open MySQL Workbench → paste this → Ctrl+Shift+Enter
--    Option B: Terminal: mysql -u root -p < student_db.sql
-- ================================================================

-- Step 1: Create database
CREATE DATABASE IF NOT EXISTS student_db;
USE student_db;

-- ----------------------------------------------------------------
-- Step 2: Admin table (for login authentication)
-- ----------------------------------------------------------------
CREATE TABLE IF NOT EXISTS admin (
    admin_id   INT          AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(100) NOT NULL,
    email      VARCHAR(100),
    created_at TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

-- Default login: username=admin, password=admin123
INSERT INTO admin (username, password, email)
VALUES ('admin', 'admin123', 'admin@sitrc.edu')
ON DUPLICATE KEY UPDATE admin_id = admin_id;

-- ----------------------------------------------------------------
-- Step 3: Students table (main data table)
-- ----------------------------------------------------------------
CREATE TABLE IF NOT EXISTS students (
    student_id  INT          AUTO_INCREMENT PRIMARY KEY,
    first_name  VARCHAR(50)  NOT NULL,
    last_name   VARCHAR(50)  NOT NULL,
    roll_number VARCHAR(30)  NOT NULL UNIQUE,
    department  VARCHAR(100) NOT NULL,
    year        INT          NOT NULL,
    email       VARCHAR(100),
    phone       VARCHAR(15),
    dob         VARCHAR(20),
    address     VARCHAR(255),
    created_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

-- ----------------------------------------------------------------
-- Step 4: Sample student data (10 records)
-- ----------------------------------------------------------------
INSERT INTO students
    (first_name, last_name, roll_number, department, year, email, phone, dob, address)
VALUES
    ('Aakash',   'Patil',    'AIDS2024001', 'AI & Data Science',       2, 'aakash@sitrc.edu',   '9876543210', '15-08-2004', 'Nashik, MH'),
    ('Priya',    'Sharma',   'AIDS2024002', 'AI & Data Science',       2, 'priya@sitrc.edu',    '9765432109', '22-03-2004', 'Pune, MH'),
    ('Rahul',    'Desai',    'COMP2024015', 'Computer Engineering',    3, 'rahul@sitrc.edu',    '9654321098', '10-11-2003', 'Mumbai, MH'),
    ('Sneha',    'Joshi',    'MECH2023022', 'Mechanical Engineering',  3, 'sneha@sitrc.edu',    '9543210987', '05-06-2003', 'Nagpur, MH'),
    ('Vikram',   'More',     'ELEX2024009', 'Electronics & Telecom',   2, 'vikram@sitrc.edu',   '9432109876', '18-01-2004', 'Aurangabad, MH'),
    ('Meena',    'Kulkarni', 'CIVIL2023031','Civil Engineering',       3, 'meena@sitrc.edu',    '9321098765', '30-09-2003', 'Nashik, MH'),
    ('Rohan',    'Gaikwad',  'AIDS2024018', 'AI & Data Science',       2, 'rohan@sitrc.edu',    '9210987654', '12-07-2004', 'Pune, MH'),
    ('Anita',    'Bhosale',  'COMP2023044', 'Computer Engineering',    3, 'anita@sitrc.edu',    '9109876543', '25-02-2003', 'Mumbai, MH'),
    ('Kiran',    'Shinde',   'MECH2024007', 'Mechanical Engineering',  2, 'kiran@sitrc.edu',    '9098765432', '08-04-2004', 'Nashik, MH'),
    ('Pooja',    'Nair',     'ELEX2023055', 'Electronics & Telecom',   3, 'pooja@sitrc.edu',    '8987654321', '20-12-2003', 'Pune, MH');

-- ----------------------------------------------------------------
-- Step 5: Verify
-- ----------------------------------------------------------------
SELECT 'Database setup complete!' AS Status;
SELECT COUNT(*) AS total_students FROM students;
SELECT username, email FROM admin;
