package gameAuthoring.scenes.enemyBuilding;

import java.io.File;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import utilities.XMLParsing.XMLParser;

public abstract class BehaviorBuilder {
    
    private static final int COMBO_BOX_WIDTH = 200;
    private static final File ENEMY_FILE = new File("./src/gameAuthoring/Resources/EnemyBehaviors.xml");    
    private static final XMLParser myXMLParser = new XMLParser(ENEMY_FILE);
    private static final int SLIDER_MIN = 1;
    private static final int SLIDER_MAX = 5;
    private static final int SLIDER_START = 5;
    private static final int SLIDER_TICK_UNIT = 1;
    
    protected ComboBox<String> myComboBox;
    protected VBox myContainer;
    
    public abstract IBehaviorKeyValuePair buildBehavior();
    
    public void createCenterDisplay(String title) {
        myContainer = new VBox();
        myContainer.setSpacing(20); 
        myContainer.setPrefWidth(COMBO_BOX_WIDTH);
        Label label = new Label(title);
        myComboBox = createComboBox(title);
        myContainer.getChildren().addAll(label, myComboBox);
    }
    
    protected void setSliderProperties (Slider slider) {
        slider.setMin(SLIDER_MIN);
        slider.setMax(SLIDER_MAX);
        slider.setValue(SLIDER_MIN);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(SLIDER_TICK_UNIT);
        slider.setMinorTickCount(0);
        slider.snapToTicksProperty().set(true);
    }
    
    private ComboBox<String> createComboBox(String comboBoxTitle){  
        ComboBox<String> CB = new ComboBox<String>();
        CB.setPrefWidth(COMBO_BOX_WIDTH);
        CB.getItems().addAll(myXMLParser.getValuesFromTag(comboBoxTitle));
        return CB;
    }

    public VBox getContainer () {
        return myContainer;
    }

    public void reset () {
        myComboBox.setValue(null);
    }
}