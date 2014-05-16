/**
 * Programmert av Carl Reinsnes
 */

import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;


public class Persontable {
    static final String DRIVER = "com.mysql.jdbc.Driver";       //driver-konstant
    static final String USER = "sebastianramsla3";              //brukernavn-konstant
    static final String PASSWORD = "pjW8iUnH";                  //passord-konstant
    static final String URL = "jdbc:mysql://sebastianramsla3.mysql.domeneshop.no/sebastianramsla3?zeroDateTimeBehavior=convertToNull";
                                                                //url til databasen
    private Connection con = null;
    private Statement stmt = null;                         //variabler for å opprette connection og utføre en SQL-query
    private ResultSet rs = null;

    public DefaultTableModel showeverythingInPersontable() {
        //Metode som oppretter en DefaultTableModel som blir sendt ut. Blir brukt til å opprette tabell i
        //Personvindu-klassen

        String query = "SELECT person_no, is_broker, firstname, middlename, surname, street, street_no, zip_code, " +
                        "telephone, email FROM person";         //SQL-query for å hente ut alle personer

        Vector columnnames = new Vector();                      //Vector for å legge inn kolonnenavn
        Vector rows = new Vector();                             //Vector for å legge inn radverdier

        try {
            Class.forName(DRIVER);                                      //erklærer driverklasse
            con = DriverManager.getConnection(URL, USER, PASSWORD);     //oppretter database-forbindelse
            stmt = con.createStatement();                               //lager et statement
            rs = stmt.executeQuery(query);                              //fyller et resultset med databaseverdier
            ResultSetMetaData metadata = rs.getMetaData();              //sender data videre til et metadata-objekt
            int numberofcolumns = metadata.getColumnCount();            //oppretter en int-variabel for å telle kolonner

            for (int column = 0; column < numberofcolumns; column++) {
                columnnames.addElement(metadata.getColumnLabel(column + 1));    //løper gjennom kolonnene og legger
                                                                                // inn resultset-verdier
            }

            while (rs.next()) {
                Vector newrows = new Vector();                                  //gjennomløper og fyller inn en ny
                for (int i = 1; i <= numberofcolumns; i++) {                    //rad-vektor med verdier
                    newrows.addElement(rs.getObject(i));
                }
                rows.addElement(newrows);                         //legger til verdiene i opprinnelig rad-vektor
            }

            stmt.close();
            rs.close();                                                 //lukker alle åpne forbindelser til databasen
            con.close();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error! Feil i SQL!");  //feilmeldinger ved SQL-feil eller
            return null;                                                //manglende driverklasse
        }catch(ClassNotFoundException cnfe){
            cnfe.printStackTrace();
            JOptionPane.showMessageDialog(null, "Klasse ikke funnet!");
        }

        DefaultTableModel everything = new DefaultTableModel(rows, columnnames);//oppretter DefaultTableModeø

        return everything;                                          //sender ut
    }

    public DefaultTableModel findPersonBySurname(String name) {
        //Oppretter DefaultTableModel med alle personer med etternavn lik den innkommende parameteren
        //Blir brukt til å opprette tabell i Personvindu

        PreparedStatement ps;
        String query = "SELECT person_no, is_broker, firstname, middlename, surname, street, street_no, zip_code, " +
                        "telephone, email FROM person WHERE surname LIKE ?";
        Vector columnnames = new Vector();
        Vector rows = new Vector();

        try {
            Class.forName(DRIVER);
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            ps = con.prepareStatement(query);

            ps.setString(1,"%" + name + "%");                                  //Denne metoden gjør alt det samme som forrige
                                                                    // metode frem til her. Den første parameteren
                                                                    //er en indeks-verdi for hvor man skal
                                                                    //plassere string-verdien.
                                                                    //Den blir plassert på første, og i dette tilfellet,
                                                                    //eneste spørsmålstegn-plass

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
            stmt.close();                                   //lukker alle åpne forbindelser
            con.close();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error! Feil i SQL!");
        }
        catch(ClassNotFoundException cnfe){
            cnfe.printStackTrace();
            JOptionPane.showMessageDialog(null, "Driverklasse ikke funnet!");
        }
        DefaultTableModel personName = new DefaultTableModel(rows, columnnames);//oppretter DefaultTableModel

        return personName;                                  //sender ut
    }
}
