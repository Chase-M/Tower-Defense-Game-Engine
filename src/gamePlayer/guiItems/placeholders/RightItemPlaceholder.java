package gamePlayer.guiItems.placeholders;

import gamePlayer.guiItems.GuiItem;
import javafx.geometry.Dimension2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * Class is a placeholder item for the RightContainer guiContainer (for testing purposes)
 * @author allankiplagat
 *
 */
public class RightItemPlaceholder extends Pane implements GuiItem {
    
    @Override
    public Node getNode () {
        return this;
    }

    @Override
    public void initialize (Dimension2D containerSize) {
        this.setPrefSize(containerSize.getWidth(), containerSize.getHeight());
        this.setStyle("-fx-background-color: cornsilk;");
    }
}
