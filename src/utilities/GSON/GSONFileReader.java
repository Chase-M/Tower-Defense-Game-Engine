package utilities.GSON;
import gameAuthoring.mainclasses.LevelDataWrapper;
import gameAuthoring.scenes.actorBuildingScenes.TowerUpgradeGroup;
import gameEngine.actors.BaseActor;
import gameEngine.actors.BaseEnemy;
import gameEngine.actors.BaseTower;
import gameEngine.levels.BaseLevel;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.lang.reflect.Type;
import utilities.errorPopup.ErrorPopup;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class GSONFileReader {

    Gson gson = new Gson();
    private List<TowerUpgradeGroup> towerList;
    private List<BaseLevel> levelList;

    public GSONFileReader(){


    }

    public List<TowerUpgradeGroup> readTowerFromFile(String fileName, String directory){
        try{		
            BufferedReader br = new BufferedReader( new FileReader(directory +fileName+".json"));	
            System.out.println(br.readLine());
            towerList = gson.fromJson(br.readLine(), new TypeToken<List<TowerUpgradeGroup>>() {}.getType() );	
        } catch(IOException e){
            new ErrorPopup("File" +fileName+ ".json could not be found.");
        }		
        return towerList;		
    }


    public List<BaseLevel> readLevelfromFile(String fileName, String directory){
        try{		
            BufferedReader br = new BufferedReader( new FileReader(directory +fileName+".json"));        
            LevelDataWrapper wrapper = gson.fromJson(br, LevelDataWrapper.class);  
            System.out.println(wrapper.levels.get(0).getEnemyMap().toString());
        } catch(IOException e){
            new ErrorPopup("File" +fileName+ ".json could not be found.");
        }		

        return levelList;
    }
}
