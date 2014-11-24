package gamePlayer.guiItems.towerUpgrade;

import javafx.geometry.Dimension2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import gameEngine.TowerInfoObject;
import gamePlayer.guiItems.GuiItem;
import gamePlayer.guiItemsListeners.UpgradeListener;
import gamePlayer.mainClasses.guiBuilder.GuiConstants;

public class TowerUpgradePanel extends Pane implements GuiItem {
	
	private HBox myButtonBox;
	private ImageView myIcon;
	private Text myName;
	private String myUpgradeName;
	private Button upgrade1Button;
	private ImageView myTowerImageView;
	
	private UpgradeListener myListener;
	
	public void setCurrentTower(TowerInfoObject current, ImageView towerImageView){
		myIcon.setImage(new Image(current.getImageLocation()));
		myName.setText(current.getName());
		myUpgradeName = current.getMyUpgrade().getName();
		upgrade1Button.setText("Upgrade to" + myUpgradeName);
		upgrade1Button.setOnAction(event -> doUpgrade());
		myTowerImageView = towerImageView;
	}
	
	private void doUpgrade(){
		myListener.upgradeTower(myTowerImageView, myUpgradeName);
	}

	@Override
	public void initialize(Dimension2D containerSize) {
		this.setPrefSize(containerSize.getWidth(), containerSize.getHeight());
		
		myListener = GuiConstants.GUI_MANAGER;
		myIcon = new ImageView();
		myName = new Text();
		upgrade1Button = new Button();
		upgrade1Button.setPrefSize(containerSize.getWidth()/3.0, containerSize.getHeight());
		myButtonBox = new HBox();
		myButtonBox.getChildren().addAll(myName, myIcon, upgrade1Button);
		this.getChildren().add(myButtonBox);
		myListener.registerUpgradePanel(this);
	}

	@Override
	public Node getNode() {
		return this;
	}

}
