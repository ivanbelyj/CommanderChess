package ivan_belyj.commanderchess;

import ivan_belyj.commanderchess.model.FieldData;
import javafx.scene.canvas.Canvas;

/** Данные об отображении поля, которые могут потребоваться различным компонентам (в частности, отрисовщику и
 * обработчику событий мыши **/
public class FieldDrawingData {
    private static final double PADDING = 40;  // Поле отображается с отступом от краев окна

    // Рисование линий начинается с учетом отступа
    public static final double PADDING_X = PADDING;
    public static final double PADDING_Y = PADDING;

    /** Размер клетки, включая толщину линии / линий **/
    private double _cellSize;

    private final double _canvasWidth;
    private final double _canvasHeight;

    // Поле можно разделять на клетки (10 x 11) или узлы (11 x 12))
//    private static final int fieldCellsX = FieldData.FIELD_NODES_X - 1;
//    private static final int fieldCellsY = FieldData.FIELD_NODES_Y - 1;

    public FieldDrawingData(Canvas canvas) {
        _canvasWidth = canvas.getWidth();
        _canvasHeight = canvas.getHeight();
        defineCellSize(canvas);
    }

    private void defineCellSize(Canvas canvas) {
        // Размер клетки определяется таким образом, чтобы клетки уместились в окно
        // (а значит, в минимальную из сторон окна)
        double minCanvasSize;
        double minSideCells;
        double minPadding;

        if (canvas.getWidth() < canvas.getHeight()) {
            minCanvasSize = canvas.getWidth();
            minSideCells = FieldData.FIELD_CELLS_X;
            minPadding = FieldDrawingData.PADDING_X;
        } else {
            minCanvasSize = canvas.getHeight();
            minSideCells = FieldData.FIELD_CELLS_Y;
            minPadding = FieldDrawingData.PADDING_Y;
        }
        _cellSize = (minCanvasSize - minPadding * 2) / minSideCells;
    }

    public double getCellSize() {
        return _cellSize;
    }

    public double getCanvasWidth() {
        return _canvasWidth;
    }

    public double getCanvasHeight() {
        return _canvasHeight;
    }
}
