package ivan_belyj.commanderchess;

/** Данные о деталях UI, которые должны быть учтены при отрисовке **/
public class UIDrawingData {
    private final int _selectedPosX;
    private final int _selectedPosY;

    public UIDrawingData(int selectedPosX, int selectedPosY) {
        _selectedPosX = selectedPosX;
        _selectedPosY = selectedPosY;
    }

    public int getSelectedPosX() {
        return _selectedPosX;
    }

    public int getSelectedPosY() {
        return _selectedPosY;
    }
}
