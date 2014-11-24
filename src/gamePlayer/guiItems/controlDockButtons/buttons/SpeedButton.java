package gamePlayer.guiItems.controlDockButtons.buttons;

import gamePlayer.guiItems.controlDockButtons.ControlDockButton;
import gamePlayer.guiItemsListeners.SpeedButtonListener;
import gamePlayer.mainClasses.guiBuilder.GuiConstants;

import java.io.File;

import javafx.geometry.Dimension2D;
import javafx.scene.Node;
import utilities.XMLParsing.XMLParser;

public class SpeedButton extends ControlDockButton {
    private String ffImage;
    private String slowImage;
    private SpeedButtonListener myListener = GuiConstants.GUI_MANAGER;

    @Override
    public void initialize (Dimension2D containerSize) {
        super.initialize(containerSize);
        String propertiesPath = GuiConstants.GUI_ELEMENT_PROPERTIES_PATH + myPropertiesPath+this.getClass().getSimpleName()+".XML";
        myParser = new XMLParser(new File(propertiesPath)); 
        
        ffImage = myParser.getValuesFromTag("FastForwardImage").get(0);
        slowImage = myParser.getValuesFromTag("SlowDownImage").get(0);

        setUpSizing (containerSize);
        myButton.setOnAction(event -> fastForward());
        setupImageViews(ffImage, slowImage);
        myButton.setGraphic(myImageView);
    }

    private void fastForward() {
        myListener.fastForward();
        myButton.setOnAction(event-> normalSpeed());
    }
    
    private void normalSpeed() {
        myListener.normalSpeed();
        myButton.setOnAction(event->fastForward());
    }
    
    @Override
    public Node getNode () {
        return myButton;
    }
}
