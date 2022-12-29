package ivan_belyj.commanderchess.model;

/** Позиция на игровом поле **/
public class NodePos {
    private int x;
    private int y;

    public NodePos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", getX(), getY());
    }
}
