package main.ui;

import main.model.*;
import main.database.*;
import java.io.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ProfileMenu extends PaperPanel{
    
    private final Color ink = new Color(35, 75, 155);
    private static final String ROW_FORMAT = "%-6s%-20s%19s%19s%25s%.0f%%";
    
    private List<PlayerProfile> allProfiles = new ArrayList<>();
    private final DefaultListModel<PlayerProfile> listModel = new DefaultListModel<>();
    private final JList<PlayerProfile> profileList = new JList<>(listModel);

    private PlayerProfile currentPlayer = null;
    private SketchLabel currentPlayerLabel;
    private PlayerProfileRepository playerProfileRepository = new PlayerProfileRepository();

    private final SketchTextArea createField = new SketchTextArea(1, 20);
    private final SketchTextArea searchField = new SketchTextArea(1, 20);

    public ProfileMenu(MainMenu menu){
        
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JPanel body = new JPanel(new GridBagLayout());
        body.setOpaque(false);

        JPanel createSection = BuildCreateSection();
        JPanel listSection = BuildListSection();
        createSection.setPreferredSize(new Dimension(0, 0));
        listSection.setPreferredSize(new Dimension(0, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;

        gbc.gridy = 0;
        gbc.weighty = 1;
        body.add(createSection, gbc);

        gbc.gridy = 1;
        gbc.weighty = 2;
        body.add(listSection, gbc);

        add(body, BorderLayout.CENTER);
        add(BuildBottomBar(menu), BorderLayout.SOUTH);

        RefreshAll();
    }

    public void SetProfile(List<PlayerProfile> profiles){
        allProfiles.clear();
        allProfiles.addAll(profiles);
        RefreshList();
    }

    public PlayerProfile getSelectedProfile(){
        return profileList.getSelectedValue();
    }

    private JPanel BuildCreateSection(){
        JPanel section = new JPanel(new BorderLayout());
        section.setOpaque(false);

        currentPlayerLabel = new SketchLabel("Your current player is: " + ((currentPlayer == null) ? " " : currentPlayer.getPlayerName()));
        currentPlayerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        SketchLabel label = new SketchLabel("Create a new profile");
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        SketchButton createButton = new SketchButton("Create", this::CreateProfile);

        JPanel row = new JPanel(new BorderLayout(20,0));
        row.setOpaque(false);
        row.add(createField, BorderLayout.CENTER);
        row.add(createButton, BorderLayout.EAST);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(currentPlayerLabel, BorderLayout.NORTH);
        top.add(label, BorderLayout.CENTER);
        top.add(row, BorderLayout.SOUTH);

        section.add(top, BorderLayout.NORTH);
        return section;
    }

    private JPanel BuildListSection(){
        JPanel section = new JPanel(new BorderLayout(0, 15));
        section.setOpaque(false);
        SketchLabel searchLabel = new SketchLabel("Search a profile");
        searchLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        searchField.getTextArea().getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent e){ RefreshList(); }

            @Override
            public void removeUpdate(DocumentEvent e){ RefreshList(); }

            @Override
            public void changedUpdate(DocumentEvent e){ RefreshList(); }

        });

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setOpaque(false);
        searchPanel.add(searchLabel, BorderLayout.NORTH);
        searchPanel.add(searchField, BorderLayout.CENTER);

        profileList.setOpaque(false);
        profileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        profileList.setCellRenderer(new ProfileRenderer());
        
        JScrollPane scroll = new JScrollPane(profileList);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createLineBorder(ink, 2));

        section.add(searchPanel, BorderLayout.NORTH);
        section.add(scroll, BorderLayout.CENTER);
        return section;
    }

    private JPanel BuildBottomBar(MainMenu menu){
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);

        SketchButton backButton = new SketchButton("BACK", () -> menu.Show("HOME"));
        SketchButton selectButton = new SketchButton("SELECT", () -> {
            menu.setSelectedProfile(getSelectedProfile());
            if(menu.getSelectedProfile() != null){
                currentPlayer = menu.getSelectedProfile();
                currentPlayerLabel.setText("Your current player is: " + currentPlayer.getPlayerName());
            }
        });
        
        bar.add(backButton, BorderLayout.WEST);
        bar.add(selectButton, BorderLayout.EAST);
        return bar;
    }
    
    private void CreateProfile(){
        String name = createField.getText().trim();
        if(name.isEmpty())
            return;

        if(Pattern.compile("[,./();]").matcher(name).find() || name.length() > 20)
            return;

        for(PlayerProfile profile : allProfiles){
            if(profile.getPlayerName().equalsIgnoreCase(name))
                return;
        }

        try{
            playerProfileRepository.create(name);
            RefreshAll();
        }catch(SQLException e){
            e.printStackTrace();
        }

        createField.setText("");
        searchField.setText("");
    }
    
    public void RefreshAll(){
        try{
            allProfiles = playerProfileRepository.findAll();
        }catch(SQLException e){
            e.printStackTrace();
        }

        String filter = searchField.getText().trim().toLowerCase();
        listModel.clear();
        for(PlayerProfile profile : allProfiles){
            if(profile.getPlayerName().toLowerCase().contains(filter))
                listModel.addElement(profile);
        }
    }

    private void RefreshList(){
        PlayerProfile selected = profileList.getSelectedValue();
        String filter = searchField.getText().trim().toLowerCase();

        listModel.clear();
        for(PlayerProfile profile : allProfiles){
            if(profile.getPlayerName().toLowerCase().contains(filter))
                listModel.addElement(profile);
        }

        if(selected != null && listModel.contains(selected))
            profileList.setSelectedValue(selected, true);
    }

    private class ProfileRenderer extends DefaultListCellRenderer{
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus){
            super.getListCellRendererComponent(list, value, index, isSelected, false);

            PlayerProfile profile = (PlayerProfile) value;
            setText(String.format(ROW_FORMAT, " ~> ", profile.getPlayerName(), profile.getCorrectHits() + " C. Hits", profile.getWrongHits() + " W. Hits", "Hit Ratio ", profile.getHitRatio()));
            setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
            setForeground(ink);
            setBackground(new Color(205, 218, 242));
            setOpaque(isSelected);
            setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
            return this;
        }
    }

}
