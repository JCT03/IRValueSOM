package robosim.ai;

import core.Duple;
import robosim.core.*;
import robosim.reinforcement.QTable;

import javax.swing.text.html.Option;
import java.util.Optional;

public class VaccumBot implements Controller {
    public QTable qTable = new QTable(7, 4, 0, 5, 1, 0.5);
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
        else if(dirtObj.isPresent() && dirtObj.get().getR() > closestBad){
            if(closestBad > 30)
                NextAction = qTable.senseActLearn(3, 0);
            else
                NextAction = qTable.senseActLearn(2, 0);
        } else if(dirtObj.isPresent()) {
            double angle = Math.abs(dirtObj.get().getTheta()) - Robot.ANGULAR_VELOCITY;
            if (Math.abs(angle) < 5) {
                NextAction = qTable.senseActLearn(4, 0);
            } else if (angle < 0) {
                NextAction = qTable.senseActLearn(5, 0);
            } else {
                NextAction = qTable.senseActLearn(6, 0);
            }
        } else {
            NextAction = qTable.senseActLearn(0, 0);
        }

    }
    public Optional<Action> dirtAction(Simulator sim) {
        int leftDirt = 0;
        int rightDirt = 0;
        int straightDirt = 0;
        for (Duple<SimObject, Polar> obj: sim.allVisibleObjects()) {
            if (obj.getFirst().isVacuumable()) {
                if (Math.abs(obj.getSecond().getTheta()) < Robot.ANGULAR_VELOCITY) {
                    straightDirt += 1;
                } else if (obj.getSecond().getTheta() < 0) {
                    leftDirt += 1;
                } else {
                    rightDirt += 1;
                }
            }
        }
        if (straightDirt > 0) {
            return Optional.of(Action.FORWARD);
        } else if (leftDirt > 0) {
            return Optional.of(Action.LEFT);
        } else if (rightDirt > 0) {
            return Optional.of(Action.RIGHT);
        } else {
            return Optional.empty();
        }
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
