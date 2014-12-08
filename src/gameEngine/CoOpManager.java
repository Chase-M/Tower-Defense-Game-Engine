package gameEngine;

import gameEngine.actors.BaseTower;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import utilities.GSON.objectWrappers.DataWrapper;
import utilities.chatroom.Chatroom;
import utilities.networking.HTTPConnection;


public class CoOpManager extends SingleThreadedEngineManager {

    private static final String GET_PLAYERS = "get_num_players";
    private static final String GET_MASTER_JSON = "get_master_json";
    private static final String UPDATE_MASTER_JSON = "update_master_json";
    private static final String MASTER_JSON = "master_json=";
    private static final String GAME_DIRECTORY = "game_directory=";
    private static final String MAKE_GAME = "make_game";
    private static final String SERVER_URL = "https://voogasalad.herokuapp.com/";
    private static final String JOIN_GAME = "join_game";
    private static final int REQUIRED_NUM_PLAYERS = 2;
    private static final HTTPConnection HTTP_CONNECTOR = new HTTPConnection(SERVER_URL);
    private static final int TIMER_END = 30;
    private static final double QUERY_SERVER_TIME = 1.0;
    private DoubleProperty myTimer;
    private String myDirectory;

    public CoOpManager () {
        super();
        myDirectory = "";
        myTimer = new SimpleDoubleProperty();
    }

    public void startNewGame (String directory) {
        myDirectory = directory;
        HTTP_CONNECTOR.sendPost(MAKE_GAME, GAME_DIRECTORY + directory);
    }

    public boolean isReady () {
        return Integer.parseInt(HTTP_CONNECTOR.sendGet(GET_PLAYERS)) >= REQUIRED_NUM_PLAYERS;
    }
    
    public DoubleProperty getTimer(){
        return myTimer;
    }

    public boolean joinGame () {
        myDirectory = HTTP_CONNECTOR.sendPost(JOIN_GAME, "");
        return !myDirectory.equals("None");
    }

    public String initializeGame (Pane engineGroup) {
        addGroups(engineGroup);
        super.initializeGame(myDirectory);
        new Chatroom();
        allowInteraction();
        return myDirectory;
    }

    private void allowInteraction () {
        myTimer.set(30);
        Timeline timeline = new Timeline();
        timeline.setCycleCount(TIMER_END);
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(QUERY_SERVER_TIME),
                                                 event -> getTowersFromServer()));
        timeline.setOnFinished(event -> startLevel());
        timeline.play();
    }
    
    @Override
    public void changeRunSpeed(double d){
        // nothing
    }

    private void startLevel () {
        getTowersFromServer();
        myTimer.set(0);
        super.resume();
    }

    @Override
    protected void onLevelEnd () {
        super.onLevelEnd();
        allowInteraction();
    }

    private void writeTowersToServer () {
        HTTP_CONNECTOR.sendPost(UPDATE_MASTER_JSON, MASTER_JSON + convertTowersToString());
    }

    private String convertTowersToString () {
        List<DataWrapper> wrapper = new ArrayList<>();
        for (BaseTower tower : myTowerGroup) {
            wrapper.add(new DataWrapper(tower));
        }
        return myFileWriter.convertWrappersToJson(wrapper);
    }

    private void getTowersFromServer () {
        myTimer.set(myTimer.get()-QUERY_SERVER_TIME);
        String response = HTTP_CONNECTOR.sendGet(GET_MASTER_JSON);
        if(response.trim().equals("None")){
            return;
        }
        List<DataWrapper> listFromServer = myFileReader.readWrappers(response);
        for (BaseTower tower : myTowerGroup) {
            if (!listFromServer.contains(new DataWrapper(tower))) {
                myTowerGroup.addActorToRemoveBuffer(tower);
            }
            else {
                listFromServer.remove(new DataWrapper(tower));
            }
        }
        for (DataWrapper wrapper : listFromServer) {
            super.addTower(wrapper.getName(), wrapper.getX(), wrapper.getY());
        }
    }

    @Override
    public void removeTower (ImageView node) {
        if (myTimer.get()>0) {
            getTowersFromServer();
            super.removeTower(node);
            writeTowersToServer();
        }
    }

    @Override
    public ImageView addTower (String name, double x, double y) {
        if (myTimer.get()>0) {
            getTowersFromServer();
            ImageView ans = super.addTower(name, x, y);
            writeTowersToServer();
            return ans;
        }
        else {
            return null;
        }
    }
}
