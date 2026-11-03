package main.ui;

import main.model.*;
import main.database.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

public class PlayerVSPlayerMenu extends PaperPanel{
    
    private double angle = 0;
    private double phase = Math.random() * Math.PI * 2;
    private static final Font HEADER_FONT = new Font(Font.MONOSPACED, Font.BOLD, 45);
    private static final Font LIST_FONT = new Font(Font.MONOSPACED, Font.BOLD, 18);

    private final Color ink = new Color(35, 75, 155);

    private final MatchRepository repository = new MatchRepository();
    private final DefaultListModel<PlayerProfile> listModel = new DefaultListModel<>();
    private final JList<PlayerProfile> playersList = new JList<>(listModel);
    private final SketchLabel selectedProfileLabel = new SketchLabel("");

    private final Timer rotationTimer = new Timer(40, e -> {
        phase += 0.025;
        angle = 1.02 * Math.sin(phase);
        PlayerVSPlayerMenu.this.repaint();
    });
    private boolean selectedHostPanel = false;
    private boolean selectedJoinPanel = false;

    public PlayerVSPlayerMenu(MainMenu menu){
    
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        SketchLabel title = new SketchLabel("Player vs. Player");
        title.setFont(HEADER_FONT);
        title.setBorder(BorderFactory.createEmptyBorder(150, 0, 20, 0));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(title, BorderLayout.CENTER);
        
        addComponentListener(new ComponentAdapter(){
            @Override
            public void componentShown(ComponentEvent e){ Refresh(menu); }
        });

        Refresh(menu);
        selectedProfileLabel.setFont(LIST_FONT);
        selectedProfileLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        selectedProfileLabel.setHorizontalAlignment(SwingConstants.CENTER);
        top.add(selectedProfileLabel, BorderLayout.SOUTH);
        
        JPanel body = new JPanel(new GridBagLayout());
        body.setOpaque(false);

        JPanel hostSection = BuildHostSection();
        JPanel joinSection = BuildJoinSection();
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 0, 0, 20);
        body.add(hostSection, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        body.add(joinSection, gbc);

        add(top, BorderLayout.NORTH);
        add(body, BorderLayout.CENTER);
        add(BuildBottomBar(menu), BorderLayout.SOUTH);

        rotationTimer.start();
    }
    
    private void Refresh(MainMenu menu){
        String selectedProfileText = menu.getSelectedProfile() == null ? "Please select a profile first!" : "You play as: " + menu.getSelectedProfile().getPlayerName();
        selectedProfileLabel.setText(selectedProfileText);
    }
    
    private JPanel BuildHostSection(){
      
        JPanel section = SketchPanel(true);
        section.setBorder(BorderFactory.createEmptyBorder(40,80,40,80));
        section.setOpaque(false);
        section.addMouseListener(new MouseAdapter() {
            @Override 
            public void mouseEntered(MouseEvent e){
                selectedHostPanel = true;
                selectedJoinPanel = false;   
                section.repaint();
                PlayerVSPlayerMenu.this.repaint();
            }
            @Override
            public void mouseExited(MouseEvent e){
                selectedHostPanel = false;
                section.repaint();
                PlayerVSPlayerMenu.this.repaint();
            }
        });

        JPanel codePanel = new JPanel(new BorderLayout());
        codePanel.setOpaque(false);

        SketchLabel hostLabel = new SketchLabel("Host Code");
        hostLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        hostLabel.setHorizontalAlignment(SwingConstants.CENTER);
        SketchLabel hostCode = new SketchLabel("A6RTH3");
        hostCode.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        hostCode.setHorizontalAlignment(SwingConstants.CENTER);

        codePanel.add(hostLabel, BorderLayout.NORTH);
        codePanel.add(hostCode, BorderLayout.CENTER);

        SketchButton hostButton = new SketchButton("HOST", () -> {
            System.out.println("Start hosting ...");
        });
        hostButton.setPreferredSize(new Dimension(120, 60));

        SketchLabel status = new SketchLabel("Waiting for opponent ...");
        
        section.add(codePanel, BorderLayout.NORTH);
        section.add(hostButton, BorderLayout.CENTER);
        section.add(status, BorderLayout.SOUTH);
        return section;
    }

    private JPanel BuildJoinSection(){
        
        JPanel section = SketchPanel(false);
        section.setBorder(BorderFactory.createEmptyBorder(40,80,40,80));
        section.setOpaque(false);
        section.addMouseListener(new MouseAdapter() {
            @Override 
            public void mouseEntered(MouseEvent e){
                selectedHostPanel = false;
                selectedJoinPanel = true;   
                section.repaint();
                PlayerVSPlayerMenu.this.repaint();
            }
            @Override
            public void mouseExited(MouseEvent e){
                selectedJoinPanel = false;
                section.repaint();
                PlayerVSPlayerMenu.this.repaint();
            }
        });
        
        JPanel codePanel = new JPanel(new BorderLayout());
        codePanel.setOpaque(false);

        SketchLabel joinLabel = new SketchLabel("Join Code");
        joinLabel.setHorizontalAlignment(SwingConstants.CENTER);

        SketchTextArea joinCode = new SketchTextArea();

        codePanel.add(joinLabel, BorderLayout.NORTH);
        codePanel.add(joinCode, BorderLayout.CENTER);

        SketchButton joinButton = new SketchButton("JOIN", () -> {
            System.out.println("Joining ...");
        });
        joinButton.setPreferredSize(new Dimension(120, 60));

        SketchLabel status = new SketchLabel("Searching for opponent ...");
    
        section.add(codePanel, BorderLayout.NORTH);
        section.add(joinButton, BorderLayout.CENTER);
        section.add(status, BorderLayout.SOUTH);
        return section;
    }

    private JPanel BuildBottomBar(MainMenu menu){
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);

        SketchButton backButton = new SketchButton("BACK", () -> menu.Show("HOME"));
        SketchButton playButton = new SketchButton("READY", () -> {
            System.out.println("Play");
        });

        bar.add(playButton, BorderLayout.EAST);
        bar.add(backButton, BorderLayout.WEST);

        return bar;
    }

    private JPanel SketchPanel(boolean isHost){
        JPanel section = new JPanel(new BorderLayout(0, 15)){
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
                
                boolean active = isHost ? selectedHostPanel : selectedJoinPanel; 

                if(active){
                    Shape oldClip = g2.getClip();
                    g2.clip(outline);

                    g2.setColor(new Color(35, 75, 155, 110));
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
                }

                g2.dispose();
            }
        };

        return section;
    }
}
