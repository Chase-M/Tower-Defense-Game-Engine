package gamePlayer.mainClasses.welcomeScreen;

import gamePlayer.mainClasses.guiBuilder.GuiConstants;
import gamePlayer.mainClasses.managers.GuiManager;
import gamePlayer.mainClasses.welcomeScreen.options.MultiPlayerOptions;
import gamePlayer.mainClasses.welcomeScreen.options.PlayerCountOptions;
import java.io.File;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import utilities.XMLParsing.XMLParser;
import utilities.textGenerator.TextGenerator;

public class WelcomeScreenManager {
    Stage myStage;
    public static final String propertiesPath = "./src/gamePlayer/properties/WelcomeScreenProperties.XML";
    private XMLParser parser;
    private WelcomeScreen welcomeScreen;

    public WelcomeScreenManager(Stage stage) {

        myStage = stage;
        parser = new XMLParser(new File(propertiesPath));
        GuiConstants.TEXT_GEN = new TextGenerator(parser.getValuesFromTag("TextGeneratorPropertiesPath").get(0));
        init(myStage);

    }

    private void init(Stage stage) {
        Group group  = new Group();
        Scene scene = new Scene(group,GuiConstants.WINDOW_WIDTH,GuiConstants.WINDOW_HEIGHT);

        String styleSheetPath = parser.getValuesFromTag("StyleSheet").get(0);
        scene.getStylesheets().add(this.getClass().getResource(styleSheetPath).toExternalForm());

        initializeWelcomeScreen(group);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void initializeWelcomeScreen (Group group) {
        welcomeScreen = new WelcomeScreen();
        welcomeScreen.setBackgroundImage(getImageFromPath(parser.getValuesFromTag("BackgroundImage").get(0),WelcomeScreen.WIDTH,WelcomeScreen.HEIGHT));

        welcomeScreen.setTopContent(getImageFromPath(parser.getValuesFromTag("Logo").get(0),WelcomeScreen.PANE_WIDTH,WelcomeScreen.PANE_HEIGHT/2));
        
        PlayerCountOptions playerCountOptions = new PlayerCountOptions();
        playerCountOptions.getSinglePlayerOption().setOnMouseReleased(event->startSinglePlayerGame());
        playerCountOptions.getMultiPlayerOption().setOnMouseReleased(event->startMultiPlayerOptions());
        welcomeScreen.setCenterContent(playerCountOptions);
        
        group.getChildren().add(welcomeScreen);
    }

    private void joinMultiPlayerGame() {
        //
    }
    
    private void startMultiPlayerGame() {
        //
    }
    
    private void startMultiPlayerOptions() {
        MultiPlayerOptions multiPlayerOptions = new MultiPlayerOptions();
        multiPlayerOptions.getNewGameOption().setOnMouseReleased(event->startMultiPlayerGame());
        multiPlayerOptions.getJoinGameOption().setOnMouseReleased(event->joinMultiPlayerGame());
        welcomeScreen.setCenterContent(multiPlayerOptions);
    }
    
    private void startSinglePlayerGame() {
        new GuiManager(myStage);
        GuiConstants.GUI_MANAGER.init();
    }

    private ImageView getImageFromPath(String imagePath,double width,double height) {
        Image image = new Image(imagePath,width,height,false,true);
        return new ImageView(image);
    }
}
