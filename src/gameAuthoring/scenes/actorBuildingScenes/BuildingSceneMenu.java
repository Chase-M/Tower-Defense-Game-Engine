package gameAuthoring.scenes.actorBuildingScenes;

import java.util.Observable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/**
 * A menu to allow the user to go to the next scene.
 * @author Austin Kyker
 *
 */
public class BuildingSceneMenu extends Observable {
    
    private MenuBar myMenuBar;
    private Menu myFileMenu;
    
    public BuildingSceneMenu() {
        myMenuBar = new MenuBar();
        myFileMenu = new Menu("File");
        MenuItem finishedBuildingItem = new MenuItem("Finished");
        finishedBuildingItem.setOnAction(event->handleClick());
        myFileMenu.getItems().add(finishedBuildingItem);
        myMenuBar.getMenus().add(myFileMenu);
    }

    private void handleClick () {
        this.setChanged();
        this.notifyObservers();
    }

    public MenuBar getNode () {
        return myMenuBar;
    }

    public void addMenuItemToFileMenu (MenuItem item) {
        myFileMenu.getItems().add(0, item);        
    }
}
