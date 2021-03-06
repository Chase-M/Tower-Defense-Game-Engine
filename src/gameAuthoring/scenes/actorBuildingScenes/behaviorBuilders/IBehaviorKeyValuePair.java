package gameAuthoring.scenes.actorBuildingScenes.behaviorBuilders;

import gameEngine.actors.behaviors.IBehavior;

/**
 * Wrapper for key value pair in Behavior map that is necessary to create an actor.
 * @author Austin Kyker
 *
 */
public class IBehaviorKeyValuePair {
    private IBehavior myIBehavior;
    private String myTypeOfBehavior;
    
    public IBehaviorKeyValuePair(String type, IBehavior behavior) {
        myTypeOfBehavior = type;
        myIBehavior = behavior;
    }
    
    public String getTypeOfBehavior() {
        return myTypeOfBehavior;
    }
    
    public IBehavior getMyIBehavior () {
        return myIBehavior;
    }
}
