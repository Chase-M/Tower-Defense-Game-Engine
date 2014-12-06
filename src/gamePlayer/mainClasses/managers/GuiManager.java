package gamePlayer.mainClasses.managers;

import gameAuthoring.mainclasses.AuthorController;
import gameAuthoring.scenes.pathBuilding.buildingPanes.BuildingPane;
import gameEngine.NullTowerInfoObject;
import gameEngine.SingleThreadedEngineManager;
import gameEngine.TowerInfoObject;
import gamePlayer.Listeners.GameItemListener;
import gamePlayer.Listeners.GameWorldListener;
import gamePlayer.Listeners.HUDListener;
import gamePlayer.Listeners.MessageDisplayListener;
import gamePlayer.Listeners.PlayButtonListener;
import gamePlayer.Listeners.SpeedButtonListener;
import gamePlayer.Listeners.StoreListener;
import gamePlayer.Listeners.UpgradeListener;
import gamePlayer.Listeners.VoogaMenuBarListener;
import gamePlayer.guiFeatures.FileLoader;
import gamePlayer.guiFeatures.TowerPlacer;
import gamePlayer.guiItems.gameWorld.GameWorld;
import gamePlayer.guiItems.headsUpDisplay.GameStats;
import gamePlayer.guiItems.headsUpDisplay.HUD;
import gamePlayer.guiItems.messageDisplay.MessageDisplay;
import gamePlayer.guiItems.store.Store;
import gamePlayer.guiItems.store.StoreItem;
import gamePlayer.guiItems.towerUpgrade.TowerUpgradePanel;
import gamePlayer.mainClasses.guiBuilder.GuiBuilder;
import gamePlayer.mainClasses.guiBuilder.GuiConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * Class controls all GUI items and MUST implement ALL of the interfaces in the
 * guiItemsListeners package The game engine accesses GUI resources through this
 * class
 * 
 * @author allankiplagat, Greg Lyons
 *
 */
public class GuiManager implements VoogaMenuBarListener, HUDListener,
		PlayButtonListener, SpeedButtonListener, StoreListener,
		GameWorldListener, GameItemListener, UpgradeListener, MessageDisplayListener {

	private static String guiBuilderPropertiesPath = "./src/gamePlayer/properties/GuiBuilderProperties.XML";

	private Stage myStage;
	private SingleThreadedEngineManager myEngineManager;
	private Group myRoot;
	private boolean gameRunning;

	private Store myStore;
	private HUD myHUD;
	private GameWorld myGameWorld;
	private TowerUpgradePanel myUpgradePanel;
	private MessageDisplay myMessageDisplay;
	private Map<String, TowerInfoObject> towerMap;

	public GuiManager(Stage stage) {
		myStage = stage;
		GuiConstants.GUI_MANAGER = this;	
		gameRunning = false;
	}
	
	public void init() {
		myRoot = GuiBuilder.getInstance().build(myStage, guiBuilderPropertiesPath);
	}
	
	private void startGame(String directoryPath){
	    myEngineManager = new SingleThreadedEngineManager(myGameWorld.getMap());
		myEngineManager.initializeGame(directoryPath);
		makeMap();
		testHUD();
		//myRoot.getChildren().add(engineGroup);
		fillStore(myEngineManager.getAllTowerTypeInformation());
		myGameWorld.getMap().getStyleClass().add("GameWorld");
		System.out.println(BuildingPane.DRAW_SCREEN_WIDTH + " " + AuthorController.SCREEN_HEIGHT);
		gameRunning = true;
	}
	
	private void makeMap(){
	    towerMap = new HashMap<String, TowerInfoObject>();
		for (TowerInfoObject info: myEngineManager.getAllTowerTypeInformation()){
			towerMap.put(info.getName(), info);
			TowerInfoObject next = info.getMyUpgrade();
			while(!(next instanceof NullTowerInfoObject)){
				towerMap.put(next.getName(), next);
				next = next.getMyUpgrade();
			}
		}
	}

	@Override
	public void loadGame() {
		File file = FileLoader.getInstance().load(myStage);
		if (file != null) {
			startGame(file.getAbsolutePath());
		}
	}

	@Override
	public void saveGame() {
		//myEngineManager.saveState("sampleFileName"+Math.random()*1000);l
	}
	
	public boolean gameRunning() {
		return gameRunning;
	}

	@Override
	public void registerStatsBoard(HUD hud) {
		myHUD = hud;
	}
	
	@Override
	public void registerGameWorld(GameWorld world){
		myGameWorld = world;
	}
	
	@Override
	public void registerUpgradePanel(TowerUpgradePanel upgradePanel){
		myUpgradePanel = upgradePanel;
	}

	@Override
	public void setGameStats(List<GameStats> stats) {
		myHUD.setGameStats(stats);
	}

	@Override
	public void pause() {
		if (!gameRunning) return;
		myEngineManager.pause();
	}

	@Override
	public void play() {
		if (!gameRunning) return;
		myEngineManager.resume();
	}

	@Override
	public void changeTheme() {
		File file = FileLoader.getInstance().load(myStage, "StyleSheets",
				"*.css");
		if (file != null) {
			myStage.getScene().getStylesheets().clear();
			myStage.getScene().getStylesheets()
					.add("file:" + file.getAbsolutePath());
		}
	}

	@Override
	public void normalSpeed() {
		if (!gameRunning) return;
		//myEngineManager.changeRunSpeed(1.0);
		play();
	}

	@Override
	public void fastForward() {
		if (!gameRunning) return;
		//myEngineManager.changeRunSpeed(3.0);
		play();
	}

	@Override
	public void registerStore(Store store) {
		myStore = store;
	}

	@Override
	public void fillStore(Collection<TowerInfoObject> towersAvailable) {
		List<StoreItem> storeItems = new ArrayList<StoreItem>();
		for (TowerInfoObject info: towersAvailable) {
			StoreItem newItem = new StoreItem(info.getName(), info.getImageLocation(), new SimpleBooleanProperty(true));
			storeItems.add(newItem);
		}
		/*
		String blackPath = "gamePlayer/mainClasses/testGameManager/storeItemImages/blackTurret.png";
        String brownPath = "gamePlayer/mainClasses/testGameManager/storeItemImages/brownTurret.png";  
        BooleanProperty blackTurretAvail = new SimpleBooleanProperty(true);
        BooleanProperty brownTurretAvail = new SimpleBooleanProperty(true);
        storeItems.add(new StoreItem("blackTurret",blackPath,blackTurretAvail));
        storeItems.add(new StoreItem("brownTurret",brownPath,brownTurretAvail));*/
		
		myStore.fillStore(storeItems);
	}

	@Override
	public void refreshStore() {
		myStore.refreshStore();
	}

	@Override
	public void upgradeTower(ImageView imageView, String upgradeName) {
		if (!gameRunning) return;
		double x = imageView.getX();
		double y = imageView.getY();
		myEngineManager.removeTower(imageView);
		//myEngineManager.addTower(upgradeName, x, y);
	}
	
	private void testHUD() {
		List<GameStats> gameStats;
        GameStats level = new GameStats();
        level.setGameStat("Level");
        level.setStatValue(1);
        
        GameStats score = new GameStats();
        score.setGameStat("Score");
        score.setStatValue(0);
        
        GameStats health = new GameStats();
        health.setGameStat("Health");
        health.setStatValue(100);
        
        gameStats = new ArrayList<GameStats>();
        gameStats.add(level); gameStats.add(score); gameStats.add(health);
        this.setGameStats(gameStats);
        
        //update game stats
        gameStats.get(1).setStatValue(50);
        gameStats.get(2).setStatValue(50);
    }

	public void makeTower(String towerName, double x, double y) {
		if (!gameRunning) return;
		ImageView towerImageView = myEngineManager.addTower(towerName, x, y);
		//String towerName = myEngineManager.getTowerName(towerImageView);
		towerImageView.setOnMouseClicked(event -> myUpgradePanel.setCurrentTower(towerMap.get(towerName), towerImageView));
	}
	
	@Override
	public void placeTower(String towerName) {
		TowerPlacer.getInstance().placeItem(towerName, myGameWorld.getMap());
	}

	@Override
	public void registerMessageDisplayListener(MessageDisplay display) {
		myMessageDisplay = display;
	}

	@Override
	public void displayMessage(String message, boolean error) {
		myMessageDisplay.showMessage(message, error);
	}

	@Override
	public void clearMessageDisplay() {
		myMessageDisplay.clear();		
	}

	@Override
	public void selectItem(int itemID) {
		
	}

	@Override
	public void sellTower(ImageView myTowerImageView) {
		myEngineManager.removeTower(myTowerImageView);
		//TODO: Cost stuff
	}
}
