package Testing;

import data.User;
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
    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
        // Chat ListView for messages

        chatList = new ListView<>(messages);
        chatList.getStyleClass().add("chat-list");
        chatList.setCellFactory(param -> new ListCell<>() {
            private final Label label = new Label();
            private final HBox content = new HBox();

            {
                label.setWrapText(true);
                label.setPadding(new Insets(10));
                label.setMaxWidth(400); // batasi lebar maksimum pesan
                content.setPadding(new Insets(5));
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    label.setText(item);

                    if (item.startsWith("Kamu:")) {
                        content.getChildren().setAll(label);
                        content.setAlignment(Pos.CENTER_RIGHT);
                        label.setStyle("-fx-background-color: #DCF8C6; -fx-background-radius: 12; -fx-text-fill: black;");
                    } else {
                        content.getChildren().setAll(label);
                        content.setAlignment(Pos.CENTER_LEFT);
                        label.setStyle("-fx-background-color: #E5E5EA; -fx-background-radius: 12; -fx-text-fill: black;");
                    }

                    setGraphic(content);
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

        // Status label for loading
        statusLabel = new Label();
        statusLabel.getStyleClass().add("status-label");

        // Input box
        HBox inputBox = new HBox(10, inputField, sendButton);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setPadding(new Insets(10));
        inputBox.getStyleClass().add("input-box");

        // Main layout
        VBox root = new VBox(10, chatList, statusLabel, inputBox);
        root.setPadding(new Insets(15));
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("root");
        VBox.setVgrow(chatList, Priority.ALWAYS);
        chatList.maxWidthProperty().bind(root.widthProperty().subtract(30));

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
        chatList.scrollTo(messages.size() - 1);
        Platform.runLater(() -> statusLabel.setText("Menunggu respons bot..."));

        new Thread(() -> {
            try {
                String response = askGPT(userMessage);
                Platform.runLater(() -> {
                    messages.add("Bot: " + response);
                    chatList.scrollTo(messages.size() - 1);
                    statusLabel.setText("");
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    messages.add("Bot: (Terjadi kesalahan: " + e.getMessage() + ")");
                    chatList.scrollTo(messages.size() - 1);
                    statusLabel.setText("");
                });
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

        try {
            JSONObject json = new JSONObject(response.toString());
            String result = json.optString("result", "(Tidak ada respons dari bot)");
            if (result.length() > 500) {
                StringBuilder formattedResult = new StringBuilder();
                for (int i = 0; i < result.length(); i += 500) {
                    int end = Math.min(i + 500, result.length());
                    formattedResult.append(result, i, end).append("\n");
                }
                return formattedResult.toString();
            }
            return result;
        } catch (Exception e) {
            return "(Error parsing API response: " + e.getMessage() + ")";
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}