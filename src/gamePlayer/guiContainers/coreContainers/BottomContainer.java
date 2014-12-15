package gamePlayer.guiContainers.coreContainers;

import gamePlayer.guiContainers.GuiContainer;
import gamePlayer.guiItems.towerUpgrade.StandardBorder;
import gamePlayer.mainClasses.guiBuilder.GuiConstants;
import gamePlayer.mainClasses.guiBuilder.GuiElement;

import java.io.File;
import java.util.List;

import javafx.geometry.Dimension2D;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import utilities.XMLParsing.XMLParser;
import utilities.reflection.Reflection;

/**
 * GuiContainer that hosts GuiElements at the bottom of the screen
 * @author allankiplagat
 *
 */
public class BottomContainer extends HBox implements GuiContainer {

    @Override
    public void initialize (Dimension2D containerSize) {
        CoreContainerSetup c = new CoreContainerSetup();
        this.getChildren().
        addAll(c.initialize(this, GuiConstants.BOTTOM_CONTAINER_WIDTH, GuiConstants.BOTTOM_CONTAINER_HEIGHT, containerSize,myPropertiesPath));
    }

    @Override
    public Node getNode () {
        return this;
    }   
}
