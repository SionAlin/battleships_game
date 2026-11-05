package main.ui;

import main.model.*;
import main.database.*;
import java.io.*;
import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

public class ShipPlacerMenu extends PaperPanel{
 
    private double angle = 0;
    private double phase = Math.random() * Math.PI * 2;
    private static final Font HEADER_FONT = new Font(Font.MONOSPACED, Font.BOLD, 45);
    private static final Font LIST_FONT = new Font(Font.MONOSPACED, Font.BOLD, 18);

    private final Color ink = new Color(35, 75, 155);
    
    private final SketchLabel subTitle = new SketchLabel("");
    private final SketchLabel errorLabel = new SketchLabel("Position Your Ships!");
    private final MainMenu menu;

    private final Timer rotationTimer = new Timer(40, e -> {
        phase += 0.025;
        angle = 1.02 * Math.sin(phase);
        ShipPlacerMenu.this.repaint();
    });

    private SketchButton readyButton;
    private SketchButton[] buttons;

    public ShipPlacerMenu(MainMenu menu){
        this.menu = menu;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        SketchLabel title = new SketchLabel("Ship Placement");
        title.setFont(HEADER_FONT);
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 3, 0));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        subTitle.setFont(LIST_FONT);
        subTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        subTitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
 
        top.add(title, BorderLayout.NORTH);
        top.add(subTitle, BorderLayout.SOUTH);

        JPanel body = new JPanel(new GridBagLayout());
        body.setOpaque(false);

        BoardPanel boardSection = new BoardPanel();
        boardSection.setPreferredSize(new Dimension(450, 450));
    
        JPanel borderHolder = SketchPanel();        
        borderHolder.setOpaque(false);
        borderHolder.setPreferredSize(new Dimension(500, 500));
        borderHolder.add(boardSection, new GridBagConstraints());

        JPanel shipsSection = BuildShipsPanel(boardSection);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.weightx = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 20);
        body.add(borderHolder, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        body.add(shipsSection, gbc);

        add(top, BorderLayout.NORTH);
        add(body, BorderLayout.CENTER);
        add(BuildBottomBar(menu), BorderLayout.SOUTH);

        rotationTimer.start();
    }
    
    private JPanel BuildShipsPanel(BoardPanel board){
        String[] names = {"Carrier", "Battleship", "Destroyer", "Submarine", "Patrolboat"};
        int[] lengths = {5, 4, 3, 3, 2};

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        buttons = new SketchButton[names.length];
        int[] selected = {-1};

        for(int i = 0; i < names.length; i++){
            final int idx = i;
            buttons[i] = new SketchButton(names[i], () -> {
                if(!buttons[idx].isEnabled()) return;
                selected[0] = idx;
                board.setSelectedShipLength(lengths[idx]);
            });
            buttons[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            buttons[i].setMaximumSize(new Dimension(300, 60));

            panel.add(Box.createRigidArea(new Dimension(0, 15)));
            panel.add(buttons[i]);
        }

        board.setOnShipPlaced(() -> {
            if(selected[0] >= 0){
                buttons[selected[0]].setEnabled(false);
                selected[0] = -1;
                updateReady();
            }
        });
        
        return panel;
    }

    private JPanel BuildBottomBar(MainMenu menu){
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);

        errorLabel.setFont(LIST_FONT);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);

        readyButton = new SketchButton("READY", () -> {
            menu.getInGameMenu().Refresh();
            menu.Show("INGAME");
        });
        
        bar.add(errorLabel, BorderLayout.CENTER);
        bar.add(readyButton, BorderLayout.EAST);

        return bar;
    }
    
    private void updateReady(){
        if(readyButton == null || buttons == null) return;

        boolean allPlaced = true;
        for(SketchButton b : buttons){
            if(b.isEnabled()){
                allPlaced = false;
                break;
            }
        }

        readyButton.setEnabled(allPlaced);
        if(allPlaced)
            errorLabel.setText("");
    }

    public void Refresh(){
        MatchRecord m = menu.getMatchRecord();
        if(m == null) return;
        subTitle.setText(m.getPlayerOne().getPlayerName() + " v.s. " + m.getPlayerTwo().getPlayerName());
        updateReady();
    }

    private JPanel SketchPanel(){
        JPanel section = new JPanel(new GridBagLayout()){
            
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
        };
        
        section.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        return section;
    }
}
