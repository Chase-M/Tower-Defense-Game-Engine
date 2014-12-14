package gameEngine.actors.behaviors;

import gameAuthoring.scenes.pathBuilding.buildingPanes.BuildingPane;
import gameAuthoring.scenes.pathBuilding.pathComponents.routeToPointTranslation.BackendRoute;
import gameEngine.actors.BaseActor;
import gameEngine.actors.BaseProjectile;
import gameEngine.actors.RealActor;
import java.util.List;
import javafx.geometry.Point2D;

public class AoEAttack extends BaseAttack{
    private static final double DISTANCE_FACTOR = 2.0;

    private double myNumBullets;
    public AoEAttack (List<Double> list) {
        super(list);
        // TODO Auto-generated constructor stub
        myNumBullets=list.get(2)==null ? 8 : list.get(2);
       
    }

    @Override
    public IBehavior copy () {
        // TODO Auto-generated method stub
        return new AoEAttack(myList);
    }

    @Override
    protected void performAttack (BaseActor actor) {
        // TODO Auto-generated method stub
        if(actor.getEnemiesInRange(myRange)!=null){
            for(int i=0; i<myNumBullets; i++){
                Point2D shooterLoc = new Point2D(actor.getX(), actor.getY());
                Point2D targetLoc=new Point2D(actor.getX()+myRange*Math.cos(360.0/myNumBullets),actor.getY()+myRange*Math.cos(360.0/myNumBullets));
                Point2D unitVector = targetLoc.subtract(shooterLoc).normalize();
                BackendRoute route =
                        new BackendRoute(shooterLoc, shooterLoc.add(unitVector
                                .multiply(BuildingPane.DRAW_SCREEN_WIDTH * DISTANCE_FACTOR)));
                BaseProjectile projectile = new BaseProjectile(((RealActor) actor).getProjectile().copy());
                projectile.getInfo().getMove().setRoute(route);
                ((RealActor)actor).spawnProjectile(projectile);
            }
        }
    }

}
