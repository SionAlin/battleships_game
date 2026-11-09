package main.ui;

import main.model.*;
import main.database.*;
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

public class ShipPlacerMenu extends PaperPanel{
 
    private static final Font HEADER_FONT = new Font(Font.MONOSPACED, Font.BOLD, 45);
    private static final Font LIST_FONT = new Font(Font.MONOSPACED, Font.BOLD, 18);

    private final Color ink = new Color(35, 75, 155);
    
    private final SketchLabel subTitle = new SketchLabel("");
    private final SketchLabel errorLabel = new SketchLabel("Position Your Ships!");
    private final MainMenu menu;

    private BoardPanel boardSection;

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

        boardSection = new BoardPanel(new Board(), BoardPanel.Mode.PLACEMENT);
        boardSection.setPreferredSize(new Dimension(450, 450));
    
        JPanel borderHolder = new SketchPanel();        
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
        add(BuildBottomBar(), BorderLayout.SOUTH);
    }
    
    private JPanel BuildShipsPanel(BoardPanel board){
        ShipType[] types = ShipType.values();

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        buttons = new SketchButton[types.length];
        int[] selected = {-1};

        for(int i = 0; i < types.length; i++){
            final int idx = i;
            buttons[i] = new SketchButton(types[i].name(), () -> {
                if(!buttons[idx].isEnabled()) return;
                selected[0] = idx;
                board.setSelectedShip(types[idx]);
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

    private JPanel BuildBottomBar(){
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);

        errorLabel.setFont(LIST_FONT);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);

        readyButton = new SketchButton("READY", () -> {
            try{
                menu.getComputerPlayer().PlaceShips();
            }catch(Exception e){
                e.printStackTrace();
                return;
            }
            menu.getInGameMenu().StartMatch(menu.getPlayerSide());
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
        
        boardSection.setBoard(menu.getPlayerPlayer().getPlayerBoard());
        for(SketchButton b : buttons) b.setEnabled(true);
        
        updateReady();
    }
}
