package Features;

import books.Book;

import exception.custom.IllegalAdminAccess;
import javafx.scene.control.ListView;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Database {

    public static final String user_database = "jdbc:sqlite:src/main/java/.database/User_database";
    public static final String book_database = "jdbc:sqlite:src/main/java/.database/Book";

    public static Connection connect(String url) throws SQLException {
        return DriverManager.getConnection(url);
    }


    //========================================== Method for Student ===================================================

    public static void student_addStudent(String nim, String pic, String name, String faculty, String major, String email) {

        String sql = "INSERT INTO mahasiswa_credentials (nim, pic, name,faculty, major, email) VALUES (?, ?, ?, ?, ?, ?)";


        try (Connection conn = connect(user_database);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nim);
            pstmt.setString(2, pic);
            pstmt.setString(3, name);
            pstmt.setString(4, faculty);
            pstmt.setString(5, major);
            pstmt.setString(6, email);

            pstmt.executeUpdate();

            System.out.println("data mahasiswa berhasil ditambahkan ke database.");
        } catch (SQLException e) {
            System.out.println("Gagal menambahkan data mahasiswa ke database: " + e.getMessage());
        }
    }



    public static boolean student_loginChecker(String nim, String pic) throws IllegalAdminAccess {

        String sql = "SELECT * FROM mahasiswa_credentials WHERE NIM = ? AND PIC = ?";

        System.out.println("[DEBUG] Checking login for NIM: " + nim + " | PIC: " + pic); // debug input

        try (Connection conn = connect(user_database);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nim);
            pstmt.setString(2, pic);

            ResultSet rs = pstmt.executeQuery();

            boolean result = rs.next();
            System.out.println("[DEBUG] Login result for NIM " + nim + ": " + result); // debug hasil

            return result;

        } catch (SQLException e) {
            throw new IllegalAdminAccess("Error = Database.student_loginChecker");
        }
    }


    public static String student_getEmailbyNIM(String nim) throws SQLException {

        String sql = "SELECT * FROM mahasiswa_credentials WHERE nim = ?";
        String email = null;

        try (Connection conn = connect(user_database);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nim);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                email = rs.getString("email");
            }
        } catch (SQLException e) {
            System.out.println("Error = Database.student_getEmailbyNIM");
        }

        return email;
    }


    public static String student_getNamaByNIM(String nim) {

        String sql = "SELECT name FROM mahasiswa_credentials WHERE nim = ?";
        String nama = null;

        try (Connection conn = connect(user_database);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nim);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                nama = rs.getString("name");
            }
        } catch (SQLException e) {
            System.out.println("Error = Database.student_getNamaByNIM");
        }

        return nama;
    }


    public static String student_displayReturnTimeForBorrowedBook(int duration) {

        LocalDate returnDate = LocalDate.now().plusDays(duration);

        DateTimeFormatter displayTime_returnBook = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return returnDate.format(displayTime_returnBook);
    }


    public static void student_displayBorrowedBooks(String nim) {
        String sql = "SELECT book_id, title, author, category, duration FROM borrowed_books WHERE nim = ?";

        try (Connection conn = connect(book_database);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nim);
            ResultSet rs = pstmt.executeQuery();

            Book.arr_borrowedBook.clear();
            while (rs.next()) {
                nim = rs.getString("book_id");
                String bookId = rs.getString("book_id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String category = rs.getString("category");
                int duration = rs.getInt("duration");


                Book.arr_borrowedBook.add(new Book(nim, bookId, title, author, category, duration));
            }

        } catch (SQLException e) {
            System.out.println("Error = Database.student_displayBorrowedBooks ");
        }
    }

    public static void student_saveBorrowedBooks(String nim) {
        String sql = "INSERT OR REPLACE INTO borrowed_books (nim, book_id, title, author, category, duration, expired_borrowedBook) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect(book_database);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (Book borrowedBook : Book.arr_borrowedBook) {
                String returnDate = Database.student_displayReturnTimeForBorrowedBook(borrowedBook.getDuration());
                pstmt.setString(1, nim);
                pstmt.setString(2, borrowedBook.getBookId());
                pstmt.setString(3, borrowedBook.getTitle());
                pstmt.setString(4, borrowedBook.getAuthor());
                pstmt.setString(5, borrowedBook.getCategory());
                pstmt.setInt(6, borrowedBook.getDuration());
                pstmt.setString(7, returnDate);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            System.out.println("Data peminjaman berhasil disimpan ke database.");
        } catch (SQLException e) {
            System.out.println("Gagal menyimpan data peminjaman ke database: " + e.getMessage());
        }
    }

    public static void student_sendEmail(String recipientEmail, String subject, String body) {
        System.out.println("Email sent to: " + recipientEmail);
    }





    //=================================================== Method for Admin ===========================================
    public static boolean admin_loginCheck(String username, String password) {

        String sql = "SELECT * FROM admin_credentials WHERE username = ? AND password = ?";

        try (Connection conn = connect(user_database);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.out.println("Gagal memeriksa kredensial admin: " + e.getMessage());
            return false;
        }
    }



    public static void admin_displayStudent(ListView<String> listView) {
        String sql = "SELECT nim, pic, name, faculty, major, email FROM mahasiswa_credentials";

        try (Connection conn = connect(user_database);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            listView.getItems().clear();

            while (rs.next()) {
                String studentInfo = "Nama     : " + rs.getString("name") + "\n" +
                        "NIM      : " + rs.getString("nim") + "\n" +
                        "Fakultas : " + rs.getString("faculty") + "\n" +
                        "Prodi    : " + rs.getString("major") + "\n" +
                        "Email    : " + rs.getString("email") + "\n" +
                        "PIC      : " + rs.getString("pic") + "\n" +
                        "===========================";

                listView.getItems().add(studentInfo);
            }

        } catch (SQLException e) {
            System.out.println("Gagal menampilkan data mahasiswa\n " + e.getMessage());
        }
    }


    public static void admin_addBook(String id, String title, String author, String category, int stock) {

        String sql = "INSERT INTO book_data (id, title, author, category, stock) VALUES (?, ?, ?, ?, ?)";

        try (Connection database = connect(book_database);
             PreparedStatement ps_add = database.prepareStatement(sql)) {

            ps_add.setString(1, id);
            ps_add.setString(2, title);
            ps_add.setString(3, author);
            ps_add.setString(4, category);
            ps_add.setInt(5, stock);

            ps_add.executeUpdate();
            System.out.println("data buku berhasil ditambahkan ke database.");

        } catch (SQLException e) {
            System.out.println("Gagal menambahkan data mahasiswa ke database: " + e.getMessage());
        }
    }



    //============================================== Method for Book ===================================================

    public static void book_bookDisplay() {
        String sql = "SELECT * FROM book_data";

        try (Connection conn = connect(book_database);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String id = rs.getString("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String category = rs.getString("category");
                int stock = rs.getInt("stock");

                Book.arr_bookList.add(new Book(id, title, author, category, stock));
            }

        } catch (SQLException e) {
            System.out.println("Gagal menampilkan data buku: " + e.getMessage());
        }
    }


    public static void book_updateBookStock() {
        String updateStockSql = "UPDATE book_data SET stock = ? WHERE id = ?";

        try (Connection conn = connect(book_database);
             PreparedStatement updateStockStmt = conn.prepareStatement(updateStockSql)) {

            for (Book book : Book.arr_bookList) {
                int newStock = book.getStock();

                // Update the stock in the book_data table
                updateStockStmt.setInt(1, newStock);
                updateStockStmt.setString(2, book.getBookId());
                updateStockStmt.addBatch();
            }

            updateStockStmt.executeBatch();
            System.out.println("berhasil update semua stok buku.");

        } catch (SQLException e) {
            System.out.println("Error = Database.updateBookStock");
        }
    }

    public static void book_saveOrDeleteBorrowedBooks(String nim) throws SQLException {

        if (!Book.arr_borrowedBook.isEmpty() && Book.arr_borrowedBook.stream().anyMatch(book -> book.getNim().equals(nim))) {
            student_saveBorrowedBooks(nim);
        }

        Connection connection = connect(book_database);
        String deleteSql = "DELETE FROM borrowed_books WHERE nim = ? AND book_id NOT IN (";
        StringBuilder add = new StringBuilder();
        for (int i = 0; i < Book.arr_borrowedBook.size(); i++) {
            add.append("?");
            if (i < Book.arr_borrowedBook.size() - 1) {
                add.append(",");
            }
        }
        deleteSql += add + ")";

        PreparedStatement deleteStmt = connection.prepareStatement(deleteSql);
        deleteStmt.setString(1, nim);
        for (int i = 0; i < Book.arr_borrowedBook.size(); i++) {
            deleteStmt.setString(2 + i, Book.arr_borrowedBook.get(i).getBookId());
        }

        int deleteBaris = deleteStmt.executeUpdate();
    }


    public static boolean book_expiredDateBorrowedBook(String nim) {
        String sql = "SELECT COUNT(*) FROM borrowed_books WHERE nim = ? AND expired_borrowedBook < ?";
        try (Connection conn = connect(book_database);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nim);
            pstmt.setString(2, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error Database_Method = book_expiredDateBorrowedBook");
        }
        return false;
    }


    public static void book_expiredBorrowedBookSendEmail(String nim) {
        String sql = "SELECT book_id, title, expired_borrowedBook FROM borrowed_books WHERE nim = ?";

        try (Connection conn = connect(book_database);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nim);
            ResultSet rs = pstmt.executeQuery();

            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            StringBuilder emailBody = new StringBuilder();
            long totalDenda = 0;

            while (rs.next()) {
                String bookId = rs.getString("book_id");
                String title = rs.getString("title");
                LocalDate expiredDate = LocalDate.parse(rs.getString("expired_borrowedBook"), formatTime);
                long daysLate = java.time.temporal.ChronoUnit.DAYS.between(expiredDate, currentDate);


                if (currentDate.isAfter(expiredDate)) {
                    long denda = daysLate * 500;

                    emailBody.append("ID Buku: ").append(bookId).append("\n")
                            .append("Nama Buku: ").append(title).append("\n")
                            .append("Keterlambatan: ").append(daysLate).append(" hari\n")
                            .append("Biaya denda /perhari: Rp. 500\n\n");

                    totalDenda += denda;
                }
            }

            String recipientEmail = student_getEmailbyNIM(nim);
            String nama = student_getNamaByNIM(nim);

            if (recipientEmail != null) {
                String subject = "Denda Keterlambatan Pengembalian Buku";
                String body = "Yth. Kepada:\n" +
                        "Nama: " + nama + "\n" +
                        "NIM: " + nim + "\n\n" +
                        "Akun anda kami tangguhkan dikarenakan buku yang anda pinjam telah melewati tanggal pengembalian yang sudah ditentukan. " +
                        "Oleh karena itu, anda dikenai denda sebagaimana untuk rinciannya sebagai berikut:\n\n" +
                        emailBody +
                        "Total Denda: Rp. " + totalDenda + "\n\n" +
                        "Silahkan untuk segera melunasi denda yang telah tertera untuk membuka penangguhan pada akun anda. Terimakasih.\n\n" +
                        "Hormat kami,\n" +
                        "UMM Library";

                SendEmail sendEmail = new SendEmail();
                sendEmail.sendEmail(recipientEmail, subject, body);

                System.out.println("Email terkirim");
            } else {
                System.out.println("Email pada nim tidak ada " + nim);
            }

        } catch (SQLException e) {
            System.out.println("Error Database Method = book_expiredDateBorrowedBook");
        }
    }

    public static void loadBooksFromDatabase() {
        try {
            Connection conn = DriverManager.getConnection(book_database);
            String query = "SELECT book_id, title, author, category FROM borrowed_books";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            Book.arr_bookList.clear(); // Bersihkan jika sebelumnya sudah ada data

            while (rs.next()) {
                String bookId   = rs.getString("book_id");
                String title    = rs.getString("title");
                String author   = rs.getString("author");
                String category = rs.getString("category");
                int stock       = 1; // Misalnya default stock 1 karena tidak ada kolom 'stock'

                Book book = new Book(bookId, title, author, category, stock);
                Book.arr_bookList.add(book);
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}



