import javax.swing.*;
import java.awt.*;

public class PaperPanel extends JPanel{

    private static final Color PAPER = new Color(253, 252, 246);
    private static final Color GRID = new Color(150, 190, 225, 115);
    private static final Color MARGIN = new Color(210, 95, 100, 150);
    private static final int GRID_SIZE = 25;
    private static final int LEFT_MARGIN = 65;

    public PaperPanel(){
        setOpaque(true);
        setBackground(PAPER);
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setColor(PAPER);
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setColor(GRID);
        g2.setStroke(new BasicStroke(1f));

        for(int x = 0; x < getWidth(); x += GRID_SIZE){
            g2.drawLine(x, 0, x, getHeight());
        }

        for(int y = 0; y < getHeight(); y += GRID_SIZE){
            g2.drawLine(0, y, getWidth(), y);
        }

        g2.setColor(MARGIN);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(LEFT_MARGIN, 0, LEFT_MARGIN, getHeight());
        g2.dispose();
    }

}
