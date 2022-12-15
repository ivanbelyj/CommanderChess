package ivan_belyj.commanderchess.model;

import javafx.scene.paint.Color;

import java.util.UUID;

public class Player {
    private UUID _id;
    private String _name;
    private Color _color;

    public Player(String name, Color color) {
        _id = UUID.randomUUID();
        _name = name;
        _color = color;
    }

    public UUID getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public Color getColor() {
        return _color;
    }
}
