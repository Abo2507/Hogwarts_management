package model;
import  interfaces.Scoreable;
import java.util.ArrayList;
import java.util.List;

public class House implements Scoreable {
    private int id;
    private String name;
    private String founder;
    private int points;
    private List<Student> students;

    public House(int id, String name, String founder, int points) {
        this.name = name;
        this.founder = founder;
        this.points = points;
        this.students = new ArrayList<>();
    }
    public House(int id, String founder, int points) {
        this.id = id;
        this.name = name;
        this.points = points;
        this.students = new ArrayList<>();
    }

    @Override
    public void addPoints(int points) {
        if (points > 0) {
            this.points += points;
        }
    }

    @Override
    public void deductPoints(int points){
        if (points >0) {
            this.points = Math.max(0, this.points - points);
        }
    }

    @Override
    public int getPoints() {
        return points;
    }
    public void addStudent(Student student){
        if (student != null && !students.contains(student)){
            students.add(student);
        }
    }
    public void removeStudent(Student student){
        students.remove(student);
    }
    public List<Student> getStudents() {
        return new ArrayList<>(students);
    }
    public int getStudentCount(){
        return students.size() ;
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
            throw new IllegalArgumentException("House name cannot be empty");
        }
        this.name = name;
    }
    public String getFounder() {
        return founder;
    }
    public void setFounder(String founder) {
        this.founder = founder;
    }
    public void setpoints(int points) {
        if (points <0) {
            throw new IllegalArgumentException("Points must be positive");
        }
        this.points = points;
    }
    public void setStudents(List<Student> students) {
        this.students = students != null ? new ArrayList<>(students) : new ArrayList<>();
    }
    @Override
    public String toString() {
        return String.format("House: %s | Founder: %s | Points: %d | Students: %d",
                name, founder, points, students.size());
    }
}
