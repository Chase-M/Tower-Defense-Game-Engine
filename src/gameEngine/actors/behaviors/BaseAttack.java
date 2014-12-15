package gameEngine.actors.behaviors;

import gameEngine.actors.BaseActor;
import java.util.List;


/**
 * Represents the basic attack behavior, where projectile(s) to be fired
 * 
 * @author Chase Malik, Timesh Patel
 *
 */
public abstract class BaseAttack implements IBehavior {
    protected List<Double> myList;
    protected double myAttackSpeed;
    protected double myRange;
    protected int myCooldown;
    protected final static int READY_TO_SHOOT = 0;
    private final static String myName = "attack";

    public BaseAttack (double attackSpeed) {
        myAttackSpeed = attackSpeed;
        myCooldown = READY_TO_SHOOT;
    }
    public BaseAttack(List<Double> list){
        myList=list;
        myAttackSpeed=list.get(0);
        myRange=list.get(1);
        myCooldown = READY_TO_SHOOT;
    }
    protected boolean readyToShoot () {
        return myCooldown == READY_TO_SHOOT;
    }

    @Override
    public void execute (BaseActor actor) {
        if (readyToShoot()) {
            performAttack(actor);
            myCooldown = (int) myAttackSpeed;
        }
        else {
            myCooldown--;
        }

    }
    /*
     * attack to be performed on execute when ready to shoot
     */
    protected abstract void performAttack (BaseActor actor);

    public void setAttackSpeed (int i) {
        myAttackSpeed = i;

    }

    public String toString () {
        return myName;
    }
    
    public double getRange(){
        return myRange;
    }
}
