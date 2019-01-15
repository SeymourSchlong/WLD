
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
    public int type;
    public int x, y, w, h;
    public Color color;
    public Image img;
    public int[] gridSlots;
    public char direction;
    public Grid grid[];
    
    public Block(int xx, int yy, int blockType, Grid g[]) {
        x = xx;
        y = yy;
        
        grid = g;
        
        type = blockType;
        
        switch(type) {
            // Big block
            case 1:
                w = 200;
                h = 200;
                chunk = true;
                color = Color.RED;
                gridSlots = new int[4];
                break;
            // Tall block
            case 2:
                w = 100;
                h = 200;
                color = Color.BLUE;
                gridSlots = new int[2];
                break;
            // Long block
            case 3:
                w = 200;
                h = 100;
                color = Color.GREEN;
                gridSlots = new int[2];
                break;
            // Small block
            case 4:
                w = 100;
                h = 100;
                color = Color.ORANGE;
                gridSlots = new int[1];
                break;
        }
        
        h -= 2;
        w -= 2;
        
        /*if(collision2() == true) {
            for(int i = 0; i < blocks.length; i++) {
                if()
            }
        }*/
    }
    
    public boolean collision2() {
        for(int i = 0; i < grid.length; i++) {
            if (grid[i].used(this, direction)) {
                return(true);
            }

        } return(false);
    }
    
    public void draw(Graphics g, ImageObserver i) {
        //g.setColor(color);
        g.setColor(new Color(0xc19a6b));
        g.fill3DRect(x, y, w, h, true);
        
        //img.draw(g, x, y, i);
    }
    
    public void slide(char dir, Point m1, Point m2) {
        direction = dir;
        int moveDistance;
        if (dir == 'x') {
            moveDistance = m1.x - m2.x;
            
            x += moveDistance;
        } else {
            moveDistance = m1.y - m2.y;
            
            y += moveDistance;
        }
        
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
