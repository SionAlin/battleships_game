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

public class LeaderboardMenu extends PaperPanel{
    
    private static final Font LIST_FONT = new Font(Font.MONOSPACED, Font.BOLD, 18);
    private static final String ROW_FORMAT = "%-6s%-22s%9s%9s%13s";
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final Color ink = new Color(35, 75, 155);

    private final MatchRepository repository = new MatchRepository();
    private final DefaultListModel<PlayerProfile> listModel = new DefaultListModel<>();
    private final DefaultListModel<MatchRecord> listHistory = new DefaultListModel<>();
    private final JList<PlayerProfile> rankingList = new JList<>(listModel);
    private final JList<MatchRecord> historyList = new JList<>(listHistory);
    private final SketchLabel statusLabel = new SketchLabel(" ");
    private final SketchLabel historyHeader = new SketchLabel(" ");

    public LeaderboardMenu(MainMenu menu){
        
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
       
        SketchLabel title = new SketchLabel("Leaderboard");
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(title, BorderLayout.NORTH);
        top.add(statusLabel, BorderLayout.CENTER);

        JPanel body = new JPanel(new GridBagLayout());
        body.setOpaque(false);

        JPanel leaderboardSection = BuildLeaderboardSection();
        JPanel historySection = BuildHistorySection();
        leaderboardSection.setPreferredSize(new Dimension(0, 0));
        historySection.setPreferredSize(new Dimension(0, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.weightx = 2;
        gbc.insets = new Insets(0, 0, 0, 20);
        body.add(leaderboardSection, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        body.add(historySection, gbc);

        add(top, BorderLayout.NORTH);
        add(body, BorderLayout.CENTER);
        add(BuildBottomBar(menu), BorderLayout.SOUTH);

        Refresh();
    }

    private JPanel BuildLeaderboardSection(){
        JPanel section = new JPanel(new BorderLayout(0, 15));
        section.setOpaque(false);
       
        SketchLabel header = new SketchLabel(String.format(ROW_FORMAT, "Rank", "Name", "Correct", "Wrong", "Record"));
        header.setFont(LIST_FONT);
        header.setBorder(BorderFactory.createEmptyBorder(0, 14, 10, 0));

        rankingList.setOpaque(false);
        rankingList.setCellRenderer(new RankRenderer());

        section.add(header, BorderLayout.NORTH);
        section.add(ScrollBox(rankingList), BorderLayout.CENTER);
        return section;
    }

    private JPanel BuildHistorySection(){
        JPanel section = new JPanel(new BorderLayout(0, 15));
        section.setOpaque(false);

        SketchLabel historyLabel = new SketchLabel("History");
        historyLabel.setFont(LIST_FONT);
        historyLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        historyList.setOpaque(false);
        historyList.setCellRenderer(new HistoryRenderer());
        
        JScrollPane historyScroll = ScrollBox(historyList);
        historyHeader.setFont(LIST_FONT);
        historyHeader.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        historyScroll.setColumnHeaderView(historyHeader);
        historyScroll.getColumnHeader().setOpaque(false);

        section.add(historyLabel, BorderLayout.NORTH);
        section.add(historyScroll, BorderLayout.CENTER);

        return section;
    }

    private JPanel BuildBottomBar(MainMenu menu){
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);

        SketchButton backButton = new SketchButton("BACK", () -> menu.Show("HOME"));
        SketchButton selectButton = new SketchButton("SELECT", () -> {
            PlayerProfile selected = getSelectedProfile();
            if(selected != null){
                listHistory.clear();

                try{
                    historyHeader.setText(selected.getPlayerName() + "'s matches");
                    List<MatchRecord> matches = repository.matchHistory(selected);
                    for(MatchRecord matche : matches){
                        listHistory.addElement(matche);
                    }
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
        });

        bar.add(selectButton, BorderLayout.EAST);
        bar.add(backButton, BorderLayout.WEST);
        
        return bar;
    }

    private PlayerProfile getSelectedProfile(){
        return rankingList.getSelectedValue();
    }

    private void Refresh(){
        listModel.clear();

        try{
            List<PlayerProfile> profiles = repository.Leaderboard();

            for(PlayerProfile profile : profiles)
                listModel.addElement(profile);

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    private JScrollPane ScrollBox(JComponent content){
        JScrollPane scroll = new JScrollPane(content);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createLineBorder(ink, 2));
        return scroll;
    }

    private class RankRenderer extends DefaultListCellRenderer{
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus){
            super.getListCellRendererComponent(list, value, index, false, false);

            PlayerProfile profile = (PlayerProfile) value;
            int[] record = profile.getRecord();
            setText(String.format(ROW_FORMAT, (index + 1) + ".", profile.getPlayerName(), profile.getCorrectHits(), profile.getWrongHits(), record[0] + "W - " + record[1] + "L"));
            setFont(LIST_FONT);
            setForeground(ink);
            setBackground(new Color(205, 218, 242));
            setOpaque(isSelected);
            setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
            return this;
        }
    }

    private class HistoryRenderer extends DefaultListCellRenderer{
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus){
            super.getListCellRendererComponent(list, value, index, false, false);

            MatchRecord matchRecord = (MatchRecord) value;

            String one = matchRecord.getPlayerOne().getPlayerName();
            String two = matchRecord.getPlayerTwo().getPlayerName();
            String winner = matchRecord.getWinner().getPlayerName();

            String left = one.equals(winner) ? "(W) " + one : one;
            String right = two.equals(winner) ? "(W) " + two : two;

            LocalDateTime startTime = matchRecord.getStartTime();
            LocalDateTime endTime = matchRecord.getEndTime();
            
            int number = list.getModel().getSize() - index;

            setText("<html>" + number + ". " + left +  " - " + right
                + "<br>"
                + "<br>Started: " + startTime.format(TIME_FORMAT)
                + "<br>Ended: " + endTime.format(TIME_FORMAT)
                + "<br><br>"
                + "</html>");

            setFont(LIST_FONT);
            setForeground(ink);
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
            return this;
        }
    }
}
