package gamePlayer.mainClasses.managers;

import gameEngine.CoOpManager;
import gameEngine.NullTowerInfoObject;
import gameEngine.SingleThreadedEngineManager;
import gameEngine.TowerInfoObject;
import gamePlayer.guiFeatures.FileLoader;
import gamePlayer.guiFeatures.TowerPlacer;
import gamePlayer.guiFeatures.WinStatusProperty;
import gamePlayer.guiItems.gameWorld.GameWorld;
import gamePlayer.guiItems.headsUpDisplay.GameStat;
import gamePlayer.guiItems.headsUpDisplay.HUD;
import gamePlayer.guiItems.messageDisplay.MessageDisplay;
import gamePlayer.guiItems.store.Store;
import gamePlayer.guiItems.store.StoreItem;
import gamePlayer.guiItems.towerUpgrade.TowerIndicator;
import gamePlayer.guiItems.towerUpgrade.TowerUpgradePanel;
import gamePlayer.guiItemsListeners.GameItemListener;
import gamePlayer.guiItemsListeners.GameWorldListener;
import gamePlayer.guiItemsListeners.HUDListener;
import gamePlayer.guiItemsListeners.MessageDisplayListener;
import gamePlayer.guiItemsListeners.PlayButtonListener;
import gamePlayer.guiItemsListeners.SpeedButtonListener;
import gamePlayer.guiItemsListeners.SpeedSliderListener;
import gamePlayer.guiItemsListeners.StoreListener;
import gamePlayer.guiItemsListeners.UpgradeListener;
import gamePlayer.guiItemsListeners.VoogaMenuBarListener;
import gamePlayer.mainClasses.Main;
import gamePlayer.mainClasses.guiBuilder.GuiBuilder;
import gamePlayer.mainClasses.guiBuilder.GuiConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import utilities.JavaFXutilities.imageView.CenteredImageView;

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
		GameWorldListener, UpgradeListener,
		MessageDisplayListener, SpeedSliderListener {

	private static String guiBuilderPropertiesPath = "./src/gamePlayer/properties/GuiBuilderProperties.XML";

	private Stage myStage;
	private SingleThreadedEngineManager myEngineManager;
	private CoOpManager myCoOpManager;

	private Group myRoot;
	private TowerIndicator activeIndicator;
	private ImageView activeTower;
	private boolean interactionAllowed;

	private Store myStore;
	private HUD myHUD;
	private GameWorld myGameWorld;
	private TowerUpgradePanel myUpgradePanel;
	private MessageDisplay myMessageDisplay;
	private Map<String, TowerInfoObject> towerMap;
	private List<GameStat> gameStats;
	private double myScore;
	private boolean isCoOp;
	private String myDirectory;
	
	private DoubleProperty endgame;

	public GuiManager(Stage stage) {
		myStage = stage;
		GuiConstants.GUI_MANAGER = this;

	}

	public void init() {
		GuiConstants.DYNAMIC_SIZING = true;
		myRoot = GuiBuilder.getInstance().build(myStage,
				guiBuilderPropertiesPath);
		
	}

	private void startGame(String directoryPath) {
		myEngineManager = new SingleThreadedEngineManager(myGameWorld.getMap());
		myEngineManager.initializeGame(directoryPath);
		addBackground(directoryPath);
		makeTowerMap();
		testHUD();
		fillStore(myEngineManager.getAllTowerTypeInformation());
		interactionAllowed = true;
	}

	@Override
	public void loadGame() {
		File file = FileLoader.getInstance().loadDirectory(myStage);
		if (file != null) {
			startGame(file.getAbsolutePath());
		}
	}
	
	@Override
	public void loadState(){
		File file = FileLoader.getInstance().load(myStage, "Json", "*.json");
		if (file != null) {
			myEngineManager.loadState(file.getAbsolutePath().replace("\\","/"));
		}
	}

	public static final String NO_UPGRADE = "No update available";
	public static final String NO_GOLD = "Not enough gold available";
	public static final String ESCAPE_TEXT = "Press ESC to escape from tower placement";
	public static final String YOU_WON = "Congratulations! You won!";
	public static final String YOU_LOST = "Sorry, you lost!";
	public static final String SCORE = "Your score: ";

	protected static final Number WIN = null;

	public void startSinglePlayerGame(String directoryPath) {
		myEngineManager = new SingleThreadedEngineManager(myGameWorld.getMap());
		myEngineManager.initializeGame(directoryPath);
		initializeNewGameElements(directoryPath);
		myDirectory = directoryPath;
		interactionAllowed = true;
	}

	@Override
	public void play() {
		if (!interactionAllowed || isCoOp)
			return;
		myEngineManager.resume();
	}

	public void joinMultiPlayerGame() {
		myCoOpManager = new CoOpManager();
		myEngineManager = myCoOpManager;
		if (myCoOpManager.joinGame()) {
			startMultiPlayerGame();
		}
	}

	public void prepareMultiPlayerGame(String directoryPath) {	        
		myCoOpManager = new CoOpManager();
		myEngineManager = myCoOpManager;
		myCoOpManager.startNewGame(directoryPath);
	}

	public boolean multiPlayerGameIsReady() {
		return myCoOpManager.isReady();
	}

	public void startMultiPlayerGame() {
		isCoOp = true;
		GuiConstants.GUI_MANAGER.init();
		String dir = myCoOpManager.initializeGame(myGameWorld.getMap());
		initializeNewGameElements(dir);
		
		GameStat time = new GameStat();
		time.setGameStat("Time");
		time.statValueProperty().bindBidirectional(myCoOpManager.getTimer());
		time.statValueProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> o, Number oldValue, Number newValue) {
				if ((double)newValue <= 0.0){
					interactionAllowed = false;
					myStore.freeze();
				}
				else {
					interactionAllowed = true;
					myStore.unfreeze();
				}
			}
		});
		gameStats.add(time);
		this.setGameStats(gameStats);
	}

	private void initializeNewGameElements(String directoryPath) {
		addBackground(directoryPath);
		makeTowerMap();
		testHUD();
		fillStore(myEngineManager.getAllTowerTypeInformation());
		/*
		endgame = new WinStatusProperty();
		endgame.bindBidirectional(myEngineManager.getWinStatus());
		endgame.addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> o, Number oldValue, Number newValue) {
				checkEndGame((double)newValue);
			}
		});*/
	}

	private void checkEndGame(double d){
		myScore = myEngineManager.getMyHealth()*myEngineManager.getCurrentLevelProperty().getValue()*myEngineManager.getMyGold();
		if (d == WinStatusProperty.WIN){
			displayMessage(YOU_WON + SCORE + myScore, false);
		} else if (d == WinStatusProperty.LOSS){
			displayMessage(YOU_LOST + SCORE + myScore, true);
		}
		
	}
	
	private void addBackground(String directory) {
		File parent = new File(directory += "/background/");
		File background = parent.listFiles()[0];
		myGameWorld.setBackground(background.getAbsolutePath());
	}

	@Override
	public void saveGame() {
		File file = FileLoader.getInstance().save(myStage);
		if (file != null) {
			myEngineManager.saveState(file.getParent().replace("\\","/"), file.getName());
		}
	}

	@Override
	public void registerStatsBoard(HUD hud) {
		myHUD = hud;
	}

	@Override
	public void registerGameWorld(GameWorld world) {
		myGameWorld = world;
	}

	@Override
	public void registerUpgradePanel(TowerUpgradePanel upgradePanel) {
		myUpgradePanel = upgradePanel;
	}

	@Override
	public void setGameStats(List<GameStat> stats) {
		myHUD.setGameStats(stats);
	}

	@Override
	public void pause() {
		if (!interactionAllowed)
			return;
		myEngineManager.pause();
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
		if (!interactionAllowed)
			return;
		// myEngineManager.changeRunSpeed(1.0);
		play();
	}

	@Override
	public void fastForward() {
		if (!interactionAllowed)
			return;
		// myEngineManager.changeRunSpeed(3.0);
		play();
	}

	@Override
	public void registerStore(Store store) {
		myStore = store;
	}

	@Override
	public void fillStore(Collection<TowerInfoObject> towersAvailable) {
		List<StoreItem> storeItems = new ArrayList<StoreItem>();
		for (TowerInfoObject info : towersAvailable) {
			StoreItem newItem = new StoreItem(info.getName(),
					info.getImageLocation(), info.getBuyCost(), new SimpleBooleanProperty(true));
			storeItems.add(newItem);
		}
		myStore.fillStore(storeItems);
	}

	@Override
	public void refreshStore() {
		myStore.refreshStore();
	}

	@Override
	public void upgradeTower(ImageView imageView, String upgradeName) {
		if (!interactionAllowed) return;
		if (upgradeName.equals(NO_UPGRADE)
				&& !myEngineManager.checkGold(towerMap.get(upgradeName))) {
			displayMessage(upgradeName, true);
			return;
		}
		DoubleProperty gold = myEngineManager.getGoldProperty();
		myEngineManager.setMyGold(gold.get()
				- towerMap.get(upgradeName).getBuyCost());
		ImageView newTower = myEngineManager.upgrade(imageView, upgradeName);
		// if (newTower == null) displayMessage(NO_GOLD, true);
		newTower.setOnMouseClicked(event -> selectTower(upgradeName, newTower));
		selectTower(upgradeName, newTower);
	}

	private void testHUD() {
		gameStats = new ArrayList<GameStat>();
		GameStat level = new GameStat();
		level.setGameStat("Level");
		level.statValueProperty().bindBidirectional(myEngineManager.getCurrentLevelProperty());

		GameStat gold = new GameStat();
		gold.setGameStat("Gold");
		gold.statValueProperty().bindBidirectional(myEngineManager.getGoldProperty());
		
		GameStat health = new GameStat();
		health.setGameStat("Health");
		health.statValueProperty().bindBidirectional(myEngineManager.getHealthProperty());

		gameStats = new ArrayList<GameStat>();
		gameStats.add(level);
		gameStats.add(gold);
		gameStats.add(health);
		this.setGameStats(gameStats);

	}

	public void makeTower(String towerName, double x, double y) {
		if (!interactionAllowed) return;
		displayMessage(MessageDisplay.DEFAULT, false);
		if (!myEngineManager.checkGold(towerMap.get(towerName))) {
			displayMessage(NO_GOLD, true);
			return;
		}
		DoubleProperty gold = myEngineManager.getGoldProperty();
		myEngineManager.setMyGold(gold.get()
				- towerMap.get(towerName).getBuyCost());
		ImageView towerImageView = myEngineManager.addTower(towerName, x, y);
		if(towerImageView == null) {
		displayMessage(NO_GOLD, true);
		return;
		}
		towerImageView.setOnMouseClicked(event -> selectTower(towerName,
				towerImageView));
	}

	private void selectTower(String towerName, ImageView tower) {
		if (!interactionAllowed) return;
		CenteredImageView centered = (CenteredImageView) tower;
		double radius = towerMap.get(towerName).getRange();
		deselectTower(activeIndicator, activeTower,
				myEngineManager.getTowerName(activeTower));
		activeIndicator = new TowerIndicator(centered.getXCenter(),
				centered.getYCenter(), radius);
		activeTower = tower;
		myUpgradePanel.setCurrentTower(towerMap.get(towerName), tower,
				activeIndicator);
		myGameWorld.getMap().getChildren().add(activeIndicator);
		tower.setOnMouseClicked(event -> deselectTower(activeIndicator, tower,
				towerName));
		tower.getParent().toFront();
	}

	private void deselectTower(TowerIndicator indicator, ImageView tower,
			String towerName) {
		myGameWorld.getMap().getChildren().remove(indicator);
		//myUpgradePanel.setCurrentTower(new NullTowerInfoObject(), null, null);
		myUpgradePanel.deselectTower();
		if (tower != null)
			tower.setOnMouseClicked(event -> selectTower(towerName, tower));
	}

	@Override
	public void placeTower(String towerName) {
		TowerPlacer.getInstance().placeItem(towerName, myGameWorld.getMap(),
				towerMap.get(towerName).getRange());
		displayMessage(ESCAPE_TEXT, false);
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
	public void sellTower(ImageView myTowerImageView, TowerIndicator indicator) {
		if (!interactionAllowed) return;
		myEngineManager.sellTower(myTowerImageView);
		myGameWorld.getMap().getChildren().remove(indicator);
	}

	/*
	 * For Tower Placing
	 */
	public boolean validPlacement(double x, double y) {
		return myEngineManager.validateTower(x, y);
	}

	@Override
	public void changeSpeed(double d) {
		myEngineManager.changeRunSpeed(d);
	}

	private void makeTowerMap() {
		towerMap = new HashMap<String, TowerInfoObject>();
		for (TowerInfoObject info : myEngineManager
				.getAllTowerTypeInformation()) {
			towerMap.put(info.getName(), info);
			TowerInfoObject next = info.getMyUpgrade();
			while (!(next instanceof NullTowerInfoObject)) {
				towerMap.put(next.getName(), next);
				next = next.getMyUpgrade();
			}
		}
	}

	@Override
	public void escapePlace() {
		myGameWorld.getMap().setOnMouseMoved(null);
		myGameWorld.getMap().setOnMouseReleased(null);
		myGameWorld.getMap().getChildren().remove(myGameWorld.getMap().getChildren().size()-1);  //remove range circle (last thing added to children)
		displayMessage(MessageDisplay.DEFAULT, false);
	}

	public void switchGame() {
		
	}

	public void replayGame() {
		init();
		if (isCoOp) startMultiPlayerGame();
		else startSinglePlayerGame(myDirectory);
	}
}
