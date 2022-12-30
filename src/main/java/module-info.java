module ivan_belyj.commanderchess {
    requires javafx.controls;
    requires javafx.fxml;


    opens ivan_belyj.commanderchess to javafx.fxml;
    exports ivan_belyj.commanderchess;
    exports ivan_belyj.commanderchess.movement_input;
    opens ivan_belyj.commanderchess.movement_input to javafx.fxml;
}