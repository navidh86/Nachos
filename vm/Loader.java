package nachos.vm;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import nachos.machine.Lib;
import nachos.machine.Machine;
import nachos.machine.Processor;
import nachos.machine.TranslationEntry;

public class Loader {
    public Loader() {
        numPhysPages = Machine.processor().getNumPhysPages();
        memory = Machine.processor().getMemory();
        swapMap = new Hashtable<>();
        isInUse = new boolean[numPhysPages];
    }
    
    // loads the needed page from swap file or disk and returns the physical page number, does not alter the page table
    public int loadPageIntoMemory(int pid, int vpn) {
        int ppn = getNewPhysicalPage();
        isInUse[ppn] = true;
        
        // check if the page is in swap
        Integer pos = swapMap.get(new Pair(pid, vpn));
        if (pos != null) {
            byte[] ret = VMKernel.swapArea.retrievePage(pos);
            // copy it in memory
            int physicalAddress = Processor.makeAddress(ppn, 0);
            System.arraycopy(ret, 0, memory, physicalAddress, Processor.pageSize);
        }
        else {
            // check if it is within coff file
            if (vpn < VMKernel.disk.getCoffLength(pid)) {
                // load it from disk
                VMKernel.disk.loadPage(pid, vpn, ppn);
            }
        }
        
        isInUse[ppn] = false;
        
        return ppn;
    }
    
    public int getNewPhysicalPage() {
        // find a suitable page first
        int ppn = -1;
        List<Integer> candidates = new ArrayList<>();
        for (int i=0; i<numPhysPages; i++) {
            if (VMKernel.table.isUnoccupied(i)) {
                return i;
            }
            if (!isInUse[i]) {
                candidates.add(i);
            }
        }
        
        ppn = candidates.get(Lib.random(candidates.size()));
        removePage(ppn);
        
        return ppn;
    }
    
    // removes a page from the main memory, and if needed stores it in swap
    public void removePage(int ppn) {
        int pid = VMKernel.table.getInvertedEntry(ppn).first;
        int vpn = VMKernel.table.getInvertedEntry(ppn).second;
        
        TranslationEntry entry = VMKernel.table.removeEntry(pid, vpn);
        
        if (entry != null && entry.dirty) {
            Integer pos = swapMap.get(new Pair(pid, vpn));
            // check if it already is in swap
            if (pos != null) {
                VMKernel.swapArea.writePageInPosition(ppn, pos);
            }
            else {
                // put it in some free space in swap
                int newPos = VMKernel.swapArea.writePage(ppn);
                swapMap.put(new Pair(pid, vpn), newPos);
            }
        }
    }
    
    public boolean isReadOnly(int pid, int vpn) {
        return VMKernel.disk.isReadOnly(pid, vpn);
    }
    
    public void setUsageStatus(int ppn, boolean status) {
        isInUse[ppn] = status;
    }
    
    public boolean isUsing(int ppn) {
        return isInUse[ppn];
    } 
    
    public void removeAllPages(int pid) {
        // remove from page table
        VMKernel.table.removeProcessEntries(pid);
        
        // remove from swap
        int len = VMKernel.disk.getCoffLength(pid) + 8 + 1;
        for (int i=0; i<len; i++) {
            Integer pos = swapMap.remove(new Pair(pid, i));
            if (pos != null) {
                VMKernel.swapArea.unallocatePage(pos);
            }
        }
    }
    
    private Hashtable<Pair, Integer> swapMap;
    private boolean[] isInUse; // keeps track of which physical pages are currently in use
    private byte[] memory;
    private int numPhysPages;
}
