package main.ui;

import main.model.*;
import main.logic.ShipPlacer;
import main.logic.player.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;

public class MainMenu{
    
    private JFrame Frame;
    private CardLayout Cards = new CardLayout();
    private JPanel root = new JPanel(Cards);

    private ShipPlacerMenu shipPlacerMenu;
    private InGameMenu inGameMenu;

    private PlayerProfile selected;
    private PlayerProfile playerProfile, computerProfile;
    private HumanPlayer playerPlayer = null;
    private AIPlayer computerPlayer = null;
    private MatchRecord matchRecord = null;

    public MainMenu(){
        int width = 1200;
        int height = 800;
        this.Frame = new JFrame("Battleships");
        this.Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.Frame.setSize(width, height);
        this.Frame.setLocationRelativeTo(null);
        
        ImageIcon icon = new ImageIcon("./src/resources/BattleshipsIcon.png");
        this.Frame.setIconImage(icon.getImage());

        root.add(BuildHome(), "HOME");
        root.add(new ProfileMenu(this), "PROFILE");
        root.add(new LeaderboardMenu(this), "LEADERBOARD");
        root.add(new PlayerVSComputerMenu(this), "PVC");
        root.add(new PlayerVSPlayerMenu(this), "PVP");

        shipPlacerMenu = new ShipPlacerMenu(this);
        root.add(shipPlacerMenu, "SHIPPLACER");
        
        inGameMenu = new InGameMenu(this);
        root.add(inGameMenu, "INGAME");

        this.Frame.setResizable(false);
        this.Frame.setContentPane(root);
        this.Frame.setVisible(true);
    }
    
    public void setSelectedProfile(PlayerProfile profile){
        this.selected = profile;
    }

    public void setPlayer(PlayerProfile profile){
        this.playerProfile = profile;
        this.playerPlayer = new HumanPlayer(profile.getPlayerName());
    }

    public void setComputer(PlayerProfile profile){
        this.computerProfile = profile;
        this.computerPlayer = new AIPlayer(profile.getPlayerName());
    }

    public PlayerProfile getSelectedProfile(){
        return selected;
    }
    
    public PlayerProfile getPlayerProfile(){
        return playerProfile;
    }

    public HumanPlayer getPlayerPlayer(){
        return playerPlayer;
    }

    public PlayerProfile getComputerProfile(){
        return computerProfile;
    }

    public AIPlayer getComputerPlayer(){
        return computerPlayer;
    }
    
    public MatchRecord getMatchRecord(){
        return matchRecord;
    }

    public void StartMatch(PlayerProfile profileOne, PlayerProfile profileTwo){
        matchRecord = new MatchRecord(profileOne, profileTwo);
    }

    public ShipPlacerMenu getShipPlacerMenu(){
        return shipPlacerMenu;
    }
    
    public InGameMenu getInGameMenu(){
        return inGameMenu;
    }

    public void Show(String screen){
        Cards.show(root, screen);
    }

    private PaperPanel BuildHome(){
        PaperPanel background = new PaperPanel();
        background.setLayout(new BorderLayout());

        SketchButton profileButton = new SketchButton("", () -> Show("PROFILE"));
        profileButton.setIcon(new SketchProfileIcon());
        profileButton.setPreferredSize(new Dimension(60, 60));
        profileButton.setToolTipText("Profile");
        SketchButton pvcButton = new SketchButton("PLAYER VS COMPUTER", () -> Show("PVC"));
        SketchButton pvpButton = new SketchButton("PLAYER VS PLAYER", () -> Show("PVP"));
        SketchButton leaderboardButton = new SketchButton("LEADERBOARD", () -> Show("LEADERBOARD"));
        
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
        return background;
    }

}
