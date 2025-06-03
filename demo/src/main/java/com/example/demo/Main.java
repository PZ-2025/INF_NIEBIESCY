package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        try {
            // Start embedded MariaDB przed odpaleniem aplikacji
            EmbeddedMariaDB.main(new String[0]);

            // Możesz też wykonać migracje lub inicjalizację bazy, jeśli potrzebne:
            DatabaseMigration.main(args);  // upewnij się, że ta klasa istnieje

            // Start aplikacji JavaFX
            launch(args);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}