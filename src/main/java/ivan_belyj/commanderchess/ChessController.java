package ivan_belyj.commanderchess;

import ivan_belyj.commanderchess.model.FieldData;
import ivan_belyj.commanderchess.model.FieldFigure;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.net.URL;
import java.util.ResourceBundle;

public class ChessController implements Initializable {
    @FXML
    private Canvas canvas;

    /** Ссылка на данные об игровом поле для отображения **/
    private FieldData fieldData;

    // Поле можно разделять на клетки (10 x 11) или узлы (11 x 12))
    private static final int fieldCellsX = FieldData.FIELD_CELLS_X;
    private static final int fieldCellsY = FieldData.FIELD_CELLS_Y;

    private static final double padding = 30;  // Поле отображается с отступом от краев окна

    /** Размер клетки, включая толщину линии / линий **/
    private double cellSize;

    private static final Paint linePaint = Color.DARKGRAY;
    private static final Paint waterPaint = Color.SKYBLUE;
    private static final Paint shallowWaterPaint = Color.ALICEBLUE;
    private static final Paint backgroundPaint = Color.WHITE;

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
        defineCellSize();
        draw();
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
        // drawFigures();
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
                if (x == 0 && y == 0
                        || x == 2 && y ==3)
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
        // Отображение фигур
        for (FieldFigure fig : fieldData.getFiguresData()) {

        }
    }
}