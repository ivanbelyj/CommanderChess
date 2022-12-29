package ivan_belyj.commanderchess;

import ivan_belyj.commanderchess.model.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.function.Supplier;

/** Класс отрисовки UI игры на Canvas **/
public class Drawer {
    private UISelectionData uiDrawing;
    private FieldDrawingData fieldDrawing;

    /** Размер фигуры **/
    private final double figureSize;

    private static final Paint linePaint = Color.DARKGRAY;
    private static final Paint waterPaint = Color.SKYBLUE;
    private static final Paint backgroundPaint = Color.WHITE;

    private static final Paint hoverPaint = Color.rgb(0, 0, 0, 0.2);
    private static final Paint selectionPaint = Color.rgb(80, 0, 255, 0.2);
    private static final Paint availablePaint = Color.rgb(0, 255, 80, 0.2);
    private static final Paint destroyAvailablePaint = Color.rgb(255, 0, 80, 0.2);

    // Используется для градиента далее
    private static final Stop[] leftStops = new Stop[] { new Stop(0, Color.SKYBLUE), new Stop(0.8, Color.WHITE) };
    private static final Stop[] rightStops = new Stop[] { new Stop(0.2, Color.WHITE), new Stop(1, Color.SKYBLUE) };
    // Клетки, где мелководье находится справа и слева, отрисовываются по-разному
    private static final Paint shallowWaterLeftPaint = new LinearGradient(0, 0.5, 1, 0.5, true, CycleMethod.NO_CYCLE, leftStops);
    private static final Paint shallowWaterRightPaint = new LinearGradient(0, 0.5, 1, 0.5, true, CycleMethod.NO_CYCLE, rightStops);

    private static final Font figureTextFont = new Font(16);

    // Следующие поля используются / устанавливаются во время отрисовки
    private GraphicsContext ctx;

    // Координаты последнего узла сетки
    private double lastNodeX;
    private double lastNodeY;

    private Supplier<FieldData> supplyFiguresData;
    public void setFiguresDataSupplier(Supplier<FieldData> supplyFiguresData) {
        this.supplyFiguresData = supplyFiguresData;
    }

    public Drawer(FieldDrawingData fieldDrawing, GraphicsContext ctx) {
        this.fieldDrawing = fieldDrawing;
        this.ctx = ctx;

        figureSize = fieldDrawing.getCellSize() * 0.8;
    }

    public void draw(UISelectionData drawingData) {
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
            drawSelections();
        if (supplyFiguresData != null && supplyFiguresData.get().getFiguresData() != null)
            drawFigures();
    }

    private void drawBackground() {
        ctx.setFill(backgroundPaint);
        ctx.fillRect(0, 0, fieldDrawing.getCanvasWidth(), fieldDrawing.getCanvasHeight());
    }

    private void drawSelections() {
        if (uiDrawing.getHoverPos() != null) {
            NodePos hover = uiDrawing.getHoverPos();
            drawCircle(hover.getX(), hover.getY(), hoverPaint);
        }
        if (uiDrawing.getSelectedPos() != null) {
            NodePos selected = uiDrawing.getSelectedPos();
            drawCircle(selected.getX(), selected.getY(), selectionPaint);
        }
        if (uiDrawing.getAvailable() != null) {
            System.out.println("draw available");
            drawCircles(uiDrawing.getAvailable(), availablePaint);
        }

    }

    private void drawCircles(NodePos[] positions, Paint paint) {
        for (NodePos nodePos : positions) {
            drawCircle(nodePos.getX(), nodePos.getY(), paint);
        }
    }

    private void drawCircle(int x, int y, Paint paint) {
        ctx.setFill(paint);
        double xPx = getCoordInCanvasByPosInField(x, false);
        double yPx = getCoordInCanvasByPosInField(y, true);
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
        for (FieldFigure fig : this.supplyFiguresData.get().getFiguresData()) {
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
