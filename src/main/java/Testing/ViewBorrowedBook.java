package Testing;

import Features.Database;
import data.Admin;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.cell.TextFieldTableCell;
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
import javafx.util.converter.IntegerStringConverter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


//KUrang tombol simpan data ke dataabase 
public class ViewBorrowedBook extends Application {

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

        public void setNim(String nim) { this.nim.set(nim); }
        public void setBookId(String bookId) { this.bookId.set(bookId); }
        public void setTitle(String title) { this.title.set(title); }
        public void setAuthor(String author) { this.author.set(author); }
        public void setCategory(String category) { this.category.set(category); }
        public void setDuration(int duration) { this.duration.set(duration); }
        public void setExpired(String expired) { this.expired.set(expired); }


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

    private final ObservableList<ManageBorrowedBook.Book> bookList = FXCollections.observableArrayList();
    private final String dbUrl = "jdbc:sqlite:src/main/java/.database/Book";
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
        tableView.setEditable(true);
        tableView.setItems(borrowedBook);

        TableColumn<Book, String> nimCol = new TableColumn<>("NIM");
        nimCol.setCellValueFactory(cell -> cell.getValue().nim);
        nimCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nimCol.setOnEditCommit(e -> e.getRowValue().setNim(e.getNewValue()));

        TableColumn<Book, String> bookIdCol = new TableColumn<>("ID Buku");
        bookIdCol.setCellValueFactory(cell -> cell.getValue().bookId);
        bookIdCol.setCellFactory(TextFieldTableCell.forTableColumn());
        bookIdCol.setOnEditCommit(e -> e.getRowValue().setBookId(e.getNewValue()));

        TableColumn<Book, String> titleCol = new TableColumn<>("Judul");
        titleCol.setCellValueFactory(cell -> cell.getValue().title);
        titleCol.setCellFactory(TextFieldTableCell.forTableColumn());
        titleCol.setOnEditCommit(e -> e.getRowValue().setTitle(e.getNewValue()));


        TableColumn<Book, String> authorCol = new TableColumn<>("Penulis");
        authorCol.setCellValueFactory(cell -> cell.getValue().author);
        authorCol.setCellFactory(TextFieldTableCell.forTableColumn());
        authorCol.setOnEditCommit(e -> e.getRowValue().setAuthor(e.getNewValue()));


        TableColumn<Book, String> categoryCol = new TableColumn<>("Kategori");
        categoryCol.setCellValueFactory(cell -> cell.getValue().category);
        categoryCol.setCellFactory(TextFieldTableCell.forTableColumn());
        categoryCol.setOnEditCommit(e -> e.getRowValue().setCategory(e.getNewValue()));


        TableColumn<Book, Integer> durationCol = new TableColumn<>("Durasi (hari)");
        durationCol.setCellValueFactory(cell -> cell.getValue().duration.asObject());
        durationCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        durationCol.setOnEditCommit(e -> e.getRowValue().setDuration(e.getNewValue()));


        TableColumn<Book, String> expiredCol = new TableColumn<>("Kembali");
        expiredCol.setCellValueFactory(cell -> cell.getValue().expired);
        expiredCol.setCellFactory(TextFieldTableCell.forTableColumn());
        expiredCol.setOnEditCommit(e -> e.getRowValue().setExpired(e.getNewValue()));


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

        Button btnUpdate = new Button("Update");
        btnUpdate.setOnAction(e -> {
            List<Book> latestData = updateBorrowedBooks();

            borrowedBook.clear();                // Kosongkan yang lama
            borrowedBook.addAll(latestData);    // Tambahkan data dari DB
            tableView.refresh();                    // Refresh tampilan tabel
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
        VBox layout = new VBox(10, titleLabel, searchBox,returnButton,btnUpdate, tableView );
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


        System.out.print("DEBUG - Ukuran ArrayList: " + addUser.size());
    }




    //========================================DATA=======================================================
    private final String DB_URL = "jdbc:sqlite:src/main/java/.database/Book";

    public void saveBorrowedBooks(List<ViewBorrowedBook.Book> borrowedBooks) {
        String sql = "INSERT INTO borrowed_books (nim, book_id, title, author, category, duration, expired_borrowedBook) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (ViewBorrowedBook.Book book : borrowedBooks) {
                pstmt.setString(1, book.getNim());
                pstmt.setString(2, book.getBookId());
                pstmt.setString(3, book.getTitle());
                pstmt.setString(4, book.getAuthor());
                pstmt.setString(5, book.getCategory());
                pstmt.setInt(6, book.getDuration());
                pstmt.setString(7, book.getExpired());
                pstmt.addBatch();
            }

            pstmt.executeBatch();
            System.out.println("✅ Data peminjaman berhasil disimpan ke database oleh ManageBorrowedBook.");

        } catch (SQLException e) {
            System.err.println("❌ Gagal menyimpan data peminjaman: " + e.getMessage());
        }
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

    public void clearBorrowedBooks() {
        String sql = "DELETE FROM borrowed_books";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.executeUpdate();
            System.out.println("🧹 Semua data peminjaman berhasil dihapus dari database.");

        } catch (SQLException e) {
            System.err.println("❌ Gagal menghapus data peminjaman: " + e.getMessage());
        }
    }

    private void saveBooksToDatabase() {
        String url = "jdbc:sqlite:src/main/java/.database/Book";
        String insertSQL = "INSERT OR REPLACE INTO borrowed_books (nim, book_id, title, author, category, duration, expired_borrowedBook) VALUES (?, ?, ?, ?, ?, ?, ?)";

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

    private void saveToDatabase() {
        String deleteAll = "DELETE FROM borrowed_books";
        String insert = "INSERT INTO borrowed_books (nim, book_id, title, author, category, duration, expired_borrowedBook) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {

            conn.setAutoCommit(false);
            stmt.executeUpdate(deleteAll);

            try (PreparedStatement ps = conn.prepareStatement(insert)) {
                for (ManageBorrowedBook.Book book : bookList) {
                    ps.setString(1, book.getNim());
                    ps.setString(2, book.getBookId());
                    ps.setString(3, book.getTitle());
                    ps.setString(4, book.getAuthor());
                    ps.setString(5, book.getCategory());
                    ps.setInt(6, book.getDuration());
                    ps.setString(7, book.getExpired());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conn.commit();
            showAlert("Berhasil", "Semua perubahan berhasil disimpan.");

        } catch (SQLException e) {
            showAlert("Gagal Menyimpan Data", e.getMessage());
        }
    }

    // Tambahkan tombol "Update" → load ulang dari database
    public static List<ViewBorrowedBook.Book> updateBorrowedBooks() {
        List<ViewBorrowedBook.Book> bookList = new ArrayList<>();
        String sql = "SELECT * FROM borrowed_books";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:src/main/java/.database/Book");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String nim = rs.getString("nim");
                String bookId = rs.getString("book_id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String category = rs.getString("category");
                int duration = rs.getInt("duration");
                String expired = rs.getString("expired_borrowedBook");

                ViewBorrowedBook.Book book = new ViewBorrowedBook.Book(nim, bookId, title, author, category, duration, expired);
                bookList.add(book);
            }

            System.out.println("🔄 Data dari database berhasil dimuat ulang.");

        } catch (SQLException e) {
            System.err.println("❌ Gagal memuat data dari database: " + e.getMessage());
        }

        return bookList;
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
