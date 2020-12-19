package nachos.vm;

import java.util.Hashtable;
import nachos.machine.*;
import nachos.threads.*;
import nachos.userprog.*;
import nachos.vm.*;

/**
 * A kernel that can support multiple demand-paging user processes.
 */
public class VMKernel extends UserKernel {
    /**
     * Allocate a new VM kernel.
     */
    public VMKernel() {
	super();
    }

    /**
     * Initialize this kernel.
     */
    public void initialize(String[] args) {
	super.initialize(args);
        VMKernel.disk = new Disk();
        VMKernel.swapArea = new SwapArea();
        VMKernel.table = new InvertedPageTable();
        VMKernel.loader = new Loader();
        VMKernel.mmu = new MemoryManagementUnit();
    }

    /**
     * Test this kernel.
     */	
    public void selfTest() {   
	super.selfTest();
    }

    /**
     * Start running user programs.
     */
    public void run() {
	super.run();
    }
    
    /**
     * Terminate this kernel. Never returns.
     */
    public void terminate() {
        VMKernel.swapArea.close();
        System.out.println("\n\n***** Page faults: " + VMKernel.pageFaults + " *****\n\n");
	super.terminate();
    }
    
    public static Disk disk;
    public static SwapArea swapArea;
    public static InvertedPageTable table;
    public static Loader loader;
    public static MemoryManagementUnit mmu;
    public static int pageFaults = 0;

    // dummy variables to make javac smarter
    private static VMProcess dummy1 = null;

    private static final char dbgVM = 'v';
}
