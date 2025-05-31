package Testing;

import data.Admin;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.sql.*;

public class ManageBorrowedBook extends Application {

    public static class Book {
        private final SimpleStringProperty nim;
        private final SimpleStringProperty bookId;
        private final SimpleStringProperty title;
        private final SimpleStringProperty author;
        private final SimpleStringProperty category;
        private final SimpleIntegerProperty duration;
        private final SimpleStringProperty expired;

        public Book(String nim, String bookId, String title, String author, String category, int duration, String expired) {
            this.nim = new SimpleStringProperty(nim);
            this.bookId = new SimpleStringProperty(bookId);
            this.title = new SimpleStringProperty(title);
            this.author = new SimpleStringProperty(author);
            this.category = new SimpleStringProperty(category);
            this.duration = new SimpleIntegerProperty(duration);
            this.expired = new SimpleStringProperty(expired);
        }

        public String getNim() { return nim.get(); }
        public String getBookId() { return bookId.get(); }
        public String getTitle() { return title.get(); }
        public String getAuthor() { return author.get(); }
        public String getCategory() { return category.get(); }
        public int getDuration() { return duration.get(); }
        public String getExpired() { return expired.get(); }

        public void setNim(String nim) { this.nim.set(nim); }
        public void setBookId(String bookId) { this.bookId.set(bookId); }
        public void setTitle(String title) { this.title.set(title); }
        public void setAuthor(String author) { this.author.set(author); }
        public void setCategory(String category) { this.category.set(category); }
        public void setDuration(int duration) { this.duration.set(duration); }
        public void setExpired(String expired) { this.expired.set(expired); }
    }

    private final ObservableList<Book> bookList = FXCollections.observableArrayList();
    private final String dbUrl = "jdbc:sqlite:src/main/java/.database/Book";

    Admin adminObj = new Admin();

    @Override
    public void start(Stage stage) {
        TableView<Book> table = new TableView<>(bookList);
        table.setEditable(true);

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

        table.getColumns().addAll(nimCol, bookIdCol, titleCol, authorCol, categoryCol, durationCol, expiredCol);

        Button backBtn = new Button("Kembali");
        backBtn.setOnAction(e -> {
            saveToDatabase();
            // logika kembali ke adminMenuobj, misalnya:
            adminObj.menu();
            stage.close();
        });

        VBox root = new VBox(10, table, backBtn);
        root.setPadding(new Insets(10));

        loadFromDatabase();
        stage.setTitle("Manajemen Peminjaman Buku");
        stage.setScene(new Scene(root, 900, 600));
        stage.show();
    }

    private void loadFromDatabase() {
        bookList.clear();
        String query = "SELECT * FROM borrowed_books";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                bookList.add(new Book(
                        rs.getString("nim"),
                        rs.getString("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("category"),
                        rs.getInt("duration"),
                        rs.getString("expired_borrowedBook")
                ));
            }
        } catch (SQLException e) {
            showAlert("Gagal Memuat Data", e.getMessage());
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
                for (Book book : bookList) {
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.setHeaderText(title);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

