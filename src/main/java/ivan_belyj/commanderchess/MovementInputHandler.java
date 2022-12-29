package ivan_belyj.commanderchess;

import ivan_belyj.commanderchess.model.NodePos;
import ivan_belyj.commanderchess.model.PartyGame;
import ivan_belyj.commanderchess.model.Player;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class MovementInputHandler {
    private Supplier<PartyGame> gameSupplier;
    public MovementInputHandler(Supplier<PartyGame> gameSupplier) {
        this.gameSupplier = gameSupplier;
    }

    public void handleSelection(NodePos toSelect, Consumer<Boolean> c) {
        boolean canSelect = canSelect(toSelect);
        // Например, общая логика обработки выбора узлов, обновление UI
        c.accept(canSelect);
        if (canSelect) {
            handleSelected(toSelect);
        }
    }
    private boolean canSelect(NodePos toSelect) {
        if (getGame() == null)
            return false;

        // Выбирать можно только свои элементы во время своего хода
        Player playerAtPos = getGame().getFieldData().playerAtPos(toSelect);
        // Клетки без игрока не запрещается выбирать (просто в дальнейшем это не используется)
        return playerAtPos == null ||
                playerAtPos.getId() == getGame().getCurrentTurn().getPlayer().getId();
    }
    private void handleSelected(NodePos selectedPos) {
        if (getGame() == null)
            return;

        System.out.println(selectedPos.toString() + " was selected. current turn: "
                + getGame().getCurrentTurn().toString());
    }

    public PartyGame getGame() {
        return gameSupplier.get();
    }
}
