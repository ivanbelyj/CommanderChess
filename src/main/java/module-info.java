module ivan_belyj.commanderchess {
    requires javafx.controls;
    requires javafx.fxml;


    opens ivan_belyj.commanderchess to javafx.fxml;
    exports ivan_belyj.commanderchess;
}