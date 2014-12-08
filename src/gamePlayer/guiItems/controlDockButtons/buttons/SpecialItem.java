package gamePlayer.guiItems.controlDockButtons.buttons;

import gamePlayer.guiItems.controlDockButtons.ControlDockButton;
import gamePlayer.mainClasses.ExceptionHandling.ExceptionHandler;
import gamePlayer.mainClasses.guiBuilder.GuiConstants;

import java.io.File;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.geometry.Dimension2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.util.Duration;
import utilities.XMLParsing.XMLParser;

public class SpecialItem extends ControlDockButton {
	
	String availableImage, unavailableImage;

	@Override
	public void initialize(Dimension2D containerSize) {
		super.initialize(containerSize);
        String propertiesPath = GuiConstants.GUI_ELEMENT_PROPERTIES_PATH + myPropertiesPath+this.getClass().getSimpleName()+".XML";
        myParser = new XMLParser(new File(propertiesPath)); 
		
        availableImage = myParser.getValuesFromTag("Images").get(0);
        unavailableImage = myParser.getValuesFromTag("Images").get(1);
        
        setUpSizing (containerSize);
        setupImageView();
        myButton.setOnAction(event -> buttonClicked());
	}

	@Override
	public Node getNode() {
		return myButton;
	}
	
	public void startAnimation(double length) {
		//TODO: add circle that will fade out with time
		Timeline timeline = new Timeline();
		KeyFrame kf = new KeyFrame(Duration.seconds(length/60));
		timeline.getKeyFrames().add(kf);
		timeline.setCycleCount(60);
		timeline.setAutoReverse(false);
		timeline.getKeyFrames().add(kf);
		timeline.setOnFinished(event -> reset());
		timeline.play();
	}
	
	private void reset() {
		myButton.setDisable(false);
		myButton.selectedProperty().set(false);
	}

	private void buttonClicked() {
		double time = GuiConstants.GUI_MANAGER.specialSelected();
		myButton.setDisable(true);
		startAnimation(time);
	}
	
	private void setupImageView() {
		Image image1 = null;
		Image image2 = null;
		try {
			image1 = new Image(availableImage, imageSize.getWidth(), imageSize.getHeight(), false, false);
			image2 = new Image(unavailableImage, imageSize.getWidth(), imageSize.getHeight(), false, false);
		} catch (NullPointerException npe) {
			ExceptionHandler.getInstance().handle(npe);
		}
		myImageView.setImage(image1);
		myImageView.imageProperty().bind(
				Bindings.when(myButton.selectedProperty())
						.then(image2)
						.otherwise(image1));
		myButton.setGraphic(myImageView);
		
	}

}
