package main.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class PaperPanel extends JPanel{
    
    private int offsetX = 0;
    private int offsetY = 0;
    private static final Color PAPER = new Color(253, 252, 246);
    private static final Color GRID = new Color(150, 190, 225, 115);
    private static final Color MARGIN = new Color(210, 95, 100, 150);
    private static final int GRID_SIZE = 25;
    private static final int LEFT_MARGIN = 65;

    public PaperPanel(){
        setOpaque(true);
        setBackground(PAPER);
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e){
                offsetX = (getWidth() - e.getX()) / 20;
                offsetY = (getHeight() - e.getY()) / 20;
                repaint();
            }            
        });
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setColor(PAPER);
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setColor(GRID);
        g2.setStroke(new BasicStroke(1f));

        for(int x = Math.floorMod(offsetX, GRID_SIZE) - GRID_SIZE; x < getWidth(); x += GRID_SIZE){
            g2.drawLine(x, 0, x, getHeight());
        }

        for(int y = Math.floorMod(offsetY, GRID_SIZE) - GRID_SIZE; y < getHeight(); y += GRID_SIZE){
            g2.drawLine(0, y, getWidth(), y);
        }

        g2.setColor(MARGIN);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(offsetX + LEFT_MARGIN, 0, offsetX + LEFT_MARGIN, getHeight());
        g2.dispose();
    }

}
