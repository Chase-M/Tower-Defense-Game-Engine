package gameAuthoring.scenes.actorBuildingScenes;


import gameAuthoring.scenes.pathBuilding.pathComponents.routeToPointTranslation.BackendRoute;
import gameEngine.actors.BaseEnemy;
import gameEngine.actors.behaviors.IBehavior;
import java.util.List;
import java.util.Map;
import javafx.scene.layout.BorderPane;

/**
 * Scene to build a new enemy.
 * @author Austin Kyker
 *
 */
public class EnemyBuildingScene extends ActorBuildingScene {

    private static final String TITLE = "Enemy";
    private static final String IMG_DIR = "./src/gameAuthoring/Resources/enemyImages/";
    private static final String BEHAVIOR_XML_LOC = "./src/gameAuthoring/Resources/EnemyBehaviors.xml";

    public EnemyBuildingScene (BorderPane root, List<BackendRoute> enemyRoutes) {
        super(root, enemyRoutes, TITLE, BEHAVIOR_XML_LOC, IMG_DIR);
    }

    @Override
    protected void makeNewActor (Map<String, IBehavior> iBehaviorMap) {
        //TODO
        myActors.add(new BaseEnemy(iBehaviorMap,
                                   myActorImage,
                                   myActorNameField.getText(), 5));       
    }
}