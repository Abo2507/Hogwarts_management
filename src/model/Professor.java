package model;

import interfaces.Validatable;
import exception.InvalidInputException;

public class Professor extends Person implements Validatable {
    private String subject;
    private double salary;

    public Professor(String name, int age, Integer houseId, String subject, double salary) {
        super(name, age, houseId);
        this.subject =subject;
        this.salary = salary;
    }
    public Professor(int id, String name, int age, Integer houseId, String subject, double salary) {
        super(id, name, age, houseId);
        this.subject = subject;
        this.salary = salary;
    }
    @Override
    public String GetRole() {
        return "professor";
    }
    @Override
    public String getDetails() {
        return String.format("%s | Subject: %s | Salary %.2f",
                getFullInfo(), subject, salary);
    }
    @Override
    public void validate() throws InvalidInputException {
        if (getName() == null || getName().trim().isEmpty()) {
            throw new InvalidInputException("Name cannot be empty")
        }
        if (getAge() < 21) {
            throw new InvalidInputException("Professor to young");
        }
        if (salary <= 0) {
            throw new InvalidInputException("Proffessor salary must be positive");
        }
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        if (subject == null || subject.trim().isEmpty()) {
            throw new IllegalArgumentException("Subject cannot be empty");
        }
        this.subject = subject;
    }

    public double getSalary() {
        return salary;
    }
    public void setSalary(double salary) {
        if (salary<=0) {
            throw new IllegalArgumentException("Salary cannot be negative");
        }
        this.salary = salary;
    }

    @Override
    public String toString(){
        return getDetails();
    }
}


