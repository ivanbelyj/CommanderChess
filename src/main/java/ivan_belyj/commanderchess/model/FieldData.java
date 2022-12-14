package ivan_belyj.commanderchess.model;

import java.util.ArrayList;
import java.util.List;

/** Данные о расстановки фигур на поле **/
public class FieldData {
    public static final int FIELD_CELLS_X = 10;
    public static final int FIELD_CELLS_Y = 11;

    public static final int FIELD_NODES_X = 11;
    public static final int FIELD_NODES_Y = 12;

    private final List<FieldFigure> _figures;

    public FieldData(List<FieldFigure> figures) {
        _figures = figures;
    }

    /** Расстановка фигур для начала игры **/
    public FieldData(Player player1, Player player2) {
        _figures = new ArrayList<>();
        // Todo: расстановка фигур перед игрой
        _figures.add(new FieldFigure(new Figure(player1, FigureType.Commander), 1, 1));
    }

    public List<FieldFigure> getFiguresData() {
        return _figures;
    }
}
