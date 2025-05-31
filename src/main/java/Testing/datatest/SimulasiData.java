package Testing.datatest;

import Features.Database;
import data.Admin;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class SimulasiData extends Application {

    public static class Book {
        private   SimpleStringProperty nim;
        private   SimpleStringProperty bookId;
        private   SimpleStringProperty title;
        private   SimpleStringProperty author;
        private   SimpleStringProperty category;
        private   SimpleIntegerProperty duration;
        private   SimpleStringProperty expired;

        private SimpleIntegerProperty stock;

        public Book(String nim, String bookId, String title, String author, String category, int duration, String expired) {
            this.nim = new SimpleStringProperty(nim);
            this.bookId = new SimpleStringProperty(bookId);
            this.title = new SimpleStringProperty(title);
            this.author = new SimpleStringProperty(author);
            this.category = new SimpleStringProperty(category);
            this.duration = new SimpleIntegerProperty(duration);
            this.expired = new SimpleStringProperty(expired);
        }

        public Book(String bookId, String title, String author, String category, int stock) {
            this.bookId = new SimpleStringProperty(bookId);
            this.title = new SimpleStringProperty(title);
            this.author = new SimpleStringProperty(author);
            this.category = new SimpleStringProperty(category);
            this.stock = new SimpleIntegerProperty(stock);

        }



        public String getNim() { return nim.get(); }
        public String getBookId() { return bookId.get(); }
        public String getTitle() { return title.get(); }
        public String getAuthor() { return author.get(); }
        public String getCategory() { return category.get(); }
        public int getDuration() { return duration.get(); }
        public String getExpired() { return expired.get(); }

        public int getStock() {
            return stock.get();
        }

    }

    public static class AddUser {
        private   SimpleStringProperty nim;
        private   SimpleStringProperty pic;
        private   SimpleStringProperty name;
        private   SimpleStringProperty faculty;
        private   SimpleStringProperty major;
        private   SimpleStringProperty email;


        public AddUser() {


        }
        public AddUser(String nim, String pic, String name, String faculty, String major, String email) {
            this.nim = new SimpleStringProperty(nim);
            this.pic = new SimpleStringProperty(pic);
            this.name = new SimpleStringProperty(name);
            this.faculty = new SimpleStringProperty(faculty);
            this.major = new SimpleStringProperty(major);
            this.email = new SimpleStringProperty(email);
        }

        public  String getNim() { return nim.get(); }
        public String getPic() { return pic.get(); }
        public String getName() { return name.get(); }
        public String getFaculty() { return faculty.get(); }
        public String getMajor() { return major.get(); }
        public String getEmail() { return email.get(); }



    }

    private   ObservableList<Book> borrowedBook = FXCollections.observableArrayList();

    private final List<AddUser> addUser = new ArrayList<>();
    private final List<Book> addbook = new ArrayList<>();


    //=====================================================================================================

    private int totalAdded = 0;
    @Override
    public void start(Stage primaryStage) {

        // Title Label
        Label titleLabel = new Label("📚 Data Peminjaman Buku");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.web("#2c3e50"));

        // Search Field
        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Cari Judul / Penulis...");
        searchField.setStyle("-fx-background-radius: 8; -fx-padding: 8px;");
        Label searchLabel = new Label("Cari:");
        HBox searchBox = new HBox(10, searchLabel, searchField);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(10, 0, 10, 0));

        // TableView
        TableView<Book> tableView = new TableView<>();
        tableView.setStyle("-fx-font-size: 13px;");
        tableView.setItems(borrowedBook);

        TableColumn<Book, String> nimCol = new TableColumn<>("NIM");
        TableColumn<Book, String> bookIdCol = new TableColumn<>("ID Buku");
        TableColumn<Book, String> titleCol = new TableColumn<>("Judul");
        TableColumn<Book, String> authorCol = new TableColumn<>("Penulis");
        TableColumn<Book, String> categoryCol = new TableColumn<>("Kategori");
        TableColumn<Book, Integer> durationCol = new TableColumn<>("Durasi (hari)");
        TableColumn<Book, String> expiredCol = new TableColumn<>("Kembali");

        nimCol.setCellValueFactory(cell -> cell.getValue().nim);
        bookIdCol.setCellValueFactory(cell -> cell.getValue().bookId);
        titleCol.setCellValueFactory(cell -> cell.getValue().title);
        authorCol.setCellValueFactory(cell -> cell.getValue().author);
        categoryCol.setCellValueFactory(cell -> cell.getValue().category);
        durationCol.setCellValueFactory(cell -> cell.getValue().duration.asObject());
        expiredCol.setCellValueFactory(cell -> cell.getValue().expired);

        nimCol.setPrefWidth(200);
        bookIdCol.setPrefWidth(200);
        titleCol.setPrefWidth(200);
        authorCol.setPrefWidth(200);
        categoryCol.setPrefWidth(110);
        durationCol.setPrefWidth(150);
        expiredCol.setPrefWidth(150);

        tableView.getColumns().addAll(nimCol, bookIdCol, titleCol, authorCol, categoryCol, durationCol, expiredCol);

        // Return Button
        Button returnButton = new Button("Kembali");
        returnButton.setOnAction(event -> {
            Admin adminObj = new Admin();
            adminObj.menu();
            primaryStage.close();
            saveBooksToDatabase();
        });

        // Search Logic
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String filter = newVal.toLowerCase();
            ObservableList<Book> filteredList = FXCollections.observableArrayList();

            if (filter.isEmpty()) {
                filteredList.addAll(borrowedBook);
            } else {
                for (Book book : borrowedBook) {
                    if (book.getNim().toLowerCase().contains(filter) || book.getTitle().toLowerCase().contains(filter) || book.getAuthor().toLowerCase().contains(filter)) {
                        filteredList.add(book);
                    }
                }
            }
            tableView.setItems(filteredList);
        });

        // Layout
        VBox layout = new VBox(10, titleLabel, searchBox,returnButton, tableView );
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #ecf0f1;");
        layout.setAlignment(Pos.TOP_CENTER);

        // Scene setup
        Scene scene = new Scene(layout, 950, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Data Peminjaman Buku");
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.show();

        loadBooksFromDatabase();

        simulateDataAddition(300, Duration.seconds(5),1100);
        System.out.print("DEBUG - Ukuran ArrayList: " + addUser.size());
    }

    private void loadBooksFromDatabase() {
        String url = "jdbc:sqlite:src/main/java/.database/Book";
        String query = "SELECT * FROM borrowed_books";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String nim = rs.getString("nim");
                String bookId = rs.getString("book_id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String category = rs.getString("category");
                int duration = rs.getInt("duration");
                String expired = rs.getString("expired_borrowedBook");

                borrowedBook.add(new Book(nim, bookId, title, author, category, duration, expired));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Gagal memuat data: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void saveBooksToDatabase() {
        String url = "jdbc:sqlite:src/main/java/.database/Book";
        String insertSQL = "INSERT INTO borrowed_books (nim, book_id, title, author, category, duration, expired_borrowedBook) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            for (Book book : borrowedBook) {
                pstmt.setString(1, book.getNim());
                pstmt.setString(2, book.getBookId());
                pstmt.setString(3, book.getTitle());
                pstmt.setString(4, book.getAuthor());
                pstmt.setString(5, book.getCategory());
                pstmt.setInt(6, book.getDuration());
                pstmt.setString(7, book.getExpired());
                pstmt.addBatch(); // Tambahkan ke batch
            }

            pstmt.executeBatch(); // Jalankan semua sekaligus
            System.out.println("✅ Semua data berhasil disimpan ke database.");

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Gagal menyimpan data: " + e.getMessage());
            alert.showAndWait();
        }
    }


    // Metode untuk mensimulasikan penambahan 300 data per hari (misalnya, setiap 10 detik sebagai "satu hari")

    private void simulateDataAddition(int jumlahData, Duration interval, int maxTotal) {
        Timeline timeline = new Timeline(new KeyFrame(interval, i -> {

            if (totalAdded >= maxTotal) {
                System.out.println("Simulasi selesai: total data mencapai " + maxTotal);
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Simulasi selesai: total data mencapai " + maxTotal);
                    alert.show(); // NON-blocking version
                });


                // Simpan semua data ke database
                //saveAllBooksToDatabase();
                //saveAllUsersToDatabase();
                saveAllBorrowedBooksToDatabase();

                System.out.println("✅ Semua data simulasi berhasil disimpan ke database.");
                ((Timeline) i.getSource()).stop();
                return;
            }

            //addSimulatedBooks(jumlahData, maxTotal);
            //addSimulatedUsers(jumlahData, maxTotal);
            addSimulatedBorrowedBooks(jumlahData, maxTotal);

            System.out.println("✅ Tambahan data berjalan... Total: " + totalAdded);

        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void addSimulatedUsers(int jumlahData, int maxTotal) {
        int startIndex = addUser.size() + 1;
        for (int a = 0; a < jumlahData && totalAdded < maxTotal; a++) {
            String nim = String.format("%015d", startIndex + a);
            String pic = "B" + (startIndex + a);
            String name = "NF" + (startIndex + a);
            String faculty = "Teknik";
            String major = "Informatika";
            String email = "nframzi051@gmail.com";

            addUser.add(new AddUser(nim, pic, name, faculty, major, email));
            totalAdded++;
            System.out.println("👤 Tambah data pengguna ke-" + totalAdded);
        }
    }

    private void addSimulatedBooks(int jumlahData, int maxTotal) {
        int startIndex = addbook.size() + 1;
        for (int a = 0; a < jumlahData && totalAdded < maxTotal; a++) {
            String bookId = "B" + String.format("%04d", startIndex + a);
            String title = "Buku Simulasi " + (startIndex + a);
            String author = "Penulis Simulasi " + (startIndex + a);
            String category = "Kategori Simulasi";
            int stock = 5;

            addbook.add(new Book(bookId, title, author, category, stock));
            totalAdded++;
            System.out.println("📚 Tambah data buku ke-" + totalAdded);
        }
    }

    private void addSimulatedBorrowedBooks(int jumlahData, int maxTotal) {
        int startIndex = borrowedBook.size() + 1;
        for (int i = 0; i < jumlahData && totalAdded < maxTotal; i++) {
            String nim = String.format("%015d", startIndex + i);
            String bookId = "B" + String.format("%04d", startIndex + i);
            String title = "Buku Simulasi " + (startIndex + i);
            String author = "Penulis Simulasi " + (startIndex + i);
            String category = "Kategori Simulasi";
            int duration = 7;
            String expired = "2025-12-31";

            borrowedBook.add(new Book(nim, bookId, title, author, category, duration, expired));
            System.out.println("📖 Tambah data pinjaman ke-" + totalAdded);
            totalAdded++;
        }
    }

    private void saveAllUsersToDatabase() {
        for (AddUser add : addUser) {
            Database.student_addStudent(
                    add.getNim(),
                    add.getPic(),
                    add.getName(),
                    add.getFaculty(),
                    add.getMajor(),
                    add.getEmail()
            );
        }
        System.out.println("✅ Semua data pengguna berhasil disimpan ke database.");
    }

    private void saveAllBooksToDatabase() {
        for (Book book : addbook) {
            Database.admin_addBook(
                    book.getBookId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getCategory(),
                    book.getStock()
            );
        }
        System.out.println("📚 Semua data buku berhasil disimpan ke database.");
    }

    private void saveAllBorrowedBooksToDatabase() {
        for (Book book : borrowedBook) {
            admin_addBorrowedBook(
                    book.getNim(),
                    book.getBookId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getCategory(),
                    book.getDuration(),
                    book.getExpired()
            );
        }
        System.out.println("📖 Semua data pinjaman berhasil disimpan ke database.");
    }
    public static void admin_addBorrowedBook(String nim, String bookId, String title, String author, String category, int duration, String expired) {
        String url = "jdbc:sqlite:src/main/java/.database/Book";
        String sql = "INSERT INTO borrowed_books (nim, book_id, title, author, category, duration, expired_borrowedBook) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nim);
            pstmt.setString(2, bookId);
            pstmt.setString(3, title);
            pstmt.setString(4, author);
            pstmt.setString(5, category);
            pstmt.setInt(6, duration);
            pstmt.setString(7, expired);

            pstmt.executeUpdate();
            System.out.println("✅ Data pinjaman buku berhasil ditambahkan: " + title);

        } catch (SQLException e) {
            System.out.println("❌ Gagal menambahkan data pinjaman buku ke database: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
