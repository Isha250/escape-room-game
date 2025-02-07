import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class GameGUI extends JFrame implements KeyListener, MouseListener {
    private int playerX = 50, playerY = 50; // Initial player position
    private final int STEP = 20; // Movement step size
    private boolean chitRead = false;
    private boolean cupboardOpen = false;
    private boolean doorUnlocked = false;
    private int currentRoom = 1;
    private boolean mirrorPuzzleSolved = false;
    private ArrayList<String> inventory = new ArrayList<>(); // Inventory List
    private Image tileImage, lavaImage; // Declare tile and lava images


    private Image playerRight, playerLeft, playerUp, playerDown;
    private String playerDirection = "down"; // Default facing direction

private final int TILE_SIZE = 40; // Size of each tile in the maze
private int[][] maze = {
    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
    {1, 0, 2, 0, 0, 0, 2, 0, 0, 1},
    {1, 0, 1, 1, 1, 1, 1, 0, 1, 1},
    {1, 0, 0, 0, 0, 0, 1, 0, 0, 1},
    {1, 1, 1, 1, 0, 1, 1, 0, 1, 1},
    {1, 0, 0, 0, 0, 1, 0, 0, 1, 1},
    {1, 2, 1, 1, 0, 0, 0, 0, 1, 1},
    {1, 0, 0, 1, 1, 1, 2, 1, 1, 1},
    {1, 0, 1, 0, 0, 0, 0, 0, 0, 1},
    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
};

    public GameGUI() {
        setTitle("🏠 Escape Room Adventure");
        setSize(600, 400);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(this);
        addMouseListener(this);
        setVisible(true);

        // Load character images
        playerRight = new ImageIcon("right.png").getImage();
        playerLeft = new ImageIcon("left.png").getImage();
        playerUp = new ImageIcon("up.png").getImage();
        playerDown = new ImageIcon("down.png").getImage();
        

        // Load tile and lava images
        tileImage = new ImageIcon("tile.jpg").getImage(); // Regular tile
        lavaImage = new ImageIcon("lava.png").getImage(); // Lava image
        // (Replace "tile.png" and "lava.png" with your actual image paths)
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(50, 50, 500, 300);

        // Draw player sprite based on direction
        Image playerImage = switch (playerDirection) {
            case "right" -> playerRight;
            case "left" -> playerLeft;
            case "up" -> playerUp;
            default -> playerDown; // Default to down
        };

        g.drawImage(playerImage, playerX, playerY, 40, 40, this);

       if (currentRoom == 1) {
        drawRoom1(g);
    } else if (currentRoom == 2) {
        drawRoom2(g);
    } else if (currentRoom == 3) {
        drawRoom3(g);
    }
        drawInventory(g);
    }

    private void drawRoom1(Graphics g) {
        g.setColor(doorUnlocked ? Color.GREEN : Color.RED);
        g.fillRect(500, 150, 50, 80);

        g.setColor(Color.ORANGE);
        g.fillRect(150, 100, 80, 40);
        g.setColor(Color.YELLOW);
        g.fillRect(140, 80, 20, 20);
        g.fillRect(230, 80, 20, 20);

        g.setColor(Color.DARK_GRAY);
        g.fillRect(350, 100, 70, 120);
        g.setColor(Color.BLACK);
        if (cupboardOpen) {
            g.drawString("🗄 Open", 365, 170);
        } else {
            g.drawString("🔒 Closed", 360, 170);
        }

        g.setColor(Color.WHITE);
        g.fillRect(180, 110, 20, 10);
        g.setColor(Color.BLACK);
        g.drawString("📜", 185, 120);

        // Draw Key if cupboard is open
        if (cupboardOpen && !inventory.contains("Key")) {
            g.setColor(Color.YELLOW);
            g.fillRect(360, 150, 10, 10);
            g.setColor(Color.BLACK);
            g.drawString("🔑", 360, 145);
        }
    }

   private void drawRoom2(Graphics g) {
    // Loop through the maze array and draw corresponding tiles
    for (int row = 0; row < maze.length; row++) {
        for (int col = 0; col < maze[row].length; col++) {
            int tile = maze[row][col];
            int x = col * TILE_SIZE;
            int y = row * TILE_SIZE;

            if (tile == 1) {
                g.setColor(Color.GRAY); // Wall color
                g.fillRect(x, y, TILE_SIZE, TILE_SIZE); // Draw wall
            } else if (tile == 2) {
                g.drawImage(lavaImage, x, y, TILE_SIZE, TILE_SIZE, this); // Lava image
            } else {
                g.drawImage(tileImage, x, y, TILE_SIZE, TILE_SIZE, this); // Regular tile
            }
        }
    }

    // Display the player's current room
    g.setColor(Color.BLACK);
    g.drawString("Welcome to Room 2! Avoid the lava and find your way out!", 200, 30);
}

private void drawRoom3(Graphics g) {
    // Walls with Perspective
    g.setColor(new Color(180, 180, 180));
    g.fillPolygon(new int[]{50, 550, 500, 100}, new int[]{50, 50, 350, 350}, 4);

    // Mirror Wall
    g.setColor(Color.LIGHT_GRAY);
    g.fillRect(250, 100, 100, 200);
    g.setColor(Color.BLACK);
    g.drawString("🪞 Mirror", 270, 120);

    // Fake Door
    g.setColor(Color.RED);
    g.fillRect(500, 150, 50, 80);
    g.setColor(Color.BLACK);
    g.drawString("🚪 Fake Door", 505, 190);

    // Real Hidden Door (only if puzzle solved)
    if (mirrorPuzzleSolved) {
        g.setColor(Color.GREEN);
        g.fillRect(50, 150, 50, 80);
        g.drawString("Exit ➡", 55, 190);
    }

    // Draw Player
    g.setColor(Color.PINK);
    g.fillOval(playerX, playerY, 30, 30);
}


    private void drawInventory(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawString("🛍 Inventory: " + inventory.toString(), 50, 370);
    }

@Override
public void keyPressed(KeyEvent e) {
    int key = e.getKeyCode();

    // Room 2 lava and wall collision detection
    int playerTileX = playerX / TILE_SIZE;
    int playerTileY = playerY / TILE_SIZE;

    if (currentRoom == 2) {
        if (maze[playerTileY][playerTileX] == 1 || maze[playerTileY][playerTileX] == 2) {
            JOptionPane.showMessageDialog(this, "❌ You can't go through walls or lava! Restarting Room 2...");
            playerX = 100; 
            playerY = 150;
            return;
        }
    }

    // Movement logic
    if (key == KeyEvent.VK_LEFT && playerX > 50) {
        playerX -= STEP;
        playerDirection = "left";
    }
    if (key == KeyEvent.VK_RIGHT && playerX < 520) {
        playerX += STEP;
        playerDirection = "right";
    }
    if (key == KeyEvent.VK_UP && playerY > 50) {
        playerY -= STEP;
        playerDirection = "up";
    }
    if (key == KeyEvent.VK_DOWN && playerY < 320) {
        playerY += STEP;
        playerDirection = "down";
    }

    // Room Transitions
    if (currentRoom == 1 && doorUnlocked && playerX >= 500 && playerY >= 150 && playerY <= 230) {
        JOptionPane.showMessageDialog(this, "🚪 You entered Room 2!");
        currentRoom = 2;
        playerX = 100;
        playerY = 150;
    } else if (currentRoom == 1 && !doorUnlocked && playerX >= 500 && playerY >= 150 && playerY <= 230) {
        JOptionPane.showMessageDialog(this, "🚪 The door is locked! Find a way to open it.");
    }

    if (currentRoom == 2 && playerX <= 50 && playerY >= 150 && playerY <= 230) {
        JOptionPane.showMessageDialog(this, "⬅ You returned to Room 1.");
        currentRoom = 1;
        playerX = 450;
        playerY = 150;
    }

    // ✅ NEW: Room 2 → Room 3 Transition
    if (currentRoom == 2 && playerX >= 500 && playerY >= 150 && playerY <= 230) {
        JOptionPane.showMessageDialog(this, "🪞 You entered Room 3!");
        currentRoom = 3;
        playerX = 100; 
        playerY = 150;
    }

    // ✅ NEW: Room 3 Fake Door
    if (currentRoom == 3 && playerX >= 500 && playerY >= 150 && playerY <= 230) {
        JOptionPane.showMessageDialog(this, "🚪 It's a fake door! Solve the mirror puzzle first.");
    }

    // ✅ NEW: Room 3 Exit (If Puzzle Solved)
    if (currentRoom == 3 && playerX <= 50 && playerY >= 150 && playerY <= 230 && mirrorPuzzleSolved) {
        JOptionPane.showMessageDialog(this, "🎉 You escaped Room 3!");
        currentRoom = 1; 
        playerX = 450;
        playerY = 150;
    }

    repaint();
}



    @Override
public void mouseClicked(MouseEvent e) {
    int mouseX = e.getX();
    int mouseY = e.getY();

    if (currentRoom == 1) {
        if (mouseX >= 180 && mouseX <= 200 && mouseY >= 110 && mouseY <= 120) {
            chitRead = true;
            inventory.add("Chit");
            JOptionPane.showMessageDialog(this, "📜 The chit says: 123");
        }

        if (mouseX >= 350 && mouseX <= 420 && mouseY >= 100 && mouseY <= 220) {
            if (chitRead) {
                cupboardOpen = true;
                JOptionPane.showMessageDialog(this, "🗄 You opened the cupboard! There's a key inside.");
            } else {
                JOptionPane.showMessageDialog(this, "🔒 The cupboard is locked. Maybe a clue can help?");
            }
        }

        if (cupboardOpen && !inventory.contains("Key") && mouseX >= 360 && mouseX <= 370 && mouseY >= 150 && mouseY <= 160) {
            inventory.add("Key");
            JOptionPane.showMessageDialog(this, "🔑 You picked up the Key!");
        }

        if (inventory.contains("Key") && mouseX >= 500 && mouseX <= 550 && mouseY >= 150 && mouseY <= 230) {
            String code = JOptionPane.showInputDialog(this, "Enter the 3-digit code to unlock the door:");
            if (code != null && code.equals("123")) {
                doorUnlocked = true;
                JOptionPane.showMessageDialog(this, "✅ Correct code! The door is now unlocked.");
            } else {
                JOptionPane.showMessageDialog(this, "❌ Incorrect code! Try again.");
            }
        }
    }

    // ✅ NEW: Room 3 Mirror Puzzle
    if (currentRoom == 3) {
        if (mouseX >= 250 && mouseX <= 350 && mouseY >= 100 && mouseY <= 300) {
            String answer = JOptionPane.showInputDialog(this, "Solve the mirror puzzle: What reflects but never speaks?");
            if (answer != null && answer.equalsIgnoreCase("mirror")) {
                mirrorPuzzleSolved = true;
                JOptionPane.showMessageDialog(this, "✅ Puzzle Solved! The hidden door is now open.");
            } else {
                JOptionPane.showMessageDialog(this, "❌ Incorrect! Try again.");
            }
        }
    }

    repaint();
}


    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public static void main(String[] args) {
        new GameGUI();
    }
}
