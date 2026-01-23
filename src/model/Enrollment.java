package model;

public class Enrollment {
    private int id;
    private int studentId;
    private int courseId;
    private String grade;

    public Enrollment(int studentId, int courseId, String grade) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.grade = grade;
    }

    public Enrollment(int id, int studentId, int courseId, String grade) {
        this.id = id;
        this.courseId = courseId;
        this.studentId = studentId;
        this.grade = grade;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getStudentId() {
        return studentId;
    }
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    public int getCourseId() {
        return courseId;
    }
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
    public String getGrade() {
        return grade;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }
    @Override
    public String toString() {
        return String.format("Enrollment ID; %d | Student: %d | Course: %d | Grade: %s",
                id, studentId, courseId, grade != null ? grade : "N/A");
    }
}
