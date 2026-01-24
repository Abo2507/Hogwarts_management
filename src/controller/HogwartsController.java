package controller;

import model.*;
import service.*;
import exception.*;
import java.util.List;
import java.util.Scanner;

public class HogwartsController {

    private final HouseService houseService;
    private final StudentService studentService;
    private final ProfessorService professorService;
    private final Scanner scanner;

    public HogwartsController() {
        this.houseService = new HouseService();
        this.studentService = new StudentService();
        this.professorService = new ProfessorService();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;

        while (running) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");

            try {
                switch (choice) {
                    case 1:
                        manageHouses();
                        break;
                    case 2:
                        manageStudents();
                        break;
                    case 3:
                        manageProfessors();
                        break;
                    case 4:
                        viewHouseStandings();
                        break;
                    case 0:
                        running = false;
                        System.out.println("\nGoodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }

            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
    }

    private void displayMainMenu() {
        System.out.println("\n============================================================");
        System.out.println("HOGWARTS MANAGEMENT SYSTEM");
        System.out.println("============================================================");
        System.out.println("1. Manage Houses");
        System.out.println("2. Manage Students");
        System.out.println("3. Manage Professors");
        System.out.println("4. View House Cup Standings");
        System.out.println("0. Exit");
        System.out.println("============================================================");
    }

    private void manageHouses() throws Exception {
        System.out.println("\n--- HOUSE MANAGEMENT ---");
        System.out.println("1. View All Houses");
        System.out.println("2. View House Details");
        System.out.println("3. Create New House");
        System.out.println("4. Update House");
        System.out.println("5. Delete House");
        System.out.println("6. Add Points to House");
        System.out.println("7. Deduct Points from House");
        System.out.println("0. Back");

        int choice = getIntInput("Enter choice: ");

        switch (choice) {
            case 1: viewAllHouses(); break;
            case 2: viewHouseDetails(); break;
            case 3: createHouse(); break;
            case 4: updateHouse(); break;
            case 5: deleteHouse(); break;
            case 6: addHousePoints(); break;
            case 7: deductHousePoints(); break;
        }
    }

    private void viewAllHouses() throws Exception {
        System.out.println("\n--- ALL HOUSES ---");
        List<House> houses = houseService.getAllHouses();

        if (houses.isEmpty()) {
            System.out.println("No houses found.");
            return;
        }

        for (House house : houses) {
            System.out.println(house);
        }
    }

    private void viewHouseDetails() throws Exception {
        int id = getIntInput("Enter House ID: ");

        try {
            House house = houseService.getHouseById(id);
            List<Student> students = studentService.getStudentsByHouse(id);

            System.out.println("\n" + house);
            System.out.println("\nStudents in " + house.getName() + ":");

            if (students.isEmpty()) {
                System.out.println("  No students enrolled.");
            } else {
                for (Student student : students) {
                    System.out.println("  - " + student.getName() + " (Year " + student.getYear() + ")");
                }
            }
        } catch (ResourceNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void createHouse() throws Exception {
        System.out.println("\n--- CREATE NEW HOUSE ---");

        scanner.nextLine();
        String name = getStringInput("Enter house name: ");
        String founder = getStringInput("Enter founder name: ");
        int points = getIntInput("Enter initial points: ");

        try {
            House house = new House(name, founder, points);
            house = houseService.createHouse(house);
            System.out.println("House created successfully!");
            System.out.println(house);
        } catch (DuplicateResourceException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void updateHouse() throws Exception {
        int id = getIntInput("Enter House ID to update: ");

        try {
            House existing = houseService.getHouseById(id);
            System.out.println("Current: " + existing);

            scanner.nextLine();
            String name = getStringInput("Enter new name (or press Enter to keep): ");
            String founder = getStringInput("Enter new founder (or press Enter to keep): ");
            String pointsStr = getStringInput("Enter new points (or press Enter to keep): ");

            if (!name.isEmpty()) existing.setName(name);
            if (!founder.isEmpty()) existing.setFounder(founder);
            if (!pointsStr.isEmpty()) {
                int newPoints = Integer.parseInt(pointsStr);
                existing.setPoints(newPoints);
            }

            houseService.updateHouse(id, existing);
            System.out.println("House updated successfully!");
            System.out.println(existing);
        } catch (ResourceNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteHouse() throws Exception {
        int id = getIntInput("Enter House ID to delete: ");

        try {
            House house = houseService.getHouseById(id);
            System.out.println("Delete: " + house.getName() + "?");
            System.out.print("Type YES to confirm: ");
            scanner.nextLine();
            String confirmation = scanner.nextLine();

            if (confirmation.equals("YES")) {
                houseService.deleteHouse(id);
                System.out.println("House deleted successfully!");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } catch (ResourceNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void addHousePoints() throws Exception {
        int id = getIntInput("Enter House ID: ");
        int points = getIntInput("Enter points to add: ");

        try {
            houseService.addPoints(id, points);
            House house = houseService.getHouseById(id);
            System.out.println("Added " + points + " points to " + house.getName());
            System.out.println("New total: " + house.getPoints() + " points");
        } catch (ResourceNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deductHousePoints() throws Exception {
        int id = getIntInput("Enter House ID: ");
        int points = getIntInput("Enter points to deduct: ");

        try {
            houseService.deductPoints(id, points);
            House house = houseService.getHouseById(id);
            System.out.println("Deducted " + points + " points from " + house.getName());
            System.out.println("New total: " + house.getPoints() + " points");
        } catch (ResourceNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void manageStudents() throws Exception {
        System.out.println("\n--- STUDENT MANAGEMENT ---");
        System.out.println("1. View All Students");
        System.out.println("2. View Student Details");
        System.out.println("3. Create New Student");
        System.out.println("4. Update Student");
        System.out.println("5. Delete Student");
        System.out.println("6. View Students by House");
        System.out.println("0. Back");

        int choice = getIntInput("Enter choice: ");

        switch (choice) {
            case 1: viewAllStudents(); break;
            case 2: viewStudentDetails(); break;
            case 3: createStudent(); break;
            case 4: updateStudent(); break;
            case 5: deleteStudent(); break;
            case 6: viewStudentsByHouse(); break;
        }
    }

    private void viewAllStudents() throws Exception {
        System.out.println("\n--- ALL STUDENTS ---");
        List<Student> students = studentService.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        for (Student student : students) {
            System.out.println(student.getDetails());
        }
    }

    private void viewStudentDetails() throws Exception {
        int id = getIntInput("Enter Student ID: ");

        try {
            Student student = studentService.getStudentById(id);
            System.out.println("\n" + student.getDetails());
        } catch (ResourceNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void createStudent() throws Exception {
        System.out.println("\n--- CREATE NEW STUDENT ---");

        scanner.nextLine();
        String name = getStringInput("Enter student name: ");
        int age = getIntInput("Enter age (11-18): ");
        int houseId = getIntInput("Enter house ID: ");
        int year = getIntInput("Enter year (1-7): ");
        scanner.nextLine();
        String patronus = getStringInput("Enter patronus (or press Enter): ");

        if (patronus.isEmpty()) patronus = null;

        try {
            Student student = new Student(name, age, houseId, year, patronus);
            student = studentService.createStudent(student);
            System.out.println("Student created successfully!");
            System.out.println(student.getDetails());
        } catch (ResourceNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void updateStudent() throws Exception {
        int id = getIntInput("Enter Student ID to update: ");

        try {
            Student existing = studentService.getStudentById(id);
            System.out.println("Current: " + existing.getDetails());

            scanner.nextLine();
            String name = getStringInput("Enter new name (or press Enter): ");
            String ageStr = getStringInput("Enter new age (or press Enter): ");
            String yearStr = getStringInput("Enter new year (or press Enter): ");
            String patronus = getStringInput("Enter new patronus (or press Enter): ");

            if (!name.isEmpty()) existing.setName(name);
            if (!ageStr.isEmpty()) existing.setAge(Integer.parseInt(ageStr));
            if (!yearStr.isEmpty()) existing.setYear(Integer.parseInt(yearStr));
            if (!patronus.isEmpty()) existing.setPatronus(patronus);

            studentService.updateStudent(id, existing);
            System.out.println("Student updated successfully!");
            System.out.println(existing.getDetails());
        } catch (ResourceNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteStudent() throws Exception {
        int id = getIntInput("Enter Student ID to delete: ");

        try {
            Student student = studentService.getStudentById(id);
            System.out.println("Delete: " + student.getName() + "?");
            System.out.print("Type YES to confirm: ");
            scanner.nextLine();
            String confirmation = scanner.nextLine();

            if (confirmation.equals("YES")) {
                studentService.deleteStudent(id);
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } catch (ResourceNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewStudentsByHouse() throws Exception {
        int houseId = getIntInput("Enter House ID: ");

        try {
            House house = houseService.getHouseById(houseId);
            List<Student> students = studentService.getStudentsByHouse(houseId);

            System.out.println("\n--- Students in " + house.getName() + " ---");

            if (students.isEmpty()) {
                System.out.println("No students in this house.");
            } else {
                for (Student student : students) {
                    System.out.println(student.getDetails());
                }
            }
        } catch (ResourceNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void manageProfessors() throws Exception {
        System.out.println("\n--- PROFESSOR MANAGEMENT ---");
        System.out.println("1. View All Professors");
        System.out.println("2. View Professor Details");
        System.out.println("3. Create New Professor");
        System.out.println("4. Update Professor");
        System.out.println("5. Delete Professor");
        System.out.println("6. Give Professor Raise");
        System.out.println("0. Back");

        int choice = getIntInput("Enter choice: ");

        switch (choice) {
            case 1: viewAllProfessors(); break;
            case 2: viewProfessorDetails(); break;
            case 3: createProfessor(); break;
            case 4: updateProfessor(); break;
            case 5: deleteProfessor(); break;
            case 6: giveProfessorRaise(); break;
        }
    }

    private void viewAllProfessors() throws Exception {
        System.out.println("\n--- ALL PROFESSORS ---");
        List<Professor> professors = professorService.getAllProfessors();

        if (professors.isEmpty()) {
            System.out.println("No professors found.");
            return;
        }

        for (Professor professor : professors) {
            System.out.println(professor.getDetails());
        }
    }

    private void viewProfessorDetails() throws Exception {
        int id = getIntInput("Enter Professor ID: ");

        try {
            Professor professor = professorService.getProfessorById(id);
            System.out.println("\n" + professor.getDetails());
        } catch (ResourceNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void createProfessor() throws Exception {
        System.out.println("\n--- CREATE NEW PROFESSOR ---");

        scanner.nextLine();
        String name = getStringInput("Enter professor name: ");
        int age = getIntInput("Enter age: ");
        scanner.nextLine();
        String houseIdStr = getStringInput("Enter house ID (or press Enter): ");
        Integer houseId = houseIdStr.isEmpty() ? null : Integer.parseInt(houseIdStr);
        String subject = getStringInput("Enter subject: ");
        double salary = getDoubleInput("Enter salary: ");

        try {
            Professor professor = new Professor(name, age, houseId, subject, salary);
            professor = professorService.createProfessor(professor);
            System.out.println("Professor created successfully!");
            System.out.println(professor.getDetails());
        } catch (ResourceNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void updateProfessor() throws Exception {
        int id = getIntInput("Enter Professor ID to update: ");

        try {
            Professor existing = professorService.getProfessorById(id);
            System.out.println("Current: " + existing.getDetails());

            scanner.nextLine();
            String name = getStringInput("Enter new name (or press Enter): ");
            String ageStr = getStringInput("Enter new age (or press Enter): ");
            String subject = getStringInput("Enter new subject (or press Enter): ");
            String salaryStr = getStringInput("Enter new salary (or press Enter): ");

            if (!name.isEmpty()) existing.setName(name);
            if (!ageStr.isEmpty()) existing.setAge(Integer.parseInt(ageStr));
            if (!subject.isEmpty()) existing.setSubject(subject);
            if (!salaryStr.isEmpty()) existing.setSalary(Double.parseDouble(salaryStr));

            professorService.updateProfessor(id, existing);
            System.out.println("Professor updated successfully!");
            System.out.println(existing.getDetails());
        } catch (ResourceNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteProfessor() throws Exception {
        int id = getIntInput("Enter Professor ID to delete: ");

        try {
            Professor professor = professorService.getProfessorById(id);
            System.out.println("Delete: " + professor.getName() + "?");
            System.out.print("Type YES to confirm: ");
            scanner.nextLine();
            String confirmation = scanner.nextLine();

            if (confirmation.equals("YES")) {
                professorService.deleteProfessor(id);
                System.out.println("Professor deleted successfully!");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } catch (ResourceNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void giveProfessorRaise() throws Exception {
        int id = getIntInput("Enter Professor ID: ");
        double amount = getDoubleInput("Enter raise amount: ");

        try {
            professorService.giveRaise(id, amount);
        } catch (ResourceNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewHouseStandings() throws Exception {
        System.out.println("\n============================================================");
        System.out.println("HOUSE CUP STANDINGS");
        System.out.println("============================================================");

        List<House> houses = houseService.getAllHouses();
        houses.sort((h1, h2) -> Integer.compare(h2.getPoints(), h1.getPoints()));

        int rank = 1;
        for (House house : houses) {
            System.out.printf("%d. %-15s : %d points%n", rank++, house.getName(), house.getPoints());
        }
    }

    private int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid. " + prompt);
            scanner.next();
        }
        return scanner.nextInt();
    }

    private double getDoubleInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.print("Invalid. " + prompt);
            scanner.next();
        }
        return scanner.nextDouble();
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public void close() {
        scanner.close();
    }
}