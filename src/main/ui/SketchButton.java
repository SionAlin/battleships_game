package main.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class SketchButton extends JComponent{

    private double angle = 0;
    private double phase = Math.random() * Math.PI * 2;

    private String text;
    private final Runnable action;

    private boolean hovered = false;
    private boolean pressed = false;

    private SketchProfileIcon icon;

    private final Color ink = new Color(35, 75, 155);
    private final Timer rotationTimer = new Timer(40, e -> {
        phase += 0.025;
        angle = 1.02 * Math.sin(phase);
        repaint();
    });

    public SketchButton(String text, Runnable action){
        this.text = text;
        this.action = action;

        setPreferredSize(new Dimension(300, 55));
        setMinimumSize(new Dimension(200, 45));
        setMaximumSize(new Dimension(300, 55));

        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setFocusable(true);
        rotationTimer.start();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e){
                hovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e){
                hovered = false;
                pressed = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e){
                if(SwingUtilities.isLeftMouseButton(e)){
                    pressed = true;
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e){
                boolean activate = pressed && contains(e.getPoint());
                pressed = false;
                repaint();

                if(activate && action != null){
                    action.run();
                }

                if(!isEnabled()) return;
                action.run();
            }
        });
    }

    public void setIcon(SketchProfileIcon icon){
        this.icon = icon;
        repaint();
    }
    
    public void setText(String text){
        this.text = text;
    }

    public String getText(){
        return text;
    }
    
    @Override
    public void setEnabled(boolean status){
        super.setEnabled(status);
        setCursor(Cursor.getPredefinedCursor(status ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
        g2.rotate(
            Math.toRadians(angle),
            getWidth() / 2.0,
            getHeight() / 2.0
        );

        int w = getWidth();
        int h = getHeight();
        
        if(icon != null){
            int iconX = (w - icon.getIconWidth()) / 2;
            int iconY = (h - icon.getIconHeight()) / 2;
            icon.paintIcon(this, g2, iconX, iconY);
        }

        if(pressed){
            g2.translate(1, 2);
        }
        
        RoundRectangle2D outline = new RoundRectangle2D.Float(
            4,4,
            getWidth() - 8,
            getHeight() - 8,
            5, 5
        );

        if(hovered){
            Shape oldClip = g2.getClip();
            g2.clip(outline);

            g2.setColor(new Color(35, 75, 155, 110));
            g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            for(int x = -h; x < w + h; x += 15){
                g2.drawLine(x, h, x + h, 0);
            }

            g2.setClip(oldClip);

        }

        g2.setColor(ink);
        g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(outline);

        if(!isEnabled()){
            g2.setColor(new Color(150, 150, 150));
        }

        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(10, 6, w - 7, 10);
        g2.drawLine(6, 11, w - 9, 13);

        g2.drawLine(w - 2, 8, w - 8, h - 10);
        g2.drawLine(w - 5, 9, w - 15 , h - 5);
        
        g2.drawLine(9, 8, 13, h - 2);
        g2.drawLine(5, 12, 9, h - 8);

        g2.drawLine(w - 4, h - 7, 9, h - 1);
        g2.drawLine(w - 7, h - 1, 5, h - 5);

        g2.setColor(ink);
        g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));

        FontMetrics fm = g2.getFontMetrics();
        int textX = (w - fm.stringWidth(text)) / 2;
        int textY = (h - fm.getHeight()) / 2 + fm.getAscent();

        g2.drawString(text, textX, textY);
        g2.dispose();
    }
}
