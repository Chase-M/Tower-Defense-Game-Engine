package gameAuthoring.scenes.pathBuilding.buildingPanes;

import gameAuthoring.scenes.pathBuilding.pathComponents.Path;
import gameAuthoring.scenes.pathBuilding.pathComponents.PathComponent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Shape;

/**
 * Allows the user to select a route by clicking on PathComponents.
 * When the user clicks on a component, the component becomes green and the user
 * can drag it to a new spot if it is not connected to a starting or ending location
 * or the user can remove it. 
 * @author Austin Kyker
 *
 */
public class SelectComponentPane extends BuildingPane {

    private Path myPath;
    private double mouseX;
    private double mouseY;

    public SelectComponentPane (Group group, Path path) {
        super(group);
        myPath = path;        
    }
    
    public void addListenersToComponents() {
        for(PathComponent component:myPath.getAllPathComponents()){
            addSelectionListeners(component);
        }
    }

    private void addSelectionListeners(PathComponent component){
        ((Shape) component).setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle (MouseEvent event) {
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();   
            }

        });
        ((Shape) component).setOnMouseDragged(new EventHandler<MouseEvent>(){
            @Override
            public void handle (MouseEvent event) {
                double deltaX = event.getSceneX() - mouseX;
                double deltaY = event.getSceneY() - mouseY;
                myPath.moveConnectedComponent(component, deltaX, deltaY);
                mouseX = event.getSceneX();
                mouseY = event.getSceneY(); 
            }                      
        });        
        ((Shape) component).setOnMouseReleased(event->handleSelectionAndTryToConnectComponents(component));
    }

    private void handleSelectionAndTryToConnectComponents (PathComponent component) {
        myPath.attemptToConnectRoutes(component);
        myPath.handleComponentSelection(component);
    }
}