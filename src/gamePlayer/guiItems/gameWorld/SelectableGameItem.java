package gamePlayer.guiItems.gameWorld;

import java.io.File;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import utilities.XMLParsing.XMLParser;

/**
 * @author brianbolze
 */
public class SelectableGameItem extends GameItem {
	
	private XMLParser myParser;
	private Circle mySelectCircle;
	private BooleanProperty selectedProperty;


	public SelectableGameItem(int ID, Point2D loc, ImageView imageView) {
		super(ID, loc, imageView);
	}
	
	protected void init() {
		
		super.init();
		
		myParser = new XMLParser(new File("./src/gamePlayer/properties/guiItems/"+this.getClass().getSimpleName()+".XML")); 
		double radius = myParser.getDoubleValuesFromTag("CircleRadius").get(0);
		double opacity = myParser.getDoubleValuesFromTag("CircleOpacity").get(0);
		String color = myParser.getValuesFromTag("CircleColor").get(0);
		
		selectedProperty = new SimpleBooleanProperty(false);
		myGroup.setOnMouseReleased(event -> select());

		mySelectCircle = new Circle(radius);
		mySelectCircle.setFill(Color.web(color));
		mySelectCircle.setOpacity(opacity);
		mySelectCircle.visibleProperty().bind(selectedProperty);

		myGroup.getChildren().add(0, mySelectCircle);
	}
	
	private void select() {
		selectedProperty.set(!selectedProperty.get());
	}

}
