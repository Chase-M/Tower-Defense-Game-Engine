package gameAuthoring.scenes.actorBuildingScenes;

import gameAuthoring.mainclasses.AuthorController;
import gameAuthoring.mainclasses.Constants;
import gameAuthoring.mainclasses.controllerInterfaces.IEnemyConfiguring;
import gameAuthoring.scenes.actorBuildingScenes.actorListView.CreatedEnemiesDisplay;
import gameEngine.actors.BaseEnemy;
import gameEngine.actors.behaviors.IBehavior;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import utilities.JavaFXutilities.numericalTextFields.LabeledNumericalTextField;
import utilities.errorPopup.ErrorPopup;


/**
 * Scene to build a new enemy.
 * 
 * @author Austin Kyker
 *
 */
public class EnemyBuildingScene extends ActorBuildingScene {

    private static final String TITLE = "Enemy";
    private static final String IMG_DIR = AuthorController.gameDir + "/enemyImages/";
    private static final String BEHAVIOR_XML_LOC =
            "./src/gameAuthoring/Resources/actorBehaviors/EnemyBehaviors.xml";
    private static final double FIELD_WIDTH = 100;

    private List<BaseEnemy> myEnemies;
    private IEnemyConfiguring myEnemyConfiguringController;
    private LabeledNumericalTextField myDamageField;

    public EnemyBuildingScene (BorderPane root, IEnemyConfiguring controller) {
        super(root, TITLE, BEHAVIOR_XML_LOC, IMG_DIR);
        myEnemyConfiguringController = controller;

    }

    @Override
    protected void makeNewActor (Map<String, IBehavior> iBehaviorMap) {
        // TODO
        myEnemies.add(new BaseEnemy(iBehaviorMap,
                                    myActorImgPath,
                                    myActorNameField.getText(),
                                    myRangeSliderContainer.getSliderValue(),
                                    myDamageField.getNumberEntered(),
                                    myEnemyConfiguringController.fetchEnemyRoutes()));
    }

    @Override
    protected void initializeActorsAndBuildActorDisplay () {
        myEnemies = FXCollections.observableArrayList();
        CreatedEnemiesDisplay enemyDisplay = new CreatedEnemiesDisplay(myEnemies);
        myPane.setLeft(enemyDisplay);
    }

    @Override
    protected void finishBuildingActors () {
        myEnemyConfiguringController.configureEnemies(myEnemies);
    }

    @Override
    protected void configureAndDisplayRightPane () {
    }

    @Override
    protected HBox addRequiredNumericalTextFields () {
        HBox fieldsContainer = new HBox(Constants.SM_PADDING);
        myDamageField = new LabeledNumericalTextField(Constants.DAMAGE, FIELD_WIDTH);
        fieldsContainer.getChildren().add(myDamageField);
        return fieldsContainer;
    }

    @Override
    public boolean actorSpecificFieldsValid () {
        return myDamageField.isValueEntered() && actorNamesAreUnique();
    }

    private boolean actorNamesAreUnique () {
        if (myEnemies.stream()
                .filter(enemy -> myActorNameField.getText().equalsIgnoreCase(enemy.toString()))
                .count() == 0) {
           return true;
        }
        new ErrorPopup(Constants.UNIQUE_NAMES_ERROR);
        return false;
    }

    @Override
    protected void clearActorSpecificFields () {
        myDamageField.clearField();
    }
}
