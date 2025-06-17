package Testing;

import Features.Database;
import Testing.JDBC.HikaruCP;
import Testing.JDBC.WriteManager;
import Testing.datatest.SimulasiData;
import data.Admin;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

//Arraylist untuk tabel tidak teruppdate
//simpan perubahan masih bermasalah yaitu data buku yang dipinjam tidak mau terupdate

//saat menghapus pinjaman via admin, stok buku tidak terupdate
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

    private final ObservableList<Book> bookList = FXCollections.observableArrayList();
    private final String dbUrl = "jdbc:sqlite:src/main/java/.database/Book";
    private   ObservableList<Book> borrowedBook = FXCollections.observableArrayList();

    private final List<AddUser> addUser = new ArrayList<>();
    private final List<Book> addbook = new ArrayList<>();


    //=====================================================================================================

    private int totalAdded = 0;

    @Override
    public void start(Stage primaryStage) {
        //Background
        Image backgroundImage = new Image("file:src/main/java/image/Manage_bukuTerpinjam.png");
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitHeight(768);
        backgroundImageView.setFitWidth(1366);
        backgroundImageView.setMouseTransparent(true);

        /*
        // Title Label
        Label titleLabel = new Label("📚 Data Peminjaman Buku");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.web("#2c3e50"));
        */

        // Search Field
        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Cari Judul / Penulis...");
        searchField.setStyle("-fx-background-radius: 8; -fx-padding: 8px;");
        Label searchLabel = new Label(" ");
        HBox searchBox = new HBox(10, searchLabel, searchField);
        searchBox.setAlignment(Pos.CENTER_RIGHT);
        searchBox.setPadding(new Insets(20, 20, 0, 0));

        // TableView
        TableView<Book> tableView = new TableView<>();
        tableView.setStyle("-fx-font-size: 13px;");
        tableView.setMinWidth(500);     // Lebar
        tableView.setMaxWidth(1278);     // Lebar
        tableView.setMinHeight(500);     // Tinggi
        tableView.setTranslateX(0);
        tableView.setTranslateY(12);
        tableView.getStylesheets().add("file:src/main/java/css/table.css");

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
        returnButton.getStylesheets().add("file:src/main/java/css/Login_button.css");
        returnButton.setOnAction(event -> {
            Admin adminObj = new Admin();
            adminObj.menu();
            primaryStage.close();

        });

        Button btn_muatUlang = new Button("Muat Ulang");
        btn_muatUlang.getStylesheets().add("file:src/main/java/css/Login_button.css");
        btn_muatUlang.setOnAction(e -> {
            List<Book> latestData = updateBorrowedBooks();

            borrowedBook.clear();                // Kosongkan yang lama
            borrowedBook.addAll(latestData);    // Tambahkan data dari DB

            tableView.refresh();                    // Refresh tampilan tabel
        });

        Button btn_update = new Button("Simpan Perubahan");
        btn_update.getStylesheets().add("file:src/main/java/css/Login_button.css");
        btn_update.setOnAction(e -> {
            btn_update.setDisable(true); // Mencegah klik ganda
            System.out.println("Menyimpan data buku ke database...");

            Task<Void> updateTask = new Task<>() {
                @Override
                protected Void call() {
                    try {
                        saveBooksToDatabase();
                        System.out.println("Data buku berhasil disimpan ke database.");
                    } catch (Exception ex) {
                        System.out.println("Terjadi kesalahan saat menyimpan data buku: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                    return null;
                }
            };

            updateTask.setOnSucceeded(ev -> {
                System.out.println("Operasi penyimpanan selesai.");
                btn_update.setDisable(false);
            });

            updateTask.setOnFailed(ev -> {
                System.out.println("Operasi penyimpanan gagal.");
                btn_update.setDisable(false);
            });

            Thread thread = new Thread(updateTask);
            thread.setDaemon(true);
            thread.start();
        });


        // Search Logic
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String filter = newVal.toLowerCase();
            ObservableList<Book> filteredList = FXCollections.observableArrayList();

            if (filter.isEmpty()) {
                filteredList.addAll(borrowedBook);
            } else {
                for (Book book : borrowedBook) {
                    if (book.getNim().toLowerCase().contains(filter) || book.getBookId().toLowerCase().contains(filter) || book.getTitle().toLowerCase().contains(filter) || book.getAuthor().toLowerCase().contains(filter)) {
                        filteredList.add(book);
                    }
                }
            }
            tableView.setItems(filteredList);
        });

        // Layout
        VBox searchBoxContainer = new VBox(10, searchBox);


        VBox rootTable = new VBox(28, tableView);

        //BUTTON DELETE
        // Inside the start method, modify the button HBox to include the delete button
        Button btn_delete = new Button("Hapus");
        btn_delete.getStylesheets().add("file:src/main/java/css/Login_button.css");
        btn_delete.setOnAction(e -> {
                    Book selectedBook = tableView.getSelectionModel().getSelectedItem();
                    if (selectedBook != null) {
                        if (selectedBook.getNim() != null && !selectedBook.getNim().isEmpty()) {
                            deleteBookFromDatabase(selectedBook.getBookId(), selectedBook.getNim());
                            borrowedBook.remove(selectedBook);
                            tableView.refresh();
                        } else {
                            showAlert("Pilih Buku", "NIM tidak valid untuk buku yang dipilih.");
                        }
                    } else {
                        showAlert("Pilih Buku", "Silakan pilih buku dari tabel untuk dihapus.");
                    }
                });

        //SUSUN INI DAI
        rootTable.setTranslateY(137);//200=makin kebawah, 100 = makin keatas
        rootTable.setTranslateX(46);
        rootTable.setPadding(new Insets(0));

        HBox rootBtn = new HBox(10, returnButton, btn_muatUlang, btn_update, btn_delete);

        //SUSUN INI DAI
        rootBtn.setTranslateY(670);
        rootBtn.setTranslateX(250);

        rootBtn.setPadding(new Insets(15));





        // Scene setup
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(backgroundImageView,searchBoxContainer, rootTable,rootBtn);

        Scene scene = new Scene(stackPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Data Peminjaman Buku");
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.show();

        loadBooksFromDatabase();


        System.out.print("DEBUG - Ukuran ArrayList borrowedBook: " + borrowedBook.size());
        System.out.print("DEBUG - Ukuran ArrayList bookList: " + bookList.size());

    }




    //========================================DATA=======================================================
    private final String DB_URL = "jdbc:sqlite:src/main/java/.database/Book";

    // New method to delete a book from the database
    private void deleteBookFromDatabase(String bookId, String nim) {

        String deleteSQL = "DELETE FROM borrowed_books WHERE book_id = ? AND nim = ?";

        WriteManager.writeAsUser(()->{
            try (Connection conn = HikaruCP.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {

                pstmt.setString(1, bookId);
                pstmt.setString(2, nim);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("✅ Buku dengan ID " + bookId + " dan NIM " + nim + " berhasil dihapus dari database.");

                } else {
                    System.out.println("❌ Tidak ada buku dengan ID " + bookId + " dan NIM " + nim + " ditemukan di database.");

                }

            } catch (SQLException e) {
                e.printStackTrace();

            }
        });
    }

    public void saveBorrowedBooks(List<Book> borrowedBooks) {
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
        String insertSQL = "INSERT OR REPLACE INTO borrowed_books (nim, book_id, title, author, category, duration, expired_borrowedBook) VALUES (?, ?, ?, ?, ?, ?, ?)";

    WriteManager.writeAsUser(() -> {
        try (Connection conn = HikaruCP.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            conn.setAutoCommit(false);
            for (Book book : borrowedBook) {
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
            conn.commit();
            System.out.println("✅ Semua data berhasil disimpan ke database.");

        } catch (SQLException e) {
            e.printStackTrace();
            javafx.application.Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Gagal menyimpan data: " + e.getMessage());
                alert.showAndWait();
            });
        }
    });
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

    // Tambahkan tombol "Update" → load ulang dari database
    public static List<Book> updateBorrowedBooks() {
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

                Book book = new ViewBorrowedBook.Book(nim, bookId, title, author, category, duration, expired);
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
