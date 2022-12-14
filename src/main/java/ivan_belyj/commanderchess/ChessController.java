package ivan_belyj.commanderchess;

import ivan_belyj.commanderchess.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class ChessController implements Initializable {
    @FXML
    private Canvas canvas;
    @FXML
    private TextFlow textFlow;
    @FXML
    private Button newGameButton;
    private String newGameText;
    private final String endGameText = "Завершить игру";

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

    // Поле можно разделять на клетки (10 x 11) или узлы (11 x 12))
    private static final int fieldCellsX = FieldData.FIELD_NODES_X - 1;
    private static final int fieldCellsY = FieldData.FIELD_NODES_Y - 1;

    private static final double padding = 30;  // Поле отображается с отступом от краев окна

    /** Размер клетки, включая толщину линии / линий **/
    private double cellSize;

    /** Размер фигуры **/
    private double figureSize;

    private static final Paint linePaint = Color.DARKGRAY;
    private static final Paint waterPaint = Color.SKYBLUE;
    private static final Paint shallowWaterPaint = Color.ALICEBLUE;
    private static final Paint backgroundPaint = Color.WHITE;

    private static final Font figureTextFont = new Font(24);

    // Следующие поля используются / устанавливаются во время отрисовки
    private GraphicsContext ctx;

    // Рисование линий начинается с учетом отступа
    private final double initialYPx = padding;
    private final double initialXPx = padding;

    // Координаты последнего узла сетки
    private double lastNodeX;
    private double lastNodeY;

    @Override
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gameManager = new GameManager();

        newGameText = newGameButton.getText();

        defineCellSize();
        figureSize = cellSize * 0.85;

        draw();
    }

    @FXML
    public void newGamePressed(ActionEvent event) {
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

        currentGame = gameManager.newGame(p1Name, p1Color, p2Name, p2Color);
        currentGame.addStartGameEventListener(this::draw);
        currentGame.startGame();
    }

    private void setDisableNewGameControls(boolean value) {
        player1Color.setDisable(true);
        player2Color.setDisable(true);
        player1Name.setDisable(true);
        player2Name.setDisable(true);
    }

    /** Фиксирует элементы управления новой игрой со введенными данными, соответствующими текущей партии **/
    private void fixNewGameControls() {
        setDisableNewGameControls(true);
        newGameButton.setText(endGameText);
    }

    private void unfixNewGameControls() {
        setDisableNewGameControls(false);
        newGameButton.setText(newGameText);
    }

    private void defineCellSize() {
        // Размер клетки определяется таким образом, чтобы клетки уместились в окно
        // (а значит, в минимальную из сторон окна)
        double minCanvasSize;
        double minSideCells;

        if (canvas.getWidth() < canvas.getHeight()) {
            minCanvasSize = canvas.getWidth();
            minSideCells = fieldCellsX;
        } else {
            minCanvasSize = canvas.getHeight();
            minSideCells = fieldCellsY;
        }
        cellSize = (minCanvasSize - padding * 2) / minSideCells;
    }

    private void draw() {
        this.ctx = canvas.getGraphicsContext2D();
        this.lastNodeX = (cellSize) * fieldCellsX + padding;
        this.lastNodeY = (cellSize) * fieldCellsY + padding;

        fillBackground();
        drawCells();
        drawLines();
        drawBorder();

        if (currentGame != null)
            drawFigures();
    }

    private void fillBackground() {
        ctx.setFill(backgroundPaint);
        ctx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void drawCells() {
        double xPx = initialXPx;
        double yPx = initialYPx;

        // В начале отрисовывается вода, а только после -- линии и фигуры
        ctx.setFill(waterPaint);
        // Покраска клетки
        // Todo: покраска поля на основе field model

        for (int x = 0; x < fieldCellsX + 1; x++, xPx += cellSize) {
            for (int y = 0; y < fieldCellsY + 1; y++, yPx += cellSize) {
                // test
                if (FieldData.getCellType(x, y) == FieldCellType.Water)
                    ctx.fillRect(xPx, yPx, cellSize, cellSize);
            }
            yPx = initialYPx;
        }
    }

    private void drawLines() {
        // Отображение линий
        double xPx = initialXPx;
        double yPx = initialYPx;

        ctx.setStroke(linePaint);

        // Для избежания дублирующейся рисовки вертикальных линий
        boolean drawVertLines = true;
        for (int x = 0; x < fieldCellsX + 1; x++) {
            // Горизонтальная линия
            ctx.strokeLine(xPx, yPx, xPx, lastNodeY);

            for (int y = 0; y < fieldCellsY + 1; y++) {
                // ctx.strokeOval(xPx, yPx, 10, 10);

                if (drawVertLines) {
                    // Вертикальная линия
                    ctx.strokeLine(xPx, yPx, lastNodeX, yPx);
                }

                yPx += cellSize;
            }
            if (drawVertLines)
                drawVertLines = false;

            xPx += cellSize;
            yPx = initialYPx;
        }
    }

    private void drawBorder() {
        // Границы поля выделяются красным
        ctx.setStroke(Color.MEDIUMVIOLETRED);
        ctx.strokeLine(initialXPx, initialYPx, initialXPx, lastNodeY);
        ctx.strokeLine(initialXPx, initialYPx, lastNodeX, initialYPx);
        ctx.strokeLine(lastNodeX, initialYPx, lastNodeX, lastNodeY);
        ctx.strokeLine(initialXPx, lastNodeY, lastNodeX, lastNodeY);
    }

    private void drawFigures() {
        for (FieldFigure fig : currentGame.getFieldData().getFiguresData()) {
            double x = initialXPx + cellSize * fig.getPosX();
            double y = initialYPx + cellSize * fig.getPosY();
            drawFigure(fig.getFigure(), x - figureSize / 2, y - figureSize / 2);
        }
    }

    private void drawFigure(Figure figure, double x, double y) {
        ctx.setStroke(Color.BLACK);
        ctx.strokeOval(x, y, figureSize, figureSize);
        ctx.setFill(figure.getPlayer().getColor());
        ctx.fillOval(x, y, figureSize, figureSize);
        
        String str = "";
        switch (figure.getFigureType()) {
            case Commander -> str = "C";
            case Infantry -> str = "In";
            case Tank -> str = "T";
            case Militia -> str = "M";
            case Engineer -> str = "E";
            case Artillery -> str = "A";
            case AntiAircraftGun -> str = "Aa";
            case Missile -> str = "Ms";
            case AirForce -> str = "Af";
            case Navy -> str = "N";
            case Headquarters -> str = "H";
        }

        // Todo: Цвет, который хорошо читается на цвете фигуры игрока
        ctx.setFill(Color.WHITE);
        ctx.setFont(figureTextFont);
        ctx.setTextAlign(TextAlignment.CENTER);
        double textOffset = figureSize / 2;
        double fontOffset = (figureTextFont.getSize() / 2) * 0.7;
        ctx.strokeText(str, x + textOffset, y + textOffset + fontOffset, figureSize);
        ctx.fillText(str, x + textOffset, y + textOffset + fontOffset, figureSize);
    }
}