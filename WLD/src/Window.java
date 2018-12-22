
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.ImageIcon;

/**
 * A remake of the puzzle game Klotski
 * 
 * @since 21.12.2018
 * @author tayloreyben0315, bradymills0605
 */
public class Window extends javax.swing.JFrame implements KeyListener, MouseListener, MouseMotionListener {

    Dimension window = new Dimension(800, 600), screen = Toolkit.getDefaultToolkit().getScreenSize();
    int moves = 0, ms = 0;
    
    boolean hovering;
    
    Block blocks[];
    
    int CURSOR_DEFAULT = 0, CURSOR_HOVER = 1, CURSOR_GRAB = 2;
    ImageIcon cursor, hover, grab;
    Point mouse;
    Graphics big;
    BufferedImage bi;
    Timer timer;
    Frame frame;
    
    public Window() {
        initComponents();
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        
        
        cursor = new ImageIcon("./img/cursor.png");
        hover = new ImageIcon("./img/hover.png");
        grab = new ImageIcon("./img/grab.png");
        
        setTitle("WLD"); // Sets the window title
        setSize(window); // Sets the window size
        setLocation((screen.width/2) - (window.width/2), (screen.height/2) - (window.height/2) - 100); // Puts the window in the center of the screen
        setIconImage(Toolkit.getDefaultToolkit().getImage("./img/icon.gif")); // Sets the toolbar and window icon
        setMouseCursor(CURSOR_DEFAULT); // Changes the mouse cursor
        
        bi = (BufferedImage) createImage(window.width, window.height);
        big = bi.createGraphics();
        timer = new Timer();
        
        // Creating the blocks
        blocks = new Block[10];
        for (int i = 0; i < blocks.length; i++) {
            int type = 0, xPos = 0, yPos = 0;
            
            switch(i) {
                // Big
                case 0:
                    xPos = 150;
                    yPos = 50;
                    break;
                // Tall
                case 1:
                    xPos = 50;
                    yPos = 50;
                    break;
                case 2:
                    xPos = 350;
                    yPos = 50;
                    break;
                case 3:
                    xPos = 50;
                    yPos = 250;
                    break;
                case 4:
                    xPos = 350;
                    yPos = 250;
                    break;
                // Long
                case 5:
                    xPos = 150;
                    yPos = 250;
                    break;
                // Small
                case 6:
                    xPos = 150;
                    yPos = 350;
                    break;
                case 7:
                    xPos = 250;
                    yPos = 350;
                    break;
                case 8:
                    xPos = 50;
                    yPos = 450;
                    break;
                case 9:
                    xPos = 350;
                    yPos = 450;
                    break;
            }
            
            if (i == 0) type = 1;
            if (i > 0 && i < 5) type = 2;
            if (i == 5) type = 3;
            if (i > 5 && i < 10) type = 4;
            
            blocks[i] = new Block(xPos, yPos, type);
        }
        
        timer.start();
    }
    
    public void setMouseCursor(int a) {
        ImageIcon i;
        
        switch (a) {
            case 0:
                i = cursor;
                break;
            case 1:
                i = hover;
                break;
            case 2:
                i = grab;
                break;
            default:
                System.out.println("Invalid cursor type.");
                return;
        }
        
        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(i.getImage(), new Point(0,0), "cursor"));
    }
    
    public void keyTyped(KeyEvent ke) {
        int key = ke.getKeyCode();
        
        switch(key) {
            
        }
    }
    
    public void mouseMoved(MouseEvent m) {
        mouse = m.getPoint();
        
        if (collision(mouse, blocks)) {
            hovering = true;
            setMouseCursor(CURSOR_HOVER);
        } else {
            hovering = false;
            setMouseCursor(CURSOR_DEFAULT);
        }
    }
    
    public void mouseClicked(MouseEvent m) {
        
    }
    
    public void mouseDragged(MouseEvent m) {
        
    }
    
    public void mousePressed(MouseEvent m) {
        setMouseCursor(CURSOR_GRAB);
    }

    public void mouseReleased(MouseEvent m) {
        if (hovering) setMouseCursor(CURSOR_HOVER);
        else setMouseCursor(CURSOR_DEFAULT);
    }
    
    //<editor-fold defaultstate="collapsed" desc=" Unused Listeners ">
    
    public void keyPressed(KeyEvent ke) { }

    public void keyReleased(KeyEvent ke) { }

    public void mouseEntered(MouseEvent m) { }

    public void mouseExited(MouseEvent m) { }
    
    //</editor-fold>
    
    public boolean collision(Point p, Block[] bs) {
        for (Block b : bs) {
            if (p.x > b.x - 1) {
                if (p.x < b.x + b.w) {
                    if (p.y > b.y - 1) {
                        if (p.y < b.y + b.h) {
                            return true;
                        }
                    }
                }
            }
        }
        
        return false;
    }

    public void paint(Graphics g) {
        // Clear the window
        big.clearRect(0, 0, window.width, window.height);
        
        big.setColor(Color.BLACK);
        big.fillRect(40, 40, 420, 520);
        
        for (Block b : blocks) {
            b.draw(big, this);
        }
        
        // Draw the new image
        g.drawImage(bi, 0, 0, this);
    }
    
    public class Timer extends Thread {
        public void run() {
            while(true) {
                
                
                try {
                    Thread.sleep(0);
                } catch(Exception e) {
                    System.out.println(e);
                }
                
                repaint();
            }
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc=" Default Generated Code ">
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Window().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    //</editor-fold>
}
