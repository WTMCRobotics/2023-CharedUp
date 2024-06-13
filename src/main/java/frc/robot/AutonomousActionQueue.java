package frc.robot;

import java.util.ArrayList;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.AutonomousActionQueue.ActionType;

public class AutonomousActionQueue {
    private int queuePosition = 0;
    private ArrayList<Action> queue;
    private AutonMovement mover;

    /**
     * A class to manage and control autonomous sequences. You can put actions in the queue and run
     * them in order later.
     * 
     * @param motors The motors on the robot
     */
    public AutonomousActionQueue(RobotMotors motors) {
        mover = new AutonMovement(motors);
        queue = new ArrayList<Action>();
    }

    /**
     * Puts an actions into the queue. The will be executed sequentially from order put in when
     * running {@link #executeNext()} or {@link #executeAll(int)}
     * 
     * @param action The type of action to queue up. e.g. MOVE_BACKWARD
     * @param value How far the robot should move when executed. In meters for moving/strafing and
     *        in degrees for rotating, and in seconds for waiting
     * @param speed How fast the executed action should be run. In M/PS for moving/strafing
     */
    public void queue(ActionType action, double value, double speed) {
        queue.add(new Action(action, value, speed));
    }

    /**
     * Executes the next action in the queue. Will move the queue counter up by one.
     * <p>
     * For example, if the queue counter was at 0, it would execute the action at slot 1
     */
    public void executeNext() {

        if (queuePosition >= queue.size() - 1) {
            return;
        }
        queuePosition++;

        Action action = queue.get(queuePosition);
        executeAction(action);


    }

    /**
     * Executes the current action in the queue. Will not move queue counter forward
     */
    public void executeCurrentAction() {
        executeAction(getCurrentAction());
    }

    /**
     * Executes the current action in the queue a specified amount of times. does not move queue
     * forward
     * 
     * @param amountOfTimes The amount of times to execute the current action
     */
    public void executeCurrentActionNTimes(int amountOfTimes) {
        for (int i = 0; i < amountOfTimes; i++) {
            executeAction(getCurrentAction());
        }
    }

    /**
     * Skips a specified amount of queued actions in the queue
     * 
     * @param amountToSkip the number of queued actions to skip
     */
    public void skipNActions(int amountToSkip) {
        queuePosition += amountToSkip;
    }

    private Action getCurrentAction() {
        if (queuePosition >= queue.size()) {
            queuePosition = queue.size() - 1;
        }
        return queue.get(queuePosition);
    }

    /**
     * Executes all the actions in the queue with a specifieced delay in between. The delay is in
     * milliseconds
     * 
     * @param millisecondDelay the delay between each action in milliseconds
     */
    public void executeAll(int millisecondDelay) {
        if (millisecondDelay < 0) {
            millisecondDelay = 0;
        }
        for (Action action : queue) {
            executeAction(action);
            Timer.delay(millisecondDelay / 1000);
        }
    }

    /**
     * Executes the rest of the remaining actions in the queue with a delay in between. the delay is
     * in milliseconds
     * 
     * @param millisecondDelay the delay between each action in milliseconds
     */
    public void executeRemaining(int millisecondDelay) {
        if (millisecondDelay < 0) {
            millisecondDelay = 0;
        }
        for (int i = queuePosition + 1; i < queue.size(); i++) {
            executeAction(queue.get(i));
            Timer.delay(millisecondDelay / 1000);
        }
    }

    /**
     * Executes the next specified amount of actions in the queue. Will move up the queue counter
     * each iteration
     * 
     * @param number the number of actions in the queue to run
     */
    public void executeNextNActions(int number) {
        for (int i = 0; i < number; i++) {
            executeNext();
        }
    }

    /**
     * Executes an action without putting it into the queue. indifferent from doing it via the
     * {@link AutonMovement} class
     * 
     * @param action The type of action to execute. e.g. MOVE_BACKWARD
     * @param value How far the robot should move when executed. In meters for moving/strafing and
     *        in degrees for rotating
     * @param speed How fast the executed action should be run. In M/PS for moving/strafing
     */
    public void executeActionWithoutQueuing(ActionType action, double value, double speed) {
        executeAction(new Action(action, value, speed));
    }

    /**
     * Sets the current position in queue. If inputted number is greater than the queue size,
     * nothing happens
     * 
     * @param position the position in queue
     */
    public void setQueuePosition(int position) {
        if (position > queue.size() || position < 0) {
            return;
        }
        queuePosition = position;
    }

    /**
     * Gets the current position in the queue
     * 
     * @return the current queue position
     */
    public int getQueuePosition() {
        return queuePosition;
    }

    /**
     * Clears the entire queue. I don't know why you'd want to do this, but it's here
     */
    public void clearQueue() {
        queue.clear();
    }

    private void executeAction(Action action) {
        switch (action.type) {
            case MOVE_BACKWARD:
                mover.AutoForward(-action.amount, action.speed);
                break;
            case MOVE_FORWARD:
                mover.AutoForward(action.amount, action.speed);
                break;
            case ROTATE_LEFT:
                mover.AutoTurn(action.amount, action.speed);
                break;
            case ROTATE_RIGHT:
                mover.AutoTurn(-action.amount, action.speed);
                break;
            case STRAFE_LEFT:
                mover.AutoStrafe(action.amount, action.speed);
                break;
            case STRAFE_RIGHT:
                mover.AutoStrafe(-action.amount, action.speed);
                break;
            case WAIT:
                Timer.delay(action.amount);
                break;
            case DO_NOTHING:
            default:
                break;

        }
    }

    public enum ActionType {
        MOVE_FORWARD, MOVE_BACKWARD, STRAFE_LEFT, STRAFE_RIGHT, ROTATE_RIGHT, ROTATE_LEFT, WAIT, DO_NOTHING
    }
}


class Action {

    public ActionType type;
    public double amount;
    public double speed;

    public Action(ActionType type, double amount, double speed) {
        this.type = type;
        this.amount = amount;
        this.speed = speed;
    }
}
