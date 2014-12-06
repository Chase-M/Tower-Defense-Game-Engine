package gamePlayer.guiFeatures;

import gamePlayer.mainClasses.guiBuilder.GuiConstants;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Singleton class to drag towers from the store and place them on the game map
 * 
 * @author brianbolze
 */

public class TowerPlacer {

    private static TowerPlacer myReference = null;

    public static TowerPlacer getInstance() {
        if (myReference==null) {
            myReference = new TowerPlacer();
        }
        return myReference;
    }

    /**
     * Drags an item and attempts to place it on the map
     * 
     * @param rootNode : The Node to attach the mouseListener to
     * @param itemID : The unique identifier of the item (tower) to be placed
     */
    public void placeItem(String itemID, Pane rootNode) {

        Circle dragCircle = new Circle(50, Color.RED);
        dragCircle.setOpacity(0.5);

        // TODO : Add the image of the tower to be added to the dragged object
        rootNode.setOnMouseMoved(event -> drag(dragCircle, event.getX(), event.getY()));
        rootNode.setOnMouseReleased(event -> drop(dragCircle, event.getX(), event.getY(), itemID, rootNode));
        rootNode.getChildren().add(dragCircle);
        
    }

    private void drag(Circle node, double X, double Y) {
        node.setTranslateX(X);
        node.setTranslateY(Y);
        if (validPlacement(X,Y)) {
            node.setFill(Color.GREEN);
        } else {
            node.setFill(Color.RED);
        }
    }

    private void drop(Circle node, double X, double Y, String towerID, Node rootNode) {

        if (!validPlacement(X,Y)) {
            GuiConstants.GUI_MANAGER.displayMessage("Invalid placement", true);
            return;
        }

        rootNode.setOnMouseMoved(null);
        rootNode.setOnMouseReleased(null);
        node.setVisible(false);

        GuiConstants.GUI_MANAGER.makeTower(towerID, X, Y);
    }

    private boolean validPlacement(double X, double Y) {
    	return GuiConstants.GUI_MANAGER.validPlacement(X,Y); 
    }
    
}
