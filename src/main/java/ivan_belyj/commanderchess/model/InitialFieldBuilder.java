package ivan_belyj.commanderchess.model;

import java.util.ArrayList;

public class InitialFieldBuilder {
    /** Класс для передачи данных о расположении фигуры **/
    private class FigureArrangement {
        public int posX;
        public int posY;
        public FigureType figureType;

        public FigureArrangement(FigureType figureType, int posX, int posY) {
            this.posX = posX;
            this.posY = posY;
            this.figureType = figureType;
        }
    }
    /** Данные о изначальной расстановке фигур **/
    private final FigureArrangement[] _initialFigures = new FigureArrangement[] {
            // 1st row
            new FigureArrangement(FigureType.Commander, 6, 0),
            // 2nd row
            new FigureArrangement(FigureType.Navy, 1, 1),
            new FigureArrangement(FigureType.AirForce, 4, 1),
            new FigureArrangement(FigureType.Headquarters, 5, 1),
            new FigureArrangement(FigureType.Headquarters, 7, 1),
            new FigureArrangement(FigureType.AirForce, 8, 1),
            // 3rd row
            new FigureArrangement(FigureType.Artillery, 3, 2),
            new FigureArrangement(FigureType.Militia, 6, 2),
            new FigureArrangement(FigureType.Artillery, 9, 2),
            // 4th row
            new FigureArrangement(FigureType.Navy, 2, 3),
            new FigureArrangement(FigureType.AntiAircraftGun, 4, 3),
            new FigureArrangement(FigureType.Tank, 5, 3),
            new FigureArrangement(FigureType.Tank, 7, 3),
            new FigureArrangement(FigureType.AntiAircraftGun, 8, 3),
            // 5th row
            new FigureArrangement(FigureType.Infantry, 2, 4),
            new FigureArrangement(FigureType.Engineer, 3, 4),
            new FigureArrangement(FigureType.Militia, 6, 4),
            new FigureArrangement(FigureType.Engineer, 9, 4),
            new FigureArrangement(FigureType.Infantry, 10, 4),
    };
    public FieldData getInitialFieldData(Player player1, Player player2) {
        ArrayList<FieldFigure> figures = new ArrayList<>();
        figures.addAll(getFiguresForPlayer(player1, false));
        figures.addAll(getFiguresForPlayer(player2, true));
        return new FieldData(figures);
    }

    /** Получает экземпляр фигуры, находящийся в начальном состоянии **/
    private Figure getInitialFigure(Player player, FigureType figureType) {
        // Todo
        return new Figure(player, figureType);
    }

    /** invert устанавливается в true, если требуется получить инвертированную расстановку фигур, т.е,
     * расстановку в нижней части доски **/
    private ArrayList<FieldFigure> getFiguresForPlayer(Player player, boolean invert) {
        ArrayList<FieldFigure> figures = new ArrayList<>();

        for (FigureArrangement arrangement : _initialFigures) {
            int x = arrangement.posX;
            int y = invert ? FieldData.FIELD_NODES_Y - arrangement.posY - 1 : arrangement.posY;
            figures.add(new FieldFigure(getInitialFigure(player, arrangement.figureType), x, y));
        }

        return figures;
    }
}
