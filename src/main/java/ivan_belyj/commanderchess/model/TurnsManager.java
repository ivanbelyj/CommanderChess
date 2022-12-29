package ivan_belyj.commanderchess.model;

public class TurnsManager {
    private Player player1;
    private Player player2;
//    private final ArrayList<Consumer<NewTurnEventArgs>> _newTurnEvent = new ArrayList<Consumer<NewTurnEventArgs>>();

    /** Ссылка на игрока, который совершает действия в данном ходе **/
    private Player currentTurnPlayer;
    /** Номер текущего хода **/
    private int turnNumber = 0;

    public TurnsManager(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

//    public void addNewTurnEventListener(Consumer<NewTurnEventArgs> event) {
//        _newTurnEvent.add(event);
//    }
//    private void fireNewTurnEvent() {
//        for (Consumer<NewTurnEventArgs> c : _newTurnEvent)
//            c.accept(new NewTurnEventArgs(_currentTurnPlayer, _turnNumber));

    public TurnData nextTurn() {
        turnNumber++;
        if (currentTurnPlayer != null && currentTurnPlayer.getId() == player1.getId()) {
            currentTurnPlayer = player2;
        } else {
            currentTurnPlayer = player1;
        }
        return new TurnData(currentTurnPlayer, turnNumber);
    }
}
