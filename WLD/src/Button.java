
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * Buttons that you can click. To be used in menus.
 * 
 * @since 21.11.2018
 * @author Taylor
 */
public class Button {
    public String text;
    public int x, y, w, h;
    public boolean hover;
    
    public Button(int xx, int yy, int ww, int hh, String txt) {
        x = xx;
        y = yy;
        w = ww;
        h = hh;
        text = txt;
    }
    
    public void draw(Graphics g, Font f) {
        if (!hover) g.setColor(new Color(0xc19a6b));
        else g.setColor(new Color(0x7e5d35));
        
        g.fill3DRect(x, y, w, h, true);
        
        g.setColor(Color.black);
        g.setFont(f);
        g.drawString(text, x + 5, y + h - 5);
    }
}
