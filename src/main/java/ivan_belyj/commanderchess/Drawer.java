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
    private UIDrawingData uiDrawing;
    private FieldDrawingData fieldDrawing;

    private PartyGame currentGame;

    /** Размер фигуры **/
    private final double figureSize;

    private static final Paint linePaint = Color.DARKGRAY;
    private static final Paint waterPaint = Color.SKYBLUE;
    private static final Paint backgroundPaint = Color.WHITE;

    private static final Paint selectionPaint = Color.rgb(0, 0, 0, 0.2);

    // Используется для градиента далее
    private static final Stop[] leftStops = new Stop[] { new Stop(0, Color.SKYBLUE), new Stop(0.8, Color.WHITE) };
    private static final Stop[] rightStops = new Stop[] { new Stop(0.2, Color.WHITE), new Stop(1, Color.SKYBLUE) };
    // Клетки, где мелководье находится справа и слева, отрисовываются по-разному
    private static final Paint shallowWaterLeftPaint = new LinearGradient(0, 0.5, 1, 0.5, true, CycleMethod.NO_CYCLE, leftStops);
    private static final Paint shallowWaterRightPaint = new LinearGradient(0, 0.5, 1, 0.5, true, CycleMethod.NO_CYCLE, rightStops);

    private static final Font figureTextFont = new Font(24);

    // Следующие поля используются / устанавливаются во время отрисовки
    private GraphicsContext ctx;

    // Координаты последнего узла сетки
    private double lastNodeX;
    private double lastNodeY;

    public Drawer(FieldDrawingData fieldDrawing, GraphicsContext ctx) {
        this.fieldDrawing = fieldDrawing;
        this.ctx = ctx;

        figureSize = fieldDrawing.getCellSize() * 0.8;
    }

    public void setCurrentGame(PartyGame currentGame) {
        this.currentGame = currentGame;
    }
    public PartyGame getCurrentGame() {
        return currentGame;
    }

    public void draw(UIDrawingData drawingData) {
        this.uiDrawing = drawingData;
        draw();
    }

    public void draw() {
        this.lastNodeX = (fieldDrawing.getCellSize()) * FieldData.FIELD_CELLS_X + FieldDrawingData.PADDING_X;
        this.lastNodeY = (fieldDrawing.getCellSize()) * FieldData.FIELD_CELLS_Y + FieldDrawingData.PADDING_Y;

        drawBackground();

        drawCells();
        drawLines();
        drawBorder();

        if (uiDrawing != null)
            drawUI();

        if (currentGame != null)
            drawFigures();
    }

    private void drawBackground() {
        ctx.setFill(backgroundPaint);
        ctx.fillRect(0, 0, fieldDrawing.getCanvasWidth(), fieldDrawing.getCanvasHeight());
    }

    private void drawUI() {
        int x = uiDrawing.getSelectedPosX();
        int y = uiDrawing.getSelectedPosY();
        // Если была выбрана точка за пределами сетки узлов
        if (x < 0 || y < 0 || x >= FieldData.FIELD_NODES_X || y >= FieldData.FIELD_NODES_Y
                || currentGame == null || currentGame.getFieldData().isEmpty(x, y)) {
            uiDrawing = null;
            return;
        }

        ctx.setFill(selectionPaint);
        double xPx = getCoordInCanvasByPosInField(uiDrawing.getSelectedPosX(), false);
        double yPx = getCoordInCanvasByPosInField(uiDrawing.getSelectedPosY(), true);
        double size = figureSize * 1.5;
        ctx.fillOval(xPx - size / 2, yPx - size / 2, size, size);
    }

    private void drawCells() {
        double xPx = FieldDrawingData.PADDING_X;
        double yPx = FieldDrawingData.PADDING_Y;

        double cellSize = fieldDrawing.getCellSize();

        for (int x = 0; x < FieldData.FIELD_CELLS_X + 1; x++, xPx += cellSize) {
            for (int y = 0; y < FieldData.FIELD_CELLS_Y + 1; y++, yPx += cellSize) {
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
            yPx = FieldDrawingData.PADDING_Y;
        }
    }

    private void drawLines() {
        // Отображение линий
        double xPx = FieldDrawingData.PADDING_X;
        double yPx = FieldDrawingData.PADDING_Y;

        ctx.setStroke(linePaint);

        // Для избежания дублирующейся рисовки вертикальных линий
        boolean drawVertLines = true;
        for (int x = 0; x <  FieldData.FIELD_CELLS_X + 1; x++) {
            // Горизонтальная линия
            ctx.strokeLine(xPx, yPx, xPx, lastNodeY);

            for (int y = 0; y <  FieldData.FIELD_CELLS_Y + 1; y++) {
                // ctx.strokeOval(xPx, yPx, 10, 10);

                if (drawVertLines) {
                    // Вертикальная линия
                    ctx.strokeLine(xPx, yPx, lastNodeX, yPx);
                }

                yPx += fieldDrawing.getCellSize();
            }
            if (drawVertLines)
                drawVertLines = false;

            xPx += fieldDrawing.getCellSize();
            yPx = FieldDrawingData.PADDING_Y;
        }
    }

    private void drawBorder() {
        double paddingX = FieldDrawingData.PADDING_X;
        double paddingY = FieldDrawingData.PADDING_Y;
        // Границы поля выделяются красным
        ctx.setStroke(Color.MEDIUMVIOLETRED);
        ctx.strokeLine(paddingX, paddingY, paddingX, lastNodeY);
        ctx.strokeLine(paddingX, paddingY, lastNodeX, paddingY);
        ctx.strokeLine(lastNodeX, paddingY, lastNodeX, lastNodeY);
        ctx.strokeLine(paddingX, lastNodeY, lastNodeX, lastNodeY);
    }

    private void drawFigures() {
        for (FieldFigure fig : currentGame.getFieldData().getFiguresData()) {
//            double x = FieldDrawingData.PADDING_X + cellSize * fig.getPosX();
//            double y = FieldDrawingData.PADDING_Y + cellSize * fig.getPosY();
            double x = getCoordInCanvasByPosInField(fig.getPosX(), false);
            double y = getCoordInCanvasByPosInField(fig.getPosY(), true);
            drawFigure(fig.getFigure(), x - figureSize / 2, y - figureSize / 2);
        }
    }

    private double getCoordInCanvasByPosInField(int pos, boolean isY) {
        return (isY ? FieldDrawingData.PADDING_Y : FieldDrawingData.PADDING_X) + fieldDrawing.getCellSize() * pos;
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
