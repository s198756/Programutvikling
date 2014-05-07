package Superiore;


import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

/**
 * Created by Dragon on 22.04.14.
 */
public class Boligtabell {
    static final String DRIVER = "com.mysql.jdbc.Driver";
    static final String USER = "sebastianramsla3";
    static final String PASSWORD = "pjW8iUnH";
    static final String URL = "jdbc:mysql://sebastianramsla3.mysql.domeneshop.no/sebastianramsla3?zeroDateTimeBehavior=convertToNull";
    private Connection con = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    public DefaultTableModel visAltIBoligtabellen() {
        String query = "SELECT * FROM dwelling_unit";

        Vector columnnames = new Vector();
        Vector rows = new Vector();
        try {
            Class.forName(DRIVER);
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
                rows.addElement(newrows);
            }
            rs.close();
            stmt.close();
            con.close();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error! Feil i SQL!");
        }catch(ClassNotFoundException cnfe){
            cnfe.printStackTrace();
            JOptionPane.showMessageDialog(null, "Finner ikke klassen!");
        }

        DefaultTableModel everything = new DefaultTableModel(rows, columnnames);
        return everything;
    }

    public DefaultTableModel finnBoligVedAASkriveInnAdresse(String adresse){
        PreparedStatement ps = null;
        String query = "SELECT * FROM dwelling_unit WHERE street LIKE ?";
        Vector rows = new Vector();
        Vector columnnames = new Vector();

        try{
            Class.forName(DRIVER);
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            ps = con.prepareStatement(query);
            ps.setString(1, "%" + adresse + "%");
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
            ps.close();
            con.close();

        }catch(SQLException sqle){
            sqle.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error! Feil i SQL!");
        }catch(ClassNotFoundException cnfe){
            cnfe.printStackTrace();
            JOptionPane.showMessageDialog(null, "Finner ikke driverklassen!");
        }
            DefaultTableModel adress = new DefaultTableModel(rows, columnnames);
            return adress;
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
        String query = "SELECT * FROM dwelling_unit";

        try{
            Class.forName(DRIVER);
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
        }catch(ClassNotFoundException cnfe){
            cnfe.printStackTrace();
            JOptionPane.showMessageDialog(null, "Driverklassen ikke funnet!");
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