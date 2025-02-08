import java.awt.*;
import javax.swing.*;

public class GamePanel extends JPanel {
    private GameGUI game;

    public GamePanel(GameGUI game) {
        this.game = game;
        setFocusable(true);
        addKeyListener(game);
        addMouseListener(game);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.drawGame(g);
    }
}