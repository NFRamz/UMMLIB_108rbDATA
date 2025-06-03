package Testing.tampilan;

import Features.DoubleClick_table;
import books.Book;
import data.Student;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class KembalikanBuku extends Application {
    public static void returnBooks() {

        Stage returnBooksStage = new Stage();
        returnBooksStage.setTitle("UMM Library - Pengembalian buku");

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

        Scene returnBookScene = new Scene(grid);
        returnBooksStage.setFullScreen(true);
        returnBooksStage.setFullScreenExitHint("");
        returnBooksStage.setScene(returnBookScene);
        returnBooksStage.show();

        //Action button
        //new
        DoubleClick_table.setupDragAndDrop(idBookColumn, bookIdField);

        submitButton.setOnAction(event -> {
            if(bookIdField.getText().isEmpty()) {
                submitFailedLabel.setVisible(true);
                submitSuccesLabel.setVisible(false);
                return;
            }

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
    public void start(Stage primaryStage) {
        returnBooks();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
