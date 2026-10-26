import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AcademicDAO {

    public boolean registerStudent(
            String roll, String name, String dept, int semester)
            throws SQLException {

        String sql = "INSERT INTO students "
                + "(roll_number, student_name, department, current_semester) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, roll);
            ps.setString(2, name);
            ps.setString(3, dept);
            ps.setInt(4, semester);

            return ps.executeUpdate() > 0;
        }
    }
    public boolean addCourse(
        String code, String title, int credits, String type)
        throws SQLException {

    String sql = "INSERT INTO courses "
            + "(course_code, course_title, credits, course_type) "
            + "VALUES (?, ?, ?, ?)";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, code);
        ps.setString(2, title);
        ps.setInt(3, credits);
        ps.setString(4, type.toUpperCase());

        return ps.executeUpdate() > 0;
    }
}
public boolean enrollStudent(
        String roll, String course, int semester,
        int total, int attended) throws SQLException {

    String sql = "INSERT INTO enrollments "
            + "(roll_number, course_code, semester, total_classes, classes_attended) "
            + "VALUES (?, ?, ?, ?, ?)";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, roll);
        ps.setString(2, course);
        ps.setInt(3, semester);
        ps.setInt(4, total);
        ps.setInt(5, attended);

        return ps.executeUpdate() > 0;
    }
}
public int getEnrollmentId(
        String roll, String course, int semester)
        throws SQLException {

    String sql = "SELECT enrollment_id FROM enrollments "
            + "WHERE roll_number = ? "
            + "AND course_code = ? AND semester = ?";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, roll);
        ps.setString(2, course);
        ps.setInt(3, semester);

        try (java.sql.ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("enrollment_id");
            }
        }
    }

    return -1;
}
public void recordPhase1Marks(int id, double cia1, double cia2)
        throws SQLException {
    String sql = "INSERT INTO assessment_marks "
            + "(enrollment_id, cia1, cia2) VALUES (?, ?, ?) "
            + "ON DUPLICATE KEY UPDATE cia1=VALUES(cia1), "
            + "cia2=VALUES(cia2)";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, id);
        ps.setDouble(2, cia1);
        ps.setDouble(3, cia2);
        ps.executeUpdate();
    }
}

public void recordPhase2Marks(int id, double assignment,
        double quiz, double lab) throws SQLException {
    String sql = "INSERT INTO assessment_marks "
            + "(enrollment_id, assignment_marks, quiz_marks, lab_evaluation) "
            + "VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE "
            + "assignment_marks=VALUES(assignment_marks), "
            + "quiz_marks=VALUES(quiz_marks), "
            + "lab_evaluation=VALUES(lab_evaluation)";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, id);
        ps.setDouble(2, assignment);
        ps.setDouble(3, quiz);
        ps.setDouble(4, lab);
        ps.executeUpdate();
    }
}

public void recordPhase3Marks(int id, double ese, boolean absent)
        throws SQLException {
    String sql = "INSERT INTO assessment_marks "
            + "(enrollment_id, ese_marks, ese_absent) VALUES (?, ?, ?) "
            + "ON DUPLICATE KEY UPDATE ese_marks=VALUES(ese_marks), "
            + "ese_absent=VALUES(ese_absent)";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, id);
        ps.setDouble(2, ese);
        ps.setBoolean(3, absent);
        ps.executeUpdate();
    }
}
}