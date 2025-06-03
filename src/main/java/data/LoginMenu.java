package data;

import Features.Database;
import commands.CMD;
import javafx.application.Platform;
import javafx.concurrent.Task;
import util.iMenu;
import exception.custom.IllegalAdminAccess;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import sound.Sound;

import java.io.IOException;


public class LoginMenu implements iMenu{

    public final static TextField usernameField = new TextField();

    @Override
    public void menu(){
        Admin adminObj = new Admin();
        Student studentObj = new Student();


//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> LABEL <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

        Label usernameLabel = new Label("Username");
        usernameLabel.setFont(Font.font("Calibri Body", FontWeight.BOLD, 15));
        usernameLabel.setTranslateY(58);

        Label passwordLabel = new Label("Password");
        passwordLabel.setFont(Font.font("Calibri Body", FontWeight.BOLD, 15));
        passwordLabel.setTranslateY(58);

        Label errorLoginMessage   = new Label("Pengguna tidak ditemukan !");
        errorLoginMessage.setFont(Font.font("Calibri Body", FontWeight.BOLD, 13));

        errorLoginMessage.setStyle("-fx-text-fill: #FFFFFF;");
        errorLoginMessage.setTranslateX(69);
        errorLoginMessage.setTranslateY(48);

//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> FIELD <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

        usernameField.setPromptText("Masukkan NIM (15 Digit!)");
        usernameField.setTranslateY(58);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Masukkan PIC");
        passwordField.setTranslateY(58);


//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> BUTTON <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

        Button loginButton = new Button("Login");
        loginButton.getStylesheets().add("file:src/main/java/css/Login_button.css");
        loginButton.setFont(Font.font("Calibri Body", FontWeight.BOLD,10));
        loginButton.setTranslateY(63);

        Button closeButton = new Button("X");
        closeButton.getStylesheets().add("file:src/main/java/css/closeButton.css");
        Tooltip closeButtonPopup = new Tooltip("Exit");
        Tooltip.install(closeButton, closeButtonPopup);
        closeButton.setTranslateX(645);
        closeButton.setTranslateY(-355);

//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> IMAGE <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

        Image backgroundImage = new Image("file:src/main/java/image/backgroundImage.png");
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitHeight(768);
        backgroundImageView.setFitWidth(1366);

        Image logoImage = new Image("file:src/main/java/image/logo.png");
        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setFitHeight(120);
        logoImageView.setFitWidth(120);
        logoImageView.setTranslateX(4);
        logoImageView.setTranslateY(-158);

        Image logoNameImage = new Image("file:src/main/java/image/logoName.png");
        ImageView logoNameImageView = new ImageView(logoNameImage);
        logoNameImageView.setFitHeight(90);
        logoNameImageView.setFitWidth(200);
        logoNameImageView.setTranslateX(4);
        logoNameImageView.setTranslateY(-65);


//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> SHAPE <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

        Rectangle rectangle = new Rectangle(300, 380);
        rectangle.setFill(Color.WHITE);
        rectangle.setArcWidth(50);
        rectangle.setArcHeight(50);
        rectangle.setTranslateX(0);
        rectangle.setTranslateY(20);

        Rectangle errorBackground = new Rectangle(300,70);
        errorBackground.setArcWidth(50);
        errorBackground.setArcHeight(50);
        errorBackground.setFill(Color.MAROON);


//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Group Element <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

        Group errorElementGroup = new Group();
        errorElementGroup.getChildren().add(errorBackground);
        errorElementGroup.getChildren().add(errorLoginMessage);
        errorElementGroup.setVisible(false);


//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Grid Settings <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        grid.add(logoNameImageView,0,0);

        grid.add(usernameLabel, 0,4);
        grid.add(passwordLabel, 0,6);

        grid.add(usernameField, 0,5);
        grid.add(passwordField, 0,7);

        grid.add(loginButton, 0,9);
        grid.add(errorElementGroup,0,10);

        grid.setVgap(10);
        grid.setHgap(5);

//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Animation Settings <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

        TranslateTransition anim_errorElemenGroup = new TranslateTransition(Duration.seconds(0.5), errorElementGroup);
        anim_errorElemenGroup.setFromY(170);
        anim_errorElemenGroup.setToY(200);


//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Window Settings <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(backgroundImageView,errorElementGroup,rectangle,grid,logoImageView,logoNameImageView,closeButton);

        Scene scene = new Scene(stackPane);

        Stage primaryStage = new Stage();
        primaryStage.setTitle("UMM Library");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.show();


//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Action Button <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

        closeButton.setOnAction(event -> {
            try {
                CMD.runCommands(CMD.listCommands[1]);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            primaryStage.close();
        });

        loginButton.setOnAction(event -> {
            String nim      = usernameField.getText();
            String username = usernameField.getText();
            String password = passwordField.getText();

            System.out.println("DEBUG - NIM: " + nim);
            System.out.println("DEBUG - PIC: " + password);

            if (Database.admin_loginCheck(username, password)) {

                adminObj.menu();
                primaryStage.close();
            } else {
                try {
                    //if (username.length() == 15 && Database.student_loginChecker(username, password)) {
                    if (username.length() == 15 && Database.student_loginChecker(username, password)) {
                        if (!Database.book_expiredDateBorrowedBook(nim)) {
                            Database.book_bookDisplay();
                            Database.student_displayBorrowedBooks(usernameField.getText());
                            studentObj.menu();
                            primaryStage.close();

                        } else {
                            errorLoginMessage.setText("Mohon Tunggu,sedang Verifikasi");
                            Sound.falseLogin();
                            errorElementGroup.setVisible(true);
                            anim_errorElemenGroup.play();
                            Task<Void> emailTask = new Task<>() {
                                @Override
                                protected Void call() {

                                    try {
                                        Database.book_expiredBorrowedBookSendEmail(nim);
                                        System.out.println("Background email task completed for NIM: " + nim);
                                    } catch (Exception e) {
                                        System.out.println("Error in background email task: " + e.getMessage());
                                    }
                                    return null;
                                }
                            };

                            // Update UI after task completes
                            emailTask.setOnSucceeded(e -> Platform.runLater(() -> {

                                errorLoginMessage.setText("Akun telah ditangguhkan!");
                                Sound.falseLogin();
                                errorElementGroup.setVisible(true);
                                anim_errorElemenGroup.play();
                            }));

                            // Handle task failure
                            emailTask.setOnFailed(e -> Platform.runLater(() -> {
                                System.out.println("Email task failed: " + emailTask.getException().getMessage());

                                errorLoginMessage.setText("Akun telah ditangguhkan!");
                                Sound.falseLogin();
                                errorElementGroup.setVisible(true);
                                anim_errorElemenGroup.play();
                            }));

                            // Start the task in a new thread
                            new Thread(emailTask).start();

                        }
                    } else {

                        Sound.falseLogin();
                        errorElementGroup.setVisible(true);
                        anim_errorElemenGroup.play();
                    }
                } catch (IllegalAdminAccess message) {

                    errorLoginMessage.setText(message.getMessage());
                    errorElementGroup.setVisible(true);

                    Sound.falseLogin();
                    anim_errorElemenGroup.play();

                }
            }

        });

    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.setHeaderText(title);
        alert.showAndWait();
    }
}