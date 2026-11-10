package main.ui;

import main.model.*;
import main.database.*;
import main.logic.*;
import main.logic.player.*;
import main.ui.design.*;
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

public class InGameMenu extends PaperPanel implements MatchView{
    
    private static final Font HEADER_FONT = new Font(Font.MONOSPACED, Font.BOLD, 45);
    private static final Font LIST_FONT = new Font(Font.MONOSPACED, Font.BOLD, 20);

    private final Color ink = new Color(35, 75, 155);

    private final SketchLabel title = new SketchLabel("");
    
    private final SketchLabel namePlayerOneHitStatus = new SketchLabel("");
    private final SketchLabel statsPlayerOneHitStatus = new SketchLabel("");
    private final SketchLabel ratioPlayerOneHitStatus = new SketchLabel("");

    private final SketchLabel namePlayerTwoHitStatus = new SketchLabel("");
    private final SketchLabel statsPlayerTwoHitStatus = new SketchLabel("");
    private final SketchLabel ratioPlayerTwoHitStatus = new SketchLabel("");

    private final MainMenu menu;
    private BoardPanel hitBoardSection, yourBoardSection;

    private SketchButton menuButton;
    
    private LocalMatchController controller;
    private MatchRecord match = null;

    private JPanel body;
    private JPanel endPanel;
    private JPanel bottom;

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
        hitBoardSection.setOnShot((row, col) -> {
            if(controller != null) controller.playerShot(row, col);
        });
        
        JPanel hitBorderHolder = new SketchPanel();        
        hitBorderHolder.setPreferredSize(new Dimension(500, 500));
        hitBorderHolder.add(hitBoardSection, new GridBagConstraints());

        yourBoardSection = new BoardPanel(new Board(), BoardPanel.Mode.DEFENSE);
        yourBoardSection.setPreferredSize(new Dimension(250, 250));

        JPanel yourBorderHolder = new SketchPanel();
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
    }
    
    private JPanel EndPanel(){
        JPanel endSection = new JPanel(new BorderLayout());
        endSection.setOpaque(false);
        
        title.setText("WINNER");
       
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
        
        menuButton = new SketchButton("Main Menu", () -> {
            menu.getProfileMenu().RefreshAll();
            menu.getLeaderboardMenu().Refresh();
            menu.Show("HOME");
        });
        
        endSection.add(playersRaw, BorderLayout.CENTER);

        namePlayerOneHitStatus.setVisible(false);
        namePlayerTwoHitStatus.setVisible(false);
        bottom.add(menuButton, BorderLayout.CENTER);
        
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
        
        JPanel boardSection = new JPanel(new GridBagLayout());
        boardSection.setOpaque(false);

        JPanel yourBoard = new BoardPanel(player.getPlayerBoard(), BoardPanel.Mode.DEFENSE);
        yourBoard.setPreferredSize(new Dimension(250, 250));

        JPanel boarderHolder = new SketchPanel();
        boarderHolder.setOpaque(false);
        boarderHolder.setPreferredSize(new Dimension(300, 300));
        boarderHolder.add(yourBoard, new GridBagConstraints());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 0);
        boardSection.add(boarderHolder, gbc);

        section.add(nameLabel, BorderLayout.NORTH);
        section.add(boardSection, BorderLayout.CENTER);
        return section;
    }

    private JPanel BuildBottomBar(MainMenu menu){
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);
        
        JPanel playerOneSection = new JPanel(new BorderLayout());
        playerOneSection.setOpaque(false);

        namePlayerOneHitStatus.setFont(LIST_FONT);
        namePlayerOneHitStatus.setHorizontalAlignment(SwingConstants.CENTER);

        statsPlayerOneHitStatus.setFont(LIST_FONT);
        statsPlayerOneHitStatus.setHorizontalAlignment(SwingConstants.CENTER);

        ratioPlayerOneHitStatus.setFont(LIST_FONT);
        ratioPlayerOneHitStatus.setHorizontalAlignment(SwingConstants.CENTER);

        playerOneSection.add(namePlayerOneHitStatus, BorderLayout.NORTH);
        playerOneSection.add(statsPlayerOneHitStatus, BorderLayout.CENTER);
        playerOneSection.add(ratioPlayerOneHitStatus, BorderLayout.SOUTH);

        
        JPanel playerTwoSection = new JPanel(new BorderLayout());
        playerTwoSection.setOpaque(false);

        namePlayerTwoHitStatus.setFont(LIST_FONT);
        namePlayerTwoHitStatus.setHorizontalAlignment(SwingConstants.CENTER);

        statsPlayerTwoHitStatus.setFont(LIST_FONT);
        statsPlayerTwoHitStatus.setHorizontalAlignment(SwingConstants.CENTER);

        ratioPlayerTwoHitStatus.setFont(LIST_FONT);
        ratioPlayerTwoHitStatus.setHorizontalAlignment(SwingConstants.CENTER);

        playerTwoSection.add(namePlayerTwoHitStatus, BorderLayout.NORTH);
        playerTwoSection.add(statsPlayerTwoHitStatus, BorderLayout.CENTER);
        playerTwoSection.add(ratioPlayerTwoHitStatus, BorderLayout.SOUTH);

        bar.add(playerOneSection, BorderLayout.WEST);
        bar.add(playerTwoSection, BorderLayout.EAST);
        return bar;
    }
    
    public void StartMatch(boolean computerFirst){
        Refresh();
        controller = new LocalMatchController(
            menu.getPlayerPlayer(), 
            menu.getComputerPlayer(),
            match,
            this
        );
        controller.startMatch(computerFirst);
    }

    @Override
    public void setAttackEnabled(boolean enabled){
        hitBoardSection.setAttackEnabled(enabled);
    }

    @Override
    public void repaintBoards(){
        hitBoardSection.repaint();
        yourBoardSection.repaint();
    }

    @Override
    public void showEndScreen(){
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
            bottom.remove(menuButton);
            add(body, BorderLayout.CENTER);
            revalidate();
            repaint();
        }

        if(match == null) return;
        Player playerOne = menu.getPlayerOne();
        Player playerTwo = menu.getPlayerTwo();
        title.setText(playerOne.getPlayerName() + " v.s. " + playerTwo.getPlayerName());

        Board mine = menu.getPlayerPlayer().getPlayerBoard();
        hitBoardSection.setBoard(mine);
        yourBoardSection.setBoard(mine);
        
        refreshStats();

        String playerOneHitRatio = String.format("Hit Ratio: %.0f%%", playerOne.HitRatio() * 100);
        namePlayerOneHitStatus.setText(playerOne.getPlayerName());
        namePlayerOneHitStatus.setVisible(true);
        statsPlayerOneHitStatus.setText("C./W. Hits: " + playerOne.getCorrectHits() + "/" + playerOne.getWrongHits());
        ratioPlayerOneHitStatus.setText(playerOneHitRatio);

        String playerTwoHitRatio = String.format("Hit Ratio: %.0f%%", playerTwo.HitRatio() * 100);
        namePlayerTwoHitStatus.setText(playerTwo.getPlayerName());
        namePlayerTwoHitStatus.setVisible(true);
        statsPlayerTwoHitStatus.setText("C./W. Hits: " + playerTwo.getCorrectHits() + "/" + playerTwo.getWrongHits());
        ratioPlayerTwoHitStatus.setText(playerTwoHitRatio);
    }

    @Override
    public void refreshStats(){
        Player playerOne = menu.getPlayerOne();
        Player playerTwo = menu.getPlayerTwo();
        
        namePlayerOneHitStatus.setText(playerOne.getPlayerName());
        namePlayerOneHitStatus.setVisible(true);
        statsPlayerOneHitStatus.setText("C./W. Hits: " + playerOne.getCorrectHits() + "/" + playerOne.getWrongHits());
        ratioPlayerOneHitStatus.setText(String.format("Hit Ratio: %.0f%%", playerOne.HitRatio() * 100));
        
        namePlayerTwoHitStatus.setText(playerTwo.getPlayerName());
        namePlayerTwoHitStatus.setVisible(true);
        statsPlayerTwoHitStatus.setText("C./W. Hits: " + playerTwo.getCorrectHits() + "/" + playerTwo.getWrongHits());
        ratioPlayerTwoHitStatus.setText(String.format("Hit Ratio: %.0f%%", playerTwo.HitRatio() * 100));
    }

}
