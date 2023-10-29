/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxapplication1;

import compilerproject.FileHandler;
import compilerproject.Scanner;
import compilerproject.Token;
import compilerproject.Token.TokenType;
import static compilerproject.Token.TokenType.IF;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author habib
 */
public class FXMLDocumentController implements Initializable {

    FileChooser fileChooser;
    File input;
    FileHandler fileHandler = new FileHandler();
    File outputFile;
    String outputContents;
    boolean isScanned = false;

    @FXML
    private ImageView myimage;
    @FXML
    private AnchorPane anchorpane;
    @FXML
    private TextArea TextArea1;
    @FXML
    private TreeTableView<Token> Table;
    @FXML
    private HBox myHBox;
    @FXML
    private Button btnBrowse;
    @FXML
    private Button btnScan;
    @FXML
    private Button btnClear;
    @FXML
    private TreeTableColumn<Token, String> Token;
    @FXML
    private TreeTableColumn<Token, Token.TokenType> Type;
    @FXML
    private Button btnsave;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        // TODO
        myimage.setImage(new Image(getClass().getResourceAsStream("Arroww.png")));
        fileChooser = new FileChooser();
        FileChooser.ExtensionFilter Filter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        FileChooser.ExtensionFilter Filter2 = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(Filter);
        fileChooser.getExtensionFilters().add(Filter2);
        // Configure the columns
        Token.setCellValueFactory(new TreeItemPropertyValueFactory<>("value"));
        Type.setCellValueFactory(new TreeItemPropertyValueFactory<>("type"));
        TreeItem<Token> root = new TreeItem<>(new Token());
        Table.setRoot(root);
        Table.setShowRoot(false);

    }

    @FXML
    private void Scan(ActionEvent event) {
        if (input == null) {
            JOptionPane.showMessageDialog(null, "Please select a file first", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Scanner scanner = new Scanner(input.getAbsolutePath());
            StringBuilder outputString = new StringBuilder();
            TreeItem<Token> root = new TreeItem<>(new Token());

            while (true) {
                Token t = scanner.getCurrentToken();
                if (t.getType() == TokenType.END_OF_FILE) {
                    break;
                }

                if (t.getType() == null || t.getValue().isEmpty()) {
                    break;
                }

                TreeItem<Token> tokenItem = new TreeItem<>(t);
                root.getChildren().add(tokenItem);
                outputString.append(t).append("\n");
                scanner.advanceInput();
            }
            outputContents = outputString.toString();
            Table.setRoot(root);
             isScanned = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void Clear(ActionEvent event) {
        input = null;
        TextArea1.clear();
        TreeItem<Token> root = new TreeItem<>(new Token());
        Table.setRoot(root);
        Table.setShowRoot(false);
        isScanned = false;

    }

    @FXML
    private void FileSelector(ActionEvent event) {
        input = fileChooser.showOpenDialog(new Stage());
        if (input != null) {
            try {
                String fileContents = fileHandler.ReadFile(input.getAbsolutePath());
                TextArea1.setText(fileContents);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception as needed
            }
        } else {
            JOptionPane.showMessageDialog(null, "You didn't select a file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    private void Save(ActionEvent event) {
        if (input == null) {
            JOptionPane.showMessageDialog(null, "Please select a file first", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!isScanned) {
            JOptionPane.showMessageDialog(null, "Please scan the input file before saving", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        FileChooser saveFileChooser = new FileChooser();
        saveFileChooser.setTitle("Save Output File");
         FileChooser.ExtensionFilter csvExtensionFilter = new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv");
    FileChooser.ExtensionFilter txtExtensionFilter = new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt");
    saveFileChooser.getExtensionFilters().addAll(csvExtensionFilter, txtExtensionFilter);

        File outputFile = saveFileChooser.showSaveDialog(null);

        if (outputFile == null) {
            JOptionPane.showMessageDialog(null, "File save canceled", "Save Canceled", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            FileHandler fileHandler = new FileHandler();
            String outputFilePath = outputFile.getAbsolutePath();
            String csvFile = fileHandler.GenerateCSV(outputContents);
            fileHandler.WriteFile(csvFile, outputFilePath);

            JOptionPane.showMessageDialog(null, "File saved successfully as '" + outputFile.getName() + "'", "Save Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while saving the file", "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
