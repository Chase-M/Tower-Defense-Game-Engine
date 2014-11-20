package gameEngine;

import gameEngine.actors.BaseActor;
import gameEngine.levels.BaseLevel;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.scene.Group;
import javafx.scene.Node;
import utilities.ErrorPopup;


public class MainGameManager {

    private static final double ONE_SECOND_IN_NANO = 1000000000.0;
    private static final int FPS = 30;

    private AtomicBoolean myIsRunning;
    private AtomicBoolean myPauseRequested;
    private AtomicBoolean readyToPlay;
    private AtomicBoolean myIsPaused;

    private double myRenderInterval;
    private double myUpdateInterval;
    // 1 is normal speed
    private AtomicInteger myUpdateSpeed;

    private double myLastUpdateTime;
    private double myLastRenderTime;
    
    private Map<Class<? extends BaseActor>, BaseActor> myAvailableActors;
    
    private static final int ENEMY_INDEX = 0;
    private static final int PROJECTILE_INDEX = 1;
    private static final int TOWER_INDEX = 2;
    
    private Group myTowerGroup;
    private Group myProjectileGroup;
    private Group myEnemyGroup;    
    private double levelDuration;
    
    private static final String INVALID_TOWER_ERROR = "Invalid Tower";
    
    private BaseLevel myCurrentLevel;
    private List<BaseActor> myTowersToAdd;
    
    public MainGameManager (Group engineGroup) {
        myIsRunning = new AtomicBoolean(false);
        readyToPlay = new AtomicBoolean(false);
        myPauseRequested = new AtomicBoolean(false);
        myIsPaused = new AtomicBoolean(true);

        myRenderInterval = ONE_SECOND_IN_NANO / FPS;
        myUpdateInterval = myRenderInterval;
        myUpdateSpeed = new AtomicInteger(1);

        myLastUpdateTime = System.nanoTime();
        myLastRenderTime = System.nanoTime();
        
        myTowerGroup = new Group();
        myProjectileGroup = new Group();
        myEnemyGroup = new Group();
        
        addToGroup(engineGroup, myTowerGroup);
        addToGroup(engineGroup, myProjectileGroup);
        addToGroup(engineGroup, myEnemyGroup);
    }

    private void addToGroup(Group group, Node node){
        group.getChildren().add(node);
    }
    
    public void fastForward() {
        //TODO: change 
        speedUp(4);
    }
    
    public void revertToNormalSpeed(){
        speedUp(1);
    }
    
    public void speedUp (int magnitude) {
        myUpdateSpeed.set(magnitude);
    }

    public void loadState (String fileName) {
    }

    public void saveState (String fileName) {
    }

    public void initializeGame (String fileName) {
        
    }

    public synchronized boolean addTower (String identifier, double x, double y) {
        BaseActor tower = myCurrentLevel.createTower(identifier, x, y);
        if(tower == null){
            return false;
        }
        myTowersToAdd.add(tower);   
        return false;
    }

    private void createNewActor(Class<? extends BaseActor> actorType){
        BaseActor exampleActor = myAvailableActors.get(actorType);
        BaseActor newActor = exampleActor.copy();
        //newActor.setX
    }
    
    public void start () {
        if (readyToPlay.get()) {
            myIsRunning.set(true);
            Thread gameLoop = new Thread() {
                public void run () {
                    gameLoop();
                }
            };
            gameLoop.start();
        }
        else {
            new ErrorPopup("Not yet ready to play");
        }
    }

    public void pause () {
        myPauseRequested.set(true);
    }

    public void resume () {
        myPauseRequested.set(false);
    }
    
    private void gameLoop () {
        while (myIsRunning.get()) {
            double now = System.nanoTime();
            if (!myPauseRequested.get() && readyToPlay.get()) {
                myIsPaused.set(false);
                double adjustedUpdateInterval = myUpdateInterval / myUpdateSpeed.get();
                double updateTimeDifference = now - myLastUpdateTime;
                double timeBetweenUpdateAndRender = now - myLastRenderTime;
                // Allow for catchup in the case of inaccurate thread waking up.
                do {
                    double updateStart = System.nanoTime();
                    update();
                    double updateEnd = System.nanoTime();
                    myLastUpdateTime += Math.max(adjustedUpdateInterval, updateEnd - updateStart);
                    updateTimeDifference = now - myLastUpdateTime;
                    timeBetweenUpdateAndRender = updateEnd - myLastRenderTime;
                } // get rid of second check if frame dropping is unimportant. Allows for more
                  // accurate updates before render
                while (updateTimeDifference > adjustedUpdateInterval &&
                       timeBetweenUpdateAndRender < myRenderInterval);
                if (now - myLastRenderTime >= myRenderInterval) {
                    render();
                    myLastRenderTime = now;
                }

                while (now - myLastRenderTime < myRenderInterval &&
                       now - myLastUpdateTime < adjustedUpdateInterval) {
                    Thread.yield();
                    try {
                        Thread.sleep(0, 500);
                    }
                    catch (InterruptedException e) {
                        // probably fine
                    }
                    now = System.nanoTime();
                }
            }
            else {
                myIsPaused.set(true);
                // Thread.yield();
                try {
                    Thread.sleep(0, 500);
                }
                catch (InterruptedException e) {
                    // Probably fine
                }
            }
        }
    }

    private void update () {
        //addNewActors();
        
        updateActors(myEnemyGroup);
        updateActors(myProjectileGroup);
        updateActors(myTowerGroup);
    }

    private void updateActors(Group group){
        List<Node> children = group.getChildren();
        for(Node child : children){
            BaseActor actor = (BaseActor)child;
            actor.update();
        }
    }
    private void render () {

    }

    public void quit () {
        myIsRunning.set(false);
    }

    public synchronized void loadLevel (BaseLevel level) {
        readyToPlay.set(false);
        while (!myIsPaused.get()) {
            try {
                Thread.sleep(0, 500);
            }
            catch (InterruptedException e) {
                // Probably fine
            }
        }
        myTowerGroup.getChildren().clear();
        myProjectileGroup.getChildren().clear();
        myEnemyGroup.getChildren().clear();
        
        //load
        readyToPlay.set(true);
    }
}
