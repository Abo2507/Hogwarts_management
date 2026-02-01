# 🏰 Hogwarts Management System

 Java-based REST-like API for managing Hogwarts School operations, it was created likely to our moodle, however have a lot of difference and gaps. It demonstrates advanced Object-Oriented Programming principles, JDBC database operations, and custom exception handling.

##  Project Overview

### Purpose
The Hogwarts Management System provides a robust backend API for managing:
- **Students** enrolled at Hogwarts
- **Professors** teaching various subjects
- **Houses** (Aidana, Slytherin, Ravenclaw, Hufflepuff) with point tracking
- **Courses** and student enrollments

### Key Features
- Multi-layer architecture (Controller → Service → Repository)
- JDBC-based database operations with PreparedStatements
- Custom exception hierarchy for error handling
- Comprehensive validation and business rules
- House Cup competition point tracking

---

## OOP Design Documentation

### Abstract Base Class: `Person`

**Purpose**: Serves as the foundation for all people at Hogwarts (Students and Professors)

**Key Features**:
- **Abstract Methods**: `getRole()`, `getDetails()` - must be implemented by subclasses
- **Concrete Method**: `getFullInfo()` - shared implementation across all subclasses
- **Encapsulation**: Private fields with public getters/setters

```java
public abstract class Person {
    private int id;
    private String name;
    private int age;
    private Integer houseId;
    
    public abstract String getRole();
    public abstract String getDetails();
    public String getFullInfo() { ... }
}
```

### Subclasses

#### 1. Student extends Person
- **Additional Fields**: `year`, `patronus`, `personalPoints`
- **Implements**: `Validatable`, `Scoreable`
- **Validation Rules**:
  - Age must be 11-18
  - Year must be 1-7
  - Must be assigned to a house

#### 2. Professor extends Person
- **Additional Fields**: `subject`, `salary`
- **Implements**: `Validatable`
- **Validation Rules**:
  - Age must be 21+
  - Must have a valid subject
  - Salary must be positive

### Interfaces

#### Validatable
```java
public interface Validatable {
    void validate() throws InvalidInputException;
}
```
- **Implemented by**: Student, Professor
- **Purpose**: Ensures data integrity before database operations

#### Scoreable
```java
public interface Scoreable {
    void addPoints(int points);
    void deductPoints(int points);
    int getPoints();
}
```
- **Implemented by**: Student, House
- **Purpose**: Manages point-based systems (student achievements, House Cup)

### Composition/Aggregation

**House contains Students** (Aggregation)
```java
public class House {
    private List<Student> students;
    
    public void addStudent(Student student) { ... }
    public List<Student> getStudents() { ... }
}
```

The House class demonstrates **aggregation** - houses contain students, but students can exist independently of houses.

### Polymorphism Examples

**1. Method Overriding**
```java
Person student = new Student("Harry Potter", 11, 1, 1, "Stag");
Person professor = new Professor("Snape", 35, 2, "Potions", 75000);

System.out.println(student.getRole());    // "Student"
System.out.println(professor.getRole());  // "Professor"
```

**2. Interface Polymorphism**
```java
Scoreable student = new Student(...);
Scoreable house = new House(...);

student.addPoints(10);  // Student-specific implementation
house.addPoints(50);    // House-specific implementation
```

---

##  Database Design

### Schema Overview

The database uses 4 main tables with relationships enforced through foreign keys:

```sql
houses
  ├── id (PK)
  ├── name (UNIQUE)
  ├── founder
  └── points (CHECK >= 0)

persons
  ├── id (PK)
  ├── name
  ├── age (CHECK > 0)
  ├── person_type (CHECK IN ('STUDENT', 'PROFESSOR'))
  ├── house_id (FK → houses.id)
  ├── year (for students, CHECK 1-7)
  ├── patronus (for students)
  ├── subject (for professors)
  └── salary (for professors)

courses
  ├── id (PK)
  ├── name (UNIQUE)
  ├── professor_id (FK → persons.id)
  └── credits (CHECK > 0)

enrollments
  ├── id (PK)
  ├── student_id (FK → persons.id)
  ├── course_id (FK → courses.id)
  ├── grade
  └── UNIQUE(student_id, course_id)
```

### Constraints
- **Primary Keys**: All tables have auto-incrementing primary keys
- **Foreign Keys**: Enforce referential integrity
- **Unique Constraints**: Prevent duplicate house names and course names
- **Check Constraints**: Validate age, points, year, person_type

### Sample Data
See `schema.sql` for complete sample data including:
- 4 Hogwarts houses
- 6 sample students
- 5 professors
- 5 courses
- 7 enrollment records

---

##  Controller Operations

### CRUD Endpoints Summary

#### House Operations
```java
houseService.createHouse(house)           // Create new house
houseService.getAllHouses()               // Get all houses
houseService.getHouseById(id)             // Get specific house
houseService.updateHouse(id, house)       // Update house
houseService.deleteHouse(id)              // Delete house
houseService.addPoints(id, points)        // Award points
houseService.deductPoints(id, points)     // Deduct points
```

#### Student Operations
```java
studentService.createStudent(student)          // Create new student
studentService.getAllStudents()                // Get all students
studentService.getStudentById(id)              // Get specific student
studentService.updateStudent(id, student)      // Update student
studentService.deleteStudent(id)               // Delete student
studentService.getStudentsByHouse(houseId)     // Get students by house
```

#### Professor Operations
```java
professorService.createProfessor(professor)     // Create new professor
professorService.getAllProfessors()             // Get all professors
professorService.getProfessorById(id)           // Get specific professor
professorService.updateProfessor(id, prof)      // Update professor
professorService.deleteProfessor(id)            // Delete professor
professorService.giveRaise(id, amount)          // Increase salary
```

---

##  Exception Hierarchy

```
Exception
  ├── InvalidInputException (base for validation errors)
  │     └── DuplicateResourceException (duplicate names, enrollments)
  ├── ResourceNotFoundException (entity not found)
  └── DatabaseOperationException (SQL/connection errors)
```

### Exception Usage Examples

```java
// InvalidInputException - validation failure
Student student = new Student("", 11, 1, 1, "Stag");
student.validate(); // Throws: "Student name cannot be empty"

// DuplicateResourceException - duplicate resource
House house = new House("Gryffindor", "Godric", 0);
houseService.createHouse(house); // Throws: "House with name 'Gryffindor' already exists"

// ResourceNotFoundException - entity not found
studentService.getStudentById(99999); // Throws: "Student with ID 99999 not found"

// DatabaseOperationException - SQL error
// Throws when connection fails or SQL syntax errors occur
```

---

##  Setup and Installation

### Prerequisites
- Java JDK 8 or higher
- MySQL 8.0 or higher (or SQLite as alternative)
- MySQL JDBC Driver (`mysql-connector-java-8.0.x.jar`)

### Database Setup

1. **Create Database**
```sql
CREATE DATABASE hogwarts_db;
USE hogwarts_db;
```

2. **Run Schema Script**
```bash
mysql -u root -p hogwarts_db < resources/schema.sql
```

3. **Configure Database Connection**

Edit `DatabaseConnection.java`:
```java
private static final String URL = "jdbc:mysql://localhost:3306/hogwarts_db";
private static final String USERNAME = "root";
private static final String PASSWORD = "your_password";
```

### Compilation

```bash
# From project root directory
javac -d bin -sourcepath src src/**/*.java

# Or compile with JDBC driver
javac -cp ".:lib/mysql-connector-java-8.0.33.jar" -d bin -sourcepath src src/**/*.java
```

### Running the Application

```bash
# Run from bin directory
java -cp ".:lib/mysql-connector-java-8.0.33.jar" Main

# For Windows
java -cp ".;lib/mysql-connector-java-8.0.33.jar" Main
```

---

##  Project Structure

```
hogwarts-management-system/
├── src/
│   ├── model/
│   │   ├── Person.java (abstract)
│   │   ├── Student.java
│   │   ├── Professor.java
│   │   ├── House.java
│   │   ├── Course.java
│   │   └── Enrollment.java
│   ├── interfaces/
│   │   ├── Validatable.java
│   │   └── Scoreable.java
│   ├── exception/
│   │   ├── InvalidInputException.java
│   │   ├── DuplicateResourceException.java
│   │   ├── ResourceNotFoundException.java
│   │   └── DatabaseOperationException.java
│   ├── repository/
│   │   ├── HouseRepository.java
│   │   ├── StudentRepository.java
│   │   ├── ProfessorRepository.java
│   │   └── CourseRepository.java
│   ├── service/
│   │   ├── HouseService.java
│   │   ├── StudentService.java
│   │   └── ProfessorService.java
│   ├── util/
│   │   └── DatabaseConnection.java
│   └── Main.java
├── resources/
│   └── schema.sql
├── lib/
│   └── mysql-connector-java-8.0.33.jar
├── docs/
│   ├── screenshots/
│   └── uml_diagram.png
└── README.md
```

---

## 📸 Screenshots

### 1. Successful CRUD Operations
![CRUD Operations](docs/screenshots/crud_operations.png)
*Demonstrates creating, reading, updating, and deleting students*

### 2. Polymorphism in Action
![Polymorphism](docs/screenshots/polymorphism.png)
*Shows Person array containing both Students and Professors*

### 3. Exception Handling
![Exception Handling](docs/screenshots/exceptions.png)
*Custom exceptions being caught and handled gracefully*

### 4. House Points System
![House Points](docs/screenshots/house_points.png)
*Awarding and deducting points from houses*

---

## 🎓 Reflection

### What I Learned


I learned how to use exceptions, how they work and why they needed. Additionally, I know exactly how make API. 

   - Abstract classes provide a powerful way to share common functionality while enforcing subclass implementation
   - Interfaces enable flexible polymorphism and contract-based design
   - Composition creates clear "has-a" relationships between entities
   - PreparedStatements prevent SQL injection and improve performance
   - Proper resource management (try-with-resources) prevents connection leaks
   - Connection pooling would be beneficial for production systems


### Challenges Faced
I chalenged some problems: It was hard to connect the Intel with PgAdmin, there appeared a lot of issues with password and connection.
Second issue was write and understand how each class packages and all staff connected.


**JDBC Advantages:**
- Direct database control and optimization
- No ORM abstraction overhead
- Clear understanding of SQL execution
- Fine-grained transaction management



