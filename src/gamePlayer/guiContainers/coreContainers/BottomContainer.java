package gamePlayer.guiContainers.coreContainers;

import gamePlayer.guiContainers.GuiContainer;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import javafx.scene.layout.HBox;
import Utilities.XMLParsing.XMLParser;
import Utilities.XMLParsing.XMLParserInstantiator;

public class BottomContainer extends HBox implements GuiContainer {
    private XMLParser myParser;

    @Override
    public void initialize (List<Double> windowSize) {
        myParser = XMLParserInstantiator.
                getInstance(new File(myPropertiesPath+this.getClass().getSimpleName()+".XML"));
        List<Double> sizeRatio = myParser.getDoubleValuesFromTag("SizeRatio");
        List<Double> mySize = Arrays.asList(windowSize.get(0)*sizeRatio.get(0),windowSize.get(1)*sizeRatio.get(1));
        this.setPrefSize(mySize.get(0),mySize.get(1));
        
        //add all child 
        List<String> myItems = myParser.getValuesFromTag("Items");
        for (String item:myItems) {
            //(GuiItem) Class.forName(item).getConstructor(new Class[]{String.class,int.class}).newInstance(property.description,property.odds)
        }
    }
}
