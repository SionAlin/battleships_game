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
import javax.swing.*;

public class PlayerVSComputerMenu extends PaperPanel{
    
    private static final Font HEADER_FONT = new Font(Font.MONOSPACED, Font.BOLD, 45);
    private static final Font LIST_FONT = new Font(Font.MONOSPACED, Font.BOLD, 18);
    private final Color ink = new Color(35, 75, 155);

    private final PlayerProfileRepository repository = new PlayerProfileRepository();
    private final DefaultListModel<PlayerProfile> listModel = new DefaultListModel<>();
    private final JList<PlayerProfile> playersOneList = new JList<>(listModel); 
    private final JList<PlayerProfile> playersTwoList = new JList<>(listModel); 
    private SketchButton playerOneButton;
    private SketchButton playerTwoButton;

    public PlayerVSComputerMenu(MainMenu menu){
    
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        SketchLabel title = new SketchLabel("Player vs. Computer");
        title.setFont(HEADER_FONT);
        title.setBorder(BorderFactory.createEmptyBorder(150, 0, 40, 0));

        title.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(title, BorderLayout.CENTER);
        
        JPanel body = new JPanel(new GridBagLayout());
        body.setOpaque(false);
        
        playerOneButton = new SketchButton("Player", () -> {
            playerOneButton.setText("Player");
            playerTwoButton.setText("Computer");
        });

        playerTwoButton = new SketchButton("Computer", () -> {
            playerOneButton.setText("Computer");
            playerTwoButton.setText("Player");
        });

        JPanel playerOneSection = BuildPlayerOneSection();
        JPanel playerTwoSection = BuildPlayerTwoSection();
       
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 0, 0, 20);
        body.add(playerOneSection, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        body.add(playerTwoSection, gbc);

        add(top, BorderLayout.NORTH);
        add(body, BorderLayout.CENTER);
        add(BuildBottomBar(menu), BorderLayout.SOUTH);
        
        Refresh();
    }

    private JPanel BuildPlayerOneSection(){
        JPanel section = new JPanel(new BorderLayout(0, 15));
        section.setBorder(BorderFactory.createEmptyBorder(40,80,40,80));
        section.setOpaque(false);

        playersOneList.setOpaque(false);
        playersOneList.setCellRenderer(new PlayersRenderer());
        
        JScrollPane scroll = new JScrollPane(playersOneList);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createLineBorder(ink, 2));

        section.add(scroll, BorderLayout.CENTER);
        section.add(playerOneButton, BorderLayout.NORTH);
        return section;
    }

    private JPanel BuildPlayerTwoSection(){
        JPanel section = new JPanel(new BorderLayout(0, 15));
        section.setBorder(BorderFactory.createEmptyBorder(40,80,40,80));
        section.setOpaque(false);

        playersTwoList.setOpaque(false);
        playersTwoList.setCellRenderer(new PlayersRenderer());
        
        JScrollPane scroll = new JScrollPane(playersTwoList);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createLineBorder(ink, 2));

        section.add(scroll, BorderLayout.CENTER);
        section.add(playerTwoButton, BorderLayout.NORTH);
        return section;
    }

    private JPanel BuildBottomBar(MainMenu menu){
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);

        SketchButton backButton = new SketchButton("BACK", () -> menu.Show("HOME"));

        SketchLabel errorLabel = new SketchLabel("");
        errorLabel.setFont(LIST_FONT);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        SketchButton playButton = new SketchButton("PLAY", () -> {
           
            if(getSelectedProfileOne() == null || getSelectedProfileTwo() == null){
                errorLabel.setText("Who Takes the Helm?");
                return;
            }

            if(playerOneButton.getText().equals("Player")){
                menu.setPlayer(getSelectedProfileOne());
                menu.setComputer(getSelectedProfileTwo());
            }else{
                menu.setComputer(getSelectedProfileOne());
                menu.setPlayer(getSelectedProfileTwo());
            }
            
            if(menu.getPlayerProfile().getPlayerName().equals(menu.getComputerProfile().getPlayerName())){
                errorLabel.setText("You can't play against yourself!");
            }else{
                menu.StartMatch(getSelectedProfileOne(), getSelectedProfileTwo());
                menu.getShipPlacerMenu().Refresh();
                menu.Show("SHIPPLACER");
                errorLabel.setText("");
            }
        });
        
        bar.add(playButton, BorderLayout.EAST);
        bar.add(errorLabel, BorderLayout.CENTER);
        bar.add(backButton, BorderLayout.WEST);

        return bar;
    }    
    
    private PlayerProfile getSelectedProfileOne(){
        return playersOneList.getSelectedValue();
    }

    private PlayerProfile getSelectedProfileTwo(){
        return playersTwoList.getSelectedValue();
    }

    private void Refresh(){
        listModel.clear();
        try{
            List<PlayerProfile> profiles = repository.findAll();

            for(PlayerProfile profile : profiles){
                listModel.addElement(profile);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    private class PlayersRenderer extends DefaultListCellRenderer{
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus){
            super.getListCellRendererComponent(list, value, index, false, false);

            PlayerProfile profile = (PlayerProfile) value;
            setText(profile.getPlayerName());
            setFont(LIST_FONT);
            setForeground(ink);
            setBackground(new Color(205, 218, 242));
            setOpaque(isSelected);
            setHorizontalAlignment(JLabel.CENTER);
            setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
            return this;
        }
    }
}
