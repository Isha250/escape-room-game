import java.awt.Label;

public class TimerThread extends Thread {
    private Label timerLabel;
    private int timeLeft = 60;
    private GameGUI gameGUI;
    private boolean running = true;

    public TimerThread(Label label, GameGUI gui) {
        this.timerLabel = label;
        this.gameGUI = gui;
    }

    public void run() {
        while (timeLeft > 0 && running) {
            try {
                Thread.sleep(1000);
                timeLeft--;
                timerLabel.setText("Time Left: " + timeLeft + " seconds");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (timeLeft == 0) {
            gameGUI.gameOver();
        }
    }

    public void stopTimer() {
        running = false;
    }
}