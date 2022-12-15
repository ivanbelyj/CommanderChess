package ivan_belyj.commanderchess;

import ivan_belyj.commanderchess.model.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class ChessController implements Initializable {
    @FXML
    private Canvas canvas;
    @FXML
    private TextFlow textFlow;
    @FXML
    private Button gameButton;
    private final String endGameText = "Завершить игру";
    private String newGameText;

    @FXML
    private ColorPicker player1Color;
    @FXML
    private ColorPicker player2Color;
    @FXML
    private TextField player1Name;
    @FXML
    private TextField player2Name;


    /** Позволяет управлять игровыми партиями **/
    private GameManager gameManager;
    /** Текущая игровая партия **/
    private PartyGame currentGame;

    private Drawer drawer;
    private MouseHandler mouseHandler;

    private FieldDrawingData fieldDrawingData;

    @Override
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gameManager = new GameManager();

        fieldDrawingData = new FieldDrawingData(canvas);

        drawer = new Drawer(fieldDrawingData, canvas);
        drawer.draw();
        mouseHandler = new MouseHandler(fieldDrawingData, canvas);
        mouseHandler.addFieldNodeSelectedEventListener(args -> {
            UIDrawingData drawingData = new UIDrawingData(args.getPosX(), args.getPosY());
            drawer.draw(drawingData);
        });

        newGameText = gameButton.getText();
    }

    private void newGame() {
        Color p1Color = player1Color.getValue();
        Color p2Color = player2Color.getValue();

        String p1Name = player1Name.getText();
        String p2Name = player2Name.getText();

        // Имена не могут быть пустыми или одинаковыми
        if (p1Name.isBlank())
            p1Name = RandomName.Generate();
        if (p2Name.isBlank())
            p2Name = RandomName.Generate();
        while (p1Name.equals(p2Name)) {
            p2Name = RandomName.Generate();
        }

        // Цвета не могут быть одинаковыми
        Random rnd = new Random();
        while (p1Color.equals(p2Color)) {
            p2Color = new Color(rnd.nextDouble(), rnd.nextDouble(), rnd.nextDouble(), 1);
        }

        player1Color.setValue(p1Color);
        player2Color.setValue(p2Color);
        player1Name.setText(p1Name);
        player2Name.setText(p2Name);

        fixNewGameControls();

        // Start game logic
        currentGame = gameManager.newGame(p1Name, p1Color, p2Name, p2Color);
        drawer.setCurrentGame(currentGame);
//        currentGame.addStartGameEventListener(this::draw);
        currentGame.startGame();
        nextTurn();
    }

    private void endGame(String endGameMessage) {
        drawer.setCurrentGame(null);
        currentGame = null;
        // currentGame.endGame();
        setEndGameUI(endGameMessage);
    }

    private void setEndGameUI(String endGameMessage) {
        textFlow.getChildren().clear();
        textFlow.getChildren().add(new Text(endGameMessage));
        unfixNewGameControls();
    }

    @FXML
    public void gameButtonPressed(ActionEvent event) {
        if (currentGame == null)
            newGame();
        else
            endGame("Игра завершена досрочно. Никто не победил");
    }

    private void nextTurn() {
        updateTurnUI(currentGame.nextTurn());
        drawer.draw();
    }

    private Font toBoldFont(Font font) {
        return Font.font(font.getName(), FontWeight.BOLD, font.getSize());
    }

    private void updateTurnUI(TurnData turnData) {
        textFlow.getChildren().clear();
        Text t1 = new Text("Ход " + turnData.getTurnNumber() + ". ");
        Text t2 = new Text(turnData.getPlayer().getName());
//        t2.setFill(turnData.getPlayer().getColor());
        t2.setFont(toBoldFont(t2.getFont()));

        textFlow.getChildren().add(t1);
        textFlow.getChildren().add(t2);
    }

    private void setDisableNewGameControls(boolean value) {
        player1Color.setDisable(value);
        player2Color.setDisable(value);
        player1Name.setDisable(value);
        player2Name.setDisable(value);
    }

    /** Фиксирует элементы управления новой игрой со введенными данными, соответствующими текущей партии **/
    private void fixNewGameControls() {
        setDisableNewGameControls(true);
        gameButton.setText(endGameText);
    }

    private void unfixNewGameControls() {
        setDisableNewGameControls(false);
        gameButton.setText(newGameText);
    }
}
