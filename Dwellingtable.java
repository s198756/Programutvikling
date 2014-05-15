

import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

/**
 * Programmert av Carl Reinsnes, s198756
 */
public class Dwellingtable {
    static final String DRIVER = "com.mysql.jdbc.Driver";                   //Stringkonstant for DRIVER
    static final String USER = "sebastianramsla3";                          //Stringkonstant for brukrnavn
    static final String PASSWORD = "pjW8iUnH";                              //Stringkonstant for passord
    static final String URL = "jdbc:mysql://sebastianramsla3.mysql.domeneshop.no/sebastianramsla3?zeroDateTimeBehavior=convertToNull";
    //Stringkonstant for URL:
    //?zeroDateTimeBehavior=convertToNull for å unngå nullpointerexception
    //i tilfelle datoverdi ikke blir lagt inn
    private Connection con = null;                            //connection-variabel
    private Statement stmt = null;                          //statement-variabel
    private ResultSet rs = null;                            //resultset-variabel

    public DefaultTableModel showEverythingInDwellingtable() {
        //Denne metoden returnerer en DefaultTableModel som blir sendt til boligvinduet og
        // oppretter et JTable når man trykker på 'Vis alle boliger'

        String query = "SELECT dwelling_unit_id, available, property_owner, " +
                "dwelling_type, size, street, street_no, zip_code, monthly_price, depositum, " +
                "incl_warmup, incl_warmwater, incl_internet, incl_tv, incl_electricity, incl_furniture, " +
                "incl_elec_appliance, allow_housepets, allow_smokers, created FROM dwelling_unit";
                //SQL-query for å hente ut alle boliger

        Vector columnnames = new Vector();                  //Vector for kolonnene
        Vector rows = new Vector();                         //Vector for radene

        try {
            Class.forName(DRIVER);                                              //erklærer driverklassen
            con = DriverManager.getConnection(URL, USER, PASSWORD);             //setter opp forbindelsen
            stmt = con.createStatement();                                       //lager et statement
            rs = stmt.executeQuery(query);                                      //gjennomfører databasesøk og legger
                                                                                // inn i resultset

            ResultSetMetaData metadata = rs.getMetaData();                      //resultsetmetadata-objekt tar i mot
                                                                                // metadata fra resultset

            int numberofcolumns = metadata.getColumnCount();                    //oppretter en teller for antall
                                                                                // kolonner i databasen

            for (int column = 0; column < numberofcolumns; column++) {          //løper gjennom en for-løkke der
                                                                                //kolonnevektoren får kolonne-verdier
                columnnames.addElement(metadata.getColumnLabel(column + 1));    //getColumnLabel teller fra 0
            }

            while (rs.next()) {                                                 //Så lenge rs har flere rader
                Vector newrows = new Vector();                                  //midlertidig vektor-objekt
                for (int i = 1; i <= numberofcolumns; i++) {                    //løper gjennom antall kolonner
                    newrows.addElement(rs.getObject(i));                        //og henter celleverdien i hver enkelt
                }
                rows.addElement(newrows);                                       //opprinnelig vector får den
                                                                                // midlertidige vectorens verdier
            }
            rs.close();                                                         //resultset,
            stmt.close();                                                       //statement og
            con.close();                                                    //connection lukkes. databasen er sårbar når
                                                                     //disse er åpne, så de skal alltid lukkes til sist

        } catch (SQLException sqle) {                                     //catch for SQL-problemer. Ved en hvilken som
                                                                                // helst feil får man melding
            sqle.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error! Feil i SQL!");
        }catch(ClassNotFoundException cnfe){                                //i tilfelle man ikke finner driverklassen
            cnfe.printStackTrace();
            JOptionPane.showMessageDialog(null, "Finner ikke driverklassen!");
        }

        DefaultTableModel everything = new DefaultTableModel(rows, columnnames); //oppretter ny DefaultTablemodel med de
                                                                                 //to vectorene som elementer i denne.
        return everything;                                                       //sender ut
    }

    public DefaultTableModel findDwellingUnitByAddress(String address){
        //stort sett lik visAltIBoligTabellen, bortsett fra at vi søker etter en spesifikk adresse,
        // og dermed må vi sende med denne adressen i metodekallet.

        PreparedStatement ps;                                                //PreparedStatement gir muligheten til
                                                                             // å kunne spesifisere søkeelement(er)

        String query = "SELECT dwelling_unit_id, available, property_owner, dwelling_type, " +
                "size, street, street_no, zip_code, monthly_price, depositum, incl_warmup, incl_warmwater, " +
                "incl_internet, incl_tv, incl_electricity, incl_furniture, " +
                "incl_elec_appliance, allow_housepets, allow_smokers, " +
                "created FROM dwelling_unit WHERE street LIKE ?"; //SQL-query der ? er hvor vi skal sette inn adressen

        Vector rows = new Vector();                                                 //oppretter to Vectorer for rader
        Vector columnnames = new Vector();                                          //og kolonner

        try{
            Class.forName(DRIVER);                                                  //erklærer driverklassen
            con = DriverManager.getConnection(URL, USER, PASSWORD);                 //oppretter connection
            ps = con.prepareStatement(query);                                       //forbereder databasesøk

            ps.setString(1, "%" + address + "%");      //setter posisjon for adressen. siden det er ett sted med ?,
                                                      // får set-verdien parameter 1.
                                                     //hadde det vært en ? etter denne, hadde denne fått parameteren 2.
                                                    //legg merke til % på begge sider. dette gjør at man kan skrive                                                        // inn en hvilken som helst bokstav eller
                                                    //en hvilken som helst rekkefølge av bokstaver, og alle
                                                    // adresser med denne bokstaven eller den
                                                    //rekkefølgen av bokstaver, kommer med i tabellen.

            rs = ps.executeQuery();                                                 //utfører databasesøk
            ResultSetMetaData metadata = rs.getMetaData();                          //metadata blir sendt videre
            int numberofcolumns = metadata.getColumnCount();                        //får kolonnetelling

            for (int column = 0; column < numberofcolumns; column++) {
                columnnames.addElement(metadata.getColumnLabel(column + 1));     //gjennomløper og fyller inn kolonner
            }

            while (rs.next()) {
                Vector newrows = new Vector();                                      //gjennomløper og fyller inn rader
                for (int i = 1; i <= numberofcolumns; i++) {
                    newrows.addElement(rs.getObject(i));
                }
                rows.addElement(newrows);                                           //legger inn i opprinnelig vector
            }
            rs.close();
            ps.close();                                                      //lukker alle åpne database-forbindelser
            con.close();

        }catch(SQLException sqle){
            sqle.printStackTrace();                                                 //catch-blokker
            JOptionPane.showMessageDialog(null, "Error! Feil i SQL!");
        }catch(ClassNotFoundException cnfe){
            cnfe.printStackTrace();
            JOptionPane.showMessageDialog(null, "Finner ikke driverklassen!");
        }
            DefaultTableModel adress = new DefaultTableModel(rows, columnnames);   //oppretter og fyller inn en
                                                                                    // DefaultTableModel
            return adress;                                                          //sender ut
    }

    public DefaultTableModel showOwnerDwellingUnits(String pNo){
        //utfører et søk etter alle boliger registrert på personnummeret som blir sendt med som parameter

        PreparedStatement ps = null;
        String query = "SELECT dwelling_unit_id, available, street, street_no, zip_code " +
                        "FROM dwelling_unit WHERE property_owner LIKE ?";
        Vector rows = new Vector();
        Vector columnnames = new Vector();

        try{
            Class.forName(DRIVER);
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            ps = con.prepareStatement(query);
            ps.setString(1, pNo);

            rs = ps.executeQuery();                                                 //utfører databasesøk
            ResultSetMetaData metadata = rs.getMetaData();                          //metadata blir sendt videre
            int numberofcolumns = metadata.getColumnCount();                        //får kolonnetelling

            for (int column = 0; column < numberofcolumns; column++) {
                columnnames.addElement(metadata.getColumnLabel(column + 1));    //gjennomløper og fyller inn kolonner
            }

            while (rs.next()) {
                Vector newrows = new Vector();                                      //gjennomløper og fyller inn rader
                for (int i = 1; i <= numberofcolumns; i++) {
                    newrows.addElement(rs.getObject(i));
                }
                rows.addElement(newrows);                                           //legger inn i opprinnelig vector
            }
            rs.close();
            ps.close();                                                    //lukker alle åpne database-forbindelser
            con.close();

        }catch(SQLException sqle){
            sqle.printStackTrace();
            JOptionPane.showMessageDialog(null, "Feil i SQL!");           //catcher exceptions og sender ut
        }catch(ClassNotFoundException cnfe){                              //feilmelding
            cnfe.printStackTrace();
            JOptionPane.showMessageDialog(null, "Feil i SQL!");
        }
        DefaultTableModel owner = new DefaultTableModel(rows, columnnames); //oppretter DefaultTableModel
        return owner;                                                       //sender ut
    }
}