import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import frc.robot.AutonomousActionQueue;
import frc.robot.RobotMotors;
import frc.robot.AutonomousActionQueue.ActionType;

public class AutonomousActionQueueTest {

    @Nested
    class Iforgor {
        AutonomousActionQueue queue;
        RobotMotors motors;

        @BeforeEach
        public void setup() {
            MotorController cntrl = mock(MotorController.class);
            motors = new RobotMotors(cntrl, cntrl, cntrl, cntrl);
            queue = new AutonomousActionQueue(motors);
        }


        @Test
        public void QueuePositionTest() {
            assertEquals(0, queue.getQueuePosition());
            queue.queue(ActionType.DO_NOTHING, 10, .252);
            queue.queue(ActionType.DO_NOTHING, 10, 34324);
            assertEquals(0, queue.getQueuePosition());
            queue.executeNext();
            assertEquals(1, queue.getQueuePosition());
        }

        @Test
        public void tooLargeAPositionTest() {
            queue.queue(ActionType.DO_NOTHING, 0, 0);
            queue.queue(ActionType.DO_NOTHING, 0, 1);
            queue.queue(ActionType.DO_NOTHING, 0, 2);
            queue.setQueuePosition(512);
            assertEquals(0, queue.getQueuePosition());
        }

        @Test
        public void testForExceptions() {

            assertDoesNotThrow(() -> {

                queue.queue(ActionType.DO_NOTHING, 0, 0);
                queue.queue(ActionType.DO_NOTHING, 0, 1);
                queue.queue(ActionType.DO_NOTHING, 0, 2);
                queue.setQueuePosition(23423);
                queue.executeNext();
                queue.setQueuePosition(-21985);
                queue.executeNextNActions(10);
                queue.executeCurrentAction();
                queue.skipNActions(200);
                queue.executeCurrentAction();
                queue.executeCurrentActionNTimes(44);
                queue.setQueuePosition(1);
                queue.executeRemaining(10);
            });

        }


    }
}
