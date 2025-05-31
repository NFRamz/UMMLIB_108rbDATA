package data;

import Features.Database;
import Main.Main;
import Testing.ViewBorrowedBook;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import util.iMenu;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.Random;


public class Admin extends User implements iMenu {

//======================================= MENU Method =======================================
    @Override
    public void menu(){


        //Label
        Label sceneTitle = new Label("Menu Admin");
        sceneTitle.setTranslateX(40);
        sceneTitle.setStyle("-fx-text-fill: #A91D3A;");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));


        //Button
        Button addStudentButton     = new Button("Tambah Mahasiswa");
        addStudentButton.getStylesheets().add("file:src/main/java/css/Login_button.css");

        Button displayStudentButton = new Button("Daftar Mahasiswa");
        displayStudentButton.getStylesheets().add("file:src/main/java/css/Login_button.css");

        //new===
        Button historyBorrowedBookButton = new Button("lihat Riwayat Peminjaman");
        historyBorrowedBookButton.getStylesheets().add("file:src/main/java/css/Login_button.css");

        Button manageBook        = new Button("kelola Buku");
        manageBook.getStylesheets().add("file:src/main/java/css/Login_button.css");

        Button manageUser        = new Button("kelola pengguna");
        manageUser.getStylesheets().add("file:src/main/java/css/Login_button.css");
        //========

        Button addBookButton        = new Button("Tambah Buku");
        addBookButton.getStylesheets().add("file:src/main/java/css/Login_button.css");

        Button logoutButton         = new Button("Logout");
        logoutButton.getStylesheets().add("file:src/main/java/css/Login_button.css");

        //Image
        Image backgroundImage = new Image("file:src/main/java/image/backgroundImage.png");
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(1366);
        backgroundImageView.setFitHeight(768);

        //Shape
        Rectangle backgroundShape = new Rectangle();
        backgroundShape.setWidth(300);
        backgroundShape.setHeight(300);
        backgroundShape.setArcWidth(50);
        backgroundShape.setArcHeight(50);
        backgroundShape.setTranslateX(0);
        backgroundShape.setTranslateY(15);
        backgroundShape.setFill(Color.WHITE);


        //Grid Layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        grid.add(sceneTitle,0,1);

        grid.add(addStudentButton, 0,2);
        grid.add(displayStudentButton, 0,3);
        grid.add(addBookButton, 0,4);
        grid.add(historyBorrowedBookButton, 0,5);
        grid.add(logoutButton,0,6);

        grid.setVgap(20);
        grid.setHgap(5);

        //Overwrite elements
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(backgroundImageView, backgroundShape ,grid);

        Scene scene = new Scene(stackPane);

        //Create window
        Stage adminMenuStage = new Stage();
        adminMenuStage.setScene(scene);
        adminMenuStage.setTitle("UMM Library - Admin Menu");
        adminMenuStage.setFullScreen(true);
        adminMenuStage.setFullScreenExitHint("");

        adminMenuStage.show();


        //Button Action
        addStudentButton.setOnAction(event -> {
            addstudent();
            adminMenuStage.close();
        });

        displayStudentButton.setOnAction(event -> {
            displaystudent();
            adminMenuStage.close();
        });

        addBookButton.setOnAction(event -> {
            inputBook();
            adminMenuStage.close();
        });

        historyBorrowedBookButton.setOnAction(event -> {
            ViewBorrowedBook viewBorrowedBook = new ViewBorrowedBook();
            viewBorrowedBook.start(new Stage());
            adminMenuStage.close();
        });

        logoutButton.setOnAction(event -> {
            Main mainobj = new Main();
            mainobj.start(new Stage());
            adminMenuStage.close();
        });

    }

//===================================== Other Method =======================================
    public void addstudent() {

        //Label
        Label sceneTitle    = new Label("Tambah Mahasiswa");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.EXTRA_BOLD, 25));
        sceneTitle.setStyle("-fx-text-fill: #A91D3A;");
        
        Label nameLabel     = new Label("Nama");
        nameLabel.setFont(Font.font("Calibri Body", FontWeight.NORMAL, 15));

        Label nimLabel      = new Label("NIM");
        nimLabel.setFont(Font.font("Calibri Body", FontWeight.NORMAL, 15));

        Label fakultasLabel = new Label("Fakultas");
        fakultasLabel.setFont(Font.font("Calibri Body", FontWeight.NORMAL, 15));

        Label jurusanLabel  = new Label("Jurusan");
        jurusanLabel.setFont(Font.font("Calibri Body", FontWeight.NORMAL, 15));

        Label emailLabel  = new Label("Email");
        emailLabel.setFont(Font.font("Calibri Body", FontWeight.NORMAL, 15));


        //Notification Label
        Label sumbitFailed = new Label("NIM harus 15 digit!");
        sumbitFailed.setVisible(false);
        sumbitFailed.setStyle("-fx-text-fill: #FF1E1E;");
        sumbitFailed.setFont(Font.font("Calibri Body",FontWeight.BOLD,15));


        //Image
        Image backgroundImage = new Image("file:src/main/java/image/add_student.png");
        ImageView backgroundImageView = new ImageView(backgroundImage);


        //Button
        Button submitButton = new Button("Submit");
        Button returnButton = new Button("Kembali");

        //Field
        TextField nameField     = new TextField();
        nameField.setPromptText("Masukkan Nama");

        TextField nimField      = new TextField();
        nimField.setPromptText("MIN.15 Digit");

        TextField fakultasField = new TextField();
        fakultasField.setPromptText("Masukkan Fakultas");

        TextField jurusanField  = new TextField();
        jurusanField.setPromptText("Masukkan nama jurusan");

        TextField emailField = new TextField();
        emailField.setPromptText("alamat Email");

        TextField picField = new TextField();
        picField.setPromptText("PIC");


        //Grid Layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER_RIGHT);
        grid.add(sceneTitle, 0,0);

        grid.add(nameLabel, 0,1);
        grid.add(nameField, 0,2);

        grid.add(nimLabel, 0,3);
        grid.add(nimField, 0,4);

        grid.add(fakultasLabel, 0,5);
        grid.add(fakultasField, 0,6);

        grid.add(jurusanLabel, 0,7);
        grid.add(jurusanField, 0,8);

        grid.add(emailLabel, 0,9);
        grid.add(emailField, 0,10);


        grid.add(returnButton,0,11);
        grid.add(submitButton,1,11);

        grid.add(sumbitFailed, 0,12);

        grid.setVgap(10);
        grid.setHgap(5);

        //Window Settings
        StackPane stackPane = new StackPane(backgroundImageView, grid);

        Scene scene = new Scene(stackPane);

        Stage addStudentStage = new Stage();
        addStudentStage.setScene(scene);
        addStudentStage.setTitle("Tambah Mahasiswa");
        addStudentStage.setFullScreen(true);
        addStudentStage.setFullScreenExitHint("");

        addStudentStage.show();

        //Action Button

        submitButton.setOnAction(event -> {
            if (nimField.getText().length() == 15) {
                Admin adminObj = new Admin();

                Student.arr_userStudent.add(new Student.UserStudent(nameField.getText(), nimField.getText(), fakultasField.getText(), jurusanField.getText(), emailField.getText(), picField.getText()));
                Database.student_addStudent(nimField.getText(), picField.getText(), nameField.getText(), fakultasField.getText(), jurusanField.getText(), emailField.getText());

                adminObj.menu();
                addStudentStage.close();

            } else {
                sumbitFailed.setVisible(true);
            }
        });
        returnButton.setOnAction(event -> {
            Admin adminObj =  new Admin();
            adminObj.menu();
            addStudentStage.close();
        });

    }

    public void displaystudent() {
        // Membuat stage baru
        Stage displayStudentStage = new Stage();
        displayStudentStage.setTitle("UMM Library - Daftar Mahasiswa");

        //Label
        Label sceneTitle    = new Label("Daftar Mahasiswa");

        //Button
        Button returnButton = new Button("Kembali");

        //Font Style
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));

        //Font Color
        sceneTitle.setStyle("-fx-text-fill: #A91D3A;");


        // Buat ListView untuk menampilkan data mahasiswa
        ListView<String> listView = new ListView<>();
        Database.admin_displayStudent(listView);

        for (String i : listView.getItems()) {
            System.out.println(i);
        }
        //Grid Layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        grid.add(sceneTitle,0,0);
        grid.add(listView,0,1);
        grid.add(returnButton,0,2);

        grid.setVgap(5);

        Scene scene = new Scene(grid);
        displayStudentStage.setMaximized(true);
        displayStudentStage.setScene(scene);
        displayStudentStage.show();

        //Action
        returnButton.setOnAction(event -> {
            Admin adminObj = new Admin();
            adminObj.menu();
            displayStudentStage.close();
        });
    }


    public void inputBook(){
        super.inputBook();
    }

    public String generateId () {
        Random random = new Random();

        StringBuilder idTengah = new StringBuilder();
        StringBuilder idAkhir = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            idTengah.append(random.nextInt(10));
            idAkhir.append(random.nextInt(10));

        }
        return ("UMM-" + idTengah + "-" + idAkhir);

    }


}
