package gameAuthoring.scenes.pathBuilding;

import gameAuthoring.mainclasses.AuthorController;
import gameAuthoring.mainclasses.controllerInterfaces.PathConfiguring;
import gameAuthoring.scenes.BuildingScene;
import gameAuthoring.scenes.pathBuilding.buildingPanes.BackgroundBuilding;
import gameAuthoring.scenes.pathBuilding.buildingPanes.BuildingPane;
import gameAuthoring.scenes.pathBuilding.buildingPanes.CurveDrawingPane;
import gameAuthoring.scenes.pathBuilding.buildingPanes.DrawingComponentOptionPane;
import gameAuthoring.scenes.pathBuilding.buildingPanes.LineDrawingPane;
import gameAuthoring.scenes.pathBuilding.buildingPanes.PathBackgroundSelectionPane;
import gameAuthoring.scenes.pathBuilding.buildingPanes.SelectComponentPane;
import gameAuthoring.scenes.pathBuilding.buildingPanes.locationPane.EnemyEndingLocationsPane;
import gameAuthoring.scenes.pathBuilding.buildingPanes.locationPane.EnemyStartingLocationsPane;
import gameAuthoring.scenes.pathBuilding.pathComponents.Path;
import gameAuthoring.scenes.pathBuilding.pathComponents.PathComponent;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import utilities.StringToImageViewConverter;

/**
 * This class allows the user to build a path from a starting location to an 
 * ending location. The scene manages the different BuildingPanes that allow
 * the user different on-click functionality.
 * @author Austin Kyker
 *
 */
public class PathBuildingScene extends BuildingScene implements BackgroundBuilding {

    private static final String SELECTED_CSS_CLASS = "selected";
    public static final int BUILDING_OPTIONS_PADDING = 10;
    public static final double SIDE_PANE_WIDTH = ((AuthorController.SCREEN_WIDTH-BuildingPane.DRAW_SCREEN_WIDTH)/2);
    private static final String TITLE = "Path";

    private Path  myPath;
    private BorderPane myPane;
    private static Group myGroup;

    private PathBackgroundSelectionPane myBackgroundSelectionPane;
    private EnemyStartingLocationsPane myEnemyStartingLocationsPane;
    private EnemyEndingLocationsPane myEnemyEndingLocationsPane;
    private LineDrawingPane myLineDrawingPane;
    private CurveDrawingPane myCurveDrawingPane;
    private SelectComponentPane mySelectionComponentPane;
    private BuildingPane myCurrentBuildingPane;

    private VBox myLinePathOptionPane;
    private VBox myCurvePathOptionPane;   
    private VBox mySelectComponentOptionPane;
    private VBox myFinishedPathBuildingOptionPane;
    private VBox myResetBuildOptionPane;
    private DefaultMapSelectionPane myDefaultMapSelectionPane;
    private PathConfiguring myPathConfiguringController;


    public PathBuildingScene (BorderPane root, PathConfiguring controller) {
        super(root, TITLE);
        myPathConfiguringController = controller;
        myPane = root;
        myGroup = new Group();
        myPath = new Path(myGroup);
        createAndDisplayDefaultMapSelectionPane();
        createBuildingPanes();
        createPathBuildingOptions();
        this.getScene().setOnKeyReleased(event->handleKeyPress(event));
        setCurrentBuildingPane(myBackgroundSelectionPane); 
    }

    private void createAndDisplayDefaultMapSelectionPane() {
        myDefaultMapSelectionPane = new DefaultMapSelectionPane((BackgroundBuilding) this);
        myPane.setLeft(myDefaultMapSelectionPane.getDefaultMapsScrollPane());
    }

    private void createBuildingPanes () {
        myBackgroundSelectionPane = new PathBackgroundSelectionPane(myGroup, this);
        myEnemyStartingLocationsPane = new EnemyStartingLocationsPane(myGroup, myPath, this);
        myEnemyEndingLocationsPane = new EnemyEndingLocationsPane(myGroup, myPath, this);
        myLineDrawingPane = new LineDrawingPane(myGroup, myPath);
        myCurveDrawingPane = new CurveDrawingPane(myGroup, myPath);
        mySelectionComponentPane = new SelectComponentPane(myGroup, myPath);
    }

    private void handleKeyPress (KeyEvent event) {
        if(event.getCode() == KeyCode.DELETE){
            List<PathComponent> deletedComponent = myPath.deleteSelectedComponent();
            if(deletedComponent != null)
                myCurrentBuildingPane.removeConnectedComponentFromScreen(deletedComponent);
        }
    }

    /**
     * Shows all of the path building options. The options are line drawing, curve drawing,
     * selection, and finished which will check to see if the path is connected, and if it is,
     * will proceed to the next scene (enemy building)
     */
    private void createPathBuildingOptions () {
        VBox pathBuildingOptions = new VBox(BUILDING_OPTIONS_PADDING);
        pathBuildingOptions.setPadding(new Insets(BUILDING_OPTIONS_PADDING));
        pathBuildingOptions.setPrefWidth(SIDE_PANE_WIDTH);
        myPane.setRight(pathBuildingOptions);

        myResetBuildOptionPane = new DrawingComponentOptionPane("Reset");
        myResetBuildOptionPane.setOnMouseReleased(event->resetBuild());

        myLinePathOptionPane = new DrawingComponentOptionPane("Line");
        myLinePathOptionPane.setOnMouseReleased(event->setCurrentDrawingPane(myLineDrawingPane));

        myCurvePathOptionPane = new DrawingComponentOptionPane("Curve");
        myCurvePathOptionPane.setOnMouseReleased(event->setCurrentDrawingPane(myCurveDrawingPane));

        mySelectComponentOptionPane = new DrawingComponentOptionPane("Selection");
        mySelectComponentOptionPane.setOnMouseReleased(event->setSelectionMode());

        myFinishedPathBuildingOptionPane = new DrawingComponentOptionPane("Finished");
        myFinishedPathBuildingOptionPane.setOnMouseReleased(event->handleFinishButtonClick());

        pathBuildingOptions.getChildren().addAll(myResetBuildOptionPane, myLinePathOptionPane, myCurvePathOptionPane,
                                                 mySelectComponentOptionPane, myFinishedPathBuildingOptionPane);
    }

    private void resetBuild () {
        myPath.resetPath();
        setCurrentBuildingPane(myBackgroundSelectionPane);        
        deselectOptionsComponents();
    }

    private void handleFinishButtonClick () {
        if(myPath.isCompletedAndRoutesVerified()) {
            myPathConfiguringController.configurePath(myPath);
        }
    }

    private void setSelectionMode () {
        if(!isCurrentPane(mySelectionComponentPane) && canDrawPathComponents()) {
            mySelectionComponentPane.addListenersToComponents();
            setCurrentDrawingPane(mySelectionComponentPane);
        }
    }

    private void setCurrentDrawingPane (BuildingPane pane) {
        if(!isCurrentPane(pane) && canDrawPathComponents()) {
            deselectOptionsComponents();
            setCurrentBuildingPane(pane);
        }
    }

    public void proceedToLineDrawerModeIfLocationsVerified () {
        if(myPath.endingLocationsConfiguredCorrectly()){
            myLinePathOptionPane.getStyleClass().add(SELECTED_CSS_CLASS);
            setCurrentBuildingPane(myLineDrawingPane);
        }
    }

    private void deselectOptionsComponents() {
        myResetBuildOptionPane.getStyleClass().remove(SELECTED_CSS_CLASS);
        myLinePathOptionPane.getStyleClass().remove(SELECTED_CSS_CLASS);
        myCurvePathOptionPane.getStyleClass().remove(SELECTED_CSS_CLASS);
        mySelectComponentOptionPane.getStyleClass().remove(SELECTED_CSS_CLASS);
    }

    private boolean canDrawPathComponents() {
        return !(isCurrentPane(myEnemyStartingLocationsPane) || 
                isCurrentPane(myEnemyEndingLocationsPane) ||
                isCurrentPane(myBackgroundSelectionPane));
    }

    private boolean isCurrentPane(BuildingPane pane){
        return myCurrentBuildingPane.equals(pane);
    }

    public void proceedToStartLocationSelection () {
        setCurrentBuildingPane(myEnemyStartingLocationsPane);        
    }

    public void proceedToEndLocationsSelection () {
        if(myPath.startingLocationsConfiguredCorrectly()){
            setCurrentBuildingPane(myEnemyEndingLocationsPane);
        }
    }

    public void setCurrentBuildingPane(BuildingPane nextPane) {
        myCurrentBuildingPane = nextPane;
        myPane.getChildren().remove(myPane.getCenter());
        myPane.setCenter(nextPane);
        nextPane.refreshScreen();
    }

    @Override
    public void setBackground (String imageFileName) {
        ImageView imageView = 
                StringToImageViewConverter.getImageView(BuildingPane.DRAW_SCREEN_WIDTH, 
                                                        AuthorController.SCREEN_HEIGHT, 
                                                        imageFileName);
        myGroup.getChildren().add(imageView); 
        myPathConfiguringController.setBackground(imageFileName);     
        proceedToStartLocationSelection();      
    }
}