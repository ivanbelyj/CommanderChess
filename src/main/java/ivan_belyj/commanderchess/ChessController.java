package ivan_belyj.commanderchess;

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

    @Override
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        draw();

    }

    private void draw() {
        // Поле можно разделять на клетки (10 x 11) или узлы (11 x 12))
        int fieldCellsX = 10;  // Todo: get from field model
        int fieldCellsY = 11;

        double padding = 30;  // Поле отображается с отступом от краев окна

        double maxX = canvas.getWidth();
        double maxY = canvas.getHeight();

        GraphicsContext ctx = canvas.getGraphicsContext2D();

        // Размер клетки, включая толщину линии / линий
        double cellSize;
        // Размер клетки определяется таким образом, чтобы клетки уместились в окно
        // (а значит, в минимальную из сторон окна)
        double min;
        double minCells;
        if (maxX < maxY) {
            min = maxX;
            minCells = fieldCellsX;
        } else {
            min = maxY;
            minCells = fieldCellsY;
        }
        cellSize = (min - padding * 2) / minCells;

        Paint linePaint = Color.DARKGRAY;
        Paint waterPaint = Color.SKYBLUE;
        Paint shallowPaint = Color.ALICEBLUE;

        ctx.setFill(Color.WHITE);
        ctx.fillRect(0, 0, maxX, maxY);

        ctx.setStroke(linePaint);

        // Рисование линий начинается с учетом отступа
        final double initialYPx = padding;
        final double initialXPx = padding;

        double xPx = initialXPx;
        double yPx = initialYPx;

        final double lastNodeX = (cellSize) * fieldCellsX + padding;
        final double lastNodeY = (cellSize) * fieldCellsY + padding;

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

        // Отображение линий
        xPx = initialXPx;
        yPx = initialYPx;

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

        // Границы поля выделяются красным
        ctx.setStroke(Color.MEDIUMVIOLETRED);
        ctx.strokeLine(initialXPx, initialYPx, initialXPx, lastNodeY);
        ctx.strokeLine(initialXPx, initialYPx, lastNodeX, initialYPx);
        ctx.strokeLine(lastNodeX, initialYPx, lastNodeX, lastNodeY);
        ctx.strokeLine(initialXPx, lastNodeY, lastNodeX, lastNodeY);
    }


}