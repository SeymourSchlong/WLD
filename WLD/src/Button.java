
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * Buttons that you can click. To be used in menus.
 * 
 * @since 21.11.2018
 * @author Taylor
 */
public class Button {
    public BufferedImage img, imgHover;
    public int x, y, w, h;
    public boolean hover;
    
    public Button(int xx, int yy, int ww, int hh) {
        x = xx;
        y = yy;
        w = ww;
        h = hh;
    }
    
    public void draw(Graphics g, Font f) {
        if (!hover) g.setColor(new Color(0xc19a6b));
        else g.setColor(new Color(0x7e5d35));
        g.fill3DRect(x, y, w, h, true);
        
        g.setColor(Color.black);
        g.setFont(f);
        g.drawString("RESET", x + 5, y + h/2 + 5);
    }
    
    public void draw(Graphics g) {
        if (!hover) g.setColor(new Color(0xc19a6b));
        else g.setColor(new Color(0x7e5d35));
        g.fill3DRect(x, y, w, h, true);
    }
}
