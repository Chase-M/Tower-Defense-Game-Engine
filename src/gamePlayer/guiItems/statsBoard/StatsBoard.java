package gamePlayer.guiItems.statsBoard;

import gamePlayer.guiItems.GuiItem;
import gamePlayer.mainClasses.guiBuilder.GuiConstants;
import java.io.File;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Dimension2D;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import utilities.XMLParsing.XMLParser;

public class StatsBoard implements GuiItem {
    private XMLParser myParser;
    private TableView<GameStats> myTableView;
    private Dimension2D mySize;
    
    @Override
    public void initialize (Dimension2D containerSize) {
        myParser = new XMLParser(new File(myPropertiesPath+this.getClass().getSimpleName()+".XML")); 

        myTableView = new TableView<GameStats>();
        Dimension2D sizeRatio = myParser.getDimension("SizeRatio");
        mySize = new Dimension2D(containerSize.getWidth()*sizeRatio.getWidth(),
                                             containerSize.getHeight()*sizeRatio.getHeight());
        myTableView.setPrefSize(mySize.getWidth(),mySize.getHeight());
        
        GuiConstants.GUI_MANAGER.registerStatsBoard(this);
    }
    
    public void setGameStats(List<GameStats> stats) {
        //convert list into observable list
        ObservableList<GameStats> statsList = FXCollections.observableArrayList(stats);
        myTableView.setItems(statsList);
        
        TableColumn<GameStats,String> statCol = new TableColumn<GameStats,String>("Stat");
        statCol.setCellValueFactory(new PropertyValueFactory("gameStat"));
        statCol.setPrefWidth(mySize.getWidth()/2);
        
        TableColumn<GameStats,String> valueCol = new TableColumn<GameStats,String>("Value");
        valueCol.setCellValueFactory(new PropertyValueFactory("statValue"));
        valueCol.setPrefWidth(mySize.getWidth()/2);
        
        myTableView.getColumns().setAll(statCol, valueCol);
        
    }

    @Override
    public Node getNode () {
        return myTableView;
    }
}
