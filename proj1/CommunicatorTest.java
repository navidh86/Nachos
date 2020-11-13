package nachos.proj1;

import nachos.threads.Communicator;
import nachos.threads.KThread;

/**
 * Tests the Communicator class
 * 
 * @author navidh86
 */
public class CommunicatorTest {
    public static void startTest() {
        System.out.println("******** Start of Communicator test **********\n");
        
        Communicator c = new Communicator();
        KThread sp1 = new KThread(new Speaker(c, 1)).setName("Speaker 1");
        KThread sp2 = new KThread(new Speaker(c, 2)).setName("Speaker 2");
        KThread sp3 = new KThread(new Speaker(c, 3)).setName("Speaker 3");
        
        KThread ls1 = new KThread(new Listener(c, 1)).setName("Listener 1");
        KThread ls2 = new KThread(new Listener(c, 2)).setName("Listener 2");
          
        ls1.fork();
        ls2.fork();
        sp1.fork();
        sp2.fork();
        sp3.fork();
        
        ls1.join();
        ls2.join();
        sp1.join();
        sp2.join();
        sp3.join();
        
        System.out.println("\n******** End of Communicator test **********\n");
    }
}

class Speaker implements Runnable {
    Speaker(Communicator c, int id) {
        this.c = c;
        this.id = id;
    }
    
    public void run() {
        for (int i=0; i<5; i++) {
            KThread.yield();
            this.c.speak(i);
            KThread.yield();
        }
    }
    
    private Communicator c;
    private int id;
}

class Listener implements Runnable {
    Listener(Communicator c, int id) {
        this.c  = c;
        this.id = id;
    }
    
    public void run() {
        while (count < 5*speakers) {
            count++;
            int ret = this.c.listen();
        }
    }
    
    private Communicator c;
    private int id;
    private static int count = 0, speakers = 3;
}