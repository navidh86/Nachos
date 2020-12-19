package nachos.vm;

import java.util.Scanner;
import nachos.machine.*;
import nachos.threads.*;
import nachos.userprog.*;
import nachos.vm.*;

/**
 * A <tt>UserProcess</tt> that supports demand-paging.
 */
public class VMProcess extends UserProcess {
    /**
     * Allocate a new process.
     */
    public VMProcess() {
	super();
    }

    /**
     * Save the state of this process in preparation for a context switch.
     * Called by <tt>UThread.saveState()</tt>.
     */
    public void saveState() {
        VMKernel.mmu.saveStateForContextSwitch(super.getProcessID());
	super.saveState();
    }

    /**
     * Restore the state of this process after a context switch. Called by
     * <tt>UThread.restoreState()</tt>.
     */
    public void restoreState() {
        //System.out.println("vm restorestate");
	//super.restoreState();
    }

    /**
     * Initializes page tables for this process so that the executable can be
     * demand-paged.
     *
     * @return	<tt>true</tt> if successful.
     */
    protected boolean loadSections() {
        return true;
    }

    /**
     * Release any resources allocated by <tt>loadSections()</tt>.
     */
    protected void unloadSections() {
	super.unloadSections();
        VMKernel.loader.removeAllPages(super.getProcessID());
    }    
    
    private void handleTLBMiss(int vaddr) {
        int vpn = Processor.pageFromAddress(vaddr);
        int pid = super.getProcessID();
        VMKernel.mmu.loadPageIntoTLB(pid, vpn);
    }

    /**
     * Handle a user exception. Called by
     * <tt>UserKernel.exceptionHandler()</tt>. The
     * <i>cause</i> argument identifies which exception occurred; see the
     * <tt>Processor.exceptionZZZ</tt> constants.
     *
     * @param	cause	the user exception that occurred.
     */
    public void handleException(int cause) {
        boolean intStatus = Machine.interrupt().disable();
        
	Processor processor = Machine.processor();

	switch (cause) {
        case Processor.exceptionTLBMiss: 
            handleTLBMiss(processor.readRegister(Processor.regBadVAddr));
            break;
	default:
	    super.handleException(cause);
	    break;
	}
        
        Machine.interrupt().restore(intStatus);
    }
	
    private static final int pageSize = Processor.pageSize;
    private static final char dbgProcess = 'a';
    private static final char dbgVM = 'v';
}
