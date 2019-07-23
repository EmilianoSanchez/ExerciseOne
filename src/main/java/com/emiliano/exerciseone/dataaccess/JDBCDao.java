package com.emiliano.exerciseone.dataaccess;

import com.emiliano.exerciseone.models.Janitor;
import com.emiliano.exerciseone.models.Student;
import com.emiliano.exerciseone.models.Subject;
import com.mysql.jdbc.Driver;

import java.sql.*;
import java.util.*;

public class JDBCDao implements Dao {

    private static JDBCDao ourInstance = new JDBCDao();

    public static JDBCDao getInstance() {
        return ourInstance;
    }

    private static final String URL = "jdbc:mysql://localhost:3306/exerciseone?createDatabaseIfNotExist=true";
    private static final String USER = "root";
    private static final String PASS = "";

    private Connection connection;

    private JDBCDao() {
        try {
            DriverManager.registerDriver(new Driver());
            connection = DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException ex) {
            throw new RuntimeException("Error connecting to the database", ex);
        }
    }

    public static void createDatabaseSchema() throws SQLException{

        String createPerson = "CREATE TABLE person(" + "dni INT NOT NULL," + "firstName VARCHAR(50) NOT NULL,"
                + "lastName VARCHAR(50) NOT NULL," + "birthDate DATE NOT NULL,"
                + "PRIMARY KEY (dni))";
        String createStudent = "CREATE TABLE student(" + "dni INT NOT NULL," + "currentYear INT NOT NULL ,"
                + "PRIMARY KEY (dni)," + "FOREIGN KEY (dni) REFERENCES person(dni))";
        String createEmployee = "CREATE TABLE employee(" + "dni INT NOT NULL," + "startingDate DATE NOT NULL,"
                + "PRIMARY KEY (dni)," + "FOREIGN KEY (dni) REFERENCES person(dni))";
        String createPrincipal = "CREATE TABLE principal(" + "dni INT NOT NULL," + "schoolInCharge VARCHAR(50) NOT NULL,"
                + "PRIMARY KEY (dni)," + "FOREIGN KEY (dni) REFERENCES employee(dni))";
        String createJenitor = "CREATE TABLE janitor(" + "dni INT NOT NULL," + "workingArea VARCHAR(50) NOT NULL,"
                + "PRIMARY KEY (dni)," + "FOREIGN KEY (dni) REFERENCES employee(dni))";

        String createSubject = "CREATE TABLE subject(" + " name VARCHAR(50) NOT NULL, PRIMARY KEY (name))";
        String createEnrollment = "CREATE TABLE enrollment(" + " subject_name VARCHAR(50) NOT NULL, " + "student_dni INT NOT NULL,"
                + "PRIMARY KEY (subject_name, student_dni),"
                + "FOREIGN KEY (subject_name) REFERENCES subject(name), "
                + "FOREIGN KEY (student_dni) REFERENCES student(dni))";

        Connection connection = getInstance().connection;

        connection.prepareStatement(createPerson).execute();
        connection.prepareStatement(createStudent).execute();
        connection.prepareStatement(createEmployee).execute();
        connection.prepareStatement(createPrincipal).execute();
        connection.prepareStatement(createJenitor).execute();
        connection.prepareStatement(createSubject).execute();
        connection.prepareStatement(createEnrollment).execute();

        String createSaveStudentProcedure = "DROP PROCEDURE IF EXISTS save_student;\n"+
            "DELIMITER $$\n" +
            "CREATE PROCEDURE save_student (IN dni INT,IN firstName VARCHAR(50),IN lastName VARCHAR(50),IN birthDate DATE,IN currentYear INT)\n" +
            "BEGIN\n" +
            "    insert into person values (dni, firstName, lastName, birthDate);\n" +
            "    insert into student values (dni , currentYear);\n" +
            "END\n" +
            "$$\n" +
            "DELIMITER ;";
        connection.prepareStatement(createSaveStudentProcedure).execute();
    }

    public static void dropDatabaseSchema(){
        Connection connection = getInstance().connection;
        try {
            connection.prepareStatement("DROP TABLE enrollment").execute();
        }catch(SQLException e){
            e.printStackTrace();
        }
        try {
            connection.prepareStatement("DROP TABLE subject").execute();
        }catch(SQLException e){
            e.printStackTrace();
        }
        try {
            connection.prepareStatement("DROP TABLE janitor").execute();
        }catch(SQLException e){
            e.printStackTrace();
        }
        try {
            connection.prepareStatement("DROP TABLE principal").execute();
        }catch(SQLException e){
            e.printStackTrace();
        }
        try {
            connection.prepareStatement("DROP TABLE employee").execute();
        }catch(SQLException e){
            e.printStackTrace();
        }
        try {
            connection.prepareStatement("DROP TABLE student").execute();
        }catch(SQLException e){
            e.printStackTrace();
        }
        try {
            connection.prepareStatement("DROP TABLE person").execute();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public boolean saveStudent(Student student){
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO person VALUES (?, ?, ?, ?)");
            ps.setInt(1, student.getDni());
            ps.setString(2, student.getFirstName());
            ps.setString(3, student.getLastName());
            ps.setDate(4, student.getBirthDate());
            int i = ps.executeUpdate();
            ps.close();
            if(i == 1) {
                ps = connection.prepareStatement("INSERT INTO student VALUES (?, ?)");
                ps.setInt(1, student.getDni());
                ps.setInt(2, student.getCurrentYear());
                i = ps.executeUpdate();
                ps.close();
                if(i == 1) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean saveStudentWithStoredProcedure(Student student){
        try {
            PreparedStatement ps = connection.prepareStatement("CALL save_student( ?, ?, ?, ?, ?)");
            ps.setInt(1, student.getDni());
            ps.setString(2, student.getFirstName());
            ps.setString(3, student.getLastName());
            ps.setDate(4, student.getBirthDate());
            ps.setInt(5, student.getCurrentYear());
            int i = ps.executeUpdate();
            ps.close();
            if(i == 1) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public Map<Character, Set<Student>> groupStudentsByFirstLetterOfLastName(){
        Map<Character, Set<Student>> result= new HashMap<Character, Set<Student>>();
        for(char firstLetter = 'a'; firstLetter <='z'; firstLetter++ ) {
            Set<Student> group=findStudentsByFirstLetterOfLastName(firstLetter);
            if(group.size()>0)
                result.put(firstLetter,group);
        }
        return result;
    }

    public Set<Student> findStudentsByFirstLetterOfLastName(char firstLetter){
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM student s INNER JOIN person p ON s.dni = p.dni " +
                            "WHERE p.lastName LIKE ?");
            ps.setString(1, firstLetter+"%");
            ResultSet rs = ps.executeQuery();
            Set<Student> students = extractStudentsFromResultSet(rs);
            ps.close();
            return students;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return new HashSet<Student>();
        }
    }

    public boolean saveSubject(Subject subject) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO subject VALUES (?)");
            ps.setString(1, subject.getName());
            int i = ps.executeUpdate();
            ps.close();
            if(i == 1) {
                    return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean enrollStudent(Subject subject, Student student) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO enrollment VALUES (?,?)");
            ps.setString(1, subject.getName());
            ps.setInt(2, student.getDni());
            int i = ps.executeUpdate();
            ps.close();
            if(i == 1) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public Set<Student> findStudentsBySubject(Subject subject) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT p.*, s.currentYear FROM student s " +
                            "INNER JOIN person p ON s.dni = p.dni " +
                            "INNER JOIN enrollment e ON s.dni = e.student_dni " +
                            "WHERE e.subject_name = ?");
            ps.setString(1, subject.getName());
            ResultSet rs = ps.executeQuery();
            Set<Student> students = extractStudentsFromResultSet(rs);
            ps.close();
            return students;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return new HashSet<Student>();
        }
    }

    public boolean saveJanitor(Janitor janitor) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO person VALUES (?, ?, ?, ?)");
            ps.setInt(1, janitor.getDni());
            ps.setString(2, janitor.getFirstName());
            ps.setString(3, janitor.getLastName());
            ps.setDate(4, janitor.getBirthDate());
            int i = ps.executeUpdate();
            ps.close();
            if(i == 1) {
                ps = connection.prepareStatement("INSERT INTO employee VALUES (?, ?)");
                ps.setInt(1, janitor.getDni());
                ps.setDate(2, janitor.getStartingDate());
                i = ps.executeUpdate();
                ps.close();
                if(i == 1) {
                    ps = connection.prepareStatement("INSERT INTO janitor VALUES (?, ?)");
                    ps.setInt(1, janitor.getDni());
                    ps.setString(2, janitor.getWorkingArea());
                    i = ps.executeUpdate();
                    ps.close();
                    if(i == 1) {
                        return true;
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public Set<Student> findStudentsByAgeRange(int from, int to) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT person.*, student.currentYear, TIMESTAMPDIFF(YEAR,birthDate,CURDATE()) AS age " +
                    "FROM student INNER JOIN person ON student.dni = person.dni " +
                    "HAVING age >= ? AND age <= ?");
            ps.setInt(1, from);
            ps.setInt(2, to);
            ResultSet rs = ps.executeQuery();
            Set<Student> students = extractStudentsFromResultSet(rs);
            ps.close();
            return students;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return new HashSet<Student>();
        }
    }

    private static Set<Student> extractStudentsFromResultSet(ResultSet rs) throws SQLException {
        Set<Student> students = new HashSet();
        while(rs.next()) {
            Student student = new Student();
            student.setDni(rs.getInt("dni"));
            student.setFirstName(rs.getString("firstName"));
            student.setLastName(rs.getString("lastName"));
            student.setBirthDate(rs.getDate("birthDate"));
            student.setCurrentYear(rs.getInt("currentYear"));
            students.add(student);
        }
        return students;
    }

    //    public static Set<Student> findStudentsBySubject(String subject){
//        try {
//            PreparedStatement ps = connection.prepareStatement("SELECT * FROM student WHERE subject=?");
//            ps.setString(1, subject);
//            ResultSet rs = ps.executeQuery();
//            Set<Student> students = extractStudentsFromResultSet(rs);
//            return students;
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//        return null;
//    }
}
