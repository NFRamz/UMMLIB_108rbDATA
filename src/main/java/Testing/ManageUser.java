package Testing;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.sql.*;
import java.util.ArrayList;

public class ManageUser extends Application {

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

        public void setNim(String nim) {
            this.nim = nim;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setFaculty(String faculty) {
            this.faculty = faculty;
        }

        public void setMajor(String major) {
            this.major = major;
        }

        public void setEmail(String email) {
            this.email = email;
        }

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
        table.setEditable(true);

        TableColumn<ViewStudent.Student, String> nimCol     = new TableColumn<>("NIM");
        TableColumn<ViewStudent.Student, String> picCol     = new TableColumn<>("PIC");
        TableColumn<ViewStudent.Student, String> nameCol    = new TableColumn<>("Nama");
        TableColumn<ViewStudent.Student, String> facultyCol = new TableColumn<>("Fakultas");
        TableColumn<ViewStudent.Student, String> majorCol   = new TableColumn<>("Jurusan");
        TableColumn<ViewStudent.Student, String> emailCol   = new TableColumn<>("Email");

        nimCol.setCellValueFactory(new PropertyValueFactory<>("nim"));
        nimCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nimCol.setOnEditCommit(e -> e.getRowValue().setNim(e.getNewValue()));

        picCol.setCellValueFactory(new PropertyValueFactory<>("pic"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        facultyCol.setCellValueFactory(new PropertyValueFactory<>("faculty"));
        majorCol.setCellValueFactory(new PropertyValueFactory<>("major"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        Button btnUpdate = new Button("Update");


        VBox root = new VBox();

        VBox layout = new VBox(table);
        Scene scene = new Scene(layout, 900, 500);

        HBox layout1 = new HBox(btnUpdate);
        layout1.setSpacing(10);
        layout1.setAlignment(Pos.CENTER);

        root.getChildren().addAll(layout, layout1);

        stage.setTitle("Data Mahasiswa");
        stage.setScene(new Scene(root, 900, 600));
        stage.show();

        //AKSI TOMBOL

        btnUpdate.setOnAction(event -> {

        });

        btnUpdate.setOnAction(event -> {

        });
    }

    private void saveToDatabase() {
        String deleteAll = "DELETE FROM borrowed_books";
        String insert = "INSERT INTO mahasiswa_C (nim, pic, name, faculty, major, email) VALUES (?,?,?,?,?,?)";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:src/main/java/.database/User_database");
             Statement stmt = conn.createStatement()) {

            conn.setAutoCommit(false);
            stmt.executeUpdate(deleteAll);

            try (PreparedStatement ps = conn.prepareStatement(insert)) {
                for (Student student : studentList) {

                    ps.setString(1, student.getNim());
                    ps.setString(2, student.getName());
                    ps.setString(3, student.getFaculty());
                    ps.setString(4, student.getMajor());
                    ps.setString(5, student.getEmail());
                    ps.addBatch();

                }
                ps.executeBatch();
            }

            conn.commit();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Gagal menyimpan data: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void saveStudentsFromDatabase() {
        String url = "jdbc:sqlite:src/main/java/.database/User_database";
        String sql = "INSERT INTO  mahasiswa_credentials (nim, pic, name, faculty, major, email) VALUES (?,?,?,?,?,?)";

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


}
