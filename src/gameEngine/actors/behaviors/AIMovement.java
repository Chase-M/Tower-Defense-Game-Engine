package gameEngine.actors.behaviors;

import gameEngine.GridPathFinder;
import gameEngine.actors.BaseActor;
import gameEngine.actors.BaseEnemy;
import gameEngine.actors.InfoObject;
import java.util.List;
import javafx.geometry.Point2D;


public class AIMovement extends BaseMovementBehavior {
    private transient GridPathFinder myPathFinder;
    private transient List<Point2D> myAIRoute;
    private int myIndex;
    public AIMovement (double speed) {
        super(speed);
        myPathFinder = new GridPathFinder();
        myIndex=0;
        // TODO Auto-generated constructor stub
    }

    @Override
    public void execute (BaseActor actor) {
        // TODO Auto-generated method stub
        InfoObject info = actor.getInfoObject();
        if (info.getTowerTileGrid().hasBeenChanged()) {
            myAIRoute =
                    myPathFinder.getPath((BaseEnemy) actor, info.getmTowerTilePane(),
                                         info.getTowerTileGrid());
            myIndex=0;
        }
        if(myAIRoute==null){
            return;
        }
        Point2D current = new Point2D(actor.getX(), actor.getY());
        Point2D destination = myRoute.get(myIndex).getPoint();
        double distance = mySpeed;

        while (distance > destination.distance(current)) {
            myIndex++;
            if (myIndex == myRoute.size()) {
                actor.died();
                return;
            }
            distance -= destination.distance(current);
            current = new Point2D(destination.getX(), destination.getY());
            destination = myRoute.get(myIndex).getPoint();
        }
        myRemainingDistance-=mySpeed;
        Point2D vector = destination.subtract(current).normalize().multiply(mySpeed);
        Point2D answer = current.add(vector);
        move(actor, answer);
    }

    @Override
    public IBehavior copy () {
        // TODO Auto-generated method stub
        return new AIMovement(mySpeed);
    }

}
