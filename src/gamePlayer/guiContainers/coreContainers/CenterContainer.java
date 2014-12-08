package gamePlayer.guiContainers.coreContainers;

import gamePlayer.guiContainers.GuiContainer;
import gamePlayer.mainClasses.guiBuilder.GuiConstants;
import gamePlayer.mainClasses.guiBuilder.GuiElement;

import java.io.File;
import java.util.List;

import javafx.geometry.Dimension2D;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import utilities.XMLParsing.XMLParser;
import utilities.reflection.Reflection;

/**
 * GuiContainer that hosts GuiElements at the center of the screen
 * 
 * @author allankiplagat
 *
 */
public class CenterContainer extends Pane implements GuiContainer {
	private XMLParser myParser;

	@Override
	public void initialize(Dimension2D containerSize) {
		
		CoreContainerSetup c = new CoreContainerSetup();
		c.initialize(this, GuiConstants.CENTER_CONTAINER_WIDTH, GuiConstants.CENTER_CONTAINER_HEIGHT, containerSize);
		this.getChildren().addAll(c.getChildList());
	}

	@Override
	public Node getNode() {
		return this;
	}
}
