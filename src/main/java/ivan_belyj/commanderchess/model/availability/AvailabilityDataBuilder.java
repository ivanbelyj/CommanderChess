package ivan_belyj.commanderchess.model.availability;

import ivan_belyj.commanderchess.model.FieldData;
import ivan_belyj.commanderchess.model.FieldFigure;

/* На основе данных о типе перемещения фигуры и данных поля получает возможные перемещения
* для фигуры по полю */
public class AvailabilityDataBuilder {
    public AvailabilityData getAvailabilities(FieldFigure fieldFigure, FieldData fieldData) {
        AvailabilityData res = new AvailabilityData();
        // test
        res.set(fieldFigure.getPosX() - 1, fieldFigure.getPosY() - 1, AvailabilityState.FREE);
        res.set(fieldFigure.getPosX(), fieldFigure.getPosY() - 1, AvailabilityState.FREE);
        res.set(fieldFigure.getPosX() + 1, fieldFigure.getPosY() - 1, AvailabilityState.FREE);
        res.set(fieldFigure.getPosX() - 1, fieldFigure.getPosY(), AvailabilityState.FREE);
        res.set(fieldFigure.getPosX() + 1, fieldFigure.getPosY() - 1, AvailabilityState.FREE);
        res.set(fieldFigure.getPosX() + 1, fieldFigure.getPosY(), AvailabilityState.FREE);
        res.set(fieldFigure.getPosX() + 1, fieldFigure.getPosY() + 1, AvailabilityState.FREE);
        res.set(fieldFigure.getPosX() - 1, fieldFigure.getPosY() + 1, AvailabilityState.FREE);
        res.set(fieldFigure.getPosX(), fieldFigure.getPosY() + 1, AvailabilityState.FREE);
        // res.setAll(AvailabilityState.FREE);
        return res;
    }
}
