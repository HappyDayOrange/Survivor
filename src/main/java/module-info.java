module is.ballus.survivor {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens is.ballus.survivor to javafx.fxml;
    exports is.ballus.survivor;
}