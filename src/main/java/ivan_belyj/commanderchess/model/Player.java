package ivan_belyj.commanderchess.model;

import javafx.scene.paint.Color;

import java.util.UUID;

public class Player {
    private UUID _id;
    private Color _color;

    public Player(UUID id, Color color) {
        _id = id;
        _color = color;
    }

    public UUID getId() {
        return _id;
    }
}
