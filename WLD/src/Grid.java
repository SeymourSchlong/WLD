
import java.awt.Color;
import java.awt.Graphics;


public class Grid {
    int x;
    int y;
    int width;
    int height;
    boolean occupado = false;
    
    public Grid(int xx, int yy) {
        x = xx;
        y = yy;
        width = 100;
        height = 100;
    }
    
    public void draw(Graphics g, int n) {
        g.setColor(Color.black);
        g.drawRect(x, y, width, height);
        g.drawString(String.valueOf(n), x + width/2, y + height/2);
    }
    
    public boolean used(Block b, char dir) {
        int area = b.x*b.y;
        if(true) {
            occupado = true;
            return(occupado);
        }
        occupado = false;
        return(occupado);
    }
}
