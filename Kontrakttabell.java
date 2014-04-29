package Superiore;

import java.sql.*;
import javax.sql.rowset.*;
import javax.swing.*;
import com.sun.rowset.CachedRowSetImpl;
/**
 * Created by Dragon on 28.04.14.
 */
public class Kontrakttabell {
    static final String USER = "sebastianramsla3";
    static final String PASSWORD = "pjW8iUnH";
    static final String URL = "sebastianramsla3.mysql.domeneshop.no";
    private Connection con = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    public CachedRowSet visAltIKontrakttabellen(){
        CachedRowSetImpl c = null;
        String query = "SELECT * FROM contracts";

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
            JOptionPane.showMessageDialog(null, "Kontraktlisten er tom!");
            return null;
        }else{
            return c;
        }
    }

    public CachedRowSet finnKontraktVedAASkriveInnId(int id){
        CachedRowSet c = null;
        PreparedStatement ps = null;
        String query = "SELECT * FROM contracts WHERE contract_id LIKE ?";

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
            JOptionPane.showMessageDialog(null, "Ingen kontrakter har denne id-en!");
            return null;
        }
        return c;
    }

    public CachedRowSet finnKontraktVedAASkriveInnBoligId(int id){
        CachedRowSet c = null;
        PreparedStatement ps = null;
        String query = "SELECT * FROM contracts WHERE dwelling_unit_id LIKE ?";

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
            JOptionPane.showMessageDialog(null, "Ingen kontrakter er lagd med denne adressen!");
            return null;
        }
        return c;
    }

    public void settGyldighetsVerdiTilTrue(int id ){
        String query = "UPDATE contracts SET valid LIKE ? WHERE contract_id LIKE ?";
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

    public void settInnNyKontraktIKontraktlisten(int id, String renter, String broker, Date in_effect_date, Date expiration_date){
        String query = "SELECT * FROM contracts";

        try{
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery(query);
            rs.moveToInsertRow();
            rs.updateInt("contract_id", id);
            rs.updateString("renter", renter);
            rs.updateString("broker", broker);
            rs.updateDate("in_effect_date", in_effect_date);
            rs.updateDate("expiration_date", expiration_date);
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
