package gamePlayer.guiItems.placeholders;

import gamePlayer.guiItems.GuiItem;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.layout.Pane;


public class CenterItemPlaceholder extends Pane implements GuiItem {

    @Override
    public Node getNode () {
        return this;
    }

    @Override
    public void initialize (List<Double> containerSize) {
        this.setPrefSize(containerSize.get(0)*1, containerSize.get(1)*1);
        this.setStyle("-fx-background-color: yellow;");
    }
}
