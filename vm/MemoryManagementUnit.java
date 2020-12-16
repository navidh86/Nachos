package nachos.vm;

import nachos.machine.Lib;
import nachos.machine.Processor;
import nachos.machine.Machine;
import nachos.machine.TranslationEntry;

public class MemoryManagementUnit {
    public static TranslationEntry loadPageIntoTLB(int pid, int vpn) {
        Processor processor = Machine.processor();
        int tlbIndex = findFreeTLBSlot(pid, processor);
        TranslationEntry entry = VMKernel.table.getEntry(pid, vpn);
        
        if (entry == null) {
            ///load the page into main memory
            int ppn = VMKernel.loader.loadPageIntoMemory(pid, vpn);
            entry = new TranslationEntry(vpn, ppn, true, VMKernel.loader.isReadOnly(pid, vpn), false, false);
            ///then save put the translation entry into inverted page table
            VMKernel.table.addEntry(pid, vpn, entry);
        }
        
        Machine.processor().writeTLBEntry(tlbIndex, entry);
        
        return entry;
    }
    public static TranslationEntry getPage(int pid, int vpn) {
        Processor processor = Machine.processor();
        for (int i = 0; i < processor.getTLBSize(); i++) {
            TranslationEntry entry = processor.readTLBEntry(i);
            if (entry != null) {
                if (entry.valid && entry.vpn == vpn) {
                    return entry;
                }
            }
        }
        return loadPageIntoTLB(pid, vpn);
    }
    
    public static int findFreeTLBSlot(int pid, Processor processor) {
        for (int number = 0; number < processor.getTLBSize(); number++) {
            TranslationEntry entry = processor.readTLBEntry(number);
            if (entry == null || !entry.valid) {
                return number;
            }
        }
        //Free some slot
        int number = Lib.random(processor.getTLBSize());
        saveEntry(pid, number);
        return number;
    }
    
    public static void saveStateForContextSwitch(int pid) {
        Processor processor = Machine.processor();
        for (int i = 0; i < processor.getTLBSize(); i++) {
            saveEntry(pid, i);
        }
    }
    
    private static void saveEntry(int pid, int i) {
        Processor processor = Machine.processor();
        TranslationEntry entry = processor.readTLBEntry(i);
        if (entry == null) return;
        VMKernel.table.addEntry(pid, entry.vpn, entry);
        entry.valid = false;
        processor.writeTLBEntry(i, entry);
    }
}
