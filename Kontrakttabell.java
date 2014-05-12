

import java.sql.*;
import javax.swing.*;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 * Created by Dragon on 28.04.14.
 */
public class Kontrakttabell {
    static final String DRIVER = "com.mysql.jdbc.Driver";
    static final String USER = "sebastianramsla3";
    static final String PASSWORD = "pjW8iUnH";
    static final String URL = "sebastianramsla3.mysql.domeneshop.no";
    private Connection con = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    public DefaultTableModel visAltIKontrakttabellen(){
        String query = "SELECT * FROM contracts";
        Vector columnnames = new Vector();
        Vector rows = new Vector();

        try{
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            ResultSetMetaData metadata = rs.getMetaData();
            int numberofcolumns = metadata.getColumnCount();


            for (int column = 0; column < numberofcolumns; column++) {
                columnnames.addElement(metadata.getColumnLabel(column + 1));
            }

            while (rs.next()) {
                Vector newrows = new Vector();
                for (int i = 1; i <= numberofcolumns; i++) {
                    newrows.addElement(rs.getObject(i));
                }
                rows.addElement(rows);
            }
            rs.close();
            stmt.close();
            con.close();

        }catch(SQLException sqle){
            sqle.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error! Feil i SQL!");
        }
        DefaultTableModel everything = new DefaultTableModel(rows, columnnames);

        return everything;
    }

    public DefaultTableModel finnKontraktVedAASkriveInnBoligId(int id){
        PreparedStatement ps = null;
        String query = "SELECT * FROM contracts WHERE dwelling_unit_id LIKE ?";
        Vector rows = new Vector();
        Vector columnnames = new Vector();

        try{
            Class.forName(DRIVER);
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            ps = con.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            ResultSetMetaData metadata = rs.getMetaData();
            int numberofcolumns = metadata.getColumnCount();


            for (int column = 0; column < numberofcolumns; column++) {
                columnnames.addElement(metadata.getColumnLabel(column + 1));
            }


            while (rs.next()) {
                Vector newrows = new Vector();
                for (int i = 1; i <= numberofcolumns; i++) {
                    newrows.addElement(rs.getObject(i));
                }
                rows.addElement(newrows);
            }
            rs.close();
            stmt.close();
            con.close();

        }catch(SQLException sqle){
            sqle.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error! Feil i SQL!");
        }catch(ClassNotFoundException cnfe){
            cnfe.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error! Driverklasse ikke funnet!");
        }
        DefaultTableModel contractID = new DefaultTableModel(rows, columnnames);

        return contractID;
    }

    public void settInnNyKontraktIKontraktlisten(int id, String renter, String broker, Date in_effect_date, Date expiration_date){
        String query = "SELECT * FROM contracts";

        try{
            Class.forName(DRIVER);
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
        }catch(ClassNotFoundException cnfe){
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
