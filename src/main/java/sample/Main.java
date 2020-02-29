package sample;

import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        // Parent root = FXMLLoader.load(getClass().getResource("../../resources/main.fxml"));
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/main.fxml"));

        Parent root = loader.load();

        primaryStage.setTitle("ACDP Test Gui");
        primaryStage.setScene(new Scene(root, 1500, 800));
        primaryStage.show();
        Controller myController = loader.getController();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
