package Testing.datatest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertStudents {

    public static void main(String[] args) {
        String url = "jdbc:sqlite:src/main/java/.database/User_database";

        try (Connection conn = DriverManager.getConnection(url)) {
            conn.setAutoCommit(false); // 🔥 Efisiensi tinggi!

            String sql = "INSERT INTO mahasiswa_credentials (nim, pic, name, faculty, major, email) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            int totalData = 500;

            for (int i = 1; i <= totalData; i++) {
                ps.setString(1, "2023103703" + String.format("%05d", i));
                ps.setString(2, "PIC" + i);
                ps.setString(3, "Nama Mahasiswa " + i);
                ps.setString(4, "Fakultas Teknik");
                ps.setString(5, "Informatika");
                ps.setString(6, "mahasiswa" + i + "@email.com");

                ps.addBatch();

                if (i % 1000 == 0) {  // 🔁 Eksekusi setiap 1000 data
                    ps.executeBatch();
                }
            }

            ps.executeBatch();  // Eksekusi sisa batch
            conn.commit();      // 🔒 Commit semua
            System.out.println("Berhasil memasukkan 108.000 data!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
