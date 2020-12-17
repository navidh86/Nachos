package nachos.vm;

import java.util.Hashtable;
import nachos.machine.Machine;
import nachos.machine.TranslationEntry;
import nachos.threads.Lock;

public class InvertedPageTable {
    public InvertedPageTable() {
        this.capacity = Machine.processor().getNumPhysPages();
        this.lock = new Lock();
        this.pageTable = new Hashtable<>();
        this.invertedTable = new Pair[capacity];
    }
    
    public boolean isFull() {
        return pageTable.size() == capacity;
    }
    
    public int getSize() {
        return pageTable.size();
    }
    
    public TranslationEntry getEntry(int pid, int vpn) {
        try {
            lock.acquire();
            
            TranslationEntry entry =  pageTable.get(new Pair(pid, vpn));
            if (entry != null)
                return new TranslationEntry(entry);
            return null;
        } finally {
            lock.release();
        }
    }
    
    public void addEntry(int pid, int vpn, TranslationEntry entry) {
        try {
            //lock.acquire();
            
            pageTable.put(new Pair(pid, vpn), new TranslationEntry(entry));
            invertedTable[entry.ppn] = new Pair(pid, vpn);
        } finally {
            //lock.release();
        }
    }
    
    public TranslationEntry removeEntry(int pid, int vpn) {
        try {
            lock.acquire();
            
            TranslationEntry entry =  pageTable.remove(new Pair(pid, vpn));
            
            if (entry != null) {
                invertedTable[entry.ppn] = null;
                return new TranslationEntry(entry);
            }                
            return null;
        } finally {
           lock.release();
        }
    }
    
    public TranslationEntry removeEntry(int ppn) {
        if (invertedTable[ppn] != null)
            return removeEntry(invertedTable[ppn].first, invertedTable[ppn].second);
        
        return null;
    }
    
    // remove all entries of this process
    public void removeProcessEntries(int pid) {
        for (int i=0; i<capacity; i++) {
            if (invertedTable[i] != null) {
                if (invertedTable[i].first == pid) {
                    removeEntry(invertedTable[i].first, invertedTable[i].second);
                }
            }
        }
    }
    
    public boolean isUnoccupied(int ppn) {
        return invertedTable[ppn] == null;
    }
    
    public Pair getInvertedEntry(int ppn) {
        return invertedTable[ppn];
    }
    
    private int capacity;
    private Hashtable<Pair, TranslationEntry> pageTable;
    private Pair[] invertedTable;
    private Lock lock;
}
