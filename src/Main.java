import model.*;
import service.*;
import service.interfaces.*;
import controller.HogwartsController;
import util.DatabaseConnection;
import util.ReflectionUtils;
import util.SortingUtils;
import exception.*;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final IHouseService houseService = new HouseService();
    private static final IStudentService studentService = new StudentService();
    private static final IProfessorService professorService = new ProfessorService();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("============================================================");
        System.out.println("HOGWARTS MANAGEMENT SYSTEM - MILESTONE 2 (SOLID)");
        System.out.println("============================================================");

        if (!DatabaseConnection.testConnection()) {
            System.err.println("Failed to connect to database. Exiting...");
            return;
        }

        System.out.println("\nChoose mode:");
        System.out.println("1. Interactive Mode (CLI with menu)");
        System.out.println("2. Demo Mode (automated demonstrations)");
        System.out.print("Enter choice: ");

        int choice = scanner.hasNextInt() ? scanner.nextInt() : 2;

        if (choice == 1) {
            HogwartsController controller = new HogwartsController();
            controller.start();
            controller.close();
        } else {
            runDemoMode();
        }

        DatabaseConnection.closeConnection();
        scanner.close();
    }

    private static void runDemoMode() {
        try {
            demonstrateCRUD();
            demonstratePolymorphism();
            demonstrateInterfaces();
            demonstrateComposition();
            demonstrateExceptionHandling();
            demonstrateBusinessLogic();
            demonstrateAdvancedFeatures();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n============================================================");
        System.out.println("Demo completed successfully!");
        System.out.println("============================================================");
    }

    private static void demonstrateCRUD() throws Exception {
        System.out.println("\n============================================================");
        System.out.println("1. CRUD OPERATIONS DEMONSTRATION");
        System.out.println("============================================================");

        System.out.println("\n--- CREATE Operation ---");
        Student newStudent = new Student("Neville Longbottom", 11, 1, 1, "Unknown");
        newStudent = studentService.createStudent(newStudent);
        System.out.println("Created: " + newStudent);

        System.out.println("\n--- READ Operation ---");
        List<Student> students = studentService.getAllStudents();
        System.out.println("Total students: " + students.size());
        students.forEach(System.out::println);

        System.out.println("\n--- UPDATE Operation ---");
        newStudent.setYear(2);
        newStudent.setPatronus("Unknown Plant");
        studentService.updateStudent(newStudent.getId(), newStudent);
        System.out.println("Updated: " + newStudent);

        System.out.println("\n--- DELETE Operation ---");
        int studentIdToDelete = newStudent.getId();
        studentService.deleteStudent(studentIdToDelete);
        System.out.println("Deleted student with ID: " + studentIdToDelete);
    }

    private static void demonstratePolymorphism() throws Exception {
        System.out.println("\n============================================================");
        System.out.println("2. POLYMORPHISM DEMONSTRATION");
        System.out.println("============================================================");

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
        System.out.println("\n============================================================");
        System.out.println("3. INTERFACE DEMONSTRATION");
        System.out.println("============================================================");

        List<Student> students = studentService.getAllStudents();
        List<House> houses = houseService.getAllHouses();

        if (!students.isEmpty() && !houses.isEmpty()) {
            Student student = students.get(0);
            House house = houses.get(0);

            System.out.println("\n--- Validatable Interface ---");
            try {
                student.validate();
                System.out.println("Student validation passed: " + student.getName());
            } catch (InvalidInputException e) {
                System.out.println("Validation failed: " + e.getMessage());
            }

            System.out.println("\n--- Scoreable Interface ---");
            System.out.println("Student " + student.getName() + " points before: " + student.getPoints());
            student.addPoints(10);
            System.out.println("Student points after adding 10: " + student.getPoints());

            System.out.println("\nHouse " + house.getName() + " points before: " + house.getPoints());
            house.addPoints(50);
            System.out.println("House points after adding 50: " + house.getPoints());
        }
    }

    private static void demonstrateComposition() throws Exception {
        System.out.println("\n============================================================");
        System.out.println("4. COMPOSITION/AGGREGATION DEMONSTRATION");
        System.out.println("============================================================");

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
        System.out.println("\n============================================================");
        System.out.println("5. EXCEPTION HANDLING DEMONSTRATION");
        System.out.println("============================================================");

        System.out.println("\n--- InvalidInputException ---");
        try {
            Student invalidStudent = new Student("", 11, 1, 1, "Stag");
            invalidStudent.validate();
        } catch (InvalidInputException e) {
            System.out.println("Caught InvalidInputException: " + e.getMessage());
        }

        System.out.println("\n--- DuplicateResourceException ---");
        try {
            House duplicateHouse = new House("Gryffindor", "Godric", 0);
            houseService.createHouse(duplicateHouse);
        } catch (DuplicateResourceException e) {
            System.out.println("Caught DuplicateResourceException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }

        System.out.println("\n--- ResourceNotFoundException ---");
        try {
            studentService.getStudentById(99999);
        } catch (ResourceNotFoundException e) {
            System.out.println("Caught ResourceNotFoundException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    private static void demonstrateBusinessLogic() throws Exception {
        System.out.println("\n============================================================");
        System.out.println("6. BUSINESS LOGIC & VALIDATION DEMONSTRATION");
        System.out.println("============================================================");

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
            System.out.println("Age validation working: " + e.getMessage());
        }

        System.out.println("\n--- Year Validation ---");
        try {
            Student invalidYear = new Student("Invalid Year", 11, 1, 10, "None");
            invalidYear.validate();
        } catch (InvalidInputException e) {
            System.out.println("Year validation working: " + e.getMessage());
        }
    }

    private static void demonstrateAdvancedFeatures() throws Exception {
        System.out.println("\n============================================================");
        System.out.println("7. ADVANCED FEATURES DEMONSTRATION (MILESTONE 2)");
        System.out.println("============================================================");

        System.out.println("\n--- REFLECTION UTILITY ---");
        Student sampleStudent = new Student("Reflection Test", 11, 1, 1, "Phoenix");
        sampleStudent.setId(999);
        ReflectionUtils.inspectObject(sampleStudent);

        System.out.println("\n--- LAMBDA SORTING ---");
        List<Student> students = studentService.getAllStudents();

        System.out.println("\nStudents sorted by NAME (ascending):");
        SortingUtils.sortByName(students);
        students.forEach(s -> System.out.println("  - " + s.getName()));

        System.out.println("\nStudents sorted by AGE (ascending):");
        SortingUtils.sortByAge(students);
        students.forEach(s -> System.out.println("  - " + s.getName() + " (Age: " + s.getAge() + ")"));

        System.out.println("\nStudents sorted by YEAR (ascending):");
        SortingUtils.sortStudentsByYear(students);
        students.forEach(s -> System.out.println("  - " + s.getName() + " (Year: " + s.getYear() + ")"));

        System.out.println("\nStudents sorted by YEAR then NAME:");
        SortingUtils.sortStudentsByYearThenName(students);
        students.forEach(s -> System.out.println("  - Year " + s.getYear() + ": " + s.getName()));

        System.out.println("\n--- LAMBDA FILTERING ---");
        List<Student> gryffindorStudents = SortingUtils.filterByHouse(students, 1);
        System.out.println("Gryffindor students (filtered by lambda):");
        gryffindorStudents.forEach(s -> System.out.println("  - " + s.getName()));

        List<Student> firstYears = SortingUtils.filterByYear(students, 1);
        System.out.println("\nFirst year students:");
        firstYears.forEach(s -> System.out.println("  - " + s.getName()));

        List<Student> withPatronus = SortingUtils.filterStudentsWithPatronus(students);
        System.out.println("\nStudents with patronus:");
        withPatronus.forEach(s -> System.out.println("  - " + s.getName() + " (" + s.getPatronus() + ")"));

        System.out.println("\n--- LAMBDA SEARCHING ---");
        List<Student> harryStudents = SortingUtils.searchByName(students, "Harry");
        System.out.println("Students with 'Harry' in name:");
        harryStudents.forEach(s -> System.out.println("  - " + s.getName()));

        System.out.println("\n--- ADVANCED OPERATIONS ---");
        double avgAge = SortingUtils.getAverageAge(students);
        System.out.println("Average student age: " + String.format("%.2f", avgAge));

        List<Student> topStudents = SortingUtils.getTopStudentsByAge(students, 3);
        System.out.println("\nTop 3 oldest students:");
        for (int i = 0; i < topStudents.size(); i++) {
            Student s = topStudents.get(i);
            System.out.println("  " + (i+1) + ". " + s.getName() + " (Age: " + s.getAge() + ")");
        }

        System.out.println(SortingUtils.getHouseStatistics(students));

        System.out.println("\n--- INTERFACE DEFAULT METHOD ---");
        Student testStudent = new Student("Invalid Student", 5, 1, 1, "None");
        if (testStudent.isValid()) {
            System.out.println("Student is valid");
        } else {
            System.out.println("Student is NOT valid (using default isValid() method)");
        }

        Student validStudent = new Student("Valid Student", 15, 1, 3, "Stag");
        if (validStudent.isValid()) {
            System.out.println("Valid student IS valid (using default isValid() method)");
        }

        System.out.println("\n--- INTERFACE STATIC METHOD ---");
        try {
            Validatable.validateNotEmpty("", "TestField");
        } catch (InvalidInputException e) {
            System.out.println("Static validation caught: " + e.getMessage());
        }

        try {
            Validatable.validatePositive(-5, "Points");
        } catch (InvalidInputException e) {
            System.out.println("Static validation caught: " + e.getMessage());
        }

        try {
            Validatable.validateRange(15, 1, 7, "Year");
        } catch (InvalidInputException e) {
            System.out.println("Static validation caught: " + e.getMessage());
        }

        System.out.println("\n--- PROFESSOR SORTING ---");
        List<Professor> professors = professorService.getAllProfessors();

        SortingUtils.sortProfessorsBySalary(professors);
        System.out.println("Professors by salary (highest first):");
        professors.forEach(p -> System.out.println("  - " + p.getName() + ": $" + String.format("%.2f", p.getSalary())));

        System.out.println("\n--- HOUSE SORTING ---");
        List<House> houses = houseService.getAllHouses();

        SortingUtils.sortHousesByPoints(houses);
        System.out.println("House Cup Standings:");
        for (int i = 0; i < houses.size(); i++) {
            House h = houses.get(i);
            String medal = i == 0 ? "🥇" : i == 1 ? "🥈" : i == 2 ? "🥉" : "  ";
            System.out.println("  " + medal + " " + (i+1) + ". " + h.getName() + ": " + h.getPoints() + " points");
        }

        System.out.println("\n--- GENERIC REPOSITORY DEMONSTRATION ---");
        System.out.println("Using CrudRepository<Student> interface:");
        System.out.println("  ✓ create(entity) returns Student");
        System.out.println("  ✓ getAll() returns List<Student>");
        System.out.println("  ✓ getById(id) returns Student");
        System.out.println("  ✓ Generic type safety enforced at compile time!");

        System.out.println("\n--- DEPENDENCY INVERSION PRINCIPLE ---");
        System.out.println("✓ HouseService depends on CrudRepository<House> interface");
        System.out.println("✓ StudentService depends on CrudRepository<Student> interface");
        System.out.println("✓ Controller depends on IHouseService, IStudentService interfaces");
        System.out.println("✓ All dependencies point to abstractions, not concrete classes!");
    }
}