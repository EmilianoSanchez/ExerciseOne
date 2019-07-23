package com.emiliano.exerciseone;

import com.emiliano.exerciseone.dataaccess.JDBCDao;
import com.emiliano.exerciseone.models.Student;
import com.emiliano.exerciseone.models.Subject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.util.Map;
import java.util.Set;

public class Tests {

    @Before
    public void initDatabase() {
        JDBCDao.createDatabaseSchema();
    }

    @After
    public void dropDatabase() {
        JDBCDao.dropDatabaseSchema();
    }

    @Test
    public void testGroupStudentsByFirstLetterOfLastName() {
        Student student1 = new Student(34291305, "Emiliano", "Sanchez", Date.valueOf("1999-12-15"), 5);
        JDBCDao.getInstance().saveStudent(student1);
        Student student2 = new Student(11111111, "Jose", "Mujica", Date.valueOf("1950-11-15"), 10);
        JDBCDao.getInstance().saveStudent(student2);

        Map<Character, Set<Student>> groupedStudents = JDBCDao.getInstance().groupStudentsByFirstLetterOfLastName();
        Assert.assertEquals(2,groupedStudents.size());
        Assert.assertEquals(1,groupedStudents.get('s').size());
        Assert.assertEquals(1,groupedStudents.get('m').size());
    }

    @Test
    public void testFindStudentsBySubject() {
        Student student1 = new Student(34291305, "Emiliano", "Sanchez", Date.valueOf("1999-12-15"), 5);
        JDBCDao.getInstance().saveStudent(student1);
        Student student2 = new Student(11111111, "Jose", "Mujica", Date.valueOf("1950-11-15"), 10);
        JDBCDao.getInstance().saveStudent(student2);

        Subject subject1 = new Subject("Fisica");
        JDBCDao.getInstance().saveSubject(subject1);

        JDBCDao.getInstance().enrollStudent(subject1, student1);
        JDBCDao.getInstance().enrollStudent(subject1, student2);

        Set<Student> students = JDBCDao.getInstance().findStudentsBySubject(subject1);
        Assert.assertEquals(2,students.size());
    }

    @Test
    public void testFindStudentsByAgeRange() {
        Student student1 = new Student(34291305, "Emiliano", "Sanchez", Date.valueOf("1999-12-15"), 5);
        JDBCDao.getInstance().saveStudent(student1);
        Student student2 = new Student(11111111, "Jose", "Mujica", Date.valueOf("1950-11-15"), 10);
        JDBCDao.getInstance().saveStudent(student2);

        Set<Student> students = JDBCDao.getInstance().findStudentsByAgeRange(19, 21);
        Assert.assertEquals(1,students.size());
    }

}
