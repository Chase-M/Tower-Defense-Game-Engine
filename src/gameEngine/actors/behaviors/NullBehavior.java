package gameEngine.actors.behaviors;

import java.util.Set;
import gameEngine.actors.BaseActor;

/**
 * Null behavior so that the authoring environment can allow users to choose no behavior
 * 
 * @author Chase Malik, Timesh Patel
 *
 */
public class NullBehavior implements IBehavior {

    @Override
    public void execute (BaseActor actor) {
        // nothing
    }

    @Override
    public IBehavior copy () {
        return new NullBehavior();
    }

    @Override
    public Set<Class<? extends BaseActor>> getType () {
        // TODO Auto-generated method stub
        return null;
    }

}
