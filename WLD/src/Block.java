
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.ImageObserver;
import java.io.File;

/**
 * A sliding block class
 * 
 * @since 21.12.2018
 * @author Taylor
 */
public class Block {
    public boolean chunk = false;
    public int xPrev, yPrev;
    public int id = -1;
    public String imgLoc;
    public int type;
    public int x, y, w, h, xStart, yStart;
    public boolean imgVisible = false;
    public Image img, blank;
    public char direction;
    
    public Block(int xx, int yy, int blockType, String loc, int num) {
        id = num;
        
        x = xx;
        y = yy;
        xStart = xx;
        yStart = yy;
        
        type = blockType;
        
        imgLoc = loc;
        
        setType(type);
        setPrev();
        loadImage();
    }
    
    public void loadImage() {
        if (new File("./img/" + imgLoc).exists()) {
            img = new Image(x, y, imgLoc);
            img.setSize(w, h);
            imgVisible = true;
        }
    }
    
    public void setPos(int xx, int yy) {
        x = xx + 1;
        y = yy + 1;
        setPrev();
    }
    
    public void setType(int blockType) {
        type = blockType;
        
        switch(type) {
            // Big block
            case 1:
                w = 200;
                h = 200;
                chunk = true;
                break;
            // Tall block
            case 2:
                w = 100;
                h = 200;
                break;
            // Long block
            case 3:
                w = 200;
                h = 100;
                break;
            // Small block
            case 4:
                w = 100;
                h = 100;
                break;
        }
        
        blank = new Image(x, y, "block_" + type + ".png");
        
        h -= 2;
        w -= 2;
    }
    
    public void draw(Graphics g, ImageObserver i) {
        if (imgVisible) img.draw(g, x, y, i);
        else blank.draw(g, x, y, i);
    }
    
    public void resetPos() {
        x = xStart;
        y = yStart;
        
        setPrev();
    }
    
    public void setPrev() {
        xPrev = x;
        yPrev = y;
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
