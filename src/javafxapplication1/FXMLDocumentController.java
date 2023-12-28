/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxapplication1;

import compilerproject.FileHandler;
import compilerproject.Parser;
import compilerproject.Scanner;
import compilerproject.SyntaxException;
import compilerproject.Token;
import java.io.ByteArrayInputStream;
//import compilerproject.Token.TokenType;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
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
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author habib
 */
public class FXMLDocumentController implements Initializable {
    Scanner scanner;
    Parser parser;
    FileChooser fileChooser;
    File input;
    String fileContent;
    FileHandler fileHandler = new FileHandler();
    File outputFile;
    String outputContents;
    boolean isScanned = false;
    @FXML
    private ImageView syntaxTreeImageView;
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
    private TreeTableColumn<Token, String> Type;
    // private TreeTableColumn<Token, Token.TokenType> Type;
    @FXML
    private Button btnsave;
    @FXML
    private Button btnParse;

    byte[] imgBytes;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        // TODO
        //myimage.setImage(new Image(getClass().getResourceAsStream("Arroww.png")));
        fileChooser = new FileChooser();
        FileChooser.ExtensionFilter Filter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        FileChooser.ExtensionFilter Filter2 = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(Filter);
        fileChooser.getExtensionFilters().add(Filter2);
        // Configure the columns
        Token.setCellValueFactory(new TreeItemPropertyValueFactory<>("TokenVal"));
        Type.setCellValueFactory(new TreeItemPropertyValueFactory<>("TokenType"));
        TreeItem<Token> root = new TreeItem<>(new Token());
        Table.setRoot(root);
        Table.setShowRoot(false);

    }


    @FXML
    private void Scan(ActionEvent event) throws IOException {
        if (TextArea1.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(null, "Please select a file first or write in textfield", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // fileContent = fileHandler.ReadFile(input.getAbsolutePath());
        Scanner scanner = new Scanner(TextArea1.getText());
         StringBuilder outputString = new StringBuilder();
        // Scanner scanner = new Scanner(fileContent);
        ArrayList<Token> tokensList = scanner.getTokensList();
        TreeItem<Token> root = new TreeItem<>(new Token());
        outputString.append("Token Value,Token Type\n");

        for (Token token : tokensList) {
            // Print token type before appending to the table
            System.out.println("Token Type: " + token.getTokenType());

            TreeItem<Token> tokenItem = new TreeItem<>(token);
            root.getChildren().add(tokenItem);
       outputString.append(token.getTokenVal()).append(",").append(token.getTokenType()).append("\n");
        }

        Token.setCellValueFactory(new TreeItemPropertyValueFactory<>("TokenVal"));
        Type.setCellValueFactory(new TreeItemPropertyValueFactory<>("TokenType"));
        Table.setRoot(root);
        Table.setShowRoot(false);
        isScanned = true;
          outputContents = outputString.toString();
    }

    @FXML
    private void Clear(ActionEvent event) {
        input = null;
        scanner = null;
        parser = null;
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
                fileContent = fileHandler.ReadFile(input.getAbsolutePath());
                TextArea1.setText(fileContent);

            } catch (IOException e) {
                e.printStackTrace();

            }
        } else {
            JOptionPane.showMessageDialog(null, "You didn't select a file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    private void Save(ActionEvent event) {
        if (TextArea1.getText().trim().isEmpty()) {
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

    /**
     * Prompts the user to select a location to save the syntax tree image generated by the parser.
     * If a valid file location is chosen, saves the syntax tree image in PNG format.
     * The method uses the FileChooser dialog to enable the user to select a file location.
     *
     * Preconditions:
     * - The parser object must be initialized.
     * - The parser must have successfully generated the syntax tree image.
     *
     * Postconditions:
     * - If successful, the syntax tree image is saved at the chosen location.
     * - If an error occurs during the process, an error message is displayed.
     *
     * @throws NullPointerException     If the parser or parser's tree is null.
     * @throws IllegalStateException    If the parser hasn't successfully generated the syntax tree image.
     */
    private void saveSyntaxTreeImage() {
        // Create a file chooser dialog to allow the user to select a location to save the image
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Syntax Tree Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files (*.png)", "*.png"));

        // Display the file chooser dialog to get the selected file location
        File selectedFile = fileChooser.showSaveDialog(null);

        // If the user cancels or does not select a file, show a message and return
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(null, "Image save canceled", "Save Canceled", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Get the absolute path of the selected file
        String fileName = selectedFile.getAbsolutePath();

        // Check if the parser and its tree are initialized
        if (parser != null && parser.getTree() != null) {
            try {
                // Retrieve the syntax tree image bytes from the parser
                imgBytes = parser.getTree().getImg("png");

                // Create a file object with the selected file location
                File imageOutputFile = new File(fileName);

                // Save the syntax tree image to the selected file location using ImageIO
                ImageIO.write(ImageIO.read(new ByteArrayInputStream(imgBytes)), "png", imageOutputFile);

                // Show a success message upon successful image save
                JOptionPane.showMessageDialog(
                        null,
                        "Image saved successfully as '" + imageOutputFile.getName() + "'",
                        "Save Successful",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } catch (Exception e) {
                // If an error occurs during the save process, display an error message
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                        null,
                        "An error occurred while saving the image",
                        "Save Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } else {
            // If the parser or its tree is null, throw a NullPointerException
            throw new NullPointerException("Parser or parser's tree is null");
        }
    }


    @FXML
    private void Parse(ActionEvent event) {
        if (TextArea1.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please select a file first or write in textfield", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            scanner = new Scanner(TextArea1.getText());
            parser = new Parser(scanner);
            try {
                parser.parse();

                saveSyntaxTreeImage();

                Image image = new Image(new ByteArrayInputStream(imgBytes));
                ImageView imageView = new ImageView(image);
                imageView.setPreserveRatio(true);

                Group root = new Group(imageView);
                Stage syntaxTreeStage = new Stage();
                syntaxTreeStage.setScene(new Scene(root));
                syntaxTreeStage.setTitle("Syntax Tree Representation");
                syntaxTreeStage.showAndWait();

            } catch (SyntaxException e) {
                System.out.println(e.getMessage());
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

            }
        }
    }
}
