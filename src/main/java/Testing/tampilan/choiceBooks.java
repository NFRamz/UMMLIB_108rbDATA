package Testing.tampilan;

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

public class choiceBooks extends Application {

    // Book class (unchanged)
    public static class Book {
        private final SimpleStringProperty bookId;
        private final SimpleStringProperty title;
        private final SimpleStringProperty author;
        private final SimpleStringProperty category;
        private final SimpleIntegerProperty stock;

        public Book(String bookId, String title, String author, String category, int stock) {
            this.bookId = new SimpleStringProperty(bookId);
            this.title = new SimpleStringProperty(title);
            this.author = new SimpleStringProperty(author);
            this.category = new SimpleStringProperty(category);
            this.stock = new SimpleIntegerProperty(stock);
        }

        public String getBookId() { return bookId.get(); }
        public String getTitle() { return title.get(); }
        public String getAuthor() { return author.get(); }
        public String getCategory() { return category.get(); }
        public int getStock() { return stock.get(); }

        public SimpleStringProperty bookIdProperty() { return bookId; }
        public SimpleStringProperty titleProperty() { return title; }
        public SimpleStringProperty authorProperty() { return author; }
        public SimpleStringProperty categoryProperty() { return category; }
        public SimpleIntegerProperty stockProperty() { return stock; }

        public static ObservableList<Book> arr_bookList = FXCollections.observableArrayList();
    }

    public void choiceBooks() {
        // Background
        Image backgroundImage = new Image("file:src/main/java/image/Manage_bukuTerpinjam.png");
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitHeight(768);
        backgroundImageView.setFitWidth(1366);
        backgroundImageView.setMouseTransparent(true);

        // Header Label
        Label headerTitle = new Label("PINJAM BUKU");
        headerTitle.setTranslateX(131);
        headerTitle.setStyle("-fx-text-fill: #A91D3A;");
        headerTitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));

        // Search Field
        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Cari Judul / Penulis...");
        searchField.setStyle("-fx-background-radius: 8; -fx-padding: 8px;");
        Label searchLabel = new Label(" ");
        HBox searchBox = new HBox(10, searchLabel, searchField);
        searchBox.setAlignment(Pos.CENTER_RIGHT);
        searchBox.setPadding(new Insets(20, 20, 0, 0));

        // Input Fields and Labels
        Label bookIdLabel = new Label("Input ID buku yang ingin dipinjam:");
        bookIdLabel.setFont(Font.font("Calibri", FontWeight.NORMAL, 15));
        TextField bookIdField = new TextField();
        bookIdField.setStyle("-fx-background-radius: 8; -fx-padding: 8px;");
        bookIdField.setMaxWidth(200);

        Label durationLabel = new Label("Berapa hari ingin meminjam buku?");
        durationLabel.setFont(Font.font("Calibri", FontWeight.NORMAL, 15));
        TextField durationField = new TextField();
        durationField.setPromptText("Maks. 7 hari");
        durationField.setStyle("-fx-background-radius: 8; -fx-padding: 8px;");
        durationField.setMaxWidth(200);

        // Notification Labels
        Label errorMaxBorrowedLabel = new Label("Batas pinjam 10 buku");
        errorMaxBorrowedLabel.setVisible(false);
        errorMaxBorrowedLabel.setStyle("-fx-text-fill: #FF1E1E;");
        errorMaxBorrowedLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 15));

        Label errorDuplicateLabel = new Label("Buku sudah dipinjam.");
        errorDuplicateLabel.setVisible(false);
        errorDuplicateLabel.setStyle("-fx-text-fill: #A91D3A;");
        errorDuplicateLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 15));

        Label errorBorrowBookLabel = new Label("Max 7 hari");
        errorBorrowBookLabel.setVisible(false);
        errorBorrowBookLabel.setStyle("-fx-text-fill: #FF1E1E;");
        errorBorrowBookLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 15));

        Label borrowBookSuccessLabel = new Label("Buku berhasil dipinjam");
        borrowBookSuccessLabel.setVisible(false);
        borrowBookSuccessLabel.setStyle("-fx-text-fill: #1A4D2E;");
        borrowBookSuccessLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 15));

        Label idNotFoundLabel = new Label("ID buku tidak ditemukan");
        idNotFoundLabel.setVisible(false);
        idNotFoundLabel.setStyle("-fx-text-fill: #b11010;");
        idNotFoundLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 15));

        Label errorDurationField = new Label("Durasi tidak boleh kosong");
        errorDurationField.setVisible(false);
        errorDurationField.setStyle("-fx-text-fill: #b11010;");
        errorDurationField.setFont(Font.font("Calibri", FontWeight.BOLD, 15));

        Label sendEmailError = new Label("Tidak ada jaringan");
        sendEmailError.setVisible(false);
        sendEmailError.setStyle("-fx-text-fill: #b11010;");
        sendEmailError.setFont(Font.font("Calibri", FontWeight.BOLD, 15));

        Label waitLabel = new Label("Mohon tunggu");
        waitLabel.setVisible(false);
        waitLabel.setStyle("-fx-text-fill: #ae7805;");
        waitLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 15));

        Label bookStockEmptyLabel = new Label("Stok buku habis");
        bookStockEmptyLabel.setVisible(false);
        bookStockEmptyLabel.setStyle("-fx-text-fill: #FF1E1E;");
        bookStockEmptyLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 15));

        // TableView
        TableView<Book> tableView = new TableView<>();
        tableView.setStyle("-fx-font-size: 13px;");
        tableView.setMinWidth(1278);
        tableView.setMaxWidth(1278);
        tableView.setMinHeight(500);
        tableView.setTranslateX(0);
        tableView.setTranslateY(12);
        tableView.getStylesheets().add("file:src/main/java/css/table.css");
        tableView.setItems(Book.arr_bookList);

        TableColumn<Book, String> idColumn = new TableColumn<>("ID Buku");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        idColumn.setPrefWidth(255);

        TableColumn<Book, String> titleColumn = new TableColumn<>("Nama Buku");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setPrefWidth(255);

        TableColumn<Book, String> authorColumn = new TableColumn<>("Penulis");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        authorColumn.setPrefWidth(255);

        TableColumn<Book, String> categoryColumn = new TableColumn<>("Kategori");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryColumn.setPrefWidth(255);

        TableColumn<Book, Integer> stockColumn = new TableColumn<>("Stok");
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        stockColumn.setPrefWidth(258);

        tableView.getColumns().addAll(idColumn, titleColumn, authorColumn, categoryColumn, stockColumn);

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

        // Buttons
        Button returnButton = new Button("Kembali");
        returnButton.getStylesheets().add("file:src/main/java/css/Login_button.css");
        returnButton.setOnAction(event -> {
            Stage stage = (Stage) returnButton.getScene().getWindow();
            stage.close();
        });

        Button submitButton = new Button("Submit");
        submitButton.getStylesheets().add("file:src/main/java/css/Login_button.css");
        submitButton.setOnAction(e -> {
            // Placeholder for existing submit logic
            System.out.println("Submit: Book ID = " + bookIdField.getText() + ", Duration = " + durationField.getText());
        });

        // Layout for Input Fields
        VBox inputBox = new VBox(10, bookIdLabel, bookIdField, durationLabel, durationField);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setTranslateY(650);
        inputBox.setMaxWidth(400);

        // Layout for Notifications
        HBox notificationBox = new HBox(10, errorMaxBorrowedLabel, errorDuplicateLabel, errorBorrowBookLabel,
                borrowBookSuccessLabel, idNotFoundLabel, errorDurationField, sendEmailError, waitLabel, bookStockEmptyLabel);
        notificationBox.setAlignment(Pos.CENTER);
        notificationBox.setTranslateY(670);
        notificationBox.setTranslateX(340);
        notificationBox.setPadding(new Insets(15));

        // Layout for Table
        VBox rootTable = new VBox(28, tableView);
        rootTable.setTranslateY(137);
        rootTable.setTranslateX(46);
        rootTable.setPadding(new Insets(0));

        // Layout for Buttons
        HBox rootBtn = new HBox(10, returnButton, submitButton);
        rootBtn.setTranslateY(670);
        rootBtn.setTranslateX(340);
        rootBtn.setPadding(new Insets(15));

        // Layout for Search
        VBox searchBoxContainer = new VBox(10, searchBox);

        // Scene setup
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(backgroundImageView, headerTitle, searchBoxContainer, rootTable, inputBox, rootBtn, notificationBox);

        Scene scene = new Scene(stackPane);
        Stage choiceBooksStage = new Stage();
        choiceBooksStage.setScene(scene);
        choiceBooksStage.setTitle("UMM Library - Pilih Buku");
        choiceBooksStage.setFullScreen(true);
        choiceBooksStage.setFullScreenExitHint("");
        choiceBooksStage.show();

        System.out.println("Total Buku di List: " + Book.arr_bookList.size());
    }

    @Override
    public void start(Stage primaryStage) {
        choiceBooks();
    }

    public static void main(String[] args) {
        launch(args);
    }
}