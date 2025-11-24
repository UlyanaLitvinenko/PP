import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Main extends JPanel implements ActionListener, KeyListener {
    int x = 350, y = 500, s = 6;
    int ballX, ballY, ballR = 15;
    int score = 0;
    int misses = 0;
    int ballSpeed = 4;
    boolean left, right, gameOver = false;
    Timer t = new Timer(16, this);
    Random rnd = new Random();

    public Main() {
        setFocusable(true);
        addKeyListener(this);
        newBall();
        t.start();
        SwingUtilities.invokeLater(this::requestFocusInWindow);
    }

    void newBall() {
        ballX = rnd.nextInt(780);
        ballY = -20;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        g.fillRect(x, y, 100, 20);
        g.setColor(Color.RED);
        g.fillOval(ballX, ballY, ballR*2, ballR*2);
        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 20, 20);
        g.drawString("Misses: " + misses, 20, 40);
        if (gameOver) {
            g.setFont(new Font("SansSerif", Font.BOLD, 48));
            g.drawString("GAME OVER", 250, 300);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (gameOver) return;
        if (left) x -= s;
        if (right) x += s;
        if (x < 0) x = 0;
        if (x > 700) x = 700;
        ballY += ballSpeed;
        if (ballY > 600) {
            misses++;
            if (misses >= 3) {
                gameOver = true;
                t.stop();
            } else {
                ballSpeed++;
                newBall();
            }
        }
        if (ballY+ballR >= y && ballX+ballR >= x && ballX <= x+100) {
            score++;
            newBall();
        }
        repaint();
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_LEFT) left = true;
        if (e.getKeyCode()==KeyEvent.VK_RIGHT) right = true;
    }
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_LEFT) left = false;
        if (e.getKeyCode()==KeyEvent.VK_RIGHT) right = false;
    }
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame f = new JFrame("Catch Ball");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(800,600);
        f.setContentPane(new Main());
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
