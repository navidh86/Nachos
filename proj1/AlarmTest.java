package nachos.proj1;

import nachos.machine.Machine;
import nachos.threads.KThread;
import nachos.threads.ThreadedKernel;

/**
 *
 * @author navidh86
 */
public class AlarmTest {
    public static void startTest() {
        System.out.println("********* Start of alram test ********\n");
        
        KThread at1 = new KThread(new AlarmRunnable(3000)).setName("Alarm Thread 1");
        KThread at2 = new KThread(new AlarmRunnable(2000)).setName("Alarm Thread 2");
        KThread at3 = new KThread(new AlarmRunnable(1000)).setName("Alarm Thread 3");
        
        at1.fork();
        at2.fork();
        at3.fork();
        
        at1.join();
        at2.join();
        at3.join();
        
        System.out.println("\n********* End of alram test ********\n");
    }
}

class AlarmRunnable implements Runnable {
    AlarmRunnable(long waitTime) {
        this.waitTime = waitTime;
    }
    
    public void run() {
        System.out.println(KThread.currentThread().getName() + ": Current time: " + Machine.timer().getTime() + 
                " need to wait for " + waitTime + " ticks");
        ThreadedKernel.alarm.waitUntil(waitTime);
        System.out.println(KThread.currentThread().getName() + ": Current time: " + Machine.timer().getTime());
    }
    
    private long waitTime;
}
