import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameOfLife extends JFrame implements ActionListener {
    private static final int WIDTH = 720;
    private static final int HEIGHT = 720;
    private static final int CELL_SIZE = 20;
    private static final int ROWS = HEIGHT / CELL_SIZE;
    private static final int COLS = WIDTH / CELL_SIZE;
    private boolean[][] cells = new boolean[ROWS][COLS];
    private boolean running = false;
    private Timer timer;
    private JPanel panel;
    private JMenuBar menuBar;
    private JMenuItem startItem, stopItem, resetItem, generateItem, exitItem;
    private boolean mousePressed = false;

    public GameOfLife() {
        setTitle("Conway's Game of Life");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JFrame frame = new JFrame();
        setResizable(false);

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (int row = 0; row < ROWS; row++) {
                    for (int col = 0; col < COLS; col++) {
                        if (cells[row][col]) {
                            g.setColor(Color.BLACK);
                            g.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                        } else {
                            g.setColor(Color.WHITE);
                            g.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                        }
                    }
                }
            }
        };
        add(panel);

        menuBar = new JMenuBar();
        startItem = new JMenuItem("Start");
        stopItem = new JMenuItem("Stop");
        resetItem = new JMenuItem("Reset");
        generateItem = new JMenuItem("Random generation");
        exitItem = new JMenuItem("Exit");
        startItem.addActionListener(this);
        stopItem.addActionListener(this);
        resetItem.addActionListener(this);
        generateItem.addActionListener(this);
        exitItem.addActionListener(this);
        menuBar.add(startItem);
        menuBar.add(stopItem);
        menuBar.add(resetItem);
        menuBar.add(generateItem);
        menuBar.add(exitItem);
        setJMenuBar(menuBar);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startItem) {
            if (!running) {
                running = true;
                timer = new Timer(100, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        update();
                        panel.repaint();
                    }
                });
                timer.start();
            }
        } else if (e.getSource() == stopItem) {
            if (running) {
                running = false;
                timer.stop();
            }
        } else if (e.getSource() == resetItem) {
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    cells[row][col] = false;
                }
            }
            panel.repaint();
        } else if (e.getSource() == generateItem) {
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    cells[row][col] = Math.random() < 0.5;
                }
            }
            panel.repaint();
        } else if (e.getSource() == exitItem){
            System.exit(0);
        }
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = e.getY() / CELL_SIZE;
                int col = e.getX() / CELL_SIZE;
                cells[row][col] = !cells[row][col];
                panel.repaint();
                mousePressed = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mousePressed = false;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mousePressed = false;
            }
        });
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (mousePressed) {
                    int row = e.getY() / CELL_SIZE;
                    int col = e.getX() / CELL_SIZE;
                    cells[row][col] = true;
                    panel.repaint();
                }
            }
        });
    }

    private void update() {
        boolean[][] nextCells = new boolean[ROWS][COLS];
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int neighbors = countNeighbors(row, col);
                if (cells[row][col]) {
                    if (neighbors == 2 || neighbors == 3) {
                        nextCells[row][col] = true;
                    }
                } else {
                    if (neighbors == 3) {
                        nextCells[row][col] = true;
                    }
                }
            }
        }
        cells = nextCells;
    }

    private int countNeighbors(int row, int col) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int r = (row + i + ROWS) % ROWS;
                int c = (col + j + COLS) % COLS;
                if (cells[r][c]) {
                    count++;
                }
            }
        }
        if (cells[row][col]) {
            count--;
        }
        return count;
    }

    public static void main(String[] args) {

        new GameOfLife();
    }
}