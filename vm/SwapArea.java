package nachos.vm;

import java.util.ArrayList;
import java.util.List;
import nachos.machine.Machine;
import nachos.machine.OpenFile;
import nachos.machine.Processor;
import nachos.threads.ThreadedKernel;

public class SwapArea {
    public SwapArea() {
        swapFile = ThreadedKernel.fileSystem.open("swap", true);
        freePositions = new ArrayList<>();
        count = 0;
    }
    
    // writes the ppn'th page from main memory to the swapFile, returns the position where it was written
    public int writePage(int ppn) {
        int pos = getFreePos();
        return writePageInPosition(ppn, pos);
    }
    
    // writes the ppn'th page from main memory to the swapFile in the specified position
    public int writePageInPosition(int ppn, int pos) {
        int physicalAddress = Processor.makeAddress(ppn, 0);

        int length = swapFile.write(pos*Processor.pageSize, Machine.processor().getMemory(), physicalAddress, 
                                Processor.pageSize);
        
        if (length < Processor.pageSize)
            return -1;
        
        return pos;
    }
    
    // retrieve the pos'th page from the swap area
    public byte[] retrievePage(int pos) {
        byte[] page = new byte[Processor.pageSize];
        
        int length = swapFile.read(pos*Processor.pageSize, page, 0, Processor.pageSize);
        
        return page;
    }
    
    public void unallocatePage(int pos) {
        freePositions.add(pos);
    }

    private int getFreePos() {
        if (freePositions.isEmpty()) {
            count++;
            return count - 1;
        }
        else {
            return freePositions.remove(0);
        }
    }
    
    public void close() {
        swapFile.close();
    }
    
    private int count; // # of pages allocated on the swapFile
    private List<Integer> freePositions;
    private OpenFile swapFile;
}
