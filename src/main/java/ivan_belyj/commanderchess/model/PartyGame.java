package ivan_belyj.commanderchess.model;

import java.util.ArrayList;

/** Представляет игровую партию **/
public class PartyGame {
    private final Player _player1;
    private final Player _player2;

    private FieldData _fieldData;

//    private final ArrayList<Runnable> _startGameEvent = new ArrayList<>();

    private TurnsManager _turnsManager;

    public PartyGame(Player p1, Player p2) {
        _player1 = p1;
        _player2 = p2;
    }

//    public void addStartGameEventListener(Runnable callback) {
//        _startGameEvent.add(callback);
//    }
//    private void fireStartGameEvent() {
//        for (Runnable r : _startGameEvent)
//            r.run();
//    }

    public Player getPlayer1() {
        return _player1;
    }

    public Player getPlayer2() {
        return _player2;
    }

    public FieldData getFieldData() {
        return _fieldData;
    }

    public void startGame() {
        _fieldData = new InitialFieldBuilder().getInitialFieldData(_player1, _player2);
        _turnsManager = new TurnsManager(_player1, _player2);
//        fireStartGameEvent();
    }

    public TurnData nextTurn() {
        return _turnsManager.nextTurn();
    }
}
