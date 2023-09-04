package robosim.ai;

import robosim.core.Action;
import robosim.core.Controller;
import robosim.core.Simulator;
import robosim.reinforcement.QTable;

public class ObstacleAvoider implements Controller {
    public QTable qTable = new QTable(4, 4, 0, 10, 5, 0.3);
    //State 0 = hit
    //State 1 = close
    //State 2 = far
    int NextAction;

    Action[] actions = new Action[]{Action.FORWARD,Action.LEFT, Action.RIGHT, Action.BACKWARD};
    @Override
    public void control(Simulator sim) {
        if(sim.wasHit())
            NextAction = qTable.senseActLearn(1, -5);
        else if(qTable.getLastAction() == 0)
            if(sim.findClosestProblem() < 30)
                NextAction = qTable.senseActLearn(1, 1);
            else
                NextAction = qTable.senseActLearn(2, 1);
        else
            if(sim.findClosestProblem() < 30)
                NextAction = qTable.senseActLearn(1, 0);
            else
                NextAction = qTable.senseActLearn(2, 0);
        actions[NextAction].applyTo(sim);
    }
}
