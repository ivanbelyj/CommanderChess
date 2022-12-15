package ivan_belyj.commanderchess.model;

public class FieldNodeSelectedEventArgs {
    private int _posX;
    private int _posY;

    public FieldNodeSelectedEventArgs(int posX, int posY) {
        _posX = posX;
        _posY = posY;
    }

    public int getPosX() {
        return _posX;
    }

    public int getPosY() {
        return _posY;
    }
}
