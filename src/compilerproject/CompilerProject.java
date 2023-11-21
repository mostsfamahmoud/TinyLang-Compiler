/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package compilerproject;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author habib
 */
public class CompilerProject extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try{
           Parent root = FXMLLoader.load(getClass().getResource("FXML.fxml"));
            Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Scanner");
        primaryStage.setScene(scene);
        primaryStage.show();
             } catch (IOException e) {
            System.out.println("Exception in GUIMain -> Start Method:-" + e);
            System.exit(0);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
