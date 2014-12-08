package utilities.JavaFXutilities.DragAndDropFilePanes.imagePanes;

import gameAuthoring.mainclasses.Constants;
import java.io.File;
import utilities.JavaFXutilities.imageView.StringToImageViewConverter;
import utilities.errorPopup.ErrorPopup;
import javafx.scene.image.ImageView;

public abstract class DragAndDropImagePane extends DragAndDropFilePane {

    protected ImageView myImageView;
    protected File myFile;

    public DragAndDropImagePane (double width,
            double height) {
        super(width, height, new String[] {".jpeg", ".jpg", ".png"});
    }

    public ImageView getImageView () {
        return myImageView;
    }

    public String getImagePath () {
        return myFile.getPath();
    }

    public void setHeight (double height) {
        myImageView.setPreserveRatio(true);
        myImageView.setFitHeight(height);
        myImageView.autosize();
        myContainer.setPrefHeight(height);       
    }

    public void displayImage () {
        myContainer.getChildren().remove(myDragAndDropPane);
        myImageView = StringToImageViewConverter.getImageView(myDragAndDropPane.getWidth(), 
                myDragAndDropPane.getHeight(), 
                myFile.getPath());
        myContainer.getChildren().add(myImageView); 
        this.setChanged();
        this.notifyObservers(myFile.getPath());
    }
    
    public boolean hasFileBeenDropped() {
        new ErrorPopup(Constants.NO_PROJECTILE_IMG);
        return myFile == null;
    }
}
