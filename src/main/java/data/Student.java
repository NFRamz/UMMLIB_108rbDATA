package data;


import Features.Database;
import Features.DoubleClick_table;
import books.Book;

import Main.Main;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import sound.Sound;
import util.iMenu;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Stack;

public class Student extends User implements iMenu {

    public static ArrayList<UserStudent> arr_userStudent = new ArrayList<>();

    public static String[] userInfo;

    public static class UserStudent {
        String nama, nim, fakultas, prodi, email, pic;

        public UserStudent(String nama, String nim, String fakultas, String prodi, String email, String pic ) {
            this.nama       = nama;
            this.nim        = nim;
            this.fakultas   = fakultas;
            this.prodi      = prodi;
            this.email      = email;
            this.pic        = pic;
        }
    }

    @Override
    public void menu() {

        //Label
        Label sceneTitle = new Label("Student Menu");
        sceneTitle.setStyle("-fx-text-fill: #A91D3A;");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));

        String nama = Database.student_getNamaByNIM(LoginMenu.usernameField.getText());
        userInfo = new String[]{nama};
        Label nameLabel  = new Label("Halo, "+userInfo[0]);
        nameLabel.setStyle("-fx-text-fill: white;");
        nameLabel.setTranslateX(510);
        nameLabel.setTranslateY(-345);
        nameLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));


        //Button
        Button borrowBookButton = new Button("Pinjam Buku");
        borrowBookButton.setTranslateX(-83);
        borrowBookButton.setTranslateY(-65);
        borrowBookButton.getStylesheets().add("file:src/main/java/css/Login_button.css");

        Button returnBookButton = new Button("Kembalikan Buku");
        returnBookButton.setTranslateX(-83);
        returnBookButton.setTranslateY(115);
        returnBookButton.getStylesheets().add("file:src/main/java/css/Login_button.css");

        Button logoutButton = new Button("Logout");
        logoutButton.setTranslateX(-83);
        logoutButton.setTranslateY(293);
        logoutButton.getStylesheets().add("file:src/main/java/css/Login_button.css");

        //Image
        Image backgroundStudentMenu = new Image("file:src/main/java/image/studentMenu.png");
        ImageView backgroundStudentMenu_view = new ImageView(backgroundStudentMenu);


        //Table
        TableView<Book> table = new TableView<>();
        table.setTranslateX(-176);
        table.setTranslateY(70);
        table.setPrefSize(400, 500);
        table.getStylesheets().add("file:src/main/java/css/table.css");

        TableColumn<Book, String> idColumn = new TableColumn<>("ID Buku");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));

        TableColumn<Book, String> titleColumn = new TableColumn<>("Nama Buku");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setMaxWidth(500);

        TableColumn<Book, String> authorColumn = new TableColumn<>("Penulis");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<Book, String> categoryColumn = new TableColumn<>("Kategori");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Book, Integer> durationColumn = new TableColumn<>("Durasi");
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

        table.getColumns().add(idColumn);
        table.getColumns().add(titleColumn);
        table.getColumns().add(authorColumn);
        table.getColumns().add(categoryColumn);
        table.getColumns().add(durationColumn);

        for (Book i : Book.arr_borrowedBook) {
                    table.getItems().add(i);
        }


        //Grid layout
        GridPane gridButton = new GridPane();

        gridButton.setAlignment(Pos.CENTER_RIGHT);

        gridButton.add(borrowBookButton, 1, 0);
        gridButton.add(returnBookButton, 1, 1);
        gridButton.add(logoutButton, 1, 2);

        gridButton.setVgap(10);
        gridButton.setHgap(5);

        GridPane gridTable = new GridPane();
        gridTable.setAlignment(Pos.CENTER);

        gridTable.add(table, 0, 0);


        //StackPane
        StackPane stackPaneStudentMenu = new StackPane(backgroundStudentMenu_view, gridTable, gridButton, nameLabel);


        //Scene
        Scene studentmenuScene = new Scene(stackPaneStudentMenu);


        //Stage
        Stage studentMenuStage = new Stage();
        studentMenuStage.setScene(studentmenuScene);
        studentMenuStage.setTitle("UMM Library - Student Menu");
        studentMenuStage.setFullScreen(true);
        studentMenuStage.setFullScreenExitHint("");

        studentMenuStage.show();

        //hover button
        borrowBookButton.setOnMouseEntered(event -> {
            Image borrowBook_background = new Image("file:src/main/java/image/borrowBook.png");
            ImageView imageView = new ImageView(borrowBook_background);
            stackPaneStudentMenu.getChildren().set(0, imageView);
        });
        borrowBookButton.setOnMouseExited(event -> {
            ImageView view = new ImageView(backgroundStudentMenu);
            stackPaneStudentMenu.getChildren().set(0, view);
        });

        returnBookButton.setOnMouseEntered(event -> {
            Image borrow = new Image("file:src/main/java/image/returnBook.png");
            ImageView view = new ImageView(borrow);
            stackPaneStudentMenu.getChildren().set(0, view);
        });
        returnBookButton.setOnMouseExited(event -> {
            ImageView view = new ImageView(backgroundStudentMenu);
            stackPaneStudentMenu.getChildren().set(0, view);
        });

        logoutButton.setOnMouseEntered(event -> {
            Image borrow = new Image("file:src/main/java/image/logout.png");
            ImageView view = new ImageView(borrow);
            stackPaneStudentMenu.getChildren().set(0, view);
        }); 
        logoutButton.setOnMouseExited(event -> {
            ImageView view = new ImageView(backgroundStudentMenu);
            stackPaneStudentMenu.getChildren().set(0, view);
        });

        //Action button
        borrowBookButton.setOnAction(event -> {
            choiceBooks();
            studentMenuStage.close();
        });

        returnBookButton.setOnAction(event -> {
            returnBooks();
            studentMenuStage.close();
        });


        logoutButton.setOnAction(event -> {
            System.out.println("Logout dimulai...");

            // 1. Segera buka halaman login dan tutup halaman lama (student menu)
            try {
                Main mainobj = new Main();
                mainobj.start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            studentMenuStage.close(); // Langsung ditutup tanpa menunggu proses di belakang

            // 2. Jalankan proses simpan data di background
            Task<Void> backgroundLogoutTask = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    String nim = LoginMenu.usernameField.getText();

                    System.out.println("Menyimpan atau menghapus data peminjaman...");
                    Database.book_saveOrDeleteBorrowedBooks(nim);
                    System.out.println("Selesai menyimpan/menghapus peminjaman.");

                    System.out.println("Mengupdate stok buku...");
                    Database.book_updateBookStock();
                    System.out.println("Stok buku terupdate.");

                    System.out.println("Membersihkan data ArrayList...");
                    Book.arr_borrowedBook.clear();
                    Book.arr_bookList.clear();
                    System.out.println("ArrayList dibersihkan.");

                    return null;
                }
            };

            backgroundLogoutTask.setOnSucceeded(e -> {
                System.out.println("Proses backend logout selesai.");
            });

            backgroundLogoutTask.setOnFailed(e -> {
                System.out.println("Terjadi kesalahan saat proses backend logout: " + backgroundLogoutTask.getException().getMessage());
            });

            Thread backgroundThread = new Thread(backgroundLogoutTask);
            backgroundThread.setDaemon(true);
            backgroundThread.start();
        });




    }

    @Override
    public void choiceBooks() {
        super.choiceBooks();
    }

    public static void returnBooks() {


        Stage returnBooksStage = new Stage();
        returnBooksStage.setTitle("UMM Library - Pengembalian buku");

        //Background
        Image backgroundImage = new Image("file:src/main/java/image/Pengembalian_buku.png");
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


        //Label
        Label bookIdLabel = new Label("Inputkan ID buku yang ingin dikembalikan");
        bookIdLabel.setPadding(new Insets(10, 0, 0, 0));
        //Notification Label
        Label submitSuccesLabel = new Label("Pengembalian berhasil");
        Label submitFailedLabel = new Label("Pengembalian gagal");
        Group notificationGroup = new Group(submitSuccesLabel, submitFailedLabel);

        //Font Style
        bookIdLabel.setFont(Font.font("Calibri Body", FontWeight.NORMAL, 15));
        submitFailedLabel.setFont(Font.font("Calibri Body", FontWeight.BOLD, 15));
        submitSuccesLabel.setFont(Font.font("Calibri Body", FontWeight.BOLD, 15));

        //Font Color
        submitSuccesLabel.setStyle("-fx-text-fill: #16FF00;");
        submitFailedLabel.setStyle("-fx-text-fill: #FF1E1E;");

        //Notification label settings
        submitSuccesLabel.setVisible(false);
        submitFailedLabel.setVisible(false);

        //Field
        TextField bookIdField = new TextField();

        //Button
        Button submitButton = new Button("Submit");
        submitButton.getStylesheets().add("file:src/main/java/css/Login_button.css");

        Button returnButton = new Button("Kembali");
        returnButton.getStylesheets().add("file:src/main/java/css/Login_button.css");
        //Table label
        TableView<Book> returnBookTable = new TableView<>();
        returnBookTable.setStyle("-fx-font-size: 13px;");
        returnBookTable.setMinWidth(1200);
        returnBookTable.setMaxWidth(1200);
        returnBookTable.setMinHeight(400);
        returnBookTable.setTranslateX(0);
        returnBookTable.setTranslateY(12);
        returnBookTable.getStylesheets().add("file:src/main/java/css/table.css");

        TableColumn<Book, String> idBookColumn = new TableColumn<>("ID Buku");
        idBookColumn.setPrefWidth(210);

        TableColumn<Book, String> titleBookColumn = new TableColumn<>("Judul");
        titleBookColumn.setPrefWidth(380);

        TableColumn<Book, String> authorBookColumn = new TableColumn<>("Author");
        authorBookColumn.setPrefWidth(230);

        TableColumn<Book, String> categoryBookColumn = new TableColumn<>("Kategori");
        categoryBookColumn.setPrefWidth(210);

        TableColumn<Book, String> durationBookColumn = new TableColumn<>("Durasi Pinjaman (Hari)");
        durationBookColumn.setPrefWidth(150);

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
            returnBookTable.setItems(filteredList);
        });

        HBox  btn = new HBox(683,returnButton, submitButton);
        btn.setPadding(new Insets(20, 0, 0, 0));
        btn.setAlignment(Pos.CENTER);

        VBox searchBoxContainer = new VBox(10, searchBox);
        searchBoxContainer.setTranslateX(80);
        searchBoxContainer.setTranslateY(-70);

        HBox notificationBox = new HBox(683,notificationGroup);
        notificationBox.setAlignment(Pos.CENTER);



        //Grid layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        grid.add(searchBoxContainer, 0, 0);
        grid.add(returnBookTable, 0, 1);

        grid.add(bookIdLabel, 0, 2);
        grid.add(bookIdField, 0, 3);

        grid.add(notificationBox, 0, 4);

        grid.add(btn, 0, 4);

        grid.setVgap(10);
        grid.setHgap(5);


        //submitButton.setTranslateX(348);
        submitSuccesLabel.setTranslateX(127);
        submitFailedLabel.setTranslateX(127);

        StackPane stackPane = new StackPane(backgroundImageView, grid);
        Scene returnBookScene = new Scene(stackPane);
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

}
