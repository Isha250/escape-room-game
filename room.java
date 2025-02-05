import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.sound.sampled.*;
import javax.swing.*;

public class Room extends JFrame implements ActionListener {
    private JLabel clueLabel, statusLabel, turnLabel, inventoryLabel;
    private JTextField answerField;
    private JButton submitButton, hintButton, nextRoomButton;
    private Puzzle puzzle1, puzzle2;
    private Player player1, player2;
    private Player currentPlayer;
    private TimerThread timerThread;
    private ArrayList<String> inventory = new ArrayList<>(); // Inventory List
    private boolean isRoomComplete = false;

    public Room(Player p1, Player p2, TimerThread timerThread) {
        this.player1 = p1;
        this.player2 = p2;
        this.timerThread = timerThread;
        this.currentPlayer = player1;

        setTitle("🏆 Escape Room - Multiplayer Challenge");
        setSize(500, 400);
        setLayout(null);
        getContentPane().setBackground(Color.DARK_GRAY);

        puzzle1 = new Puzzle("🌟 I speak without a mouth and hear without ears. What am I?", "echo");
        puzzle2 = new Puzzle("🚶‍♂️ The more you take, the more you leave behind. What are they?", "footsteps");

        turnLabel = new JLabel("🎲 Current Turn: " + currentPlayer.getName());
        turnLabel.setBounds(50, 20, 400, 30);
        turnLabel.setForeground(Color.YELLOW);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(turnLabel);

        clueLabel = new JLabel("<html><center>🧩 Clue:<br>" + getCurrentPuzzle().getQuestion() + "</center></html>");
        clueLabel.setBounds(50, 60, 400, 60);
        clueLabel.setForeground(Color.CYAN);
        clueLabel.setFont(new Font("Arial", Font.BOLD, 14));
        clueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(clueLabel);

        answerField = new JTextField(20);
        answerField.setBounds(150, 140, 200, 30);
        add(answerField);

        submitButton = new JButton("🔍 Submit Answer");
        submitButton.setBounds(180, 180, 150, 40);
        submitButton.setBackground(Color.GREEN);
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.addActionListener(this);
        add(submitButton);

        hintButton = new JButton("💡 Get Hint");
        hintButton.setBounds(180, 230, 150, 40);
        hintButton.setBackground(Color.ORANGE);
        hintButton.setFont(new Font("Arial", Font.BOLD, 14));
        hintButton.addActionListener(e -> showHint());
        add(hintButton);

        statusLabel = new JLabel("");
        statusLabel.setBounds(120, 280, 300, 30);
        statusLabel.setForeground(Color.ORANGE);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(statusLabel);

        inventoryLabel = new JLabel("🛍 Inventory: " + inventory.toString());
        inventoryLabel.setBounds(50, 330, 400, 30);
        inventoryLabel.setForeground(Color.WHITE);
        inventoryLabel.setFont(new Font("Arial", Font.BOLD, 12));
        add(inventoryLabel);

        nextRoomButton = new JButton("➡ Next Room");
        nextRoomButton.setBounds(180, 280, 150, 40);
        nextRoomButton.setBackground(Color.BLUE);
        nextRoomButton.setFont(new Font("Arial", Font.BOLD, 14));
        nextRoomButton.addActionListener(e -> switchToNextRoom());
        nextRoomButton.setEnabled(false); // Disabled until the current room is completed
        add(nextRoomButton);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String answer = answerField.getText().trim().toLowerCase();
        if (getCurrentPuzzle().checkAnswer(answer)) {
            playSound("success.wav");
            statusLabel.setText("🎉 " + currentPlayer.getName() + " escaped! 🏆");
            statusLabel.setForeground(Color.GREEN);
            inventory.add("Golden Key");
            inventoryLabel.setText("🛍 Inventory: " + inventory.toString());
            currentPlayer.winGame();
            timerThread.stopTimer();
            nextRoomButton.setEnabled(true); // Enable the button to go to next room
            JOptionPane.showMessageDialog(this, "🔑 You found the Golden Key! Escape Unlocked!");
        } else {
            playSound("fail.wav");
            statusLabel.setText("❌ Wrong answer! Next player's turn.");
            statusLabel.setForeground(Color.RED);
            switchTurn();
        }
    }

    private void showHint() {
        JOptionPane.showMessageDialog(this, "🤔 Hint: Think about common phrases related to the question.");
    }

    private Puzzle getCurrentPuzzle() {
        return (currentPlayer == player1) ? puzzle1 : puzzle2;
    }

    private void switchTurn() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
        turnLabel.setText("🎲 Current Turn: " + currentPlayer.getName());
        clueLabel.setText("<html><center>🧩 Clue:<br>" + getCurrentPuzzle().getQuestion() + "</center></html>");
        answerField.setText("");
    }

    private void switchToNextRoom() {
        // Dispose of the current room to switch to the next room
        dispose();

        // Create and show the next room
        // You can instantiate the next room with relevant player data, inventory, etc.
        Room nextRoom = new Room(player1, player2, timerThread);
        nextRoom.setVisible(true); // Make the next room visible
    }

    private void playSound(String soundFile) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(getClass().getResource(soundFile)));
            clip.start();
        } catch (Exception ex) {
            System.out.println("Sound error: " + ex.getMessage());
        }
    }
}