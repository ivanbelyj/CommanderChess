package ivan_belyj.commanderchess;

import ivan_belyj.commanderchess.model.FieldData;
import ivan_belyj.commanderchess.model.FieldFigure;
import ivan_belyj.commanderchess.model.Figure;
import ivan_belyj.commanderchess.model.PartyGame;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/** Класс отрисовки UI игры на Canvas **/
public class Drawer {
    private PartyGame currentGame;

    private final Canvas canvas;

    // Поле можно разделять на клетки (10 x 11) или узлы (11 x 12))
    private static final int fieldCellsX = FieldData.FIELD_NODES_X - 1;
    private static final int fieldCellsY = FieldData.FIELD_NODES_Y - 1;

    private static final double padding = 30;  // Поле отображается с отступом от краев окна

    /** Размер клетки, включая толщину линии / линий **/
    private double cellSize;

    /** Размер фигуры **/
    private final double figureSize;

    private static final Paint linePaint = Color.DARKGRAY;
    private static final Paint waterPaint = Color.SKYBLUE;
    private static final Paint backgroundPaint = Color.WHITE;

    // Используется для градиента далее
    private static final Stop[] leftStops = new Stop[] { new Stop(0, Color.SKYBLUE), new Stop(0.8, Color.WHITE) };
    private static final Stop[] rightStops = new Stop[] { new Stop(0.2, Color.WHITE), new Stop(1, Color.SKYBLUE) };
    // Клетки, где мелководье находится справа и слева, отрисовываются по-разному
    private static final Paint shallowWaterLeftPaint = new LinearGradient(0, 0.5, 1, 0.5, true, CycleMethod.NO_CYCLE, leftStops);
    private static final Paint shallowWaterRightPaint = new LinearGradient(0, 0.5, 1, 0.5, true, CycleMethod.NO_CYCLE, rightStops);

    private static final Font figureTextFont = new Font(24);

    // Следующие поля используются / устанавливаются во время отрисовки
    private GraphicsContext ctx;

    // Рисование линий начинается с учетом отступа
    private final double initialYPx = padding;
    private final double initialXPx = padding;

    // Координаты последнего узла сетки
    private double lastNodeX;
    private double lastNodeY;

    public Drawer(Canvas canvas) {
        this.canvas = canvas;

        defineCellSize();
        figureSize = cellSize * 0.85;
    }

    public void setCurrentGame(PartyGame currentGame) {
        this.currentGame = currentGame;
    }
    public PartyGame getCurrentGame() {
        return currentGame;
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

    public void draw() {
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

        for (int x = 0; x < fieldCellsX + 1; x++, xPx += cellSize) {
            for (int y = 0; y < fieldCellsY + 1; y++, yPx += cellSize) {
                // test
                switch (FieldData.getCellType(x, y)) {
                    case Water:
                        ctx.setFill(waterPaint);
                        break;
                    case ShallowWaterLeft:
                        ctx.setFill(shallowWaterLeftPaint);
                        break;
                    case ShallowWaterRight:
                        ctx.setFill(shallowWaterRightPaint);
                        break;
                    default:
                        // Отрисовка иных типов ячеек игнорируется
                        continue;
                }
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

        ctx.setFill(Color.WHITE);
        ctx.setFont(figureTextFont);
        ctx.setTextAlign(TextAlignment.CENTER);
        double textOffset = figureSize / 2;
        double fontOffset = (figureTextFont.getSize() / 2) * 0.7;
        ctx.strokeText(str, x + textOffset, y + textOffset + fontOffset, figureSize);
        ctx.fillText(str, x + textOffset, y + textOffset + fontOffset, figureSize);
    }

}
