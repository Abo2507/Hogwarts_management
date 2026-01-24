-- Hogwarts Management System Database Schema for PostgreSQL

-- Drop tables if they exist
DROP TABLE IF EXISTS enrollments CASCADE;
DROP TABLE IF EXISTS courses CASCADE;
DROP TABLE IF EXISTS persons CASCADE;
DROP TABLE IF EXISTS houses CASCADE;

-- Houses Table
CREATE TABLE houses (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    founder VARCHAR(100) NOT NULL,
    points INT DEFAULT 0,
    CONSTRAINT check_points CHECK (points >= 0)
);

-- Persons Table (stores both Students and Professors)
CREATE TABLE persons (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    person_type VARCHAR(20) NOT NULL,
    house_id INT,
    year INT,
    patronus VARCHAR(50),
    subject VARCHAR(100),
    salary DECIMAL(10,2),
    CONSTRAINT fk_house FOREIGN KEY (house_id) REFERENCES houses(id) ON DELETE SET NULL,
    CONSTRAINT check_age CHECK (age > 0),
    CONSTRAINT check_person_type CHECK (person_type IN ('STUDENT', 'PROFESSOR')),
    CONSTRAINT check_year CHECK (year IS NULL OR (year >= 1 AND year <= 7))
);

-- Courses Table
CREATE TABLE courses (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    professor_id INT,
    credits INT NOT NULL,
    CONSTRAINT fk_professor FOREIGN KEY (professor_id) REFERENCES persons(id) ON DELETE SET NULL,
    CONSTRAINT check_credits CHECK (credits > 0)
);

-- Enrollments Table
CREATE TABLE enrollments (
    id SERIAL PRIMARY KEY,
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    grade VARCHAR(2),
    CONSTRAINT fk_student FOREIGN KEY (student_id) REFERENCES persons(id) ON DELETE CASCADE,
    CONSTRAINT fk_course FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    CONSTRAINT unique_enrollment UNIQUE(student_id, course_id)
);

-- Sample Data Inserts

-- Insert Houses
INSERT INTO houses (name, founder, points) VALUES
('Gryffindor', 'Godric Gryffindor', 0),
('Slytherin', 'Salazar Slytherin', 0),
('Ravenclaw', 'Rowena Ravenclaw', 0),
('Hufflepuff', 'Helga Hufflepuff', 0);

-- Insert Students
INSERT INTO persons (name, age, person_type, house_id, year, patronus) VALUES
('Harry Alikhan', 11, 'STUDENT', 1, 1, 'Stag'),
('Hermione Sayan', 11, 'STUDENT', 1, 1, 'Otter'),
('Ron Ulan', 11, 'STUDENT', 1, 1, 'Jack Russell Terrier'),
('Draco Malfoy', 11, 'STUDENT', 2, 1, NULL),
('Luna Lovegood', 11, 'STUDENT', 3, 1, 'Hare'),
('Cedric Diggory', 15, 'STUDENT', 4, 5, NULL);

-- Insert Professors
INSERT INTO persons (name, age, person_type, house_id, subject, salary) VALUES
('Aidana Aidynkyzy', 35, 'PROFESSOR', 1, 'Transfiguration', 100000.00),
('Severus Snape', 35, 'PROFESSOR', 2, 'Potions', 75000.00),
('Minerva McGonagall', 70, 'PROFESSOR', 1, 'Transfiguration', 85000.00),
('Filius Flitwick', 65, 'PROFESSOR', 3, 'Charms', 70000.00),
('Pomona Sprout', 60, 'PROFESSOR', 4, 'Herbology', 65000.00);

-- Insert Courses
INSERT INTO courses (name, professor_id, credits) VALUES
('Potions', 8, 3),
('Transfiguration', 9, 4),
('Charms', 10, 3),
('Defense Against the Dark Arts', NULL, 4),
('Herbology', 11, 2);

-- Insert Enrollments
INSERT INTO enrollments (student_id, course_id, grade) VALUES
(1, 1, 'E'),
(1, 2, 'O'),
(2, 1, 'O'),
(2, 2, 'O'),
(2, 3, 'O'),
(3, 1, 'A'),
(4, 1, 'E');