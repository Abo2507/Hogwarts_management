# Hogwarts Management System

**Assignment 4: SOLID Architecture & Advanced OOP Features (Refactoring Project)**

---

## 📌 Project Overview

This project is a **console-based Java application** that simulates a Hogwarts management system. It demonstrates **clean architecture**, **SOLID principles**, and **advanced OOP features** through a layered design consisting of **Controller, Service, Repository, and Database layers**.

The system manages:

* Houses
* Students
* Professors
* House points and relationships

The main goal of the project is **not the domain itself**, but the **architecture quality and correct application of OOP and SOLID principles**.

---

##  Architecture Overview

The application follows a **layered architecture**:

```
Controller → Service → Repository → Database
```

* **Controller layer**: Handles user interaction (CLI) and delegates logic to services
* **Service layer**: Contains business logic, validation, and rules
* **Repository layer**: Handles persistence using JDBC
* **Database layer**: PostgreSQL connection via JDBC

All dependencies are injected through constructors using **Dependency Injection (DI)**.

---

##  SOLID Principles Implementation

### 1 Single Responsibility Principle (SRP)

Each class has **one clear responsibility**:

* Controllers handle input/output only
* Services handle business logic and validation
* Repositories handle database operations
* Utility classes handle cross-cutting concerns (sorting, reflection)

---

### 2 Open/Closed Principle (OCP)

* Core logic is open for extension via interfaces
* New implementations can be added **without modifying existing code**

Example:

* `CrudRepository<T>` allows adding new repositories
* Service logic works with interfaces, not implementations

---

### 3 Liskov Substitution Principle (LSP)

* `Student` and `Professor` correctly extend the abstract class `Person`
* They can be used interchangeably via `Person` references

Demonstrated in **polymorphism demo** in `Main`.

---

### 4 Interface Segregation Principle (ISP)

* Small, focused interfaces are used:

  * `Validatable`
  * `Scoreable`
  * `Printable`

Classes implement **only what they need**, avoiding large "god interfaces".

---

### 5 Dependency Inversion Principle (DIP)

* High-level modules depend on **interfaces**, not concrete classes
* `HogwartsController` depends on `IHouseService`, `IStudentService`, `IProfessorService`
* Services depend on repository interfaces
* Concrete implementations are created only in `Main` (Composition Root)

This removes tight coupling and enables easy testing and extension.

---

##  Core OOP Features

### - Encapsulation

* Fields are private
* Access controlled through getters/setters

### - Inheritance

* `Person` is an abstract base class
* `Student` and `Professor` extend `Person`

### - Polymorphism

* Methods like `getRole()` and `getDetails()` are overridden
* Demonstrated via `Person[]` in demo mode

### - Abstraction

* Abstract class `Person`
* Interfaces for services and repositories

---

##  Advanced Java Features (Milestone 2)

###  Generics

* `CrudRepository<T>` provides type-safe CRUD operations
* Generic utility methods for sorting and filtering

---

###  Lambda Expressions & Streams

Used extensively in `SortingUtils`:

* Sorting
* Filtering
* Searching
* Aggregations (average age, statistics)

Example:

```java
students.stream()
    .filter(s -> s.getYear() == 1)
    .collect(Collectors.toList());
```

---

### Reflection

* `ReflectionUtils` inspects objects at runtime
* Prints class name, fields, methods, and modifiers

Used to demonstrate **runtime type inspection (RTTI)**.

---

###  Default & Static Methods in Interfaces

Interface `Validatable` includes:

* `default boolean isValid()`
* `static validateNotEmpty()`
* `static validatePositive()`
* `static validateRange()`

This demonstrates modern Java interface capabilities.

---

##  Exception Handling

A custom exception hierarchy is implemented:

* `InvalidInputException`
* `DuplicateResourceException`
* `ResourceNotFoundException`
* `DatabaseOperationException`

Exceptions are:

* Thrown in **service layer**
* Handled gracefully in controller or demo mode

---

##  Composition & Aggregation

* A `House` aggregates multiple `Student` objects
* Relationship maintained via `houseId` (foreign key)
* Demonstrated dynamically in demo mode

---

##  Database

* PostgreSQL database
* JDBC used for all persistence operations
* Tables:

  * `houses`
  * `students`
  * `professors`

Connection handled via `DatabaseConnection` utility class.

---

##  How to Run

1. Configure PostgreSQL database
2. Update credentials in `DatabaseConnection`
3. Run `Main.java`
4. Choose:

   * **Interactive Mode** (CLI menu)
   * **Demo Mode** (automatic demonstrations)

---

##  Demonstrations Included

The Demo Mode showcases:

* CRUD operations
* Polymorphism
* Interface usage
* Composition & aggregation
* Exception handling
* Business rules
* Lambdas & streams
* Reflection

---

##  Possible Improvements (Future Work)

* Add DTO layer for API/UI separation
* Add unit tests (JUnit)
* Introduce REST API (Spring Boot)
* Improve logging framework

---

