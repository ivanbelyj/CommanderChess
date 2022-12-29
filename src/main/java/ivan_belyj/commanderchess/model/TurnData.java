package ivan_belyj.commanderchess.model;

public class TurnData {
    private Player _player;
    private int _turnNumber;

    public TurnData(Player player, int turnNumber) {
        _player = player;
        _turnNumber = turnNumber;
    }

    public Player getPlayer() {
        return _player;
    }

    public int getTurnNumber() {
        return _turnNumber;
    }

    @Override
    public String toString() {
        return "TurnData{" +
                "_player=" + _player +
                ", _turnNumber=" + _turnNumber +
                '}';
    }
}
