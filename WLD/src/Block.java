/**
 * A sliding block class
 * 
 * @since 21.12.2018
 * @author Taylor
 */
public class Block {
    public boolean chunk = false;
    public int w, h;
    
    public Block(int type) {
        switch(type) {
            // Small block
            case 0:
                w = 1;
                h = 1;
                break;
            // Tall block
            case 1:
                w = 1;
                h = 2;
                break;
            // Long block
            case 2:
                w = 2;
                h = 1;
                break;
            // Big block
            case 3:
                w = 2;
                h = 2;
                chunk = true;
                break;
        }
    }
    
    public void slide() {
        
    }
    
    public boolean isKing() {
        boolean finna = true;
        
        if (chunk == true) return finna;
        
        return false;
    }
}
