package Testing.datatest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DeleteStudents {

    public static void main(String[] args) {
        String url = "jdbc:sqlite:src/main/java/.database/User_database";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            String sql = "DELETE FROM mahasiswa_credentials";
            stmt.executeUpdate(sql);

            System.out.println("Semua data dalam tabel 'students' telah dihapus.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
