/**
 * Created by Sebastian Ramsland on 09.05.2014.
 */

import com.sun.rowset.CachedRowSetImpl;
import java.sql.*;

public class Location {

    // Stedsinformasjon
    private String area;                                // Poststed
    private String township;                            // Kommune
    private String county;                              // Fylke
    private String category;                            // Beskrivelse av adresse

    // Oppretter SQLInterface
    private SQLInterface dbInterface = new SQLInterface();

    // Initialiserer CachedRowSet
    private CachedRowSetImpl cachedRowSet;

    // Oppretter CachedRowSet med fullstendig liste over norske postnummere, poststedsnavn, kommuner, fylker og adressekategorier.
    public Location () {

        // create SQLInterface object
        // execute query
        if (!dbInterface.execQuery(
                "SELECT zip_code, area, township, county, category FROM location")) {

            // exception caught, halt execution
            System.exit(1);
        }

        // create CachedRowSet
        try {
            cachedRowSet = new CachedRowSetImpl();
            cachedRowSet = dbInterface.getCachedRowSet();
        }
        catch (SQLException se) {
            System.out.println("Error code: " + se.getErrorCode() + "\tLocalizedMessage: " + se.getLocalizedMessage());
        }
    }

    // Tar imot et postnummer og laster inn tilhørende stedsinformasjon.
    // Returnerer "false" dersom postnummeret ikke eksisterer.
    public boolean refreshValues(int zip) throws SQLException {
        try {
            // Søker igjennom tabellen
            while(cachedRowSet.next()) {
                if(zip == cachedRowSet.getInt("zip_code")) {

                    // Leser av verdier
                    area = cachedRowSet.getString("area");
                    township = cachedRowSet.getString("township");
                    county = cachedRowSet.getString("county");
                    category = cachedRowSet.getString("category");

                    return true;
                }
            }
            System.out.println("Postnummeret eksisterer ikke. \n");
            return false;
        }
        catch (SQLException e){
            System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
            return false;
        }
    }

    public String getArea() {
        return area;
    }

    public String getTownship() {
        return township;
    }

    public String getCounty() {
        return county;
    }

    public String getCategory() {
        return category;
    }
}