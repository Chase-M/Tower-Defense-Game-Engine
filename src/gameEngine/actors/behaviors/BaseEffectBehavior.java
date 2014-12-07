package gameEngine.actors.behaviors;

import gameEngine.actors.BaseActor;
import java.util.Set;

public abstract class BaseEffectBehavior implements IBehavior {

    protected double myChange;
    protected String myString;
    public BaseEffectBehavior(double change){
        myChange = change;
    }
    
    public abstract void performEffect(BaseActor actor);
    public String toString(){
        return myString;
    }
    @Override
    public int hashCode(){
        return myString.hashCode();
    }
    @Override
    public boolean equals(Object o){
        return this.myString.equals(o.toString());
    }
}
