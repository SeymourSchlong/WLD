
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.ImageObserver;

/**
 * A sliding block class
 * 
 * @since 21.12.2018
 * @author Taylor
 */
public class Block {
    public boolean chunk = false;
    public int x, y, w, h;
    public Color color;
    public Image img;
    
    public Block(int xx, int yy, int type) {
        x = xx;
        y = yy;
        
        switch(type) {
            // Big block
            case 1:
                w = 200;
                h = 200;
                chunk = true;
                color = Color.RED;
                break;
            // Tall block
            case 2:
                w = 100;
                h = 200;
                color = Color.BLUE;
                break;
            // Long block
            case 3:
                w = 200;
                h = 100;
                color = Color.GREEN;
                break;
            // Small block
            case 4:
                w = 100;
                h = 100;
                color = Color.ORANGE;
                break;
        }
    }
    
    public void draw(Graphics g, ImageObserver i) {
        //g.setColor(color);
        g.setColor(new Color(0xc19a6b));
        g.fill3DRect(x, y, w, h, true);
        
        //img.draw(g, x, y, i);
    }
    
    public void slide() {
        // Stops the block from going out of bounds
        if (x < 50) x = 50;
        if (x + w > 450) x = 450 - w;
        if (y < 50) y = 50;
        if (y + h > 550) y = 550 - h;
    }
    
    public boolean isKing() {
        boolean finna = true;
        
        if (chunk == true) return finna;
        
        return false;
    }
}
