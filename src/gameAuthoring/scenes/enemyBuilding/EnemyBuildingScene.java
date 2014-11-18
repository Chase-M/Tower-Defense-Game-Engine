package gameAuthoring.scenes.enemyBuilding;


import gameAuthoring.mainclasses.AuthorController;
import gameAuthoring.scenes.BuildingScene;
import gameAuthoring.scenes.pathBuilding.pathComponents.routeToPointTranslation.BackendRoute;
import gameEngine.actors.BaseActor;
import gameEngine.actors.behaviors.IBehavior;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import utilities.DragAndDropFilePane;

public class EnemyBuildingScene extends BuildingScene implements Observer {

    private static final String ENEMY_IMAGES_DIR = "./src/gameAuthoring/Resources/enemyImages/";
    public static final int ENEMY_IMG_HEIGHT = 150;
    public static final int ENEMY_IMG_WIDTH = 150;

    private static final String TITLE = "Enemy Building";

    private BorderPane myPane;
    private Image myImageForEnemyBeingCreated;
    private ObservableList<BaseActor> myEnemies;
    private List<BehaviorBuilder> myBehaviorBuilders;
    private EnemiesScrollPane myEnemiesScrollPane;
    private DragAndDropFilePane myDragAndDrop;
    private TextField myEnemyNameField;

    public EnemyBuildingScene (BorderPane root, List<BackendRoute> enemyRoutes) {
        super(root, TITLE);
        myPane = root;
        myEnemies = FXCollections.observableArrayList();
        myEnemiesScrollPane = new EnemiesScrollPane(myEnemies);
        myPane.setLeft(myEnemiesScrollPane);
        myBehaviorBuilders = new ArrayList<BehaviorBuilder>();
        myBehaviorBuilders.add(new MovementBuilder(enemyRoutes));
        myBehaviorBuilders.add(new AttackBuilder());
        createCenterDisplay();
    }

    private void createCenterDisplay() {
        VBox centerOptionsBox = new VBox(25);
        Label title = new Label("Enemy Behaviors");
        title.getStyleClass().add("behaviorsTitle");
        centerOptionsBox.getChildren().addAll(title, createEnemyNameTextField());
        centerOptionsBox.setPadding(new Insets(10));
        for(BehaviorBuilder builder:myBehaviorBuilders){
            centerOptionsBox.getChildren().add(builder.getContainer());
        }
        centerOptionsBox.getChildren().add(createSaveButton());	
        myPane.setCenter(centerOptionsBox);
        myDragAndDrop = 
                new DragAndDropFilePane(560, AuthorController.SCREEN_HEIGHT, new String[]{".jpg", ".jpeg", ".png"}, 
                                        ENEMY_IMAGES_DIR);
        myDragAndDrop.addObserver(this);
        myDragAndDrop.getPane().getStyleClass().add("dragAndDrop");
        myPane.setRight(myDragAndDrop.getPane());
    }

    private VBox createEnemyNameTextField () {
        VBox box = new VBox(5);
        Label label = new Label("Enemy Name");
        myEnemyNameField = new TextField();
        box.getChildren().addAll(label, myEnemyNameField);
        return box;
    }

    private Button createSaveButton(){
        Button saveButton = new Button("Save");
        saveButton.setOnAction(event->handleSaveButtonClicked());
        return saveButton;		
    }

    private void handleSaveButtonClicked () {
        Map<String, IBehavior> iBehaviorMap = buildIBehaviorMap();
        if(fieldsAreValidForEnemyCreation(iBehaviorMap) && enemyNameIsUnique()){
            makeNewEnemy(iBehaviorMap);
            clearFields();
        }
    }

    private void makeNewEnemy (Map<String, IBehavior> iBehaviorMap) {
        myEnemies.add(new BaseActor(iBehaviorMap,
                                    myImageForEnemyBeingCreated,
                                    myEnemyNameField.getText()));
    }

    private boolean enemyNameIsUnique () {
        return myEnemies
                .stream()
                .filter(enemy -> enemy.toString().equalsIgnoreCase(myEnemyNameField.getText()))
                .count() == 0;
    }
    
    private void clearFields() {
        myEnemyNameField.clear();
        myPane.getChildren().remove(myPane.getRight());
        myPane.setRight(myDragAndDrop.getPane());
        for(BehaviorBuilder builder:myBehaviorBuilders) {
            builder.reset();
        }
    }

    private boolean fieldsAreValidForEnemyCreation (Map<String, IBehavior> iBehaviorMap) {
        return myImageForEnemyBeingCreated != null && 
                !iBehaviorMap.isEmpty() &&
                !myEnemyNameField.getText().isEmpty();
    }

    private Map<String, IBehavior> buildIBehaviorMap () {
        Map<String, IBehavior> iBehaviorMap = new HashMap<String, IBehavior>();
        for(BehaviorBuilder builder:myBehaviorBuilders){
            IBehaviorKeyValuePair pair = builder.buildBehavior();
            iBehaviorMap.put(pair.getTypeOfBehavior(), pair.getMyIBehavior());
        }
        return iBehaviorMap;
    }

    @Override
    public void update (Observable arg0, Object arg1) {
        try {
            myImageForEnemyBeingCreated = new Image(new FileInputStream((File) arg1), ENEMY_IMG_WIDTH, ENEMY_IMG_HEIGHT, false, true);    
            ImageView imageView = new ImageView(myImageForEnemyBeingCreated);
            imageView.setScaleX(1.5);
            imageView.setScaleY(1.5);
            imageView.setLayoutX(220);
            imageView.setLayoutY(220);
            Pane rightPane = new Pane();
            rightPane.setPrefWidth(560);
            rightPane.getChildren().add(imageView);
            rightPane.setStyle("-fx-background-color: white;");
            myPane.getChildren().remove(myDragAndDrop);
            myPane.setRight(rightPane);
        }
        catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
    }
}