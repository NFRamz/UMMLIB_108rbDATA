
package Testing.Backup;

import Features.Database;
import Features.DoubleClick_table;

import Testing.tampilan.choiceBooks;
import books.*;
import data.Admin;
import data.LoginMenu;
import data.Student;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import Features.SendEmail;

import java.sql.SQLException;


public class Student_Backup {

//====================================== METHOD =======================================

    //Method yang digunakan untuk meminjam buku
    public void choiceBooks() {
        Book bookObj = new Book();
        Student studentObj = new Student();

        //Background
        Image backgroundImage = new Image("file:src/main/java/image/listBuku.png");
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitHeight(768);
        backgroundImageView.setFitWidth(1366);
        backgroundImageView.setMouseTransparent(true);

        //Label
        Label headerTitle = new Label("PINJAM BUKU");
        headerTitle.setTranslateX(131);
        headerTitle.setStyle("-fx-text-fill: #A91D3A;");
        headerTitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));

        Label bookIdLabel = new Label("Input ID buku yang ingin dipinjam:");

        Label durationLabel = new Label("Berapa hari ingin meminjam buku?");

        // Search Field
        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Cari Judul / Penulis...");
        searchField.setStyle("-fx-background-radius: 8; -fx-padding: 8px;");
        Label searchLabel = new Label(" ");
        HBox searchBox = new HBox(10, searchLabel, searchField);
        searchBox.setAlignment(Pos.CENTER_RIGHT);
        searchBox.setPadding(new Insets(20, 20, 0, 0));

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

        //Field
        TextField bookIdField = new TextField();

        TextField durationField = new TextField();
        durationField.setPromptText("Maks. 7 hari");

        //Button
        Button submitButton = new Button("Submit");
        submitButton.setTranslateX(348);

        Button returnButton = new Button("Kembali");


        //Table
        TableView<Book> tableView = new TableView<>();
        tableView.setStyle("-fx-font-size: 13px;");
        tableView.setMinWidth(1278);
        tableView.setMaxWidth(1278);
        tableView.setMinHeight(500);
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

        idColumn.setPrefWidth(200);
        titleColumn.setPrefWidth(200);
        authorColumn.setPrefWidth(200);
        categoryColumn.setPrefWidth(200);
        categoryColumn.setPrefWidth(110);
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

        //Grid layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        grid.add(headerTitle,0,0);
        grid.add(tableView,0,1);

        grid.add(bookIdLabel,  0, 2);
        grid.add(bookIdField, 0, 3);

        grid.add(durationLabel,0,4);

        grid.add(durationField, 0,5);

        grid.add(submitButton,0,6);
        grid.add(returnButton,0,6);

        grid.add(errorDurationField,0,6);
        grid.add(errorDuplicateLabel,0,6);
        grid.add(errorborrowBookLabel,0,6);
        grid.add(borrowBookSuccesLabel,0,6);
        grid.add(bookStockEmptyLabel, 0,6);
        grid.add(idNotFoundLabel,0,6);
        grid.add(sendEmailError,0,6);
        grid.add(errorMaxBorrowedLabel,0,6);
        grid.add(waitLabel,0,6);

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
                                showAlert("Berhasil", "Buku berhasil dipinjam. Silakan cek email Anda untuk detail peminjaman.");
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
    public static void returnBooks() {

        Stage returnBooksStage = new Stage();
        returnBooksStage.setTitle("UMM Library - Pengembalian buku");

        //Label
        Label headerTitle = new Label("PENGEMBALIAN BUKU");
        Label bookIdLabel = new Label("Inputkan ID buku yang ingin dikembalikan");

        //Notification Label
        Label submitSuccesLabel = new Label("Pengembalian berhasil");
        Label submitFailedLabel = new Label("Pengembalian gagal");

        //Font Style
        headerTitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        bookIdLabel.setFont(Font.font("Calibri Body", FontWeight.NORMAL, 15));
        submitFailedLabel.setFont(Font.font("Calibri Body", FontWeight.BOLD, 15));
        submitSuccesLabel.setFont(Font.font("Calibri Body", FontWeight.BOLD, 15));

        //Font Color
        headerTitle.setStyle("-fx-text-fill: #A91D3A;");
        submitSuccesLabel.setStyle("-fx-text-fill: #16FF00;");
        submitFailedLabel.setStyle("-fx-text-fill: #FF1E1E;");

        //Notification label settings
        submitSuccesLabel.setVisible(false);
        submitFailedLabel.setVisible(false);

        //Field
        TextField bookIdField = new TextField();

        //Button
        Button submitButton = new Button("Submit");
        Button returnButton = new Button("Kembali");

        //Table label
        TableView<Book> returnBookTable = new TableView<>();

        TableColumn<Book, String> idBookColumn = new TableColumn<>("ID Buku");
        TableColumn<Book, String> titleBookColumn = new TableColumn<>("Judul");
        TableColumn<Book, String> authorBookColumn = new TableColumn<>("Author");
        TableColumn<Book, String> categoryBookColumn = new TableColumn<>("Kategori");
        TableColumn<Book, String> durationBookColumn = new TableColumn<>("Durasi Pinjaman (Hari)");

        idBookColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        titleBookColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorBookColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        categoryBookColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        durationBookColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

        returnBookTable.getColumns().add(idBookColumn);
        returnBookTable.getColumns().add(titleBookColumn);
        returnBookTable.getColumns().add(authorBookColumn);
        returnBookTable.getColumns().add(categoryBookColumn);
        returnBookTable.getColumns().add(durationBookColumn);

        //Tabel access array arr_borrowedBook
        for (Book i : Book.arr_borrowedBook) {
            for (Book j : Book.arr_bookList) {
                if (i.getBookId().equals(j.getBookId())) {
                    j.setDuration(i.getDuration());
                    returnBookTable.getItems().add(j);
                    break;
                }
            }
        }

        //Grid layout
        GridPane grid = new GridPane();

        grid.setAlignment(Pos.CENTER);

        grid.add(headerTitle, 0, 0);
        grid.add(returnBookTable, 0, 1);

        grid.add(bookIdLabel, 0, 2);
        grid.add(bookIdField, 0, 3);

        grid.add(returnButton, 0, 4);
        grid.add(submitButton, 0, 4);

        grid.add(submitSuccesLabel, 0, 4);
        grid.add(submitFailedLabel, 0, 4);


        grid.setVgap(10);
        grid.setHgap(5);

        headerTitle.setTranslateX(97);
        submitButton.setTranslateX(348);
        submitSuccesLabel.setTranslateX(127);
        submitFailedLabel.setTranslateX(127);

        Scene returnBookScene = new Scene(grid);
        returnBooksStage.setFullScreen(true);
        returnBooksStage.setFullScreenExitHint("");
        returnBooksStage.setScene(returnBookScene);
        returnBooksStage.show();

        //Action button
        //new
        DoubleClick_table.setupDragAndDrop(idBookColumn, bookIdField);

        submitButton.setOnAction(event -> {

            boolean validasiReturnBooks = false;
            //For dengan variabel i yang membaca ukuran arraylist arr_borrowedBook.
            for (int i = 0; i < Book.arr_borrowedBook.size(); i++) {

                //If untuk membandingkan variabel idBukuYangDipinjam dengan id yang ada di arr_borrowedBook.
                if (Book.arr_borrowedBook.get(i).getBookId().equals(bookIdField.getText())) {
                    for (Book book : Book.arr_bookList) {
                        if (book.getBookId().equals(bookIdField.getText())) {

                            int returnBook = book.getStock();
                            returnBook++;
                            book.setStock(returnBook);

                            Book.arr_borrowedBook.remove(i);

                            validasiReturnBooks = true;
                            break;

                        }
                    }
                }
            }
            if (validasiReturnBooks) {
                returnBookTable.refresh();
                submitSuccesLabel.setVisible(true);
                submitFailedLabel.setVisible(false);

            } else {

                submitFailedLabel.setVisible(true);
                submitSuccesLabel.setVisible(false);
            }
        });

        returnButton.setOnAction(event -> {
            Student studentObj = new Student();

            studentObj.menu();
            returnBooksStage.close();

        });

    }

    public void inputBook() {
        Book  textBookObj    = new TextBook();
        Book  storyBookObj   = new StoryBook();
        Book  historyBookObj = new HistoryBook();



        //Label
        Label sceneTitle = new Label("Tambah Buku");
        Label chooseBook = new Label("Pilih kategori buku:");

        //Font Label style
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        chooseBook.setFont(Font.font("Calibri Body", FontWeight.NORMAL, 15));

        //Font label color
        sceneTitle.setStyle("-fx-text-fill: #A91D3A;");

        //Button
        Button historyBookButton  = new Button("History Book");
        Button storyBookButton    = new Button("Story Book");
        Button textBookButton     = new Button("Text Book");

        //Grid layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        grid.add(sceneTitle, 0,0);
        grid.add(chooseBook, 0,1);
        grid.add(historyBookButton, 2, 0 );
        grid.add(storyBookButton, 2, 1 );
        grid.add(textBookButton, 2, 2 );

        grid.setVgap(10);
        grid.setHgap(5);

        Scene scene = new Scene(grid);
        Stage inputBookStage = new Stage();

        inputBookStage.setTitle("UMM Library - Input Book");
        inputBookStage.setScene(scene);
        inputBookStage.setFullScreen(true);
        inputBookStage.setFullScreenExitHint("");

        inputBookStage.show();


        //Action Button
        historyBookButton.setOnAction(event -> {
            addBook(historyBookObj, "UMM Library - Add History Book ", "History Book");
            inputBookStage.close();
        });

        storyBookButton.setOnAction(event -> {
            addBook(storyBookObj, "UMM Library - Add Story Book ", "Story Book");
            inputBookStage.close();
        });

        textBookButton.setOnAction(event -> {
            addBook(textBookObj, "UMM Library - Add Text Book", "Text Book");
            inputBookStage.close();
        });

    }
    public void addBook(Book genreBook, String addBookStageTitle, String addBookSceneTitle) {
        Admin adminObj       = new Admin();
        Book  bookObj        = new Book();

        Stage addbookStage = new Stage();
        addbookStage.setTitle(addBookStageTitle);

        //Label
        Label sceneTitleLabel= new Label(addBookSceneTitle);
        sceneTitleLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        sceneTitleLabel.setStyle("-fx-text-fill: #A91D3A;");

        Label bookIdLabel    = new Label("ID Buku    :");
        bookIdLabel.setFont(Font.font("Calibri Body", FontWeight.NORMAL, 15));

        Label bookTitleLabel = new Label("Judul Buku :");
        bookTitleLabel.setFont(Font.font("Calibri Body", FontWeight.NORMAL, 15));

        Label authorLabel    = new Label("Penulis    :");
        authorLabel.setFont(Font.font("Calibri Body", FontWeight.NORMAL, 15));

        Label stockLabel     = new Label("Stok       :");
        stockLabel.setFont(Font.font("Calibri Body", FontWeight.NORMAL, 15));

        //Label notification
        Label errorMessageLabel = new Label("Stok harus berupa angka");
        errorMessageLabel.setVisible(false);
        errorMessageLabel.setStyle("-fx-text-fill: #FF1E1E;");


        //Field
        TextField bookIdField    = new TextField(adminObj.generateId());
        TextField bookTitleField =  new TextField();
        TextField authorField    = new TextField();
        TextField stockField     = new TextField();


        //Button
        Button submitButton = new Button("Submit");

        Button returnButton = new Button("Kembali");
        returnButton.setTranslateX(-22);

        //Grid layout
        GridPane gridAddBook = new GridPane();
        gridAddBook.setAlignment(Pos.CENTER);

        gridAddBook.add(sceneTitleLabel, 1,0);
        gridAddBook.add(bookIdLabel, 0,1);
        gridAddBook.add(bookTitleLabel,0,2);
        gridAddBook.add(authorLabel, 0,3);
        gridAddBook.add(stockLabel, 0,4);
        gridAddBook.add(errorMessageLabel, 0, 5);

        gridAddBook.add(bookIdField,2,1);
        gridAddBook.add(bookTitleField, 2,2);
        gridAddBook.add(authorField, 2,3);
        gridAddBook.add(stockField,2,4);

        gridAddBook.add(submitButton,3,5);
        gridAddBook.add(returnButton, 2,5);


        Scene addbookScene = new Scene(gridAddBook);
        addbookStage.setScene(addbookScene);
        addbookStage.setFullScreen(true);
        addbookStage.setFullScreenExitHint("");
        addbookStage.show();

        //Action button
        submitButton.setOnAction(event -> {
            //add book to array
            try {
                errorMessageLabel.setVisible(false);

                bookObj.setBookId(bookIdField.getText());
                bookObj.setTitle(bookTitleField.getText());
                genreBook.setCategory(sceneTitleLabel.getText());
                bookObj.setAuthor(authorField.getText());
                bookObj.setStock(Integer.parseInt(stockField.getText()));

                Database.admin_addBook(bookObj.getBookId(), bookObj.getTitle(), bookObj.getAuthor(), genreBook.getCategory(), bookObj.getStock());

                adminObj.menu();
                addbookStage.close();
            }catch (Exception e){
                errorMessageLabel.setVisible(true);
                addbookStage.show();
            }
        });

    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.setHeaderText(title);
        alert.showAndWait();
    }


}




