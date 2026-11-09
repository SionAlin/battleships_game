package main.ui.design;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.RoundRectangle2D;

public class SketchTextArea extends JPanel{

    private final JTextArea area;
    private final Color ink = new Color(35, 75, 155);
    private final Color bkg = new Color(35, 75, 155, 70);
    private final Font font = new Font(Font.MONOSPACED, Font.BOLD, 18);
    private boolean focused = false;

    public SketchTextArea(){
        this(1, 15);
    }

    public SketchTextArea(int rows, int columns){
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));

        area = new JTextArea(rows, columns);
        area.setOpaque(false);
        area.setBorder(null);
        area.setFont(font);
        area.setForeground(ink);
        area.setCaretColor(ink);
        area.setSelectionColor(bkg);
        area.setSelectedTextColor(ink);
        area.getInputMap().put(
            KeyStroke.getKeyStroke("ENTER"),
            "none"
        );
        add(area, BorderLayout.CENTER);
    
        area.addFocusListener(new FocusAdapter(){
            
            @Override
            public void focusGained(FocusEvent e){
                focused = true;
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e){
                focused = false;
                repaint();
            }

        });

        addMouseListener(new MouseAdapter(){
            
            @Override
            public void mousePressed(MouseEvent e){
                area.requestFocusInWindow();
            }
        });
    }

    public String getText(){
        return area.getText();
    }

    public void setText(String text){
        area.setText(text);
    }

    public void setEditable(boolean editable){
        area.setEditable(editable);
    }

    public JTextArea getTextArea(){
        return area;
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        RoundRectangle2D outline = new RoundRectangle2D.Float(
            4, 4,
            w - 8,
            h - 8,
            5, 5
        );

        if(focused){
            Shape oldClip = g2.getClip();
            g2.clip(outline);

            g2.setColor(new Color(35, 75, 155, 28));
            g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            for(int x = -h; x < w + h; x += 15){
                g2.drawLine(x, h, x + h, 0);
            }

            g2.setClip(oldClip);
        }

        g2.setColor(ink);
        g2.setStroke(new BasicStroke(focused ? 2.6f : 2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(outline);

        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(10, 6, w - 7, 10);
        g2.drawLine(6, 11, w - 9, 13);
        
        g2.drawLine(w - 2, 8, w - 8, h - 10);
        g2.drawLine(w - 5, 9, w - 15, h - 5);

        g2.drawLine(9, 8, 13, h - 2);
        g2.drawLine(5, 12, 9, h - 8);

        g2.drawLine(w - 4, h - 7, 9, h - 1);
        g2.drawLine(w - 7, h - 1, 5, h - 5);

        g2.dispose();
    }
}
