package gamePlayer.guiItems;

import gamePlayer.mainClasses.GuiElement;
import gamePlayer.mainClasses.guiBuilder.GuiConstants;


/**
 * Tag interface for a GuiItem which is hosted by a GuiContainer
 * A GuiItem is a GUI module that performs particular functions e.g. a MenuBar, StatsBoard, etc.
 * Known implementing classes: VoogaMenuBar and StatsBoard classes
 * @author allankiplagat
 *
 */
public interface GuiItem extends GuiElement  {
    public static final String myPropertiesPath =  GuiConstants.GUI_ELEMENT_PROPERTIES_PATH + "guiItems/";
}
