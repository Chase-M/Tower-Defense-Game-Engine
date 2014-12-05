package gameEngine.actors.behaviors;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import gameEngine.actors.BaseActor;
import gameEngine.actors.BaseEnemy;


public class FarthestRangeAttack extends RangeAttack {

    public FarthestRangeAttack (double attackSpeed) {
        super(attackSpeed);
    }

    @Override
    public Comparator<BaseActor> defineComparison (BaseActor a) {
        return (BaseActor a1, BaseActor a2) -> Double
                .compare(((BaseMovementBehavior) (a1.getBehavior("movement")))
                        .getRemainingDistance(),
                         ((BaseMovementBehavior) (a2.getBehavior("movement")))
                                 .getRemainingDistance());
    }

    @Override
    public IBehavior copy () {
        return new FarthestRangeAttack(myAttackSpeed);
    }

    @Override
    public Set<Class<? extends BaseActor>> getType () {
        Set<Class<? extends BaseActor>> a = new HashSet<Class<? extends BaseActor>>();
        a.add(BaseEnemy.class);
        return a;
    }
}
