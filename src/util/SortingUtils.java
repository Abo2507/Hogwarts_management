package util;

import model.Person;
import  model.Professor;
import model.Student;
import model.House;

import java.util.List;
import java.util.Comparator;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SortingUtils {
    public static <T extends Person> void sortByName(List<T> list) {
        list.sort((p1, p2) -> p2.getName().compareToIgnoreCase(p1.getName()));
    }

    public static <T extends Person> void sortByNameDescending(List<T> list) {
        list.sort((p1, p2) -> p2.getName().compareToIgnoreCase(p1.getName()));
    }
    //--------------------
    public static <T extends Person> void sortByage(List<T> list) {
        list.sort((Comparator.comparingInt(Person::getAge)));
    }

    public static <T extends Person> void sortByageDescending(List<T> list){
        list.sort((p1, p2) -> Integer.compare(p2.getAge(), p1.getAge()));
    }
    //-------------------(Name and Patronus)
    public static void sortstudentsBytear(List<Student> students){
        students.sort((s1, s2) -> Integer.compare(s1.getYear(), s2.getYear()));
    }

    public static void sortStudentByYearDescending(List<Student> students) {
        students.sort((s1, s2) -> Integer.compare(s2.getYear(), s1.getYear()));
    }

    public static void sortStudentByPatronus(List<Student> students){
        students.sort((s1, s2) -> {
            String p1 = s1.getPatronus() != null ? s1.getPatronus() : "";
            String p2 = s2.getPatronus() != null ? s2.getPatronus() : "";
            return p1.compareToIgnoreCase(p2);
        });
    }
    //--------------------(Professors sorting)

    public static void sortProfessorsBySalaryAscending(List<Professor> professors) {
        professors.sort(Comparator.comparingDouble(Professor ::getSalary));
    }

    public static void sortProfessorBySalaryAscending(List<Professor> professors) {
        professors.sort(Comparator.comparingDouble((Professor ::getSalary)));
    }

    public static void sortProfessorBySubject(List<Professor> professors){
        professors.sort((p1, p2) -> p1.getSubject().compareToIgnoreCase(p2.getSubject()));

    }
    //--------------------(House sorting)

    public static void sortHouseByPoints(List<House> houses){
        houses.sort((h1, h2) -> Integer.compare(h2.getPoints(), h1.getPoints()));
    }

    public static void sortHouseByAscending(List<House> houses) {
        houses.sort(Comparator.comparingInt(House::getPoints));
    }

    public static void sortHouseByName(List<House> houses) {
        houses.sort((h1, h2) -> h1.getName().compareToIgnoreCase(h2.getName()));
    }

    public static void sortHousesByFounder(List<House> houses) {
        houses.sort((h1, h2) -> h1.getFounder().compareToIgnoreCase(h2.getFounder()));
    }

    //--------------------FIlTERING

    public static <T extends Person> List<T>filterByHouse(List<T> list, int houseId) {
        return list.stream().filter(p -> p.getHouseId() != null && p.getHouseId() == houseId)
                .collect(Collectors.toList());
    }

    public static List<Student> filterByYear(List<Student> students, int year) {
        return students.stream().filter(s -> s.getYear() == year)
                .collect(Collectors.toList());
    }

    public static List<Professor> filterBySubject(List<Professor> professors, String subject) {
        return professors.stream().filter(p -> p.getSubject().equalsIgnoreCase(subject))
                .collect(Collectors.toList());
    }

    public static List<Professor> filterBySalary(List<Professor> professors, double minSalary) {
        return professors.stream()
                .filter(p -> p.getSalary() >= minSalary)
                .collect(Collectors.toList());
    }

    public static List<House> filterHousesByMinPoints(List<House> houses, int minPoints) {
        return houses.stream()
                .filter(h -> h.getPoints() >= minPoints)
                .collect(Collectors.toList());
    }

    //------------Searching

    public static <T extends Person> List<T> searchByName(List<T> list, String keyword){
        return list.stream().filter(p -> p.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    public static <T extends Person> List<T> searchByNameExact(List<T> list, String name) {
        return list.stream().filter(p -> p.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    public static List<Student> searchByPatronus(List<Student> students, String patrons) {
        return students.stream().filter(s-> s.getPatronus() != null &&
                s.getPatronus().toLowerCase().contains((patrons.toLowerCase())))
                .collect(Collectors.toList());
    }

    public static List<Professor> searchBySubject(List<Professor> professors, String keyword){
        return professors.stream().filter(p -> p.getSubject().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    public static List<House> searchHousesByName(List<House> houses, String keyword){
        return houses.stream().filter(h -> h.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    //-------------Advanced operations
    /**
     * Gets top N students by age
     */
    public static List<Student> getTopStudentsByAge(List<Student> students, int n){
        return students.stream().sorted((s1, s2) -> Double.compare(s1.getAge(), s2.getAge())).limit(n)
                .collect(Collectors.toList());
    }
    /**
     * Gets top N professors by salary
     */
    public static List<Professor> getTopProfessorsBySalary(List<Professor> professors, int n) {
        return professors.stream().sorted((p1, p2) -> Double.compare(p2.getSalary(), p1.getSalary()))
                .limit(n).collect(Collectors.toList());
    }

    /**
     * Gets top N houses by points
     */
    public static List<House> getTopHousesByPoints(List<House> houses, int n) {
        return houses.stream().sorted((h1, h2)-> Integer.compare(h2.getPoints(), h1.getPoints()))
                .limit(n)
                .collect(Collectors.toList());
    }

    /**
     * Counts persons in each house
     * Returns a formatted string with statistics
     */

    public static <T extends Person> String getHouseStatistics(List<T> persons) {
        StringBuilder stats = new StringBuilder();
        stats.append("\n--- House Statistics ---\n");

        long house1Count = persons.stream().filter(p -> p.getHouseId() != null && p.getHouseId() == 1).count();
        long house2Count = persons.stream().filter(p -> p.getHouseId() != null && p.getHouseId() == 2).count();
        long house3Count = persons.stream().filter(p -> p.getHouseId() != null && p.getHouseId() == 3).count();
        long house4Count = persons.stream().filter(p -> p.getHouseId() != null && p.getHouseId() == 4).count();
        long noHouse = persons.stream().filter(p -> p.getHouseId() == null).count();

        stats.append("House 1 (Gryffindor): ").append(house1Count).append("\n");
        stats.append("House 2 (Slytherin): ").append(house2Count).append("\n");
        stats.append("House 3 (Ravenclaw): ").append(house3Count).append("\n");
        stats.append("House 4 (Hufflepuff): ").append(house4Count).append("\n");
        stats.append("No House: ").append(noHouse).append("\n");

        return stats.toString();
    }

    /**
     * Gets average age of persons in list
     */

    public static <T extends Person> double getAverageAge(List<T> persons) {
        return persons.stream().mapToInt(Person::getAge)
                .average()
                .orElse(0.0);
    }

    /**
     * Gets total points across all houses
     */
    public static int getTotalPoints(List<House> houses) {
        return houses.stream()
                .mapToInt(House::getPoints)
                .sum();
    }
    /**
     * Custom sort with multiple criteria
     * Sort students by year first, then by name
     */
    public static void sortStudentsByYearThenName(List<Student> students) {
        students.sort(Comparator
                .comparingInt(Student::getYear)
                .thenComparing(Student::getName, String.CASE_INSENSITIVE_ORDER));
    }

    /**
     * Custom sort: Professors by subject, then by salary
     */
    public static void sortProfessorsBySubjectThenSalary(List<Professor> professors) {
        professors.sort(Comparator
                .comparing(Professor::getSubject, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(Professor::getSalary).reversed());
    }
    /**
     * Prints a sorted list with index numbers
     */
    public static <T> void printNumberedList(List<T> list, String title) {
        System.out.println("\n" + title);
        System.out.println("-".repeat(title.length()));
        for (int i = 0; i < list.size(); i++) {
            System.out.println((i + 1) + ". " + list.get(i));
        }
    }


}