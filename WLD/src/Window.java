
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 * A remake of the puzzle game Klotski
 * 
 * @since 21.12.2018
 * @author Brady, Taylor, Jakob
 */
public class Window extends javax.swing.JFrame implements MouseListener, MouseMotionListener {

    Dimension window = new Dimension(800, 600), screen = Toolkit.getDefaultToolkit().getScreenSize();
    int moves = 0, mls = 0, secs = 0, mins = 0, hours = 0, clickedBlock = -1, xDistance, yDistance, dragLength;
    int[] xPositions = { 50, 150, 250, 350, 450 },
            yPositions = { 50, 150, 250, 350, 450, 550 };
    long openTime = System.currentTimeMillis();
    String time = "";
    char dir;
    
    ArrayList<Point> dragPos = new ArrayList();
    
    boolean hovering, clicking;
    
    Block blocks[];
    Grid grid[];
    
    int CURSOR_DEFAULT = 0, CURSOR_HOVER = 1, CURSOR_GRAB = 2;
    ImageIcon cursor, hover, grab;
    Font customFont, font;
    Point mouse, lastClick, frame;
    Graphics big;
    BufferedImage bi;
    Timer timer;
    
    public Window() {
        initComponents();
        addMouseListener(this);
        addMouseMotionListener(this);
        
        cursor = new ImageIcon("./img/cursor.png");
        hover = new ImageIcon("./img/hover.png");
        grab = new ImageIcon("./img/grab.png");
        
        setTitle("WLD"); // Sets the window title
        setSize(window); // Sets the window size
        setResizable(false); // Makes it so you can't change the window size
        setLocation((screen.width/2) - (window.width/2), (screen.height/2) - (window.height/2) - 100); // Puts the window in the center of the screen
        setIconImage(Toolkit.getDefaultToolkit().getImage("./img/icon.gif")); // Sets the toolbar and window icon
        setMouseCursor(CURSOR_DEFAULT); // Changes the mouse cursor
        
        bi = (BufferedImage) createImage(window.width, window.height);
        big = bi.createGraphics();
        timer = new Timer();
        mouse = new Point();
        
        // Creating the blocks
        blocks = new Block[10];
        grid = new Grid[20]; 
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 4; j++) {
                grid[i*4 + j] = new Grid(((j*100)+50), ((i*100)+50));
                System.out.println((i*4)+j);
            }
        }
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
            
            // Assigning the blocks their types
            if (i == 0) type = 1;
            if (i > 0 && i < 5) type = 2;
            if (i == 5) type = 3;
            if (i > 5 && i < 10) type = 4;
            
            blocks[i] = new Block(xPos + 1, yPos + 1, type);
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
        
        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(i.getImage(), new Point(0, 0), "cursor"));
    }
    
    public void mouseMoved(MouseEvent m) {
        mouse = m.getPoint();
        if (isHovering(mouse, blocks)) {
            hovering = true;
            setMouseCursor(CURSOR_HOVER);
        } else {
            hovering = false;
            setMouseCursor(CURSOR_DEFAULT);
        }
    }
    
    public void snapBlock(Block b) {
            for (int i = 0; i < xPositions.length - 1; i++) {
                // Check if the "center" of the block is in between
                if (xPositions[i] <= blocks[clickedBlock].x + 50 && blocks[clickedBlock].x + 50 <= xPositions[i + 1]) {
                    blocks[clickedBlock].x = xPositions[i] + 1;
                    break;
                }
            }
            for (int i = 0; i < yPositions.length - 1; i++) {
                if (yPositions[i] <= blocks[clickedBlock].y + 50 && blocks[clickedBlock].y + 50 <= yPositions[i + 1]) {
                    blocks[clickedBlock].y = yPositions[i] + 1;
                    break;
                }
            }
            moves++;
            
            clickedBlock = -1;
    }
    
    public void mouseDragged(MouseEvent m) {
        mouse = m.getPoint();
        dragLength++;
        
        if (clickedBlock != -1) {
            if (dragLength == 1) {
                xDistance = mouse.x - lastClick.x;
                yDistance = mouse.y - lastClick.y;
                
//                if (xDistance != 0) {
//                    if (yDistance != 0) {
//                        double slope = yDistance/xDistance;
//                        
//                        if (slope > 1) dir = 'y';
//                        else dir = 'x';
//                    } else dir = 'y';
//                } else dir = 'x';
                
                if (Math.abs(xDistance) == Math.abs(yDistance)) {
                    dragLength--;
                } else if (Math.abs(xDistance) > Math.abs(yDistance)) {
                    dir = 'x';
                } else if (Math.abs(xDistance) < Math.abs(yDistance)) {
                    // Set the moving direction to up/down
                    dir = 'y';
                }
                
                System.out.println(dir);
                
                dragPos.add(dragLength, mouse);
            } else {
                dragPos.add(dragLength, mouse);
                blocks[clickedBlock].slide(dir, mouse, dragPos.get(dragLength-1));
                
                /*
                for (int i = 0; i < grid.length; i++) {
                    for (int num : blocks[clickedBlock].gridSlots) if (num == i) return;
                    
                    
                    
                    if (true) {
                        
                    }
                }//*/
                
                for (int i = 0; i < blocks.length; i++) {
                    Block b = blocks[i];
                    
                    if (b != blocks[clickedBlock]) {
                        int side = collision(blocks[clickedBlock], b, dir);
                        
                        if (side != 0) {
                            System.out.println("Clicked block #" + clickedBlock + " has collided with block #" + i + " on side " + side);
                            
                            snapBlock(blocks[clickedBlock]);
                            return;
                            /*
                            if (dir == 'x') {
                                // Left side
                                if (side == 1) {
                                    blocks[clickedBlock].x = b.x - blocks[clickedBlock].w - 2;
                                }
                                // Right side
                                else if (side == 2) {
                                    blocks[clickedBlock].x = b.x + blocks[clickedBlock].w - 2;
                                }
                            } else {
                                // Top side
                                if (side == 1) {
                                    blocks[clickedBlock].y = b.y - blocks[clickedBlock].h - 2;
                                }
                                // Bottom side
                                else if (side == 2) {
                                    blocks[clickedBlock].y = b.y + blocks[clickedBlock].h - 2;
                                }
                            }//*/
                        }
                    }
                }//*/
            }
        }
        
        if (isHovering(mouse, blocks)) hovering = true;
        else hovering = false;
    }
    
    public void mousePressed(MouseEvent m) {
        lastClick = m.getPoint(); // Sets the position of the click.
        clicking = true;
        dragLength = 0;
        dragPos.add(0, lastClick);
        
        for (int i = 0; i < blocks.length; i++) {
            if (collision(mouse, blocks[i])) {
                clickedBlock = i;
            }
        }
        
        setMouseCursor(CURSOR_GRAB);
    }

    public void mouseReleased(MouseEvent m) {
        clicking = false;
        
        // Snap to nearest location
        if (clickedBlock != -1) {
            snapBlock(blocks[clickedBlock]);
        }
        
        if (hovering) setMouseCursor(CURSOR_HOVER);
        else setMouseCursor(CURSOR_DEFAULT);
        
        dragPos.clear();
    }
    
    //<editor-fold defaultstate="collapsed" desc=" Unused Listeners ">
    
    public void mouseClicked(MouseEvent m) { }

    public void mouseEntered(MouseEvent m) { }

    public void mouseExited(MouseEvent m) { }
    
    //</editor-fold>
    
    // Checks if the mouse is hovering over ANY block.
    public boolean isHovering(Point p, Block[] bs) {
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
    
    public boolean collision(Point p, Block b) {
        if (p.x > b.x) {
            if (p.x < b.x + b.w) {
                if (p.y > b.y) {
                    if (p.y < b.y + b.h) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    public int collision(Block b1, Block b2, char dir) {
        
        /*// Hits top/bottom
        if (dir == 'x') {
            // This should be true if it's on the same row.
            if (b1.y <= b2.y) {
                System.out.println("Same row as another block...");
                return 1;
            }
        }
        // Hits left/right
        else if (dir == 'y') {
            
        }//*/
        
        // Old collision code... Maybe it could work?
        // Left/Right
        if (dir == 'x') {
            if (b1.y <= b2.y) {
                if (b1.y <= b2.y + b2.h) {
                    // Left
                    if (b1.x + b1.w >= b2.x && b1.x + b1.w <= b2.x + b2.w) {
                        return 1;
                    }
                    // Right
                    if (b1.x <= b2.x + b2.w && b1.x > b2.x) {
                        return 2;
                    }
                }
            }
        }
        // Top/Bottom
        else if (dir == 'y') {
            if (b1.x <= b2.x) {
                if (b1.x <= b2.x + b2.w) {
                    // Top
                    if (b1.y + b1.h >= b2.y && b1.y + b1.h <= b2.y + b2.h) {
                        return 1;
                    }
                    // Bottom
                    if (b1.y <= b2.y + b2.h && b1.y > b2.y) {
                        return 2;
                    }
                }
            }
        }//*/
        
        return 0;
    }
    
    // Updates the time on the timer
    public void updateTime() {
        // Find the difference in milliseconds since when it was opened and the current time.
        mls = (int) (System.currentTimeMillis() - openTime);
        int totalSecs, totalMins, totalHours;
        
        // Find the amount of...
        totalSecs = mls/1000;           // seconds the program has been open
        totalMins = totalSecs / 60;     // minutes the program has been open
        totalHours = totalMins / 60;    // hours the program has been open
                                        
        // Convert into HH:MM:SS
        secs = totalSecs % 60;          // How many seconds
        mins = totalMins % 60;          // How many minutes
        hours = totalHours % 60;        // How many hours
        
        time = hours + ":" + (mins < 10 ? "0" + mins : mins) + ":" + (secs < 10 ? "0" + secs : secs);
    }

    public void paint(Graphics g) {
        // Clear the window
        big.clearRect(0, 0, window.width, window.height);
        
        font = new Font("Comic Sans MS", Font.PLAIN, 18);
        big.setFont(font);
        
        // Background for the bricks
        big.setColor(new Color(0x663300));
        big.fill3DRect(40, 40, 420, 520, true);
        
        big.setColor(Color.BLACK);
        big.drawString("Time Elapsed", 600, 500);
        big.drawString(time, 600, 550);
        big.drawString("Total Moves", 600, 200);
        big.drawString(String.valueOf(moves), 600, 250);
        
        // Draw the bricks
        for (int i = 0; i < blocks.length; i++) {
            Block b = blocks[i];
            blocks[i].draw(big, this);
            big.setColor(Color.BLACK);
            big.drawString(String.valueOf(i), b.x + b.w/2 - 3, b.y + b.h/2 + 6);
        }
        /*for(int i = 0; i < 20; i++) {
            grid[i].draw(big, i);
        }//*/
        // Draw the new image
        g.drawImage(bi, 0, 0, this);
    }
    
    public class Timer extends Thread {
        public void run() {
            while(true) {
                updateTime();
                
                try {
                    Thread.sleep(1);
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
