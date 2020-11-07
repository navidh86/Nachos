/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nachos.proj1;

import nachos.threads.Communicator;
import nachos.threads.KThread;

/**
 *
 * @author navidh86
 */
public class CommunicatorTest {
    public static void startTest() {
        Communicator c = new Communicator();
        KThread sp1 = new KThread(new Speaker(c, 1));
        KThread sp2 = new KThread(new Speaker(c, 2));
        //KThread sp3 = new KThread(new Speaker(c, 3));
        
        KThread ls1 = new KThread(new Listener(c, 1));
        KThread ls2 = new KThread(new Listener(c, 2));
        
        sp1.fork();
        sp2.fork();
        //sp3.fork();
        ls1.fork();
        ls2.fork();
        
        sp1.join();
        sp2.join();
        //sp3.join();
        ls1.join();
        ls2.join();
    }
}

class Speaker implements Runnable {
    Speaker(Communicator c, int id) {
        this.c = c;
        this.id = id;
    }
    
    void say(int word) {
        //System.out.println("Spekaer " + id + " trying to say --> " + word);
        this.c.speak(word);
    }
    
    public void run() {
        for (int i=0; i<5; i++) {
            say(i);
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
    
    void listen() {
        //System.out.println("Listener " + id + " trying to listen");
        int ret = this.c.listen();
        //System.out.println("Listener " + id + " heard : " + ret);
    }
    
    public void run() {
        while (count < 10) {
            listen();
            count++;
        }
    }
    
    private Communicator c;
    private int id;
    private static int count = 0;
}