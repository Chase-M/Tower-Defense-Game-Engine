package gameEngine.actors.behaviors;

import gameEngine.actors.BaseActor;

public class SlowEffect extends BaseOnHitBehavior {
   
    public SlowEffect (double duration, double multiplier) {
        super(duration, multiplier);
        // TODO Auto-generated constructor stub
        myString="slowEffect";
    }
    public SlowEffect(double multiplier){
        this(30.0,multiplier);
    }
    
    @Override
    public IBehavior copy () {
        // TODO Auto-generated method stub
        return new SlowEffect(myDuration,myMultiplier);
    }

    @Override
    public void end (BaseActor actor) {
        // TODO Auto-generated method stub
        BaseMovementBehavior m=((BaseMovementBehavior) actor.getBehavior("movement"));
        m.setSpeed(m.getSpeed()/myMultiplier);
    }
    @Override
    public void during (BaseActor actor) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void start (BaseActor actor) {
        // TODO Auto-generated method stub
        BaseMovementBehavior m=((BaseMovementBehavior) actor.getBehavior("movement"));
        double d=m.getSpeed()*myMultiplier;
        m.setSpeed(d);
    }


}
