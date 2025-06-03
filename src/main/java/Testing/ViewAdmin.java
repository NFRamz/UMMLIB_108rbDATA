package Testing;

import data.Admin;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.*;

public class ViewAdmin extends Application {

    // Inner class for Admin
    public static class Admin {
        private final SimpleStringProperty username;
        private final SimpleStringProperty password;

        public Admin(String username, String password) {
            this.username = new SimpleStringProperty(username);
            this.password = new SimpleStringProperty(password);
        }

        public String getUsername() { return username.get(); }
        public String getPassword() { return password.get(); }

        public void setUsername(String username) { this.username.set(username); }
        public void setPassword(String password) { this.password.set(password); }

        public SimpleStringProperty usernameProperty() { return username; }
        public SimpleStringProperty passwordProperty() { return password; }
    }

    private final ObservableList<Admin> adminList = FXCollections.observableArrayList();
    private final String dbUrl = "jdbc:sqlite:src/main/java/.database/User_database";

    private void loadAdminsFromDatabase() {
        String sql = "SELECT * FROM admin_credentials";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            adminList.clear(); // Clear existing data
            while (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                adminList.add(new Admin(username, password));
            }
            System.out.println("DEBUG - Ukuran ObservableList adminList: " + adminList.size());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveAdminsToDatabase() {
        String sql = "UPDATE admin_credentials SET password = ? WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (Admin admin : adminList) {
                pstmt.setString(1, admin.getPassword());
                pstmt.setString(2, admin.getUsername());
                pstmt.executeUpdate();
            }
            System.out.println("Data admins berhasil disimpan ke database.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        // Load data from database
        loadAdminsFromDatabase();

        // Background
        Image backgroundImage = new Image("file:src/main/java/image/Manage_bukuTerpinjam.png");
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitHeight(768);
        backgroundImageView.setFitWidth(1366);
        backgroundImageView.setMouseTransparent(true);

        // Search Field
        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Cari Username...");
        searchField.setStyle("-fx-background-radius: 8; -fx-padding: 8px;");
        Label searchLabel = new Label(" ");
        HBox searchBox = new HBox(10, searchLabel, searchField);
        searchBox.setAlignment(Pos.CENTER_RIGHT);
        searchBox.setPadding(new Insets(20, 20, 0, 0));

        // TableView
        TableView<Admin> tableView = new TableView<>();
        tableView.setStyle("-fx-font-size: 13px;");
        tableView.setMinWidth(1278);
        tableView.setMaxWidth(1278);
        tableView.setMinHeight(500);
        tableView.setTranslateX(0);
        tableView.setTranslateY(12);
        tableView.getStylesheets().add("file:src/main/java/css/table.css");
        tableView.setEditable(true);
        tableView.setItems(adminList);

        // Table Columns
        TableColumn<Admin, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(cell -> cell.getValue().usernameProperty());
        usernameCol.setPrefWidth(639); // Half of 1278

        TableColumn<Admin, String> passwordCol = new TableColumn<>("Password");
        passwordCol.setCellValueFactory(cell -> cell.getValue().passwordProperty());
        passwordCol.setCellFactory(TextFieldTableCell.forTableColumn());
        passwordCol.setOnEditCommit(e -> e.getRowValue().setPassword(e.getNewValue()));
        passwordCol.setPrefWidth(639); // Half of 1278

        tableView.getColumns().addAll(usernameCol, passwordCol);

        // Buttons
        Button returnButton = new Button("Kembali");
        returnButton.getStylesheets().add("file:src/main/java/css/Login_button.css");
        returnButton.setOnAction(event -> {
            data.Admin adminObj = new data.Admin();
            adminObj.menu();
            primaryStage.close();
        });

        Button btn_muatUlang = new Button("Muat Ulang");
        btn_muatUlang.getStylesheets().add("file:src/main/java/css/Login_button.css");
        btn_muatUlang.setOnAction(e -> {
            loadAdminsFromDatabase();
            tableView.refresh();
        });

        Button btn_update = new Button("Simpan Perubahan");
        btn_update.getStylesheets().add("file:src/main/java/css/Login_button.css");
        btn_update.setOnAction(e -> {
            saveAdminsToDatabase();
        });

        // Search Logic
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String filter = newVal.toLowerCase();
            ObservableList<Admin> filteredList = FXCollections.observableArrayList();

            if (filter.isEmpty()) {
                filteredList.addAll(adminList);
            } else {
                for (Admin admin : adminList) {
                    if (admin.getUsername().toLowerCase().contains(filter)) {
                        filteredList.add(admin);
                    }
                }
            }
            tableView.setItems(filteredList);
        });

        // Layout
        VBox searchBoxContainer = new VBox(10, searchBox);
        VBox rootTable = new VBox(28, tableView);
        rootTable.setTranslateY(137);
        rootTable.setTranslateX(46);
        rootTable.setPadding(new Insets(0));
        // Tombol untuk membuka chatbot
        Button openChatbotButton = new Button("💬 Ask?");
        openChatbotButton.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-padding: 8 12;" +
                        "-fx-background-color: linear-gradient(to right, #ffffff, #ffffff);" +
                        "-fx-text-fill: black;" +
                        "-fx-background-radius: 30;" +
                        "-fx-cursor: hand;"

        );
        VBox chatBot = new VBox(20, openChatbotButton);
        chatBot.setAlignment(Pos.TOP_RIGHT);
        chatBot.setTranslateX(-230);
        chatBot.setTranslateY(20);

        openChatbotButton.setOnAction(e -> {
            try {
                Chatbot chatbotApp = new Chatbot();
                primaryStage.setFullScreen(false);
                chatbotApp.start(new Stage());

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        HBox rootBtn = new HBox(10, returnButton, btn_muatUlang, btn_update);
        rootBtn.setTranslateY(670);
        rootBtn.setTranslateX(340);
        rootBtn.setPadding(new Insets(15));

        // Scene setup
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(backgroundImageView, searchBoxContainer, rootTable, rootBtn,chatBot);

        Scene scene = new Scene(stackPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Data Admin");
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}