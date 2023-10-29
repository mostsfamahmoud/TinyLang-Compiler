module com.example.testscanner {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.testscanner to javafx.fxml;
    exports com.example.testscanner;
}