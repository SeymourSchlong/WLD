
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
    int moves = 0, clickedBlock = -1, xDistance, yDistance, dragLength;
    int totalSecs, totalMins, totalHours, mls = 0, secs = 0, mins = 0, hours = 0;
    int[] xPositions = { 50, 150, 250, 350, 450 },
            yPositions = { 50, 150, 250, 350, 450, 550 };
    int[] blockMoves;
    long openTime = System.currentTimeMillis();
    String time = "";
    char dir;
    int state = 0;
    /**
     * 0 : Playing
     * 1 : Victory screen
     */
    
    ArrayList<Point> dragPos = new ArrayList();
    
    boolean hovering, clicking, imagesVisible = true;
    
    Block blocks[];
    
    int CURSOR_DEFAULT = 0, CURSOR_HOVER = 1, CURSOR_GRAB = 2;
    Image background;
    ImageIcon cursor, hover, grab;
    Font font, header;
    Point mouse, lastClick, frame;
    Button reset, gotem, save, load, image, newGame;
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
        
        reset = new Button(600, 75, 80, 25, "RESET");
        gotem = new Button(window.width - 6, window.height - 6, 5, 5, "");
        save = new Button(600, 110, 80, 25, "SAVE");
        load = new Button(600, 145, 80, 25, "LOAD");
        image = new Button(600, 180, 80, 25, "IMAGE");
        newGame = new Button(195, 450, 110, 25, "NEW GAME");
        
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
        
        background = new Image(40, 40, "background.png");
        
        blockMoves = new int[4];
        
        // Creating the blocks
        blocks = new Block[10];
        for (int i = 0; i < blocks.length; i++) {
            int type = 0, xPos = 0, yPos = 0;
            String imgLoc = "";
            
            switch(i) {
                // Big
                case 0:
                    xPos = 150;
                    yPos = 50;
                    imgLoc = "the one true king";
                    break;
                // Tall
                case 1:
                    xPos = 50;
                    yPos = 50;
                    imgLoc = "MY LEG!!";
                    break;
                case 2:
                    xPos = 350;
                    yPos = 50;
                    imgLoc = "gerron";
                    break;
                case 3:
                    xPos = 50;
                    yPos = 250;
                    imgLoc = "succ and cuck master";
                    break;
                case 4:
                    xPos = 350;
                    yPos = 250;
                    imgLoc = "karate joe for smash";
                    break;
                // Long
                case 5:
                    xPos = 150;
                    yPos = 250;
                    imgLoc = "caring and supportive funky kong gives you a ride home from the airport";
                    break;
                // Small
                case 6:
                    xPos = 150;
                    yPos = 350;
                    imgLoc = "sal";
                    break;
                case 7:
                    xPos = 250;
                    yPos = 350;
                    imgLoc = "wtf";
                    break;
                case 8:
                    xPos = 50;
                    yPos = 450;
                    imgLoc = "you may touch it once";
                    break;
                case 9:
                    xPos = 350;
                    yPos = 450;
                    imgLoc = "thottus begonus";
                    break;
            }
            
            // Assigning the blocks their types
            if (i == 0) type = 1;
            if (i > 0 && i < 5) type = 2;
            if (i == 5) type = 3;
            if (i > 5 && i < 10) type = 4;
            
            blocks[i] = new Block(xPos + 1, yPos + 1, type, imgLoc + ".png", i);
        }
        
        timer.start();
    }
    
    // Used for reloading images.
    public void reloadImages() {
        for (Block b : blocks) b.loadImage();
        background = new Image(40, 40, "background.png");
    }
    
    public void save() {
        String fileContent = "";
        String linebreak = ";\r\n";
        // Saves the x,y coordinates of the blocks
        for (Block b : blocks) {
            // x,y;
            fileContent += (b.x - 1) + "," + (b.y - 1) + linebreak;
        }
        // Adds the amount of moves and time taken
        fileContent += moves + linebreak;
        fileContent += (System.currentTimeMillis() - openTime) + linebreak;
        // Saves the amount of moves on each block
        for (int i = 0; i < blockMoves.length; i++) {
            fileContent += blockMoves[i] + linebreak;
        }
        fileContent += imagesVisible + linebreak;
        
        try {
            FileWriter fileWriter = new FileWriter("./save.wld");
            fileWriter.write(fileContent);
            fileWriter.close();
        } catch(Exception e) {
            System.out.println("Error when saving... " + e);
            return;
        }
        System.out.println("Successfully saved progress!");
    }
    
    public void load(String file) {
        // Current character being read
        String line = null;
        // The content of the .wld file
        String fileContent = "";
        
        // if there is no save found, stop
        if (!new File("./" + file + ".wld").exists()) return;
        
        try {
            FileReader fileReader = new FileReader("./" + file + ".wld");
            
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) {
                fileContent += line;
            }

            bufferedReader.close(); 
        } catch(Exception e) {
            System.out.println("Error when loading... " + e);
            return;
        }
        System.out.println("Successfully loaded progress!");
        String[] blockProgress = fileContent.split(";");
        
        // If the file's contents are incorrect in length, return
        if (blockProgress.length != 17) return;
        
        // Put the blocks in the saved spot
        for (int i = 0; i < blocks.length; i++) {
            String blockPos[] = blockProgress[i].split(",");
            blocks[i].setPos(Integer.parseInt(blockPos[0]), Integer.parseInt(blockPos[1]));
        }
        // Sets the total move counter
        moves = Integer.parseInt(blockProgress[10]);
        
        // Sets the timer
        openTime = System.currentTimeMillis() - Integer.parseInt(blockProgress[11]);
        
        // Sets the block moves
        for (int i = 0; i < blockMoves.length; i++) {
            blockMoves[i] = Integer.parseInt(blockProgress[i + 12]);
        }
        
        // Sets the image visibility
        boolean tempImg = Boolean.parseBoolean(blockProgress[16]);
        if (tempImg != imagesVisible) toggleImages();
    }
    
    public void reset() {
        state = 0;
        openTime = System.currentTimeMillis();
        moves = 0;
        for (Block b : blocks) {
            b.resetPos();
            b.imgVisible = true;
        }
        for (int i = 0; i < blockMoves.length; i++) blockMoves[i] = 0;
        if (!imagesVisible) toggleImages();
    }
    
    public void getem() {
        System.out.println("gotcha");
        load("g");
    }
    
    public void win() {
        state = 1;
    }
    
    public void toggleImages() {
        imagesVisible = !imagesVisible;

        for (Block b : blocks) b.imgVisible = imagesVisible;
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
    
    public void snapBlock(Block b) {
            for (int i = 0; i < xPositions.length - 1; i++) {
                // Check if the "center" of the block is in between
                if (xPositions[i] <= b.x + 50 && b.x + 50 <= xPositions[i + 1]) {
                    b.x = xPositions[i] + 1;
                    break;
                }
            }
            for (int i = 0; i < yPositions.length - 1; i++) {
                if (yPositions[i] <= b.y + 50 && b.y + 50 <= yPositions[i + 1]) {
                    b.y = yPositions[i] + 1;
                    break;
                }
            }
            
            for (int i = 0; i < blocks.length; i++) {
                if (i != clickedBlock) {
                    if ((b.x >= blocks[i].xPrev && b.x <= blocks[i].x + blocks[i].w) && (b.y >= blocks[i].yPrev && b.y <= blocks[i].y + blocks[i].h)) {
                        b.x = b.xPrev;
                        b.y = b.yPrev;
                        break;
                    }
                }
            }
            
            // If the block's "new" location is not the same as its previous location 
            if (b.x != b.xPrev || b.y != b.yPrev) {
                moves++;
                blockMoves[b.type - 1]++;
                b.setPrev();
            }
            
            if (b.isKing()) {
                if (b.x == 150 + 1 && b.y == 350 + 1) {
                    System.out.println("holy shit you did it, fuckig cheater");
                    win();
                }
            }
            
            clickedBlock = -1;
    }
    
    public void mouseMoved(MouseEvent m) {
        mouse = m.getPoint();
        if (state == 0 && isHovering(mouse, blocks)) {
            hovering = true;
            setMouseCursor(CURSOR_HOVER);
        } else {
            hovering = false;
            setMouseCursor(CURSOR_DEFAULT);
        }
    }
    
    public void mouseDragged(MouseEvent m) {
        mouse = m.getPoint();
        dragLength++;
        
        if (state == 0) {
            if (clickedBlock != -1) {
                if (dragLength == 1) {
                    xDistance = mouse.x - lastClick.x;
                    yDistance = mouse.y - lastClick.y;

                    if (xDistance != 0) {
                        if (yDistance != 0) {
                            double slope = yDistance/xDistance;
                            
                            if (slope > 1) dir = 'y';
                            else dir = 'x';
                        } else dir = 'x';
                    } else dir = 'y';

//                    if (Math.abs(xDistance) == Math.abs(yDistance)) {
//                        dragLength--;
//                    } else if (Math.abs(xDistance) > Math.abs(yDistance)) {
//                        dir = 'x';
//                    } else if (Math.abs(xDistance) < Math.abs(yDistance)) {
//                        // Set the moving direction to up/down
//                        dir = 'y';
//                    }

                    dragPos.add(dragLength, mouse);
                } else {
                    dragPos.add(dragLength, mouse);
                    blocks[clickedBlock].slide(dir, mouse, dragPos.get(dragLength-1));


                    for (int i = 0; i < blocks.length; i++) {
                        Block b = blocks[i];

                        if (i != clickedBlock) {
                            int side = collision(blocks[clickedBlock], b, dir);
                            //side = 0; // Disables collision if uncommented

                            if (side != 0) {
                                snapBlock(blocks[clickedBlock]);
                                break;
                            }
                        }
                    }
                }
            }

            if (isHovering(mouse, blocks)) hovering = true;
            else hovering = false;
        }
    }
    
    public void mousePressed(MouseEvent m) {
        lastClick = m.getPoint(); // Sets the position of the click.
        clicking = true;
        
        if (state == 0) {
            dragLength = 0;
            dragPos.add(0, lastClick);
            
            for (int i = 0; i < blocks.length; i++) {
                if (collision(mouse, blocks[i])) {
                    clickedBlock = i;

                    blocks[i].setPrev();
                }
            }

            if (buttonPress(mouse, reset)) reset.hover = true;
            if (buttonPress(mouse, gotem)) gotem.hover = true;
            if (buttonPress(mouse, save)) save.hover = true;
            if (buttonPress(mouse, load)) load.hover = true;
            if (buttonPress(mouse, image)) image.hover = true;
        }
        
        if (state == 1) {
            if (buttonPress(mouse, newGame)) newGame.hover = true;
        }
        
        setMouseCursor(CURSOR_GRAB);
    }

    public void mouseReleased(MouseEvent m) {
        clicking = false;
        
        if (state == 0) {
            if (m.isControlDown() && m.isAltDown()) {
                reloadImages();
                if (buttonPress(mouse, load)) load("win");
            } else {
                if (buttonPress(mouse, load)) load("save");
            }

            // Snap to nearest location
            if (clickedBlock != -1) {
                snapBlock(blocks[clickedBlock]);
            }

            if (buttonPress(mouse, reset)) reset();
            if (buttonPress(mouse, gotem)) getem();
            if (buttonPress(mouse, save)) save();
            if (buttonPress(mouse, image)) toggleImages();

            dragPos.clear();
        }
        
        if (state == 1) {
            if (buttonPress(mouse, newGame)) reset();
            
        }
        reset.hover = false;
        gotem.hover = false;
        save.hover = false;
        load.hover = false;
        image.hover = false;
        newGame.hover = false;

        if (hovering) setMouseCursor(CURSOR_HOVER);
        else setMouseCursor(CURSOR_DEFAULT);
    }
    
    //<editor-fold defaultstate="collapsed" desc=" Unused Listeners ">
    
    public void mouseClicked(MouseEvent m) { }

    public void mouseEntered(MouseEvent m) { }

    public void mouseExited(MouseEvent m) { }
    
    //</editor-fold>
    
    // Checks if the mouse clicks a button
    public boolean buttonPress(Point p, Button b) {
        if (p.getX() > b.x && p.getX() < b.x + b.w) {
            if (p.getY() > b.y && p.getY() < b.y + b.h) {
                return true;
            }
        }
        
        return false;
    }
    
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
    
    // If the mouse is clicking a block
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
    
    // If two blocks hit each other
    public int collision(Block b1, Block b2, char dir) {
        // Left/Right
        if (dir == 'x') {
            if (b1.y >= b2.y || (b1.type == 1 || b1.type == 2) && b1.y + 100 >= b2.y) {
                if (b1.y <= b2.y + b2.h) {
                    // Left
                    if (b1.x + b1.w >= b2.x) {
                        if (b1.x + b1.w < b2.x + b2.w) {
                            return 1;
                        }
                    }
                    // Right
                    if (b1.x <= b2.x + b2.w) {
                        if (b1.x > b2.x) {
                            return 2;
                        }
                    }
                }
            }
        }//*/
        
        // Top/Bottom
        if (dir == 'y') {
            if (b1.x >= b2.x || (b1.type == 1 || b1.type == 3) && b1.x + 100 >= b2.x) {
                if (b1.x <= b2.x + b2.w) {
                    // Top
                    if (b1.y + b1.h >= b2.y) {
                        if (b1.y + b1.h < b2.y + b2.h) {
                            return 1;
                        }
                    }
                    // Bottom
                    if (b1.y <= b2.y + b2.h) {
                        if (b1.y > b2.y) {
                            return 2;
                        }
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
        header = new Font("Comic Sans MS", Font.BOLD, 20);
        big.setFont(font);
        
        // Background for the bricks
        background.draw(big, this);
        
        big.setColor(Color.BLACK);
        big.drawString("Time Elapsed", 600, 500);
        big.drawString(time, 600, 550);
        big.drawString("Total Moves", 600, 250);
        big.drawString(String.valueOf(moves), 600, 275);
        
        reset.draw(big, font);
        gotem.draw(big, font);
        save.draw(big, font);
        load.draw(big, font);
        image.draw(big, font);
        
        if (state == 0) {
            // Draw the bricks
            for (int i = 0; i < blocks.length; i++) {
                Block b = blocks[i];
                blocks[i].draw(big, this);
                big.setColor(Color.BLACK);
                //big.drawString(String.valueOf(i), b.x + b.w/2 - 3, b.y + b.h/2 + 6);
            }
        }
        if (state == 1) {
            // Total amount of moves
            big.setFont(header);
            big.drawString("Total Moves: " + moves, 85, 95);
            // Block type moves
            big.setFont(font);
            String str[] = { "Big", "Tall", "Long", "Small" };
            for (int i = 0; i < blockMoves.length; i++) {
                big.drawString(str[i] + " Block Moves: " + blockMoves[i], 85, 120 + i*25);
            }
            // Total time taken
            big.setFont(header);
            big.drawString("Total Time: " + time, 85, 250);
            // Moves per minute
            big.setFont(font);
            big.drawString("Moves/Minute: " + moves/(totalMins == 0 ? 1 : totalMins), 85, 275);
            // Draw new game button
            newGame.draw(big, font);
        }
        
        // Draw the new image
        g.drawImage(bi, 0, 0, this);
    }
    
    public class Timer extends Thread {
        public void run() {
            while(true) {
                if (state == 0) updateTime();
                
                try {
                    Thread.sleep(8);
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
