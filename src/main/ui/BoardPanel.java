package main.ui;

import main.model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BoardPanel extends JPanel{
    
    public enum Mode { PLACEMENT, ATTACK, DEFENSE }
    public interface ShotListener { void onShot(int row, int col); }

    private static final int GRID_SIZE = 10;
    private static final Color PAPER = new Color(253, 252, 246);
    private static final Color GRID = new Color(35, 75, 155);
    private static final Color OK = new Color(17, 136, 123);
    private static final Color BAD = new Color(172, 50, 53);
    private static final Color SHIP = new Color(35, 75, 155);
    
    private Board board;
    private final Mode mode;
    
    private ShipType selectedShip = null;
    private boolean horizontal = true;
    private boolean attackEnabled = true;
    private int hoverCol = -1, hoverRow = -1;
    private Runnable onShipPlaced;
    private ShotListener onShot;

    public BoardPanel(Board board, Mode mode) {
        this.board = board;
        this.mode = mode;
        setOpaque(true);
        setBackground(PAPER);

        if(mode == Mode.DEFENSE) return;

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
                if (mode == Mode.PLACEMENT && SwingUtilities.isRightMouseButton(e)) {
                    rotate();
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    updateHover(e);
                    click();
                }
            }
        });
        
        if(mode == Mode.PLACEMENT){
            addMouseWheelListener(e -> rotate());
            getActionMap().put("rotate", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    rotate();
                }
            });
        }
    }

    public void setBoard(Board board){
        this.board = board;
        this.selectedShip = null;
        repaint();
    }

    public void setSelectedShip(ShipType type) {
        this.selectedShip = type;
        repaint();
    }

    public void setOnShipPlaced(Runnable r) {
        this.onShipPlaced = r;
    }

    public void setOnShot(ShotListener l) {
        this.onShot = l;
    }

    public void setAttackEnabled(boolean b) {
        this.attackEnabled = b;
        repaint();
    }

    private int rotation(){
        return horizontal ? 0 : 1;
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
        boolean inside = e.getX() >= offsetX() && e.getY() >= offsetY() && col < GRID_SIZE && row < GRID_SIZE;
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

    private void click(){
        if(hoverRow < 0) return;

        if(mode == Mode.PLACEMENT){
            if(selectedShip == null || !board.CanPlace(selectedShip, hoverRow, hoverCol, rotation())) return;
            try{
                board.AddShip(selectedShip, new Point(hoverRow, hoverCol), rotation());
            }catch(Exception e){
                return;
            }
            selectedShip = null;
            repaint();
            if(onShipPlaced != null) onShipPlaced.run();
        }else if(mode == Mode.ATTACK){
            if(!attackEnabled || onShot == null) return;
            Cell[][] b = board.getBoard();
            if(b[hoverRow][hoverCol].getPlayerHit() != 0) return;
            onShot.onShot(hoverRow, hoverCol);
        }
    }

    private void hatchCell(Graphics2D g2, int x, int y, int size){
        Shape oldClip = g2.getClip();
        Stroke oldStroke = g2.getStroke();

        g2.clipRect(x, y, size, size);
        g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        for(int d = -size; d < 2 * size; d += 8){
            g2.drawLine(x + d, y + size, x + d + size, y);
        }

        g2.setClip(oldClip);
        g2.setStroke(oldStroke);
    }

    private void drawX(Graphics2D g2, int x, int y, int size, Color color){
        int m = size / 5;
        g2.setColor(color);
        g2.setStroke(new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(x + m, y + m, x + size - m, y + size - m);
        g2.drawLine(x + size - m, y + m, x + m, y + size - m);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int cs = cellSize();
        int ox = offsetX(), oy = offsetY();

        for(int r = 0; r < GRID_SIZE; r++){
            for(int c = 0; c < GRID_SIZE; c++){
                Cell[][] b =  board.getBoard();
                Cell cell = b[r][c];
                int x = ox + c * cs, y = oy + r * cs;

                if(mode != Mode.ATTACK && cell.getCell() == 1){
                    g2.setColor(SHIP);
                    hatchCell(g2, x, y, cs);
                }

                if(mode == Mode.DEFENSE && cell.getEnemyHit() == 1){
                    g2.setColor(BAD);
                    int d = cs / 3;
                    g2.fillOval(x + (cs - d) / 2, y + (cs - d) / 2, d, d);
                }

                if(mode == Mode.ATTACK && cell.getPlayerHit() != 0){
                    drawX(g2, x, y, cs, cell.getPlayerHit() == 2 ? OK : BAD);
                }
            }
        }

        if (mode == Mode.PLACEMENT && selectedShip != null && hoverCol >= 0) {
            g2.setColor(board.CanPlace(selectedShip, hoverRow, hoverCol, rotation()) ? OK : BAD);
            for(int i = 0; i < selectedShip.getLength(); i++) {
                int c = horizontal ? hoverCol + i : hoverCol;
                int r = horizontal ? hoverRow : hoverRow + i;
                if (c < GRID_SIZE && r < GRID_SIZE) {
                    hatchCell(g2, ox + c * cs, oy + r * cs, cs);
                }
            }
        }

        Cell[][] b = board.getBoard();
        if(mode == Mode.ATTACK && attackEnabled && hoverCol >= 0 && b[hoverRow][hoverCol].getPlayerHit() == 0){
            g2.setColor(GRID);
            g2.setStroke(new BasicStroke(3f));
            g2.drawRect(ox + hoverCol * cs + 3, oy + hoverRow * cs + 3, cs - 6, cs - 6);
        }

        g2.setColor(GRID);
        g2.setStroke(new BasicStroke(3.5f));
        int size = cs * GRID_SIZE;
        for(int i = 0; i <= GRID_SIZE; i++) {
            g2.drawLine(ox + i * cs, oy, ox + i * cs, oy + size);
            g2.drawLine(ox, oy + i * cs, ox + size, oy + i * cs);
        }

        g2.dispose();
    }

}
