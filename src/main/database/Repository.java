package main.database;

import main.model.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Repository{

    protected static final String DB_URL = System.getenv().getOrDefault("DB_URL", "jdbc:postgresql://localhost:5432/battleship");
    protected static final String DB_USER = System.getenv().getOrDefault("DB_USER", "battleship");
    protected static final String DB_PASS = System.getenv().getOrDefault("DB_PASS", "1234");

    protected Connection getConnection() throws SQLException{
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    protected int getId(String name) throws SQLException{
        String sql = "SELECT id FROM players WHERE name = ?";

        try(Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
        
            pstmt.setString(1, name);
            
            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next())
                    return rs.getInt("id");
            }
        }

        return -1;
    }

    protected PlayerProfile getPlayer(int id) throws SQLException{
        String sql = "SELECT name, correct_hits, wrong_hits FROM players WHERE id = ?";

        try(Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            
            pstmt.setInt(1, id);

            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    return new PlayerProfile(
                        rs.getString("name"),
                        rs.getInt("correct_hits"),
                        rs.getInt("wrong_hits")
                    );
                }
            } 
        }

        return null;
    }
}
