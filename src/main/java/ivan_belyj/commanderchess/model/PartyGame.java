package ivan_belyj.commanderchess.model;

import java.util.ArrayList;

/** Представляет игровую партию **/
public class PartyGame {
    private Player _player1;
    private Player _player2;

    private FieldData _fieldData;

    private ArrayList<Runnable> _startGameEvent = new ArrayList<>();

    public PartyGame(Player p1, Player p2) {
        _player1 = p1;
        _player2 = p2;
    }

    public void addStartGameEventListener(Runnable callback) {
        _startGameEvent.add(callback);
    }
    private void fireStartGameEventListener() {
        for (Runnable r : _startGameEvent)
            r.run();
    }

    public void startGame() {
        _fieldData = getInitialFieldData();
        fireStartGameEventListener();
    }

    private FieldData getInitialFieldData() {
        ArrayList<FieldFigure> figures = new ArrayList<>();

        // Todo: расстановка фигур перед игрой
        figures.add(new FieldFigure(new Figure(_player1, FigureType.Commander), 1, 1));
        figures.add(new FieldFigure(new Figure(_player1, FigureType.AntiAircraftGun), 4, 2));
        return new FieldData(figures);
    }

    public Player getPlayer1() {
        return _player1;
    }

    public Player getPlayer2() {
        return _player2;
    }

    public FieldData getFieldData() {
        return _fieldData;
    }
}
