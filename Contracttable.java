import java.sql.*;
import javax.swing.*;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 * Created by Dragon on 28.04.14.
 */
public class Contracttable {
    static final String DRIVER = "com.mysql.jdbc.Driver";
    static final String USER = "sebastianramsla3";
    static final String PASSWORD = "pjW8iUnH";
    static final String URL = "jdbc:mysql://sebastianramsla3.mysql.domeneshop.no/sebastianramsla3?zeroDateTimeBehavior=convertToNull";
    private Connection con = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    public DefaultTableModel showEverythingInContracttable(){
        //Metoden henter ut alle kontrakter i databasen og sender ut disse i en DefaultTableModel

        String query = "SELECT * FROM contract";   //SQL-query
        Vector columnnames = new Vector();          //Vektorer for lagring av databaseverdier
        Vector rows = new Vector();

        try{
            Class.forName(DRIVER);                                      //erklærer driverklasse
            con = DriverManager.getConnection(URL, USER, PASSWORD);     //oppretter database-forbindelse
            stmt = con.createStatement();                               //oppretter statement
            rs = stmt.executeQuery(query);                              //gjennomfører databasesøk og sender
                                                                        // til resultset
            ResultSetMetaData metadata = rs.getMetaData();              //fyller inn data i metadata-objekt
            int numberofcolumns = metadata.getColumnCount();            //teller kolonner


            for (int column = 0; column < numberofcolumns; column++) {
                columnnames.addElement(metadata.getColumnLabel(column + 1));//gjennomløper og fyller inn kolonner
            }

            while (rs.next()) {
                Vector newrows = new Vector();
                for (int i = 1; i <= numberofcolumns; i++) {
                    newrows.addElement(rs.getObject(i));            //gjennomløper og fyller midlertidig vektor med
                                                                    //radene
                }
                rows.addElement(newrows);                           //legger verdier i opprinnelig rad-vektor
            }
            rs.close();
            stmt.close();                                           //lukker alle åpne database-forbindelser
            con.close();

        }catch(SQLException sqle){
            sqle.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error! Feil i SQL!");  //catcher exceptions og sender ut feil-
        }catch(ClassNotFoundException cnfe){                            //meldinger
            cnfe.printStackTrace();
            JOptionPane.showMessageDialog(null, "Driverklasse ikke funnet!");
        }
        DefaultTableModel everything = new DefaultTableModel(rows, columnnames);//oppretter DefaultTableModel

        return everything;                              //sender ut
    }

    public DefaultTableModel findContractByDwellingUnit(int id){
        //Metoden oppretter en defaultTablemodel med alle kontrakter der bolig-id er lik innkommende parameter

        PreparedStatement ps;
        String query = "SELECT * FROM contract WHERE dwelling_unit_id LIKE ?"; //SQL-query: legg merke til spm-tegn
        Vector rows = new Vector();
        Vector columnnames = new Vector();

        try{
            Class.forName(DRIVER);
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            ps = con.prepareStatement(query);

            ps.setInt(1, id);                                        //Setter inn int-verdi på plassen i SQL-query
                                                                     //der spørsmålstegnet er. Gjør ellers alt etter
                                                                     //dette helt likt som i forrige metode.
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
            ps.close();                                         //lukker alle åpne forbindelser til databasen
            con.close();

        }catch(SQLException sqle){
            sqle.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error! Feil i SQL!");      //catch-blokk med feilmeldinger
        }catch(ClassNotFoundException cnfe){
            cnfe.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error! Driverklasse ikke funnet!");
        }

        DefaultTableModel contractID = new DefaultTableModel(rows, columnnames);//oppretter DefaultTablemodel

        return contractID;                      //sender ut
    }

    public DefaultTableModel showPersonContracts(String pNo){
        //Metoden sender ut en DefaultTableModel med kontraktene knyttet til en person som enten er leietaker
        //eller megler

        PreparedStatement ps;
        String query = "SELECT contract_id, valid, in_effect_date, expiration_date FROM contract WHERE broker LIKE ? OR renter LIKE ?";
        Vector rows = new Vector();
        Vector columnnames = new Vector();

        try{
            Class.forName(DRIVER);
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            ps = con.prepareStatement(query);

            ps.setString(1, pNo);       //int-verdien indikerer at den medsendte string-verdien blir satt der det
                                        // første spørsmålstegnet står
            ps.setString(2, pNo);       //int-verdien indikerer det samme for det andre spørsmålstegnet

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
            ps.close();                               //lukker alle åpne forbindelser
            con.close();

        }catch(SQLException sqle){
            sqle.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error! Feil i SQL!");
        }catch(ClassNotFoundException cnfe){
            cnfe.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error! Driverklasse ikke funnet!");
        }
        DefaultTableModel ownercontracts = new DefaultTableModel(rows, columnnames);//oppretter DefaultTableModel

        return ownercontracts;                  //sender ut
    }
}
