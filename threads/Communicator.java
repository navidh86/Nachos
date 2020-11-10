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
        this.speakerReady = new Condition2(conditionLock);
        this.listenerReady = new Condition2(conditionLock);
        this.spoke = new Condition2(conditionLock);
        this.listened = new Condition2(conditionLock);
        this.transferred = new Condition2(conditionLock);
        this.isSpeaking = false;
        this.isListening = false;
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
            while (isSpeaking) {
                transferred.sleep();
            }      
            
            isSpeaking = true;
            speakerReady.wake();
            listenerReady.sleep();
            
            this.word = word;
            System.out.println(KThread.currentThread().getName() + " said: " + word);
            spoke.wake();
            listened.sleep();

            isSpeaking = false;
            isListening = false;
            transferred.wake();
            
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
            while (!isSpeaking || isListening) {
                speakerReady.sleep();
            }
     
            isListening = true;
            listenerReady.wake();
            spoke.sleep();
            
            int ret = this.word;
            System.out.println(KThread.currentThread().getName() + " heard: " + ret);
            
            listened.wake();
            
            return ret;
        } finally {
            conditionLock.release();
        }    
    }
    
    private Lock conditionLock;
    private Condition2 speakerReady, listenerReady, spoke, listened, transferred;
    private int word;
    private boolean isSpeaking, isListening;
}
