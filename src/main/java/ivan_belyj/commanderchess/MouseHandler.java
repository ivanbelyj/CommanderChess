package ivan_belyj.commanderchess;

import ivan_belyj.commanderchess.model.FieldData;
import ivan_belyj.commanderchess.model.FieldNodeSelectedEventArgs;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.function.Consumer;

/** Отвечает за обработку событий мыши на области отрисовки (Canvas) **/
public class MouseHandler {
    private FieldDrawingData _fieldDrawingData;

    /** Событие выбора игроком узла игрового поля. Также вызывается, когда никакой узел не выбран. В таком случае
     * координаты выбранного узла будут указаны как (-1, -1) **/
    private ArrayList<Consumer<FieldNodeSelectedEventArgs>> _fieldNodeSelectedEvent = new ArrayList<>();
    public MouseHandler(FieldDrawingData fieldDrawingData, Canvas canvas) {
        _fieldDrawingData = fieldDrawingData;

        canvas.addEventFilter(MouseEvent.ANY, mouseEvent -> {
            if (mouseEvent.getEventType() == MouseEvent.MOUSE_MOVED) {
                Pair<Integer, Integer> nodePos = fieldNodePosByMousePos(mouseEvent.getX(), mouseEvent.getY());
                fireFieldNodeSelectedEvent(new FieldNodeSelectedEventArgs(nodePos.getKey(), nodePos.getValue()));
            }
        }
        );
    }
    public void addFieldNodeSelectedEventListener(Consumer<FieldNodeSelectedEventArgs> listener) {
        _fieldNodeSelectedEvent.add(listener);
    }
    private void fireFieldNodeSelectedEvent(FieldNodeSelectedEventArgs args) {
        for (Consumer<FieldNodeSelectedEventArgs> c : _fieldNodeSelectedEvent) {
            c.accept(args);
        }
    }

    private Pair<Integer, Integer> fieldNodePosByMousePos(double x, double y) {
        double a = (x - FieldDrawingData.PADDING_X) / _fieldDrawingData.getCellSize();
        double b = (y - FieldDrawingData.PADDING_Y) / _fieldDrawingData.getCellSize();
        return new Pair<Integer, Integer>((int)Math.round(a), (int)Math.round(b));
    }
}
