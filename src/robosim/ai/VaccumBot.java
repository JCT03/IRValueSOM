package robosim.ai;

import core.Duple;
import robosim.core.*;
import robosim.reinforcement.QTable;

import javax.swing.text.html.Option;
import java.util.Optional;

public class VaccumBot implements Controller {
    public QTable qTable = new QTable(7, 4, 0, 5, 2, 0.5);
    // States:
    // 0: Bumped
    // 1: DirtFound
    // 2: Close
    // 3: Clear
    // 4: Dirt Front
    // 5: Dirt Left
    // 6: Dirt Right
    public int dirt = 0;
    int NextAction;

    Action[] actions = new Action[]{Action.FORWARD,Action.LEFT, Action.RIGHT, Action.BACKWARD};
    @Override
    public void control(Simulator sim) {

        Optional<Polar> dirtObj = sim.findClosestDirt();
        double closestBad = sim.findClosestProblem();
        if(sim.getDirt() > dirt){
            dirt = sim.getDirt();
            NextAction = qTable.senseActLearn(1, 10);
        }
        else if (sim.wasHit()) {
            NextAction = qTable.senseActLearn(0, -5);
        }
        else if(dirtObj.isPresent() && dirtObj.get().getR() > closestBad && closestBad <30){
            NextAction = qTable.senseActLearn(2, 0);
        } 
        else if(dirtObj.isPresent()) {
            double angle = Math.abs(dirtObj.get().getTheta()) - Robot.ANGULAR_VELOCITY;
            if (Math.abs(angle) < 2) {
                NextAction = qTable.senseActLearn(4, 0);
            } else if (angle < 0) {
                NextAction = qTable.senseActLearn(5, 0);
            } else {
                NextAction = qTable.senseActLearn(6, 0);
            }
        } else {
            if (qTable.getLastAction() == 0) {
                NextAction = qTable.senseActLearn(3, 1);
            } else {
                NextAction = qTable.senseActLearn(3, 0);
            }
        }
        actions[NextAction].applyTo(sim);

    }

//    public Duple<SimObject, Polar> findClosestObject(Simulator sim) {
//        Duple<SimObject, Polar> closestObject = null;
//        for (Duple<SimObject, Polar> obj: sim.allVisibleObjects()) {
//            if(closestObject == null){
//                closestObject = obj;
//            } else if(closestObject.getSecond().getR() > obj.getSecond().getR()) {
//                closestObject = obj;
//            }
//        }
//        return closestObject.getFirst() && ob.get().getR() < e ? ob.get().getR() : e;
//    }
}
