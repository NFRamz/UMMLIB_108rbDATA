package Testing;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;

public class ViewStudent extends Application {

    // Inner class untuk Student
    public static class Student {
        private String nim;
        private String pic;
        private String name;
        private String faculty;
        private String major;
        private String email;

        public Student(String nim, String pic, String name, String faculty, String major, String email) {
            this.nim = nim;
            this.pic = pic;
            this.name = name;
            this.faculty = faculty;
            this.major = major;
            this.email = email;
        }

        public String getNim() { return nim; }
        public String getPic() { return pic; }
        public String getName() { return name; }
        public String getFaculty() { return faculty; }
        public String getMajor() { return major; }
        public String getEmail() { return email; }
    }

    private final ArrayList<Student> studentList = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    private void loadStudentsFromDatabase() {
        String url = "jdbc:sqlite:src/main/java/.database/User_database";
        String sql = "SELECT * FROM mahasiswa_credentials";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String nim = rs.getString("nim");
                String pic = rs.getString("pic");
                String name = rs.getString("name");
                String faculty = rs.getString("faculty");
                String major = rs.getString("major");
                String email = rs.getString("email");
                System.out.println("Ukuran ArrayList: " + studentList.size());
                studentList.add(new Student(nim, pic, name, faculty, major, email));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) {
        loadStudentsFromDatabase();

        TableView<Student> table = new TableView<>();

        TableColumn<Student, String> nimCol     = new TableColumn<>("NIM");
        TableColumn<Student, String> picCol     = new TableColumn<>("PIC");
        TableColumn<Student, String> nameCol    = new TableColumn<>("Nama");
        TableColumn<Student, String> facultyCol = new TableColumn<>("Fakultas");
        TableColumn<Student, String> majorCol   = new TableColumn<>("Jurusan");
        TableColumn<Student, String> emailCol   = new TableColumn<>("Email");

        nimCol.setCellValueFactory(new PropertyValueFactory<>("nim"));
        picCol.setCellValueFactory(new PropertyValueFactory<>("pic"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        facultyCol.setCellValueFactory(new PropertyValueFactory<>("faculty"));
        majorCol.setCellValueFactory(new PropertyValueFactory<>("major"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        table.getColumns().addAll(nimCol, picCol, nameCol, facultyCol, majorCol, emailCol);
        table.getItems().addAll(studentList);

        VBox root = new VBox(table);
        Scene scene = new Scene(root, 900, 500);

        stage.setTitle("Data Mahasiswa");
        stage.setScene(scene);
        stage.show();
    }
}
