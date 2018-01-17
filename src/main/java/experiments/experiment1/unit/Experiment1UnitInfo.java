package experiments.experiment1.unit;

import core.model.btree.task.unit.Wait;
import core.unit.Unit;
import core.unit.UnitTypeInfo;
import experiments.experiment1.model.btree.task.unit.followerunit.IsApproaching;
import experiments.experiment1.model.btree.task.unit.followerunit.IsCloseEnough;
import experiments.experiment1.model.btree.task.unit.followerunit.Move;
import experiments.experiment1.model.btree.task.unit.followerunit.TurnToHeading;
import experiments.experiment1.unit.Experiment1Unit;
import experiments.experiment1.unit.FollowerUnit;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Experiment1UnitInfo {

    public static void init() {
        UnitTypeInfo.add(
                "Follower", "F", FollowerUnit.class,
                Arrays.asList(
                        Move.class,
                        Wait.class,
                        IsApproaching.class,
                        IsCloseEnough.class,
                        TurnToHeading.class
                )
        );
        UnitTypeInfo.add(
                "Wanderer", "W", Experiment1Unit.class, new ArrayList<>()
        );
    }

}