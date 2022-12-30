package ivan_belyj.commanderchess.model;

import java.util.List;

/** Данные о расстановке фигур на поле **/
public class FieldData {
    public static final int FIELD_NODES_X = 11;
    public static final int FIELD_NODES_Y = 12;

    public static final int FIELD_CELLS_X = FIELD_NODES_X - 1;
    public static final int FIELD_CELLS_Y = FIELD_NODES_Y - 1;

    private final List<FieldFigure> _figures;

    public FieldData(List<FieldFigure> figures) {
        _figures = figures;
    }

    public List<FieldFigure> getFiguresData() {
        return _figures;
    }

    /** Каждый узел поля имеет свой тип **/
    private FieldCellType nodeType() {
        return null;
    }

    /** В некоторых случаях (в частности, отрисовка) удобнее иметь информацию о типе не узла, а условной клетки из узлов **/
    public static FieldCellType getCellType(int x, int y) {
        if (x < 0 || x > FIELD_NODES_X - 2  // Клеток на 1 меньше, чем узлов
                || y < 0 || y > FIELD_NODES_Y - 2)
            return FieldCellType.Unknown;

        if (y == 5 && (x == 4 || x == 6))
            return FieldCellType.ShallowWaterLeft;
        if (y == 5 && (x == 5 || x == 7))
            return FieldCellType.ShallowWaterRight;

        // Слева - море, по центру - река
        if (x <= 1 || y == 5)
            return FieldCellType.Water;

        return FieldCellType.Unknown;
    }

    /** Свободен ли узел с данными координатами **/
    public boolean isEmpty(int x, int y) {
        for (FieldFigure fig : _figures) {
            if (fig.getPosX() == x && fig.getPosY() == y) {
                return false;
            }
        }
        return true;
    }

    public Player playerAtPos(NodePos pos) {
        FieldFigure fig = getFigureAtPos(pos);
        return fig == null ? null : fig.getFigure().getPlayer();
    }

    public FieldFigure getFigureAtPos(NodePos pos) {
        for (FieldFigure fig : _figures) {
            if (fig.getPosX() == pos.getX() && fig.getPosY() == pos.getY()) {
                return fig;
            }
        }
        return null;
    }
}
