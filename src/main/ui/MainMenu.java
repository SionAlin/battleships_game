package main.ui;

import java.io.*;
import java.awt.*;
import javax.swing.*;

public class MainMenu{
    
    private JFrame Frame;

    public MainMenu(){
        int width = 1200;
        int height = 800;
        this.Frame = new JFrame("Battleships");
        this.Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.Frame.setSize(width, height);
        this.Frame.setLocationRelativeTo(null);
        
        ImageIcon icon = new ImageIcon("./src/resources/BattleshipsIcon.png");
        this.Frame.setIconImage(icon.getImage());

        PaperPanel background = new PaperPanel();
        background.setLayout(new BorderLayout());
        
        this.Frame.setContentPane(background);

        SketchButton profileButton = new SketchButton("", () -> {
            System.out.println("Profile");
        });

        profileButton.setIcon(new SketchProfileIcon());
        profileButton.setPreferredSize(new Dimension(60, 60));
        profileButton.setToolTipText("Profile");

        JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topRightPanel.setOpaque(false);
        topRightPanel.add(profileButton);
        background.add(topRightPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        ImageIcon originalIcon = new ImageIcon("./src/resources/BattleshipsLogo.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(600, 300, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(scaledImage);
        JLabel imageLabel = new JLabel(resizedIcon);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        SketchButton pvcButton = new SketchButton("PLAYER VS COMPUTER", () -> {
            System.out.println("Player vs Computer");
        });
        SketchButton pvpButton = new SketchButton("PLAYER VS PLAYER", () -> {
            System.out.println("Player vs Player");
        });
        SketchButton leaderboardButton = new SketchButton("LEADERBOARD", () -> {
            System.out.println("Leaderboard");
        });

        Dimension mainButtonsDimension = new Dimension(400, 600);

        pvcButton.setMaximumSize(mainButtonsDimension);
        pvpButton.setMaximumSize(mainButtonsDimension);
        leaderboardButton.setMaximumSize(mainButtonsDimension);

        pvcButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        pvpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        leaderboardButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(imageLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        centerPanel.add(pvcButton);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        centerPanel.add(pvpButton);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        centerPanel.add(leaderboardButton);
        centerPanel.add(Box.createVerticalGlue());

        background.add(centerPanel, BorderLayout.CENTER);

        this.Frame.setVisible(true);
    }
}
