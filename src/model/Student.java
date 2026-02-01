package model;

import interfaces.Validatable;
import interfaces.Scoreable;
import exception.InvalidInputException;

public class Student extends Person implements Validatable, Scoreable {
    private int year;
    private String patronus;
    private int personalPoints;

    public Student(String name, int age, Integer houseId, int year, String patronus) {
        super(name, age, houseId);
        this.year = year;
        this.patronus = patronus;
        this.personalPoints = 0;
    }

    public Student(int id, String name, int age, Integer houseId, int year, String patronus) {
        super(id, name, age, houseId);
        this.year = year;
        this.patronus = patronus;
        this.personalPoints = 0;
    }

    @Override
    public String getRole() {
        return "Student";
    }

    @Override
    public String getDetails() {
        return String.format("%s | Year: %d | Patronus: %s | Points: %d",
                getFullInfo(), year, patronus != null ? patronus : "Unknown", personalPoints);
    }

    // Implementing Validatable interface
    @Override
    public void validate() throws InvalidInputException {
        Validatable.validateNotEmpty(getName(), "Student name");
        if (getAge() < 11 || getAge() > 18) {
            throw new InvalidInputException("Student age must be between 11 and 18");
        }
        if (year < 1 || year > 7) {
            throw new InvalidInputException("Student year must be between 1 and 7");
        }
        if (getHouseId() == null) {
            throw new InvalidInputException("Student must be assigned to a house");
        }
    }

    // Implementing Scoreable interface
    @Override
    public void addPoints(int points) {
        if (points > 0) {
            this.personalPoints += points;
        }
    }

    @Override
    public void deductPoints(int points) {
        if (points > 0) {
            this.personalPoints = Math.max(0, this.personalPoints - points);
        }
    }

    @Override
    public int getPoints() {
        return personalPoints;
    }

    // Getters and Setters
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        if (year < 1 || year > 7) {
            throw new IllegalArgumentException("Year must be between 1 and 7");
        }
        this.year = year;
    }

    public String getPatronus() {
        return patronus;
    }

    public void setPatronus(String patronus) {
        this.patronus = patronus;
    }

    public void setPersonalPoints(int personalPoints) {
        this.personalPoints = personalPoints;
    }

    @Override
    public String toString() {
        return getDetails();
    }
}