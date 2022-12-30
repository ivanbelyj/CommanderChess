package ivan_belyj.commanderchess.movement_input;

import ivan_belyj.commanderchess.model.FieldFigure;
import ivan_belyj.commanderchess.model.NodePos;

public class FigureToMoveSelectedEventArgs {
    private FieldFigure fieldFigure;
    private NodePos[] availableToMove;

    public FigureToMoveSelectedEventArgs(FieldFigure fieldFigure, NodePos[] availableToMove) {
        this.fieldFigure = fieldFigure;
        this.availableToMove = availableToMove;
    }

    public FieldFigure getFieldFigure() {
        return fieldFigure;
    }

    public NodePos[] getAvailableToMove() {
        return availableToMove;
    }
}
