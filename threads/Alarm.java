package nachos.threads;

import java.util.PriorityQueue;
import java.util.Queue;
import nachos.machine.*;

/**
 * Uses the hardware timer to provide preemption, and to allow threads to sleep
 * until a certain time.
 */
public class Alarm {
    /**
     * Allocate a new Alarm. Set the machine's timer interrupt handler to this
     * alarm's callback.
     *
     * <p><b>Note</b>: Nachos will not function correctly with more than one
     * alarm.
     */
    public Alarm() {
	Machine.timer().setInterruptHandler(new Runnable() {
		public void run() { timerInterrupt(); }
	    });
        
        this.waitQueue = new PriorityQueue<>();
    }

    /**
     * The timer interrupt handler. This is called by the machine's timer
     * periodically (approximately every 500 clock ticks). Causes the current
     * thread to yield, forcing a context switch if there is another thread
     * that should be run.
     */
    public void timerInterrupt() {
        boolean intStatus = Machine.interrupt().disable();
        
        long currentTime = Machine.timer().getTime();
        
        while (!waitQueue.isEmpty() && waitQueue.peek().wakeTime <= currentTime) {
            waitQueue.poll().target.ready();
        }
        
        Machine.interrupt().restore(intStatus);
        
	KThread.currentThread().yield();
    }

    /**
     * Put the current thread to sleep for at least <i>x</i> ticks,
     * waking it up in the timer interrupt handler. The thread must be
     * woken up (placed in the scheduler ready set) during the first timer
     * interrupt where
     *
     * <p><blockquote>
     * (current time) >= (WaitUntil called time)+(x)
     * </blockquote>
     *
     * @param	x	the minimum number of clock ticks to wait.
     *
     * @see	nachos.machine.Timer#getTime()
     */
    public void waitUntil(long x) {
        boolean intStatus = Machine.interrupt().disable();
	
        long wakeTime = Machine.timer().getTime() + x;
        waitQueue.add(new Entry(wakeTime, KThread.currentThread()));
        KThread.sleep();
        
        Machine.interrupt().restore(intStatus);	
    }
    
    
    //Entry class to store the waiting threads and their wake time
    private class Entry implements Comparable<Entry> {
        Entry(long wakeTime, KThread target) {
            this.wakeTime = wakeTime;
            this.target = target;
        }
        
        public int compareTo(Entry other) {
            if (this.wakeTime <= other.wakeTime)
                return -1;
            
            return 1;
        }
        
        private long wakeTime;
        private KThread target;
    }
    
    private Queue<Entry> waitQueue;
}
