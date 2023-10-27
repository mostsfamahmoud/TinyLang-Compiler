/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxapplication1;

import compilerproject.CustomInput;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

    @FXML
    private ImageView myimage;
    @FXML
    private AnchorPane anchorpane;
    @FXML
    private TextArea TextArea1;
    @FXML
    private TreeTableView<?> Table;
    @FXML
    private HBox myHBox;
    @FXML
    private Button btnBrowse;
    @FXML
    private Button btnScan;
    @FXML
    private Button btnClear;
    @FXML
    private TreeTableColumn<?, ?> Token;
    @FXML
    private TreeTableColumn<?, ?> Type;

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
        

    }    



    @FXML
    private void Scan(ActionEvent event) {
    }

    @FXML
    private void Clear(ActionEvent event) {
    }

    @FXML
    private void FileSelector(ActionEvent event) {
           input = fileChooser.showOpenDialog(new Stage());
        if (input != null ) {
            InputStream orgInStream = System.in;
            try {
                System.setIn(new FileInputStream(input));
            } catch (FileNotFoundException ee) {
                ee.printStackTrace();
            }
            String xml = CustomInput.readString();
            TextArea1.setText(xml);
            CustomInput.close();
            System.setIn(orgInStream);
           
        } else {  
         JOptionPane.showMessageDialog(null, "You didn't select a file","Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    
}
