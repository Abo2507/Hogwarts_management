package model;

public class Course {
    private int id;
    private String name;
    private Integer professorId;
    private int credits;

    public Course(String name, Integer professorId, int credits) {
        this.name = name;
        this.professorId = professorId;
        this.credits = credits;
    }

    public Course(int id, String name, Integer professorId, int credits) {
        this.id = id;
        this.name = name;
        this.credits = credits;
        this.professorId = professorId;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Course name cannot be empty");
        }
        this.name = name;
    }

    public Integer getProfessorId() {
        return professorId;
    }

    public void setProfessorId(Integer professorId) {
        this.professorId = professorId;
    }

    public int getCredits() {
        return credits;
    }
    public void setCredits(int credits) {
        if (credits <= 0) {
            throw new IllegalArgumentException("Credits must be positive");
        }
        this.credits = credits;
    }
    @Override
    public String toString() {
        return String.format("Course: %s | Credits: %d | professor Id: %s", name, credits, professorId != null ? professorId : "None");
    }
}
