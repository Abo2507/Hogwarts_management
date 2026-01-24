package model;

public abstract class Person {
    private int id;
    private String name;
    private int age;
    private Integer houseId;

    public Person(String name, int age, Integer houseId) {
        this.name = name;
        this.age = age;
        this.houseId = houseId;
    }

    public Person(int id, String name, int age, Integer houseId) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.houseId = houseId;
    }

    public abstract String getRole();
    public abstract String getDetails();

    public String getFullInfo() {
        return String.format("ID: %d | Name: %s | Age: %d | Role: %s",
                id, name, age, getRole());
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
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age <= 0) {
            throw new IllegalArgumentException("Age must be positive");
        }
        this.age = age;
    }

    public Integer getHouseId() {
        return houseId;
    }

    public void setHouseId(Integer houseId) {
        this.houseId = houseId;
    }

    @Override
    public String toString() {
        return getFullInfo();
    }
}