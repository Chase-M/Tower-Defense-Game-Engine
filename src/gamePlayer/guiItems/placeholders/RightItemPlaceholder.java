package gamePlayer.guiItems.placeholders;

import gamePlayer.guiItems.GuiItemInterface;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.layout.Pane;


public class RightItemPlaceholder extends Pane implements GuiItemInterface {

    @Override
    public void initialize (List<Double> componentSize) {
        this.setPrefSize(componentSize.get(0)*1, componentSize.get(1)*1);
        this.setStyle("-fx-background-color: cornsilk;");
    }

    @Override
    public Node getNode () {
        return this;
    }
}
