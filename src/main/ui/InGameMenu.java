package main.ui;

import main.model.*;
import main.database.*;
import main.logic.player.*;
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

public class InGameMenu extends PaperPanel{
    
    private double angle = 0;
    private double phase = Math.random() * Math.PI * 2;
    private static final Font HEADER_FONT = new Font(Font.MONOSPACED, Font.BOLD, 45);
    private static final Font LIST_FONT = new Font(Font.MONOSPACED, Font.BOLD, 20);

    private final Color ink = new Color(35, 75, 155);

    private final SketchLabel title = new SketchLabel("");
    
    private final SketchLabel namePlayerHitStatus = new SketchLabel("");
    private final SketchLabel statsPlayerHitStatus = new SketchLabel("");
    private final SketchLabel ratioPlayerHitStatus = new SketchLabel("");

    private final SketchLabel nameComputerHitStatus = new SketchLabel("");
    private final SketchLabel statsComputerHitStatus = new SketchLabel("");
    private final SketchLabel ratioComputerHitStatus = new SketchLabel("");

    private final MainMenu menu;
    private BoardPanel hitBoardSection, yourBoardSection;
   
    private MatchRepository matchRepository = new MatchRepository();
    private PlayerProfileRepository playerProfileRepository = new PlayerProfileRepository();
    private MatchRecord match = null;

    private JPanel body;
    private JPanel endPanel;
    private JPanel bottom;

    private final Timer rotationTimer = new Timer(40, e -> {
        phase += 0.025;
        angle = 1.02 * Math.sin(phase);
        InGameMenu.this.repaint();
    });

    public InGameMenu(MainMenu menu){
        this.menu = menu;
        
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        title.setFont(HEADER_FONT);
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 3, 0));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(title, BorderLayout.NORTH);

        body = new JPanel(new GridBagLayout());
        body.setOpaque(false);

        hitBoardSection = new BoardPanel(new Board(), BoardPanel.Mode.ATTACK);
        hitBoardSection.setPreferredSize(new Dimension(450, 450));
    
        JPanel hitBorderHolder = SketchPanel();        
        hitBorderHolder.setOpaque(false);
        hitBorderHolder.setPreferredSize(new Dimension(500, 500));
        hitBorderHolder.add(hitBoardSection, new GridBagConstraints());

        yourBoardSection = new BoardPanel(new Board(), BoardPanel.Mode.DEFENSE);
        yourBoardSection.setPreferredSize(new Dimension(250, 250));

        hitBoardSection.setOnShot(this::playerShot);

        JPanel yourBorderHolder = SketchPanel();
        yourBorderHolder.setOpaque(false);
        yourBorderHolder.setPreferredSize(new Dimension(300, 300));
        yourBorderHolder.add(yourBoardSection, new GridBagConstraints());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.weightx = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 20);
        body.add(hitBorderHolder, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 0);
        body.add(yourBorderHolder, gbc);
        
        bottom = BuildBottomBar(menu);

        add(top, BorderLayout.NORTH);
        add(body, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        rotationTimer.start();
    }
    
    private JPanel EndPanel(){
        JPanel endSection = new JPanel(new BorderLayout());
        endSection.setOpaque(false);
        
        title.setText("WINNER");
        bottom.setVisible(false);
       
        SketchLabel winnerLabel = new SketchLabel(match.getWinner().getPlayerName());
        winnerLabel.setFont(HEADER_FONT);
        winnerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        endSection.add(winnerLabel, BorderLayout.NORTH);
        
        JPanel playerOneSection = BuildPlayerSection(match.getPlayerOne()); 
        JPanel playerTwoSection = BuildPlayerSection(match.getPlayerTwo());
        
        JPanel playersRaw = new JPanel(new GridBagLayout());
        playersRaw.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 0, 0, 20);
        playersRaw.add(playerOneSection, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        playersRaw.add(playerTwoSection, gbc);
        
        SketchButton menuButton = new SketchButton("Main Menu", () -> {
            menu.Show("HOME");
        });
        
        endSection.add(playersRaw, BorderLayout.CENTER);
        endSection.add(menuButton, BorderLayout.SOUTH);
        return endSection;
    }
    
    private JPanel BuildPlayerSection(PlayerProfile playerProfile){
        MatchRecord match = menu.getMatchRecord();
        PlayerProfile winner = match.getWinner();
        Player playerPlayer = menu.getPlayerPlayer();
        Player computer = menu.getComputerPlayer();

        Player player = playerPlayer.getPlayerName().equals(playerProfile.getPlayerName()) ? playerPlayer : computer;

        JPanel section = new JPanel(new BorderLayout());
        section.setOpaque(false);

        SketchLabel nameLabel = playerProfile.getPlayerName().equals(winner.getPlayerName()) ? new SketchLabel("(W) " + playerProfile.getPlayerName()) : new SketchLabel(playerProfile.getPlayerName());
        nameLabel.setFont(HEADER_FONT);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        SketchLabel statsLabel = new SketchLabel("C./W. Hits: " + player.getCorrectHits() + "/" + player.getWrongHits());
        statsLabel.setFont(LIST_FONT);
        statsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statsLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        String ratioText = String.format("Hit Ratio: %.0f%%", player.HitRatio() * 100);
        SketchLabel ratioLabel = new SketchLabel(ratioText);
        ratioLabel.setFont(HEADER_FONT);
        ratioLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ratioLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        section.add(nameLabel, BorderLayout.NORTH);
        section.add(statsLabel, BorderLayout.CENTER);
        section.add(ratioLabel, BorderLayout.SOUTH);

        return section;
    }

    private JPanel BuildBottomBar(MainMenu menu){
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);
        
        JPanel playerOneSection = new JPanel(new BorderLayout());
        playerOneSection.setOpaque(false);

        namePlayerHitStatus.setFont(LIST_FONT);
        namePlayerHitStatus.setHorizontalAlignment(SwingConstants.CENTER);

        statsPlayerHitStatus.setFont(LIST_FONT);
        statsPlayerHitStatus.setHorizontalAlignment(SwingConstants.CENTER);

        ratioPlayerHitStatus.setFont(LIST_FONT);
        ratioPlayerHitStatus.setHorizontalAlignment(SwingConstants.CENTER);

        playerOneSection.add(namePlayerHitStatus, BorderLayout.NORTH);
        playerOneSection.add(statsPlayerHitStatus, BorderLayout.CENTER);
        playerOneSection.add(ratioPlayerHitStatus, BorderLayout.SOUTH);

        
        JPanel playerTwoSection = new JPanel(new BorderLayout());
        playerTwoSection.setOpaque(false);

        nameComputerHitStatus.setFont(LIST_FONT);
        nameComputerHitStatus.setHorizontalAlignment(SwingConstants.CENTER);

        statsComputerHitStatus.setFont(LIST_FONT);
        statsComputerHitStatus.setHorizontalAlignment(SwingConstants.CENTER);

        ratioComputerHitStatus.setFont(LIST_FONT);
        ratioComputerHitStatus.setHorizontalAlignment(SwingConstants.CENTER);

        playerTwoSection.add(nameComputerHitStatus, BorderLayout.NORTH);
        playerTwoSection.add(statsComputerHitStatus, BorderLayout.CENTER);
        playerTwoSection.add(ratioComputerHitStatus, BorderLayout.SOUTH);

        bar.add(playerOneSection, BorderLayout.WEST);
        bar.add(playerTwoSection, BorderLayout.EAST);
        return bar;
    }
    
    private void playerShot(int row, int col){
        HumanPlayer me = menu.getPlayerPlayer();
        AIPlayer com = menu.getComputerPlayer();

        if(me.fire(com.getPlayerBoard(), row, col) == ShotResult.ALREADY_SHOT) return;
        hitBoardSection.repaint();
        Refresh();

        if(!com.getPlayerBoard().HasShipsLeft()){ finish(me, com, me.getPlayerName()); return; }

        hitBoardSection.setAttackEnabled(false);
        Timer t = new Timer(700, e -> {
            com.makeMove(me.getPlayerBoard());
            yourBoardSection.repaint();
            Refresh();
            if(!me.getPlayerBoard().HasShipsLeft()) finish(me, com, com.getPlayerName());
            else hitBoardSection.setAttackEnabled(true);
        });
        t.setRepeats(false);
        t.start();
    }

    private void finish(Player playerOne, Player playerTwo, String winner){
        try{
            match.setWinner(winner);
            matchRepository.create(match);
            playerProfileRepository.updateHits(playerOne);
            playerProfileRepository.updateHits(playerTwo);      
        }catch(SQLException e){
            e.printStackTrace();
        }

        hitBoardSection.setAttackEnabled(false);
        remove(body);
        endPanel = EndPanel();
        add(endPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void Refresh(){
        match = menu.getMatchRecord();

        if(endPanel != null){
            remove(endPanel);
            endPanel = null;
            add(body, BorderLayout.CENTER);
            bottom.setVisible(true);
            revalidate();
            repaint();
        }

        HumanPlayer player = menu.getPlayerPlayer();
        AIPlayer computer = menu.getComputerPlayer();

        if(match == null) return;
        title.setText(match.getPlayerOne().getPlayerName() + " v.s. " + match.getPlayerTwo().getPlayerName());

        Board mine = menu.getPlayerPlayer().getPlayerBoard();
        hitBoardSection.setBoard(mine);
        yourBoardSection.setBoard(mine);
        hitBoardSection.setAttackEnabled(true);

        String playerHitRatio = String.format("Hit Ratio: %.0f%%", player.HitRatio() * 100);
        namePlayerHitStatus.setText(player.getPlayerName());
        statsPlayerHitStatus.setText("C./W. Hits: " + player.getCorrectHits() + "/" + player.getWrongHits());
        ratioPlayerHitStatus.setText(playerHitRatio);

        String computerHitRatio = String.format("Hit Ratio: %.0f%%", computer.HitRatio() * 100);
        nameComputerHitStatus.setText(computer.getPlayerName());
        statsComputerHitStatus.setText("C./W. Hits: " + computer.getCorrectHits() + "/" + computer.getWrongHits());
        ratioComputerHitStatus.setText(computerHitRatio);
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
