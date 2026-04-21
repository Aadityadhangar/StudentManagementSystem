package com.sms.model;

/**
 * Student.java — Model / POJO class
 * ─────────────────────────────────────────────────────────────
 * Represents one student record from the `students` DB table.
 * Used to pass data between DAO ↔ UI layers.
 * ─────────────────────────────────────────────────────────────
 */
public class Student {

    // Fields match the `students` table columns
    private int    studentId;
    private String firstName;
    private String lastName;
    private String rollNumber;
    private String department;
    private int    year;
    private String email;
    private String phone;
    private String dob;
    private String address;

    // ── Constructors ──────────────────────────────────────────

    /** Empty constructor */
    public Student() {}

    /**
     * Constructor WITHOUT studentId — used when INSERTING a new student.
     * The DB auto-generates the ID.
     */
    public Student(String firstName, String lastName, String rollNumber,
                   String department, int year, String email,
                   String phone, String dob, String address) {
        this.firstName  = firstName;
        this.lastName   = lastName;
        this.rollNumber = rollNumber;
        this.department = department;
        this.year       = year;
        this.email      = email;
        this.phone      = phone;
        this.dob        = dob;
        this.address    = address;
    }

    /**
     * Full constructor WITH studentId — used when READING from DB.
     */
    public Student(int studentId, String firstName, String lastName, String rollNumber,
                   String department, int year, String email,
                   String phone, String dob, String address) {
        this.studentId  = studentId;
        this.firstName  = firstName;
        this.lastName   = lastName;
        this.rollNumber = rollNumber;
        this.department = department;
        this.year       = year;
        this.email      = email;
        this.phone      = phone;
        this.dob        = dob;
        this.address    = address;
    }

    // ── Getters ───────────────────────────────────────────────
    public int    getStudentId()  { return studentId;  }
    public String getFirstName()  { return firstName;  }
    public String getLastName()   { return lastName;   }
    public String getRollNumber() { return rollNumber; }
    public String getDepartment() { return department; }
    public int    getYear()       { return year;       }
    public String getEmail()      { return email;      }
    public String getPhone()      { return phone;      }
    public String getDob()        { return dob;        }
    public String getAddress()    { return address;    }

    // ── Setters ───────────────────────────────────────────────
    public void setStudentId (int studentId)      { this.studentId  = studentId;  }
    public void setFirstName (String firstName)   { this.firstName  = firstName;  }
    public void setLastName  (String lastName)    { this.lastName   = lastName;   }
    public void setRollNumber(String rollNumber)  { this.rollNumber = rollNumber; }
    public void setDepartment(String department)  { this.department = department; }
    public void setYear      (int year)           { this.year       = year;       }
    public void setEmail     (String email)       { this.email      = email;      }
    public void setPhone     (String phone)       { this.phone      = phone;      }
    public void setDob       (String dob)         { this.dob        = dob;        }
    public void setAddress   (String address)     { this.address    = address;    }

    /** Used in JComboBox dropdowns if needed */
    @Override
    public String toString() {
        return rollNumber + " — " + firstName + " " + lastName;
    }
}
