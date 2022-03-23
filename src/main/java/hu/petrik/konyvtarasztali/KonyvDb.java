package hu.petrik.konyvtarasztali;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KonyvDb {
    Connection conn;

    public KonyvDb() throws SQLException {
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/vizsga-2022-14s-wip-db", "root", "");
    }

    public List<Konyv> getKonyvek() throws SQLException {
        List<Konyv> konyvList = new ArrayList<>();
        Statement stmt = conn.createStatement();
        String sql = "SELECT * FROM books;";
        ResultSet result = stmt.executeQuery(sql);
        while (result.next()) {
            int id = result.getInt("id");
            String title = result.getString("title");
            String author = result.getString("author");
            int publish_year = result.getInt("publish_year");
            int page_count = result.getInt("page_count");
            Konyv konyv = new Konyv(id, title, author, publish_year, page_count);
            konyvList.add(konyv);
        }
        return konyvList;
    }

    public boolean deleteKonyv(int id) throws SQLException {
        String sql = "DELETE FROM books WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        int erintettSorok = stmt.executeUpdate();
        return erintettSorok == 1;
    }
}
