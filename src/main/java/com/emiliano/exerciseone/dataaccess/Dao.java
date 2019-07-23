package com.emiliano.exerciseone.dataaccess;

import com.emiliano.exerciseone.models.Janitor;
import com.emiliano.exerciseone.models.Student;
import com.emiliano.exerciseone.models.Subject;

import java.util.Map;
import java.util.Set;

public interface Dao {
    public boolean saveStudent(Student student);

    public Map<Character, Set<Student>> groupStudentsByFirstLetterOfLastName();

    public Set<Student> findStudentsByFirstLetterOfLastName(char firstLetter);

    public boolean saveSubject(Subject subject);

    public boolean enrollStudent(Subject subject, Student student);

    public Set<Student> findStudentsBySubject(Subject subject);

    public boolean saveJanitor(Janitor janitor);

    public Set<Student> findStudentsByAgeRange(int from, int to);
}
