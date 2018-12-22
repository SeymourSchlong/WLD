
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * Makes images easier to make and takes up less space in the main file.
 * 
 * @since 22.11.2018
 * @author tayloreyben0315
 */
public class Image {
    private int x, y, w, h;
    private BufferedImage img;
    
    public Image(int xx, int yy, String str) {
        try {
            File imgLoc = new File("./img/" + str);
            img = ImageIO.read(imgLoc);
        } catch(Exception e) {
            System.out.println(e);
        }
        
        x = xx;
        y = yy;
        w = img.getWidth();
        h = img.getHeight();
    }
    
    public void draw(Graphics g, ImageObserver i) {
        g.drawImage(img, x, y, w, h, i);
    }
    
    public void draw(Graphics g, int xx, int yy, ImageObserver i) {
        g.drawImage(img, xx, yy, w, h, i);
    }
    
    // Setters
    public void setPos(int xx, int yy) {
        x = xx;
        y = yy;
    }
    
    public void setSize(int ww, int hh) {
        w = ww;
        h = hh;
    }
    
    // Getters
    public Point getPos() {
        return new Point(x, y);
    }
    
    public int getWidth() {
        return w;
    }
    
    public int getHeight() {
        return w;
    }
    
    public BufferedImage getImage() {
        return img;
    }
}
