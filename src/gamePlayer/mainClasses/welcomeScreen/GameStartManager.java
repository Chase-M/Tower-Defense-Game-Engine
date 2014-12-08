package gamePlayer.mainClasses.welcomeScreen;

import gamePlayer.mainClasses.guiBuilder.GuiConstants;
import gamePlayer.mainClasses.guiBuilder.GuiText;
import gamePlayer.mainClasses.managers.GuiManager;
import gamePlayer.mainClasses.welcomeScreen.availableGames.GameChooser;
import gamePlayer.mainClasses.welcomeScreen.startingOptions.MultiPlayerOptions;
import gamePlayer.mainClasses.welcomeScreen.startingOptions.PlayerCountOptions;

import java.io.File;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import utilities.XMLParsing.XMLParser;
import utilities.textGenerator.TextGenerator;

public class GameStartManager {
    private Stage myStage;
    public static final String propertiesPath = "./src/gamePlayer/properties/WelcomeScreenProperties.XML";
    private XMLParser parser;
    private WelcomeScreen welcomeScreen;
    private String gameTypeBeingChosen;

    public GameStartManager(Stage stage) {
        myStage = stage;
        parser = new XMLParser(new File(propertiesPath));
        GuiConstants.TEXT_GEN = new TextGenerator(parser.getValuesFromTag("TextGeneratorPropertiesPath").get(0));
        GuiConstants.GAME_START_MANAGER = this;
        init();
    }

    	
    public void init() {
    	GuiConstants.DYNAMIC_SIZING = true;
        Group group  = new Group();
        Scene scene = new Scene(group,GuiConstants.WINDOW_WIDTH,GuiConstants.WINDOW_HEIGHT);

        String styleSheetPath = parser.getValuesFromTag("StyleSheet").get(0);
        scene.getStylesheets().add(this.getClass().getResource(styleSheetPath).toExternalForm());

        initializeWelcomeScreen(group);

        myStage.setScene(scene);
        myStage.setResizable(false);
        myStage.show();
    }

    private void initializeWelcomeScreen (Group group) {
        welcomeScreen = new WelcomeScreen();
        welcomeScreen.setBackgroundImage(getImageFromPath(parser.getValuesFromTag("BackgroundImage").get(0),WelcomeScreen.WIDTH,WelcomeScreen.HEIGHT));

        welcomeScreen.setTopContent(getImageFromPath(parser.getValuesFromTag("Logo").get(0),WelcomeScreen.PANE_WIDTH,WelcomeScreen.PANE_HEIGHT/2));

        PlayerCountOptions playerCountOptions = new PlayerCountOptions();
        playerCountOptions.getSinglePlayerOption().setOnMouseReleased(event->startSinglePlayerGameChooser());
        playerCountOptions.getMultiPlayerOption().setOnMouseReleased(event->startMultiPlayerOptions());
        welcomeScreen.setCenterContent(playerCountOptions); 
        
//        leapConnector = new LMConnector();
//        leapConnector.initialize(new Dimension2D(WelcomeScreen.PANE_WIDTH, WelcomeScreen.PANE_HEIGHT));
//        welcomeScreen.setBottomContent(leapConnector.getNode());

        group.getChildren().add(welcomeScreen);
    }

    public void joinMultiPlayerGame() {
        GuiManager manager = new GuiManager(myStage);
        manager.joinMultiPlayerGame();
    }

    private void startSinglePlayerGameChooser() {
        gameTypeBeingChosen = GuiConstants.SINGLE_PLAYER_GAME;
        GameChooser chooser = new GameChooser(GuiConstants.SINGLE_PLAYER_GAMES_DIRECTORY);
        welcomeScreen.setCenterContent(chooser);
    }

    private void startMultiPlayerGameChooser() {
        gameTypeBeingChosen = GuiConstants.MULTI_PLAYER_GAME;
        GameChooser chooser = new GameChooser(GuiConstants.MULTI_PLAYER_GAMES_DIRECTORY);
        welcomeScreen.setCenterContent(chooser);
    }

    private void startMultiPlayerOptions() {
        MultiPlayerOptions multiPlayerOptions = new MultiPlayerOptions();
        multiPlayerOptions.getNewGameOption().setOnMouseReleased(event->startMultiPlayerGameChooser());
        multiPlayerOptions.getJoinGameOption().setOnMouseReleased(event->joinMultiPlayerGame());
        welcomeScreen.setCenterContent(multiPlayerOptions);
    }

    public void startSinglePlayerGame(String directoryPath) {
        GuiManager manager = new GuiManager(myStage);
        GuiConstants.GUI_MANAGER.init();
        manager.startSinglePlayerGame(directoryPath);
    }

    private void startMultiPlayerGame(String directoryPath) {
        //wait for other player to join
        welcomeScreen.setCenterContent(new LoadingIndicator(GuiConstants.TEXT_GEN.get(GuiText.WAITING_FOR_CHALLENGER)));

        GuiManager manager = new GuiManager(myStage);
        manager.prepareMultiPlayerGame(directoryPath);

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> pollEngineForMultiPlayerReadiness(manager,timeline)));
        timeline.play();
    }

    private void pollEngineForMultiPlayerReadiness(GuiManager manager,Timeline timeline) {
        if (manager.multiPlayerGameIsReady()) {
            timeline.stop();
            manager.startMultiPlayerGame();
        }
    }

    private ImageView getImageFromPath(String imagePath,double width,double height) {
        Image image = new Image(imagePath,width,height,false,true);
        return new ImageView(image);
    }

    public void startGame (File file) {
        
        if (gameTypeBeingChosen.equals(GuiConstants.SINGLE_PLAYER_GAME)) {
            startSinglePlayerGame(file.getPath());
        } else if (gameTypeBeingChosen.equals(GuiConstants.MULTI_PLAYER_GAME)) {
            startMultiPlayerGame(file.getPath());
        }
    }
}
