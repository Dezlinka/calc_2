package ru.itis2016;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ManagementSystem {
    private static ManagementSystem instance;
    private static Context ctx = null;
    private static DataSource dataSource =null;
    private ManagementSystem() {
    }


    public Connection createConnection() {

        Context ctx = null;
        try {
            ctx = new InitialContext();
            return ((DataSource) ctx.lookup("java:comp/env/jdbc/StudentsDS")).getConnection();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ;
        // Использовать DRIVER и DBURL для создания соединения
        // Рекомендовать реализацию/использование пула соединений
        return null;
    }

    public Connection getConnection() throws SQLException {
        return createConnection();
    }

    public static synchronized ManagementSystem getInstance() {
        if (instance == null) {
                instance = new ManagementSystem();
        }
        return instance;
    }

    public List getGroups() throws SQLException {
        List groups = new ArrayList();
        Connection cnct=getConnection();
        Statement stmt = cnct.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT id, groupName, curator, speciality FROM groups");
        while (rs.next()) {
            Group gr = new Group();
            gr.setGroupId(rs.getInt(1));
            gr.setNameGroup(rs.getString(2));
            gr.setCurator(rs.getString(3));
            gr.setSpeciality(rs.getString(4));
            groups.add(gr);
        }
        rs.close();
        stmt.close();
        cnct.close();
        return groups;
    }

    public Collection getAllStudents() throws SQLException {
        Collection students = new ArrayList();
        Connection cnct=getConnection();
        Statement stmt = cnct.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT student_id, firstName, patronymic, surName, "
                + "sex, dateOfBirth, group_id, educationYear FROM students ORDER BY surName, firstName, patronymic");
        while (rs.next()) {
            Student st = new Student(rs);
            students.add(st);
        }
        rs.close();
        stmt.close();
        cnct.close();
        return students;
    }

    public Collection getStudentsFromGroup(Group group, int year) throws SQLException {
        Collection students = new ArrayList();
        Connection cnct=getConnection();
        PreparedStatement stmt = cnct.prepareStatement("SELECT student_id, firstName, patronymic, surName, "
                + "sex, dateOfBirth, group_id, educationYear FROM students "
                + "WHERE group_id =  ? AND  educationYear =  ? "
                + "ORDER BY surName, firstName, patronymic");
        stmt.setInt(1, group.getGroupId());
        stmt.setInt(2, year);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Student st = new Student(rs);
            students.add(st);
        }
        rs.close();
        stmt.close();
        cnct.close();
        return students;
    }

    public Collection getStudentsFromGroup(Group group) throws SQLException {
        Collection students = new ArrayList();
        Connection cnct=getConnection();
        PreparedStatement stmt = cnct.prepareStatement("SELECT student_id, firstName, patronymic, surName, "
                + "sex, dateOfBirth, group_id, educationYear FROM students "
                + "WHERE group_id =  ? "
                + "ORDER BY surName, firstName, patronymic");
        stmt.setInt(1, group.getGroupId());
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Student st = new Student(rs);
            students.add(st);
        }
        rs.close();
        stmt.close();
        cnct.close();
        return students;
    }

    public Student getStudentById(int studentId) throws SQLException {
        Student student = null;Connection cnct=getConnection();
        PreparedStatement stmt = cnct.prepareStatement("SELECT student_id, firstName, patronymic, surName,"
                + "sex, dateOfBirth, group_id, educationYear FROM students WHERE student_id = ?");
        stmt.setInt(1, studentId);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            student = new Student(rs);
        }
        rs.close();
        stmt.close();
        cnct.close();
        return student;
    }

    public void moveStudentsToGroup(Group oldGroup, int oldYear, Group newGroup, int newYear) throws SQLException {
        Connection cnct=getConnection();
        PreparedStatement stmt = cnct.prepareStatement("UPDATE students SET group_id =  ?, educationYear=? "
                + "WHERE group_id =  ? AND  educationYear = ?");
        stmt.setInt(1, newGroup.getGroupId());
        stmt.setInt(2, newYear);
        stmt.setInt(3, oldGroup.getGroupId());
        stmt.setInt(4, oldYear);
        stmt.execute();
        cnct.close();
    }

    public void removeStudentsFromGroup(Group group, int year) throws SQLException {
        Connection cnct=getConnection();
        PreparedStatement stmt = cnct.prepareStatement("DELETE FROM students WHERE group_id = ? AND educationYear = ?");
        stmt.setInt(1, group.getGroupId());
        stmt.setInt(2, year);
        stmt.execute();
        cnct.close();
    }

    public void insertStudent(Student student) throws SQLException {
        Connection cnct=getConnection();
        PreparedStatement stmt = cnct.prepareStatement("INSERT INTO students "
                + "(firstName, patronymic, surName, sex, dateOfBirth, group_id, educationYear)"
                + "VALUES( ?,  ?,  ?,  ?,  ?,  ?,  ?)");
        stmt.setString(1, student.getFirstName());
        stmt.setString(2, student.getPatronymic());
        stmt.setString(3, student.getSurName());
        stmt.setString(4, new String(new char[]{student.getSex()}));
        stmt.setDate(5, new Date(student.getDateOfBirth().getTime()));
        stmt.setInt(6, student.getGroupId());
        stmt.setInt(7, student.getEducationYear());
        stmt.execute();
        cnct.close();
    }

    public void updateStudent(Student student) throws SQLException {
        Connection cnct=getConnection();
        PreparedStatement stmt = cnct.prepareStatement("UPDATE students "
                + "SET firstName=?, patronymic=?, surName=?, sex=?, dateOfBirth=?, group_id=?,"
                + "educationYear=? WHERE student_id=?");
        stmt.setString(1, student.getFirstName());
        stmt.setString(2, student.getPatronymic());
        stmt.setString(3, student.getSurName());
        stmt.setString(4, new String(new char[]{student.getSex()}));
        stmt.setDate(5, new Date(student.getDateOfBirth().getTime()));
        stmt.setInt(6, student.getGroupId());
        stmt.setInt(7, student.getEducationYear());
        stmt.setInt(8, student.getStudentId());
        stmt.execute();
        cnct.close();
    }

    public void deleteStudent(Student student) throws SQLException {
        Connection cnct=getConnection();
        PreparedStatement stmt = cnct.prepareStatement("DELETE FROM students WHERE student_id =  ?");
        stmt.setInt(1, student.getStudentId());
        stmt.execute();
        cnct.close();
    }
}