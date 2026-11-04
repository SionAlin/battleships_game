package main.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BoardPanel extends JPanel{

    private static final int GRID_SIZE = 10;
    private static final Color PAPER = new Color(253, 252, 246);
    private static final Color GRID = new Color(150, 190, 225, 115);
    private static final Color OK = new Color(60, 180, 90, 130);
    private static final Color BAD = new Color(210, 60, 60, 130);
    private static final Color SHIP = new Color(90, 100, 120, 170);

    private final boolean[][] occupied = new boolean[GRID_SIZE][GRID_SIZE];

    private int shipLength = 0;     
    private boolean horizontal = true;
    private int hoverCol = -1, hoverRow = -1;
    private Runnable onShipPlaced;

    public BoardPanel() {
        setOpaque(true);
        setBackground(PAPER);

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                updateHover(e);
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                updateHover(e);
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                hoverCol = hoverRow = -1;
                repaint();
            }
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    rotate();
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    updateHover(e);
                    tryPlace();
                }
            }
        });

        addMouseWheelListener(e -> rotate());

        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('R'), "rotate");
        getActionMap().put("rotate", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotate();
            }
        });
    }

    public void setSelectedShipLength(int length) {
        this.shipLength = length;
        repaint();
    }

    public void setOnShipPlaced(Runnable r) {
        this.onShipPlaced = r;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public boolean[][] getOccupied() {
        return occupied;
    }

    private int cellSize() {
        return Math.min(getWidth(), getHeight()) / GRID_SIZE;
    }

    private int offsetX() {
        return (getWidth() - cellSize() * GRID_SIZE) / 2;
    }

    private int offsetY() {
        return (getHeight() - cellSize() * GRID_SIZE) / 2;
    }

    private void updateHover(MouseEvent e) {
        int cs = cellSize();
        if (cs <= 0) return;
        int col = (e.getX() - offsetX()) / cs;
        int row = (e.getY() - offsetY()) / cs;
        boolean inside = e.getX() >= offsetX() && e.getY() >= offsetY()
                && col < GRID_SIZE && row < GRID_SIZE;
        if (!inside) {
            col = row = -1;
        }
        if (col != hoverCol || row != hoverRow) {
            hoverCol = col;
            hoverRow = row;
            repaint();
        }
    }

    private void rotate() {
        horizontal = !horizontal;
        repaint();
    }

    private boolean canPlace(int col, int row) {
        if (shipLength <= 0 || col < 0 || row < 0) return false;
        for (int i = 0; i < shipLength; i++) {
            int c = horizontal ? col + i : col;
            int r = horizontal ? row : row + i;
            if (c >= GRID_SIZE || r >= GRID_SIZE || occupied[r][c]) return false;
        }
        return true;
    }

    private void tryPlace() {
        if (!canPlace(hoverCol, hoverRow)) return;
        for (int i = 0; i < shipLength; i++) {
            int c = horizontal ? hoverCol + i : hoverCol;
            int r = horizontal ? hoverRow : hoverRow + i;
            occupied[r][c] = true;
        }
        shipLength = 0;
        repaint();
        if (onShipPlaced != null) onShipPlaced.run();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int cs = cellSize();
        int ox = offsetX(), oy = offsetY();

        g2.setColor(SHIP);
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                if (occupied[r][c]) {
                    g2.fillRect(ox + c * cs, oy + r * cs, cs, cs);
                }
            }
        }

        if (shipLength > 0 && hoverCol >= 0) {
            g2.setColor(canPlace(hoverCol, hoverRow) ? OK : BAD);
            for (int i = 0; i < shipLength; i++) {
                int c = horizontal ? hoverCol + i : hoverCol;
                int r = horizontal ? hoverRow : hoverRow + i;
                if (c < GRID_SIZE && r < GRID_SIZE) {
                    g2.fillRect(ox + c * cs, oy + r * cs, cs, cs);
                }
            }
        }

        g2.setColor(GRID);
        g2.setStroke(new BasicStroke(3.5f));
        int size = cs * GRID_SIZE;
        for (int i = 0; i <= GRID_SIZE; i++) {
            g2.drawLine(ox + i * cs, oy, ox + i * cs, oy + size);
            g2.drawLine(ox, oy + i * cs, ox + size, oy + i * cs);
        }

        g2.dispose();
    }

}
