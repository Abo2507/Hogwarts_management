import model.*;
import service.*;
import util.DatabaseConnection;
import exception.*;
import interfaces.*;

import java.util.List;

public class Main {

    private static final HouseService houseService = new HouseService();
    private static final StudentService studentService = new StudentService();
    private static final ProfessorService professorService = new ProfessorService();

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("🏰 HOGWARTS MANAGEMENT SYSTEM 🏰");
        System.out.println("=".repeat(60));

        if (!DatabaseConnection.testConnection()) {
            System.err.println("❌ Failed to connect to database. Exiting...");
            return;
        }

        try {
            demonstrateCRUD();
            demonstratePolymorphism();
            demonstrateInterfaces();
            demonstrateComposition();
            demonstrateExceptionHandling();
            demonstrateBusinessLogic();
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("✓ Program completed successfully!");
        System.out.println("=".repeat(60));
    }

    private static void demonstrateCRUD() throws Exception {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("1. CRUD OPERATIONS DEMONSTRATION");
        System.out.println("=".repeat(60));

        System.out.println("\n--- CREATE Operation ---");
        Student newStudent = new Student("Neville Longbottom", 11, 1, 1, "Unknown");
        newStudent = studentService.createStudent(newStudent);
        System.out.println("✓ Created: " + newStudent);

        System.out.println("\n--- READ Operation ---");
        List<Student> students = studentService.getAllStudents();
        System.out.println("Total students: " + students.size());
        students.forEach(System.out::println);

        System.out.println("\n--- UPDATE Operation ---");
        newStudent.setYear(2);
        newStudent.setPatronus("Unknown Plant");
        studentService.updateStudent(newStudent.getId(), newStudent);
        System.out.println("✓ Updated: " + newStudent);

        System.out.println("\n--- DELETE Operation ---");
        int studentIdToDelete = newStudent.getId();
        studentService.deleteStudent(studentIdToDelete);
        System.out.println("✓ Deleted student with ID: " + studentIdToDelete);
    }

    private static void demonstratePolymorphism() throws Exception {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("2. POLYMORPHISM DEMONSTRATION");
        System.out.println("=".repeat(60));

        List<Student> students = studentService.getAllStudents();
        List<Professor> professors = professorService.getAllProfessors();

        Person[] people = new Person[Math.min(3, students.size() + professors.size())];
        int index = 0;

        for (int i = 0; i < Math.min(2, students.size()) && index < people.length; i++) {
            people[index++] = students.get(i);
        }

        for (int i = 0; i < Math.min(1, professors.size()) && index < people.length; i++) {
            people[index++] = professors.get(i);
        }

        System.out.println("\nCalling polymorphic methods:");
        for (Person person : people) {
            if (person != null) {
                System.out.println("\n" + person.getFullInfo());
                System.out.println("Role: " + person.getRole());
                System.out.println("Details: " + person.getDetails());
            }
        }
    }

    private static void demonstrateInterfaces() throws Exception {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("3. INTERFACE DEMONSTRATION");
        System.out.println("=".repeat(60));

        List<Student> students = studentService.getAllStudents();
        List<House> houses = houseService.getAllHouses();

        if (!students.isEmpty() && !houses.isEmpty()) {
            Student student = students.get(0);
            House house = houses.get(0);

            System.out.println("\n--- Validatable Interface ---");
            Validatable validatable = student;
            try {
                validatable.validate();
                System.out.println("✓ Student validation passed: " + student.getName());
            } catch (InvalidInputException e) {
                System.out.println("✗ Validation failed: " + e.getMessage());
            }

            System.out.println("\n--- Scoreable Interface ---");
            Scoreable scoreableStudent = student;
            System.out.println("Student " + student.getName() + " points before: " + scoreableStudent.getPoints());
            scoreableStudent.addPoints(10);
            System.out.println("Student points after adding 10: " + scoreableStudent.getPoints());

            Scoreable scoreableHouse = house;
            System.out.println("\nHouse " + house.getName() + " points before: " + scoreableHouse.getPoints());
            scoreableHouse.addPoints(50);
            System.out.println("House points after adding 50: " + scoreableHouse.getPoints());
        }
    }

    private static void demonstrateComposition() throws Exception {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("4. COMPOSITION/AGGREGATION DEMONSTRATION");
        System.out.println("=".repeat(60));

        List<House> houses = houseService.getAllHouses();

        for (House house : houses) {
            List<Student> houseStudents = studentService.getStudentsByHouse(house.getId());

            for (Student student : houseStudents) {
                house.addStudent(student);
            }

            System.out.println("\n" + house);
            System.out.println("Students in " + house.getName() + ":");
            for (Student student : house.getStudents()) {
                System.out.println("  - " + student.getName() + " (Year " + student.getYear() + ")");
            }
        }
    }

    private static void demonstrateExceptionHandling() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("5. EXCEPTION HANDLING DEMONSTRATION");
        System.out.println("=".repeat(60));

        System.out.println("\n--- InvalidInputException ---");
        try {
            Student invalidStudent = new Student("", 11, 1, 1, "Stag");
            invalidStudent.validate();
        } catch (InvalidInputException e) {
            System.out.println("✓ Caught InvalidInputException: " + e.getMessage());
        }

        System.out.println("\n--- DuplicateResourceException ---");
        try {
            House duplicateHouse = new House("Gryffindor", "Godric", 0);
            houseService.createHouse(duplicateHouse);
        } catch (DuplicateResourceException e) {
            System.out.println("✓ Caught DuplicateResourceException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Unexpected error: " + e.getMessage());
        }

        System.out.println("\n--- ResourceNotFoundException ---");
        try {
            studentService.getStudentById(99999);
        } catch (ResourceNotFoundException e) {
            System.out.println("✓ Caught ResourceNotFoundException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Unexpected error: " + e.getMessage());
        }
    }

    private static void demonstrateBusinessLogic() throws Exception {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("6. BUSINESS LOGIC & VALIDATION DEMONSTRATION");
        System.out.println("=".repeat(60));

        System.out.println("\n--- House Points System ---");
        List<House> houses = houseService.getAllHouses();
        if (!houses.isEmpty()) {
            House gryffindor = houses.get(0);
            System.out.println("Before: " + gryffindor);

            houseService.addPoints(gryffindor.getId(), 50);
            gryffindor = houseService.getHouseById(gryffindor.getId());
            System.out.println("After adding 50 points: " + gryffindor);

            houseService.deductPoints(gryffindor.getId(), 20);
            gryffindor = houseService.getHouseById(gryffindor.getId());
            System.out.println("After deducting 20 points: " + gryffindor);
        }

        System.out.println("\n--- Age Validation ---");
        try {
            Student invalidAge = new Student("Too Young", 8, 1, 1, "None");
            invalidAge.validate();
        } catch (InvalidInputException e) {
            System.out.println("✓ Age validation working: " + e.getMessage());
        }

        System.out.println("\n--- Year Validation ---");
        try {
            Student invalidYear = new Student("Invalid Year", 11, 1, 10, "None");
            invalidYear.validate();
        } catch (InvalidInputException e) {
            System.out.println("✓ Year validation working: " + e.getMessage());
        }
    }
}
