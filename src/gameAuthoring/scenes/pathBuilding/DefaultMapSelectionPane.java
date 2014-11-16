package gameAuthoring.scenes.pathBuilding;

import gameAuthoring.mainclasses.AuthorController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Observable;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import utilities.ErrorPopup;

public class DefaultMapSelectionPane extends Observable {

	private static final String DEFAULT_MAP_IMAGES_DIR = "./src/gameAuthoring/Resources/DefaultMapImages/";
	public static final Double SCREEN_WIDTH_RATIO = 0.15;
	public static final Double PANE_WIDTH = AuthorController.SCREEN_WIDTH*SCREEN_WIDTH_RATIO;
	private static final Double IMAGE_WIDTH = PANE_WIDTH-40;
	private static final Double IMAGE_HEIGHT = 80.0;
	private static final Double IMAGE_PADDING = 10.0;
	

	private ScrollPane myScrollPane;
	private VBox myImageDisplayVBox;



	public DefaultMapSelectionPane(){
		myScrollPane = new ScrollPane();

		myScrollPane.setPrefWidth(PANE_WIDTH);
		File mapDirectory = new File(DEFAULT_MAP_IMAGES_DIR);

		File[] defaultMapImages = mapDirectory.listFiles();

		myImageDisplayVBox = new VBox(IMAGE_PADDING);
		myImageDisplayVBox.setPadding(new Insets(IMAGE_PADDING));


		for(File f:defaultMapImages){

			ImageView imgView = new ImageView();

			try {

				imgView.setImage(new Image(new FileInputStream(f), IMAGE_WIDTH, IMAGE_HEIGHT, false,true) );
				imgView.setOnMouseClicked(event -> handleImageClick(f));
				myImageDisplayVBox.getChildren().add(imgView);
			} catch (FileNotFoundException e) {
				new ErrorPopup("Image File Not Found");
			}				
		}
		
		myScrollPane.setContent(myImageDisplayVBox);
		
	}
	
	private void handleImageClick(File fileCorrespondingToMapSelected) {
		this.setChanged();
		this.notifyObservers(fileCorrespondingToMapSelected);
	}

	public ScrollPane getDefaultMapsScrollPane(){
		return myScrollPane;
	}
	
}
