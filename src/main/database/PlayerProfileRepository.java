package main.database;

import main.model.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlayerProfileRepository extends Repository{
    
    public boolean create(String name) throws SQLException{
        String sql = "INSERT INTO players (name) VALUES (?)";
        
        try(Connection conn = getConnection(); 
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            
            pstmt.setString(1, name);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        
        }catch(SQLException e){
            if("23505".equals(e.getSQLState()))
                return false;

            throw e;
        }

    }

    public List<PlayerProfile> findAll() throws SQLException{
        String sql = "SELECT id, name, correct_hits, wrong_hits FROM players ORDER BY name";
        List<PlayerProfile> profiles = new ArrayList<>();

        try(Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()){
            
            while(rs.next()){
                profiles.add(new PlayerProfile(
                    rs.getString("name"),
                    rs.getInt("correct_hits"),
                    rs.getInt("wrong_hits")
                ));
            }
        }
        
        return profiles;

    }

    public boolean updateHits(PlayerProfile player) throws SQLException{
        String sql = "UPDATE players SET correct_hits = correct_hits + ?, wrong_hits = wrong_hits + ? WHERE name = ?";
        
        try(Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1, player.getCorrectHits());
            pstmt.setInt(2, player.getWrongHits());
            pstmt.setString(3, player.getPlayerName());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean delete(PlayerProfile player) throws SQLException{
        String sql = "DELETE FROM players WHERE name = ?";

        try(Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, player.getPlayerName());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }


}
