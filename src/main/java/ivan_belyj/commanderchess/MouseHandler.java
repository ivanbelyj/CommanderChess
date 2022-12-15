package ivan_belyj.commanderchess;

import ivan_belyj.commanderchess.model.FieldData;
import ivan_belyj.commanderchess.model.FieldNodeSelectedEventArgs;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.function.Consumer;

/** Отвечает за обработку событий мыши на области отрисовки (Canvas) **/
public class MouseHandler {
    private FieldDrawingData _fieldDrawingData;
    private Canvas _canvas;

    /** Событие выбора игроком узла игрового поля. Также вызывается, когда никакой узел не выбран. В таком случае
     * координаты выбранного узла будут указаны как (-1, -1) **/
    private ArrayList<Consumer<FieldNodeSelectedEventArgs>> _fieldNodeSelectedEvent = new ArrayList<>();
    public MouseHandler(FieldDrawingData fieldDrawingData, Canvas canvas) {
        _fieldDrawingData = fieldDrawingData;
        _canvas = canvas;

//        Rectangle rectBound = new Rectangle();
//        rectBound.setX(FieldDrawingData.PADDING_X);
//        rectBound.setY(FieldDrawingData.PADDING_Y);
//        rectBound.setWidth(canvas.getWidth() - FieldDrawingData.PADDING_X * 2);
//        rectBound.setHeight(canvas.getHeight() - FieldDrawingData.PADDING_Y * 2);

        canvas.addEventFilter(MouseEvent.ANY, new EventHandler<MouseEvent>() {
//            boolean shouldDraw = false;
//            double dX;
//            double dY;

            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getEventType() == MouseEvent.MOUSE_MOVED) {
                    // Todo: определить координаты выбранного узла
                    Pair<Integer, Integer> nodePos = fieldNodePosByMousePos(mouseEvent.getX(), mouseEvent.getY());
                    fireFieldNodeSelectedEvent(new FieldNodeSelectedEventArgs(nodePos.getKey(), nodePos.getValue()));
                } // else if (mouseEvent.getEventType() == MouseEvent.MOUSE_EXITED) {
                //    fireFieldNodeSelectedEvent(new FieldNodeSelectedEventArgs(-1, -1));
                //}
                // else if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
//                    shouldDraw = rectBound.contains(mouseEvent.getX(), mouseEvent.getY());
//                    dX = mouseEvent.getX();
//                    dY = mouseEvent.getY();
//                } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
//                    if (shouldDraw) {
//                        double x = (mouseEvent.getX() + rectBound.getX() - dX),
//                                y = (mouseEvent.getY() + rectBound.getY() - dY);
//
//                        canvas.getGraphicsContext2D().clearRect(rectBound.getX(),
//                                rectBound.getY(), rectBound.getWidth(),//50
//                                rectBound.getHeight());
//
//                        rectBound.setY(y);
//                        rectBound.setX(x);
//
//                        dX = mouseEvent.getX();
//                        dY = mouseEvent.getY();
//                    }
//                }
            }
        });
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
