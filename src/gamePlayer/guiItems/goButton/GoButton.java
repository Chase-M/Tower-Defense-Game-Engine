package gamePlayer.guiItems.goButton;

import java.io.File;
import java.util.List;

import utilities.XMLParsing.XMLParser;
import javafx.geometry.Dimension2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import gamePlayer.guiItems.GuiItem;

public class GoButton implements GuiItem {

	private XMLParser myParser;
	private Button myButton;
	private ImageView myImageView;
	private Dimension2D buttonSize;
	private Dimension2D imageSize;
	
	private String playImage;
	private String ffImage;
			
	
	public GoButton() {

	}

	@Override
	public void initialize(Dimension2D containerSize) {
		myParser = new XMLParser(new File(myPropertiesPath+this.getClass().getSimpleName()+".XML"));
		myImageView = new ImageView();
		myButton = new Button();
		
		List<String> images = myParser.getValuesFromTag("Images");
		playImage = images.get(0);
		ffImage = images.get(1);
		
		setUpSizing(containerSize);
		
		myButton.setOnAction(event -> play());
		setImage(playImage);
		myButton.setGraphic(myImageView);
	}

	@Override
	public Node getNode() {
		return myButton;
	}
	
	private void setImage(String path){
		try{
			Image newImage = new Image(path);
			myImageView.setImage(newImage);
		}
		catch(NullPointerException npe){
		}
	}
	
	private void play(){
		setImage(ffImage);
		myButton.setOnAction(event -> fastForward());
	}
	
	private void fastForward(){
		setImage(playImage);
		myButton.setOnAction(event -> play());
	}
	
	public void pause(){
		setImage(playImage);
		myButton.setOnAction(event -> play());
	}
	
	private void setUpSizing(Dimension2D containerSize){
		Dimension2D buttonRatio = myParser.getDimension("SizeRatio");
		Dimension2D imageRatio = myParser.getDimension("ImageRatio");

		buttonSize = new Dimension2D(buttonRatio.getWidth()*containerSize.getWidth(), 
								buttonRatio.getHeight()*containerSize.getHeight());
		imageSize = new Dimension2D(imageRatio.getWidth()*buttonSize.getWidth(),
									imageRatio.getHeight()*buttonSize.getHeight());

		myButton.setPrefSize(buttonSize.getWidth(), buttonSize.getHeight());
		myImageView.setFitHeight(imageSize.getHeight());
		myImageView.setFitWidth(imageSize.getWidth());
	}
}
