package ivan_belyj.commanderchess.model;

/** Информация о фигуре **/
public class Figure {
    private Player _player;
    private FigureType _figureType;

    public Figure(Player player, FigureType type) {
        _player = player;
        _figureType = type;
    }

    /** Игрок, которому принадлежит фигура **/
    public Player getPlayer() {
        return _player;
    }

    public FigureType getFigureType() {
        return _figureType;
    }


}
