/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nachos.proj1;

import nachos.threads.KThread;

/**
 *
 * @author navidh86
 */
public class JoinTest {
    public static void startTest() {
        System.out.println("*********** Starting join test **********\n");
        KThread kt1 = new KThread(new PingTest(1));
        System.out.println("Forking new thread and calling join on it");
        kt1.fork();
        kt1.join();
        System.out.println("New thread has completed and joined in main thread");
        System.out.println("\n*********** End of join test **********\n");
    }
}

class PingTest implements Runnable {
    PingTest(int which) {
        this.which = which;
    }

    public void run() {
        for (int i=0; i<5; i++) {
            System.out.println("*** thread " + which + " looped "
                               + i + " times");
            KThread.yield();
        }
    }

    private int which;
}