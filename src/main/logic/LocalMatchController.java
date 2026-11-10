package main.logic;

import main.model.*;
import main.database.*;
import main.logic.player.*;
import java.sql.SQLException;
import javax.swing.Timer;

public class LocalMatchController{
    
    private final HumanPlayer human;
    private final AIPlayer computer;
    private final MatchRecord match;
    private final MatchView view;
    private final PlayerProfileRepository playerProfileRepository = new PlayerProfileRepository();
    private final MatchRepository matchRepository = new MatchRepository();

    public LocalMatchController(HumanPlayer human, AIPlayer computer, MatchRecord match, MatchView view){
        this.human = human;
        this.computer = computer;
        this.match = match;
        this.view = view;
    }

    public void startMatch(boolean computerFirst){
        if(computerFirst) computerTurn();
        else view.setAttackEnabled(true);
    }

    public void playerShot(int row, int col){
        if(human.fire(computer.getPlayerBoard(), row, col) == ShotResult.ALREADY_SHOT) return;
        view.repaintBoards();
        view.refreshStats();

        if(!computer.getPlayerBoard().HasShipsLeft()){
            finish(human.getPlayerName());
            return;
        }
        computerTurn();
    }

    private void computerTurn(){
        view.setAttackEnabled(false);
        Timer t = new Timer(700, e -> {
            computer.makeMove(human.getPlayerBoard());
            view.repaintBoards();
            view.refreshStats();

            if(!human.getPlayerBoard().HasShipsLeft()) finish(computer.getPlayerName());
            else view.setAttackEnabled(true);
        });
        t.setRepeats(false);
        t.start();
    }

    private void finish(String winner){
        match.setWinner(winner);
        try{
            matchRepository.create(match);
            playerProfileRepository.updateHits(human);
            playerProfileRepository.updateHits(computer);
        }catch(SQLException e){
            e.printStackTrace();
        }
        view.setAttackEnabled(false);
        view.showEndScreen();
    }
}
