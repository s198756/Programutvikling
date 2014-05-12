package Superiore;

/**
 * Created by Dragon on 28.04.14.
 */

import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;


public class Persontabell {
    static final String DRIVER = "com.mysql.jdbc.Driver";
    static final String USER = "sebastianramsla3";
    static final String PASSWORD = "pjW8iUnH";
    static final String URL = "sebastianramsla3.mysql.domeneshop.no";
    private Connection con = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    public DefaultTableModel visAltIPersontabellen() {
        String query = "SELECT * FROM persons";
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
            stmt.close();
            rs.close();
            con.close();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error! Feil i SQL!");
            return null;
        }catch(ClassNotFoundException cnfe){
            cnfe.printStackTrace();
            JOptionPane.showMessageDialog(null, "Klasse ikke funnet!");
        }
        DefaultTableModel everything = new DefaultTableModel(rows, columnnames);

        return everything;
    }

    public DefaultTableModel finnPersonVedAASkriveInnPersonnummer(int id) {
        PreparedStatement ps = null;
        String query = "SELECT * FROM persons WHERE person_no LIKE ?";
        Vector columnnames = new Vector();
        Vector rows = new Vector();

        try {
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
                rows.addElement(rows);
            }
            rs.close();
            stmt.close();
            con.close();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error! Feil i SQL!");
        }
        DefaultTableModel personID = new DefaultTableModel(rows, columnnames);

        return personID;
    }

    public DefaultTableModel finnPersonVedAASkriveInnEtternavn(String navn) {
        PreparedStatement ps = null;
        String query = "SELECT * FROM persons WHERE surname LIKE ?";
        Vector columnnames = new Vector();
        Vector rows = new Vector();

        try {
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            ps = con.prepareStatement(query);
            ps.setString(1, navn);
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
                rows.addElement(rows);
            }
            rs.close();
            stmt.close();
            con.close();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error! Feil i SQL!");
        }
        DefaultTableModel personName = new DefaultTableModel(rows, columnnames);

        return personName;
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
