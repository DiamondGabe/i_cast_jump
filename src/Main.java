import javax.swing.JFrame;

public static void main(String[] args) {
    JFrame frame = new JFrame("I Cast Jump");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(800, 600);
    frame.setResizable(false);
    frame.setLocationRelativeTo(null);
    GamePanel gamePanel = new GamePanel();
    frame.add(gamePanel);
    frame.pack();
    frame.setVisible(true);
    gamePanel.startGameThread();
}

