package main.ui;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

public class SketchProfileIcon implements Icon{

    private final Color ink = new Color(35, 75, 155);

    @Override
    public int getIconWidth(){
        return 36;
    }

    @Override
    public int getIconHeight(){
        return 36;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y){
        Graphics2D g2 = (Graphics2D) g.create();

        g2.translate(x, y);
        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setColor(ink);
        g2.setStroke(new BasicStroke(
            1f,
            BasicStroke.CAP_ROUND,
            BasicStroke.JOIN_ROUND
        ));

        Ellipse2D head1 = new Ellipse2D.Float(12, 3, 12, 13);
        Ellipse2D head2 = new Ellipse2D.Float(11, 4, 13, 12);

        g2.draw(head1);
        g2.draw(head2);

        Path2D shoulders1 = new Path2D.Float();
        shoulders1.moveTo(5, 32);
        shoulders1.curveTo(5, 23, 11, 19, 18, 19);
        shoulders1.curveTo(25, 19, 31, 24, 31, 32);

        Path2D shoulders2 = new Path2D.Float();
        shoulders2.moveTo(6, 33);
        shoulders2.curveTo(7, 24, 12, 20, 18, 20);
        shoulders2.curveTo(26, 20, 30, 25, 30, 33);

        g2.draw(shoulders1);
        g2.draw(shoulders2);

        g2.dispose();
    }
}
