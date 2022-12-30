package ivan_belyj.commanderchess.movement_input;

import ivan_belyj.commanderchess.model.FieldFigure;
import ivan_belyj.commanderchess.model.NodePos;
import ivan_belyj.commanderchess.model.PartyGame;
import ivan_belyj.commanderchess.model.Player;
import ivan_belyj.commanderchess.model.availability.AvailabilityData;
import ivan_belyj.commanderchess.model.availability.AvailabilityDataBuilder;
import ivan_belyj.commanderchess.model.availability.AvailabilityState;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MovementInputHandler {
    private Supplier<PartyGame> gameSupplier;
    private FieldFigure selectedFigure;
    private ArrayList<Consumer<FigureToMoveSelectedEventArgs>> figureToMoveSelectedEvent;
    private ArrayList<Runnable> selectionResetEvent;
    public MovementInputHandler(Supplier<PartyGame> gameSupplier) {
        this.gameSupplier = gameSupplier;
        figureToMoveSelectedEvent = new ArrayList<>();
        selectionResetEvent = new ArrayList<>();
    }

    public void addFigureToMoveSelectedEventListener(Consumer<FigureToMoveSelectedEventArgs> c) {
        figureToMoveSelectedEvent.add(c);
    }

    private void fireFigureToMoveSelectedEvent(FigureToMoveSelectedEventArgs args) {
        for (Consumer<FigureToMoveSelectedEventArgs> c : figureToMoveSelectedEvent) {
            c.accept(args);
        }
    }

    public void addSelectionResetEventEventListener(Runnable r) {
        selectionResetEvent.add(r);
    }

    private void fireSelectionResetEvent() {
        for (Runnable r : selectionResetEvent) {
            r.run();
            System.out.println("reset selection!");
        }
    }

    /* Метод, вызываемый из любого внешнего кода выбора элементов */
    public void handleSelection(NodePos toSelect, Consumer<Boolean> c) {
        boolean canSelect = canSelect(toSelect);
        // Например, общая логика обработки выбора узлов, обновление UI
        c.accept(canSelect);
        if (canSelect) {
            handleSelected(toSelect);
        } else
            fireFigureToMoveSelectedEvent(new FigureToMoveSelectedEventArgs(null, null));
    }
    private boolean canSelect(NodePos toSelect) {
        if (getGame() == null)
            return false;

        // Выбирать можно только свои элементы во время своего хода
        Player playerAtPos = getGame().getFieldData().playerAtPos(toSelect);
        // Клетки без игрока не запрещается выбирать
        return playerAtPos == null ||
                playerAtPos.getId() == getGame().getCurrentTurn().getPlayer().getId();
    }
    private void handleSelected(NodePos selectedPos) {
        if (getGame() == null)
            return;

        // Если до этого уже была выбрана фигура, значит, сейчас выбирается целевая точка
        // для перемещения
        if (selectedFigure != null) {
            handleTargetNode();
        }

        selectedFigure = getGame().getFieldData().getFigureAtPos(selectedPos);
        if (selectedFigure == null)
            return;

        // Фигура обрабатывается
        handleFigureToMove();
    }

    private void handleFigureToMove() {
        System.out.println(selectedFigure.getFigure().getFigureType() + " was selected. current turn: "
                + getGame().getCurrentTurn().toString());

        NodePos[] availableToMove = createAvailable(selectedFigure);
        FigureToMoveSelectedEventArgs args = new FigureToMoveSelectedEventArgs(selectedFigure, availableToMove);
        fireFigureToMoveSelectedEvent(args);
    }

    private void handleTargetNode() {
        System.out.println("target node: " + selectedFigure.toString());
        selectedFigure = null;
        // fireFigureToMoveSelectedEvent(new FigureToMoveSelectedEventArgs(null, null));
        fireSelectionResetEvent();
    }

    private NodePos[] createAvailable(FieldFigure figure) {
        AvailabilityDataBuilder adb = new AvailabilityDataBuilder();
        AvailabilityData data = adb.getAvailabilities(figure, getGame().getFieldData());

        ArrayList<NodePos> free = new ArrayList<>();
        data.forEach(pair -> {
            if (pair.getValue() == AvailabilityState.FREE) {
                free.add(pair.getKey());
            }
        });
        return free.toArray(NodePos[]::new);
    }

    private PartyGame getGame() {
        return gameSupplier.get();
    }
}
