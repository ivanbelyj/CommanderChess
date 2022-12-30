package ivan_belyj.commanderchess.model.availability;

import ivan_belyj.commanderchess.model.FieldData;
import ivan_belyj.commanderchess.model.NodePos;
import javafx.util.Pair;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.function.Consumer;

/* Хранит и предоставляет данные о доступности для какой-либо фигуры */
public class AvailabilityData {
    private final AvailabilityState[][] data;
    public AvailabilityData() {
        data = new AvailabilityState[FieldData.FIELD_NODES_Y][];
        for (int row = 0; row < data.length; row++) {
            data[row] = new AvailabilityState[FieldData.FIELD_NODES_X];
        }
    }

    public void setAll(AvailabilityState state) {
        for (int row = 0; row < data.length; row++) {
            Arrays.fill(data[row], state);
        }
    }

    public void forEach(Consumer<Pair<NodePos, AvailabilityState>> c) {
        for (int row = 0; row < data.length; row++) {
            for (int col = 0; col < data[row].length; col++) {
                c.accept(new Pair<>(new NodePos(col, row), data[row][col]));
            }
        }
    }

    public void set(int x, int y, AvailabilityState state) {
        if (x < 0 || y < 0 || x >= FieldData.FIELD_NODES_X || y >= FieldData.FIELD_NODES_Y)
            return;
        data[y][x] = state;
    }
    public AvailabilityState get(int x, int y) {
        return data[y][x];
    }
}
