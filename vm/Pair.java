package nachos.vm;

import java.util.Objects;

public class Pair {
    public Pair(Integer first, Integer second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final Pair other = (Pair) obj;
        
        return Objects.equals(this.first, other.first) && Objects.equals(this.second, other.second);
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.first);
        hash = 97 * hash + Objects.hashCode(this.second);
        
        return hash;
    }
    
    public final Integer first, second;
}
