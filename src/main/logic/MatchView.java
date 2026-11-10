package main.logic;

public interface MatchView{
    void setAttackEnabled(boolean enabled);
    void repaintBoards();
    void refreshStats();
    void showEndScreen();
}
