package nachos.vm;

import java.util.Hashtable;
import nachos.machine.Coff;

public class Disk {
    
    public Disk() {
        coffMap = new Hashtable<>();
    }
    
    public void loadPage(int pid, int vpn, int ppn) {
        Coff coff = coffMap.get(pid).coff;
        
        int count = 0;
        for (int i=0; i<coff.getNumSections(); i++) {
            if (coff.getSection(i).getLength() + count > vpn) {
                coff.getSection(i).loadPage(vpn-count, ppn);
                break;
            }
            else count += coff.getSection(i).getLength();
        }
    }
    
    public int getCoffLength(int pid) {
        return coffMap.get(pid).coffLength;
    }
    
    public void addCoff(int pid, Coff coff, int coffLength) {
        coffMap.put(pid, new CoffEntry(coff, coffLength));
    }
    
    
    private class CoffEntry {
        CoffEntry(Coff coff, int coffLength) {
            this.coff = coff;
            this.coffLength = coffLength;
        }
        
        Coff coff;
        int coffLength;
    }
    
    private Hashtable<Integer, CoffEntry> coffMap;
}
