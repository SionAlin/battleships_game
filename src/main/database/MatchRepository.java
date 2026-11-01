package main.database;

import main.model.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class MatchRepository extends Repository{

    public boolean create(MatchRecord match) throws SQLException{
        String sql = "INSERT INTO matches (player_one_id, player_two_id, winner_id, started_at, ended_at) VALUES (?, ?, ?, ?, ?)";

        PlayerProfile playerOne = match.getPlayerOne();
        PlayerProfile playerTwo = match.getPlayerTwo();
        PlayerProfile winner = match.getWinner();
        int playerOneId = getId(playerOne.getPlayerName());
        int playerTwoId = getId(playerTwo.getPlayerName());
        int winnerId = getId(winner.getPlayerName());

        try(Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1, playerOneId);
            pstmt.setInt(2, playerTwoId);
            pstmt.setInt(3, winnerId);
            pstmt.setObject(4, match.getStartTime());
            pstmt.setObject(5, match.getEndTime());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }catch(SQLException e){
            if("23505".equals(e.getSQLState()))
                return false;
            
            throw e;
        }

    }

    public List<MatchRecord> matchHistory(PlayerProfile player) throws SQLException{
        String sql = "SELECT id, player_one_id, player_two_id, winner_id, started_at, ended_at FROM matches WHERE player_one_id = ? OR player_two_id = ? ORDER BY id DESC";
        List<MatchRecord> matches = new ArrayList<>();
        int playerId = getId(player.getPlayerName());

        try(Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            
            pstmt.setInt(1, playerId);
            pstmt.setInt(2, playerId);
            
            try(ResultSet rs = pstmt.executeQuery()){
                while(rs.next()){
                    matches.add(new MatchRecord(
                        getPlayer(rs.getInt("player_one_id")),
                        getPlayer(rs.getInt("player_two_id")),
                        getPlayer(rs.getInt("winner_id")),
                        rs.getObject("started_at", LocalDateTime.class),
                        rs.getObject("ended_at", LocalDateTime.class)
                    ));
                }
            }
        }

        return matches;
    }
    
    public List<PlayerProfile> Leaderboard() throws SQLException{
        String sql = """ 
            WITH
                all_player_matches AS(
                    SELECT player_one_id AS player_id, winner_id FROM matches
                    UNION ALL
                    SELECT player_two_id AS player_id, winner_id FROM matches
                ),
                
                global_stats AS(
                    SELECT SUM(CASE WHEN winner_id = player_id THEN 1 ELSE 0 END)::FLOAT / NULLIF(COUNT(*), 0) AS global_win_rate
                    FROM all_player_matches
                ),
                
                player_stats AS(
                    SELECT
                        player_id,
                        COUNT(*) AS total_matches,
                        SUM(CASE WHEN winner_id = player_id THEN 1 ELSE 0 END) AS total_wins,
                        SUM(CASE WHEN winner_id <> player_id THEN 1 ELSE 0 END) AS total_losses
                    FROM all_player_matches
                    GROUP BY player_id
                ),

                stability_const AS (
                    SELECT PERCENTILE_CONT(0.5) WITHIN GROUP (ORDER BY total_matches)::FLOAT AS m
                    FROM player_stats
                )

                SELECT
                    ps.player_id,
                    ps.total_matches,
                    ps.total_wins,
                    ps.total_losses,
                    
                    (ps.total_wins + COALESCE(sc.m, 10) * gs.global_win_rate) / (ps.total_matches + COALESCE(sc.m, 10)) AS bayesian_score
                
                FROM player_stats ps
                CROSS JOIN global_stats gs
                CROSS JOIN stability_const sc
                ORDER BY bayesian_score DESC
                LIMIT 15;
        """;
        List<PlayerProfile> leaderboard = new ArrayList<>();
        PlayerProfile player = null;

        try(Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()){

            while(rs.next()){
                player = getPlayer(rs.getInt("player_id"));
                player.updateRecord(new int[]{rs.getInt("total_wins"), rs.getInt("total_losses")});
                leaderboard.add(player);
            }
        }

        return leaderboard;
    }

    public boolean delete(int id) throws SQLException{
        String sql = "DELETE FROM matches WHERE id = ?";

        try(Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            
            pstmt.setInt(1, id);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
