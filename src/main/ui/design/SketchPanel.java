package main.ui.design;

import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

public class SketchPanel extends JPanel{
    
    private static final Color ink = new Color(35, 75, 155);

    private double angle = 0;
    private double phase = Math.random() * Math.PI * 2;
    
    private final Timer rotationTimer = new Timer(40, e ->{
        phase += 0.025;
        angle = 1.02 * Math.sin(phase);
        repaint();
    });
    
    public SketchPanel(){
        this(new GridBagLayout());
    }

    public SketchPanel(LayoutManager layout){
        super(layout);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        addHierarchyListener(e -> {
            if((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0){
                if(isShowing()) rotationTimer.start();
                else rotationTimer.stop();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int w = getWidth();
        int h = getHeight();
        
        g2.rotate(
            Math.toRadians(angle),
            w / 2.0,
            h / 2.0
        );

        RoundRectangle2D outline = new RoundRectangle2D.Float(
            4, 4,
            w - 8,
            h - 8,
            5, 5
        );
        

        Shape oldClip = g2.getClip();
        g2.clip(outline);

        g2.setColor(ink);
        g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        for(int x = -h; x < w + h; x += 15){
            g2.drawLine(x, h, x + h, 0);
        }

        g2.drawLine(10, 6, w - 7, 10);
        g2.drawLine(6, 11, w - 9, 13);

        g2.drawLine(w - 2, 8, w - 8, h - 10);
        g2.drawLine(w - 5, 9, w - 15 , h - 5);
        
        g2.drawLine(9, 8, 13, h - 2);
        g2.drawLine(5, 12, 9, h - 8);

        g2.drawLine(w - 4, h - 7, 9, h - 1);
        g2.drawLine(w - 7, h - 1, 5, h - 5);

        g2.setClip(oldClip);
        g2.dispose();
    }

}

