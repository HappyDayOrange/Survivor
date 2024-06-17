module is.ballus.survivor {
    requires javafx.controls;
    requires javafx.fxml;


    opens is.ballus.survivor to javafx.fxml;
    exports is.ballus.survivor;
}