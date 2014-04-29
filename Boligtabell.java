package Superiore;

import com.sun.rowset.CachedRowSetImpl;

import java.sql.*;
import javax.sql.rowset.*;
import javax.swing.*;

/**
 * Created by Dragon on 22.04.14.
 */
public class Boligtabell {
    static final String USER = "sebastianramsla3";
    static final String PASSWORD = "pjW8iUnH";
    static final String URL = "sebastianramsla3.mysql.domeneshop.no";
    private Connection con = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    public CachedRowSet visAltIBoligtabellen(){
        CachedRowSetImpl c = null;
        String query = "SELECT * FROM dwelling_units";

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
            JOptionPane.showMessageDialog(null, "Boliglisten er tom!");
            return null;
        }else{
        return c;
        }
    }

    public CachedRowSet finnBoligVedAASkriveInnId(int id){
        CachedRowSet c = null;
        PreparedStatement ps = null;
        String query = "SELECT * FROM dwelling_units WHERE dwelling_unit_id LIKE ?";

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
            JOptionPane.showMessageDialog(null, "Ingen boliger har denne id-en!");
            return null;
        }
        return c;
    }

    public CachedRowSet finnBoligVedAASkriveInnAdresse(String adresse){
        CachedRowSet c = null;
        PreparedStatement ps = null;
        String query = "SELECT * FROM dwelling_units WHERE street LIKE ?";

        try{
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            ps = con.prepareStatement(query);
            ps.setString(1, adresse);
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
            JOptionPane.showMessageDialog(null, "Ingen boliger har denne adressen!");
            return null;
        }
        return c;
    }

    public void settVarmtvannVerdiTilTrue(int id ){
        String query = "UPDATE dwelling_unit SET ind_warmwater LIKE ? WHERE dwelling_unit_id LIKE ?";
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

    public void settInnNyBoligIBoliglisten(String owner, String type, int size, String street, int streetnr, int zip, int monthly, int deposit){
        String query = "SELECT * FROM dwelling_units";

        try{
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery(query);
            rs.moveToInsertRow();
            rs.updateString("property_owner", owner);
            rs.updateString("dwelling_type", type);
            rs.updateInt("size", size);
            rs.updateString("street", street);
            rs.updateInt("street_no", streetnr);
            rs.updateInt("zip_code", zip);
            rs.updateInt("monthly_price", monthly);
            rs.updateInt("depositum", deposit);
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