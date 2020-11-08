package nachos.proj1;

import nachos.machine.Machine;
import nachos.threads.ThreadedKernel;

/**
 *
 * @author navidh86
 */
public class AlarmTest {
    public static void startTest() {
        System.out.println("Current time: " + Machine.timer().getTime());
        ThreadedKernel.alarm.waitUntil(1000);
        System.out.println("Current time: " + Machine.timer().getTime());
    }
}
