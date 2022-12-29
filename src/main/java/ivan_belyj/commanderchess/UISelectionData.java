package ivan_belyj.commanderchess;

import ivan_belyj.commanderchess.model.NodePos;

/** Данные о selection UI, которые должны быть учтены при отрисовке **/
public class UISelectionData {
    private NodePos selectedPos;
    private NodePos hoverPos;
    private NodePos[] available;

    public UISelectionData() {

    }

    public NodePos getSelectedPos() {
        return selectedPos;
    }

    public NodePos[] getAvailable() {
        return available;
    }

    public void setSelectedPos(NodePos selected) {
        this.selectedPos = selected;
    }

    public void setAvailable(NodePos[] available) {
        this.available = available;
    }

    public NodePos getHoverPos() {
        return hoverPos;
    }

    public void setHoverPos(NodePos hoverPos) {
        this.hoverPos = hoverPos;
    }
}
