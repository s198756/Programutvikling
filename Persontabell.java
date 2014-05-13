

/**
 * Created by Carl Reinsnes
 */

import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;


public class Persontabell {
    static final String DRIVER = "com.mysql.jdbc.Driver";
    static final String USER = "sebastianramsla3";
    static final String PASSWORD = "pjW8iUnH";
    static final String URL = "jdbc:mysql://sebastianramsla3.mysql.domeneshop.no/sebastianramsla3?zeroDateTimeBehavior=convertToNull";
    private Connection con = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    public DefaultTableModel visAltIPersontabellen() {
        String query = "SELECT * FROM person";
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
}
