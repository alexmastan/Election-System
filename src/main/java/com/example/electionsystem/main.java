package com.example.electionsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        System.setProperty("prism.lcdtext", "false");
        FXMLLoader fxmlLoader = new FXMLLoader(main.class.getResource("admin_login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Image icon = new Image("C:\\Facultate\\OOP\\ElectionSystem\\src\\main\\resources\\misc\\icon.png");
        stage.getIcons().add(icon);
        stage.setTitle("Election System");

//        Font.loadFont(main.class.getResource("/misc/Manrope-Light.ttf").toExternalForm(), 10);
//        Font.loadFont(main.class.getResource("/misc/Manrope-Regular.ttf").toExternalForm(), 10);
        String css = this.getClass().getResource("admin_login.css").toExternalForm();
        scene.getStylesheets().add(css);

        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}