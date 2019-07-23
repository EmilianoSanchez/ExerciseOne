package com.emiliano.exerciseone;

import com.emiliano.exerciseone.dataaccess.JDBCDao;
import com.emiliano.exerciseone.models.Janitor;
import com.emiliano.exerciseone.models.Student;
import com.emiliano.exerciseone.models.Subject;

import java.sql.Date;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        try {
            JDBCDao.createDatabaseSchema();

            Student student1 = new Student(34291305, "Emiliano", "Sanchez", Date.valueOf("1999-12-15"), 5);
            JDBCDao.getInstance().saveStudent(student1);
            Student student2 = new Student(11111111, "Jose", "Mujica", Date.valueOf("1950-11-15"), 10);
            JDBCDao.getInstance().saveStudent(student2);

            Subject subject1 = new Subject("Fisica");
            JDBCDao.getInstance().saveSubject(subject1);

            JDBCDao.getInstance().enrollStudent(subject1,student1);
            JDBCDao.getInstance().enrollStudent(subject1,student2);


              Janitor newJanitor= new Janitor(22222222,"Paula", "Rodriguez", Date.valueOf("1980-5-5"), Date.valueOf("2019-3-3"),"Main hall");
              JDBCDao.getInstance().saveJanitor(newJanitor);

            Map<Character, Set<Student>> groupedStudents = JDBCDao.getInstance().groupStudentsByFirstLetterOfLastName();
            for (Map.Entry<Character, Set<Student>> group : groupedStudents.entrySet()) {
                System.out.println("Char " + group.getKey() + ": " + group.getValue().size() + " students");
            }

            Set<Student> students = JDBCDao.getInstance().findStudentsBySubject(subject1);
            for (Student student : students) {
                System.out.println(student.getDni()+" "+student.getFirstName()+" "+student.getLastName()+" "+student.getBirthDate()+" "+student.getCurrentYear());
            }

            students = JDBCDao.getInstance().findStudentsByAgeRange(19,21);
            for (Student student : students) {
                System.out.println(student.getDni()+" "+student.getFirstName()+" "+student.getLastName()+" "+student.getBirthDate()+" "+student.getCurrentYear());
            }

            Student student3 = new Student(20000000, "Maria", "DB", Date.valueOf("2000-5-5"), 5);
            JDBCDao.getInstance().saveStudentWithStoredProcedure(student3);

        }catch(Exception e){
            e.printStackTrace();
        }finally {
//            JDBCDao.dropDatabaseSchema();
        }
    }
}
