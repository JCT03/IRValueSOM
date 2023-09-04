package robosim.ai;

import robosim.core.Action;
import robosim.core.Controller;
import robosim.core.Simulator;
import robosim.reinforcement.QTable;

public class ObstacleAvoider implements Controller {
    public QTable qTable = new QTable(3, 4, 0, 5, 2, 0.5);
    //State 0 = moving forwards
    //State 1 = hit
    //State 2 = Turning, moving backwards
    int NextAction;

    Action[] actions = new Action[]{Action.FORWARD,Action.LEFT, Action.RIGHT, Action.BACKWARD};
    @Override
    public void control(Simulator sim) {
        if(sim.wasHit())
            NextAction = qTable.senseActLearn(1, -5);
        else if(qTable.getLastAction() == 0)
            NextAction = qTable.senseActLearn(0, 1);
        else
            NextAction = qTable.senseActLearn(2, 0);
        actions[NextAction].applyTo(sim);
    }
}
