package Testing.tampilan;

import Features.Database;
import Features.DoubleClick_table;
import Features.SendEmail;
import books.Book;
import data.LoginMenu;
import data.Student;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.Stack;

public class pinjamBuku extends Application {
    public void choiceBooks() {
        Book bookObj = new Book();
        Student studentObj = new Student();

        //Background
        Image backgroundImage = new Image("file:src/main/java/image/listBuku.png");
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitHeight(768);
        backgroundImageView.setFitWidth(1366);
        backgroundImageView.setMouseTransparent(true);

        // Search Field
        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Cari Judul / Penulis...");
        searchField.setStyle("-fx-background-radius: 8; -fx-padding: 8px;");
        Label searchLabel = new Label(" ");
        HBox searchBox = new HBox(10, searchLabel, searchField);
        searchBox.setAlignment(Pos.CENTER_RIGHT);
        searchBox.setPadding(new Insets(20, 20, 0, 0));


        Label bookIdLabel = new Label("Input ID buku yang ingin dipinjam:");
        bookIdLabel.setPadding(new Insets(10, 0, 0, 0));

        Label durationLabel = new Label("Berapa hari ingin meminjam buku?");


        //Label notification

        Label errorMaxBorrowedLabel = new Label("Batas pinjam 10 buku");
        errorMaxBorrowedLabel.setVisible(false);
        errorMaxBorrowedLabel.setTranslateX(120);
        errorMaxBorrowedLabel.setStyle("-fx-text-fill: #FF1E1E;");
        errorMaxBorrowedLabel.setFont(Font.font("Calibri Body", FontWeight.BOLD, 15));

        Label errorDuplicateLabel = new Label("Buku sudah dipinjam.");
        errorDuplicateLabel.setVisible(false);
        errorDuplicateLabel.setTranslateX(120);
        errorDuplicateLabel.setStyle("-fx-text-fill: #A91D3A;");
        errorDuplicateLabel.setFont(Font.font("Calibri Body", FontWeight.BOLD, 15));

        Label errorborrowBookLabel = new Label("Max 7 hari");
        errorborrowBookLabel.setVisible(false);
        errorborrowBookLabel.setTranslateX(151);
        errorborrowBookLabel.setStyle("-fx-text-fill: #FF1E1E;");
        errorborrowBookLabel.setFont(Font.font("Calibri Body", FontWeight.BOLD, 15));

        Label borrowBookSuccesLabel = new Label("Buku berhasil dipinjam");
        borrowBookSuccesLabel.setVisible(false);
        borrowBookSuccesLabel.setTranslateX(120);
        borrowBookSuccesLabel.setStyle("-fx-text-fill: #1A4D2E;");
        borrowBookSuccesLabel.setFont(Font.font("Calibri Body", FontWeight.BOLD, 15));

        Label idNotFoundLabel = new Label("ID buku tidak ditemukan");
        idNotFoundLabel.setVisible(false);
        idNotFoundLabel.setStyle("-fx-text-fill: #b11010;");
        idNotFoundLabel.setTranslateX(120);
        idNotFoundLabel.setFont(Font.font("Calibri Body", FontWeight.BOLD, 15));

        Label errorDurationField = new Label("Durasi tidak boleh kosong");
        errorDurationField.setVisible(false);
        errorDurationField.setStyle("-fx-text-fill: #b11010;");
        errorDurationField.setTranslateX(120);
        errorDurationField.setFont(Font.font("Calibri Body", FontWeight.BOLD, 15));

        Label sendEmailError = new Label("Tidak ada jaringan");
        sendEmailError.setVisible(false);
        sendEmailError.setTranslateX(120);
        sendEmailError.setStyle("-fx-text-fill: #b11010;");
        sendEmailError.setFont(Font.font("Calibri Body", FontWeight.BOLD, 15));

        Label waitLabel = new Label("Mohon tunggu");
        waitLabel.setVisible(false);
        waitLabel.setTranslateX(140);
        waitLabel.setStyle("-fx-text-fill: #ae7805;");
        waitLabel.setFont(Font.font("Calibri Body", FontWeight.BOLD, 15));

        Label bookStockEmptyLabel = new Label("Stok buku habis");
        bookStockEmptyLabel.setVisible(false);
        bookStockEmptyLabel.setTranslateX(135);
        bookStockEmptyLabel.setStyle("-fx-text-fill: #FF1E1E;");
        bookStockEmptyLabel.setFont(Font.font("Calibri Body", FontWeight.BOLD, 15));

        Group notificationGroup = new Group();
        notificationGroup.getChildren().addAll(
                errorborrowBookLabel,
                errorDurationField,
                errorDuplicateLabel,
                borrowBookSuccesLabel,
                idNotFoundLabel,
                sendEmailError,
                errorMaxBorrowedLabel,
                bookStockEmptyLabel,
                waitLabel
        );

        //Field
        TextField bookIdField = new TextField();

        TextField durationField = new TextField();
        durationField.setPromptText("Maks. 7 hari");

        //Button
        Button submitButton = new Button("Submit");
        submitButton.getStylesheets().add("file:src/main/java/css/Login_button.css");
        //submitButton.setTranslateX(348);

        Button returnButton = new Button("Kembali");
        returnButton.getStylesheets().add("file:src/main/java/css/Login_button.css");

        //Table
        TableView<Book> tableView = new TableView<>();
        tableView.setStyle("-fx-font-size: 13px;");
        tableView.setMinWidth(1200);
        tableView.setMaxWidth(1200);
        tableView.setMinHeight(400);
        tableView.setTranslateX(0);
        tableView.setTranslateY(12);
        tableView.getStylesheets().add("file:src/main/java/css/table.css");


        TableColumn<Book, String> idColumn       = new TableColumn<>("ID Buku");
        TableColumn<Book, String> titleColumn    = new TableColumn<>("Nama Buku");
        TableColumn<Book, String> authorColumn   = new TableColumn<>("Penulis");
        TableColumn<Book, String> categoryColumn = new TableColumn<>("Kategori");
        TableColumn<Book, Integer> stockColumn   = new TableColumn<>("Stok");

        tableView.getColumns().add(idColumn);
        tableView.getColumns().add(titleColumn);
        tableView.getColumns().add(authorColumn);
        tableView.getColumns().add(categoryColumn);
        tableView.getColumns().add(stockColumn);

        idColumn.setPrefWidth(210);
        titleColumn.setPrefWidth(380);
        authorColumn.setPrefWidth(230);
        categoryColumn.setPrefWidth(210);
        stockColumn.setPrefWidth(150);


        idColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));


        for (Book i : Book.arr_bookList) {
            tableView.getItems().add(i);

        }

        // Search Logic
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String filter = newVal.toLowerCase();
            ObservableList<Book> filteredList = FXCollections.observableArrayList();

            if (filter.isEmpty()) {
                filteredList.addAll(Book.arr_bookList);
            } else {
                for (Book book : Book.arr_bookList) {
                    if (book.getTitle().toLowerCase().contains(filter) || book.getAuthor().toLowerCase().contains(filter)) {
                        filteredList.add(book);
                    }
                }
            }
            tableView.setItems(filteredList);
        });

        /*
        // Table
        TableView<Book> tableView = new TableView<>();
        tableView.setPrefWidth(600);    // Lebar preferensi
        tableView.setMinWidth(400);     // Lebar minimum
        tableView.setMaxWidth(800);     // Lebar maksimum

        TableColumn<Book, String> idColumn       = new TableColumn<>("ID Buku");
        TableColumn<Book, String> titleColumn    = new TableColumn<>("Nama Buku");
        TableColumn<Book, String> authorColumn   = new TableColumn<>("Penulis");
        TableColumn<Book, String> categoryColumn = new TableColumn<>("Kategori");
        TableColumn<Book, Integer> stockColumn   = new TableColumn<>("Stok");

        // Tambahkan kolom ke tabel
        tableView.getColumns().addAll(idColumn, titleColumn, authorColumn, categoryColumn, stockColumn);

        // Atur property value factory
        idColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        // Gunakan ObservableList langsung tanpa for-loop
        tableView.setItems(Book.arr_bookList);
        */

        System.out.println("Total Buku di List: " + Book.arr_bookList.size());

        HBox  btn = new HBox(683,returnButton, submitButton);
        btn.setAlignment(Pos.CENTER);

        HBox notificationBox = new HBox(683,notificationGroup);
        notificationBox.setAlignment(Pos.CENTER);

        //Grid layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        VBox searchBoxContainer = new VBox(10, searchBox);
        searchBoxContainer.setTranslateX(80);
        searchBoxContainer.setTranslateY(-40);
        grid.add(searchBoxContainer,0,0);
        grid.add(tableView,0,1);

        grid.add(bookIdLabel,  0, 2);
        grid.add(bookIdField, 0, 3);

        grid.add(durationLabel,0,4);

        grid.add(durationField, 0,5);

        grid.add(notificationBox,0,6);
        grid.add(btn,0,6);



        grid.setHgap(5);
        grid.setVgap(10);


        //Scene
        Scene scene = new Scene(grid);

        //Stage
        Stage choiceBooksStage = new Stage();

        choiceBooksStage.setScene(scene);

        choiceBooksStage.setTitle("UMM library - Pilih Buku");
        choiceBooksStage.setFullScreen(true);
        choiceBooksStage.setFullScreenExitHint("");

        choiceBooksStage.show();


        //Features
        DoubleClick_table.setupDragAndDrop(idColumn, bookIdField);


        //Action button
        submitButton.setOnAction(event -> {

            errorDurationField.setVisible(false);
            errorMaxBorrowedLabel.setVisible(false);
            sendEmailError.setVisible(false);
            errorborrowBookLabel.setVisible(false);
            borrowBookSuccesLabel.setVisible(false);
            bookStockEmptyLabel.setVisible(false);
            idNotFoundLabel.setVisible(false);
            errorDuplicateLabel.setVisible(false);
            waitLabel.setVisible(false);

            if (Book.arr_borrowedBook.size() >= 10) {
                errorMaxBorrowedLabel.setVisible(true);
                return;
            }

            for (Book i : Book.arr_borrowedBook) {
                if (bookIdField.getText().equals(i.getBookId())) {
                    errorDuplicateLabel.setVisible(true);
                    return;
                }
            }

            if (durationField.getText().isEmpty()) {
                errorDurationField.setVisible(true);
                return;
            }

            int inputWaktuPinjaman = Integer.parseInt(durationField.getText());
            if (inputWaktuPinjaman <= 0 || inputWaktuPinjaman > 7) {
                errorborrowBookLabel.setVisible(true);
                return;
            }

            boolean bookFound = false;
            for (Book i : Book.arr_bookList) {
                if (i.getBookId().equals(bookIdField.getText())) {
                    bookFound = true;
                    if (i.getStock() == 0) {
                        bookStockEmptyLabel.setVisible(true);
                        return;
                    }

                    int stock = i.getStock();
                    stock--;
                    i.setStock(stock);
                    i.setDuration(inputWaktuPinjaman);

                    SendEmail sendEmail = new SendEmail();
                    String nim = LoginMenu.usernameField.getText();
                    String returnDate = Database.student_displayReturnTimeForBorrowedBook(inputWaktuPinjaman);

                    try {
                        String recipientEmail = Database.student_getEmailbyNIM(nim);

                        String subject = "Peminjaman Buku Berhasil!";
                        String body = "Terimakasih telah berkunjung ke perpustakaan pusat UMM.\n"
                                + "Berikut lampiran tentang buku yang telah dipinjam :\n\n"
                                + "Book ID    : " + i.getBookId() + "\n"
                                + "Title      : " + i.getTitle() + "\n"
                                + "Category   : " + i.getCategory() + "\n"
                                + "Duration of borrowing : " + inputWaktuPinjaman + " days\n\n"
                                + "Batas pengembalian   : " + returnDate + "\n\n"
                                + sendEmail.dateinfo_now();

                        waitLabel.setVisible(true);

                        Platform.runLater(() -> {
                            try {
                                sendEmail.sendEmail(recipientEmail, subject, body);
                                Book.arr_borrowedBook.add(new Book(nim, bookIdField.getText(), i.getTitle(), i.getAuthor(), i.getCategory(), i.getDuration()));
                                //showAlert("Berhasil", "Buku berhasil dipinjam. Silakan cek email Anda untuk detail peminjaman.");
                                tableView.refresh();

                                borrowBookSuccesLabel.setVisible(true);
                                waitLabel.setVisible(false);
                            } catch (Exception e) {
                                int returnStock = i.getStock();
                                returnStock++;
                                i.setStock(returnStock);

                                sendEmailError.setVisible(true);
                                waitLabel.setVisible(false);
                            }
                        });

                    } catch (SQLException e) {
                        System.out.println("Terjadi kesalahan saat mencari email untuk NIM " + nim + ": " + e.getMessage());
                    }

                    break;
                }
            }

            if (!bookFound) {
                idNotFoundLabel.setVisible(true);
            }

        });

        returnButton.setOnAction(event -> {
            studentObj.menu();
            choiceBooksStage.close();
        });

    }

    @Override
    public void start(Stage primaryStage) {
        choiceBooks();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

