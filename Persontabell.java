package Superiore;

/**
 * Created by Dragon on 28.04.14.
 */
import com.sun.rowset.CachedRowSetImpl;

import java.sql.*;
import javax.sql.rowset.*;
import javax.swing.*;


public class Persontabell {
    static final String USER = "sebastianramsla3";
    static final String PASSWORD = "pjW8iUnH";
    static final String URL = "sebastianramsla3.mysql.domeneshop.no";
    private Connection con = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    public CachedRowSet visAltIPersontabellen(){
        CachedRowSetImpl c = null;
        String query = "SELECT * FROM persons";

        try{
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            c = new CachedRowSetImpl();
            c.populate(rs);
        }catch(SQLException sqle){
            sqle.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error! Feil i SQL!");
        }finally{
            try{
                rs.close();
                stmt.close();
                con.close();
            }catch(SQLException sqle){
                sqle.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error! Feil i SQL!");
            }
        }
        if(c == null){
            JOptionPane.showMessageDialog(null, "Personlisten er tom!");
            return null;
        }else{
            return c;
        }
    }

    public CachedRowSet finnPersonVedAASkriveInnPersonnummer(int id){
        CachedRowSet c = null;
        PreparedStatement ps = null;
        String query = "SELECT * FROM persons WHERE person_no LIKE ?";

        try{
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            ps = con.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            c = new CachedRowSetImpl();
            c.populate(rs);
            c.setPageSize(30);
        }catch(SQLException sqle){
            sqle.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error! Feil i SQL!");
        }finally{
            try{
                rs.close();
                stmt.close();
                con.close();
            }catch(SQLException sqle){
                sqle.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error! Feil i SQL!");
            }
        }
        if(c == null){
            JOptionPane.showMessageDialog(null, "Ingen personer har denne id-en!");
            return null;
        }
        return c;
    }

    public CachedRowSet finnPersonVedAASkriveInnEtternavn(String navn){
        CachedRowSet c = null;
        PreparedStatement ps = null;
        String query = "SELECT * FROM persons WHERE surname LIKE ?";

        try{
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            ps = con.prepareStatement(query);
            ps.setString(1, navn);
            rs = ps.executeQuery();
            c = new CachedRowSetImpl();
            c.populate(rs);
            c.setPageSize(30);
        }catch(SQLException sqle){
            sqle.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error! Feil i SQL!");
        }finally{
            try{
                rs.close();
                stmt.close();
                con.close();
            }catch(SQLException sqle){
                sqle.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error! Feil i SQL!");
            }
        }
        if(c == null){
            JOptionPane.showMessageDialog(null, "Ingen personer har dette navnet!");
            return null;
        }
        return c;
    }

    public void settRoykeVerdiTilTrue(int id ){
        String query = "UPDATE persons SET smoker LIKE ? WHERE person_no LIKE ?";
        PreparedStatement ps = null;

        try{
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            ps = con.prepareStatement(query);
            ps.setBoolean(1, true);
            ps.setInt(2, id);
            ps.executeUpdate();
        }catch(SQLException sqle){
            sqle.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error! Feil i SQL!");
        }finally{
            try{
                ps.close();
                con.close();
            }catch(SQLException sqle){
                sqle.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error! Feil i SQL!");
            }
        }
    }

    public void settInnNyPersonIPersonlisten(String pnr, boolean broker, String fornavn, String etternavn, String adresse, int gatenr, int zip, String telefon, String mail){
        String query = "SELECT * FROM persons";

        try{
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery(query);
            rs.moveToInsertRow();
            rs.updateString("person_no", pnr);
            rs.updateBoolean("broker", broker);
            rs.updateString("firstname", fornavn);
            rs.updateString("surname", etternavn);
            rs.updateString("adress", adresse);
            rs.updateInt("street_no", gatenr);
            rs.updateInt("zip_code", zip);
            rs.updateString("telephone", telefon);
            rs.updateString("mail", mail);
            rs.insertRow();
            rs.moveToCurrentRow();
        }catch(SQLException sqle){
            sqle.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error! Feil i SQL!");
        }finally{
            try{
                rs.close();
                stmt.close();
                con.close();
            }catch(SQLException sqle){
                sqle.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error! Feil i SQL!");
            }
        }
    }
}
