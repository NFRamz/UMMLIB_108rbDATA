package Testing;

import data.Admin;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.*;

public class ViewStudent extends Application {

    // Inner class for Student
    public static class Student {
        private final SimpleStringProperty nim;
        private final SimpleStringProperty pic;
        private final SimpleStringProperty name;
        private final SimpleStringProperty faculty;
        private final SimpleStringProperty major;
        private final SimpleStringProperty email;

        public Student(String nim, String pic, String name, String faculty, String major, String email) {
            this.nim = new SimpleStringProperty(nim);
            this.pic = new SimpleStringProperty(pic);
            this.name = new SimpleStringProperty(name);
            this.faculty = new SimpleStringProperty(faculty);
            this.major = new SimpleStringProperty(major);
            this.email = new SimpleStringProperty(email);
        }

        public String getNim() { return nim.get(); }
        public String getPic() { return pic.get(); }
        public String getName() { return name.get(); }
        public String getFaculty() { return faculty.get(); }
        public String getMajor() { return major.get(); }
        public String getEmail() { return email.get(); }

        public void setNim(String nim) { this.nim.set(nim); }
        public void setPic(String pic) { this.pic.set(pic); }
        public void setName(String name) { this.name.set(name); }
        public void setFaculty(String faculty) { this.faculty.set(faculty); }
        public void setMajor(String major) { this.major.set(major); }
        public void setEmail(String email) { this.email.set(email); }

        public SimpleStringProperty nimProperty() { return nim; }
        public SimpleStringProperty picProperty() { return pic; }
        public SimpleStringProperty nameProperty() { return name; }
        public SimpleStringProperty facultyProperty() { return faculty; }
        public SimpleStringProperty majorProperty() { return major; }
        public SimpleStringProperty emailProperty() { return email; }
    }

    private final ObservableList<Student> studentList = FXCollections.observableArrayList();
    private final String dbUrl = "jdbc:sqlite:src/main/java/.database/User_database";


    private void loadStudentsFromDatabase() {
        String sql = "SELECT * FROM mahasiswa_credentials";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            studentList.clear(); // Clear existing data
            while (rs.next()) {
                String nim = rs.getString("nim");
                String pic = rs.getString("pic");
                String name = rs.getString("name");
                String faculty = rs.getString("faculty");
                String major = rs.getString("major");
                String email = rs.getString("email");
                studentList.add(new Student(nim, pic, name, faculty, major, email));
            }
            System.out.println("DEBUG - Ukuran ObservableList studentList: " + studentList.size());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveStudentsToDatabase() {
        String sql = "UPDATE mahasiswa_credentials SET pic = ?, name = ?, faculty = ?, major = ?, email = ? WHERE nim = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (Student student : studentList) {
                pstmt.setString(1, student.getPic());
                pstmt.setString(2, student.getName());
                pstmt.setString(3, student.getFaculty());
                pstmt.setString(4, student.getMajor());
                pstmt.setString(5, student.getEmail());
                pstmt.setString(6, student.getNim());
                pstmt.executeUpdate();
            }
            System.out.println("Data students berhasil disimpan ke database.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        // Load data from database
        loadStudentsFromDatabase();

        // Background
        Image backgroundImage = new Image("file:src/main/java/image/Manage_bukuTerpinjam.png");
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitHeight(768);
        backgroundImageView.setFitWidth(1366);
        backgroundImageView.setMouseTransparent(true);

        // Search Field
        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Cari NIM / Nama...");
        searchField.setStyle("-fx-background-radius: 8; -fx-padding: 8px;");
        Label searchLabel = new Label(" ");
        HBox searchBox = new HBox(10, searchLabel, searchField );
        searchBox.setAlignment(Pos.CENTER_RIGHT);
        searchBox.setPadding(new Insets(20, 20, 0, 0));

        // TableView
        TableView<Student> tableView = new TableView<>();
        tableView.setStyle("-fx-font-size: 13px;");
        tableView.setMinWidth(1278); // Match ViewBorrowedBook
        tableView.setMaxWidth(1278);
        tableView.setMinHeight(500);
        tableView.setTranslateX(0);
        tableView.setTranslateY(12);
        tableView.getStylesheets().add("file:src/main/java/css/table.css");
        tableView.setEditable(true);
        tableView.setItems(studentList);

        // Table Columns
        TableColumn<Student, String> nimCol = new TableColumn<>("NIM");
        nimCol.setCellValueFactory(cell -> cell.getValue().nimProperty());
        nimCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nimCol.setOnEditCommit(e -> e.getRowValue().setNim(e.getNewValue()));
        nimCol.setPrefWidth(200);

        TableColumn<Student, String> picCol = new TableColumn<>("PIC");
        picCol.setCellValueFactory(cell -> cell.getValue().picProperty());
        picCol.setCellFactory(TextFieldTableCell.forTableColumn());
        picCol.setOnEditCommit(e -> e.getRowValue().setPic(e.getNewValue()));
        picCol.setPrefWidth(180);

        TableColumn<Student, String> nameCol = new TableColumn<>("Nama");
        nameCol.setCellValueFactory(cell -> cell.getValue().nameProperty());
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setOnEditCommit(e -> e.getRowValue().setName(e.getNewValue()));
        nameCol.setPrefWidth(200);

        TableColumn<Student, String> facultyCol = new TableColumn<>("Fakultas");
        facultyCol.setCellValueFactory(cell -> cell.getValue().facultyProperty());
        facultyCol.setCellFactory(TextFieldTableCell.forTableColumn());
        facultyCol.setOnEditCommit(e -> e.getRowValue().setFaculty(e.getNewValue()));
        facultyCol.setPrefWidth(200);

        TableColumn<Student, String> majorCol = new TableColumn<>("Jurusan");
        majorCol.setCellValueFactory(cell -> cell.getValue().majorProperty());
        majorCol.setCellFactory(TextFieldTableCell.forTableColumn());
        majorCol.setOnEditCommit(e -> e.getRowValue().setMajor(e.getNewValue()));
        majorCol.setPrefWidth(200);

        TableColumn<Student, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(cell -> cell.getValue().emailProperty());
        emailCol.setCellFactory(TextFieldTableCell.forTableColumn());
        emailCol.setOnEditCommit(e -> e.getRowValue().setEmail(e.getNewValue()));
        emailCol.setPrefWidth(278); // Adjusted to fit table width (1278 total)

        tableView.getColumns().addAll(nimCol, picCol, nameCol, facultyCol, majorCol, emailCol);

        // Buttons
        Button returnButton = new Button("Kembali");
        returnButton.getStylesheets().add("file:src/main/java/css/Login_button.css");
        returnButton.setOnAction(event -> {
            Admin adminObj = new Admin();
            adminObj.menu();
            primaryStage.close();
        });

        Button btn_muatUlang = new Button("Muat Ulang");
        btn_muatUlang.getStylesheets().add("file:src/main/java/css/Login_button.css");
        btn_muatUlang.setOnAction(e -> {
            loadStudentsFromDatabase();
            tableView.refresh();
        });

        Button btn_update = new Button("Simpan Perubahan");
        btn_update.getStylesheets().add("file:src/main/java/css/Login_button.css");
        btn_update.setOnAction(e -> {
            saveStudentsToDatabase();
        });

        // Search Logic
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String filter = newVal.toLowerCase();
            ObservableList<Student> filteredList = FXCollections.observableArrayList();

            if (filter.isEmpty()) {
                filteredList.addAll(studentList);
            } else {
                for (Student student : studentList) {
                    if (student.getNim().toLowerCase().contains(filter) || student.getName().toLowerCase().contains(filter)) {
                        filteredList.add(student);
                    }
                }
            }
            tableView.setItems(filteredList);
        });


        //BUTTON DELETE
        // Inside the start method, modify the button HBox to include the delete button
        Button btn_delete = new Button("Hapus");
        btn_delete.getStylesheets().add("file:src/main/java/css/Login_button.css");
        btn_delete.setOnAction(e -> {
            Student selectedUser = tableView.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                deleteUserFromDatabase(selectedUser);
                studentList.remove(selectedUser);
                tableView.refresh();
            } else {
                showAlert("Pilih Buku", "Silakan pilih buku dari tabel untuk dihapus.");
            }
        });

        // Layout
        VBox searchBoxContainer = new VBox(10, searchBox);
        VBox rootTable = new VBox(28, tableView);
        rootTable.setTranslateY(137); // Match ViewBorrowedBook
        rootTable.setTranslateX(46);
        rootTable.setPadding(new Insets(0));

        HBox rootBtn = new HBox(10, returnButton, btn_muatUlang, btn_update,btn_delete);
        rootBtn.setTranslateY(670);
        rootBtn.setTranslateX(250);
        rootBtn.setPadding(new Insets(15));


        // Scene setup
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(backgroundImageView, searchBoxContainer, rootTable, rootBtn);

        Scene scene = new Scene(stackPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Data Mahasiswa");
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.show();
    }
//================================================================================================

    // New method to delete a book from the database
    private void deleteUserFromDatabase(Student studentList) {
        String url = "jdbc:sqlite:src/main/java/.database/User_database";
        String deleteSQL = "DELETE FROM mahasiswa_credentials WHERE nim = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {

            pstmt.setString(1, studentList.getNim());
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Buku dengan ID " + studentList.getNim() + " berhasil dihapus dari database.");

            } else {
                System.out.println("❌ Tidak ada buku dengan ID " + studentList.getNim() + " ditemukan di database.");

            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.setHeaderText(title);
        alert.showAndWait();
    }
    public static void main(String[] args) {
        launch(args);
    }
}