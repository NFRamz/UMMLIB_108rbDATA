package data;


import Features.Database;
import Features.DoubleClick_table;
import books.Book;

import Main.Main;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
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
            String nim = LoginMenu.usernameField.getText();

            try {
                Database.book_saveOrDeleteBorrowedBooks(LoginMenu.usernameField.getText());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            Database.book_updateBookStock();
            Book.arr_borrowedBook.clear();
            Book.arr_bookList.clear();

            Main mainobj = new Main();
            mainobj.start(new Stage());
            studentMenuStage.close();
        });



    }

    @Override
    public void choiceBooks() {
        super.choiceBooks();
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

}
