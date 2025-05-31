package Testing;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Chatbot extends Application {

    private ObservableList<String> messages = FXCollections.observableArrayList();
    private ListView<String> chatList;
    private TextField inputField;
    private Button sendButton;

    @Override
    public void start(Stage primaryStage) {
        // Chat ListView for messages
        chatList = new ListView<>(messages);
        chatList.setPrefHeight(400);
        chatList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    // Style user and bot messages differently
                    if (item.startsWith("Kamu:")) {
                        setStyle("-fx-background-color: #e0f7fa; -fx-padding: 10; -fx-background-radius: 10; -fx-alignment: top-right;");
                    } else {
                        setStyle("-fx-background-color: #f5f5f5; -fx-padding: 10; -fx-background-radius: 10; -fx-alignment: top-left;");
                    }
                }
            }
        });

        // Input field
        inputField = new TextField();
        inputField.setPromptText("Tulis pesan di sini...");
        inputField.setOnAction(e -> sendMessage());
        inputField.getStyleClass().add("input-field");

        // Send button
        sendButton = new Button("Kirim");
        sendButton.setOnAction(e -> sendMessage());
        sendButton.getStyleClass().add("send-button");

        // Input box
        HBox inputBox = new HBox(10, inputField, sendButton);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setPadding(new Insets(10));
        inputBox.getStyleClass().add("input-box");

        // Main layout
        VBox root = new VBox(10, chatList, inputBox);
        root.setPadding(new Insets(15));
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("root");

        // Scene and stage
        Scene scene = new Scene(root, 600, 500);
        scene.getStylesheets().add("file:src/main/java/Testing/style.css");
        primaryStage.setTitle("Chatbot GPT-4o");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void sendMessage() {
        String userMessage = inputField.getText().trim();
        if (userMessage.isEmpty()) return;

        messages.add("Kamu: " + userMessage);
        inputField.clear();

        new Thread(() -> {
            try {
                String response = askGPT(userMessage);
                Platform.runLater(() -> messages.add("Bot: " + response));
            } catch (Exception e) {
                Platform.runLater(() -> messages.add("Bot: (Terjadi kesalahan: " + e.getMessage() + ")"));
            }
        }).start();
    }

    private String askGPT(String question) throws IOException {
        URL url = new URL("https://fastrestapis.fasturl.cloud/aillm/gpt-4o?ask=" + URLEncoder.encode(question, "UTF-8"));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int status = conn.getResponseCode();
        InputStream stream = (status >= 200 && status < 300) ? conn.getInputStream() : conn.getErrorStream();

        BufferedReader in = new BufferedReader(new InputStreamReader(stream));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }

        in.close();
        conn.disconnect();

        // Parse JSON dan ambil hanya bagian "result"
        JSONObject json = new JSONObject(response.toString());
        return json.optString("result", "(Tidak ada respons dari bot)");
    }

    public static void main(String[] args) {
        launch(args);
    }
}