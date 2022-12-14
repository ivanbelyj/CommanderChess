package ivan_belyj.commanderchess.model;

/** Данные о фигуре, касающиеся расстановки на поле (где фигура стоит и на ком) **/
public class FieldFigure {
    private Figure _figure;
    private int _posX;
    private int _posY;

    public FieldFigure(Figure figure, int posX, int posY) {
        _figure = figure;
        _posX = posX;
        _posY = posY;
    }

    public Figure getFigure() {
        return _figure;
    }

    public int getPosX() {
        return _posX;
    }

    public int getPosY() {
        return _posY;
    }
}
