package nachos.threads;

import nachos.machine.*;

/**
 * A <i>communicator</i> allows threads to synchronously exchange 32-bit
 * messages. Multiple threads can be waiting to <i>speak</i>,
 * and multiple threads can be waiting to <i>listen</i>. But there should never
 * be a time when both a speaker and a listener are waiting, because the two
 * threads can be paired off at this point.
 */
public class Communicator {
    /**
     * Allocate a new communicator.
     */
    public Communicator() {
        this.conditionLock = new Lock();
        this.isListening = new Condition(conditionLock);
        this.hasSpoken = new Condition(conditionLock);
        this.hasListened = new Condition(conditionLock);
        this.written = false;
    }

    /**
     * Wait for a thread to listen through this communicator, and then transfer
     * <i>word</i> to the listener.
     *
     * <p>
     * Does not return until this thread is paired up with a listening thread.
     * Exactly one listener should receive <i>word</i>.
     *
     * @param	word	the integer to transfer.
     */
    public void speak(int word) {
        conditionLock.acquire();
        
        try {
            while (written) {
                hasListened.sleep();
            }      
            
            
            this.word = word;
            written = true;
            
            System.out.println("Said " + word);
            
            hasSpoken.wake();
            isListening.sleep();
            
            hasListened.wake();
            
            return;
        } finally {
            conditionLock.release();
        }
    }

    /**
     * Wait for a thread to speak through this communicator, and then return
     * the <i>word</i> that thread passed to <tt>speak()</tt>.
     *
     * @return	the integer transferred.
     */    
    public int listen() {
        conditionLock.acquire();
        
        try {
            if (!written) {
                hasSpoken.sleep();
            }
            
            int ret = this.word;
            written = false;
            
            System.out.println("Heard: " + ret);
            
            isListening.wake();
            
            return ret;
        } finally {
            conditionLock.release();
        }
        
    }
    
    private Lock conditionLock;
    private Condition isListening, hasSpoken, hasListened;
    private int word;
    private boolean written;
}
