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
import javax.swing.*;

public class ShipPlacerMenu extends PaperPanel{
 
    private static final Font HEADER_FONT = new Font(Font.MONOSPACED, Font.BOLD, 45);
    private static final Font LIST_FONT = new Font(Font.MONOSPACED, Font.BOLD, 18);

    public ShipPlacerMenu(MainMenu menu){
        
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        SketchLabel title = new SketchLabel("Ship Placement");
        title.setFont(HEADER_FONT);
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(title, BorderLayout.NORTH);
        
        JPanel body = new JPanel(new GridBagLayout());
        body.setOpaque(false);

        BoardPanel boardSection = new BoardPanel();
    
        JPanel borderHolder = new JPanel(null) {
            @Override
            public void doLayout(){
                int s = Math.min(getWidth(), getHeight());
                boardSection.setBounds((getWidth() - s) / 2, (getHeight() - s) / 2, s, s);
            }
        };
        borderHolder.setOpaque(false);
        borderHolder.setPreferredSize(new Dimension(500, 500));
        borderHolder.add(boardSection);

        JPanel shipsSection = BuildShipsPanel(boardSection);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.weightx = 2;
        gbc.insets = new Insets(0, 0, 0, 20);
        body.add(borderHolder, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        body.add(shipsSection, gbc);

        add(top, BorderLayout.NORTH);
        add(body, BorderLayout.CENTER);
        add(BuildBottomBar(menu), BorderLayout.SOUTH);
    }
    
    private JPanel BuildShipsPanel(BoardPanel board){
        String[] names = {"Carrier", "Battleship", "Destroyer", "Submarine", "Patrolboat"};
        int[] lengths = {5, 4, 3, 3, 2};

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        SketchButton[] buttons = new SketchButton[names.length];
        int[] selected = {-1};

        for(int i = 0; i < names.length; i++){
            final int idx = i;
            buttons[i] = new SketchButton(names[i] + " (" + lengths[i] + ")", () -> {
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
            }
        });

        return panel;
    }

    private JPanel BuildBottomBar(MainMenu menu){
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);

        SketchButton readyButton = new SketchButton("READY", () -> {
            System.out.println("");
        });
        
        bar.add(readyButton, BorderLayout.EAST);

        return bar;
    } 
}
