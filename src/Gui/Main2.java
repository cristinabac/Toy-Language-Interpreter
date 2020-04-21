package Gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main2 extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{


        Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
        primaryStage.setTitle("Welcome to toy language!");
        primaryStage.setScene(new Scene(root, 1000, 800));
        primaryStage.show();


    }

    //TODO - synchronize everything

    public static void main(String[] args) {
        launch(args);
    }
}
