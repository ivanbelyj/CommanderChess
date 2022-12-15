package ivan_belyj.commanderchess.model;

public class TurnsManager {
    private Player _player1;
    private Player _player2;
//    private final ArrayList<Consumer<NewTurnEventArgs>> _newTurnEvent = new ArrayList<Consumer<NewTurnEventArgs>>();

    /** Ссылка на игрока, который совершает действия в данном ходе **/
    private Player _currentTurnPlayer;
    /** Номер текущего хода **/
    private int _turnNumber = 0;

    public TurnsManager(Player player1, Player player2) {
        _player1 = player1;
        _player2 = player2;
    }

//    public void addNewTurnEventListener(Consumer<NewTurnEventArgs> event) {
//        _newTurnEvent.add(event);
//    }
//    private void fireNewTurnEvent() {
//        for (Consumer<NewTurnEventArgs> c : _newTurnEvent)
//            c.accept(new NewTurnEventArgs(_currentTurnPlayer, _turnNumber));

    public TurnData nextTurn() {
        _turnNumber++;
        if (_currentTurnPlayer != null && _currentTurnPlayer.getId() == _player1.getId()) {
            _currentTurnPlayer = _player2;
        } else {
            _currentTurnPlayer = _player1;
        }
        return new TurnData(_currentTurnPlayer, _turnNumber);
    }
}
