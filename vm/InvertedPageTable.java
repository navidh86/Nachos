package nachos.vm;

import java.util.Hashtable;
import nachos.machine.TranslationEntry;
import nachos.threads.Lock;

public class InvertedPageTable {
    public InvertedPageTable() {
        this.lock = new Lock();
        this.pageTable = new Hashtable<>();
    }
    
    public TranslationEntry getEntry(int pid, int vpn) {
        try {
            lock.acquire();
            
            return pageTable.get(new Pair(pid, vpn));
        } finally {
            lock.release();
        }
    }
    
    public void addEntry(int pid, int vpn, TranslationEntry entry) {
        try {
            lock.acquire();
            
            pageTable.put(new Pair(pid, vpn), entry);
        } finally {
            lock.release();
        }
    }
    
    public TranslationEntry removeEntry(int pid, int vpn) {
        try {
            lock.acquire();
            
            return pageTable.remove(new Pair(pid, vpn));
        } finally {
            lock.release();
        }
    }
    
    private Hashtable<Pair, TranslationEntry> pageTable;
    private Lock lock;
}
