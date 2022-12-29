package ivan_belyj.commanderchess;

import ivan_belyj.commanderchess.model.FieldNodeSelectedEventArgs;
import ivan_belyj.commanderchess.model.NodePos;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.function.Consumer;

/** Отвечает за обработку событий мыши на области отрисовки (Canvas) **/
public class MouseHandler {
    private final FieldDrawingData _fieldDrawingData;

    /** Событие выбора игроком узла игрового поля. Также вызывается, когда никакой узел не выбран. В таком случае
     * координаты выбранного узла будут указаны как (-1, -1) **/
    private final ArrayList<Consumer<FieldNodeSelectedEventArgs>> _fieldNodeHoveredEvent = new ArrayList<>();

    private final ArrayList<Consumer<FieldNodeSelectedEventArgs>> _fieldNodeClickedEvent = new ArrayList<>();

    public MouseHandler(FieldDrawingData fieldDrawingData, Canvas canvas) {
        _fieldDrawingData = fieldDrawingData;

        canvas.addEventFilter(MouseEvent.ANY, mouseEvent -> {
            Pair<Integer, Integer> nodePos = fieldNodePosByMousePos(mouseEvent.getX(), mouseEvent.getY());
            FieldNodeSelectedEventArgs eventArgs = new FieldNodeSelectedEventArgs(nodePos.getKey(), nodePos.getValue());
            if (mouseEvent.getEventType() == MouseEvent.MOUSE_MOVED) {
                fireFieldNodeHoveredEvent(eventArgs);
            } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED) {
                fireFieldNodeClickedEvent(eventArgs);
            }
        });
    }
    public void addFieldNodeHoveredEventListener(Consumer<FieldNodeSelectedEventArgs> listener) {
        _fieldNodeHoveredEvent.add(listener);
    }
    private void fireFieldNodeHoveredEvent(FieldNodeSelectedEventArgs args) {
        for (Consumer<FieldNodeSelectedEventArgs> c : _fieldNodeHoveredEvent) {
            c.accept(args);
        }
    }

    public void addFieldNodeClickedEventListener(Consumer<FieldNodeSelectedEventArgs> listener) {
        _fieldNodeClickedEvent.add(listener);
    }
    private void fireFieldNodeClickedEvent(FieldNodeSelectedEventArgs args) {
        for (Consumer<FieldNodeSelectedEventArgs> c : _fieldNodeClickedEvent) {
            c.accept(args);
        }
    }

    private Pair<Integer, Integer> fieldNodePosByMousePos(double x, double y) {
        double a = (x - FieldDrawingData.PADDING_X) / _fieldDrawingData.getCellSize();
        double b = (y - FieldDrawingData.PADDING_Y) / _fieldDrawingData.getCellSize();
        return new Pair<Integer, Integer>((int)Math.round(a), (int)Math.round(b));
    }
}
